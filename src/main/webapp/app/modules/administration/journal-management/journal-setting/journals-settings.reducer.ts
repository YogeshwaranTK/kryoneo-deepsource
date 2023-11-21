import axios from 'axios';
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {IQueryParams, serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {JOURNAL_SETTINGS} from "app/config/env";
import {SubmissionListModel} from "app/config/submissions.config";
import {JOUR_MODEL} from "app/config/journals.config";
import {JournalFileFormatListModel, JournalLangListModel} from "app/config/journal-settings.config";

const initialState = {
  loading: false,
  errorMessage: null,
  journalDetails: <JOUR_MODEL>{},
  category: <any>[],
  journalLangList: <JournalLangListModel>[],
  journalCategoryList: <any>[],
  fileFormatList: <JournalFileFormatListModel>[],
  fileTypeList: <any>[],
  journalFileTypeList: <any>[],
  article: <SubmissionListModel>[],
  totalItems: 0,
  CategoryUpdateRequestStatus: false,
  ArticleRevisionUploadStatus: false,
  ArticleDeleteStatus: false,
  UpdateSummarySuccess: false,
  GuidLineSubmitSuccess: false,
  deleteCheckListSuccess: false,
  checkListDetails: [],
  journalAccessRole: {},
  journalAccessSuccess: false,
  getCheckListSuccess: false,
  putCheckListSuccess: false,
  postCheckListSuccess: false,
  metadataUpdateSuccess: false,
  publishDetailsUpdateSuccess: false

};

export const getSubmissionList = createAsyncThunk(
  'settingsManagement/getSubmissionList', async ({
                                                   page,
                                                   size,
                                                   sort,
                                                   searchText,
                                                   submissionListRequestType
                                                 }: IQueryParams) => {
    const requestUrl = `${JOURNAL_SETTINGS.submissionDetailsList}${
      sort
        ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}${submissionListRequestType ? "&workflowStage=" + submissionListRequestType : ""}`
        : `?page=${page}&size=${size}&sort=createdAt,desc&searchText=${searchText}${submissionListRequestType ? "&workflowStage=" + submissionListRequestType : ""}`
    }`;
    return axios.get<SubmissionListModel>(requestUrl);
  });


export const JournalFileTypeListRequest = createAsyncThunk('settingsManagement/JournalFileTypeListRequest',
  async () => {
    const Req_Url = `api/v1/journal/file-types?searchText=&page=0&size=100`;
    return axios.get<JournalFileFormatListModel>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);
export const submissionFileTypeListRequest = createAsyncThunk('settingsManagement/submissionFileTypeListRequest',
  async () => {
    const Req_Url = `api/v1/file-types?searchText=&page=0&size=100`;
    return axios.get<JournalFileFormatListModel>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);


export const getJournalDetails = createAsyncThunk(
  'settingsManagement/getJournalDetails',
  async (id: any) => {
    const requestUrl = `${JOURNAL_SETTINGS.JournalDetails}?id=${id}`;
    return axios.get<JOUR_MODEL>(requestUrl);
  },
  {serializeError: serializeAxiosError}
);

export const getJournalAccessRole = createAsyncThunk(
  'settingsManagement/getJournalAccessRole',
  async () => {
    const requestUrl = `${JOURNAL_SETTINGS.journalAccessRole}`;
    return axios.get<any>(requestUrl);
  },
  {serializeError: serializeAxiosError}
);

export const publishDetailsUpdate = createAsyncThunk(
  'settingsManagement/publishDetailsUpdate',
  async (data: {
    publishedBy: string;
    onlineIssn: string;
    printIssn: string;
    description: string;
    editorChief: string
  }) => {
    const requestUrl = `${JOURNAL_SETTINGS.PublishingDetailsUpdate}`;
    return axios.put<JOUR_MODEL>(requestUrl, data);
  },
  {serializeError: serializeAxiosError}
);

export const UpdateSummary = createAsyncThunk(
  'settingsManagement/UpdateSummary',
  async (data: { summary: string; thumbnail: string }) => {
    const requestUrl = `${JOURNAL_SETTINGS.SummaryDetailsUpdate}`;
    return axios.put<any>(requestUrl, data);
  },
  {serializeError: serializeAxiosError}
);

export const GuidLineSubmit = createAsyncThunk(
  'settingsManagement/GuidLineSubmit',
  async (data: any) => {
    const requestUrl = `${JOURNAL_SETTINGS.GuidelinesUpdate}`;
    return axios.put<any>(requestUrl, data);
  },
  {serializeError: serializeAxiosError}
);

export const metadataUpdate = createAsyncThunk(
  'settingsManagement/metadataUpdate',
  async (data: any) => {
    const requestUrl = `${JOURNAL_SETTINGS.metadataUpdate}`;
    return axios.put<any>(requestUrl, data);
  },
  {serializeError: serializeAxiosError}
);

export const CategoryUpdateRequest = createAsyncThunk(
  'settingsManagement/CategoryUpdate',
  async (data: any) => {
    const requestUrl = `${JOURNAL_SETTINGS.CategoryUpdate}`;
    return axios.put<JournalFileFormatListModel>(requestUrl, data);
  },
  {serializeError: serializeAxiosError}
);

export const getCategoryList = createAsyncThunk(
  'settingsManagement/getCategoryList', async () => {
    const requestUrl = `api/v1/submission-article/journal-category/list?searchText=&page=0&size=50`;
    return axios.get<any>(requestUrl);
  });

export const getJournalFiletypeRequest = async () => {
  return axios.get<any>(`${JOURNAL_SETTINGS.JournalFiletypeArticle}?page=0&size=50`)
};

export const getJournalSubmissionLangListRequest = async () => {
  return axios.get<any>(`${JOURNAL_SETTINGS.SubmissionlanguageList}?page=0&size=50`)
};

export const getJournalLangListRequest = createAsyncThunk(
  'settingsManagement/getJournalLangListRequest',
  async () => {
    const requestUrl = `${JOURNAL_SETTINGS.JournallanguageList}?page=0&size=200`;
    return axios.get<JournalLangListModel>(requestUrl);
  },
  {serializeError: serializeAxiosError}
);

export const CreateJournalCategory = createAsyncThunk(
  'settingsManagement/CreateJournalCategory',
  async (header: string) => {

    const requestUrl = `${JOURNAL_SETTINGS.CreateJournalCategory}?name=${header}`;
    return axios.post<any>(requestUrl);
  },
  {serializeError: serializeAxiosError}
);

export const CreateJournalFileType = createAsyncThunk(
  'settingsManagement/CreateJournalFileType',
  async (header: string) => {

    const requestUrl = `${JOURNAL_SETTINGS.CreateArticleSubmissionFileType}?name=${header}`;
    return axios.post<any>(requestUrl);
  },
  {serializeError: serializeAxiosError}
);

export const getJournalCategoryList = createAsyncThunk(
  'settingsManagement/getJournalCategoryList',
  async () => {
    const requestUrl = `api/v1/category/list?page=0&size=50`;
    return axios.get<JournalLangListModel>(requestUrl);
  },
  {serializeError: serializeAxiosError}
);

export const fileFormatListRequest = createAsyncThunk(
  'settingsManagement/fileFormatList',
  async () => {
    const requestUrl = `${JOURNAL_SETTINGS.ArticleSubmissionFileFormat}?page=0&size=50`;
    return axios.get<JournalFileFormatListModel>(requestUrl);
  },
  {serializeError: serializeAxiosError}
);


export const CreateChecklistRequest = createAsyncThunk(
  'settingsManagement/CreateChecklistRequest',
  async (data: any) => {
    const Req_Url = `api/v1/journal/journal-checklist`;
    return axios.post<any>(Req_Url, data);
  },
  {serializeError: serializeAxiosError}
);

export const getCheckList = createAsyncThunk('settingsManagement/getCheckList', async () => {
  const Req_Url = `api/v1/journal/journal-checklist?page=0&size=50`;
  return axios.get<any>(`${Req_Url}`)
});

export const putChecklistRequest = createAsyncThunk(
  'settingsManagement/PutChecklistRequest',
  async (data: any) => {
    const Req_Url = `api/v1/journal/journal-checklist`;
    return axios.put<any>(Req_Url, data);
  },
  {serializeError: serializeAxiosError}
);

export const deleteChecklistRequest = createAsyncThunk(
  'settingsManagement/DeleteChecklistRequest',
  async (itemId: number) => {
    const Req_Url = `api/v1/journal/journal-checklist?id=${itemId}`;
    return axios.delete<any>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);


export const ArticleDeleteModelRequest = createAsyncThunk(
  'settingsManagement/JournalDeleteModelRequest',
  async (data: any) => {
    const Req_Url = `api/v1/submission-article?id=${data.id}&deletedRemarks=${data.deletedRemarks}`;
    return axios.delete<any>(Req_Url, data);
  },
  {serializeError: serializeAxiosError}
);


export const ArticleRevisionUpload = createAsyncThunk(
  'settingsManagement/ArticleRevisionUpload',
  async (data: any) => {
    const Req_Url = `api/v1/submission-article/revision/author-file-upload`;
    return axios.post<any>(Req_Url, data);
  },
  {serializeError: serializeAxiosError}
);


export type settingsManagementState = Readonly<typeof initialState>;
export const settingsManagementSlice = createSlice({
  name: 'settingsManagement',
  initialState: initialState as settingsManagementState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(ArticleDeleteModelRequest.fulfilled, (state) => {
        state.loading = false;
        state.ArticleDeleteStatus = true;
      })
      .addCase(ArticleRevisionUpload.fulfilled, (state) => {
        state.loading = false;
        state.ArticleRevisionUploadStatus = true
      })
      .addCase(submissionFileTypeListRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.fileTypeList = action.payload.data;
      })
      .addCase(JournalFileTypeListRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.journalFileTypeList = action.payload.data;
      })
      .addCase(CategoryUpdateRequest.fulfilled, (state) => {
        state.loading = false;
        state.CategoryUpdateRequestStatus = true
      })
      .addCase(getSubmissionList.fulfilled, (state, action) => {
        state.loading = false;
        state.article = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
        state.ArticleRevisionUploadStatus = false
        state.ArticleDeleteStatus = false;
      })
      .addCase(getJournalLangListRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.journalLangList = action.payload.data;
      })
      .addCase(getJournalCategoryList.fulfilled, (state, action) => {
        state.loading = false;
        state.journalCategoryList = action.payload.data;
      })
      .addCase(GuidLineSubmit.fulfilled, (state) => {
        state.loading = false;
        state.GuidLineSubmitSuccess = true;
      })
      .addCase(metadataUpdate.fulfilled, (state) => {
        state.loading = false;
        state.metadataUpdateSuccess = true;
      })
      .addCase(fileFormatListRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.fileFormatList = action.payload.data;
      })
      .addCase(getCategoryList.fulfilled, (state, action) => {
        state.loading = false;
        state.category = action.payload.data;
        state.CategoryUpdateRequestStatus = false;
      })
      .addCase(UpdateSummary.fulfilled, (state) => {
        state.loading = false;
        state.UpdateSummarySuccess = true;
      })
      .addCase(getJournalDetails.fulfilled, (state, action) => {
        state.loading = false;
        state.journalDetails = action.payload.data;
        state.publishDetailsUpdateSuccess = false
      })
      .addCase(getJournalAccessRole.fulfilled, (state, action) => {
        state.loading = false;
        state.journalAccessRole = action.payload.data;
        state.journalAccessSuccess = true;
      })
      .addCase(getJournalAccessRole.pending, (state) => {
        state.loading = true;
        state.journalAccessSuccess = false;
        state.errorMessage = null;
      })
      .addCase(publishDetailsUpdate.fulfilled, (state) => {
        state.loading = false;
        state.publishDetailsUpdateSuccess = true;
      })

      .addCase(getCheckList.fulfilled, (state, action) => {
        state.loading = false;
        state.getCheckListSuccess = true;
        state.checkListDetails = action.payload.data;
        state.putCheckListSuccess = false;
        state.postCheckListSuccess = false;
        state.deleteCheckListSuccess = false;
      })
      .addCase(deleteChecklistRequest.fulfilled, (state) => {
        state.loading = false;
        state.deleteCheckListSuccess = true;
      })
      .addCase(putChecklistRequest.fulfilled, (state) => {
        state.loading = false;
        state.putCheckListSuccess = true;
      })
      .addCase(CreateChecklistRequest.fulfilled, (state) => {
        state.loading = false;
        state.postCheckListSuccess = true;
      })
      .addCase(getSubmissionList.pending, (state) => {
        state.loading = true;
        state.errorMessage = null;
        state.article = <SubmissionListModel>[];
      })

      .addMatcher(isPending(metadataUpdate, deleteChecklistRequest, getJournalCategoryList, submissionFileTypeListRequest, CategoryUpdateRequest, GuidLineSubmit, getJournalLangListRequest, fileFormatListRequest, getJournalDetails, publishDetailsUpdate, UpdateSummary, getCategoryList, getCheckList, getSubmissionList), state => {
        state.loading = true;
        state.errorMessage = null;
      })
      .addMatcher(isRejected(metadataUpdate, deleteChecklistRequest, getJournalCategoryList, submissionFileTypeListRequest, CategoryUpdateRequest, GuidLineSubmit, getJournalLangListRequest, fileFormatListRequest, getJournalDetails, publishDetailsUpdate, getJournalAccessRole, UpdateSummary, getCategoryList, getCheckList, getSubmissionList), (state, action) => {
        state.loading = false;
        state.errorMessage = action.error.message;
      });
  },
});
export const {reset} = settingsManagementSlice.actions;

export default settingsManagementSlice.reducer;
