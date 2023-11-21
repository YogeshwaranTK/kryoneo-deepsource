import React, {useState} from 'react';
import Breadcrumb from 'app/shared/breadcrumb/breadcrumb';
import ReviewerSubmissionList from "app/modules/administration/journal-management/review/reviewer-workflow/reviewer-list/reviewer-list-management";
import {reviewWorkflowConfig} from "app/config/review.config";

export const ReviewerSubmissionsManagement = () => {
  const [activeTab, setActiveTab] = useState('pending');

  const BreadCrumbRoutes = [
    {name: 'Home', path: '/journal'},
    {name: 'Journals', path: '/journal'},
    {name: 'Reviewer list'},
  ];

  const handleTabClick = (tabName) => {
    setActiveTab(tabName);
  };

  return (
    <>
      <div className="row ">
        <div className="col-12 pt-2 ps-4">
          <Breadcrumb props={BreadCrumbRoutes}/>
        </div>
        <div className='work_flow'>
          <ul className="nav nav-tabs  mt-2">
            <button className={`nav-link ${activeTab === 'pending' ? 'active' : ''} `}
                    onClick={() => handleTabClick('pending')}>Pending
            </button>
            <button className={`nav-link ${activeTab === 'completed' ? 'active' : ''} }`}
                    onClick={() => handleTabClick('completed')}>Completed
            </button>
          </ul>
          <div className="tab-content" id="nav-tabContent">
            {activeTab === 'pending' &&
              <ReviewerSubmissionList reviewerStatus={reviewWorkflowConfig.reviewerStatusPending}/>}
            {activeTab === 'completed' &&
              <ReviewerSubmissionList reviewerStatus={reviewWorkflowConfig.reviewerStatusCompleted}/>}
          </div>
        </div>
      </div>
    </>
  )
}

export default ReviewerSubmissionsManagement
