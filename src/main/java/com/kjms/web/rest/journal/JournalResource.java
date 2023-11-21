package com.kjms.web.rest.journal;

import com.kjms.config.Constants;
import com.kjms.security.ExportConstants;
import com.kjms.service.JournalService;
import com.kjms.service.dto.*;
import com.kjms.service.dto.requests.JournalBasicDetailUpdateRequest;
import com.kjms.service.dto.requests.JournalCreateRequest;
import com.kjms.service.dto.requests.JournalGuidelineUpdateRequest;
import com.kjms.service.dto.requests.JournalMetadataUpdateRequest;
import com.kjms.web.rest.errors.WrongSortKeyException;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing the Journal.
 */
@Tag(name = "Journal")
@RestController
@RequestMapping("/api/v1")
public class JournalResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = List.of("id", "title", "key", "createdAt", "accessType");
    private static final List<String> ALLOWED_ORDERED_LANG_PROPERTIES = List.of("id", "langId.name", "langId.langKey");
    private static final List<String> ALLOWED_ORDERED_FORMAT_PROPERTIES = List.of("id");

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return !pageable.getSort().stream()
            .map(Sort.Order::getProperty)
            .allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    private boolean onlyContainsAllowedLangProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_LANG_PROPERTIES::contains);
    }

    private boolean onlyContainsAllowedFormatProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_FORMAT_PROPERTIES::contains);
    }

    private final JournalService journalService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(JournalResource.class);

    public JournalResource(JournalService journalService) {
        this.journalService = journalService;
    }

    @PostMapping(value = "/journal/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Long>> createJournal(@RequestPart(name = "title") String title,
                                                           @RequestPart(name = "key") String key,
                                                           @RequestPart(name = "description", required = false) String description,
                                                           @RequestPart(name = "onlineIssn") String onlineIssn,
                                                           @RequestPart(name = "printIssn") String printIssn,
                                                           @RequestPart(name = "editorChief") String editorChief,
                                                           @RequestPart(name = "summary") String summary,
                                                           @RequestPart(name = "submissionLanguages") String submissionLanguages,
                                                           @RequestPart(name = "guidelines") String guidelines,
                                                           @RequestPart(name = "categories", required = false) String categories,
                                                           @RequestPart(name = "fileTypes") String fileTypes,
                                                           MultipartHttpServletRequest httpServletRequest) {


        JournalCreateRequest journalCreateRequest = new JournalCreateRequest();
        journalCreateRequest.setTitle(title);
        journalCreateRequest.setKey(key);
        journalCreateRequest.setDescription(description);
        journalCreateRequest.setOnlineIssn(onlineIssn);
        journalCreateRequest.setPrintIssn(printIssn);
        journalCreateRequest.setEditorChief(editorChief);
        journalCreateRequest.setSummary(summary);
        journalCreateRequest.setArticleSubmissionLanguages(Arrays.stream(submissionLanguages.split(",")).map(String::trim).collect(Collectors.toList()));
        journalCreateRequest.setGuidelines(guidelines);
        journalCreateRequest.setCategories(Arrays.stream(categories.split(",")).map(String::trim).collect(Collectors.toList()));
        journalCreateRequest.setFileTypes(Arrays.stream(fileTypes.split(",")).map(String::trim).map(Long::parseLong).collect(Collectors.toList()));

        log.debug("REST request to save Journal: {}", journalCreateRequest);

        Long journalId = journalService.createJournal(journalCreateRequest, httpServletRequest.getFileMap());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "journal.created", journalCreateRequest.getTitle()))
            .body(Map.of(Constants.JOURNAL_ID, journalId));
    }

    /**
     * {@code PUT /api/v1/journal/create } : update journal.
     *
     * @param journalBasicDetailUpdateRequest the journal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}.
     */
    @PutMapping("/journal/settings/basic-detail-update")
    public ResponseEntity<Journal> updateJournal(
        @RequestHeader(name = Constants.JOURNAL_ID, required = false) Long journalId,
        @Valid @RequestBody JournalBasicDetailUpdateRequest journalBasicDetailUpdateRequest) {

        log.debug("REST request to update Journal: {}", journalBasicDetailUpdateRequest);

        return new ResponseEntity<>(journalService.updateJournal(journalId, journalBasicDetailUpdateRequest), HttpStatus.OK);
    }

    /**
     * {@code GET api/v1/journal} : get the journal.
     *
     * @param journalId the journal to get.
     * @return the {@link ResponseEntity} with status (OK) and {@link Journal} in body
     */
    @GetMapping("/journal")
//    @PreAuthorize("hasEndPointAccess(\"" + RouteId.GET_JOURNAL_DETAIL + "\")")
    public ResponseEntity<Journal> getJournal(@RequestParam(value = "id") Long journalId) {
        log.debug("REST request to get Journal: {}", journalId);

        return ResponseEntity.ok().body(journalService.getJournalById(journalId));
    }

    /**
     * {@code GET api/v1/journal/list} : get all journals with all the details.
     *
     * @param searchText searchText
     * @param pageable   for pagination
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with all journals.
     * @throws WrongSortKeyException {@code 400 (Bad Request)} if the sort key is wrong.
     */
    @GetMapping(path = "/journals")
