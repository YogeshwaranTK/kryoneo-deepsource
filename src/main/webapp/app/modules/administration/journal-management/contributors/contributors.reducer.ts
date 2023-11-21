import axios from 'axios';
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {IQueryParams, serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {CONTRIBUTORS} from "app/config/env";

export interface JOURNAL_GROUP_MODEL {
  id: number;
  name: string;
  createdByName: string;
  createdDate: Date;
  lastModifiedByName: string;
  lastModifiedDate: Date;
  memberCount: number;
  journalId: number;
}

export interface JOURNAL_USER_MODEL {
  id: number;
  userId: string;
  userFullName: string;
  journalGroupName: string;
  userEmail: string;
  userName: string;
  email: string;
  createdDate: string;
}

export interface CREATE_GROUP_MODEL {
  name: string;
}

export interface DELETE_GROUP_MODEL {
  userId: string;
  deletedRemarks: string;
}

const initialState = {
  loading: false,
  errorMessage: null,
  journalGroup: <JOURNAL_GROUP_MODEL>(<any>[]),
  totalItems: 0,
  deletedGroup: false,
  createdGroup: false,
  updateGroup: false,
  journalUser: <JOURNAL_USER_MODEL>(<any>[]),
  userTotalItem: 0,
  deletedUserStatus: false,
  createdUser: false,
  JournalUserCreateStatus: false,
  ContributorGroupDetails: {},
  userGroupDeails: [],
  userGroupDeailstotalItems: 0,
  AddJournalUserGroupStatus: null,
  GroupUserDeleteStatus: false,
  assignAuthorRolePayloadPost: [],
  assignAuthorRolePostSuccess: false,
  authorDetailsTotalItems: 0,
  assignAuthorRolePayload: <any>[],
  deletedAuthorSuccess: false,
  assignReviewerRolePayloadPost: [],
  assignReviewerRolePayload: [],
  reviewerDetailsTotalItems: 0,
  assignReviewerRolePostSuccess: false,
  deletedReviewerSuccess: false,
  assignEditorialRolePayload: [],
  assignEditorialRolePostSuccess: false,
  deletedEditorialSuccess: false,
  editorialDetailsTotalItems: 0,
  assignEditorialRolePayloadPost: [],
  deletedEditorialStatusCode: 0,
  getEditorialListSuccess: false

};

export const getJournalRoles = async () => {
  return axios.get<any>(`${CONTRIBUTORS.getJournalRoles}`);
};


export const getAuthorRoleList = createAsyncThunk('journalContributors/getAuthorRoleList', async ({
                                                                                                    page,
                                                                                                    size,
                                                                                                    sort,
                                                                                                    searchText
                                                                                                  }: IQueryParams) => {
  const requestUrl = `${CONTRIBUTORS.contributionAddAuthorPost}s${
    sort ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}` : `?page=${page}&size=${size}&sort=id,desc&searchText=${searchText}`
  }`;
  return axios.get<any>(requestUrl);
});

export const getAuthorRoleListRaw = async ({page, size, sort, searchText}: IQueryParams) => {
  const requestUrl = `${CONTRIBUTORS.contributionAddAuthorPost}s${
    sort ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}` : `?page=${page}&size=${size}&sort=id,desc&searchText=${searchText}`
  }`;
  return axios.get<any>(requestUrl);
}


export const getReviewerRoleList = createAsyncThunk('journalContributors/getReviewerRoleList', async ({
                                                                                                        page,
                                                                                                        size,
                                                                                                        sort,
                                                                                                        searchText
                                                                                                      }: IQueryParams) => {
  const requestUrl = `${CONTRIBUTORS.contributionAddReviewerPost}s${
    sort ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}` : `?page=${page}&size=${size}&sort=id,desc&searchText=${searchText}`
  }`;
  return axios.get<any>(requestUrl);
});

export const getReviewerRoleListRaw = async ({page, size, sort, searchText}: IQueryParams) => {
  const requestUrl = `${CONTRIBUTORS.contributionAddReviewerPost}s${
    sort ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}` : `?page=${page}&size=${size}&sort=id,desc&searchText=${searchText}`
  }`;
  return axios.get<any>(requestUrl);
}


export const getEditorialRoleListRaw = async ({page, size, sort, searchText}: IQueryParams) => {
  const requestUrl = `${CONTRIBUTORS.contributionAddEditorialPost}${
    sort ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}` : `?page=${page}&size=${size}&sort=id,desc&searchText=${searchText}`
  }`;
  return axios.get<any>(requestUrl);
}

export const getEditorialRoleList = createAsyncThunk('journalContributors/getEditorialRoleList', async ({
                                                                                                          page,
                                                                                                          size,
                                                                                                          sort,
                                                                                                          searchText
                                                                                                        }: IQueryParams) => {
  const requestUrl = `${CONTRIBUTORS.contributionAddEditorialPost}${
    sort ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}` : `?page=${page}&size=${size}&sort=id,desc&searchText=${searchText}`
  }`;
  return axios.get<any>(requestUrl);
});


