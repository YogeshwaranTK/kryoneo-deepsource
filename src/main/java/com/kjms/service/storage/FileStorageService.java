package com.kjms.service.storage;

import javax.transaction.Transactional;

import com.kjms.config.ApplicationProperties;
import com.kjms.config.StorageConstant;
import com.kjms.service.SubmissionService;
import com.kjms.service.utils.FileUtils;
import com.microsoft.azure.storage.StorageException;
import org.apache.commons.compress.utils.FileNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.kjms.service.SubmissionService.*;

/**
 * Service class for saving file
 */
@Service
@Transactional
public class FileStorageService {
    private final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    private final AzureCloudStorageService azureCloudStorageService;
    private final LocalStorageService localStorageService;
    private final String host;

    public FileStorageService(AzureCloudStorageService azureCloudStorageService,
                              LocalStorageService localStorageService,
                              ApplicationProperties applicationProperties) {
        this.azureCloudStorageService = azureCloudStorageService;
        this.localStorageService = localStorageService;
        this.host = applicationProperties.getSiteURL();
    }

//    public String createDownloadLink(String containerName, String filePath, String fileName) {
//
//        String downloadUrl = host + StorageConstant.DOWNLOAD_API_V1;
//
//        downloadUrl += "?" + StorageConstant.CONTAINER_PARAM_NAME + "=" + containerName;
//
//        downloadUrl += "&" + StorageConstant.FILE_PATH_PARAM_NAME + "=" + filePath;
//
//        downloadUrl += "&" + StorageConstant.FILE_NAME_PARAM_NAME + "=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
//
//        return downloadUrl;
//    }


//    public String createZipFileDownloadLink(String containerName, List<Map<String, String>> filePaths) {
//        String zipFileName = UUID.randomUUID() + ".zip";
//
//        String zipPath = StorageConstant.TEMP_FOLDER + zipFileName;
//
//        List<Map<String, String>> fileNameWithoutDuplicate = new ArrayList<>();
//        Map<String, Integer> nameCountMap = new HashMap<>();
//
////         Change the file name for remove duplicate names for create zip
//        for (Map<String, String> map : filePaths) {
//
//            String fileName = map.get(SubmissionService.FILE_NAME);
//
//            int count = nameCountMap.getOrDefault(fileName, 0);
//            nameCountMap.put(fileName, count + 1);
//
//            if (count > 0) {
//                map.put(SubmissionService.FILE_NAME, FileNameUtils.getBaseName(fileName) + (new Random().nextInt(900) + 100)
//                    + "." + FileNameUtils.getExtension(fileName));
//            }
//            fileNameWithoutDuplicate.add(map);
//        }
//
//        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//             ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
//
//            for (Map<String, String> file : fileNameWithoutDuplicate) {
//                String fileName = file.get(FILE_NAME);
//                String filePath = file.get(FILE_PATH);
//
//                byte[] byteArray = download(containerName, filePath, storageType);
//
//                if (byteArray != null) {
//                    ZipEntry zipEntry = new ZipEntry(fileName);
//                    zipOut.putNextEntry(zipEntry);
//                    zipOut.write(byteArray);
//                    zipOut.closeEntry();
//                }
//            }
//
//            zipOut.close();
//
//            if (storageType.equals(StorageConstant.LOCAL_STORAGE_TYPE)) {
//                localStorageService.upload(containerName, zipPath, byteArrayOutputStream);
//            } else if (storageType.equals(StorageConstant.AZURE_STORAGE_TYPE)) {
//                azureCloudStorageService.upload(containerName, zipPath, byteArrayOutputStream);
//            }
//
//        } catch (IOException e) {
//            return "";
//        }
//
//        try {
//            Files.createDirectories(Path.of(zipPath).getParent());
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            return null;
//        }
//
//        return createDownloadLink(containerName, zipPath, zipFileName);
//    }

//    public String getFilePathFromDownloadLink(String downloadLink, String containerName) {
//        try {
//            URL url = new URL(downloadLink);
//
//            Map<String, String> parameters = getQueryParameters(url);
//
//            String urlContainerName = parameters.get(StorageConstant.CONTAINER_PARAM_NAME);
//
//            String filePath = parameters.get(StorageConstant.FILE_PATH_PARAM_NAME);
//
//            if (!containerName.equals(urlContainerName) || filePath == null) {
//                log.error("Invalid Download url: {}", downloadLink);
//                return null;
//            }
//
//            boolean exists;
//
//            if (storageType.equals(StorageConstant.LOCAL_STORAGE_TYPE)) {
//                exists = localStorageService.exists(containerName, filePath);
//            } else if (storageType.equals(StorageConstant.AZURE_STORAGE_TYPE)) {
//                exists = azureCloudStorageService.exists(containerName, filePath);
//            } else {
//                log.error("Storage Type Not Found");
//                return null;
//            }
//
//            if (!exists) {
//                log.error("File Not Found in path: {}", filePath);
//                return null;
//            }
//
//            return StringUtils.cleanPath(filePath);
//
//        } catch (MalformedURLException e) {
//            log.error("Azure Storage Account Not Found");
//            return null;
//        }
//    }

//    public String upload(String containerName, MultipartFile multipartFile, String fileName) throws URISyntaxException, IOException, InvalidKeyException, StorageException {
//
//        String filePath;
//
//        if (storageType.equals(StorageConstant.LOCAL_STORAGE_TYPE)) {
//            filePath = localStorageService.upload(containerName, multipartFile);
//        } else if (storageType.equals(StorageConstant.AZURE_STORAGE_TYPE)) {
//            filePath = azureCloudStorageService.upload(containerName, multipartFile);
//        } else {
//            log.error("Storage Type Not Found");
//
//            return null;
//        }
//
//        return createDownloadLink(containerName, filePath, fileName);
//    }

