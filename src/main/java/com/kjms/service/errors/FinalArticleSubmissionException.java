package com.kjms.service.errors;

import com.kjms.web.rest.errors.ErrorConstants;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.Map;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class FinalArticleSubmissionException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;
    private static final String ERROR_TITLE = "Final Article Submission Error";
    private final Map<String, Object> parameters;
    private final String entityName;
    private final String errorKey;

    public FinalArticleSubmissionException(@Nullable Map<String, Object> parameters, @Nullable String entityName, @Nullable String errorKey) {
        super(ErrorConstants.FINAL_ARTICLE_SUBMISSION_TYPE, ERROR_TITLE, Status.BAD_REQUEST, null, null, null, parameters);
        this.parameters = parameters;
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

}
