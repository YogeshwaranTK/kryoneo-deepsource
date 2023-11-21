import axios from 'axios';
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {COPY_EDITING, Review} from "app/config/env";


const initialState = {
  loading: false,
  errorMessage: null,
  assignUserSuccess: false,
  fileUploadCopyEditingReadyFilesSuccess: false,
  postCopyEditingFilesUploadSuccessPayload: [],
  participantList: [],
  addDiscussionSuccess: false,
  copyEditingDetails: [],
  copyEditedFilesDetails: [],
  participantDeleteSuccess: false,
  addChatDiscussionSuccess: false,
  addCompletedFileSuccess: false,
  updateDiscussStatusSucess: false,
  postAssignUserSuccessPayload: [],
  postAddDiscussionChatPayload: {},
  postAddDiscussionPayload: {},
  postCompletedFilePayload: [],
  fileUploadCopyEditedFileSuccess: false,
  postfileUploadCopyEditedFilePayload:[],
  moveToProductionSuccess:false,
  moveToProductionData:[]
};


export const postAddDiscussionRequest = createAsyncThunk(
  'copy-editing/postAddDiscussionRequest',
  async (requestData: { formData, submissionId }) => {
    const response = await axios.post<any>(`${COPY_EDITING.CopyEditingDiscussionAdd}?submissionId=${requestData.submissionId}`, requestData.formData);
    return response.data;
  }
);

export const postAddChatDiscussionRequest = createAsyncThunk(
  'copy-editing/postAddChatDiscussionRequest',
  async (requestData: { formData, submissionId,discussionId }) => {
    const response = await axios.post<any>(`${COPY_EDITING.copyEditingDiscussionAddChat}?submissionId=${requestData.submissionId}&discussionId=${requestData.discussionId}`, requestData.formData);
    return response.data;
  }
);

// export const postUploadCopyEditingFileRequest = createAsyncThunk(
//   'copy-editing/postUploadCopyEditingFileRequest',
//   async (requestData: { formData, submissionId }) => {
//     const response = await axios.post<any>(`${COPY_EDITING.CopyEditingCompletedFilesPost}?submissionId=${requestData.submissionId}`, requestData.formData);
//     return response.data;
//   }
// );

export const getCopyEditedFilesRequest = createAsyncThunk(
  'copy-editing/getCopyEditedFilesRequest',
  async (submissionId:number ) => {
    const response = await axios.get<any>(`${COPY_EDITING.CopyEditedFiles}?submissionId=${submissionId}`);
    return response;
  }
);



export const postAssignUserCopyEditing = createAsyncThunk('copy-editing/postAssignUserCopyEditing', async (datas: {
  userIds: string[];
  description: string,
  submissionId: number
}) => {
  const requestBody = {
    userIds: datas.userIds,
    description: datas.description
  }
  const response = await axios.post(`${COPY_EDITING.assignUser}?submissionId=${datas.submissionId}`, requestBody);
  return response.data;
});

export const postCopyEditingReadyFilesUpload = createAsyncThunk(
  'copy-editing/postCopyEditingReadyFilesUpload',
  async (requestData: { formData, submissionId }) => {
    return await axios.post<any>(`${COPY_EDITING.CopyEditedFileUpload}?submissionId=${requestData.submissionId}`, requestData.formData)
  }
);



export const postCopyEditingEditedFileUpload = createAsyncThunk(
  'copy-editing/postCopyEditingEditedFileUpload',
  async (requestData: { formData, submissionId }) => {
    return await axios.post<any>(`${COPY_EDITING.CopyEditingReadyFiles}?submissionId=${requestData.submissionId}`, requestData.formData)
  }
);


// export const postAddDiscussionChat = createAsyncThunk(
//   'copy-editing/postAddDiscussionChat',
//   async (requestData: { submissionId?: number, discussionId: number }) => {
//     await axios.post<any>(`${COPY_EDITING.productionDiscussionAdd}/${requestData.discussionId}/chat?submissionId=${requestData.submissionId}`),
//       {serializeError: serializeAxiosError}
//   }
// );

export const getCopyEditingDiscussionListRaw = async (submissionId?: number) => {
  return axios.get<any>(`${COPY_EDITING.CopyEditingDiscussionAdd}s?submissionId=${submissionId}`)
};

export const getDiscussionChatRequest = async (requestData: { submissionId?: number, discussionId: number }) => {
  return axios.get<any>(`${COPY_EDITING.copyEditingDiscussionAddChat}?submissionId=${requestData.submissionId}&discussionId=${requestData.discussionId}`)
};

export const getParticipantsListRaw = async (submissionId?: number) => {
  return axios.get<any>(`${COPY_EDITING.assignUsersList}?submissionId=${submissionId}`)
};

// export const getParticipantsList = createAsyncThunk('journalManagement/getParticipantsList', async (submissionId?: number) => {
//   return axios.get<any>(`${COPY_EDITING.assignUsersList}?submissionId=${submissionId}`)
// });

