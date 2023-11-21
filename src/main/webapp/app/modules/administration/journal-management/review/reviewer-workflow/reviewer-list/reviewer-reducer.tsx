import axios from "axios";
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {JOURNAL_SETTINGS, Review, Reviewer} from "app/config/env";
import {serializeAxiosError} from "app/shared/reducers/reducer.utils";


const initialState = {
  loading: false,
  errorMessage: null,
  reviewerSubmissionDetails: [],
  getReviewRoundContributorDetails: [],
  getDiscussionReviewerDetails: [],
  reviewerSubmissionDetailsTotalItems: 0,
  reviewerSubmissionSingleDetails: {},
  acceptRequestForReviewerSuccess: false,
  declineRequestForReviewerSuccess: false,
  completeReviewForReviewerSuccess: false,
  reviewerReplyUploadFileSuccess: false,
  getReviewRoundContributorSuccess: false,
  getDiscussionReviewerSuccess: false,
  addDiscussionReviewerSuccess: false,
  addDiscussionChatReviewerSuccess : false
};


export const getReviewerSubmissionDetails = createAsyncThunk(
  'reviewer/getReviewerSubmissionDetails',
  async (reviewRoundReviewId: number) => {
    return axios.get<any>(`${Reviewer.getReviewerSubmissionSingleURL}?reviewRoundReviewId=${reviewRoundReviewId}`);
  }
);

export const getReviewerReplyFilesRequest = async (reviewRoundReviewerId: number, reviewRoundId: number) => {
  return axios.get<any>(`${Reviewer.getReviewerReplyFiles}?reviewRoundReviewerId=${reviewRoundReviewerId}&reviewRoundId=${reviewRoundId}`)
};

export const getReviewerSubmissionListPagination = createAsyncThunk('reviewer/getReviewerSubmissionListPagination', async (data: {
  page,
  size,
  sort,
  searchText,
  status
}) => {
  const requestUrl = `${Reviewer.getReviewerSubmissionURL}${
    data.sort
      ? `?status=${data.status}&page=${data.page}&size=${data.size}&sort=${data.sort}&searchText=${data.searchText}`
      : `?status=${data.status}&page=${data.page}&size=${data.size}&sort=createdAt,desc&searchText=${data.searchText}`
  }`;

  return axios.get<any>(requestUrl);
});

