import axios from "axios";
import { createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import {COPY_EDITING, Review} from "app/config/env";


const initialState = {
  loading: false,
  reviewRound: null,
  errorMessage: null,
  CreateReviewSuccess: false,
  ReviewRoundData: [],
  reviewRoundDetails: [],
  fileUploadReviewReadyFilesSuccess: false,
  fileuploadReview: [],
  assignUserSuccess: false,
  postAssignUserSuccessPayload: [],
  addDiscussionSuccess: false,
  reviewAddDiscussionPayload: {},
  addChatDiscussionSuccess: false,
  reviewAddDiscussionChatPayload: {},
  participantDeleteSuccess: false,
  addReviewSuccess: false,
  reviewAddReviewerPayload: {},
  updateDiscussStatusSucess: false,
  moveToReviewData : [],
  moveToReveiewSuccess: false
};


export const createRoundreview = createAsyncThunk('review/CreateReviewRound',
  async (data?: any) => {
    const Req_Url = `${Review.createRound}?submissionId=${data.submissionId}`;
    return axios.post<any>(Req_Url, data);
  },
  { serializeError: serializeAxiosError }
);


export const postReviewRoundFilesUpload = createAsyncThunk(
  'review/postReviewReadyFilesUpload',
  async (requestData: { formData: FormData; submissionId: string; reviewRoundId: string }) => {
    const response = await axios.post<any>(
      `${Review.ReviewReadyFiles}?submissionId=${requestData.submissionId}&reviewRoundId=${requestData.reviewRoundId}`,
      requestData.formData
    );

    return response;
  }
);

export const updateDiscussStatus = createAsyncThunk(
  'review/putUpdateDiscussionStatusRequest',
  async (requestData: { reviewRoundId,discussionId,status }) => {
    const response = await axios.put<any>(`${Review.reviewUpdateStatus}?reviewRoundId=${requestData.reviewRoundId}&discussionId=${requestData.discussionId}&status=${requestData.status}`);
    return response.data;
  }
);

export const cancelReviewerStatus = createAsyncThunk(
  'review/putCancelReviewerRequest',
  async (requestData:{reviewRoundId,userId}) => {
    const response = await axios.put<any>(`${Review.cancelUpdateStatus}?reviewRoundId=${requestData.reviewRoundId}&userId=${requestData.userId}`);
    return response.data;
  }
);


export const fileDownloadUrlRequest = async (requestUrl) => {
  return axios.get<any>(requestUrl, {responseType: 'blob'})
};



export const getRoundreview = createAsyncThunk(
  'review/getRoundreview',
  async (data: { submissionId: number }) => axios.get<any>(`${Review.review_get}?submissionId=${data.submissionId}`),
  { serializeError: serializeAxiosError }
);




export const getReviewrRoundList = createAsyncThunk(
  'review/getReviewrRoundList',
  async ({ submissionId, reviewRoundId }: { submissionId?: any; reviewRoundId?: any }) => {
    return axios.get<any>(`${Review.GetReviewRound}?submissionId=${submissionId}&reviewRoundId=${reviewRoundId}`);
  }
);

// add reviewer participate
export const postAssignUserReview = createAsyncThunk('review/postAssignUserReview', async (datas: {
  userIds: string[];
  description: string,
  submissionId: number,
  reviewRoundId: number
}) => {
  const requestBody = {
    userIds: datas.userIds,
    description: datas.description
  }
  const response = await axios.post(`${Review.assignUser}?submissionId=${datas.submissionId}&reviewRoundId=${datas.reviewRoundId}`, requestBody);
  return response.data;
});

// add reviewe discussion
export const reviewAddDiscussionRequest = createAsyncThunk(
  'review/reviewAddDiscussionRequest',
  async (requestData: { formData, submissionId,reviewRoundId }) => {
    const response = await axios.post<any>(`${Review.reviewDiscussionAdd}?submissionId=${requestData.submissionId}&reviewRoundId=${requestData.reviewRoundId}`, requestData.formData);
    return response.data;
  }
);

// add review chat
export const reviewAddChatDiscussionRequest = createAsyncThunk(
  'review/reviewAddChatDiscussionRequest',
  async (requestData: { formData, submissionId,discussionId,reviewRoundId }) => {
    const response = await axios.post<any>(`${Review.reviewDiscussionAddChat}?submissionId=${requestData.submissionId}&discussionId=${requestData.discussionId}&reviewRoundId=${requestData.reviewRoundId}`, requestData.formData);
    return response.data;
  }
);

// get discussiondata
export const getReviewDiscussionListRaw = async (reviewRoundId?: string) => {
  return axios.get<any>(`${Review.reviewDiscussionAdd}?reviewRoundId=${reviewRoundId}`)
};



// participate delete
export const deleteParticipantUser = createAsyncThunk('review/deleteParticipantUser',
async (paramsDetails: { reviewRoundId, contributorId}) => {
  return axios.delete<any>(`${Review.deleteParticipantUser}?reviewRoundId=${paramsDetails.reviewRoundId}&contributorId=${paramsDetails.contributorId}`);
});

export const getParticipantsListRaw = async (submissionId?: number,reviewRoundId?: string) => {
  return axios.get<any>(`${Review.assignUsersList}?submissionId=${submissionId}&reviewRoundId=${reviewRoundId}`)
};



export const reviewerAdd = createAsyncThunk(
  'review/reviewerAdd',
  async (datas: {
    userId: string;
    message: string,
    submissionId: number,
    reviewRoundId: number,
    reviewDueDate: any,
    responseDueDate: any,
    submissionReviewType:string,
    reviewRoundFileIds:string[]
  }) => {
    const requestBody = {
      userId: datas.userId,
      message: datas.message,
      responseDueDate: datas.responseDueDate,
      reviewDueDate:datas.reviewDueDate,
      submissionReviewType: datas.submissionReviewType,
      reviewRoundId:datas.reviewRoundId,
      reviewRoundFileIds:datas.reviewRoundFileIds
    }
    const response = await axios.post<any>(`${Review.addReviewer}?submissionId=${datas.submissionId}`,requestBody);
    return response.data;
  }
);



export const movetoReview = createAsyncThunk('review/movetoProduction', async (datas: {
  from: string;
  submissionId: number,
  file:string[],
  skipReviewMail:string,
  reviewRoundId:number
}) => {
  const requestBody = {
    fromWorkflowStage: datas.from,
    fileIds: datas.file,
    skipReviewMail:datas.skipReviewMail,
    reviewRoundId:datas.reviewRoundId
  }

  const response = await axios.post(`${COPY_EDITING.SubmissionMoveToCopyEditing}?submissionId=${datas.submissionId}`, requestBody);
  return response.data;
});


export const downloadReviewerFile = async (submissionId?: number,reviewRoundId?: string) => {
  return axios.get<ArrayBuffer>(`${Review.ReviewDownloadFile}?submissionId=${submissionId}&reviewRoundId=${reviewRoundId}`, {
    responseType: 'arraybuffer',
  });
};

export type workwlowState = Readonly<typeof initialState>;
export const ReviewSlice = createSlice({
  name: "Review",
  initialState: initialState as workwlowState,
  reducers: {
    reviewreset(state) {
      state.CreateReviewSuccess = false
    },
    stateFalse(state) {
      state.assignUserSuccess = false
      state.addChatDiscussionSuccess = false
      state.participantDeleteSuccess = false
      state.postAssignUserSuccessPayload = []
      state.moveToReveiewSuccess = false
    }
  },
  extraReducers(builder) {
    builder





    .addCase(movetoReview.fulfilled,(state,action) =>{
      state.loading = false;
      state.moveToReveiewSuccess = true;
      state.moveToReviewData = action.payload
    })
    .addCase(deleteParticipantUser.fulfilled, (state, action) => {
        state.loading = false;
        state.participantDeleteSuccess = true
      })
      .addCase(postAssignUserReview.fulfilled, (state, action) => {
        state.loading = false;
        state.assignUserSuccess = true;
        state.postAssignUserSuccessPayload = action.payload
      })
      .addCase(createRoundreview.fulfilled, (state, action) => {
        state.loading = false;
        state.reviewRound = action.payload.data;
        state.CreateReviewSuccess = true
      })
      .addCase(reviewAddChatDiscussionRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.addChatDiscussionSuccess = true
        state.reviewAddDiscussionChatPayload = action.payload
      })
      .addCase(reviewAddDiscussionRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.addDiscussionSuccess = true
        state.reviewAddDiscussionPayload = action.payload
      })
      .addCase(getRoundreview.fulfilled, (state, action) => {
        state.loading = false;
        state.ReviewRoundData = action.payload.data;
        state.CreateReviewSuccess = false
      })

      .addCase(getReviewrRoundList.fulfilled, (state, action) => {
        state.loading = false;
        state.reviewRoundDetails = action.payload.data;
        state.updateDiscussStatusSucess = false
      })
      .addCase(updateDiscussStatus.fulfilled, (state, action) => {
        state.loading = false;
        state.updateDiscussStatusSucess = true
      })
      .addCase(postReviewRoundFilesUpload.fulfilled, (state, action) => {
        state.loading = false;
        state.fileuploadReview = action.payload.data
        state.fileUploadReviewReadyFilesSuccess = true
      })
      .addCase(reviewerAdd.fulfilled, (state, action) => {
        state.loading = false;
        state.reviewAddReviewerPayload = action.payload
        state.addReviewSuccess = true

      })

      .addMatcher(isPending(movetoReview,reviewerAdd,createRoundreview, getRoundreview, postReviewRoundFilesUpload,postAssignUserReview,getReviewrRoundList),
        state => {
          state.loading = true;
          state.errorMessage = null;
          state.assignUserSuccess = false;
          state.participantDeleteSuccess= false
        }
      )
      .addMatcher(isRejected(movetoReview,reviewerAdd,createRoundreview, getRoundreview, postReviewRoundFilesUpload,postAssignUserReview),
        state => {
          state.loading = false;
          state.errorMessage = null;
        }
      );
  }
})



export const { reviewreset,stateFalse } = ReviewSlice.actions;
export default ReviewSlice.reducer;



