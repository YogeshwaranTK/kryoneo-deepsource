export const BASE_URL = 'api/v1';
export const BASE_URL_V2 = 'api/v2';
// export const ASSETS_URL = 'https://uat-kjms-static.kryoneo.com'
const Submission_Article = '/submission';
const Contributors_Group = '/journal-group';
const Contributors_User = '/journal-user';
const Contributors_invite = '/api/admin/user';
const journal = "/journal";
const users = "/users";
const user = "/user";
const group = "/group";
const role = "/org-role";
const organization = "/organization";
const Journal_Setting = '/journal/settings';
const production = '/production';
const submission = '/submission';
const copy_editing = '/copy-editing';
const review = "/review"
const reviewer = "/reviewer"
const mail = "/mail"

export const WORK_FLOW = {
  articleGetRequest: `${BASE_URL + Submission_Article}`,
  basciJournalUpdate: `${BASE_URL + Submission_Article}/basic-detail/update`,
  thumbuploadImge_Article_Upload: `${BASE_URL + Submission_Article}/file/upload`,
  ContributerUpdateRequest: `${BASE_URL + Submission_Article}/author/update`,
  ArticleUploadFileUpdate: `${BASE_URL + Submission_Article}/file/update`,
  ArticleCategoryUpdateRequest: `${BASE_URL + Submission_Article}/category/update`,
  AricleFinalRequest: `${BASE_URL + Submission_Article}/final`,
  ArticleUploadRequest: `${BASE_URL + Submission_Article}/files/upload`,
  createNewArticle: `${BASE_URL + Submission_Article}/basic-detail/create`,
  getJournalSubmisionlangListRequest_d: `${BASE_URL}/journal/submission-languages`,
  ArticlefileFormateListRequest: `${BASE_URL}/journal/submission-file-formats`,
};

export const JOURNAL_MANAGEMENT = {
  JournalList: `${BASE_URL + journal}s`,
  JournalCreate: `${BASE_URL + journal}/create`,
  JournalDelete: `${BASE_URL + journal}`,
  JournalExportExcel: `${BASE_URL + journal}/export-excel`,
  JournalExportPDF: `${BASE_URL + journal}/export-pdf`
};

export const CONTRIBUTORS = {
  ContributorsGroupList: `${BASE_URL + Contributors_Group}/list`,
  ContributorsGroupCURD: `${BASE_URL + Contributors_Group}`,
  ContributorsUserList: `${BASE_URL + Contributors_User}/list`,
  ContributorsUserCURD: `${BASE_URL + Contributors_User}`,
  ContributorsInviteList: `${Contributors_invite}/journal-user/invite-list`,
  ContributorsInviteCURD: `${Contributors_invite}/invite`,
  ContributorsUserSearchList: `${BASE_URL + Contributors_User}/non-journal-users/list`,
  contributionAddAuthorPost: `${BASE_URL}/author`,
  contributionAddReviewerPost: `${BASE_URL}/reviewer`,
  contributionAddEditorialPost: `${BASE_URL}/editorial-users`,
  getJournalRoles: `${BASE_URL}/journal-role/roles`
};

export const JOURNAL_SETTINGS = {
  PublishingDetailsUpdate: `${BASE_URL + Journal_Setting}/basic-detail-update`,
  SummaryDetailsUpdate: `${BASE_URL + Journal_Setting}/summary-update`,
  GuidelinesUpdate: `${BASE_URL + Journal_Setting}/guidelines-update`,
  metadataUpdate: `${BASE_URL + Journal_Setting}/metadata-update`,
  CategoryUpdate: `${BASE_URL + Journal_Setting}/journal-category-update`,
  JournalDetails: `${BASE_URL + journal}`,
  submissionDetailsList: `${BASE_URL}/submissions`,
  CategoryList: `${BASE_URL}/category/list`,
  ThumbnailUpload: `${BASE_URL + journal}/upload-thumbnail`,
  SubmissionFileTypeList: `${BASE_URL + journal}/submission-file-format/list`,
  SubmissionlanguageList: `${BASE_URL + journal}/submission-lang/list`,
  JournallanguageList: `${BASE_URL}/language/list`,
  ArticleSubmissionFileFormat: `${BASE_URL}/article-submission-file-format/list`,
  CreateArticleSubmissionFileType: `${BASE_URL}/submission-file-type/create`,
  CreateJournalCategory: `${BASE_URL}/category/create`,
  JournalFiletypeArticle: `${BASE_URL}/journal/file-types`,
  journalAccessRole: `${BASE_URL}/journal/access`,
};

