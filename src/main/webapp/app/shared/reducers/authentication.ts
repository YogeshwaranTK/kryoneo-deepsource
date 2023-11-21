import axios from 'axios';
import { Storage } from 'react-jhipster';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { serializeAxiosError } from './reducer.utils';
import { AppThunk } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';
const AUTH_TOKEN_KEY = 'jhi-authenticationToken';
const role = 'role';
import {AUTH_API} from "app/config/env";

export const initialState = {
  loading: false,
  isAuthenticated: false,
  loginSuccess: false,
  loginError: false, // Errors returned from server side
  showModalLogin: false,
  account: {} as any,
  errorMessage: null as unknown as string, // Errors returned from server side
  redirectMessage: null as unknown as string,
  sessionHasBeenFetched: false,
  logoutUrl: null as unknown as string,
  loginStatus: [] as any,
  loginResData: [] as any,
};
export type AuthenticationState = Readonly<typeof initialState>;

export const getSession = (): AppThunk => async (dispatch, getState) => {
  await dispatch(getAccount());
  const { account } = getState().authentication;
  const data ={
    id_token : account?.id_token
  }
  validateToken(data)

  if (account && account.langKey) {
    const langKey = Storage.session.get('locale', account.langKey);
    await dispatch(setLocale(langKey));
  }
};

export const getAccount = createAsyncThunk('authentication/get_account',
  async () => axios.get<any>(`${AUTH_API.GetAccount}`), {
  serializeError: serializeAxiosError,
});

interface IAuthParams {
  username: string;
  password: string;
  rememberMe?: boolean;
  orgId?: string;
}
export const userData = {
  username: '',
  password: '',
  rememberMe: false,
  orgId: null,
};
export const authenticate = createAsyncThunk(
  'authentication/login',
  async (auth: IAuthParams) =>
    axios.post<any>(
      `${AUTH_API.Authentication}`,
      {
        username: auth.username,
        password: auth.password,
        rememberMe: auth.rememberMe,
      }
    ),
  {
    serializeError: serializeAxiosError,
  }
);

export const validateToken = data => {
  const bearerToken = data.id_token;
  const token = Storage.local.get('jhi-authenticationToken')
  if (bearerToken) {
    if (userData.rememberMe || token) {
      Storage.local.set(AUTH_TOKEN_KEY, bearerToken);
    } else {
      Storage.session.set(AUTH_TOKEN_KEY, bearerToken);
    }
    return true;
  }
  return true;
};

export const clearAuthToken = () => {
  if (Storage.local.get(AUTH_TOKEN_KEY)) {
    Storage.local.remove(AUTH_TOKEN_KEY);
  }
  if (Storage.session.get(AUTH_TOKEN_KEY)) {
    Storage.session.remove(AUTH_TOKEN_KEY);
  }
  if (Storage.session.get(role)){
    Storage.session.remove(role);
  }

};

export const logout: () => AppThunk = () => dispatch => {
  clearAuthToken();
  dispatch(logoutSession());
};

export const clearAuthentication = messageKey => dispatch => {
  clearAuthToken();
  dispatch(authError(messageKey));
  dispatch(clearAuth());
};

export const AuthenticationSlice = createSlice({
  name: 'authentication',
  initialState: initialState as AuthenticationState,
  reducers: {
    logoutSession() {
      return {
        ...initialState,
        showModalLogin: true,
      };
    },
    authError(state, action) {
      return {
        ...state,
        showModalLogin: true,
        redirectMessage: action.payload,
      };
    },
    clearAuth(state) {
      return {
        ...state,
        loading: false,
        showModalLogin: true,
        isAuthenticated: false,
      };
    },
  },
  extraReducers(builder) {
    builder
      .addCase(authenticate.fulfilled, (state, action) => {
        state.loading= false;
        state.loginError= false;
        //   state.loginSuccess= true;
        state.loginResData = action.payload.data;
        state.loginStatus = action.payload.status;
      })
      .addCase(getAccount.fulfilled, (state, action) => {
        const isAuthenticated = action.payload && action.payload.data && action.payload.data.activated;
        return {
          ...state,
          isAuthenticated,
          loading: false,
          sessionHasBeenFetched: true,
          account: action.payload.data,
        };
      })
      .addCase(authenticate.pending, state => {
        state.loading = true;
      })
      .addCase(getAccount.pending, state => {
        state.loading = true;
      })
      .addCase(getAccount.rejected, (state, action) => ({
        ...state,
        loading: false,
        isAuthenticated: false,
        sessionHasBeenFetched: true,
        showModalLogin: true,
        errorMessage: action.error.message,
      }))
      .addCase(authenticate.rejected, (state, action) => ({
        ...initialState,
        errorMessage: action.error.message,
        showModalLogin: true,
        loginError: true,
        loading : false
      }))
  },
});

export const { logoutSession, authError, clearAuth } = AuthenticationSlice.actions;

// Reducer
export default AuthenticationSlice.reducer;
