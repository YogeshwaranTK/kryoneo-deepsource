package com.kjms.web.rest.workflow.common;


import com.kjms.config.ApplicationProperties;
import com.kjms.config.Constants;
import com.kjms.config.StorageConstant;
import com.kjms.service.WorkflowFileManagementService;
import com.kjms.service.dto.FileLibrary;
import com.kjms.web.rest.common.CommonResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Tag(name = "File Management")
@RestController
public class WorkflowFileManagementResource {

   private final WorkflowFileManagementService workflowFileManagementService;
    private final Logger log = LoggerFactory.getLogger(CommonResource.class);

    private final String fileSystemPath;

    public WorkflowFileManagementResource(WorkflowFileManagementService workflowFileManagementService, ApplicationProperties applicationProperties) {
        this.workflowFileManagementService = workflowFileManagementService;
        this.fileSystemPath = applicationProperties.getFileSystemPath();

    }

    @GetMapping(path = StorageConstant.DOWNLOAD_API_V1)
    public ResponseEntity<byte[]> downloadFile(
        @RequestParam(name = "path") String filePath
    ) throws IOException {
        log.debug("REST Request for download file: {}", filePath);
        Path path = Path.of(new File(fileSystemPath + "/" + filePath).getAbsolutePath());
        byte[] file = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLDecoder.decode(path.getFileName().toString(), StandardCharsets.UTF_8)); // Set the file name

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(file);
    }

    @GetMapping("api/v1/file-library")
    public ResponseEntity<FileLibrary> getFileLibrary(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                      @RequestParam(name = "submissionId") Long submissionId){

        return new ResponseEntity<>(workflowFileManagementService.getFileLibrary(journalId,submissionId), HttpStatus.OK);
    }


}