export const USER_MANAGEMENT = {
  UserList: `${BASE_URL}/user/list`,
  UserCreate: `${BASE_URL + user}/create`,
  UserRemove: `${BASE_URL + user}/remove`,
  UserUpdate: `${BASE_URL + user}/update`,
  UserDetail: `${BASE_URL + users}`,
  RoleList: `${BASE_URL + role}/list`,
  RoleCreate: `${BASE_URL + role}/create`,
  RoleRemove: `${BASE_URL + role}`,
  RoleDetail: `${BASE_URL + role}`,
  RoleUserList: `${BASE_URL + role}/user/list`,
  GroupList: `${BASE_URL + group}/list`,
  GroupCRUD: `${BASE_URL + group}`,
  GroupUserList: `${BASE_URL + group}/user/list`,
  GroupUserCURD: `${BASE_URL + group}/user`,
  userData: `${BASE_URL + user}`
};

export const ACCOUNT = {
  OrgCreate: `${BASE_URL + organization}/create`,
  OrgEmailVerify: `${BASE_URL + organization}/email-verify`,
  OrgEmailOTP: `${BASE_URL}/send-forgot-password-email-otp`,
  OrgResendOTP: `${BASE_URL + organization}/resend-email-otp`,
}

export const PUBLIC_API = {
  CountryList: `${BASE_URL}/country/list`,
}

export const AUTH_API = {
  GetAccount: `${BASE_URL}/account`,
  Authentication: `${BASE_URL}/authenticate`
}

export const PRODUCTION = {
  assignUser: `${BASE_URL + production}/add-contributors`,
  assignUsersList: `${BASE_URL + production}/contributors`,
  ProductionReadyFiles: `${BASE_URL + production}/production-ready-file/upload`,
  productionDiscussionAdd: `${BASE_URL + production}/discussion`,
  productionDiscussionAddChat: `${BASE_URL + production}/discussion/chat`,
  getProductionListDetails: `${BASE_URL + production}`,
  deleteProductionParticipantUser: `${BASE_URL + production}/remove-contributor`,
  productionReadyFilesGet: `${BASE_URL + production}/production-ready-files`,
  productionCompletedFilesPost: `${BASE_URL + production}/production-completed-file/upload`,
  productionUpdateStatus: `${BASE_URL + production}/discussion/status`,
  productionDeleteFile: `${BASE_URL + production}/production-ready-files`
}

export const COPY_EDITING = {
  assignUser: `${BASE_URL + copy_editing}/add-contributors`,
  assignUsersList: `${BASE_URL + copy_editing}/contributors`,
  CopyEditingReadyFiles: `${BASE_URL + copy_editing}/draft-files/upload`,
  CopyEditingDiscussionAdd: `${BASE_URL + copy_editing}/discussion`,
  copyEditingDiscussionAddChat: `${BASE_URL + copy_editing}/discussion/chat`,
  getCopyEditingListDetails: `${BASE_URL + copy_editing}`,
  deleteParticipantUser: `${BASE_URL + copy_editing}/remove-contributor`,
  CopyEditingReadyFilesGet: `${BASE_URL + copy_editing}/production-ready-files`,
  CopyEditedFiles: `${BASE_URL + copy_editing}/copy-edited-files`,
  CopyEditedFileUpload: `${BASE_URL + copy_editing}/copy-edited-files/upload`,
  CopyEditingUpdateStatus: `${BASE_URL + copy_editing}/discussion/status`,
  SubmissionMoveToCopyEditing: `${BASE_URL + copy_editing}/move-to-copy-editing`,
  CopyEditingMoveToProduction: `${BASE_URL + production}/move-to-production`,
}

