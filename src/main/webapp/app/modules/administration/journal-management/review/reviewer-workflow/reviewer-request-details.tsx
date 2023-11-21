import React, {useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {useNavigate, useParams} from "react-router-dom";
import {Modal} from "reactstrap";
import LoaderMain from "app/shared/Loader/loader-main";
import JButton from "app/shared/component/button/button";
import {
  acceptRequestForReviewer, declineRequestForReviewer, stateReviewerFalse
} from "app/modules/administration/journal-management/review/reviewer-workflow/reviewer-list/reviewer-reducer";
import {formatDate} from "app/config/componance-config";
import RejectReviewer
  from "app/modules/administration/journal-management/review/reviewer-workflow/reuse-reviewer-component/decline-review-request";
import {
  blindTypeSet
} from "app/modules/administration/journal-management/review/reviewer-workflow/reuse-reviewer-component/reviewer.model";


const ReviewRequestDetails = (props) => {
  const dispatch = useAppDispatch();
  const reviewerSubmissionSingleDetails = useAppSelector(state => state.reviewer.reviewerSubmissionSingleDetails);
  const acceptRequestForReviewerSuccess = useAppSelector(state => state.reviewer.acceptRequestForReviewerSuccess);
  const declineRequestForReviewerSuccess = useAppSelector(state => state.reviewer.declineRequestForReviewerSuccess);
  const loading = useAppSelector(state => state.reviewer.loading);
  const [showModel, setShowModel] = useState(false);
  const [termsStatus, setTermsStatus] = useState(false)
  const [errors, setErrors] = useState<{
    termStatusError?: string;
  }>({});
  const navigate = useNavigate();
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]

  const handleClose = () => {
    setShowModel(false);
    setShowModelType("")
  };


  useEffect(() => {
    if (acceptRequestForReviewerSuccess) {
      props.onTabChange("guidelinesTab")
      dispatch(stateReviewerFalse());
    } else if (declineRequestForReviewerSuccess) {
      handleClose()
      navigate(`/journal/${Jo_id}/reviewer-dashboard`)
    }
  }, [acceptRequestForReviewerSuccess, declineRequestForReviewerSuccess])


  const handleTermsCheckbox = () => {
    if (termsStatus) {
      setTermsStatus(false);
    } else {
      const updatedReactData = {...errors};
      delete updatedReactData["termStatusError"];
      setErrors(updatedReactData);
      setTermsStatus(true);
    }
  }

  const handleSubmit = () => {
    if (errorCheck()) {
      dispatch(acceptRequestForReviewer(props.routeState))
    }
  }

  const requestSendFunction = (data) => {
    const requestData = {description: data?.discussionDescription, reviewRoundReviewerId: props.routeState}
    dispatch(declineRequestForReviewer(requestData))
  }

  const handleDeclineSubmit = () => {
    setShowModel(true)
    setShowModelType("declineRequest")
  }

  const errorCheck = () => {
    const errorsValidate: { termStatusError?: string; } = {};
    if (!termsStatus) {
      errorsValidate.termStatusError = 'Terms & Conditions Required';
    }
    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  }
  const [showModelType, setShowModelType] = useState("")

  return (
    <>
      <div className="d-flex mt-3 Submittd_list position-relative">
        {showModelType === "declineRequest" && <RejectReviewer showModal={showModel} handleClose={handleClose}
                                                               requestSendFunction={requestSendFunction}
        />
        }
        {loading ? <LoaderMain/> : null}
        <div className="col-12 px-3 " style={{borderRight: '1px solid #d9d9d9'}}>
          <div className="me-auto p-2 ps-0 b-bottom">
            <div className="d-flex">
              <div className="line"></div>
              <h6 className="heading pb-0 mb-0">
                Request for Review
              </h6>
            </div>
            <p className="title-description pt-1">
              You have been selected as a potential reviewer of the following submission. Below is an overview of the
              submission, as well as the timeline for this review. We hope that you are able to participate
            </p>
          </div>
          <div className='b-bottom'>
            <div className="d-flex px-3 mt-4">
              <div className="col-4">
                <p className="article-head">Article Title</p>
              </div>
              <div className="col-8">
                <div
                  className={`journal-profile color-${reviewerSubmissionSingleDetails?.submissionTitle?.slice(0, 1).toUpperCase()}`}>
                  {reviewerSubmissionSingleDetails?.submissionTitle?.slice(0, 2).toUpperCase()}
                </div>
                <div className="ps-3 ms-3">
                  <p className="article-details m-1">{reviewerSubmissionSingleDetails?.submissionTitle}</p>
                </div>
              </div>
            </div>
            <div className="d-flex px-3 mt-1">
              <div className="col-4">
                <p className="article-head">Abstract</p>
              </div>
              <div className="col-8">
                <div className="article-details">
                  {reviewerSubmissionSingleDetails?.submissionAbstract !== '<p><br></p>' && reviewerSubmissionSingleDetails?.submissionAbstract !== null ?
                    <button className="article-btn" onClick={() => {
                      setShowModel(true);
                      setShowModelType("abstract")
                    }}>View</button>
                    : '-'}
                  {showModelType === "abstract" &&
                    <Modal isOpen={showModel} toggle={handleClose} backdrop="static" autoFocus={false}>
                      <div className="modal-content">
                        <div className="modal-header">
                          <div className="d-flex">
                            <div className="line"></div>
                            <h5 className="m-0">Abstract</h5>
                          </div>
                          <button type="button" className="btn-close" onClick={handleClose}></button>
                        </div>
                        <div className="modal-body ps-4">
                          <div
                            dangerouslySetInnerHTML={{__html: reviewerSubmissionSingleDetails?.submissionAbstract}}/>
                        </div>
                        <div className="modal-footer">
                          <button className="btn btn--primary" type="submit" onClick={handleClose}>
                            CLOSE
                          </button>
                        </div>
                      </div>
                    </Modal>}

                </div>
              </div>
            </div>

            <div className="d-flex px-3 mt-1">
              <div className="col-4">
                <p className="article-head">Review Type</p>
              </div>
              <div className="col-8">
                {reviewerSubmissionSingleDetails?.reviewerReviewType ? blindTypeSet(reviewerSubmissionSingleDetails?.reviewerReviewType) : '-'}
              </div>
            </div>
          </div>

          <div className='b-bottom pt-3'>
            <div className="d-flex px-3">
              <div className="col-4">
                <p className="article-head">Review Files</p>
              </div>
              <div className="col-8">
                <ul className="ps-3 m-0">
                  {reviewerSubmissionSingleDetails?.reviewRoundReviewer?.reviewerFiles?.map((file) => (
                    <li className="pt-2" key={file?.id}>
                      <span><b>{file?.submissionArticleTypeName}</b> -  </span>
                      <a className="atricle-files article-details" href={file?.filePath} download={file?.fileName}>
                        {file?.fileName}
                      </a>
                    </li>
                  ))}
                  <br/>
                </ul>

                <div className="article-details">
                  <button className="article-btn"
                    // onClick={downloadAllFile}
                  >Download All
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div className='b-bottom pt-3'>

            <div className="d-flex px-3 ">
              <div className="col-4">
                <p className="article-head">{`Editor's Request Date`}</p>
              </div>
              <div className="col-8">
                {reviewerSubmissionSingleDetails?.reviewRequestedDate ? formatDate(reviewerSubmissionSingleDetails?.reviewRequestedDate) : "-"}
              </div>
            </div>

            <div className="d-flex px-3 mt-1">
              <div className="col-4">
                <p className="article-head">{`Response Due Date`}</p>
              </div>
              <div className="col-8">
                {formatDate(reviewerSubmissionSingleDetails?.responseDueDate)}
              </div>
            </div>

            <div className="d-flex px-3 mt-1">
              <div className="col-4">
                <p className="article-head">{`Review Due Date`}</p>
              </div>
              <div className="col-8">
                {formatDate(reviewerSubmissionSingleDetails?.reviewDueDate)}
              </div>
            </div>

          </div>
          <div className="submission ps-2 mt-3">
            <input className={`form-check-input ms-0 ${!termsStatus ? 'agree_error' : ''}`}
                   type="checkbox" name="agree"
                   checked={termsStatus || reviewerSubmissionSingleDetails?.reviewStatus === "REQUEST_ACCEPTED"}
                   onChange={handleTermsCheckbox}
            />
            <label>
              <p className="article-head">Yes, I agree to have my data collected and stored according to the privacy
                statement.<span
                  className="error_class">*</span> {errors.termStatusError &&
                  <span className="error"> - {errors.termStatusError}</span>}</p>
            </label>
            <br/>
          </div>
          <div className="pe-2 m-top text-end">

            {reviewerSubmissionSingleDetails?.reviewStatus !== "REQUEST_ACCEPTED" ? <>
                <JButton JbuttonValue="DECLINE REVIEW REQUEST"
                         onclick={handleDeclineSubmit}
                         type={"button"}
                         className={"btn custom-btn-secondary  me-2"}/>

                <JButton JbuttonValue="ACCEPT REVIEW & NEXT"
                         onclick={handleSubmit}
                         type={"button"}
                         className={"btn custom-btn"}/>
              </>
              :
              <JButton JbuttonValue="NEXT"
                       onclick={() => {
                         props.onTabChange("guidelinesTab")
                       }}
                       type={"button"}
                       className={"btn custom-btn"}/>}
          </div>
        </div>
      </div>
    </>
  )
}

export default ReviewRequestDetails;
