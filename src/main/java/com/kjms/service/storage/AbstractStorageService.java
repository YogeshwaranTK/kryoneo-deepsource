package com.kjms.service.storage;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;

/**
 * Abstract Class for Cloud & Local Storage Service.
 */
public abstract class AbstractStorageService {

    // Upload a file to save in storage place.
    public abstract String upload(@Nullable String folderName,@Nullable MultipartFile multipartFile);

    public abstract void upload(@Nullable String folderName,@Nullable String path,@Nullable ByteArrayOutputStream byteArrayOutputStream);

    // move a file from source to destination place
    public abstract boolean move(@Nullable String folderName,@Nullable String sourcePath,@Nullable String destinationPath);

    // get multipart from storage by path
    public abstract byte[] download(@Nullable String folderName,@Nullable String path);

    // Get size of file
    public abstract long getSize(@Nullable String folderName,@Nullable String path);

    // Get file exists or not
    public abstract boolean exists(@Nullable String folderName,@Nullable String path);
}
