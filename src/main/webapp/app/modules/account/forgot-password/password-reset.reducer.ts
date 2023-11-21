import axios from 'axios';
import { createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';

import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import {ACCOUNT} from "app/config/env";

const initialState = {
  loading: false,
  resetPasswordSuccess: false,
  resetPasswordFailure: false,
  successMessage: null,
  verifyemailcondition: false,
  otpverifycondution: false,
  passwordverifycondution: false,
  passwordverifykey: [] as ReadonlyArray<any>,
  newpasschangeSussfully: false,
};

export type PasswordResetState = Readonly<typeof initialState>;

const apiUrl = 'api/v1';

export const handleForgotPasswordEmailVerify = createAsyncThunk(
  'passwordReset/handleForgotPasswordEmailVerify',
  async (data: { email: string }) => axios.post(`${apiUrl}/send-forgot-password-email-otp`, data),
  { serializeError: serializeAxiosError }
);
export const handleForgotOtpEmailVerify = createAsyncThunk(
  'passwordReset/handleForgotOtpEmailVerify',
  async (data: { otp: string; email: string }) => axios.post<any>(`${apiUrl}/verify-email-otp`, data),
  { serializeError: serializeAxiosError }
);

export const handleForgotChangePassword = createAsyncThunk(
  'passwordReset/handleForgotChangePassword',
  async (data: { email: string; password: string; verifyKey: string }) => axios.put(`${apiUrl}/change-password`, data),
  { serializeError: serializeAxiosError }
);

export const ForgotresendEmailOtp = createAsyncThunk(
  'register/ForgotresendEmailOtp',
  async (data: { email: string }) => {
    const requestUrl = `${ACCOUNT.OrgEmailOTP}`;
    return axios.post<any>(requestUrl, data);
  },
  { serializeError: serializeAxiosError }
);

export const PasswordResetSlice = createSlice({
  name: 'passwordReset',
  initialState: initialState as PasswordResetState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(handleForgotPasswordEmailVerify.fulfilled, () => ({
        ...initialState,
        loading: false,
        resetPasswordSuccess: true,
        successMessage: 'reset.request.messages.success',
        verifyemailcondition: true,
        otpverifycondution: true,
        newpasschangeSussfully: false,
      }))
      .addCase(handleForgotOtpEmailVerify.fulfilled, (state, action) => {
        state.passwordverifykey = action.payload.data;
        (state.verifyemailcondition = true), (state.otpverifycondution = false), (state.passwordverifycondution = true);
      })
      .addCase(handleForgotChangePassword.fulfilled, () => ({
        ...initialState,
        loading: false,
        resetPasswordSuccess: true,
        successMessage: 'reset.finish.messages.success',
        newpasschangeSussfully: true,
      }))
      .addCase(handleForgotPasswordEmailVerify.rejected, () => ({
        ...initialState,
        loading: false,
        verifyemailcondition: false,
        otpverifycondution: false,
        passwordverifycondution: false,
      }))
      .addCase(handleForgotOtpEmailVerify.rejected, () => ({
        ...initialState,
        loading: false,
        verifyemailcondition: true,
        otpverifycondution: true,
      }))
      .addCase(handleForgotChangePassword.rejected, () => ({
        ...initialState,
        loading: false,
        verifyemailcondition: true,
        otpverifycondution: false,
        passwordverifycondution: true,
      }))

      .addMatcher(isPending(handleForgotPasswordEmailVerify, handleForgotOtpEmailVerify, handleForgotChangePassword), state => {
        state.loading = true;
      });
  },
});

export const { reset } = PasswordResetSlice.actions;

// Reducer
export default PasswordResetSlice.reducer;
