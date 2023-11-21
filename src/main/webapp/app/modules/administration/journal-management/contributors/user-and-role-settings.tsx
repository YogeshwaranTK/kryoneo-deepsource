// Tab.js
import React, {useEffect, useState} from 'react';

import {useAppDispatch, useAppSelector} from "app/config/store";
import {useParams} from "react-router-dom";
import Breadcrumb from 'app/shared/breadcrumb/breadcrumb';
import '../create-new-submission/work-flow.scss';
import '../contributors/user-contributors.scss'
import 'app/modules/administration/user-management/group-management.scss'

import {
  getJournalDetails
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import AuthorContributors
  from "app/modules/administration/journal-management/contributors/author-contributors/author-contributors-list";
import EditorialContributors
  from "app/modules/administration/journal-management/contributors/editorial-contributors/editorial-contributors-list";
import ReviewerContributors
  from "app/modules/administration/journal-management/contributors/reviewer-contributors/reviewer-contributors-list";
import {Translate,translate} from "react-jhipster";

const UserAndRoleSettingsManagement = () => {

  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const [activeTab, setActiveTab] = useState('authors');
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]
  const dispatch = useAppDispatch();
  const path = `/journal/${parseInt(Jo_id, 10)}`;

  useEffect(() => {
    dispatch(getJournalDetails(Jo_id));
  }, []);

  const BreadCrumbRoutes = [
    {name: (translate("contributors_breadcrumb.home")), path: '/journal'},
    {name: (translate("contributors_breadcrumb.journals")), path: '/journal'},
    {name: `${journals.key ? journals.key : ''}`, path},
    {name: (translate("contributors_breadcrumb.Users_&_Roles_Settings"))},
  ];
  const handleTabClick = (tabName) => {
    setActiveTab(tabName);
  };

  return (<>
      <div className="row height-100">
        <div className="col-12 pt-2 ps-4">
          <Breadcrumb props={BreadCrumbRoutes}/>
        </div>
        <div className='work_flow'>
          <ul className="nav nav-tabs  mt-2">
            <button className={`nav-link ${activeTab === 'authors' ? 'active' : ''} `}
                    onClick={() => handleTabClick('authors')}><Translate contentKey="user_and_role.Authors"></Translate>
            </button>
            <button className={`nav-link ${activeTab === 'reviewers' ? 'active' : ''} }`}
                    onClick={() => handleTabClick('reviewers')}><Translate contentKey="user_and_role.Reviewers"></Translate>
            </button>
            <button className={`nav-link ${activeTab === 'editorialUsers' ? 'active' : ''} }`}
                    onClick={() => handleTabClick('editorialUsers')}><Translate contentKey="user_and_role.Editorial_Users"></Translate>
            </button>

          </ul>
          <div className="tab-content" id="nav-tabContent">
            {activeTab === 'authors' && <AuthorContributors/>}
            {activeTab === 'reviewers' && <ReviewerContributors/>}
            {activeTab === 'editorialUsers' && <EditorialContributors/>}
          </div>


        </div>
      </div>
    </>

  );
};

export default UserAndRoleSettingsManagement;
