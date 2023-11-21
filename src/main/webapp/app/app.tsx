import './app.scss';
import 'app/config/dayjs.ts';
import 'bootstrap/dist/js/bootstrap.js';
import React, { useEffect, useState } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import ErrorBoundary from 'app/shared/error/error-boundary';
import AppRoutes from 'app/routes';
import { CurrentUserContext, CurrentUserContextType } from './shared/layout/header/ThemeContext';
import ErrorModal from "app/config/ErrorModal";
const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');
import { Button } from 'reactstrap'; // Import Reactstrap components
import errorMiddleware, { setShowErrorModal } from './config/notification-middleware';
import {getNotification} from "app/shared/layout/Notification/Notification.reducer"; // Import the error middleware

export const App = () => {
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(getSession());
    dispatch(getProfile());
    dispatch(getNotification())
  }, []);

  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const [context, setContext] = useState('light');
  const [errorModalOpen, setErrorModalOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [errorCode, setErrorCode] = useState(0);

  const openErrorModal = (message: string,code:number) => {
    setErrorMessage(message);
    setErrorCode(code);
    setErrorModalOpen(true);
  };

  const closeErrorModal = () => {
    setErrorModalOpen(false);
  };

  // Set the showErrorModal function using setShowErrorModal
  setShowErrorModal(openErrorModal);

  return (
    <BrowserRouter basename={baseHref}>
      <CurrentUserContext.Provider value={[context, setContext]}>
        {/*<div className="alert alert-warning" role="alert">*/}
        {/*  A simple warning alert—check it out!*/}
        {/*</div>*/}
        <ErrorModal
          errorCode={errorCode}
          isOpen={errorModalOpen}
          message={errorMessage}
          onClose={closeErrorModal}
        />
        <div className="app-container" style={isAuthenticated ? { marginTop: 0 } : {}}>
            <ErrorBoundary>
              <AppRoutes />
            </ErrorBoundary>
        </div>
      </CurrentUserContext.Provider>
    </BrowserRouter>
  );
};

export default App;
