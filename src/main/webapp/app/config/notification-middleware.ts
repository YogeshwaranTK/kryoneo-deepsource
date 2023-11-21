import { translate } from 'react-jhipster';
// import { toast } from 'react-toastify';
import { isFulfilledAction, isRejectedAction } from 'app/shared/reducers/reducer.utils';

const addErrorAlert = (message, key?, data?) => {
  key = key ? key : message;

  // showErrorModal?.(message || 'Unknown error!', 0)
};

let showErrorModal: ((message: string,code:number) => void) | null = null;

export const setShowErrorModal = (showModalFunc: (message: string,code:number) => void) => {
  showErrorModal = showModalFunc;
};

export default () => (next) => (action) => {
  const { error, payload } = action;
  const handleFulfilledAction = (headers) => {
    if (headers) {
      let alert = null;
      let alertParams = null;
      Object.entries<string>(headers).forEach(([k, v]) => {
        if (k.toLowerCase().endsWith('app-alert')) {
          alert = v;
        } else if (k.toLowerCase().endsWith('app-params')) {
          alertParams = decodeURIComponent(v.replace(/\+/g, ' '));
        }
      });
      if (alert) {
        const alertParam = alertParams;
        // toast.success(translate(alert, { param: alertParam }));
      }
    }
  };

  const handleRejectedAction = (response, data) => {
    if (response.status === 0) {
      addErrorAlert('Server not reachable', 'error.server.not.reachable');
    } else if (response.status === 400) {
      handle400Error(response, data);
    } else if (response.status === 404) {
      addErrorAlert('Not found', 'error.url.not.found');
    } else if (response.status !== 401) {
      handleDefaultError(response,data);
    }
  };

  const handle400Error = (response, data) => {
    let errorHeader = null;
    let entityKey = null;
    response?.headers &&
    Object.entries<string>(response.headers).forEach(([k, v]) => {
      if (k.toLowerCase().endsWith('app-error')) {
        errorHeader = v;
      } else if (k.toLowerCase().endsWith('app-params')) {
        entityKey = v;
      }
    });
    if (errorHeader) {
      const entityName = translate('global.menu.entities.' + entityKey);

      addErrorAlert(errorHeader, errorHeader, { entityName });
    } else if (data?.fieldErrors) {
      handleFieldErrors(data.fieldErrors);
    } else if (typeof data === 'string' && data !== '') {
      addErrorAlert(data);
    } else {
      // toast.error(data?.message || data?.error || data?.title || 'Unknown error!');
      //  showErrorModal?.(error.message || 'Unknown error!',0)
    }
  };

  const handleFieldErrors = (fieldErrors) => {
    for (const fieldError of fieldErrors) {
      if (['Min', 'Max', 'DecimalMin', 'DecimalMax'].includes(fieldError.message)) {
        fieldError.message = 'Size';
      }
      // convert 'something[14].other[4].id' to 'something[].other[].id' so translations can be written to it
      const convertedField = fieldError.field.replace(/\[\d*\]/g, '[]');
      const fieldName = translate(`kjmsApp.${fieldError.objectName}.${convertedField}`);
      addErrorAlert(`Error on field "${fieldName}"`, `error.${fieldError.message}`, { fieldName });
    }
  };

  const handleDefaultError = (response,data) => {
    if (typeof data === 'string' && data !== '') {
      console.log(response,data)
      addErrorAlert(data);
    } else {
      // toast.error(data?.message || data?.error || data?.title || 'Unknown error!');
         showErrorModal?.(error.message || 'Unknown error!',0)
    }
  };

  if (isFulfilledAction(action)) {
    handleFulfilledAction(payload?.headers);
  } else if (isRejectedAction(action)) {
    if (error && error.isAxiosError) {
      handleRejectedAction(error.response, error.response?.data);
    } else if (error) {
      if (error.message.includes('500')) {
        showErrorModal?.(translate('error.http.500') || 'Unknown error!',500);
      } else if(error.message.includes('403')) {
        showErrorModal?.(translate('error.http.403') || 'Unknown error!',403);
      }
    }
  }

  return next(action);
};
