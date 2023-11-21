export interface SubmissionDetailsModel {
  id: number;
  prefix: string | null;
  title: string;
  subTitle: string;
  description: string;
  createdDate: string;
  createdBy: string;
  lastModifiedDate: string;
  submissionLanguage: {
    id: number;
    name: string;
    langKey: string;
  };
  status: string;
  categories: any[];
  submissionFiles: SubmissionFile[];
  authors: Author[];
  keywords: string[];
  journalTitle: string | null;
  allFilePath: string | null;
  agree: boolean;
  submissionContributors: SubmissionContributor[];
  submissionDiscussions: SubmissionDiscussion[];
  ownSubmission: any;
}

interface SubmissionFile {
  id: number;
  fileType: {
    id: number;
    name: string;
  };
  file: string;
  fileEndPoint: string;
  createdAt: string;
}

interface Author {
  id: number;
  prefix: string | null;
  firstName: string;
  surName: string | null;
  middleName: string | null;
  email: string;
  affiliation: string | null;
  orcidId: string | null;
  role: string | null;
  browserList: string | null;
  ownAuthorDetail: string | null;
  affiliations: any[];
  primary: boolean;
}

interface SubmissionContributor {
  fullName: string;
  id: string;
}

interface SubmissionDiscussion {
  id: number;
  topic: string;
  description: string;
  createdAt: string;
  discussionMessages: SubmissionDiscussionMessage[];
  discussionFiles: any[];
  status: string;
}

interface SubmissionDiscussionMessage {
  id: number;
  discussionId: number;
  userFullName: string;
  message: string;
  submissionDiscussionMessageFiles: any[];
  createdAt: string;
}


export interface SubmissionListModel {
  id?: number;
  prefix?: null;
  title?: string;
  subTitle?: null;
  description?: null;
  createdDate?: string;
  createdBy?: string;
  lastModifiedDate?: string;
  submissionLanguage?: null;
  status?: string;
  categories?: null;
  submissionFiles?: null;
  authors?: null;
  keywords?: null;
  journalTitle?: null;
  allFilePath?: null;
  agree?: boolean;
  submissionContributors?: null;
  submissionDiscussions?: null;
  ownSubmission?: null;
}
