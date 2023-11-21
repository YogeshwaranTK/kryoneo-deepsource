package com.kjms.service.storage;

import com.kjms.config.ApplicationProperties;
import com.kjms.config.StorageConstant;

import org.apache.commons.compress.utils.FileNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalStorageService extends AbstractStorageService {

    private final Logger log = LoggerFactory.getLogger(LocalStorageService.class);

    private final String fileSystemPath;

    public LocalStorageService(ApplicationProperties applicationProperties) {
        this.fileSystemPath = applicationProperties.getFileSystemPath();
    }

    public String upload(String folderName, MultipartFile multipartFile) {
        String storagePath = fileSystemPath + "/";

        File targetStorageSpace = new File(storagePath);

        if (!targetStorageSpace.exists()) {
            log.error("Storage Space Not Found");
            return null;
        }

        String newFileName = UUID.randomUUID() + "." + FileNameUtils.getExtension(multipartFile.getOriginalFilename());

        String filePath = storagePath + folderName + "/" + StorageConstant.TEMP_FOLDER + newFileName;

        Path path = Path.of(filePath);

        try {
            Files.createDirectories(path.getParent());

            multipartFile.transferTo(new File(filePath));

            log.debug("File Saved in Target Directory: {}", "");

            return StorageConstant.TEMP_FOLDER + newFileName;
        } catch (Exception e) {
            log.error("File cannot be saved in Target path : {}", "");

            return null;
        }
    }

    public boolean move(String folderName, String sourcePath, String destinationPath) {

        String storagePath = fileSystemPath + "/";

        try {
            File container = new File(storagePath, folderName);

            if (!container.exists()) {
                log.error("No Storage Space Found for {}", storagePath);

                return false;
            }

            Path source = Path.of(new File(container, sourcePath).getAbsolutePath());

            Path target = Path.of(new File(container, destinationPath).getAbsolutePath());

            Files.createDirectories(target.getParent());

            // Move the file to the target location
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);

            log.debug("File moved to source path: {}", target);

            return true;

        } catch (IOException e) {
            log.error("File Cannot Moved from {} to {}.", sourcePath, destinationPath);

            return false;
        }
    }

    public byte[] download(String folderName, String filePath) {
        try {

            String storagePath = fileSystemPath + "/";

            File container = new File(storagePath, folderName);

            if (!container.exists()) {
                log.error("No Storage Space Found for {}", storagePath);
            }

            Path path = Path.of(new File(container, filePath).getAbsolutePath());

            return Files.readAllBytes(path);

        } catch (IOException e) {
            log.error("File Cannot be Download : {}", filePath);

            return null;
        }
    }

    public long getSize(String folderName, String filePath) {

        String storagePath = fileSystemPath + "/";

        File container = new File(storagePath, folderName);

        if (!container.exists()) {
            log.error("No Storage Space Found for {}", storagePath);
        }

        return new File(container, filePath).getAbsoluteFile().length();

    }

    public boolean exists(String folderName, String filePath) {
        String storagePath = fileSystemPath + "/";

        File container = new File(storagePath, folderName);

        if (!container.exists()) {
            log.error("No Storage Space Found for {}", storagePath);

            return false;
        }

        return new File(container, filePath).exists();
    }

    public void upload(String folderName, String filePath, ByteArrayOutputStream byteArrayOutputStream) {

        String storagePath = fileSystemPath + "/";

        File container = new File(storagePath, folderName);

        if (!container.exists()) {
            log.error("No Storage Space Found for {}", storagePath);
            return;
        }

        byte[] buffer = new byte[4096];

        int bytesRead;

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())) {
            while ((bytesRead = byteArrayInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            try (FileOutputStream fos = new FileOutputStream(new File(container.getAbsolutePath() + "/" + filePath).getAbsolutePath())) {
                fos.write(byteArrayOutputStream.toByteArray());

                log.debug("Zip File saved in path: {}", filePath);
            }
        } catch (IOException e) {
            log.error("Zip File saved failure in path: {}", filePath);
            log.error(String.valueOf(e));
        }
    }
}