export const postAuthorRoleAdd = createAsyncThunk(
  'journalContributors/postAuthorRoleAdd',
  async (data: { userIds: any[] }) => {
    const response = await axios.post(`${CONTRIBUTORS.contributionAddAuthorPost}`, data);
    return response.data;
  }
);

export const postEditorialRoleAdd = createAsyncThunk(
  'journalContributors/postEditorialRoleAdd',
  async (data: {
    usersIds,
    existingRole,
    "roleName"
    submissionEnabled,
    reviewEnabled,
    copyEditingEnabled,
    productionEnabled
  }) => {
    const response = await axios.post(`${CONTRIBUTORS.contributionAddEditorialPost}`, data);
    return response.data;
  }
);

export const postReviewerRoleAdd = createAsyncThunk(
  'journalContributors/postReviewerRoleAdd',
  async (data: { userIds: any[] }) => {
    const response = await axios.post(`${CONTRIBUTORS.contributionAddReviewerPost}`, data);
    return response.data;
  }
);


export const ContributorGroupDetails = createAsyncThunk(
  'journalContributors/ContributorGroupDetails',
  async ({page, size, sort, groupId}: IQueryParams) => {
    const requestUrl = `api/v1/journal-group/journal-user/list${
      sort ? `?page=${page}&size=${size}&sort=${sort}&journalGroupId=${groupId}` : `?page=${page}&size=${size}&sort=id,desc&journalGroupId=${groupId}`
    }`;
    return axios.get<any>(requestUrl);
  }
);

export const ContributorGroupDetailsGet = createAsyncThunk(
  'journalContributors/ContributorGroupDetailsGet',
  async (data: { id: number }) => axios.get<any>(`api/v1/journal-group?id=${data.id}`),
  {serializeError: serializeAxiosError}
);

export const ContributorGroupUserDelete = createAsyncThunk(
  'journalContributors/ContributorGroupUserDelete',
  async (data: { id: any; deletedRemarks: string }) =>
    axios.post<any>(`api/v1/journal-group/journal-user/remove`, data),
  {serializeError: serializeAxiosError}
);

export const AddJournalUserGroup = createAsyncThunk(
  'journalContributors/AddJournalUserGroup',
  async (data: any) => axios.post<any>(`api/v1/journal-group/journal-user`, data),
  {
    serializeError: serializeAxiosError,
  }
);

export const UserAddRoleGroup = createAsyncThunk(
  'journalContributors/UserAddRoleGroup',
  async (data: any) => axios.post<any>(`api/v1/org-role/user/add`, data),
  {
    serializeError: serializeAxiosError,
  }
);

