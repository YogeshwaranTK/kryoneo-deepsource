import axios from 'axios';
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {IQueryParams, serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {IJournal, JCmodel} from 'app/modules/administration/journal-management/journal-management-list/journal.model';
import {JOURNAL_MANAGEMENT} from "app/config/env";
import {
  DELETE_GROUP_MODEL_journal
} from "app/modules/administration/journal-management/journal-management-list/journal-delete";

const initialState = {
  loading: false,
  errorMessage: null,
  journals: <IJournal>[],
  totalItems: 0,
  journalDeleteStatus: false,
  successfullyCreatedJournal: false,
  successfullyDeletedJournal: false,
  journalId: 0,
};

export const getJournals = createAsyncThunk('journalManagement/fetch_journals', async ({
                                                                                         page,
                                                                                         size,
                                                                                         sort,
                                                                                         searchText
                                                                                       }: IQueryParams) => {
  const requestUrl = `${JOURNAL_MANAGEMENT.JournalList}${
    sort
      ? `?page=${page}&size=${size}&sort=${sort}&searchText=${searchText}`
      : `?page=${page}&size=${size}&sort=createdAt,desc&searchText=${searchText}`
  }`;
  return axios.get<IJournal>(requestUrl);
});

export const getJournalsPdf = async () => {
  return axios.get<any>(`${JOURNAL_MANAGEMENT.JournalExportPDF}`, {responseType: 'blob'});
};

export const getJournalsExcel = async () => {
  return axios.get<any>(`${JOURNAL_MANAGEMENT.JournalExportExcel}`, {responseType: 'blob'});
};

export const postNewJournal = createAsyncThunk(
  'journalManagement/postNewJournal',
  async (data: {}) =>
    axios.post<JCmodel>(`${JOURNAL_MANAGEMENT.JournalCreate}`, data),
  {serializeError: serializeAxiosError}
);

export const deleteJournal = createAsyncThunk(
  'journalManagement/deleteJournal',
  async (data: DELETE_GROUP_MODEL_journal) =>
    axios.delete<DELETE_GROUP_MODEL_journal>(`${JOURNAL_MANAGEMENT.JournalDelete}?id=${data.id}&deletedRemarks=${data.deletedRemarks}&key=${data.key}`),
  {serializeError: serializeAxiosError}
);

export type JournalManagementState = Readonly<typeof initialState>;

export const JournalManagementSlice = createSlice({
  name: 'journalManagement',
  initialState: initialState as JournalManagementState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getJournals.fulfilled, (state, action) => {
        state.loading = false;
        state.journals = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
        state.successfullyDeletedJournal = false;
        state.successfullyCreatedJournal = false;
      })
      .addCase(deleteJournal.fulfilled, (state) => {
        state.loading = false;
        state.successfullyDeletedJournal = true;
      })
      .addCase(postNewJournal.fulfilled, (state, action) => {
        state.loading = false;
        state.successfullyCreatedJournal = true;
        state.journalId = action.payload.data['journalId']
      })
      .addMatcher(isPending(getJournals, postNewJournal), state => {
        state.loading = true;
        state.errorMessage = null;
        state.journals = <IJournal>[];
      })
      .addMatcher(isRejected(getJournals, postNewJournal), (state, action) => {
        state.loading = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const {reset} = JournalManagementSlice.actions;

export default JournalManagementSlice.reducer;
