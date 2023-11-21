import React, {useEffect, useState} from 'react';
import '../../create-new-submission/work-flow.scss';
import Breadcrumb from 'app/shared/breadcrumb/breadcrumb';
import {useLocation, useParams} from "react-router-dom";
import ReviewerGuidelines
  from "app/modules/administration/journal-management/review/reviewer-workflow/reviewer-guidelines";
import ReviewerReview
  from "app/modules/administration/journal-management/review/reviewer-workflow/reviewer-review";
import {submissionWorkflowConfig} from "app/config/submission-workflow.config";
import ReviewerRequestDetails
  from "app/modules/administration/journal-management/review/reviewer-workflow/reviewer-request-details";
import {getReviewerSubmissionDetails} from "app/modules/administration/journal-management/review/reviewer-workflow/reviewer-list/reviewer-reducer";
import {useAppDispatch, useAppSelector} from "app/config/store";


export const ReviewerViewManagement = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]
  const [activeTab, setActiveTab] = useState(submissionWorkflowConfig.reviewerWorkflowActivePage);
  const reviewerSubmissionSingleDetails = useAppSelector(state => state.reviewer.reviewerSubmissionSingleDetails);

  const journalTitle = (e) => {
    if (e.length > 18) {
      return e.slice(0, 18) + '...';
    } else {
      return e
    }
  }

  const BreadCrumbRoutes = [
    {name: 'Home', path: '/journal'},
    {name: 'Journals', path: '/journal'},
    {name: 'Reviews', path: '/journal/' + Jo_id + '/reviewer-dashboard'},
    {
      name: `${reviewerSubmissionSingleDetails?.submission?.title ? journalTitle(reviewerSubmissionSingleDetails?.submission?.title) : ''}`,
      path: ''
    },
  ];


  useEffect(() => {
    if (Number(location.state) !== 0) {
      dispatch(getReviewerSubmissionDetails(Number(location.state)));
    }
  }, []);

  const handleTabChange = (tab) => {
    setActiveTab(tab)
  };

  return (
    <>
      <div className="row work_flow">
        <div className="col-12 pt-3 ps-4">
          <Breadcrumb props={BreadCrumbRoutes}/>
          <div className="d-flex   pb-3">
            <div className="p-2 w-100">
              <ul className="nav nav-tabs">

                <button
                  className={`nav-link ${activeTab === 'requestTab' ? 'active' : ''}`}
                  onClick={() => handleTabChange('requestTab')}> Request
                </button>

                <button className={`nav-link ${activeTab === 'guidelinesTab' ? 'active' : ''} `}
                        onClick={() => handleTabChange('guidelinesTab')}>
                  Guidelines
                </button>

                <button className={`nav-link ${activeTab === 'reviewTab' ? 'active' : ''}`}
                        onClick={() => handleTabChange('reviewTab')}>
                  Download & Review
                </button>
              </ul>

              <div className="tab-content" id="nav-tabContent">

                {activeTab === 'requestTab' && <ReviewerRequestDetails onTabChange={handleTabChange}
                                                                       routeState={location.state}
                />
                }
                {activeTab === 'guidelinesTab' && <ReviewerGuidelines onTabChange={handleTabChange}
                                                                      routeState={location.state}
                />
                }
                {activeTab === 'reviewTab' && <ReviewerReview onTabChange={handleTabChange}
                                                              routeState={location.state}
                />
                }
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default ReviewerViewManagement;
