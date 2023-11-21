import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from "app/config/store";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useLocation } from "react-router-dom";
import { Button } from "reactstrap";
import { upperCase } from "lodash";
import ButtonPermissionsReview from "app/modules/administration/journal-management/review/buttons-permission";
import {
  createRoundreview, downloadReviewerFile, getReviewDiscussionListRaw, getReviewrRoundList, getRoundreview,
  postReviewRoundFilesUpload, reviewAddChatDiscussionRequest, reviewerAdd, reviewreset, stateFalse
} from '../reviewReducer';
import UploadFilesReview from './upload-file-review';
import '../review.scss'
import AddChatReview from '../add-chat-review';
import AddReviewer from '../add-reviewer';
import AddDiscussionReview from "app/modules/administration/journal-management/review/review-add-discussion";
import { updateDiscussStatus } from "app/modules/administration/journal-management/review/reviewReducer";
import CancelReviewer from "app/modules/administration/journal-management/review/cancel-reviewer";
import { formatDate } from "app/config/componance-config";
import { Translate } from 'react-jhipster';

const ReviewDiscussion = (props) => {
  const { submissionId } = props
  const dispatch = useAppDispatch()
  const location = useLocation()
  const queryParams = new URLSearchParams(location.search);
  const submissionListRequestType = queryParams.get('submissionListRequestType');
  const [reviewRoundId, setReviewRoundId] = useState('')
  const [activeTab, setActiveTab] = useState(
    submissionListRequestType === "PEER_REVIEW" ? "Review" :
      submissionListRequestType === "COPY_EDITING" ? "Copyediting" :
        submissionListRequestType === 'PRODUCTION' ? 'Production' :
          "Submissions"
  );
  const reviewRound = useAppSelector(state => state.review.reviewRound);
  const CreateReviewSuccess = useAppSelector(state => state.review.CreateReviewSuccess);
  const ReviewRoundDatas = useAppSelector(state => state.review.ReviewRoundData)
  const fileUploadReviewReadyFilesSuccess = useAppSelector(state => state.review.fileUploadReviewReadyFilesSuccess);
  const fileUploadReview = useAppSelector(state => state.review.fileuploadReview);
  const reviewRoundDetails = useAppSelector(state => state.review.reviewRoundDetails)
  const addChatDiscussionSuccess = useAppSelector(state => state.review.addChatDiscussionSuccess);
  const reviewAddReviewerPayload = useAppSelector(state => state.review.reviewAddReviewerPayload);
  const addReviewSuccess = useAppSelector(state => state.review.addReviewSuccess)
  const [reviewFiles, setReviewFiles] = useState([])
  const [participantList, setParticipantList] = useState([]);
  const [reviewDiscussionList, setReviewDiscussionList] = useState([])
  const [expandedItem, setExpandedItem] = useState([]);
  const [selectDiscussionId, setSelectDiscussionId] = useState(0)
  const [loadingLocal, setLoadingLocal] = useState(false)
  const [roundReviewer, setRoundReviewer] = useState([])
  const [showModelType, setShowModelType] = useState('');
  const updateDiscussStatusSucess = useAppSelector(state => state.review.updateDiscussStatusSuccess);
  const [reviewRoundData, setReviewRoundData] = useState([])
  const [showModal, setShowModal] = useState(false);
  const [modelWorkingType, setModelWorkingType] = useState('')

  useEffect(() => {
    if (reviewRoundDetails.length !== 0) {
      setReviewFiles(reviewRoundDetails.reviewRoundFiles)
      setParticipantList(reviewRoundDetails?.reviewRoundContributors)
      setReviewDiscussionList(reviewRoundDetails?.reviewRoundDiscussions)
      setRoundReviewer(reviewRoundDetails?.reviewRoundReviewers)
    }

  }, [reviewRoundDetails])

  const getFileExtension = (filename) => {
    const lastDotIndex = filename.lastIndexOf(".");
    if (lastDotIndex !== -1) {
      return filename.substring(lastDotIndex);
    } else {
      return null;
    }
  }




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
          <div style={{ border: '1px solid rgb(217, 217, 217)' }}>
            <div style={{ padding: '10px 10px 0px 10px' }}>
              {details?.reviewRoundDiscussionMessageFiles.length !== 0 &&
                <>
                  <div className="sub-title mx-2"
                    style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <span>{details?.userFullName}:</span>

                  </div>
                  <div className="p-2 bg-light border border-light mb-2"
                    dangerouslySetInnerHTML={{ __html: details?.message }}></div>
                </>
              }
              {details?.reviewRoundDiscussionMessageFiles.length !== 0 && <>
                <div className="sub-title mx-2 pb-0">
                  <Translate contentKey="Reviewtitle.Attachments"></Translate>
                </div>
                <ul className="ps-3 m-0 pb-0">
                  {details?.reviewRoundDiscussionMessageFiles?.map((file, fileIndex) => (
                    <li className="pt-2" key={fileIndex}>
                      <span><b>{file?.fileType?.name}</b> -  </span>
                      <a className="atricle-files article-details"
                      //  onClick={() => downloadFile(file?.fileEndPoint, file?.file)}
                      >
                        {file?.file}
                      </a>
                    </li>
                  ))}
                  <br />
                </ul>
              </>
              }
            </div>
          </div>
        </td>
      </tr>
    )
  }

  const reviewDetails = (reviewer, index) => {
    return (
      <tr>
        <td colSpan={8}>
          <div className="pb-2 b-bottom">
            <div className="tab-padding">
              <p>
                <span className="highlights"><Translate contentKey="Reviewtitle.Request_Type :"></Translate></span>
                <span className="ms-4">{reviewer?.reviewStatus}</span>
              </p>
              <p>
                <span className="highlights"><Translate contentKey="Reviewtitle.Editor_Comments :"></Translate> </span>
                <span className="ms-4" dangerouslySetInnerHTML={{ __html: reviewer?.editorComment }}></span>
              </p>
              <p>
                <span className="highlights"><Translate contentKey="Reviewtitle.Editor_&_Author_Comments :"></Translate></span>
                <span className="ms-4" dangerouslySetInnerHTML={{ __html: reviewer?.editorAndAuthorComment }}></span>
              </p>
              <p>
                <span className="highlights"><Translate contentKey="Reviewtitle.Editor_Comments :"></Translate>  </span>
                <span className="ms-4">{reviewer?.reviewerRecommendation}</span>
              </p>
              <p>
                <span className="highlights"><Translate contentKey="Reviewtitle.Editor_Comments :"></Translate> </span>
                <span className="ms-4">{reviewer?.reviewerRecommendation}</span>
              </p>

              <p>
                <span className="highlights"><Translate contentKey="Reviewtitle.Reviewer_Files"></Translate>  </span>
                <ul className="ps-3 m-0">
                  {reviewer.reviewerFiles?.map((file) => (
                    <li className="pt-2" key={file?.id}>
                      <a className="atricle-files article-details" href={file?.filePath}
                        download={file?.fileName}>
                        {file?.fileName}
                      </a>
                    </li>
                  ))}
                  <br />
                </ul>
              </p>

            </div>
          </div>
        </td>
      </tr>

    )
  }


  const handleClose = () => {
    setShowModal(false);
    setModelWorkingType('')
  };

  const handleAddReviewerFileUpload = (requestData) => {
    dispatch(postReviewRoundFilesUpload(requestData))
  }

  const handleDiscussionStatus = (discussionId, status) => {
    dispatch(updateDiscussStatus({ reviewRoundId, discussionId, status }));
  };


  const handleFileUploadModelRequest = (requestData) => {
    if (modelWorkingType === 'reviewReadyFiles') {
      dispatch(postReviewRoundFilesUpload(requestData))
    }
    if (modelWorkingType === 'addChatReview') {
      dispatch(reviewAddChatDiscussionRequest(requestData))
    }
    if (modelWorkingType === 'reviewerAdd') {
      dispatch(reviewerAdd(requestData))
    }

  }


  useEffect(() => {
    if (fileUploadReviewReadyFilesSuccess) {
      const mergedata = [...reviewFiles, ...fileUploadReview]
      setReviewFiles(mergedata)
      if (modelWorkingType !== 'reviewerAdd') {
        handleClose();
      }
    } else if (addChatDiscussionSuccess) {
      setLoadingLocal(true)
      getReviewDiscussionListRaw(reviewRoundId).then(response => {
        setReviewDiscussionList(response.data)
        setLoadingLocal(false)
      })
        .catch(error => {
          setLoadingLocal(false)
          console.error(error);
        });

      handleClose()
      dispatch(stateFalse())
    }

  }, [fileUploadReviewReadyFilesSuccess, addChatDiscussionSuccess]);

  useEffect(() => {
    if (addReviewSuccess) {
      handleClose();
      const mergingdata = [...roundReviewer, reviewAddReviewerPayload]
      setRoundReviewer(mergingdata)

    }
    dispatch(stateFalse())
  }, [addReviewSuccess])


  useEffect(() => {
    if (typeof submissionId !== 'object') {
      dispatch(getRoundreview({ submissionId: Number(submissionId) }));
    }
  }, [])


  useEffect(() => {
    if (ReviewRoundDatas.length !== 0) {
      setReviewRoundData(ReviewRoundDatas)
      const lastReviewRound = ReviewRoundDatas[ReviewRoundDatas.length - 1];
      setActiveTab(lastReviewRound.name);
      setReviewRoundId(lastReviewRound.id);
    }

  }, [ReviewRoundDatas])


  useEffect(() => {
    if (CreateReviewSuccess) {
      const reviewpostdata = [...reviewRoundData, reviewRound]
      setReviewRoundData(reviewpostdata)
      dispatch(reviewreset())
      setActiveTab(reviewRound.name);
      setReviewRoundId(reviewRound.id)
    }

  }, [CreateReviewSuccess])

  useEffect(() => {
    if (reviewRoundId !== '' && reviewRoundId !== undefined) {
      dispatch(getReviewrRoundList({ submissionId, reviewRoundId }));
    }
  }, [reviewRoundId]);

  useEffect(() => {
    if (updateDiscussStatusSucess) {
      dispatch(getReviewrRoundList({ submissionId, reviewRoundId }));
    }
  }, [updateDiscussStatusSucess]);

  const handleAddNewTab = async () => {
    await dispatch(createRoundreview({ submissionId }))
  };

  const handleTabClick = (tabName) => {
    setActiveTab(tabName.name);
    setReviewRoundId(tabName.id)

  };

  const downloadAllFile = () => {
    downloadReviewerFile(submissionId, reviewRoundId).then(response => {
      const blob = new Blob([response.data]);
      const url = URL.createObjectURL(blob);
      const contentDisposition = response.headers['content-disposition'];
      const fileName = contentDisposition.split('filename=')[1].trim() || 'download.zip';
      const link = document.createElement('a');
      link.href = url;
      link.download = fileName;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url);
    })
      .catch(error => {
        console.error(error);
      });
  };

  return (
    <div className="d-flex mt-3 Submittd_list">
      {showModelType === "review-addDiscussion" &&
        <AddDiscussionReview showModal={showModal} handleClose={handleClose}
          participantLists={participantList}
          submissionId={submissionId}
          reviewRoundId={reviewRoundId}
          productionDiscussionList={reviewDiscussionList}
          setProductionDiscussionList={setReviewDiscussionList}
        />}
      <div className="col-9 position-relative " style={{ borderRight: '1px solid #d9d9d9' }}>
        <div className="work_flow d-flex ">
          <div className="tabs-container review_round">
            <ul className="nav nav-tabs mt-2 flex-nowrap">
              {reviewRoundData.length > 0 ? (
                reviewRoundData.map((data) => (
                  <li
                    key={data.id}
                    className={`nav-link text-nowrap ${activeTab === data.name ? 'active' : ''}`}
                    onClick={() => handleTabClick(data)}
                  >
                    {data.name}
                  </li>
                ))
              ) : (
                <p><Translate contentKey="Reviewtitle.+Add_Round"></Translate></p>
              )}
            </ul>
          </div>
          <div className='contribution' style={{ borderBottom: '1px solid #0e3aab' }}>
            <div className="contribution_top">

              <p
                className='submission_detail p-2'
                onClick={handleAddNewTab}
              >
                <Translate contentKey="Reviewtitle.+Add_Round"></Translate>
              </p>
            </div>
          </div>
        </div>


        <div className="tab-content pt-3" id="nav-tabContent">

          <div>
            <div className="px-3 position-relative" style={{ borderRight: '1px solid #d9d9d9' }}>
              {(showModal && modelWorkingType === 'reviewReadyFiles') &&
                <UploadFilesReview showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                  reviewRoundId={reviewRoundId}
                  handleFileUploadModelRequest={handleFileUploadModelRequest}
                />}
              {(showModal && modelWorkingType === 'addChatReview') &&
                <AddChatReview showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                  reviewRoundId={reviewRoundId}
                  handleFileUploadModelRequest={handleFileUploadModelRequest}
                  selectDiscussionId={selectDiscussionId} />}
              <div className='review_align_center'>
                <h3 className='ms-2' style={{ border: "none", paddingBottom: "0" }}><Translate contentKey="Reviewtitle.Review_Files"></Translate></h3>
                <button className='btn custom-btn pd-3' style={{ marginBottom: "10px" }} onClick={() => {
                  setShowModal(true);
                  setModelWorkingType("reviewReadyFiles")
                }}>
                  <Translate contentKey="Reviewtitle.UPLOAD_FILE"></Translate>
                </button>
              </div>
              <div className='b-bottom b-top'>
                {reviewFiles?.length > 0 ?
                  <div className="d-flex px-3">
                    <table className="table work-flow_table">
                      <thead>
                        <tr>
                          <th scope="col" className="hand text-nowrap">
                            #
                          </th>
                          <th scope="col" className="hand text-nowrap">
                            <Translate contentKey="Reviewtitle.File_Name"></Translate>
                          </th>
                          <th scope="col" className="hand text-nowrap">
                            <Translate contentKey="Reviewtitle.File_Type"></Translate>
                          </th>
                          <th scope="col" className="hand text-nowrap">
                            <Translate contentKey="Reviewtitle.File_Format"></Translate>
                          </th>
                          <th scope="col" className="hand text-nowrap">
                            <Translate contentKey="Reviewtitle.Download"></Translate>
                          </th>
                          <th scope="col" className="hand text-nowrap">
                            <Translate contentKey="Reviewtitle.Action(s)"></Translate>
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                        {reviewFiles?.map((file, index) => (
                          <tr key={index}>
                            <td>{index + 1}</td>
                            <td>{file.file}</td>
                            <td>
                              {upperCase(file?.fileType?.name)}
                            </td>
                            <td>{upperCase(getFileExtension(file?.file))}</td>
                            <td>
                              <a
                                // onClick={() => downloadFile(file?.fileEndPoint, file?.file)}
                                target="_blank"
                                rel="noopener noreferrer"
                              >
                                <FontAwesomeIcon icon="cloud-arrow-down" className="fa-cloud-arrow-down" />
                              </a>
                            </td>
                            <td><FontAwesomeIcon icon="trash" className="fa-cloud-arrow-down" /></td>
                          </tr>
                        ))
                        }
                      </tbody>


                    </table>
                  </div> :

                  <div className='text-center'>
                    <div className='py-4 text-center'><Translate contentKey="Reviewtitle.Review_Files_is_Empty"></Translate></div>
                  </div>
                }
                <div className="article-details text-end">
                  <button className="article-btn" onClick={downloadAllFile}><Translate contentKey="Reviewtitle.Download_All"></Translate></button>
                </div>
              </div>
            </div>
          </div>
        </div>


        <div>
          {/* Reviewer design start*/}
          <div className="p-3 position-relative" style={{ borderRight: '1px solid #d9d9d9' }}>
            {(showModal && modelWorkingType === 'reviewerAdd') &&
              <AddReviewer showModal={showModal} handleClose={handleClose}
                participantLists={participantList}
                setParticipantList={setParticipantList}
                submissionId={submissionId}
                reviewRoundId={reviewRoundId}
                reviewFiles={reviewFiles}
                handleFileUploadModelRequest={handleFileUploadModelRequest}
                handleAddReviewerFileUpload={handleAddReviewerFileUpload}
              />

            }

            {(showModal && modelWorkingType === 'cancelReviewer') &&
              <CancelReviewer showModal={showModal} handleClose={handleClose}
                participantLists={participantList}
                setParticipantList={setParticipantList}
                submissionId={submissionId}
                reviewRoundId={reviewRoundId}
                reviewFiles={reviewFiles}
                handleFileUploadModelRequest={handleFileUploadModelRequest}
                handleAddReviewerFileUpload={handleAddReviewerFileUpload}
              />

            }


            <div className='review_align_center'>
              <h3 className='ms-2' style={{ border: "none", paddingBottom: "0" }}> <Translate contentKey="Reviewtitle.Reviewer"></Translate></h3>
              <button className='btn custom-btn pd-3' style={{ marginBottom: "10px" }} onClick={() => {
                setShowModal(true);
                setModelWorkingType("reviewerAdd")
              }}>
                <Translate contentKey="Reviewtitle.ADD_REVIEWER"></Translate>
              </button>
            </div>
            <div className='b-bottom b-top'>
              {roundReviewer?.length > 0 ?
                <div className="d-flex px-3">
                  <table className="table table-borderless">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th><Translate contentKey="Reviewtitle.Reviewer_Name"></Translate></th>
                        <th><Translate contentKey="Reviewtitle.Type"></Translate></th>
                        <th><Translate contentKey="Reviewtitle.Response_Due_Date"></Translate></th>
                        <th><Translate contentKey="Reviewtitle.Status"></Translate></th>
                        <th><Translate contentKey="Reviewtitle.Action(s)"></Translate></th>
                      </tr>
                    </thead>
                    <tbody>

                      {roundReviewer?.map((reviewer, index) => (
                        <React.Fragment key={index + 1}>
                          <tr>
                            <td>{index + 1}</td>
                            <td onClick={() => toggleItem(index + 1)}>
                              <span className="titles"> {reviewer?.fullName} </span>
                              {expandedItem.includes(index + 1) ? (
                                <FontAwesomeIcon icon="angle-down" className="fa-angle-down" />
                              ) : (
                                <FontAwesomeIcon icon="angle-right" className="fa-angle-right" />
                              )}
                            </td>
                            <td>{reviewer?.reviewerReviewType}</td>
                            <td>{formatDate(reviewer?.responseDueDate)}</td>
                            <td>{reviewer?.reviewDueDate}</td>

                            <td className="align-middle text-end pb-2">
                              <div className="dropdown custom-dropdown me-3">
                                <Button color="" className="td-dot-btn" data-bs-toggle="dropdown" aria-expanded="false">
                                  <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size" />
                                  <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size px-1" />
                                  <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size " />
                                </Button>
                                <ul className="dropdown-menu p-0 dropdown-menu-end">
                                  <li><a className="dropdown-item"><Translate contentKey="Reviewtitle.Review_Details"></Translate> </a></li>
                                  <li><a className="dropdown-item"><Translate contentKey="Reviewtitle.Email_Reviewer"></Translate> </a></li>
                                  <li><a className="dropdown-item"><Translate contentKey="Reviewtitle.Edit"></Translate> </a></li>
                                  <li><a className='dropdown-item' onClick={() => {
                                    setShowModal(true);
                                    setModelWorkingType("cancelReviewer")
                                  }}>
                                    <Translate contentKey="Reviewtitle.Cancel_reviewer"></Translate>
                                  </a></li>
                                  <li><a className="dropdown-item"><Translate contentKey="Reviewtitle.History"></Translate></a></li>
                                  <li><a className="dropdown-item"><Translate contentKey="Reviewtitle.Login_As"></Translate></a></li>
                                  <li><a className="dropdown-item"><Translate contentKey="Reviewtitle.Editorial_Notes"></Translate> </a></li>
                                </ul>
                              </div>
                            </td>

                          </tr>
                          {expandedItem.includes(index + 1) && reviewDetails(reviewer, index)}
                        </React.Fragment>
                      ))}
                    </tbody>
                  </table>

                </div> :
                <div className='text-center'>
                  <div className='py-4 text-center'><Translate contentKey="Reviewtitle.Production_Ready_Files_is_Empty"></Translate></div>
                </div>
              }
            </div>
          </div>
          {/* Reviewer design end */}
        </div>


        <div
          className={`${expandedItem.length === 0 ? 'b-bottom' : ''}  b-top p-3 d-flex justify-content-between align-items-center `}>
          <h3 className='ms-2 my-3' style={{ border: "none", paddingBottom: "0" }}>
            <Translate contentKey="Reviewtitle.Discussions"></Translate>
          </h3>
          <Button
            className={`custom-btn pd-3`}
            onClick={() => {
              setShowModal(true);
              setShowModelType("review-addDiscussion");
            }}
          >
            <Translate contentKey="Reviewtitle.ADD_DISCUSSION"></Translate>
          </Button>
        </div>


        {reviewDiscussionList?.length !== 0 ? <table className="table table-borderless b-bottom">
          <tbody>
            {reviewDiscussionList?.map((item, index) => (
              <React.Fragment key={index}>
                <tr>
                  <td className="align-middle pb-2" style={{ width: '80%' }} onClick={() => {
                    toggleItem(item?.topic)
                  }}>
                    <span className="titles pe-1 ms-2" style={{ fontSize: '16px' }}> {item?.topic}</span>
                    {expandedItem.includes(item?.topic) ? (
                      <FontAwesomeIcon icon="angle-down" className="fa-angle-down" />
                    ) : (
                      <FontAwesomeIcon icon="angle-right" className="fa-angle-right" />
                    )}
                  </td>

                  <td className="align-middle pb-2">
                    <span className={item.status === 'closed' ? 'red-dot' : 'green-dot'}></span>
                    {item.status}
                  </td>

                  <td className="align-middle text-end pb-2">
                    <div className="dropdown custom-dropdown me-3">
                      <Button color="" className="td-dot-btn" data-bs-toggle="dropdown" aria-expanded="false">
                        <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size" />
                        <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size px-1" />
                        <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size " />
                      </Button>
                      <ul className="dropdown-menu p-0 dropdown-menu-end">
                        {(item?.status === "open" || item?.status === "reopened") && (
                          <li>
                            <a className="dropdown-item" onClick={() => handleDiscussionStatus(item?.id, "closed")}>
                              <Translate contentKey="Reviewtitle.Closed"></Translate>
                            </a>
                          </li>
                        )}
                        {(item?.status === "closed") && <li>
                          <a className="dropdown-item"
                            onClick={() => handleDiscussionStatus(item?.id, "reopened")}><Translate contentKey="Reviewtitle.Re-Open"></Translate></a>
                        </li>}
                      </ul>
                    </div>
                  </td>
                </tr>

                {expandedItem.includes(item.topic) &&
                  (
                    <tr>
                      <td colSpan={6}>
                        <div style={{ border: '1px solid rgb(217, 217, 217)' }}>
                          <div style={{ padding: '10px 10px 0px 10px' }}>
                            <div className="sub-title mx-2"
                              style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                              <span> <Translate contentKey="Reviewtitle.Discussion_Description:"></Translate></span>

                            </div>
                            <div className="p-2 bg-light border border-light mb-2"
                              dangerouslySetInnerHTML={{ __html: item?.description }}></div>
                            {item?.discussionFiles.length !== 0 && <>
                              <div className="sub-title mx-2  pb-0">
                                <Translate contentKey="Reviewtitle.Attachments"></Translate>
                              </div>
                              <ul className="ps-3 m-0 pb-0">
                                {item.discussionFiles?.map((file, fileIndex) => (
                                  <li className="pt-2" key={fileIndex}>
                                    <span><b>{file?.fileType?.name}</b> -  </span>
                                    <a className="atricle-files article-details"
                                    //  onClick={() => downloadFile(file?.fileEndPoint, file?.file)}
                                    >
                                      {file?.file}
                                    </a>
                                  </li>
                                ))}
                                <br />
                              </ul>
                            </>
                            }
                          </div>
                        </div>
                      </td>
                    </tr>
                  )
                }

                {expandedItem.includes(item.topic) && (
                  <>
                    {item.discussionMessages.map((detail, indexNumber) => (
                      subDetails(detail, indexNumber)
                    ))}
                  </>
                )}

                {expandedItem.includes(item.topic) && (
                  <tr key={index}>
                    <td colSpan={5}>
                      <div className="pb-2 b-bottom">
                        <div className="tab-padding">
                          <div className='text-end  mb-2'>
                            <button className='btn custom-btn' onClick={() => {
                              setShowModal(true);
                              setModelWorkingType('addChatReview');
                              setSelectDiscussionId(item.id);
                            }}>
                              <Translate contentKey="Reviewtitle.ADD_CHAT"></Translate>
                            </button>
                          </div>
                        </div>
                      </div>
                    </td>
                  </tr>
                )}

              </React.Fragment>
            ))}
          </tbody>
        </table>
          : <div className='text-center'>
            <div className='py-4 text-center'><Translate contentKey="Reviewtitle.Discussion_Not_Yet_Started"></Translate></div>
          </div>}
      </div>
      <ButtonPermissionsReview submissionId={submissionId} reviewRoundId={reviewRoundId}
        participantList={participantList}
        setParticipantList={setParticipantList}
        reviewDiscussionList={reviewDiscussionList}
        setReviewDiscussionList={setReviewDiscussionList}
        reviewFiles={reviewFiles}
      />
    </div>


  );
};

export default ReviewDiscussion;
