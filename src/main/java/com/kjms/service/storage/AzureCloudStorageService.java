package com.kjms.service.storage;

import com.kjms.config.ApplicationProperties;
import com.kjms.config.StorageConstant;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.apache.commons.compress.utils.FileNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URISyntaxException;

import java.security.InvalidKeyException;
import java.util.*;

/**
 * Service class for save, get download link of file.
 */
@Service
public class AzureCloudStorageService extends AbstractStorageService {
    private final Logger log = LoggerFactory.getLogger(AzureCloudStorageService.class);

    private final String connectionString;

    protected AzureCloudStorageService(ApplicationProperties applicationProperties) {
        this.connectionString = applicationProperties.getAzureConnectionString();
    }

    public String upload(String containerName, MultipartFile file) {
        if (file == null) {
            return null;
        }

        String filePath = StorageConstant.TEMP_FOLDER;

        filePath += UUID.randomUUID() + "." + FileNameUtils.getExtension(file.getOriginalFilename());

        try {
            CloudBlob blob = getAzureCloudBlob(containerName, filePath);

            blob.upload(file.getInputStream(), file.getSize());

            log.debug("File Upload to Azure Container: {}", filePath);

            return filePath;

        } catch (StorageException | IOException e) {

            log.error("File Cannot Save to Azure Storage Account {}/{}", containerName, filePath);

            return null;
        }
    }

    public boolean move(String containerName, String sourceFilePath, String destinationFilePath) {
        try {
            // Get a reference to the source blob
            CloudBlob sourceBlob = getAzureCloudBlob(containerName, sourceFilePath);

            if (!sourceBlob.exists()) {
                return false;
            }

            // Get a reference to the destination blob
            CloudBlob destinationBlob = getAzureCloudBlob(containerName, destinationFilePath);

            // Start the copy operation
            destinationBlob.startCopy(sourceBlob.getUri());

            // delete the temporary file
            sourceBlob.delete();

            log.debug("Azure File Copy to folder: {}", destinationFilePath);

            return true;

        } catch (StorageException e) {
            log.error("Azure {} File can't Copy to folder: {}", sourceFilePath, destinationFilePath);

            return false;
        }
    }

    public CloudBlob getAzureCloudBlob(String containerName, String filePath) {
        try {
            // Parse the connection string and create a CloudStorageAccount object
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);

            // Create a CloudBlobClient object to interact with the Blob service
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            CloudBlobContainer cloudBlobContainer = blobClient.getContainerReference(containerName.toLowerCase());

            if (!cloudBlobContainer.exists()) {
                cloudBlobContainer.create();
            }

            // return a reference to the blob
            return cloudBlobContainer.getBlockBlobReference(filePath);
        } catch (URISyntaxException | StorageException | InvalidKeyException e) {
            return null;
        }
    }

    public byte[] download(String containerName, String filePath) {

        try {
            CloudBlob blob = getAzureCloudBlob(containerName, filePath);

            return inputStreamToByteArray(blob.openInputStream());
        } catch (StorageException | IOException e) {
            return null;
        }
    }

    public long getSize(String containerName, String filePath) {
        CloudBlob blob = getAzureCloudBlob(containerName, filePath);

        return blob.getProperties().getLength();
    }

    private static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

    public boolean exists(String containerName, String filePath) {
        try {
            CloudBlob blob = getAzureCloudBlob(containerName, filePath);

            return blob.exists();
        } catch (StorageException e) {
            log.error(String.valueOf(e));
            return false;
        }
    }

    public void upload(String containerName, String filePath, ByteArrayOutputStream byteArrayOutputStream) {
        try {
            CloudBlob blob = getAzureCloudBlob(containerName, filePath);

            blob.upload(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), byteArrayOutputStream.size());
        } catch (IOException | StorageException e) {
            log.error("Zip Cannot be save in Azure :{}", filePath);
        }
    }

    public void upload(String containerName, String filePath, ByteArrayInputStream byteArrayInputStream, ByteArrayOutputStream byteArrayOutputStream) {
        try {
            CloudBlob blob = getAzureCloudBlob(containerName, filePath);

            blob.upload(byteArrayInputStream, byteArrayOutputStream.toByteArray().length);
        } catch (IOException | StorageException e) {
            log.error("Zip Cannot be save in Azure :{}", filePath);
        }
    }
}
