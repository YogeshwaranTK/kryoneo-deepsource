package com.kjms.config;

public final class StorageConstant {
    private StorageConstant() {
    }

    public final static String AZURE_STORAGE_TYPE = "azure";
    public final static String LOCAL_STORAGE_TYPE = "local";
    public final static String FILE_PATH_PARAM_NAME = "f";
    public final static String CONTAINER_PARAM_NAME = "c";
    public final static String FILE_NAME_PARAM_NAME = "n";
    public final static String DOWNLOAD_API_V1 = "/api/v1/download";
    public final static String ORIGINAL_FILE_PREFIX_NAME = "Org_";
    public final static String SUBMISSION_REVISION_FILE_PREFIX_NAME = "Sub_Rev_{0}_";
    public final static String PEER_REVISION_FILE_PREFIX_NAME = "Peer_Rev_{0}_";
    private final static String REVISION_FOLDER = "Revision-{1}/";
    private final static String UPLOADS_FOLDER = "uploads/";
    public final static String TEMP_FOLDER = "temp/";
    public final static String JOURNAL_THUMBNAIL_FOLDER = "Journal-Thumbnails/";
    private final static String SUBMISSION_FOLDER = "Submissions/";
    public final static String SUBMISSION_TEMP_FOLDER = SUBMISSION_FOLDER + "temp/";
    public final static String SUBMISSION_ARTICLE_FOLDER = SUBMISSION_FOLDER + "Article-{0}/";
    public final static String SUBMISSION_ARTICLE_ORIGINAL_FILE_FOLDER = SUBMISSION_ARTICLE_FOLDER + "Original-files/";
    private final static String TECHNICAL_CHECK_FOLDER = SUBMISSION_ARTICLE_FOLDER + "Technical-Check/" + REVISION_FOLDER;
    public final static String TECHNICAL_CHECK_ATTACHMENT_FOLDER = TECHNICAL_CHECK_FOLDER + "attachments/";
    public final static String SUB_REVISION_UPLOAD_FOLDER = TECHNICAL_CHECK_FOLDER + UPLOADS_FOLDER;
    private final static String PEER_REVIEW_FOLDER = SUBMISSION_ARTICLE_FOLDER + "Peer-Review/";
    public final static String PEER_REVIEW_FILE_FOLDER = PEER_REVIEW_FOLDER + "reviewed-files/";
    private final static String PEER_REVIEW_REVISION_FOLDER = PEER_REVIEW_FOLDER + REVISION_FOLDER;
    public final static String PEER_REVIEW_REVISION_ATTACHMENT_FOLDER = PEER_REVIEW_REVISION_FOLDER + "attachments/";
    public final static String PEER_REVIEW_REVISION_UPLOAD_FOLDER = PEER_REVIEW_REVISION_FOLDER + UPLOADS_FOLDER;
}
