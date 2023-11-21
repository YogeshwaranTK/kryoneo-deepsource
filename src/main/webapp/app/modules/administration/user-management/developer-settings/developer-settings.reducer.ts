import axios from 'axios';
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {DeveloperSettings} from "app/config/env";

const initialState = {
  loading: false,
  errorMessage: null,
  mailActionsPayload: [],
  mailActionDetailsPayload: [],
  postMailActionDetailsSuccess: false
};

export const getMailActionsUser = createAsyncThunk('developerSettings/getMailActionsUser', async () => {
  const requestUrl = `${DeveloperSettings.getMailActions}`
  return axios.get<any>(requestUrl);
});

export const getMailActionDetailsUser = createAsyncThunk('developerSettings/getMailActionDetailsUser', async (actionId: string) => {
  const requestUrl = `${DeveloperSettings.getMailActionDetails}?actionId=${actionId}`
  return axios.get<any>(requestUrl);
});

export const postMailActionDetailsUser = createAsyncThunk(
  'developerSettings/postMailActionDetailsUser',
  async (data: { actionId: string; html: string; active: boolean; mailToVariables: string[] }) =>
    axios.post<any>(`${DeveloperSettings.getMailActionDetails}`, data),
  {serializeError: serializeAxiosError}
);

export type DeveloperSettingsState = Readonly<typeof initialState>;

export const DeveloperSettingsSlice = createSlice({
  name: 'developerSettings',
  initialState: initialState as DeveloperSettingsState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getMailActionsUser.fulfilled, (state, action) => {
        state.loading = false;
        state.mailActionsPayload = action.payload.data;
        state.postMailActionDetailsSuccess = false;
      })
      .addCase(getMailActionDetailsUser.fulfilled, (state, action) => {
        state.loading = false;
        state.mailActionDetailsPayload = action.payload.data;
      })
      .addCase(postMailActionDetailsUser.fulfilled, (state) => {
        state.loading = false;
        state.postMailActionDetailsSuccess = true;
      })
      .addMatcher(isPending(getMailActionsUser, getMailActionDetailsUser, postMailActionDetailsUser), state => {
        state.loading = true;
        state.errorMessage = null;
      })
      .addMatcher(isRejected(getMailActionsUser, getMailActionDetailsUser, postMailActionDetailsUser), (state, action) => {
        state.loading = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const {reset} = DeveloperSettingsSlice.actions;

export default DeveloperSettingsSlice.reducer;
