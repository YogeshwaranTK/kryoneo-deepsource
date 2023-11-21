package com.kjms.web.rest.common;


import com.kjms.config.StorageConstant;
import com.kjms.service.storage.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Tag(name = "Download")
@RestController
public class DownloadResource {

    private final Logger log = LoggerFactory.getLogger(CommonResource.class);

    private final FileStorageService fileStorageService;


    public DownloadResource(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

////    @GetMapping(path = StorageConstant.DOWNLOAD_API_V1)
//    public ResponseEntity<byte[]> downloadFile(
//        @RequestParam(name = StorageConstant.CONTAINER_PARAM_NAME) String containerName,
//        @RequestParam(name = StorageConstant.FILE_PATH_PARAM_NAME) String filePath,
//        @RequestParam(name = StorageConstant.FILE_NAME_PARAM_NAME) String fileName
//    ) {
//        log.debug("REST Request for download file: {}/{}", containerName, filePath);
//
//        byte[] file = fileStorageService.download(containerName, filePath);
//
//        if (file != null) {
//            HttpHeaders headers = new HttpHeaders();
//
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLDecoder.decode(fileName, StandardCharsets.UTF_8)); // Set the file name
//
//            return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(file);
//        } else {
//            return ResponseEntity.ok().build();
//        }
//    }
}
