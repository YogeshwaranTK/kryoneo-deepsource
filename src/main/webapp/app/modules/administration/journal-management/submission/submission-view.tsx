import React, {useState} from 'react';
import {useAppSelector} from "app/config/store";
import Breadcrumb from "app/shared/breadcrumb/breadcrumb";
import {useLocation, useParams} from "react-router-dom";
import SubmissionDiscussion from "app/modules/administration/journal-management/submission/submission-discussion";
import SubmissionDetails
  from "app/modules/administration/journal-management/submission/right-sidebar-submission-details/submisssion-details";
import {truncateString} from "app/config/journals.config";


const SubmissionView = () => {
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const submissionDetails = useAppSelector(state => state.submission.submissionDetails);
  const [submissionToggle, setSubmissionToggle] = useState(false);
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]
  const path = `/journal/${parseInt(Jo_id, 10)}`;

  const location = useLocation()

  const BreadCrumbRoutes = [
    {name: 'Home', path: '/journal'},
    {name: 'Journals', path: '/journal'},
    {name: `${journals.key ? journals.key : ''}`, path},
    {name: 'Submission', path: '/journal/' + Jo_id + '?submissionListRequestType=SUBMISSION'},
    {name: `${submissionDetails.title ? truncateString(submissionDetails.title) : ''}`, path: ''},
  ];

  const toggleSubmission = () => {
    setSubmissionToggle(!submissionToggle);
  }

  return (<>
    <div className="row work_flow height-100">
        <div className="col-12 pt-3 ps-2">
          <div className='d-flex px-3 '>
            <Breadcrumb props={BreadCrumbRoutes}/>
            <p className=' submission_detail ms-auto ' onClick={toggleSubmission}>Submission sfrrev Details</p>
            <SubmissionDetails isOpen={submissionToggle} onClose={toggleSubmission} submissionId={location.state}/>
            <div className={submissionToggle ? 'overlay' : ''} onClick={toggleSubmission}></div>
          </div>
          <div className="d-flex pb-3">
            <div className="p-2 w-100">
              <div className="tab-content" id="nav-tabContent">
                <SubmissionDiscussion submissionId={location.state}/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default SubmissionView;
