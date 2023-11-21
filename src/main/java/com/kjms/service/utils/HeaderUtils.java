package com.kjms.service.utils;

import com.kjms.service.message.ResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class HeaderUtils {

    @Value("${jhipster.clientApp.name}")
    private static String applicationName;

    public static HttpHeaders create(ResponseMessage responseMessage) {
        HttpHeaders headers = new HttpHeaders();

        final String params = responseMessage.getEntityName() + "." + responseMessage.getKey();

        headers.add("X-" + applicationName + "-alert", responseMessage.getDefaultMessage());

        headers.add("X-" + applicationName + "-params", URLEncoder.encode(params, StandardCharsets.UTF_8));

        return headers;
    }
}
