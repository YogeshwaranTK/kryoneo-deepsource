import React, {useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {useNavigate, useParams} from "react-router-dom";
import {Button} from "reactstrap";
import LoaderMain from "app/shared/Loader/loader-main";
import RichText from "app/shared/rich-text/rich-text";
import Select from "react-select";
import JButton from "app/shared/component/button/button";
import {Translate, translate} from "react-jhipster";
import {upperCase} from "lodash";
import {downloadFile} from "app/modules/administration/journal-management/submission/reuse-submission-component";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
  getFileExtension
} from "app/modules/administration/journal-management/review/reviewer-workflow/reuse-reviewer-component/reviewer.model";
import {
  completeReviewForReviewer, getReviewerReplyFilesRequest,
  reviewerReplyUploadFile, stateReviewerFalse, updateReviewerDiscussionStatus
} from "app/modules/administration/journal-management/review/reviewer-workflow/reviewer-list/reviewer-reducer";
import WarningModel from "app/shared/component/warning";
import UploadFilesReviewer
  from "app/modules/administration/journal-management/review/reviewer-workflow/reuse-reviewer-component/reviewer-file-upload";
import AddDiscussionReviewer
  from "app/modules/administration/journal-management/review/reviewer-workflow/add-discussion-reviewer";
import AddChatReviewer
  from "app/modules/administration/journal-management/review/reviewer-workflow/add-chat-discussion";


