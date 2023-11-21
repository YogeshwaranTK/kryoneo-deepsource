package com.kjms.service.mapper;

import com.kjms.domain.EntityFileFormat;
import com.kjms.service.dto.SubmissionFileFormat;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Mapper class responsible for converting {@link EntityFileFormat} to {@link SubmissionFileFormat}
 */
@Service
public class SubmissionFileFormatMapper {

    private SubmissionFileFormat entityArticleSubmissionFileFormatToArticleSubmissionFileFormat(
        EntityFileFormat entityFileFormat
    ) {
        SubmissionFileFormat submissionFileFormat = new SubmissionFileFormat();

        submissionFileFormat.setId(entityFileFormat.getId());
        submissionFileFormat.setName(entityFileFormat.getName());

        return submissionFileFormat;
    }

    public Page<SubmissionFileFormat> entityArticleSubmissionFileFormatsToArticleSubmissionFileFormats(
        Page<EntityFileFormat> entityArticleSubmissionFileFormats
    ) {
        return entityArticleSubmissionFileFormats.map(this::entityArticleSubmissionFileFormatToArticleSubmissionFileFormat);
    }
}
