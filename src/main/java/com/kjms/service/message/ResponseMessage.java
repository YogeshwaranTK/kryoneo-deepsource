package com.kjms.service.message;

import com.kjms.config.Constants;
import com.kjms.config.EntityNameConstant;

import java.net.URI;

/**
 * An interface representing a response message that consists of key, message and entity name.
 * Implementing classes should provide methods to retrieve the key and message.
 * Key is used for translate in frontend app & Message is used by default if the key is not present in frontend.
 */
public interface ResponseMessage {

    /**
     * Get the key associated with the response message.
     *
     * @return The key as a string.
     */
    String getKey();

    /**
     * Get the message associated with the response.
     *
     * @return The message as a string.
     */
    String getDefaultMessage();

    /**
     * Get the entity name associated with the response.
     *
     * @return The entity name as a string.
     */
    default String getEntityName() {
        return EntityNameConstant.COMMON;
    }

    /**
     * Get the URI type String associated with the response.
     *
     * @return The URI type String as a string.
     */
    URI getType();

}
