import axios from 'axios';
import { Storage } from 'react-jhipster';
import jwtDecode from 'jwt-decode';

const TIMEOUT = 1 * 60 * 1000;
axios.defaults.timeout = TIMEOUT;
axios.defaults.baseURL = SERVER_API_URL;

interface TokenPayload {
  sub: string;
  auth: string;
  organization: string;
  organizations: string;
  exp: string;
}

function getDecodedTokenValue(token: string): TokenPayload | null {
  try {
    const decodedToken = jwtDecode<TokenPayload>(token);
    return decodedToken;
  } catch (error) {
    console.error('Error decoding JWT token:', error);
    return null;
  }
}

const setupAxiosInterceptors = onUnauthenticated => {
  const onRequestSuccess = config => {
    const token = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
    const journal_id = Storage.local.get('journal_id');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    if (journal_id) {
      config.headers.journalId = journal_id;
    }
    return config;
  };

  const onResponseSuccess = response => response;
  const onResponseError = err => {
    const status = err.status || (err.response ? err.response.status : 0);
    if (status === 401) {
      onUnauthenticated();
    }
    return Promise.reject(err);
  };
  axios.interceptors.request.use(onRequestSuccess);
  axios.interceptors.response.use(onResponseSuccess, onResponseError);
};

export default setupAxiosInterceptors;

