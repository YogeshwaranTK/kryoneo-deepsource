import {
  getJournalFiletypeRequest
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import {fileDownloadUrlRequest} from "app/modules/administration/journal-management/review/reviewReducer";

export const getJournalFileTypeArticle = (setFileTypeList, setIsLoading) => {
  setIsLoading(true);
  getJournalFiletypeRequest()
    .then(response => {
      setFileTypeList(response.data);
      setIsLoading(false);
    })
    .catch(error => {
      setIsLoading(false);
      console.error(error);
    });
};


export const downloadFile = (requestUrl, fileName) => {
  fileDownloadUrlRequest(requestUrl)
    .then(response => {
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const a = document.createElement('a');
      a.href = url;
      a.download = fileName; // Replace with the file name
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
    })
    .catch(error => {
      console.error('Error downloading the file:', error);
    });
};