export const acceptRequestForReviewer = createAsyncThunk('reviewer/acceptRequestForReviewer',
  async (reviewRoundReviewerId ?: number) => {
    const Req_Url = `${Reviewer.acceptReviewerRequest}?reviewRoundReviewerId=${reviewRoundReviewerId}`;
    return axios.put<any>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);

export const reviewerReplyUploadFile = createAsyncThunk('reviewer/reviewerReplyUploadFile',
  async (data: { reviewRoundReviewerId, reviewRoundId, formData }) => {
    const Req_Url = `${Reviewer.reviewerReplyFileUpload}?reviewRoundReviewerId=${data.reviewRoundReviewerId}&reviewRoundId=${data.reviewRoundId}`;
    return axios.post<any>(Req_Url, data.formData);
  },
  {serializeError: serializeAxiosError}
);

export const postAddDiscussionReviewerRequest = createAsyncThunk(
  'reviewer/postAddSubmissionDiscussionReviewerRequest',
  async (requestData: { formData, submissionId, reviewRoundId, reviewRoundReviewerId }) => {
    const response = await axios.post<any>(`${Reviewer.reviewerDiscussionAdd}?submissionId=${requestData.submissionId}&reviewRoundId=${requestData.reviewRoundId}&reviewRoundReviewerId=${requestData.reviewRoundReviewerId}`, requestData.formData);
    return response.data;
  }
);

export const postAddDiscussionChatReviewerRequest = createAsyncThunk(
  'reviewer/postAddDiscussionChatReviewerRequest',
  async (requestData: { formData, submissionId, reviewRoundId, discussionId }) => {
    const response = await axios.post<any>(`${Reviewer.reviewerDiscussionChatAdd}?submissionId=${requestData.submissionId}&reviewRoundId=${requestData.reviewRoundId}&discussionId=${requestData.discussionId}`, requestData.formData);
    return response.data;
  }
);

export const getDiscussionReviewerRequest = createAsyncThunk(
  'reviewer/getDiscussionReviewerRequest',
  async (requestData: { reviewRoundReviewerId, reviewRoundId }) => {
    return await axios.get<any>(`${Reviewer.getReviewerDiscussion}?reviewRoundId=${requestData.reviewRoundId}&reviewRoundReviewerId=${requestData.reviewRoundReviewerId}`);
  }
);

export const getDiscussionReviewerRequestRaw = async (requestData: { reviewRoundReviewerId, reviewRoundId }) => {
  return axios.get<any>(`${Reviewer.getReviewerDiscussion}?reviewRoundId=${requestData.reviewRoundId}&reviewRoundReviewerId=${requestData.reviewRoundReviewerId}`)
};

export const updateReviewerDiscussionStatus = createAsyncThunk(
  'submission/putUpdateReviewerDiscussionStatusRequest',
  async (requestData: { discussionId, reviewRoundId, reviewRoundReviewerId, status }) => {
    const response = await axios.put<any>(`${Reviewer.putReviewerDiscussionStatus}?discussionId=${requestData.discussionId}&reviewRoundId=${requestData.reviewRoundId}&reviewRoundReviewerId=${requestData.reviewRoundReviewerId}&status=${requestData.status}`);
    return response.data;
  }
);

export const declineRequestForReviewer = createAsyncThunk('reviewer/declineRequestForReviewer',
  async (data: { reviewRoundReviewerId: number, description: string }) => {
    const Req_Url = `${Reviewer.declineReviewerRequest}?reviewRoundReviewerId=${data.reviewRoundReviewerId}&rejectReason=${data.description}`;
    return axios.put<any>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);

export const getReviewRoundContributor = createAsyncThunk(
  'reviewer/getReviewRoundContributor',
  async (reviewRoundId: number) => {
    return axios.get<any>(`${Review.getReviewRoundContributors}?reviewRoundId=${reviewRoundId}`);
  }
);

export const completeReviewForReviewer = createAsyncThunk('reviewer/completeReviewForReviewer',
  async (data: {
    reviewRoundReviewerId: number,
    commentToEditorAndAuthor: string,
    commentToEditor: string,
    reviewerRecommendation: string
  }) => {
    const Req_Url = `${Reviewer.completeReviewerRequest}?reviewRoundReviewerId=${data.reviewRoundReviewerId}&commentToEditorAndAuthor=${data.commentToEditorAndAuthor}&commentToEditor=${data.commentToEditor}&reviewerRecommendation=${data.reviewerRecommendation}`;
    return axios.put<any>(Req_Url);
  },
  {serializeError: serializeAxiosError}
);


export type reviewerWorkflowState = Readonly<typeof initialState>;
export const ReviewerSlice = createSlice({
  name: "reviewer",
  initialState: initialState as reviewerWorkflowState,
  reducers: {
    stateReviewerFalse(state) {
      state.acceptRequestForReviewerSuccess = false;
      state.reviewerReplyUploadFileSuccess = false;
      state.getReviewRoundContributorSuccess = false;
      state.addDiscussionReviewerSuccess = false;
      state.addDiscussionChatReviewerSuccess = false;
    }
  },
  extraReducers(builder) {
    builder
      .addCase(getReviewerSubmissionDetails.fulfilled, (state, action) => {
        state.loading = false;
        state.reviewerSubmissionSingleDetails = action.payload.data;
        state.acceptRequestForReviewerSuccess = false;
        state.declineRequestForReviewerSuccess = false;
        state.completeReviewForReviewerSuccess = false;
      })
      .addCase(getReviewerSubmissionListPagination.fulfilled, (state, action) => {
        state.loading = false;
        state.reviewerSubmissionDetails = action.payload.data;
        state.reviewerSubmissionDetailsTotalItems = parseInt(action.payload.headers['x-total-count'], 10);
        state.completeReviewForReviewerSuccess = false;
      })
      .addCase(acceptRequestForReviewer.fulfilled, (state) => {
        state.loading = false;
        state.acceptRequestForReviewerSuccess = true;
      })
      .addCase(declineRequestForReviewer.fulfilled, (state) => {
        state.loading = false;
        state.declineRequestForReviewerSuccess = true;
      })
      .addCase(completeReviewForReviewer.fulfilled, (state) => {
        state.loading = false;
        state.completeReviewForReviewerSuccess = true;
      })
      .addCase(reviewerReplyUploadFile.fulfilled, (state) => {
        state.loading = false;
        state.reviewerReplyUploadFileSuccess = true;
      })
      .addCase(getReviewRoundContributor.fulfilled, (state, action) => {
        state.loading = false;
        state.getReviewRoundContributorSuccess = true;
        state.getReviewRoundContributorDetails = action.payload.data;
      })
      .addCase(getDiscussionReviewerRequest.fulfilled, (state, action) => {
        state.loading = false;
        state.getDiscussionReviewerSuccess = true;
        state.getDiscussionReviewerDetails = action.payload.data;
        state.addDiscussionReviewerSuccess = false;
      })
      .addCase(postAddDiscussionReviewerRequest.fulfilled, (state) => {
        state.loading = false;
        state.addDiscussionReviewerSuccess = true
      })
      .addCase(postAddDiscussionChatReviewerRequest.fulfilled, (state) => {
        state.loading = false;
        state.addDiscussionChatReviewerSuccess = true
      })

      .addMatcher(isPending(reviewerReplyUploadFile, getReviewRoundContributor, postAddDiscussionChatReviewerRequest, getDiscussionReviewerRequest, postAddDiscussionReviewerRequest, completeReviewForReviewer, getReviewerSubmissionDetails, getReviewerSubmissionListPagination, acceptRequestForReviewer, declineRequestForReviewer),
        state => {
          state.loading = true;
          state.errorMessage = null;
        }
      )
      .addMatcher(isRejected(reviewerReplyUploadFile, getReviewRoundContributor, postAddDiscussionChatReviewerRequest, getDiscussionReviewerRequest, postAddDiscussionReviewerRequest, completeReviewForReviewer, getReviewerSubmissionDetails, getReviewerSubmissionListPagination, acceptRequestForReviewer, declineRequestForReviewer),
        state => {
          state.loading = false;
          state.errorMessage = null;
        }
      );
  }
})


export const {stateReviewerFalse} = ReviewerSlice.actions;
export default ReviewerSlice.reducer;



