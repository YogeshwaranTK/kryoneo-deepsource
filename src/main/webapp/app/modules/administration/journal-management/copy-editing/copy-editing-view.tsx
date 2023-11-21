import React, {useState} from 'react';
import {useAppSelector} from "app/config/store";
import Breadcrumb from "app/shared/breadcrumb/breadcrumb";
import {useLocation, useParams} from "react-router-dom";
import CopyEditingSubmissionDetails
  from "app/modules/administration/journal-management/copy-editing/submission-details-copy-editing";
import CopyEditingGalley from "app/modules/administration/journal-management/copy-editing/copy-editing-galley";
import CopyEditingDiscussion from "app/modules/administration/journal-management/copy-editing/copy-editing-discussion";
import SubmissionDetails
  from "app/modules/administration/journal-management/submission/right-sidebar-submission-details/submisssion-details";

const CopyEditingViewList = () => {
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]
  const path = `/journal/${parseInt(Jo_id, 10)}`;
  const location = useLocation()


  const BreadCrumbRoutes = [
    {name: 'Home', path: '/journal'},
    {name: 'Journals', path: '/journal'},
    {name: `${journals.key ? journals.key : ''}`, path},
    {name: 'Copy-editing', path: '/journal/' + Jo_id + '?submissionListRequestType=COPY_EDITING'},
  ];

  const [copyEditingToggle, setCopyEditingToggle] = useState(false);
  const toggleCopyEditing = () => {
    setCopyEditingToggle(!copyEditingToggle);
  }

  return (<>
      <div className="row work_flow height-100">
        <div className="col-12 pt-3 ps-2 ">
          <div className='d-flex px-3 '>
            <Breadcrumb props={BreadCrumbRoutes}/>
            <p className=' submission_detail ms-auto' onClick={toggleCopyEditing}>Submission Details</p>
            <SubmissionDetails isOpen={copyEditingToggle} onClose={toggleCopyEditing}
                               submissionId={location.state}/>
            <div className={copyEditingToggle ? 'overlay' : ''} onClick={toggleCopyEditing}></div>
          </div>
          <div className="d-flex pb-3">
            <div className="p-2 w-100">
              <div className="tab-content" id="nav-tabContent">
                {/* {activeTab === 'Submitted_List' && <CopyEditingSubmissionDetails  submissionId={location.state} />} */}
                <CopyEditingDiscussion submissionId={location.state}/>
                <CopyEditingGalley submissionId={location.state}/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default CopyEditingViewList;
