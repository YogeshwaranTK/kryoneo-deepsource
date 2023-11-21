export const truncateString = (str) => {
  if (str.length > 50) {
    return str.slice(0, 50) + '...';
  } else {
    return str;
  }
};


export interface JOUR_MODEL {
  abbreviation: string;
  accessType: string;
  createdByName: string;
  createdDate: string;
  description: string;
  id: number;
  initials: string;
  key: string;
  lastModifiedByName: string;
  lastModifiedDate: string;
  onlineIssn: string;
  printIssn: string;
  published: string;
  publishedBy: string;
  publishedDateTime: string;
  summary: string;
  thumbnail: string;
  title: string;
}
