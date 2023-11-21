import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import {Button} from "reactstrap";
import {upperCase} from "lodash";
import {
  getSubmissionDiscussionListRaw,
  getSubmissionDetails,
  postAddChatDiscussionRequest,
  postSubmissionReadyFilesUpload, putUploadRevision,
  stateFalse, updateDiscussStatus, deleteSubmissionFile, downloadSubmissionFile
}
  from "app/modules/administration/journal-management/submission/submission.reducer";
import '../create-new-submission/article-details-management.scss'
import AddChatSubmission from "app/modules/administration/journal-management/submission/add-chat-submission";
import UploadFilesSubmission from "app/modules/administration/journal-management/submission/upload-file-submission";
import UploadRevision from "app/modules/administration/journal-management/submission/upload-revision";
import SubmissionButtonPermissions
  from "app/modules/administration/journal-management/submission/submission-buttons-permission";
import {Translate} from "react-jhipster";
import {downloadFile} from "app/modules/administration/journal-management/submission/reuse-submission-component";
import AddDiscussionSubmission
  from "app/modules/administration/journal-management/submission/add-discussion-submission";
import DeleteSubmissionFile from "app/modules/administration/journal-management/submission/deleteSubmissionFile";


const SubmissionDiscussion = (props) => {
  const {submissionId} = props
  const dispatch = useAppDispatch()
  const fileUploadSubmissionReadyFilesSuccess = useAppSelector(state => state.submission.fileUploadSubmissionReadyFilesSuccess);
  const postUploadFilePayload = useAppSelector(state => state.submission.postUploadFilePayload);
  const addChatDiscussionSuccess = useAppSelector(state => state.submission.addChatDiscussionSuccess);
  const addDiscussionSuccess = useAppSelector(state => state.submission.addDiscussionSuccess);
  const updateDiscussStatusSuccess = useAppSelector(state => state.submission.updateDiscussStatusSuccess);
  const deleteSubmissionFileSuccess = useAppSelector(state => state.submission.deleteSubmissionFileSuccess);
  const loading = useAppSelector(state => state.submission.loading);
  const submissionDetails = useAppSelector(state => state.submission.submissionDetails);
  const [showModal, setShowModal] = useState(false);
  const [expandedItem, setExpandedItem] = useState([]);
  const [submissionReadyFilesList, setSubmissionReadyFilesList] = useState([])
  const [participantList, setParticipantList] = useState([]);
  const [showModelType, setShowModelType] = useState('');

  useEffect(() => {
    if (fileUploadSubmissionReadyFilesSuccess) {
      setSubmissionReadyFilesList([...submissionReadyFilesList, ...postUploadFilePayload]);
      dispatch(stateFalse())
      handleClose()
    } else if (addChatDiscussionSuccess || addDiscussionSuccess) {
      setLoadingLocal(true)
      getSubmissionDiscussionListRaw(submissionId).then(response => {
        setSubmissionDiscussionList(response.data)
        setLoadingLocal(false)
      })
        .catch(error => {
          setLoadingLocal(false)
          console.error(error);
        });
      handleClose()
      dispatch(stateFalse())
    }
  }, [fileUploadSubmissionReadyFilesSuccess, addChatDiscussionSuccess,addDiscussionSuccess])


  const subDetails = (details, index) => {
    return (
      <tr key={index}>
        <td colSpan={6}>
          <div style={{border: '1px solid rgb(217, 217, 217)'}}>
            <div style={{padding: '10px 10px 0px 10px'}}>

              {details?.submissionDiscussionMessageFiles.length !== 0}
              <>
                <div className="sub-title mx-2"
                     style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                  <span>{details?.userFullName}:</span>
                </div>
                <div className="p-2 bg-light border border-light mb-2"
                     dangerouslySetInnerHTML={{__html: details?.message}}></div>
              </>
              {details?.submissionDiscussionMessageFiles.length !== 0 && <>
                <div className="sub-title mx-2 pb-0">
                  Attachments
                </div>
                <ul className="ps-3 m-0 pb-0">
                  {details?.submissionDiscussionMessageFiles?.map((file, fileIndex) => (
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


  const toggleItem = (itemId) => {
    setExpandedItem((prevExpandedItems) => {
      if (prevExpandedItems.includes(itemId)) {
        return prevExpandedItems.filter((id) => id !== itemId);
      } else {
        return [...prevExpandedItems, itemId];
      }
    });
  };

  const handleClose = () => {
    setShowModal(false);
    setModelWorkingType('')
    setRevisionFileDetails({id: 0, otherProperty: 'otherValue'});
    setSubmissionFileId(0)
  };

  const handleDiscussionStatus = (discussionId, status) => {
    dispatch(updateDiscussStatus({submissionId, discussionId, status}));
  };

  const [submissionFileId, setSubmissionFileId]= useState(0)

  const handleSubmissionFileDelete = () => {
    dispatch(deleteSubmissionFile({submissionId, submissionFileId}));
    handleClose()
  };


  useEffect(() => {
    if (updateDiscussStatusSuccess || deleteSubmissionFileSuccess) {
      dispatch(getSubmissionDetails(submissionId));

    }
  }, [updateDiscussStatusSuccess,deleteSubmissionFileSuccess]);


  const downloadAllFile = () => {
    downloadSubmissionFile(submissionId).then(response => {
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

  const getFileExtension = (filename) => {
    const lastDotIndex = filename.lastIndexOf(".");
    if (lastDotIndex !== -1) {
      return filename.substring(lastDotIndex);
    } else {
      return null; // or any other value to indicate no extension found
    }
  }

  const [modelWorkingType, setModelWorkingType] = useState('')
  const handleFileUploadModelRequest = (requestData) => {
    if (modelWorkingType === 'submissionReadyFiles') {
      dispatch(postSubmissionReadyFilesUpload(requestData))
    } else if (modelWorkingType === 'uploadRevision') {
      dispatch(putUploadRevision(requestData))
    } else if (modelWorkingType === 'addChatSubmission') {
      dispatch(postAddChatDiscussionRequest(requestData))
    }
  }

  const [loadingLocal, setLoadingLocal] = useState(false)
  const [revisionFileDetails, setRevisionFileDetails] = useState({id: 0, otherProperty: 'otherValue'});

  useEffect(() => {
    dispatch(getSubmissionDetails(submissionId))
  }, [])

  const [submissionDiscussionList, setSubmissionDiscussionList] = useState([])
  const [selectDiscussionId, setSelectDiscussionId] = useState(0)
  useEffect(() => {
    setSubmissionReadyFilesList(submissionDetails?.submissionFiles)
    setParticipantList(submissionDetails?.submissionContributors)
    setSubmissionDiscussionList(submissionDetails?.submissionDiscussions)
  }, [submissionDetails])


  return (
    <div className="d-flex mt-3 Submittd_list" style={{border: 'none !important'}}>
      {(showModal && modelWorkingType === 'submissionReadyFiles') &&
        <UploadFilesSubmission showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                               handleFileUploadModelRequest={handleFileUploadModelRequest}/>}

      {(showModal && modelWorkingType === 'uploadRevision') &&
        <UploadRevision showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                        revisionFileDetails={revisionFileDetails} productionReadyFilesList={submissionReadyFilesList}
                        handleFileUploadModelRequest={handleFileUploadModelRequest}/>}

      {(showModal && modelWorkingType === 'deleteSubmissionFile') &&
        <DeleteSubmissionFile showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                        revisionFileDetails={revisionFileDetails} productionReadyFilesList={submissionReadyFilesList}
                              handleSubmissionFileDelete={handleSubmissionFileDelete}/>}

      {(showModal && modelWorkingType === 'addChatSubmission') &&
        <AddChatSubmission showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                           handleFileUploadModelRequest={handleFileUploadModelRequest}
                           selectDiscussionId={selectDiscussionId}/>}

      {showModelType && modelWorkingType === "addDiscussion" &&
        <AddDiscussionSubmission showModal={showModal} handleClose={handleClose} participantLists={participantList}
                                 submissionId={props.submissionId} submissionDiscussionList={submissionDiscussionList}
                                 setSubmissionDiscussionList={setSubmissionDiscussionList}/>}
      {loading || loadingLocal ? <LoaderMain/> : null}
      <div className="col-9 px-3 position-relative" style={{borderRight: '1px solid #d9d9d9'}}>
        <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
          <h3 className='ms-2' style={{border: "none", paddingBottom: "0"}}>Submission Files</h3>
          <button className='btn custom-btn pd-3' style={{marginBottom: "10px"}} onClick={() => {
            setShowModal(true);
            setModelWorkingType("submissionReadyFiles")
          }}>
            UPLOAD FILE
          </button>
        </div>
        <div className='b-bottom b-top'>
          {submissionReadyFilesList?.length > 0 ? <div className="d-flex px-3">
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
                  <th scope="col" style={{width: '8%'}}>
                    <Translate contentKey="table.Action"></Translate>
                  </th>
                </tr>
                </thead>
                <tbody>

                {submissionReadyFilesList?.map((item, index) => (
                  <tr key={index}>
                    <td>{index + 1}</td>
                    <td>{item?.file}</td>
                    <td>
                      {upperCase(item?.fileType?.name)}
                    </td>
                    <td>{upperCase(getFileExtension(item?.file))}</td>
                    <td>
                      <a
                        onClick={() => downloadFile(item?.fileEndPoint, item?.file)}
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
                            <a className="dropdown-item" onClick={() => {
                              setShowModal(true);
                              setModelWorkingType("uploadRevision");
                              setRevisionFileDetails({id: item.id, otherProperty: item?.fileType?.name});
                            }}>
                              Upload Revision
                            </a>
                          </li>
                          {/*<li>*/}
                          {/*  <a className="dropdown-item" onClick={() => handleSubmissionFileDelete(item?.id)}>*/}
                          {/*    Delete*/}
                          {/*  </a>*/}
                          {/*</li>*/}
                          <li>
                            <a className="dropdown-item" onClick={() => {
                              setShowModal(true);
                              setModelWorkingType("deleteSubmissionFile");
                              setSubmissionFileId(item?.id);
                            }}>
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
              <div className='py-4 text-center'>Submission Ready Files is Empty</div>
            </div>
          }
          <div className="article-details text-end">
            <button className="article-btn" onClick={downloadAllFile}>Download All</button>
          </div>
        </div>
        <div
          className={`${expandedItem.length === 0 ? 'b-bottom' : ''}  b-top p-3 d-flex justify-content-between align-items-center `}>
          <h3 className='ms-2 my-3' style={{border: "none", paddingBottom: "0"}}>
            Pre-Review Discussions
          </h3>
          <Button
            className={`custom-btn pd-3`}
            onClick={() => {
              setShowModal(true);
              setShowModelType("addDiscussion");
              setModelWorkingType("addDiscussion");
            }}
          >
            ADD DISCUSSION
          </Button>
        </div>
        {submissionDiscussionList?.length !== 0 ? <table className="table table-borderless b-bottom">
            <tbody >
            {submissionDiscussionList?.map((item, index) => (
              <React.Fragment key={index}>
                <tr style={{border:null}}>
                  <td className="pb-2" style={{width: '70%'}} onClick={() => {
                    toggleItem(item?.topic)
                  }}>
                    <span className="titles pe-1 ms-2" style={{fontSize: '16px'}}> {item?.topic}</span>
                    {expandedItem.includes(item?.topic) ? (
                      <FontAwesomeIcon icon="angle-down" className="fa-angle-down"/>
                    ) : (
                      <FontAwesomeIcon icon="angle-right" className="fa-angle-right"/>
                    )}
                  </td>
                  <td className="align-middle pb-2">
                    <span className={item.status === 'closed' ? 'red-dot' : 'green-dot'}></span>
                    {item.status}
                  </td>
                  <td className="align-middle text-end pb-2">
                    <div className="dropdown custom-dropdown me-3">
                      <Button color="" className="td-dot-btn" data-bs-toggle="dropdown" aria-expanded="false">
                        <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size"/>
                        <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size px-1"/>
                        <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size "/>
                      </Button>
                      <ul className="dropdown-menu p-0 dropdown-menu-end">
                        {(item?.status === "open" || item?.status === "reopened") && (
                          <li>
                            <a className="dropdown-item" onClick={() => handleDiscussionStatus(item?.id, "closed")}>
                              Closed
                            </a>
                          </li>
                        )}
                        {(item?.status === "closed") && <li>
                          <a className="dropdown-item"
                             onClick={() => handleDiscussionStatus(item?.id, "reopened")}>Re-Open</a>
                        </li>}
                      </ul>
                    </div>
                  </td>
                </tr>
                {expandedItem.includes(item.topic) &&
                  (
                    <tr>
                      <td colSpan={6}>
                        <div style={{border: '1px solid rgb(217, 217, 217)'}}>
                          <div style={{padding: '10px 10px 0px 10px'}}>
                            <div className="sub-title mx-2"
                                 style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                              <span>Discussion Description:</span>
                            </div>
                            <div className="p-2 bg-light border border-light mb-2"
                                 dangerouslySetInnerHTML={{__html: item?.description}}></div>
                            {item?.discussionFiles.length !== 0 && <>
                              <div className="sub-title mx-2  pb-0">
                                Attachments
                              </div>
                              <ul className="ps-3 m-0 pb-0">
                                {item.discussionFiles?.map((file, fileIndex) => (
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
                {expandedItem.includes(item.topic) && (
                  <>
                    {item?.discussionMessages?.map((detail, indexNumber) => (
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
                                setModelWorkingType('addChatSubmission');
                                setSelectDiscussionId(item.id);
                              }}>
                                ADD CHAT
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
            <div className='py-4 text-center'>Discussion Not Yet Started</div>
          </div>}

      </div>
      <SubmissionButtonPermissions submissionId={submissionId} participantList={participantList}
                                   setParticipantList={setParticipantList}
                                   submissionDiscussionList={submissionDiscussionList}
                                   setSubmissionDiscussionList={setSubmissionDiscussionList}
                                   submissionReadyFilesList={submissionReadyFilesList}
      />
    </div>
  );
};

export default SubmissionDiscussion;
