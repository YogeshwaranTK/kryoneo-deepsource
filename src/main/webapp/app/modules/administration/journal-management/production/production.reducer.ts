import axios from 'axios';
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {JOURNAL_SETTINGS, PRODUCTION, SUBMISSION} from "app/config/env";
import {ASUserModel} from "app/modules/administration/journal-management/production/production.model";

const initialState = {
  loading: false,
  errorMessage: null,
  assignUserSuccess: false,
  fileUploadProductionReadyFilesSuccess: false,
  participantList: [],
  addDiscussionSuccess: false,
  productionDetails: [],
  productionParticipantDeleteSuccess: false,
  addChatDiscussionSuccess: false,
  addCompletedFileSuccess: false,
  updateProductionDiscussStatusSucess: false,
  postAssignUserSuccessPayload: [],
  postAddDiscussionChatPayload: {},
  postAddDiscussionPayload: {},
  postCompletedFilePayload: []
};


export const postAddDiscussionRequest = createAsyncThunk(
  'production/postAddDiscussionRequest',
  async (requestData: { formData, submissionId }) => {
    const response = await axios.post<any>(`${PRODUCTION.productionDiscussionAdd}?submissionId=${requestData.submissionId}`, requestData.formData);
    return response.data;
  }
);

export const postAddChatDiscussionRequest = createAsyncThunk(
  'production/postAddChatDiscussionRequest',
  async (requestData: { formData, submissionId,discussionId }) => {
    const response = await axios.post<any>(`${PRODUCTION.productionDiscussionAddChat}?submissionId=${requestData.submissionId}&discussionId=${requestData.discussionId}`, requestData.formData);
    return response.data;
  }
);

export const postUploadCompletedFileRequest = createAsyncThunk(
  'production/postUploadCompletedFileRequest',
  async (requestData: { formData, submissionId }) => {
    const response = await axios.post<any>(`${PRODUCTION.productionCompletedFilesPost}?submissionId=${requestData.submissionId}`, requestData.formData);
    return response.data;
  }
);

export const deleteProductionFile = createAsyncThunk(
  'production/deleteProductionRequest',
  async (requestData: { submissionId,productionFileId }) => {
    const response = await axios.delete<any>(`${PRODUCTION.productionDeleteFile}?submissionId=${requestData.submissionId}&fileId=${requestData.productionFileId}`);
    return response.data;
  }
);
export const postAssignUserProduction = createAsyncThunk('production/postAssignUserProduction', async (datas: {
  userIds: string[];
  description: string,
  submissionId: number
}) => {
  const requestBody = {
    userIds: datas.userIds,
    description: datas.description
  }
  const response = await axios.post(`${PRODUCTION.assignUser}?submissionId=${datas.submissionId}`, requestBody);
  return response.data;
});

export const postProductionReadyFilesUpload = createAsyncThunk(
  'production/postProductionReadyFilesUpload',
  async (requestData: { formData, submissionId }) => {
    await axios.post<ASUserModel>(`${PRODUCTION.ProductionReadyFiles}?submissionId=${requestData.submissionId}`, requestData.formData),
      {serializeError: serializeAxiosError}
  }
);

export const getParticipantsListRaw = async (submissionId?: number) => {
  return axios.get<any>(`${PRODUCTION.assignUsersList}?submissionId=${submissionId}`)
};

export const getParticipantsList = createAsyncThunk('journalManagement/getParticipantsList', async (submissionId?: number) => {
  return axios.get<any>(`${PRODUCTION.assignUsersList}?submissionId=${submissionId}`)
});

export const getProductionReadyFilesListRaw = async (submissionId?: number) => {
  return axios.get<any>(`${PRODUCTION.productionReadyFilesGet}?submissionId=${submissionId}`)
};

export const getProductionDiscussionListRaw = async (submissionId?: number) => {
  return axios.get<any>(`${PRODUCTION.productionDiscussionAdd}s?submissionId=${submissionId}`)
};