    // move file from a source path to a destination path in container.
    // if moved successfully, return true else return false.
//    public boolean move(String containerName, String sourcePath, String destinationPath) {
//        if (storageType.equals(StorageConstant.LOCAL_STORAGE_TYPE)) {
//            return localStorageService.move(containerName, sourcePath, destinationPath);
//        } else if (storageType.equals(StorageConstant.AZURE_STORAGE_TYPE)) {
//            return azureCloudStorageService.move(containerName, sourcePath, destinationPath);
//        } else {
//            log.error("Storage Type Not Found");
//
//            return false;
//        }
//    }

//    public byte[] download(String containerName, String filePath) {
//        if (storageType.equals(StorageConstant.LOCAL_STORAGE_TYPE)) {
//            return localStorageService.download(containerName, filePath);
//        } else if (storageType.equals(StorageConstant.AZURE_STORAGE_TYPE)) {
//            return azureCloudStorageService.download(containerName, filePath);
//        } else {
//            log.error("Storage Type Not Found");
//            return null;
//        }
//    }

//    private static Map<String, String> getQueryParameters(URL url) {
//        Map<String, String> queryParameters = new HashMap<>();
//
//        String query = url.getQuery();
//        if (query != null) {
//            String[] pairs = query.split("&");
//            for (String pair : pairs) {
//                String[] keyValue = pair.split("=");
//                if (keyValue.length == 2) {
//                    String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
//                    String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
//                    queryParameters.put(key, value);
//                }
//            }
//        }
//        return queryParameters;
//    }

//    private byte[] download(String containerName, String filePath, String storageType) {
//        if (storageType.equals(StorageConstant.LOCAL_STORAGE_TYPE)) {
//            return localStorageService.download(containerName, filePath);
//        } else if (storageType.equals(StorageConstant.AZURE_STORAGE_TYPE)) {
//            return azureCloudStorageService.download(containerName, filePath);
//        } else {
//            log.error("Storage Type Not Found");
//            return null;
//        }
//    }


}



