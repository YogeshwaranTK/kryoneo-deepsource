import React, {useEffect, useState} from 'react';
import PublishingDetails from "app/modules/administration/journal-management/journal-setting/publishing-details";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {useParams} from "react-router-dom";
import Breadcrumb from 'app/shared/breadcrumb/breadcrumb';
import '../create-new-submission/work-flow.scss';
import {
  getJournalDetails
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import MetaData from "app/modules/administration/journal-management/journal-setting/metadata";
import LoaderMain from "app/shared/Loader/loader-main";
import AuthorGuidelines from "app/modules/administration/journal-management/journal-setting/author-guidelines";
import CheckList from "app/modules/administration/journal-management/journal-setting/checklist";


const SiteSettingsManagement = () => {
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const [activeTab, setActiveTab] = useState('publishing-details');
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0];
  const dispatch = useAppDispatch();
  const path = `/journal/${parseInt(Jo_id, 10)}`;
  const loading = useAppSelector(state => state.settingsManagement.loading);
  const journalManagementLoading = useAppSelector(state => state.journalManagement.loading);


  useEffect(() => {
    dispatch(getJournalDetails(Jo_id));
  }, []);

  const BreadCrumbRoutes = [
    {name: 'Home', path: '/journal'},
    {name: 'Journals', path: '/journal'},
    {name: `${journals.key ? journals.key : ''}`, path},
    {name: 'Site Settings'},
  ];
  const handleTabClick = (tabName) => {
    setActiveTab(tabName);
  };

  return (
    <>
      <div className="row height-100">
        <div className="col-12 pt-2 ps-4">
          <Breadcrumb props={BreadCrumbRoutes}/>
        </div>
        {loading || journalManagementLoading ? <LoaderMain/> : null}
        <div className='work_flow'>
          <ul className="nav nav-tabs  mt-2">

            <button className={`nav-link ${activeTab === 'publishing-details' ? 'active' : ''} `}
                    onClick={() => handleTabClick('publishing-details')}>Journal Details
            </button>

            <button className={`nav-link ${activeTab === 'metadata' ? 'active' : ''} `}
                    onClick={() => handleTabClick('metadata')}>Metadata
            </button>

            <button className={`nav-link ${activeTab === 'authorGuidelines' ? 'active' : ''} `}
                    onClick={() => handleTabClick('authorGuidelines')}>Author Guidelines
            </button>

            <button className={`nav-link ${activeTab === 'checklist' ? 'active' : ''} `}
                    onClick={() => handleTabClick('checklist')}>CheckList
            </button>

          </ul>
          <div className="tab-content" id="nav-tabContent">
            {activeTab === 'publishing-details' && <PublishingDetails/>}
            {activeTab === 'metadata' && <MetaData/>}
            {activeTab === 'authorGuidelines' && <AuthorGuidelines/>}
            {activeTab === 'checklist' && <CheckList/>}
          </div>
        </div>
      </div>
    </>
  );
};

export default SiteSettingsManagement;
