import axios from "axios";
import {createAsyncThunk, createSlice, isPending, isRejected} from '@reduxjs/toolkit';
import {serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {Author} from "app/config/env";

const initialState = {
  loading: false,
  errorMessage: null,
  registerAuthorSuccess: false
}

export type AuthorState = Readonly<typeof initialState>;

export const authorRegister = createAsyncThunk(
  'authorRegister/authorRegister',
  async (data?: any) => {
    const Req_Url = `${Author.register}`;
    return axios.post<any>(Req_Url, data);
  },
  {serializeError: serializeAxiosError}
);

export const AuthorRegisterSlice = createSlice({
  name: 'authorRegister',

  initialState: initialState as AuthorState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase(authorRegister.fulfilled, (state, action) => {
        state.loading = false;
        state.registerAuthorSuccess = true
      })
      .addMatcher(isPending(authorRegister),
        state => {
          state.loading = true;
          state.errorMessage = null;
        }
      )
      .addMatcher(isRejected(authorRegister),
        state => {
          state.loading = false;
          state.errorMessage = null;
        }
      );
  }
})


export default AuthorRegisterSlice.reducer;