export const getCopyEditingList = createAsyncThunk('journalManagement/getCopyEditingList', async (submissionId?: number) => {
  return axios.get<any>(`${COPY_EDITING.getCopyEditingListDetails}?submissionId=${submissionId}`)
});

export const getCopyEditingReadyFilesListRaw = async (submissionId?: number) => {
  return axios.get<any>(`${COPY_EDITING.CopyEditingReadyFilesGet}?submissionId=${submissionId}`)
};

export const fileDownloadUrlRequest = async (requestUrl) => {
  return axios.get<any>(requestUrl, {responseType: 'blob'})
};

export const deleteParticipantUser = createAsyncThunk('journalManagement/deleteParticipantUser', async (paramsDetails: {
  submissionId,
  contributorId
}) => {
  return axios.delete<any>(`${COPY_EDITING.deleteParticipantUser}?submissionId=${paramsDetails.submissionId}&contributorId=${paramsDetails.contributorId}`);
});

export const updateDiscussStatus = createAsyncThunk(
  'review/putUpdateDiscussionStatusRequest',
  async (requestData: { submissionId,discussionId,status }) => {
    const response = await axios.put<any>(`${COPY_EDITING.CopyEditingUpdateStatus}?submissionId=${requestData.submissionId}&discussionId=${requestData.discussionId}&status=${requestData.status}`);
    return response.data;
  }
);


export const movetoProduction = createAsyncThunk('review/movetoproduction', async (datas: {
  from: string;
  submissionId: number,
  file:string[],
  skipReviewMail:string,
}) => {
  const requestBody = {
    fromWorkflowStage: datas.from,
    fileIds: datas.file,
    skipReviewMail:datas.skipReviewMail,
  }

  const response = await axios.post(`${COPY_EDITING.CopyEditingMoveToProduction}?submissionId=${datas.submissionId}`, requestBody);
  return response.data;
});




export type CopyEditingState = Readonly<typeof initialState>;

export const CopyEditingSlice = createSlice({
  name: 'copyEditing',
  initialState: initialState as CopyEditingState,
  reducers: {
    reset() {
      return initialState;
    },
    stateFalse(state) {
      state.assignUserSuccess = false
      state.participantDeleteSuccess = false
      state.addChatDiscussionSuccess = false
      state.addDiscussionSuccess = false
      state.fileUploadCopyEditingReadyFilesSuccess = false
      state.fileUploadCopyEditedFileSuccess = false
      state.moveToProductionSuccess = false

    }
  },
  extraReducers(builder) {
    builder

    .addCase(movetoProduction.fulfilled,(state,action) =>{
      state.loading = false;
      state.moveToProductionSuccess = true;
      state.moveToProductionData = action.payload
    })

    .addCase(postCopyEditingEditedFileUpload.fulfilled, (state, action) => {
      state.loading = false;
      state.fileUploadCopyEditedFileSuccess = true;
      state.postfileUploadCopyEditedFilePayload = action.payload.data
    })

      .addCase(postAssignUserCopyEditing.fulfilled, (state, action) => {
        state.loading = false;
        state.assignUserSuccess = true;
        state.postAssignUserSuccessPayload = action.payload
      })
      .addCase(postCopyEditingReadyFilesUpload.fulfilled, (state, action) => {
        state.loading = false;
        state.fileUploadCopyEditingReadyFilesSuccess = true;
        state.postCopyEditingFilesUploadSuccessPayload = action.payload.data
      })
      .addCase(postAddDiscussionRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.addDiscussionSuccess = true
        state.postAddDiscussionPayload = action.payload
      })
      .addCase(postAddChatDiscussionRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.addChatDiscussionSuccess = true
        state.postAddDiscussionChatPayload = action.payload
      })

      .addCase(getCopyEditingList.fulfilled, (state, action) => {
        state.loading = false;
        state.assignUserSuccess = false
        state.participantDeleteSuccess = false
        state.copyEditingDetails = action.payload.data;
        state.updateDiscussStatusSucess = false
      })

      .addCase(updateDiscussStatus.fulfilled, (state, action) => {
        state.loading = false;
        state.updateDiscussStatusSucess = true
      })



      .addCase(getCopyEditedFilesRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.assignUserSuccess = false
        state.copyEditedFilesDetails = action.payload.data;
      })
      .addMatcher(isPending(movetoProduction,postCopyEditingEditedFileUpload,postAssignUserCopyEditing,updateDiscussStatus, postAddDiscussionRequest, postCopyEditingReadyFilesUpload, getCopyEditingList, getCopyEditedFilesRequest), state => {
        state.loading = true;
        state.errorMessage = null;
        state.assignUserSuccess = false
        state.participantDeleteSuccess = false
      })
      .addMatcher(isRejected(movetoProduction,postCopyEditingEditedFileUpload,postAssignUserCopyEditing, postAddDiscussionRequest,updateDiscussStatus, postCopyEditingReadyFilesUpload, getCopyEditedFilesRequest), (state, action) => {
        state.loading = false;
        state.errorMessage = action.error.message;
      })
  },
});

export const {reset, stateFalse} = CopyEditingSlice.actions;

export default CopyEditingSlice.reducer;
