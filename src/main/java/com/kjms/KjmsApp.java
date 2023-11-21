package com.kjms;

import com.kjms.config.ApplicationProperties;
import com.kjms.config.CRLFLogConverter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.naming.ConfigurationException;

import com.kjms.config.StorageConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

import static com.kjms.config.ApplicationConstants.*;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class KjmsApp {

    private static final Logger log = LoggerFactory.getLogger(KjmsApp.class);

    private final Environment env;

    public KjmsApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes kjms.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
                activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
            );
        }
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
                activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            log.error(
                "You have misconfigured your application! It should not " + "run with both the 'dev' and 'cloud' profiles at the same time."
            );
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) throws ConfigurationException {
        SpringApplication app = new SpringApplication(KjmsApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        app.setDefaultProperties(getAppDefaultProperties());
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
            CRLFLogConverter.CRLF_SAFE_MARKER,
            "\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}{}\n\t" +
                "External: \t{}://{}:{}{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }

    private static Properties getAppDefaultProperties() throws ConfigurationException {

        Properties properties = new Properties();

        try {

            properties.put("spring.datasource.url", System.getenv(DB_URL));
            properties.put("spring.datasource.username", System.getenv(DB_USERNAME));
            properties.put("spring.datasource.password", System.getenv(DB_PASSWORD));
            properties.put("mail.smtp.host", System.getenv(MAIL_HOST));
            properties.put("mail.smtp.port", System.getenv(MAIL_PORT));
            properties.put("mail.smtp.auth", System.getenv(MAIL_AUTH));
            properties.put("mail.smtp.starttls.enable", System.getenv(MAIL_STARTTLS_ENABLE));
            properties.put("mail.smtp.username", System.getenv(MAIL_USERNAME));
            properties.put("mail.smtp.password", System.getenv(MAIL_PASSWORD));
            properties.put("application.siteURL", System.getenv(SITE_URL));
            properties.put("application.fileSystemPath", System.getenv(LOCAL_PATH));

        } catch (NullPointerException e) {

            e.printStackTrace();
            throw new ConfigurationException("Invalid Application configuration.");
        }

        return properties;
    }
}
