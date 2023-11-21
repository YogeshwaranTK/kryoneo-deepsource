import React from 'react';
import {Route} from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import SubmissionList from './submission/submission-list';
import SubmissionManagement
  from "app/modules/administration/journal-management/create-new-submission/submission-management";
import
  SubmissionView
  from "app/modules/administration/journal-management/submission/submission-view";
import PageNotFound from "app/shared/error/page-not-found";
import ReviewViewList from "app/modules/administration/journal-management/review/review-view";
import CopyEditingView from "app/modules/administration/journal-management/copy-editing/copy-editing-view";
import ReviewerViewManagement
  from "app/modules/administration/journal-management/review/reviewer-workflow/reviewer-view-management";
import SiteSettingsManagement
  from "app/modules/administration/journal-management/journal-setting/site-setting_mangement";
import UserAndRoleSettingsManagement
  from "app/modules/administration/journal-management/contributors/user-and-role-settings";
import ProductionViewList from "app/modules/administration/journal-management/production/production-view"
import ReviewList from "app/modules/administration/journal-management/review/review-list";
import CopyEditingList from "app/modules/administration/journal-management/copy-editing/copy-editing-list";
import ProductionList from "app/modules/administration/journal-management/production/production-list";
import JournalDeveloperSettings
  from "app/modules/administration/journal-management/journal-developer-settings/email-settings/journal-developer-email-settings-management";
import ReviewerSubmissionsManagement from "app/modules/administration/journal-management/review/reviewer-workflow/reviewer-list/reviewer-management";
import TopNav from "app/modules/administration/journal-management/top-nav/top-nav";


const ArticleSubmissionRoutes = () => {
  return (
    <>
      <TopNav />
    <ErrorBoundaryRoutes>
      <Route index element={<SubmissionList/>}/>
      <Route path="submissions" element={<SubmissionList/>}/>
      <Route path="workflow" element={<SubmissionManagement/>}/>
      <Route path="user-role-settings" element={<UserAndRoleSettingsManagement/>}/>
      <Route path="developer-settings" element={<JournalDeveloperSettings/>}/>
      <Route path="site-settings" element={<SiteSettingsManagement/>}/>
      <Route path="review" element={<ReviewViewList/>}/>
      <Route path="reviewer-workflow" element={<ReviewerViewManagement/>}/>
      <Route path="reviewer-dashboard" element={<ReviewerSubmissionsManagement/>}/>
      <Route path="copy-editing" element={<CopyEditingView/>}/>
      <Route path="production" element={<ProductionViewList/>}/>
      <Route path="submission-workflow" element={<SubmissionView/>}/>
      <Route path="reviews" element={<ReviewList/>}/>
      <Route path="copyeditings" element={<CopyEditingList/>}/>
      <Route path="productions" element={<ProductionList/>}/>
      <Route path="user-role-settings">
        <Route index element={<UserAndRoleSettingsManagement/>}/>
      </Route>
      <Route path="developer-settings">
        <Route index element={<JournalDeveloperSettings/>}/>
        <Route path="email" element={<JournalDeveloperSettings/>}/>
      </Route>
      <Route path="*" element={<PageNotFound/>}/>
    </ErrorBoundaryRoutes>
    </>
  );
}

export default ArticleSubmissionRoutes;
