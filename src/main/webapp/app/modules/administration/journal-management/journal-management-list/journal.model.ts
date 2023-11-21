export interface IJournal {
  id?: any;
  title?: string;
  description?: string;
  thumbnail?: string;
  onlineIssn?: string;
  printIssn?: string;
  accessType?: string;
  createdByName?: string;
  createdDate?: Date | null;
  lastModifiedByName?: string;
  lastModifiedDate?: Date | null;
  key?: string;
  initials?: string;
  abbreviation?: string;
  publishedBy?: string;
  publishedDateTime?: Date | null;
}

export const journalDefaultValue: Readonly<IJournal> = {
  id: '',
  title: '',
  description: '',
  thumbnail: '',
  onlineIssn: '',
  printIssn: '',
  accessType: '',
  createdByName: '',
  createdDate: null,
  lastModifiedByName: '',
  lastModifiedDate: null,
  key: '',
  initials: '',
  abbreviation: '',
  publishedBy: '',
  publishedDateTime: null,
};

export interface JCmodel {
  title: string;
  key: string;
  description: string;
  onlineIssn:string;
  printIssn: string;
  editorChief:string;
}


