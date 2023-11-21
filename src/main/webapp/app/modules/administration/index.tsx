import React from 'react';
import {Route} from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import UserManagement from './user-management';
import JournalManagement from './journal-management/journal-management-list/journal-management';
import ArticleSubmission from './journal-management';
import Header from "app/shared/layout/header/header";
import {useAppSelector} from "app/config/store";
import PageNotFound from "app/shared/error/page-not-found";
import UserAccountDetails from "app/modules/administration/user-management/user-management/user-account-details";
import CreateNewJournal
  from "app/modules/administration/journal-management/create-new-journal/journal-management-create";

const AdministrationRoutes = () => {
  const currentLocale = useAppSelector(state => state.locale.currentLocale);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const ribbonEnv = useAppSelector(state => state.applicationProfile.ribbonEnv);
  const isInProduction = useAppSelector(state => state.applicationProfile.inProduction);
  const isOpenAPIEnabled = useAppSelector(state => state.applicationProfile.isOpenAPIEnabled);


  return (
    <div>
      <Header
        isAuthenticated={isAuthenticated}
        currentLocale={currentLocale}
        ribbonEnv={ribbonEnv}
        isInProduction={isInProduction}
        isOpenAPIEnabled={isOpenAPIEnabled}
      />
      <div className={`container-fluid view-container pt-0 ps-0 pe-0`} id="app-view-container">
        <ErrorBoundaryRoutes>
          <Route index element={<JournalManagement/>}/>
          <Route path="userdetails" element={<UserAccountDetails/>}/>
          <Route path="new" element={<CreateNewJournal/>}/>
          <Route path=":1/*" element={<ArticleSubmission/>}/>
          <Route path="user-management/*" element={<UserManagement/>}/>
          <Route path="*" element={<PageNotFound/>}/>
        </ErrorBoundaryRoutes>
      </div>
    </div>
  );
}
export default AdministrationRoutes;
