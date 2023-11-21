import React from "react";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import JButton from "app/shared/component/button/button";

const ReviewerGuidelines = (props) => {
  const {onTabChange} = props
  const loading = useAppSelector(state => state.articleDetailManagement.loading);
  const acceptRequestForReviewerSuccess = useAppSelector(state => state.reviewer.acceptRequestForReviewerSuccess);

  const handleWorkflowPage = (tab) => {
    onTabChange(tab)
  }

  return (
    <>
      <div className="d-flex mt-3 Submssion_details position-relative">
        {loading ? <LoaderMain/> : null}
        <div className="col-12 px-3 pb-3" style={{borderBottom: '1px solid #d9d9d9'}}>
          <div className="me-auto p-2 ps-0 b-bottom">
            <div className="d-flex">
              <div className="line"></div>
              <h6 className="heading pb-0 mb-0">
                Reviewer Guidelines
              </h6>
            </div>
          </div>
          {acceptRequestForReviewerSuccess?.guideLines !== undefined ? <div className='p-5 bg-light mt-3'
                                                                            dangerouslySetInnerHTML={{__html: acceptRequestForReviewerSuccess?.guideLines}}>
            </div> :
            <div className='p-5 bg-light text-center mt-3'>
              This publisher has not set any reviewer guidelines.
            </div>
          }
        </div>
      </div>
      <div className='pt-4 text-end me-2'>
        <JButton JbuttonValue='PREVIOUS PAGE' type="button"
                 onclick={() => handleWorkflowPage("requestTab")} className={"btn custom-btn-secondary me-3"}/>
        <JButton
          JbuttonValue="ACCEPT GUIDELINES & NEXT"
          onclick={() => handleWorkflowPage("reviewTab")}
          type="button"
          className="btn custom-btn"
        />
      </div>
    </>
  )
}

export default ReviewerGuidelines;
