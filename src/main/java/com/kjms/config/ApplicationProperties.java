package com.kjms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Kjms.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private String siteURL;
    private String storageType;
    private String fileSystemPath;
    private String azureConnectionString;

    public String getAzureConnectionString() {
        return azureConnectionString;
    }

    public void setAzureConnectionString(String azureConnectionString) {
        this.azureConnectionString = azureConnectionString;
    }

    public String getFileSystemPath() {
        return fileSystemPath;
    }

    public void setFileSystemPath(String fileSystemPath) {
        this.fileSystemPath = fileSystemPath;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getSiteURL() {
        return siteURL;
    }

    public void setSiteURL(String siteURL) {
        this.siteURL = siteURL;
    }
}
