import React, {useEffect, useState} from 'react';
import './work-flow.scss';
import '../contributors/user-contributors.scss'
import Breadcrumb from 'app/shared/breadcrumb/breadcrumb';
import {useLocation, useParams} from "react-router-dom";
import {useAppDispatch, useAppSelector} from "app/config/store";
import BasicDetails
  from "app/modules/administration/journal-management/create-new-submission/basic-details";
import UploadFile
  from "app/modules/administration/journal-management/create-new-submission/upload-file";
import Contribution
  from "app/modules/administration/journal-management/create-new-submission/contribution";
import {translate, Translate} from "react-jhipster";
import {getSubmissionDetails} from "app/modules/administration/journal-management/submission/submission.reducer";
import {
  getJournalSubmissionLangListRequest, JournalFileTypeListRequest, journalFileTypeListRequest
} from "app/modules/administration/journal-management/create-new-submission/work-flow.reducer";

export const SubmissionManagement = () => {
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const location = useLocation();
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0];
  const path = `/journal/${parseInt(Jo_id, 10)}`;
  const [activeTab, setActiveTab] = useState('Basic_Details_Tab');
  const [BasicError, setBasicError] = useState('');
  const dispatch = useAppDispatch();

  const BreadCrumbRoutes = [
    {name: translate("Submission_Create_Breadcrumb.Home"), path: '/journal'},
    {name: translate("Submission_Create_Breadcrumb.Journals"), path: '/journal'},
    {name: `${journals.key ? journals.key : ''}`, path},
    {name: translate("Submission_Create_Breadcrumb.Create_Submission")},
  ];

  const handleTabChange = (tab) => {
    if ((tab === 'Basic_Details_Tab' || tab === 'Upload_File_tab' || tab === 'Contribution_tab')) {
      setActiveTab(tab);
    }
  };

  useEffect(()=>{
    if (Number(location.state) !== 0) {
      dispatch(getJournalSubmissionLangListRequest());
      dispatch(JournalFileTypeListRequest());
    }
  },[])

  return (<>
      <div className="row work_flow">
        <div className="col-12 pt-3 ps-4">
          <Breadcrumb props={BreadCrumbRoutes}/>
          <div className="d-flex   pb-3">
            <div className="p-2 w-100">
              <ul className="nav nav-tabs">
                <button
                  className={`nav-link ${activeTab === 'Basic_Details_Tab' ? 'active' : ''}
                ${BasicError === 'error' ? 'workflow_error' : ''}
                `}
                  // ${FinalSubmissionError?.basicDetails?.length > 0 ? 'workflow_error' : ''}
                  onClick={() => handleTabChange('Basic_Details_Tab')}><Translate
                  contentKey="Workflow_Title.Basic_details">Basic Details</Translate></button>
                <button className={`nav-link ${activeTab === 'Contribution_tab' ? 'active' : ''} `}
                        onClick={() => handleTabChange('Contribution_tab')}>
                  Contributors
                </button>
                <button className={`nav-link ${activeTab === 'Upload_File_tab' ? 'active' : ''}`}
                        onClick={() => handleTabChange('Upload_File_tab')}>
                  <Translate contentKey="Workflow_Title.Upload_File"> Upload File</Translate>
                </button>
              </ul>
              <div className="tab-content" id="nav-tabContent">
                {activeTab === 'Basic_Details_Tab' &&
                  <BasicDetails setBasicError={setBasicError} BasicError={BasicError} onTabChange={handleTabChange}
                                 routeState={location.state}/>}
                {activeTab === 'Upload_File_tab' &&
                  <UploadFile routeState={location.state}
                  />}
                {activeTab === 'Contribution_tab' &&
                  <Contribution onTabChange={handleTabChange} routeState={location.state}
                  />}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default SubmissionManagement;
