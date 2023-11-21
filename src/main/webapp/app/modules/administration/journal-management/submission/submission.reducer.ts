import axios from 'axios';
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {COPY_EDITING, PRODUCTION, SUBMISSION} from "app/config/env";
import {SubmissionDetailsModel} from "app/config/submissions.config";


const initialState = {
  loading: false,
  errorMessage: null,
  assignUserSuccess: false,
  fileUploadSubmissionReadyFilesSuccess: false,
  addDiscussionSuccess: false,
  submissionDetails: <SubmissionDetailsModel>{},
  participantDeleteSuccess: false,
  addChatDiscussionSuccess: false,
  addCompletedFileSuccess: false,
  postAssignUserSuccessPayload: [],
  postAddDiscussionChatPayload: {},
  postAddDiscussionPayload: {},
  postCompletedFilePayload: [],
  postUploadFilePayload: [],
  postAcceptFileAndMoveReviewSuccess: false,
  updateDiscussStatusSuccess: false,
  deleteSubmissionFileSuccess: false,
  moveToSubmissionSuccess: false
};


export const postAddDiscussionRequest = createAsyncThunk(
  'submission/postAddSubmissionDiscussionRequest',
  async (requestData: { formData, submissionId }) => {
    const response = await axios.post<any>(`${SUBMISSION.submissionDiscussionAdd}?submissionId=${requestData.submissionId}`, requestData.formData);
    return response.data;
  }
);

export const postAcceptFileAndMoveReview = createAsyncThunk(
  'submission/postAcceptFileAndMoveReview',
  async (requestData: { selectedFinalFileList, submissionId }) => {
    const response = await axios.post<any>(`${SUBMISSION.acceptSubmissionAndMoveReview}?submissionId=${requestData.submissionId}`, requestData.selectedFinalFileList);
    return response.data;
  }
);

export const postAddChatDiscussionRequest = createAsyncThunk(
  'production/postAddChatDiscussionRequest',
  async (requestData: { formData, submissionId, discussionId }) => {
    const response = await axios.post<any>(`${SUBMISSION.submissionDiscussionAddChat}?submissionId=${requestData.submissionId}&discussionId=${requestData.discussionId}`, requestData.formData);
    return response.data;
  }
);

export const updateDiscussStatus = createAsyncThunk(
  'submission/putUpdateDiscussionStatusRequest',
  async (requestData: { submissionId, discussionId, status }) => {
    const response = await axios.put<any>(`${SUBMISSION.SubmissionUpdateStatus}?submissionId=${requestData.submissionId}&discussionId=${requestData.discussionId}&status=${requestData.status}`);
    return response.data;
  }
);