export const getJournalsGroup = createAsyncThunk(
  'journalContributors/getJournalsGroup',
  async ({page, size, sort, searchText}: IQueryParams) => {
    const requestUrl = `${CONTRIBUTORS.ContributorsGroupList}${
      sort
        ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}`
        : `?page=${page}&size=${size}&sort=createdAt,desc&searchText=${searchText}`
    }`;
    return axios.get<JOURNAL_GROUP_MODEL>(requestUrl);
  }
);

export const postJournalsGroup = createAsyncThunk(
  'journalContributors/postJournalsGroup',
  async (data: { name: string }) => axios.post<JOURNAL_GROUP_MODEL>(`${CONTRIBUTORS.ContributorsGroupCURD}`, data),
  {serializeError: serializeAxiosError}
);


export const UpdateJournalsGroup = createAsyncThunk(
  'journalContributors/UpdateJournalsGroup',
  async (data: any) => axios.put<JOURNAL_GROUP_MODEL>(`${CONTRIBUTORS.ContributorsGroupCURD}`, data),
  {serializeError: serializeAxiosError}
);

export const deleteJournalsGroup = createAsyncThunk(
  'journalContributors/delete',
  async (data: { id: number; deletedRemarks: string }) =>
    axios.delete<DELETE_GROUP_MODEL>(`${CONTRIBUTORS.ContributorsGroupCURD}?id=${data.id}&deletedRemarks=${data.deletedRemarks}`),
  {serializeError: serializeAxiosError}
);

export const deleteAssignedAuthor = createAsyncThunk(
  'journalContributors/deleteAssignedAuthor',
  async (data: { userId: string; deletedRemarks: string }) =>
    axios.delete<DELETE_GROUP_MODEL>(`${CONTRIBUTORS.contributionAddAuthorPost}?userId=${data.userId}&deletedRemarks=${data.deletedRemarks}`),
  {serializeError: serializeAxiosError}
);

export const deleteAssignedEditorial = createAsyncThunk(
  'journalContributors/deleteAssignedEditorial',
  async (data: { userId: string; deletedRemarks: string }) =>
    axios.delete<DELETE_GROUP_MODEL>(`${CONTRIBUTORS.contributionAddEditorialPost}?userId=${data.userId}&deletedRemarks=${data.deletedRemarks}`),
  {serializeError: serializeAxiosError}
);

export const deleteAssignedReviewer = createAsyncThunk(
  'journalContributors/deleteAssignedReviewer',
  async (data: { userId: string; deletedRemarks: string }) =>
    axios.delete<DELETE_GROUP_MODEL>(`${CONTRIBUTORS.contributionAddReviewerPost}?userId=${data.userId}&deletedRemarks=${data.deletedRemarks}`),
  {serializeError: serializeAxiosError}
);

export const GetJournalsGroup = async (groupId: string, journalId: number) => {
  return axios.get<any>(`${CONTRIBUTORS.ContributorsGroupCURD}?id=${groupId}&journalId=${journalId}`)
};

export const getuserSearchList = async (query: string, Jo_id: string) => {
  return axios.get<any>(`${CONTRIBUTORS.ContributorsUserSearchList}?page=0&size=20&journalId=${Jo_id}&searchText=${query}`);
};

export const getJournalsUserSearch = async ({page, size, searchText, journalId}: IQueryParams) => {
  const requestUrl = `${CONTRIBUTORS.ContributorsUserList}?page=${page}&size=${size}&sort=createdAt,desc&journalId=${journalId}&searchText=${searchText}`
  return axios.get<any>(requestUrl);
}

export const getJournalsUser = createAsyncThunk(
  'journalContributors/getJournalsUser',
  async ({page, size, sort, searchText, journalId}: IQueryParams) => {
    const requestUrl = `${CONTRIBUTORS.ContributorsUserList}${
      sort
        ? `?page=${page}&size=${size}&sort=${sort}&journalId=${journalId}&searchText=${searchText}`
        : `?page=${page}&size=${size}&sort=createdAt,desc&journalId=${journalId}&searchText=${searchText}`
    }`;
    return axios.get<JOURNAL_USER_MODEL>(requestUrl);
  }
);

export const journalUseradd = createAsyncThunk(
  'journalContributors/journalUseradd',
  async (data: any) => axios.post<any>(`${CONTRIBUTORS.ContributorsUserCURD}`, data),
  {serializeError: serializeAxiosError}
);

export const deleteJournalsUser = createAsyncThunk(
  'journalContributors/deleteJournalsUser',
  async (data: { id: number; deletedRemarks: string }) =>
    axios.delete<DELETE_GROUP_MODEL>(`${CONTRIBUTORS.ContributorsUserCURD}?roleId=${data.id}&deletedRemarks=${data.deletedRemarks}`),
  {serializeError: serializeAxiosError}
);

export type contributorsGroupState = Readonly<typeof initialState>;

export const contributorsGroupSlice = createSlice({
  name: 'journalContributors',
  initialState: initialState as contributorsGroupState,
  reducers: {
    reset() {
      return initialState;
    },
    resetStateBooleans(state) {
      state.deletedAuthorSuccess = false
      state.assignAuthorRolePostSuccess = false
      state.assignReviewerRolePostSuccess = false
      state.deletedReviewerSuccess = false
      state.assignEditorialRolePostSuccess = false
      state.deletedEditorialSuccess = false
    }

  },
  extraReducers(builder) {
    builder
      .addCase(postAuthorRoleAdd.fulfilled, (state, action) => {
        state.loading = false;
        state.assignAuthorRolePayloadPost = action.payload;
        state.assignAuthorRolePostSuccess = true
      })
      .addCase(postEditorialRoleAdd.fulfilled, (state, action) => {
        state.loading = false;
        state.assignEditorialRolePayloadPost = action.payload;
        state.assignEditorialRolePostSuccess = true
      })
      .addCase(postReviewerRoleAdd.fulfilled, (state, action) => {
        state.loading = false;
        state.assignReviewerRolePayloadPost = action.payload;
        state.assignReviewerRolePostSuccess = true
      })
      .addCase(getAuthorRoleList.fulfilled, (state, action) => {
        state.loading = false;
        state.assignAuthorRolePayload = action.payload.data;
        state.assignAuthorRolePostSuccess = false
        state.deletedAuthorSuccess = false
        state.authorDetailsTotalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addCase(getReviewerRoleList.fulfilled, (state, action) => {
        state.loading = false;
        state.assignReviewerRolePayload = action.payload.data;
        state.assignReviewerRolePostSuccess = false
        state.deletedReviewerSuccess = false
        state.reviewerDetailsTotalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addCase(getEditorialRoleList.fulfilled, (state, action) => {
        state.loading = false;
        state.assignEditorialRolePayload = action.payload.data;
        state.getEditorialListSuccess = true
        state.assignEditorialRolePostSuccess = false
        state.deletedEditorialSuccess = false
        state.editorialDetailsTotalItems = parseInt(action.payload.headers['x-total-count'], 10);

      })
      .addCase(deleteAssignedAuthor.fulfilled, (state) => {
        state.loading = false;
        state.deletedAuthorSuccess = true;
      })
      .addCase(deleteAssignedReviewer.fulfilled, (state) => {
        state.loading = false;
        state.deletedReviewerSuccess = true;
      })
      .addCase(deleteAssignedEditorial.fulfilled, (state) => {
        state.loading = false;
        state.deletedEditorialSuccess = true;
      })
      .addCase(ContributorGroupDetails.fulfilled, (state, action) => {
        state.loading = false;
        state.userGroupDeails = action.payload.data;
        state.userGroupDeailstotalItems = parseInt(action.payload.headers['x-total-count'], 10);
        state.GroupUserDeleteStatus = false
      })
      .addCase(ContributorGroupUserDelete.fulfilled, state => {
        state.loading = false;
        state.GroupUserDeleteStatus = true
      })
      .addCase(ContributorGroupDetailsGet.fulfilled, (state, action) => {
        state.loading = false;
        state.ContributorGroupDetails = action.payload.data;
      })
      .addCase(AddJournalUserGroup.fulfilled, state => {
        state.loading = false;
        state.AddJournalUserGroupStatus = Date();
      })
      .addCase(UserAddRoleGroup.fulfilled, state => {
        state.loading = false;
        state.AddJournalUserGroupStatus = Date();
      })
      .addCase(getJournalsGroup.fulfilled, (state, action) => {
        state.loading = false;
        state.journalGroup = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
        state.createdGroup = false;
        state.deletedGroup = false;
        state.updateGroup = false;
      })
      .addCase(getJournalsUser.fulfilled, (state, action) => {
        state.loading = false;
        state.journalUser = action.payload.data;
        state.userTotalItem = parseInt(action.payload.headers['x-total-count'], 10);
        state.createdUser = false;
        state.deletedUserStatus = false;
        state.JournalUserCreateStatus = false;
      })
      .addCase(journalUseradd.fulfilled, (state) => {
        state.loading = false;
        state.JournalUserCreateStatus = true;
      })
      .addCase(postJournalsGroup.fulfilled, (state) => {
        state.loading = false;
        state.createdGroup = true;
      })
      .addCase(UpdateJournalsGroup.fulfilled, (state) => {
        state.loading = false;
        state.updateGroup = true;
      })
      .addCase(deleteJournalsGroup.fulfilled, (state) => {
        state.loading = false;
        state.deletedGroup = true;
      })
      .addCase(deleteJournalsUser.fulfilled, (state) => {
        state.loading = false;
        state.deletedUserStatus = true;
      })

      .addMatcher(
        isPending(
          journalUseradd,
          getJournalsGroup,
          postJournalsGroup,
          deleteJournalsGroup,
          getJournalsUser,
          deleteJournalsUser,
          AddJournalUserGroup,
          UserAddRoleGroup,
          ContributorGroupDetails,
          ContributorGroupUserDelete,

          postAuthorRoleAdd,
          deleteAssignedAuthor,
          deleteAssignedReviewer,
          postReviewerRoleAdd,
          getAuthorRoleList,
          getReviewerRoleList,
          postEditorialRoleAdd,
          getEditorialRoleList,
          deleteAssignedEditorial
        ),
        state => {
          state.loading = true;
          state.errorMessage = null;
          state.getEditorialListSuccess = false;

        }
      )
      .addMatcher(
        isRejected(
          journalUseradd,
          getJournalsGroup,
          postJournalsGroup,
          deleteJournalsGroup,
          getJournalsUser,
          deleteJournalsUser,
          AddJournalUserGroup,
          UserAddRoleGroup,
          ContributorGroupDetails,
          ContributorGroupUserDelete,

          postAuthorRoleAdd,
          deleteAssignedAuthor,
          deleteAssignedReviewer,
          postReviewerRoleAdd,
          getAuthorRoleList,
          getReviewerRoleList,
          postEditorialRoleAdd
        ),
        state => {
          state.loading = false;
          state.errorMessage = null;
          state.getEditorialListSuccess = false;
        }
      );
  },
});

export const {reset, resetStateBooleans} = contributorsGroupSlice.actions;
export default contributorsGroupSlice.reducer;
