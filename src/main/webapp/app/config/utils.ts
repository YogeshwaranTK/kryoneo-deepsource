import {
  CreateJournalFileType
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import {journalFileTypeListRequest} from "app/modules/administration/journal-management/create-new-submission/work-flow.reducer";

export const handleCreateJournalFileType = async (fileType, setChangedFileType, dispatch) => {
  const isNewItems = fileType.filter(item => item['__isNew__'] === true);

  if (isNewItems.length !== 0) {
    const headers = isNewItems[0]['value'];
    setChangedFileType(isNewItems[0]['value']);

    try {
      const resultAction = await dispatch(CreateJournalFileType(headers));

      if (CreateJournalFileType.fulfilled.match(resultAction) && resultAction.payload.status === 200) {
        dispatch(journalFileTypeListRequest());
      }
    } catch (error) {
      console.error('Error in handleCreateJournalFileType:', error);
    }
  }
};


export const updateFileType = (fileType, fileTypeList, changedFileType) => {
  const filteredData = fileTypeList.filter(item => item.name === changedFileType);

  if (filteredData.length !== 0) {
    return fileType.map(item => ({
      ...item,
      value: item['__isNew__'] ? filteredData[0]['id'] : item['value'],
      __isNew__: item['__isNew__'] ? false : item['__isNew__'],
    }));
  }
  return fileType;
};

export const handleNewItems = (isNewItems, setChangedCategory, dispatch, createFunction, successCallback) => {
  if (isNewItems.length !== 0) {
    const headers = isNewItems[0]['value'];
    setChangedCategory(isNewItems[0]['value']);

    dispatch(createFunction(headers))
      .then((resultAction) => {
        if (successCallback && createFunction.fulfilled.match(resultAction)) {
          if (resultAction.payload.status === 200) {
            successCallback();
          }
        }
      })
      .catch((error) => {
        // Handle errors if needed
        console.error('Error in handleNewItems:', error);
      });
  }
};
