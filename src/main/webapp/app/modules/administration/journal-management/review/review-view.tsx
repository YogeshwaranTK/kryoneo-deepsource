import React, {useEffect, useState} from 'react';
import {useAppDispatch, useAppSelector} from "app/config/store";
import Breadcrumb from "app/shared/breadcrumb/breadcrumb";
import {useLocation, useParams} from "react-router-dom";
import ReviewDiscussion
  from "app/modules/administration/journal-management/review/peerReviewDiscussion/reviewDiscussion";
import {truncateString} from "app/config/journals.config";
import {getSubmissionDetails} from "app/modules/administration/journal-management/submission/submission.reducer";
import SubmissionDetails
  from "app/modules/administration/journal-management/submission/right-sidebar-submission-details/submisssion-details";
import {Translate} from "react-jhipster";

const ReviewViewList = () => {
  const dispatch = useAppDispatch();
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const submissionDetails = useAppSelector(state => state.submission.submissionDetails);
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0];
  const path = `/journal/${parseInt(Jo_id, 10)}`;
  const location = useLocation();
  const submissionId = location?.state;
  const [reviewToggle, setReviewToggle] = useState(false);


  useEffect(() => {
    if (typeof submissionId !== 'object') {
      dispatch(getSubmissionDetails(Number(submissionId)));
    }
  }, [])

  const BreadCrumbRoutes = [
    {name: 'Home', path: '/journal'},
    {name: 'Journals', path: '/journal'},
    {name: `${journals.key ? journals.key : ''}`, path},
    {name: 'Reviews', path: '/journal/' + Jo_id + '?submissionListRequestType=PEER_REVIEW'},
    {name: `${submissionDetails.title ? truncateString(submissionDetails.title) : ''}`, path: ''},
  ];

  const toggleReview = () => {
    setReviewToggle(!reviewToggle);
  }

  return (
    <>
      <div className="row work_flow height-100">
        <div className="col-12 pt-2 ps-4">
          <div className='d-flex px-3 '>
            <Breadcrumb props={BreadCrumbRoutes}/>
            <p className=' submission_detail ms-auto' onClick={toggleReview}><Translate contentKey="Submission_Title.Submission_Details"></Translate> </p>
            <SubmissionDetails isOpen={reviewToggle} onClose={toggleReview} submissionId={location.state}/>
            <div className={reviewToggle ? 'overlay' : ''} onClick={toggleReview}></div>
          </div>
          <div className="d-flex pb-3">
            <div className="p-2 w-100">
              <div className="tab-content" id="nav-tabContent">
                <ReviewDiscussion submissionId={submissionId}/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default ReviewViewList;