export const fileDownloadUrlRequest = async (requestUrl) => {
  return axios.get<any>(requestUrl, {responseType: 'blob'})
};

export const getProductionList = createAsyncThunk('journalManagement/getProductionList', async (submissionId?: number) => {
  return axios.get<any>(`${PRODUCTION.getProductionListDetails}?submissionId=${submissionId}`)
});

export const updateProductionDiscussionStatus = createAsyncThunk(
  'production/putUpdateDiscussionStatusRequest',
  async (requestData: { submissionId,discussionId,status }) => {
    const response = await axios.put<any>(`${PRODUCTION.productionUpdateStatus}?submissionId=${requestData.submissionId}&discussionId=${requestData.discussionId}&status=${requestData.status}`);
    return response.data;
  }
);

export const deleteProductionParticipantUser = createAsyncThunk('journalManagement/deleteProductionParticipantUser', async (paramsDetails: {
  submissionId,
  contributorId
}) => {
  return axios.delete<any>(`${PRODUCTION.deleteProductionParticipantUser}?submissionId=${paramsDetails.submissionId}&contributorId=${paramsDetails.contributorId}`);
});

export type ProductionState = Readonly<typeof initialState>;

export const ProductionSlice = createSlice({
  name: 'production',
  initialState: initialState as ProductionState,
  reducers: {
    reset() {
      return initialState;
    },
    stateFalse(state) {
      state.assignUserSuccess = false
      state.productionParticipantDeleteSuccess = false
      state.addChatDiscussionSuccess = false
      state.fileUploadProductionReadyFilesSuccess = false

    }
  },
  extraReducers(builder) {
    builder
      .addCase(postAssignUserProduction.fulfilled, (state, action) => {
        state.loading = false;
        state.assignUserSuccess = true;
        state.postAssignUserSuccessPayload = action.payload
      })
      .addCase(postUploadCompletedFileRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.addCompletedFileSuccess = true;
        state.postCompletedFilePayload = action.payload
      })
      .addCase(postProductionReadyFilesUpload.fulfilled, (state, action) => {
        state.loading = false;
        state.fileUploadProductionReadyFilesSuccess = true
      })
      .addCase(getProductionList.fulfilled, (state, action) => {
        state.loading = false;
        state.productionDetails = action.payload.data;
        state.updateProductionDiscussStatusSucess = false
        state.fileUploadProductionReadyFilesSuccess = false
      })

      .addCase(updateProductionDiscussionStatus.fulfilled, (state, action) => {
        state.loading = false;
        state.updateProductionDiscussStatusSucess = true
      })

      .addCase(postAddChatDiscussionRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.addChatDiscussionSuccess = true
        state.postAddDiscussionChatPayload = action.payload
      })
      .addCase(deleteProductionParticipantUser.fulfilled, (state, action) => {
        state.loading = false;
        state.productionParticipantDeleteSuccess = true
      })
      .addCase(postAddDiscussionRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.addDiscussionSuccess = true
        state.postAddDiscussionPayload = action.payload
      })
      .addCase(getParticipantsList.fulfilled, (state, action) => {
        state.loading = false;
        state.assignUserSuccess = false
        state.productionParticipantDeleteSuccess = false
        state.participantList = action.payload.data;
      })
      .addMatcher(isPending(postAddDiscussionRequest, updateProductionDiscussionStatus, postProductionReadyFilesUpload, postAssignUserProduction, getParticipantsList, deleteProductionParticipantUser, getProductionList), state => {
        state.loading = true;
        state.errorMessage = null;
        state.assignUserSuccess = false
      })
      .addMatcher(isRejected(postAddDiscussionRequest, updateProductionDiscussionStatus, postProductionReadyFilesUpload, postAssignUserProduction, getParticipantsList, deleteProductionParticipantUser), (state, action) => {
        state.loading = false;
        state.errorMessage = action.error.message;
      })
  },
});

export const {reset, stateFalse} = ProductionSlice.actions;

export default ProductionSlice.reducer;
