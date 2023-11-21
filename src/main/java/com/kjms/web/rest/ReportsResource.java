package com.kjms.web.rest;

import com.kjms.service.ReportsService;
import com.kjms.service.dto.ArticlesReport;
import com.kjms.service.dto.JournalsReport;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Reports")
@RestController
@RequestMapping("/api/v1/report")
public class ReportsResource {

    private final ReportsService reportsService;

    public ReportsResource(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/journals")
    public ResponseEntity<List<JournalsReport>> getJournalsReport() {

        return ResponseEntity.ok().body(reportsService.getJournalsReport());
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticlesReport>> getArticlesReport(
        @RequestParam Long journalId
    ) {

        return ResponseEntity.ok().body(reportsService.getArticlesReport(journalId));
    }

}
