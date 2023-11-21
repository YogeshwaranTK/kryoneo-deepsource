import axios from 'axios';
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {WORK_FLOW} from "app/config/env";
import {JournalFileFormatListModel, JournalLangListModel} from "app/config/journal-settings.config";


const initialState = {
  loading: false,
  errorMessage: null,
  createNewArticleStatus: false,
  basicJournalUpdateStatus: null,
  submissionId: null,
  JournalLangList: <JournalLangListModel>[],
  journalFileFormatList: <JournalFileFormatListModel>[],
  fileTypeList: <any>[],
  journalFileTypeList: <any>[],
  ArticleUploadFileUpdateStatus: false,
  ArticleList: {},
  ArticleUploadRequestStatus: false,
  CategoryUpdateRequestStatus: null,
  contributorUpdateRequestStatus: false,
  finalSubmit: false,
  finishArticle_status: false,
  articleGetRequestPreviewDetails: {},
  FinalSubmissionError: <any>null,

};

export const getSubmissionDetailsRaw = async (id?: number) => {
  return axios.get<any>(`${WORK_FLOW.articleGetRequest}?submissionId=${id}`)
};

export const articleGetRequestPreview = createAsyncThunk('workflow/articleGetRequestPreview',
  async (previewID?: any) => {
    const Req_Url = `${WORK_FLOW.articleGetRequest}?id=${previewID}`;
    return axios.get<any>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);

export const basicJournalUpdate = createAsyncThunk('workflow/basicJournalUpdate',
  async (data?: any) => {
    const Req_Url = `${WORK_FLOW.basciJournalUpdate}?submissionId=${data.submissionId}`;
    return axios.put<any>(Req_Url, data.data);
  },
  {serializeError: serializeAxiosError}
);

export const ContributerUpdateRequest = createAsyncThunk('workflow/ContributerUpdateRequest',
  async (data?: any) => {
    const Req_Url = `${WORK_FLOW.ContributerUpdateRequest}?submissionId=${data.submissionId}`;
    return axios.put<any>(Req_Url, data.submissionAuthors);
  },
  {serializeError: serializeAxiosError}
);

export const ArticleFileFormatListRequest = createAsyncThunk('workflow/fileFormatList',
  async () => {
    const Req_Url = `${WORK_FLOW.ArticlefileFormateListRequest}?page=0&size=100`;
    return axios.get<JournalFileFormatListModel>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);

export const journalFileTypeListRequest = createAsyncThunk('workflow/submissionFileTypeListRequest',
  async () => {
    const Req_Url = `api/v1/file-types?searchText=&page=0&size=100`;
    return axios.get<JournalFileFormatListModel>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);

export const JournalFileTypeListRequest = createAsyncThunk('workflow/JournalFileTypeListRequest',
  async () => {
    const Req_Url = `api/v1/journal/file-types?searchText=&page=0&size=100`;
    return axios.get<JournalFileFormatListModel>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);

export const ArticleUploadFileUpdate = createAsyncThunk('workflow/ArticleUploadFileUpdate',
  async (data: any) => {
    const Req_Url = `${WORK_FLOW.ArticleUploadFileUpdate}`;
    return axios.put<any>(Req_Url, data);
  },
  {serializeError: serializeAxiosError}
);


export const ArticleCategoryUpdateRequest = createAsyncThunk('workflow/ArticleCategoryUpdateRequest',
  async (data: any) => {
    const Req_Url = `${WORK_FLOW.ArticleCategoryUpdateRequest}`;
    return axios.put<JournalFileFormatListModel>(Req_Url, data);
  },
  {serializeError: serializeAxiosError}
);

export const AricleFinalRequest = createAsyncThunk('workflow/AricleFinalRequest',
  async (submissionId: number) => {
    const Req_Url = `${WORK_FLOW.AricleFinalRequest}?submissionId=${submissionId}`;
    return axios.put<any>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);

export const ArticleUploadRequest = createAsyncThunk('workflow/ArticleUploadRequest',
  async (data: any) => {
    const Req_Url = `${WORK_FLOW.ArticleUploadRequest}?submissionId=${data.submissionId}`;
    return axios.post<JournalFileFormatListModel>(Req_Url, data.formData);
  },
  {serializeError: serializeAxiosError}
);

export const getJournalSubmissionLangListRequest = createAsyncThunk('workflow/getJournalSubmissionLangListRequest',
  async () => {
    const Req_Url = `${WORK_FLOW.getJournalSubmisionlangListRequest_d}?page=0&size=500`;
    return axios.get<JournalLangListModel>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);

export const createNewArticle = createAsyncThunk('workflow/createNewArticle',
  async (data?: any) => {
    const Req_Url = `${WORK_FLOW.createNewArticle}`;
    return axios.post<any>(Req_Url, data);
  },
  {serializeError: serializeAxiosError}
);

export const GetSampleExcelFile = async () => {
  return axios.get<any>(`/api/v1/submission-article/contributor-add-sample-file`, {responseType: 'blob'})
};


export type workwlowState = Readonly<typeof initialState>;
export const WorkFlowSlice = createSlice({
  name: 'workflow',
  initialState: initialState as workwlowState,
  reducers: {
    reset() {
      return initialState;
    },
    contributorUpdateTrigger(state) {
      state.contributorUpdateRequestStatus = false
    }
  },
  extraReducers(builder) {
    builder
      .addCase(articleGetRequestPreview.fulfilled, (state, action) => {
        state.loading = false;
        state.articleGetRequestPreviewDetails = action.payload.data;
        state.contributorUpdateRequestStatus = false;
      })
      .addCase(createNewArticle.fulfilled, (state, action) => {
        state.loading = false;
        state.createNewArticleStatus = true;
        state.submissionId = action.payload.data.submissionId;
        state.finishArticle_status = false;
      })

      .addCase(AricleFinalRequest.fulfilled, (state) => {
        state.loading = false;
        state.finishArticle_status = true;
      })
      .addCase(ContributerUpdateRequest.fulfilled, (state) => {
        state.loading = false;
        state.contributorUpdateRequestStatus = true
        state.finalSubmit = true
      })
      .addCase(ArticleCategoryUpdateRequest.fulfilled, (state) => {
        state.loading = false;
        state.CategoryUpdateRequestStatus = Date();
      })
      .addCase(ArticleUploadRequest.fulfilled, (state) => {
        state.loading = false;
        state.ArticleUploadRequestStatus = true
        state.finalSubmit = true
      })
      .addCase(ArticleFileFormatListRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.journalFileFormatList = action.payload.data;
        state.ArticleUploadRequestStatus = false;
        state.FinalSubmissionError = null
      })
      .addCase(journalFileTypeListRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.fileTypeList = action.payload.data;
        state.ArticleUploadRequestStatus = false;
      })
      .addCase(JournalFileTypeListRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.journalFileTypeList = action.payload.data;
      })
      .addCase(ArticleUploadFileUpdate.fulfilled, (state) => {
        state.loading = false;
        state.ArticleUploadFileUpdateStatus = true
      })
      .addCase(getJournalSubmissionLangListRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.JournalLangList = action.payload.data;
        state.createNewArticleStatus = false
      })
      .addCase(basicJournalUpdate.fulfilled, (state) => {
        state.loading = false;
        state.basicJournalUpdateStatus = Date();
      })
      .addMatcher(isRejected(AricleFinalRequest, journalFileTypeListRequest),
        (state) => {
          state.loading = false;
          state.finishArticle_status = false
        }
      )
      .addMatcher(isPending(journalFileTypeListRequest, AricleFinalRequest, articleGetRequestPreview, basicJournalUpdate, ArticleCategoryUpdateRequest, ArticleUploadRequest),
        state => {
          state.loading = true;
          state.errorMessage = null;
        }
      )
      .addMatcher(isRejected(basicJournalUpdate),
        (state, action) => {
          state.loading = false;
          state.errorMessage = null;
          state.FinalSubmissionError = action['error']['response']['data']['errorMessages'];
        }
      );
  },
});

export const {reset, contributorUpdateTrigger} = WorkFlowSlice.actions;

export default WorkFlowSlice.reducer;
