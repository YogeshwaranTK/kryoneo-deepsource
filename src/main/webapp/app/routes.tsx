import React from 'react';
import { Route } from 'react-router-dom';
import Loadable from 'react-loadable';

import Login from 'app/modules/login/login';
import PasswordResetInit from 'app/modules/account/forgot-password/password-reset-init';
import Logout from 'app/modules/login/logout';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PageNotFound from 'app/shared/error/page-not-found';
import { AUTHORITIES } from 'app/config/constants';
import { useAppSelector } from 'app/config/store';
import LoaderMain from "app/shared/Loader/loader-main";
import Docs from "app/modules/administration/docs/docs";

const loading = <LoaderMain />;

const Account = Loadable({
  loader: () => import(/* webpackChunkName: "account" */ 'app/modules/account'),
  loading: () => loading,
});

const Journal = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => loading,
});

const Author = Loadable({
  loader: () => import(/* webpackChunkName: "author" */ 'app/modules/author/author'),
  loading: () => loading,
});

const AppRoutes = () => {
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  return (
    <div className="view-routes">
      <ErrorBoundaryRoutes>
        <Route index element={isAuthenticated?<Journal/>:<Login />} />
        <Route path="login" element={<Login />} />
        <Route path="logout" element={<Logout />} />
        <Route path="/journal/:journalId/author/registration" element={<Author />} />

        <Route path="account">
          <Route index element={<Login />} />
          <Route path="forgotpassword" element={<PasswordResetInit />} />
          <Route path="*" element={<PageNotFound />} />
        </Route>
        <Route path="admin/docs" element={<Docs/>}/>
        <Route path="journal/*" element= {
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
                <Journal />
            </PrivateRoute>
          }
        />

        <Route path="*" element={<PageNotFound />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};

export default AppRoutes;