//    @PreAuthorize("hasEndPointAccess(\"" + RouteId.GET_JOURNAL_LIST + "\")")
    public ResponseEntity<List<Journal>> getJournals(@RequestParam(name = Constants.SEARCH_TEXT, required = false) String searchText, @ParameterObject Pageable pageable) {
        log.debug("REST request to get all journals");

        if (onlyContainsAllowedProperties(pageable)) {
            throw new WrongSortKeyException();
        }

        final Page<Journal> journalList = journalService.getJournals(searchText, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), journalList);

        return new ResponseEntity<>(journalList.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE api/v1/journal} : delete the journal.
     *
     * @param journalId      the journal to delete.
     * @param deletedRemarks the remarks for delete.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}.
     */
    @DeleteMapping("/journal")
    public ResponseEntity<Void> deleteJournal(@RequestParam(name = "id") Long journalId, @RequestParam(name = Constants.DELETED_REMARKS) String deletedRemarks, @RequestParam(name = "key") String key) {
        log.debug("REST request to delete Journal: {}", journalId.toString());

        String title = journalService.deleteJournal(journalId, key, deletedRemarks);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "journal.deleted", title)).build();
    }

    /**
     * {@code GET api/v1/journal/export-excel} : Get all Journals in Excel.
     *
     * @return excel it contains journal details with status {@code 200 OK}
     * @throws IOException when file not able to save
     */
    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException, JRException, ParseException {
        log.debug("REST request to get all Journals in Excel.");

        JasperPrint jasperPrint = journalService.export(ExportConstants.EXCEL);

        // Export the Jasper report to Excel format
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.exportReport();

        // Set the response headers and content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("journal.xlsx").build());

        // Return the Excel data as a byte array in the response entity
        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET api/v1/journal/export-pdf} : Get all Journals in pdf
     *
     * @return pdf it contains journal details
     * @throws IOException when file not able to save
     */
    @GetMapping("/export-pdf")
    public ResponseEntity<byte[]> exportToPdf() throws JRException, IOException, ParseException {
        log.debug("REST request to get all Journals in PDF.");

        JasperPrint jasperPrint = journalService.export(ExportConstants.PDF);

        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // Set the response headers and content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("journal.pdf").build());

        // Return the Excel data as a byte array in the response entity
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    /**
     * {@code PUT api/v1/journal/metadata-update} : Updates Metadata of a journal.
     *
     * @param journalMetadataUpdateRequest the metadata to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     */
    @PutMapping(path = "/journal/settings/metadata-update")
    public ResponseEntity<Journal> updateMetadata(@RequestHeader(name = Constants.JOURNAL_ID,
        required = false) Long journalId, @Valid @RequestBody JournalMetadataUpdateRequest journalMetadataUpdateRequest) {

        log.debug("REST request to update metadata: {}", journalMetadataUpdateRequest);

        Journal journal = journalService.updateMetadata(journalId, journalMetadataUpdateRequest);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "journal.metadataUpdated", "New File"))
            .body(journal);
    }


    /**
     * {@code PUT api/v1/journal/guidelines-update} : Updates Guidelines of a journal.
     *
     * @param journalGuidelineUpdateRequest the guidelines to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     */
    @PutMapping(path = "/journal/settings/guidelines-update")
    public ResponseEntity<Journal> updateGuideLines(@RequestHeader(name = Constants.JOURNAL_ID,
        required = false) Long journalId,
                                                    @Valid @RequestBody JournalGuidelineUpdateRequest journalGuidelineUpdateRequest) {

        log.debug("REST request to update Guidelines: {}", journalGuidelineUpdateRequest);

        Journal journal = journalService.updateGuideLine(journalId, journalGuidelineUpdateRequest);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "journal.guidelinesUpdated", "New File"))
            .body(journal);
    }

    /**
     * {@code GET api/v1/journal/lang/list} : get all journal languages.
     *
     * @param searchText searchText
     * @param pageable   for pagination
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with all journals.
     * @throws WrongSortKeyException {@code 400 (Bad Request)} if the sort key is wrong.
     */
    @GetMapping(path = "/journal/submission-languages")
    public ResponseEntity<List<JournalLanguage>> getJournalLangList(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId, @RequestParam(name = Constants.SEARCH_TEXT, required = false) String searchText, @ParameterObject Pageable pageable) {
        log.debug("REST request to get all journal languages");

        if (!onlyContainsAllowedLangProperties(pageable)) {
            throw new WrongSortKeyException();
        }

        final Page<JournalLanguage> journalLanguages = journalService.getJournalLangList(journalId, searchText, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), journalLanguages);

        return new ResponseEntity<>(journalLanguages.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping(path = "/journal/file-types")
    public ResponseEntity<List<JournalFileType>> getJournalFileTypes(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId) {

        log.debug("REST request to get all journal Submission File Types");

        return new ResponseEntity<>(journalService.getJournalFileTypes(journalId), HttpStatus.OK);
    }
}
