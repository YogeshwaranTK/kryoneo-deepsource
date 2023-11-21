import axios from 'axios';
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {serializeAxiosError} from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  notifications: <any>[],
  isLoading: true,
  removeNotify: false
};

export const getNotification = createAsyncThunk('notificationManagement/getNotification', async () => {
  const requestUrl = "api/v1/notification/list?page=0&size=100"
  return axios.get<any>(requestUrl);
});

export const removeNotification = createAsyncThunk(
  'notificationManagement/removeNotification',
  async (data: any) =>
    axios.put<any>('api/v1/notification/update-read', data),
  {serializeError: serializeAxiosError}
);

export type NotificationManagementState = Readonly<typeof initialState>;

export const NotificationManagementSlice = createSlice({
  name: 'notificationManagement',
  initialState: initialState as NotificationManagementState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getNotification.fulfilled, (state, action) => {
        state.loading = false;
        state.notifications = action.payload.data;
        state.isLoading = false;
      })
      .addCase(removeNotification.fulfilled, (state, action) => {
        state.loading = false;
        state.removeNotify = action.payload.data;
      })
      .addMatcher(isPending(getNotification, removeNotification), state => {
        state.loading = true;
        state.isLoading = true;
        state.errorMessage = null;
      })
      .addMatcher(isRejected(getNotification, removeNotification), (state, action) => {
        state.loading = false;
        state.isLoading = true;
        state.errorMessage = action.error.message;
      });
  },
});
export const {reset} = NotificationManagementSlice.actions;

export default NotificationManagementSlice.reducer;
