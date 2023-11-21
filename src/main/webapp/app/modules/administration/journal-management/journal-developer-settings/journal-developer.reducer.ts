import axios from 'axios';
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {JournalDeveloperSettings} from "app/config/env";

const initialState = {
  loading: false,
  errorMessage: null,
  mailActionsPayload: [],
  mailActionDetailsPayload: [],
  postMailActionDetailsSuccess: false
};

export const getMailActions = createAsyncThunk('journalDeveloperSettings/getMailActions', async () => {
  const requestUrl = `${JournalDeveloperSettings.getMailActions}`
  return axios.get<any>(requestUrl);
});

export const getMailActionDetails = createAsyncThunk('journalDeveloperSettings/getMailActionDetails', async (actionId: string) => {
  const requestUrl = `${JournalDeveloperSettings.getMailActionDetails}?actionId=${actionId}`
  return axios.get<any>(requestUrl);
});

export const postMailActionDetails = createAsyncThunk(
  'journalDeveloperSettings/postMailActionDetails',
  async (data: { actionId: string; html: string; active: boolean; mailToVariables: string[] }) =>
    axios.post<any>(`${JournalDeveloperSettings.getMailActionDetails}`, data),
  {serializeError: serializeAxiosError}
);

export type JournalDeveloperSettingsState = Readonly<typeof initialState>;

export const JournalDeveloperSettingsSlice = createSlice({
  name: 'journalDeveloperSettings',
  initialState: initialState as JournalDeveloperSettingsState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getMailActions.fulfilled, (state, action) => {
        state.loading = false;
        state.mailActionsPayload = action.payload.data;
        state.postMailActionDetailsSuccess = false;
      })
      .addCase(getMailActionDetails.fulfilled, (state, action) => {
        state.loading = false;
        state.mailActionDetailsPayload = action.payload.data;
      })
      .addCase(postMailActionDetails.fulfilled, (state) => {
        state.loading = false;
        state.postMailActionDetailsSuccess = true;
      })
      .addMatcher(isPending(getMailActions, getMailActionDetails, postMailActionDetails), state => {
        state.loading = true;
        state.errorMessage = null;
      })
      .addMatcher(isRejected(getMailActions, getMailActionDetails, postMailActionDetails), (state, action) => {
        state.loading = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const {reset} = JournalDeveloperSettingsSlice.actions;

export default JournalDeveloperSettingsSlice.reducer;