const ReviewerReview = (props) => {
  const {onTabChange, routeState} = props;
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0];
  const dispatch = useAppDispatch();
  const loading = useAppSelector(state => state.reviewer.loading);
  const reviewerSubmissionSingleDetails = useAppSelector(state => state.reviewer.reviewerSubmissionSingleDetails);
  const submissionId = reviewerSubmissionSingleDetails?.submissionId
  const completeReviewForReviewerSuccess = useAppSelector(state => state.reviewer.completeReviewForReviewerSuccess);
  const reviewerReplyUploadFileSuccess = useAppSelector(state => state.reviewer.reviewerReplyUploadFileSuccess);
  const reviewRoundId = reviewerSubmissionSingleDetails?.reviewRoundId
  const reviewRoundReviewerId = reviewerSubmissionSingleDetails?.id
  const [showModel, setShowModel] = useState(false);
  const navigate = useNavigate();
  const [expandedItem, setExpandedItem] = useState([]);
  const [selectDiscussionId, setSelectDiscussionId] = useState(0);
  const [reviewerFiles, setReviewerFiles] = useState([]);
  const [reviewRoundDiscussions, setReviewRoundDiscussions] = useState([]);
  const [authorEditorsComments, setAuthorEditorsComments] = useState('');
  const [editorsComments, setEditorsComments] = useState('')
  const [recommendation, setRecommendation] = useState<{
    value?: string;
    id?: string;
  }>({});
  const [isLoading, setIsLoading] = useState(false);
  const [modelWorkingType, setModelWorkingType] = useState('');

  const handleClose = () => {
    setShowModel(false);
    setModelWorkingType('')
  };

  const recommendationTypes = [
    {
      name: 'Accepted submission',
      id: 'ACCEPT_SUBMISSION'
    },
    {
      name: 'Revisions Required',
      id: 'REVISIONS_REQUIRED'
    },
    {
      name: 'Resubmit for review',
      id: 'RESUBMIT_FOR_REVIEW'
    },
    {
      name: 'Resubmit Elsewhere',
      id: 'RESUBMIT_ELSEWHERE'
    },
    {
      name: 'Decline Submission',
      id: 'DECLINE_SUBMISSION'
    },
    {
      name: 'See Comments',
      id: 'SEE_COMMENTS'
    },
  ]

  const handleWorkflowPage = (tab) => {
    onTabChange(tab)
  }


  const handleSubmit = () => {
    if (validateForm()) {
      setShowModel(true)
      setModelWorkingType('reviewerFileUpload')
    }
  }

  const warningRequestData = () => {
    const requestData = {
      reviewRoundReviewerId: routeState,
      commentToEditorAndAuthor: authorEditorsComments,
      commentToEditor: editorsComments,
      reviewerRecommendation: recommendation?.value
    }
    dispatch(completeReviewForReviewer(requestData))
  }


  useEffect(() => {
    if (completeReviewForReviewerSuccess) {
      navigate(`/journal/${Jo_id}/reviewer-dashboard`)
    } else if (reviewerReplyUploadFileSuccess) {
      setIsLoading(true)
      getReviewerReplyFilesRequest(reviewerSubmissionSingleDetails?.reviewRoundReviewerReview?.id, reviewerSubmissionSingleDetails?.reviewRoundReviewerReview?.reviewRoundId)
        .then(response => {
          setReviewerFiles(response.data)
          setIsLoading(false)
        })
        .catch(error => {
          setIsLoading(false)
          console.error(error);
        });
      dispatch(stateReviewerFalse())
    }
  }, [completeReviewForReviewerSuccess, reviewerReplyUploadFileSuccess, ])


  const authorEditorsCommentsChange = (value: string) => {
    setAuthorEditorsComments(value)
  }

  const editorsCommentsChange = (value: string) => {
    setEditorsComments(value)
  }

  const [errors, setErrors] = useState<{
    authorEditorsCommentsError?: string;
    editorsCommentsError?: string;
    recommendationError?: string;
  }>({});

  const validateForm = () => {
    const errorsValidate: {
      authorEditorsCommentsError?: string;
      editorsCommentsError?: string;
      recommendationError?: string;
    } = {};
    if (Object.keys(recommendation).length === 0) {
      errorsValidate.recommendationError = 'Select Recommendation';
    }
    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };

  useEffect(() => {
    if (reviewerSubmissionSingleDetails) {
      if (reviewerSubmissionSingleDetails?.reviewerReplyFiles !== undefined && reviewerSubmissionSingleDetails?.reviewerReplyFiles?.length !== 0) {
        setReviewerFiles(reviewerSubmissionSingleDetails?.reviewerReplyFiles)
      }
      if (reviewerSubmissionSingleDetails?.reviewerDiscussions !== undefined && reviewerSubmissionSingleDetails?.reviewerDiscussions?.length !== 0) {
        setReviewRoundDiscussions(reviewerSubmissionSingleDetails?.reviewerDiscussions)
      }
    }
  }, [reviewerSubmissionSingleDetails])

  const toggleItem = (itemId) => {
    setExpandedItem((prevExpandedItems) => {
      if (prevExpandedItems.includes(itemId)) {
        return prevExpandedItems.filter((id) => id !== itemId);
      } else {
        return [...prevExpandedItems, itemId];
      }
    });
  };


  const subDetails = (details, index) => {
    return (
      <tr key={index}>
        <td colSpan={6}>
          <div style={{border: '1px solid rgb(217, 217, 217)'}}>
            <div style={{padding: '10px 10px 0px 10px'}}>
              <div className="sub-title mx-2"
                   style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <span>{details?.userFullName}:</span>
              </div>
              <div className="p-2 bg-light border border-light mb-2"
                   dangerouslySetInnerHTML={{__html: details?.message}}></div>
              {details?.discussionMessageFiles.length !== 0 && <>
                <div className="sub-title mx-2 pb-0">
                  Attachments
                </div>
                <ul className="ps-3 m-0 pb-0">
                  {details?.discussionMessageFiles?.map((file, fileIndex) => (
                    <li className="pt-2" key={fileIndex}>
                      <span><b>{file?.fileType?.name}</b> -  </span>
                      <a className="atricle-files article-details"
                         onClick={() => downloadFile(file?.fileEndPoint, file?.file)}>
                        {file?.file}
                      </a>
                    </li>
                  ))}
                  <br/>
                </ul>
              </>
              }
            </div>
          </div>
        </td>
      </tr>
    )
  }

  const handleUploadFileReviewer = (formData) => {
    const requestData = {
      formData,
      reviewRoundReviewerId: reviewerSubmissionSingleDetails?.reviewRoundReviewerReview?.id,
      reviewRoundId: reviewerSubmissionSingleDetails?.reviewRoundReviewerReview?.reviewRoundId
    }
    dispatch(reviewerReplyUploadFile(requestData))
  }

  const handleReviewerDiscussionStatus = (discussionId, status) => {
    dispatch(updateReviewerDiscussionStatus({discussionId, reviewRoundId, reviewRoundReviewerId, status}));
  };

  return (
    <>
      {
        (showModel && modelWorkingType === "warningModel") &&
        <WarningModel showModal={showModel} handleClose={handleClose} warningRequestData={warningRequestData}
                      title={'Confirm'} subTitle={'Are You Sure You Want to Submit the Review'}/>
      }
      {
        (showModel && modelWorkingType === "reviewerFileUpload") &&
        <UploadFilesReviewer showModal={showModel} handleClose={handleClose}
                             handleUploadFileReviewer={handleUploadFileReviewer} setReviewerFiles={setReviewerFiles}

        />
      }
      {
        (showModel && modelWorkingType === "addDiscussionReviewer") &&
        <AddDiscussionReviewer showModal={showModel} handleClose={handleClose}
          submissionId={submissionId} setReviewRoundDiscussions={setReviewRoundDiscussions}
        />
      }
      {
        (showModel && modelWorkingType === "addChatSubmission") &&
        <AddChatReviewer showModal={showModel} handleClose={handleClose} selectDiscussionId={selectDiscussionId}
                          submissionId={submissionId} setReviewRoundDiscussions={setReviewRoundDiscussions}
        />
      }

      <div className="d-flex mt-3 basic_datails position-relative">
        {loading || isLoading ? <LoaderMain/> : null}
        <div className="col-12 px-3 " style={{borderRight: '1px solid #d9d9d9'}}>

          <div className="me-auto p-2 ps-0 b-bottom">
            <div className="d-flex">
              <div className="line"></div>
              <h6 className="heading pb-0 mb-0">
                Download & Review
              </h6>
            </div>
          </div>

          <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
            <h3 className='ms-2'
                style={{border: "none", paddingBottom: "0", fontSize: '18px', marginTop: '10px'}}>Review Files</h3>
          </div>

          <div className='b-bottom b-top'>
            {reviewerSubmissionSingleDetails?.reviewRoundReviewerFiles?.length > 0 ? <div className="d-flex px-3">
                <table className="table work-flow_table">
                  <thead>
                  <tr>
                    <th scope="col" className="hand">
                      #
                    </th>
                    <th scope="col" className="hand">
                      File Name
                    </th>
                    <th scope="col" className="hand">
                      File Type
                    </th>
                    <th scope="col" className="hand">
                      File Format
                    </th>
                    <th scope="col" className="hand">
                      Download
                    </th>
                  </tr>
                  </thead>
                  <tbody>
                  {reviewerSubmissionSingleDetails?.reviewRoundReviewerFiles?.map((reviewFile, index) => (
                    <tr key={index}>
                      <td>{index + 1}</td>
                      <td>{reviewFile?.file}</td>
                      <td>
                        {upperCase(reviewFile?.fileType?.name)}
                      </td>
                      <td>{upperCase(getFileExtension(reviewFile?.file))}</td>
                      <td>
                        <a
                          onClick={() => downloadFile(reviewFile?.fileEndPoint, reviewFile?.file)}
                          target="_blank"
                          rel="noopener noreferrer"
                        >
                          <FontAwesomeIcon icon="cloud-arrow-down" className="fa-cloud-arrow-down"/>
                        </a>
                      </td>
                    </tr>
                  ))}
                  </tbody>
                </table>
              </div> :
              <div className='text-center'>
                <div className='py-4 text-center'>Review Files is Empty</div>
              </div>
            }
            <div className="article-details text-end">
              <button className="article-btn"
                // onClick={downloadAllFile}
              >Download All
              </button>
            </div>
          </div>

          <div className='b-bottom' style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
            <h3 className='ms-2 ' style={{paddingBottom: "0", fontSize: '18px', marginTop: '10px'}}>Review</h3>
          </div>

          <div className='b-bottom pt-3'>
            <div className='mt-2'>
              <div className="article-head">For Author and Editor Comments:</div>
              <RichText
                value={authorEditorsComments}
                placeHolderText='For Author and Editor Comments'
                onValueChange={authorEditorsCommentsChange}
              />
            </div>
            <div className='mt-2'>
              <div className="article-head">For Editor Comments:</div>
              <RichText
                value={editorsComments}
                placeHolderText='For Editor Comments'
                onValueChange={editorsCommentsChange}
              />
            </div>
          </div>

          <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginTop: '10px'}}>
            <h3 className='ms-2' style={{border: "none", paddingBottom: "0", fontSize: '18px'}}>Reviewer Files</h3>
            <button className='btn custom-btn pd-3' style={{marginBottom: "10px"}} onClick={() => {
              setShowModel(true);
              setModelWorkingType("reviewerFileUpload")
            }}>
              UPLOAD FILE
            </button>
          </div>

          <div className='b-bottom b-top'>
            {reviewerFiles?.length > 0 ? <div className="d-flex px-3">
                <table className="table work-flow_table">
                  <thead>
                  <tr>
                    <th scope="col" className="hand">
                      #
                    </th>
                    <th scope="col" className="hand">
                      File Name
                    </th>
                    <th scope="col" className="hand">
                      File Format
                    </th>
                    <th scope="col" className="hand">
                      Download
                    </th>
                    <th scope="col" style={{width: '8%'}}>
                      <Translate contentKey="table.Action"></Translate>
                    </th>
                  </tr>
                  </thead>
                  <tbody>

                  {reviewerFiles?.map((reviewerFile, index) => (
                    <tr key={index}>
                      <td>{index + 1}</td>
                      <td>{reviewerFile?.file}</td>
                      <td>{upperCase(getFileExtension(reviewerFile?.file))}</td>
                      <td>
                        <a
                          onClick={() => downloadFile(reviewerFile?.fileEndPoint, reviewerFile?.file)}
                          target="_blank"
                          rel="noopener noreferrer"
                        >
                          <FontAwesomeIcon icon="cloud-arrow-down" className="fa-cloud-arrow-down"/>
                        </a>
                      </td>
                      <td>
                        <div className="dropdown custom-dropdown">
                          <Button color="" className="td-dot-btn" data-bs-toggle="dropdown" aria-expanded="false">
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size"/>
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size px-1"/>
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size "/>
                          </Button>
                          <ul className="dropdown-menu p-0 dropdown-menu-end">
                            <li>
                              <a className="dropdown-item"
                                // onClick={() => handleSubmissionFileDelete(item?.id)}
                              >
                                Delete
                              </a>
                            </li>
                          </ul>
                        </div>
                      </td>
                    </tr>
                  ))}
                  </tbody>
                </table>
              </div> :
              <div className='text-center'>
                <div className='py-4 text-center'>Reviewer Files is Empty</div>
              </div>
            }
            <div className="article-details text-end">
              <button className="article-btn"
                // onClick={downloadAllFile}
              >Download All
              </button>
            </div>
          </div>


          <div className='b-bottom'
               style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginTop: '10px'}}>
            <h3 className='ms-2' style={{border: "none", paddingBottom: "0", fontSize: '18px'}}>Review Discussions</h3>
            <button className='btn custom-btn pd-3' style={{marginBottom: "10px"}} onClick={() => {
              setShowModel(true);
              setModelWorkingType("addDiscussionReviewer")

            }}>
              ADD DISCUSSION
            </button>
          </div>

          {reviewRoundDiscussions?.length !== 0 ? <table className="table table-borderless b-bottom">
              <tbody>
              {reviewRoundDiscussions?.map((reviewRoundDiscussion, index) => (
                <React.Fragment key={index}>
                  <tr style={{border: null}}>

                    <td className="pb-2" style={{width: '70%'}} onClick={() => {
                      toggleItem(reviewRoundDiscussion?.topic)
                    }}>
                      <span className="titles pe-1 ms-2" style={{fontSize: '16px'}}> {reviewRoundDiscussion?.topic}</span>
                      {expandedItem.includes(reviewRoundDiscussion?.topic) ? (
                        <FontAwesomeIcon icon="angle-down" className="fa-angle-down"/>
                      ) : (
                        <FontAwesomeIcon icon="angle-right" className="fa-angle-right"/>
                      )}
                    </td>

                    <td className="align-middle pb-2">
                      <span className={reviewRoundDiscussion?.status === 'closed' ? 'red-dot' : 'green-dot'}></span>
                      {reviewRoundDiscussion?.status}
                    </td>

                    <td className="align-middle text-end pb-2">
                      <div className="dropdown custom-dropdown me-3">
                        <Button color="" className="td-dot-btn" data-bs-toggle="dropdown" aria-expanded="false">
                          <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size"/>
                          <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size px-1"/>
                          <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size "/>
                        </Button>
                        <ul className="dropdown-menu p-0 dropdown-menu-end">
                          {(reviewRoundDiscussion?.status === "open" || reviewRoundDiscussion?.status === "reopened") && (
                            <li>
                              <a className="dropdown-item"
                                onClick={() => handleReviewerDiscussionStatus(reviewRoundDiscussion?.id, "closed")}
                              >
                                Closed
                              </a>
                            </li>
                          )}
                          {(reviewRoundDiscussion?.status === "closed") && <li>
                            <a className="dropdown-item"
                              onClick={() => handleReviewerDiscussionStatus(reviewRoundDiscussion?.id, "reopened")}
                            >Re-Open</a>
                          </li>}
                        </ul>
                      </div>
                    </td>
                  </tr>

                  {expandedItem.includes(reviewRoundDiscussion?.topic) &&
                    (
                      <tr>
                        <td colSpan={6}>
                          <div style={{border: '1px solid rgb(217, 217, 217)'}}>
                            <div style={{padding: '10px 10px 0px 10px'}}>
                              <div className="sub-title mx-2"
                                   style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                                <span>Discussion Description:</span>
                                <div style={{display: 'flex', gap: '10px'}}>
                                  {/*<FontAwesomeIcon*/}
                                  {/*  icon="trash"*/}
                                  {/*  className="fa-trash"*/}
                                  {/*  style={{cursor: 'pointer', fontSize: '12px'}}*/}
                                  {/*/>*/}
                                  {/*<FontAwesomeIcon*/}
                                  {/*  icon="edit"*/}
                                  {/*  className="fa-edit"*/}
                                  {/*  style={{cursor: 'pointer', fontSize: '14px'}}*/}
                                  {/*/>*/}
                                </div>
                              </div>
                              <div className="p-2 bg-light border border-light mb-2"
                                   dangerouslySetInnerHTML={{__html: reviewRoundDiscussion?.description}}></div>
                              {reviewRoundDiscussion?.discussionFiles.length !== 0 && <>
                                <div className="sub-title mx-2  pb-0">
                                  Attachments
                                </div>
                                <ul className="ps-3 m-0 pb-0">
                                  {reviewRoundDiscussion?.discussionFiles?.map((file, fileIndex) => (
                                    <li className="pt-2" key={fileIndex}>
                                      <span><b>{file?.fileType?.name}</b> -  </span>
                                      <a className="atricle-files article-details"
                                         onClick={() => downloadFile(file?.fileEndPoint, file?.file)}>
                                        {file?.file}
                                      </a>
                                    </li>
                                  ))}
                                  <br/>
                                </ul>
                              </>
                              }
                            </div>
                          </div>
                        </td>
                      </tr>
                    )
                  }

                  {expandedItem.includes(reviewRoundDiscussion?.topic) && (
                    <>
                      {reviewRoundDiscussion?.discussionMessages?.map((detail, indexNumber) => (
                        subDetails(detail, indexNumber)
                      ))}
                    </>
                  )}


                  {expandedItem.includes(reviewRoundDiscussion?.topic) && (
                    <tr key={index}>
                      <td colSpan={5}>
                        <div className="pb-2 b-bottom">
                          <div className="tab-padding">
                            {/*{richTextVisibility[index] ? '' : (*/}
                            <div className='text-end  mb-2'>
                              <button className='btn custom-btn' onClick={() => {
                                setShowModel(true);
                                setModelWorkingType('addChatSubmission');
                                setSelectDiscussionId(reviewRoundDiscussion.id);
                              }}>
                                ADD CHAT
                              </button>
                            </div>
                            {/*)*/}
                            {/*}*/}
                          </div>
                        </div>
                      </td>
                    </tr>
                  )}

                </React.Fragment>
              ))}
              </tbody>
            </table>
            : <div className='text-center b-bottom'>
              <div className='py-4 text-center'>Discussion Not Yet Started</div>
            </div>}

          <div className='mt-2 b-bottom py-3'>
            <label className="form-label">
              Recommendation<span className="error_class">*</span> {errors.recommendationError &&
              <span className="error_class">{errors.recommendationError}</span>}
            </label>
            <Select placeholder={'Select File Type'}
                    onChange={(values) => {
                      delete errors.recommendationError;
                      setRecommendation(values)
                    }}
                    options={recommendationTypes?.map(({id, name}) => ({
                      value: id,
                      label: name,
                    }))}/>
          </div>

          <div className='pt-4 text-end'>
            <JButton JbuttonValue='PREVIOUS PAGE' type={"button"}
                     onclick={() => handleWorkflowPage("guidelinesTab")} className={"btn custom-btn-secondary me-3"}/>
            <JButton JbuttonValue='SUBMIT REVIEW' type={"button"}
                     onclick={handleSubmit}
                     className={"btn custom-btn me-3"}/>
          </div>
        </div>
        <JButton JbuttonValue={translate("buttons.Save_Draft")}
          // onclick={saveAndDraft}
                 type="button"
                 className={"btn custom-btn-secondary save_draft_btn"}/>
      </div>
    </>
  )
}

export default ReviewerReview;
