package com.kjms.service.dto.requests;

import com.kjms.config.Constants;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Represents a request object for update journal basic details.
 */
@Schema(description = "Request Body for Update Journal Publishing Details", title = "Journal Publishing Detail")
public class JournalBasicDetailUpdateRequest {

    @Schema(required = true, description = "This is title of Journal")
    private String title;

    @Schema(description = "Maximum Five Alphabet Characters & it unique", required = true)
    @Pattern(regexp = Constants.JOURNAL_KEY_REGEX, message = "journal.maxFiveAlphaCharacters")
    private String key;

    @Schema(required = true, defaultValue = "This is default description")
    @NotNull
    private String description;

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String onlineIssn;

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String printIssn;

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String editorChief;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOnlineIssn() {
        return onlineIssn;
    }

    public void setOnlineIssn(String onlineIssn) {
        this.onlineIssn = onlineIssn;
    }

    public String getPrintIssn() {
        return printIssn;
    }

    public void setPrintIssn(String printIssn) {
        this.printIssn = printIssn;
    }

    public String getEditorChief() {
        return editorChief;
    }

    public void setEditorChief(String editorChief) {
        this.editorChief = editorChief;
    }
}
