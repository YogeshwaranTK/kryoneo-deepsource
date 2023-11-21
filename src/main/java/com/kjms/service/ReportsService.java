package com.kjms.service;

import com.kjms.repository.EntityJournalRepository;
import com.kjms.repository.EntitySubmissionRepository;
import com.kjms.service.dto.ArticlesReport;
import com.kjms.service.dto.JournalsReport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportsService {
    private final EntitySubmissionRepository entitySubmissionRepository;

    public ReportsService(EntitySubmissionRepository entitySubmissionRepository) {
        this.entitySubmissionRepository = entitySubmissionRepository;
    }

    public List<JournalsReport> getJournalsReport() {

        return entitySubmissionRepository.findJournalSubmissionsCount();

    }

    public List<ArticlesReport> getArticlesReport(Long journalId) {
        return null;
    }
}
