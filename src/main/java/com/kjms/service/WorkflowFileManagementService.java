package com.kjms.service;

import com.kjms.repository.EntitySubmissionDiscussionFileRepository;
import com.kjms.repository.EntitySubmissionFileRepository;
import com.kjms.service.dto.FileLibrary;
import com.kjms.service.dto.SubmissionWorkFlowFile;
import com.kjms.service.mapper.SubmissionMapper;
import org.springframework.stereotype.Service;

@Service
public class WorkflowFileManagementService {
    private final EntitySubmissionFileRepository entitySubmissionFileRepository;

    private final SubmissionMapper submissionMapper;
    private final EntitySubmissionDiscussionFileRepository entitySubmissionDiscussionFileRepository;

    public WorkflowFileManagementService(EntitySubmissionFileRepository entitySubmissionFileRepository, SubmissionMapper submissionMapper,
                                         EntitySubmissionDiscussionFileRepository entitySubmissionDiscussionFileRepository) {
        this.entitySubmissionFileRepository = entitySubmissionFileRepository;
        this.submissionMapper = submissionMapper;
        this.entitySubmissionDiscussionFileRepository = entitySubmissionDiscussionFileRepository;
    }


    public FileLibrary getFileLibrary(Long journalId, Long submissionId) {

        FileLibrary fileLibrary = new FileLibrary();

        fileLibrary.setSubmission(getSubmissionWorkFlowFile(journalId, submissionId));

        return fileLibrary;
    }

    private SubmissionWorkFlowFile getSubmissionWorkFlowFile(Long journalId, Long submissionId) {

        SubmissionWorkFlowFile submissionWorkFlowFile = new SubmissionWorkFlowFile();

        submissionWorkFlowFile.setSubmissionFiles(submissionMapper.entitySubmissionFilesToSubmissionFiles(entitySubmissionFileRepository.getSubmissionFiles(journalId, submissionId)));

        return submissionWorkFlowFile;
    }
}
