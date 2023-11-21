import { ReducersMapObject } from '@reduxjs/toolkit';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';
import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';
import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management/user-management.reducer';
import developerSettings from 'app/modules/administration/user-management/developer-settings/developer-settings.reducer';
import journalManagement from 'app/modules/administration/journal-management/journal-management-list/journal-management.reducer';
import journalDeveloperSettings from 'app/modules/administration/journal-management/journal-developer-settings/journal-developer.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/forgot-password/password-reset.reducer';
import settingsManagement from 'app/modules/administration/journal-management/journal-setting/journals-settings.reducer';
import journalContributors from 'app/modules/administration/journal-management/contributors/contributors.reducer';
import workflow from 'app/modules/administration/journal-management/create-new-submission/work-flow.reducer';
import notificationManagement from 'app/shared/layout/Notification/Notification.reducer'
import production from 'app/modules/administration/journal-management/production/production.reducer'
import submission from 'app/modules/administration/journal-management/submission/submission.reducer'
import copyEditing from 'app/modules/administration/journal-management/copy-editing/copy-editing.reducer'
import review from 'app/modules/administration/journal-management/review/reviewReducer'
import reviewer from 'app/modules/administration/journal-management/review/reviewer-workflow/reviewer-list/reviewer-reducer'
import authorRegister from 'app/modules/author/authorReducer'
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer: ReducersMapObject = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  journalManagement,
  settingsManagement,
  activate,
  passwordReset,
  password,
  settings,
  journalContributors,
  workflow,
  production,
  submission,
  copyEditing,
  notificationManagement,
  loadingBar,
  review,
  reviewer,
  journalDeveloperSettings,
  developerSettings,
  authorRegister

};

export default rootReducer;
