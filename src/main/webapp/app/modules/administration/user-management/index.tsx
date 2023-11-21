import React from 'react';
import {Route} from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import UserManagement from './user-management/user-management';
import UserManagementUpdate from './user-management/user-management-update';
import UserManagementCreate from "app/modules/administration/user-management/user-management/user-management-create";
import PageNotFound from "app/shared/error/page-not-found";
import DeveloperEmailSettings
  from "app/modules/administration/user-management/developer-settings/email-settings/developer-email-settings-management";

const UserManagementRoutes = () => (
  <div style={{paddingLeft: '1rem', paddingRight: '1rem'}}>
    <ErrorBoundaryRoutes>
      <Route index element={<UserManagement/>}/>
      <Route path="user" element={<UserManagement/>}/>
      <Route>
        <Route index element={<UserManagement/>}/>
        <Route path="edit" element={<UserManagementUpdate/>}/>
        <Route path="create" element={<UserManagementCreate/>}/>
      </Route>
      <Route path="developer-settings">
        <Route index element={<DeveloperEmailSettings/>}/>
        <Route path="email" element={<DeveloperEmailSettings/>}/>
      </Route>
      <Route path="*" element={<PageNotFound/>}/>
    </ErrorBoundaryRoutes>
  </div>
);

export default UserManagementRoutes;
