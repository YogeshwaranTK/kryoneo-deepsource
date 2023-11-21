import axios from 'axios';
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {IUser} from 'app/shared/model/user.model';
import {IQueryParams, serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {JOURNAL_MANAGEMENT, PUBLIC_API, USER_MANAGEMENT} from "app/config/env";
import {CountryModel} from "app/config/user-management.config";

const initialState = {
  loading: false,
  loadinguserRemove: false,
  errorMessage: null,
  users: [] as ReadonlyArray<IUser>,
  authorities: [] as any[],
  updatedUser: false,
  deletedUser: false,
  createdUser: false,
  totalItems: 0,
  countryData: [] as CountryModel[],
  successMessage: false,
  SiteSettingMailData: {},
  SiteSettingMailRequestStatus: null,
  SiteSettingStorageData: {},
  SiteSettingStorageDataStatus: false,
  userremoveStatus: false,
  resetpassdata:{}
};

export const getUsers = createAsyncThunk('userManagement/fetch_users', async ({
                                                                                page,
                                                                                size,
                                                                                sort,
                                                                                searchText
                                                                              }: IQueryParams) => {
  const requestUrl = `${USER_MANAGEMENT.UserDetail}${sort
    ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}`
    : `?page=${page}&size=${size}&sort=createdAt,desc&searchText=${searchText}`
  }`;

  return axios.get<IUser[]>(requestUrl);
});

export const getUsersRaw = async ({ page,
                                    size,
                                    sort,
                                    searchText
                                  }: IQueryParams) => {

  const requestUrl = `${USER_MANAGEMENT.UserDetail}${sort
    ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}`
    : `?page=${page}&size=${size}&sort=createdAt,desc&searchText=${searchText}`
  }`;
  return axios.get<any>(requestUrl)
};

export const userCreate = createAsyncThunk(
  'userManagement/userCreate',
  async (user: IUser) =>
    axios.post<IUser>(`${USER_MANAGEMENT.UserCreate}`, user),
  {serializeError: serializeAxiosError}
);


export const userUpdate = createAsyncThunk(
  'userManagement/update',
  async (user: IUser) =>
    axios.put<any>(`${USER_MANAGEMENT.UserUpdate}`, user),
  {serializeError: serializeAxiosError}
);

export const userGet = async (id: string) => {
  return axios.get<any>(`${USER_MANAGEMENT.UserDetail}?id=${id}`)
};



export const userGetdata = async (userId: Number) => {
  return axios.get<any>(`${USER_MANAGEMENT.userData}?id=${userId}`)
};

export const JournalGroupList = async (id: string) => {
  return axios.get<any>(`api/v1/journal-group-list?journalId=${id}&searchText=&page=0&size=20`)
};

export const GetJournalList = async () => {
  return axios.get<any>(`api/v1/journals?page=0&size=20&sort=createdAt,desc&searchText=`)
};

export const getJournalList = async ({ page,
                                    size,
                                    sort,
                                    searchText
                                  }: IQueryParams) => {

  const requestUrl = `${JOURNAL_MANAGEMENT.JournalList}${sort
    ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}`
    : `?page=${page}&size=${size}&sort=createdAt,desc&searchText=${searchText}`
  }`;
  return axios.get<any>(requestUrl)
};

export const userRemove = createAsyncThunk(
  'userManagement/userRemove',
  async (data: any) =>
    axios.put<any>(`${USER_MANAGEMENT.UserRemove}?id=${data.userId}&deletedRemarks=${data.reason}`),
  {serializeError: serializeAxiosError}
);


export const getRoles = createAsyncThunk('roleManagement/fetch_roles', async () => {
  return axios.get<any[]>(`${USER_MANAGEMENT.RoleList}`);
});

export const SiteSettingStorage = createAsyncThunk('roleManagement/SiteSettingStorage', async () => {
  return axios.get<any[]>(`api/v1/site-setting/storage/azure`);
});
export const SiteSettingMail = createAsyncThunk('roleManagement/SiteSettingMail', async () => {
  return axios.get<any[]>(`api/v1/site-setting/mail`);
});

export const SiteSettingMailRequest = createAsyncThunk('roleManagement/SiteSettingMailRequest', async (data: any) => {
  return axios.put<any[]>(`api/v1/site-setting/update/mail`, data);
});
export const SiteSettingStorageRequest = createAsyncThunk('roleManagement/SiteSettingStorageRequest', async (data: any) => {
  return axios.put<any[]>(`api/v1/site-setting/update/mail`, data);
});
export const userPasswordRest = createAsyncThunk('roleManagement/userPasswordRest', async (data: any) => {
  return axios.put<any>(`/api/v1/reset-password?userId=${data}`);
});


export const getCountryList = createAsyncThunk(
  'userManagement/countryList',
  async () => {
    const requestUrl = `${PUBLIC_API.CountryList}?page=0&size=500`;
    return axios.get<any>(requestUrl);
  },
  {serializeError: serializeAxiosError}
);

export type UserManagementState = Readonly<typeof initialState>;

export const UserManagementSlice = createSlice({
  name: 'userManagement',
  initialState: initialState as UserManagementState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {

    builder

      .addCase(userRemove.fulfilled, (state) => {
        state.userremoveStatus = true;
        state.loadinguserRemove= false;
      })
      .addCase(SiteSettingStorage.fulfilled, (state, action) => {
        state.SiteSettingStorageData = action.payload.data;
        state.loading = false;
        state.successMessage = false;
        state.SiteSettingStorageDataStatus = false;

      })
      .addCase(SiteSettingStorageRequest.fulfilled, (state) => {
        state.loading = false;
        state.SiteSettingStorageDataStatus = true;
      })
      .addCase(SiteSettingMail.fulfilled, (state, action) => {
        state.SiteSettingMailData = action.payload.data;
        state.loading = false;
        state.successMessage = false;
      })
      .addCase(SiteSettingMailRequest.fulfilled, (state) => {
        state.loading = false;
        state.SiteSettingMailRequestStatus = Date();
      })
      .addCase(getCountryList.fulfilled, (state, action) => {
        state.countryData = action.payload.data;
        state.loading = false;
        state.successMessage = false;
      })
      .addCase(getUsers.fulfilled, (state, action) => {
        state.loading = false;
        state.users = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
        state.createdUser = false;
        state.updatedUser = false;
        state.userremoveStatus = false;
      })
      .addCase(userCreate.fulfilled, (state) => {
        state.loading = false;
        state.createdUser = true;
      })
      .addCase(userUpdate.fulfilled, (state) => {
        state.loading = false;
        state.updatedUser = true;
      })
      .addCase(getRoles.fulfilled, (state, action) => {
        state.authorities = action.payload.data;
      })
      .addCase(userPasswordRest.fulfilled, (state, action) => {
        state.loading = false;
        state.resetpassdata = action.payload?.headers;
      })
      .addMatcher(isPending(userRemove), state => {
        state.loadinguserRemove= true;
        state.errorMessage = null;
      })
      .addMatcher(isPending(userPasswordRest, getUsers, userUpdate, userCreate, SiteSettingMail, SiteSettingMailRequest, SiteSettingStorage, SiteSettingStorageRequest), state => {
        state.loading = true;
        state.errorMessage = null;
      })
      .addMatcher(isRejected(userPasswordRest,userRemove, getUsers, getRoles, userUpdate, userCreate, SiteSettingMail, SiteSettingMailRequest, SiteSettingStorage, SiteSettingStorageRequest), (state, action) => {
        state.loading = false;
        state.errorMessage = action.error.message;
        state.loadinguserRemove= false;
      })
      .addMatcher(isPending(getCountryList), (state) => {
        state.loading = true;
      })
      .addMatcher(isRejected(getCountryList), (state, action) => {
        state.loading = false;
        state.errorMessage = action.error.message;
      })
  },
});

export const {reset} = UserManagementSlice.actions;
export default UserManagementSlice.reducer;