export const SUBMISSION = {
  assignUser: `${BASE_URL + submission}/add-contributors`,
  assignUsersList: `${BASE_URL + submission}/contributors`,
  SubmissionReadyFiles: `${BASE_URL + submission}/files/upload`,
  submissionDiscussionAdd: `${BASE_URL + submission}/discussion`,
  submissionDiscussionAddChat: `${BASE_URL + submission}/discussion/chat`,
  getSubmissionListDetails: `${BASE_URL + submission}`,
  deleteParticipantUser: `${BASE_URL + submission}/remove-contributor`,
  submissionReadyFilesGet: `${BASE_URL + submission}/submission-ready-files`,
  submissionCompletedFilesPost: `${BASE_URL + submission}/submission-completed-file/upload`,
  acceptSubmissionAndMoveReview: `${BASE_URL + review}/move-to-review`,
  SubmissionUploadRevision: `${BASE_URL + submission}/revision/file/upload`,
  SubmissionUpdateStatus: `${BASE_URL + submission}/discussion/status`,
  SubmissionDeleteFile: `${BASE_URL + submission}/file`,
  SubmissionDownloadFile: `${BASE_URL+submission}/files/download-as-zip`
}

export const Review = {
  createRound: `${BASE_URL + review}/create-review-round`,
  review_get: `${BASE_URL + review}/rounds`,
  getReviewRoundContributors: `${BASE_URL + review}/review-round/contributors`,
  ReviewReadyFiles: `${BASE_URL + review}/review-round-files/upload`,
  GetReviewRound: `${BASE_URL + review}/round`,
  assignUser: `${BASE_URL + review}/review-round/add-contributors`,
  reviewDiscussionAdd: `${BASE_URL + review}/review-round/discussion`,
  reviewDiscussionAddChat: `${BASE_URL + review}/review-round/discussion/chat`,
  deleteParticipantUser: `${BASE_URL + review}/review-round/remove-contributor`,
  assignUsersList: `${BASE_URL + review}/review-round/contributors`,
  addReviewer: `${BASE_URL + review}/review-round/add-reviewer`,
  reviewUpdateStatus: `${BASE_URL + review}/review-round/discussion/status`,
  cancelUpdateStatus: `${BASE_URL + review}/review-round/reviewer/un-assign`,
  ReviewDownloadFile: `${BASE_URL + review}/review-round/files/download-as-zip`
}

export const Reviewer = {
  getReviewerSubmissionURL: `${BASE_URL + review + reviewer}/assigned-reviews`,
  getReviewerSubmissionSingleURL: `${BASE_URL + review + reviewer}/assigned-review`,
  acceptReviewerRequest: `${BASE_URL + review + reviewer}/accept-request`,
  declineReviewerRequest: `${BASE_URL + review + reviewer}/reject-request`,
  completeReviewerRequest: `${BASE_URL + review + reviewer}/complete-review`,
  reviewerReplyFileUpload: `${BASE_URL + review + reviewer}/reply/file-upload`,
  getReviewerReplyFiles: `${BASE_URL + review + reviewer}/reply/files`,
  reviewerDiscussionAdd: `${BASE_URL + review}/review-round/reviewer/discussion`,
  reviewerDiscussionChatAdd: `${BASE_URL + review}/review-round/reviewer/discussion/chat`,
  getReviewerDiscussion: `${BASE_URL + review}/review-round/reviewer/discussion`,
  putReviewerDiscussionStatus: `${BASE_URL + review}/review-round/reviewer/discussion/status`
}

export const JournalDeveloperSettings = {
  getMailActions: `${BASE_URL + mail}/actions`,
  getMailActionDetails: `${BASE_URL + mail}/action`
}

export const DeveloperSettings = {
  getMailActions: `${BASE_URL + mail}/common/actions`,
  getMailActionDetails: `${BASE_URL + mail}/common/action`
}

export const Author = {
  register: `${BASE_URL}/register-as-author`
}