export const deleteSubmissionFile = createAsyncThunk(
  'submission/deleteSubmissionRequest',
  async (requestData: { submissionId, submissionFileId }) => {
    const response = await axios.delete<any>(`${SUBMISSION.SubmissionDeleteFile}?submissionId=${requestData.submissionId}&submissionFileId=${requestData.submissionFileId}`);
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

export const postAssignUserSubmission = createAsyncThunk('submission/postAssignUserSubmission', async (data: {
  userIds: string[];
  description: string,
  submissionId: number
}) => {
  const requestBody = {
    userIds: data.userIds,
    description: data.description
  }
  const response = await axios.post(`${SUBMISSION.assignUser}?submissionId=${data.submissionId}`, requestBody);
  return response.data;
});


export const postSubmissionReadyFilesUpload = createAsyncThunk(
  'submission/postSubmissionReadyFilesUpload',
  async (requestData: { formData, submissionId }) => {
    const response = await axios.post<any>(`${SUBMISSION.SubmissionReadyFiles}?submissionId=${requestData.submissionId}`, requestData.formData);
    return response.data;
  }
);

export const putUploadRevision = createAsyncThunk(
  'submission/putUploadRevision',

  async (requestData: { formData, submissionId }) => {

    const response = await axios.put<any>(`${SUBMISSION.SubmissionUploadRevision}?submissionId=${requestData.submissionId}`, requestData.formData);
    return response.data;
  }
);

export const getParticipantsListRaw = async (submissionId?: number) => {
  return axios.get<any>(`${SUBMISSION.assignUsersList}?submissionId=${submissionId}`)
};

export const getParticipantsList = createAsyncThunk('journalManagement/getParticipantsList', async (submissionId?: number) => {
  return axios.get<any>(`${SUBMISSION.assignUsersList}?submissionId=${submissionId}`)
});

export const getSubmissionDiscussionListRaw = async (submissionId?: number) => {
  return axios.get<any>(`${SUBMISSION.submissionDiscussionAdd}s?submissionId=${submissionId}`)
};

export const getSubmissionDetails = createAsyncThunk('journalManagement/getSubmissionDetails', async (submissionId?: number) => {
  return axios.get<any>(`${SUBMISSION.getSubmissionListDetails}?submissionId=${submissionId}`)
});

export const deleteParticipantUser = createAsyncThunk('journalManagement/deleteParticipantUser', async (paramsDetails: {
  submissionId,
  contributorId
}) => {
  return axios.delete<any>(`${SUBMISSION.deleteParticipantUser}?submissionId=${paramsDetails.submissionId}&contributorId=${paramsDetails.contributorId}`);
});

export const downloadSubmissionFile = async (submissionId?: number) => {
  return axios.get<ArrayBuffer>(`${SUBMISSION.SubmissionDownloadFile}?submissionId=${submissionId}`, {
    responseType: 'arraybuffer',
  });
};

export const movetoProduction = createAsyncThunk('submission/movetoProduction', async (data: {
  from: string;
  submissionId: number,
  file: string[],
  skipReviewMail: string
}) => {
  const requestBody = {
    fromWorkflowStage: data.from,
    fileIds: data.file,
    skipReviewMail: data.skipReviewMail
  }

  const response = await axios.post(`${COPY_EDITING.SubmissionMoveToCopyEditing}?submissionId=${data.submissionId}`, requestBody);
  return response.data;
});

export type SubmissionState = Readonly<typeof initialState>;

export const SubmissionSlice = createSlice({
  name: 'submission',
  initialState: initialState as SubmissionState,
  reducers: {
    reset() {
      return initialState;
    },
    stateFalse(state) {
      state.assignUserSuccess = false
      state.participantDeleteSuccess = false
      state.addChatDiscussionSuccess = false
      state.postAcceptFileAndMoveReviewSuccess = false
      state.fileUploadSubmissionReadyFilesSuccess = false
      state.moveToSubmissionSuccess = false

    }
  },
  extraReducers(builder) {
    builder
      .addCase(movetoProduction.fulfilled, (state) => {
        state.loading = false;
        state.moveToSubmissionSuccess = true;
      })
      .addCase(postAssignUserSubmission.fulfilled, (state, action) => {
        state.loading = false;
        state.assignUserSuccess = true;
        state.postAssignUserSuccessPayload = action.payload;
      })
      .addCase(postUploadCompletedFileRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.addCompletedFileSuccess = true;
        state.postCompletedFilePayload = action.payload;
      })
      .addCase(postSubmissionReadyFilesUpload.fulfilled, (state, action) => {
        state.loading = false;
        state.fileUploadSubmissionReadyFilesSuccess = true;
        state.postUploadFilePayload = action.payload;
      })
      .addCase(getSubmissionDetails.fulfilled, (state, action) => {
        state.loading = false;
        state.submissionDetails = action.payload.data;
        state.updateDiscussStatusSuccess = false;
        state.deleteSubmissionFileSuccess = false;
      })
      .addCase(deleteSubmissionFile.fulfilled, (state) => {
        state.loading = false;
        state.deleteSubmissionFileSuccess = true;
      })
      .addCase(postAddChatDiscussionRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.addChatDiscussionSuccess = true;
        state.postAddDiscussionChatPayload = action.payload;
      })
      .addCase(postAcceptFileAndMoveReview.fulfilled, (state) => {
        state.loading = false;
        state.postAcceptFileAndMoveReviewSuccess = true;
      })
      .addCase(deleteParticipantUser.fulfilled, (state) => {
        state.loading = false;
        state.participantDeleteSuccess = true;
      })
      .addCase(postAddDiscussionRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.addDiscussionSuccess = true;
        state.postAddDiscussionPayload = action.payload;
      })
      .addCase(getParticipantsList.fulfilled, (state) => {
        state.loading = false;
        state.assignUserSuccess = false;
        state.participantDeleteSuccess = false;
      })
      .addCase(updateDiscussStatus.fulfilled, (state) => {
        state.loading = false;
        state.updateDiscussStatusSuccess = true;
      })
      .addMatcher(isPending(postAddChatDiscussionRequest, movetoProduction, postAddDiscussionRequest, deleteSubmissionFile, postAcceptFileAndMoveReview, postSubmissionReadyFilesUpload, postAssignUserSubmission, getParticipantsList, deleteParticipantUser, getSubmissionDetails, updateDiscussStatus), state => {
        state.loading = true;
        state.errorMessage = null;
        state.assignUserSuccess = false;
        state.participantDeleteSuccess = false;
      })
      .addMatcher(isRejected(postAddChatDiscussionRequest, movetoProduction, postAddDiscussionRequest, deleteSubmissionFile, postAcceptFileAndMoveReview, postSubmissionReadyFilesUpload, postAssignUserSubmission, getParticipantsList, getSubmissionDetails, updateDiscussStatus), (state, action) => {
        state.loading = false;
        state.errorMessage = action.error.message;
      })
  },
});

export const {reset, stateFalse} = SubmissionSlice.actions;

export default SubmissionSlice.reducer;
