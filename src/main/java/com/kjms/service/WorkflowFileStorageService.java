package com.kjms.service;

import com.kjms.config.ApplicationProperties;
import com.kjms.config.StorageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class WorkflowFileStorageService {

    private final Logger log = LoggerFactory.getLogger(WorkflowFileStorageService.class);
    private final String fileSystemPath;
    private final String host;


    public WorkflowFileStorageService(ApplicationProperties applicationProperties) {
        this.fileSystemPath = applicationProperties.getFileSystemPath();
        this.host = applicationProperties.getSiteURL();
    }

    public String uploadToFinalDirectory(String filePath, MultipartFile multipartFile) {

        String systemPath = fileSystemPath;
        if (!systemPath.endsWith("/")) {
            systemPath = fileSystemPath + "/";
        }
        String storagePath = systemPath + "/";

        File targetStorageSpace = new File(storagePath);

        if (!targetStorageSpace.exists()) {
            log.error("Storage Space Not Found");
            return null;
        }

        String fullPath = storagePath + filePath;

        Path path = Path.of(fullPath);

        try {
            Files.createDirectories(path.getParent());

            multipartFile.transferTo(new File(fullPath));

            log.debug("File Saved in Target Directory: {}", "");

            return filePath;
        } catch (Exception e) {
            log.error("File cannot be saved in Target path : {}", "");

            return null;
        }

    }

    public String createDownloadLink(String filePath) {

        return host.substring(0, host.length() - 1) + StorageConstant.DOWNLOAD_API_V1 + "=?path=" + filePath;
    }

    public String moveFile(String sourceFilePath, String targetDirectory) {

        String systemPath = fileSystemPath;

        if (!systemPath.endsWith("/")) {
            systemPath = fileSystemPath + "/";
        }
        String storagePath = systemPath + "/";

        File targetStorageSpace = new File(storagePath);

        if (!targetStorageSpace.exists()) {
            log.error("Storage Space Not Found");
            return null;
        }

        String sourceFullPath = storagePath + sourceFilePath;

        String targetFullPath = storagePath + targetDirectory;

        Path sourcePath = Paths.get(sourceFullPath);

        Path targetPath = Paths.get(targetFullPath);

        try {

            Files.createDirectories(targetPath.getParent());

            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return targetDirectory;
    }

    public File createZipFile(String fileNamePrefix, List<String> filePaths) throws IOException {

        File directory = new File(fileSystemPath + "/zip-temp/" + Instant.now().toEpochMilli() + "/" + fileNamePrefix);

        File zipFileName = new File(directory + ".zip");

        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + directory.getAbsolutePath());
        }

        if (!zipFileName.createNewFile()) {
            throw new IOException("Failed to create zip file: " + zipFileName.getAbsolutePath());
        }

        try (ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(zipFileName))) {
            filePaths.forEach(path -> {
                try {
                    addToZipFile(Paths.get(fileSystemPath, path), zipStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        return zipFileName;
    }
//
//    private void addToZipFile(Path file, ZipOutputStream zipStream) {
//        String inputFileName = file.toFile().getPath();
//        try (FileInputStream inputStream = new FileInputStream(inputFileName)) {
//
//            // create a new ZipEntry, which is basically another file
//            // within the archive. We omit the path from the filename
//            ZipEntry entry = new ZipEntry(file.toFile().getName());
//            entry.setCreationTime(FileTime.fromMillis(file.toFile().lastModified()));
//            entry.setComment("Created by TheCodersCorner");
//            zipStream.putNextEntry(entry);
//
//            // Now we copy the existing file into the zip archive. To do
//            // this we write into the zip stream, the call to putNextEntry
//            // above prepared the stream, we now write the bytes for this
//            // entry. For another source such as an in memory array, you'd
//            // just change where you read the information from.
//            byte[] readBuffer = new byte[2048];
//            int amountRead;
//            int written = 0;
//
//            while ((amountRead = inputStream.read(readBuffer)) > 0) {
//                zipStream.write(readBuffer, 0, amountRead);
//                written += amountRead;
//            }
//        } catch (IOException e) {
//            throw new ZipParsingException("Unable to process " + inputFileName, e);
//        }
//    }

    private void addToZipFile(Path filePath, ZipOutputStream zipStream) throws IOException {
        try (InputStream fileInputStream = new FileInputStream(filePath.toFile())) {
            ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
            zipStream.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fileInputStream.read(bytes)) >= 0) {
                zipStream.write(bytes, 0, length);
            }

            zipStream.closeEntry();
        }
    }
}
