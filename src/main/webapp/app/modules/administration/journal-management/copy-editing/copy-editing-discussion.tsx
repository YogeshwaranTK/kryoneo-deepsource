import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useAppDispatch, useAppSelector} from "app/config/store";
import '../create-new-submission/article-details-management.scss'
import LoaderMain from "app/shared/Loader/loader-main";
import {Button} from "reactstrap";
import CopyEditingButtonPermissions
  from "app/modules/administration/journal-management/copy-editing/copy-editing-buttons-permission";
import {upperCase} from "lodash";
import UploadFilesCopyEditing from "app/modules/administration/journal-management/copy-editing/upload-file-copy-editing";
import {
  getCopyEditingList,
  stateFalse,
  postAddChatDiscussionRequest,
  getCopyEditingDiscussionListRaw,
  postCopyEditingEditedFileUpload
} from "app/modules/administration/journal-management/copy-editing/copy-editing.reducer";
import AddChatCopyEditing from "app/modules/administration/journal-management/copy-editing/add-chat-copy-editing";
import AddDiscussionCopyEditing
  from "app/modules/administration/journal-management/copy-editing/add-discussion-copy-editing";
import {updateDiscussStatus} from "app/modules/administration/journal-management/copy-editing/copy-editing.reducer";

const CopyEditingDiscussion = (props) => {
  const {submissionId} = props
  const dispatch = useAppDispatch()
  const addChatDiscussionSuccess = useAppSelector(state => state.copyEditing.addChatDiscussionSuccess);
  const loading = useAppSelector(state => state.copyEditing.loading);
  const copyEditingDetails = useAppSelector(state => state.copyEditing.copyEditingDetails);
  const [showModal, setShowModal] = useState(false);
  const submissionDetails = useAppSelector(state => state.submission.submissionDetails);
  const [expandedItem, setExpandedItem] = useState([]);
  const [participantList, setParticipantList] = useState([]);
  const fileUploadCopyEditedFileSuccess = useAppSelector(state => state.copyEditing.fileUploadCopyEditedFileSuccess);
  const postfileUploadCopyEditedFilePayload = useAppSelector(state => state.copyEditing.postfileUploadCopyEditedFilePayload);
  const [showModelType, setShowModelType] = useState('');
  const updateDiscussStatusSucess = useAppSelector(state => state.copyEditing.updateDiscussStatusSuccess);

  useEffect(() => {

      if (addChatDiscussionSuccess === true){
        handleClose()
        setLoadingLocal(true)
        getCopyEditingDiscussionListRaw(submissionId).then(response => {
          setCopyEditingDiscussionList(response.data)
          setLoadingLocal(false)
        })
          .catch(error => {
            setLoadingLocal(false)
            console.error(error);
          });
        handleClose()
        dispatch(stateFalse())

    }else if (fileUploadCopyEditedFileSuccess){
      const mergedArray = [...copyEditingDraftFilesList ,...postfileUploadCopyEditedFilePayload]
        setCopyEditingDraftFilesList(mergedArray)
        dispatch(stateFalse())
    }
  }, [fileUploadCopyEditedFileSuccess,addChatDiscussionSuccess])


  const subdetails = (details, index) => {
    return (
      <tr key={index}>
        <td colSpan={6}>
          <div style={{border: '1px solid rgb(217, 217, 217)'}}>
            <div style={{padding: '10px 10px 0px 10px'}}>
              {details?.message === '' ? null :
                <>
                  <div className="sub-title mx-2"
                       style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                    <span>{details?.userFullName}:</span>
                    {/*<div style={{display: 'flex', gap: '10px'}}>*/}
                    {/*  <FontAwesomeIcon*/}
                    {/*    icon="trash"*/}
                    {/*    className="fa-trash"*/}
                    {/*    style={{cursor: 'pointer', fontSize: '12px'}}*/}
                    {/*  />*/}
                    {/*  <FontAwesomeIcon*/}
                    {/*    icon="edit"*/}
                    {/*    className="fa-edit"*/}
                    {/*    style={{cursor: 'pointer', fontSize: '14px'}}*/}
                    {/*  />*/}
                    {/*</div>*/}
                  </div>
                  <div className="p-2 bg-light border border-light mb-2"
                       dangerouslySetInnerHTML={{__html: details?.message}}></div>
                </>}
              {details?.productionDiscussionMessageFiles.length !== 0 &&
                <>
                <div className="sub-title mx-2 pb-0">
                  Attachments
                </div>
                <ul className="ps-3 m-0 pb-0">
                  {details?.productionDiscussionMessageFiles?.map((file, fileIndex) => (
                    <li className="pt-2" key={fileIndex}>
                      <span><b>{file?.fileType?.name}</b> -  </span>
                      <a className="atricle-files article-details"
                        // onClick={() => downloadFile(file?.fileEndPoint, file?.file)}
                      >
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

  // const downloadFile = (requestUrl, fileName) => {
  //
  //   fileDownloadUrlRequest(requestUrl).then(response => {
  //     // Create a download link and trigger the download
  //     const url = window.URL.createObjectURL(new Blob([response.data]));
  //     const a = document.createElement('a');
  //     a.href = url;
  //     a.download = fileName; // Replace with the file name
  //     document.body.appendChild(a);
  //     a.click();
  //
  //     // Clean up
  //     window.URL.revokeObjectURL(url);
  //   })
  //     .catch(error => {
  //       console.error('Error downloading the file:', error);
  //     });
  // };


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
  };

  const handleDiscussionStatus = (discussionId, status) => {
    dispatch(updateDiscussStatus({submissionId, discussionId, status}));
  };

  useEffect(() => {
    if (updateDiscussStatusSucess) {
      dispatch(getCopyEditingList(submissionId));

    }
  }, [updateDiscussStatusSucess]);


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
    if (modelWorkingType === 'CopyEditingDraftFiles') {
      dispatch(postCopyEditingEditedFileUpload(requestData))
    }

    else if (modelWorkingType === 'addChatCopyEditing'){
      dispatch(postAddChatDiscussionRequest(requestData))
    }
  }

  const [loadingLocal, setLoadingLocal] = useState(false)


  useEffect(() => {
    dispatch(getCopyEditingList(submissionId))
  }, [])

  const [copyEditingDiscussionList, setCopyEditingDiscussionList] = useState([])
  const [copyEditingDraftFilesList, setCopyEditingDraftFilesList] = useState([])
  const [selectDiscussionId, setSelectDiscussionId] = useState(0)

  useEffect(() => {
    setParticipantList(copyEditingDetails?.copyEditingContributors)
    setCopyEditingDiscussionList(copyEditingDetails?.copyEditingDiscussions)
    setCopyEditingDraftFilesList(copyEditingDetails?.copyEditingDraftFiles)
  }, [copyEditingDetails])



  return (
    <div className="d-flex mt-3 Submittd_list" style={{border: 'none !important'}}>
      {(showModal && modelWorkingType === 'CopyEditingDraftFiles') &&
        <UploadFilesCopyEditing showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                                handleFileUploadModelRequest={handleFileUploadModelRequest}
        />}

      {(showModal && modelWorkingType === 'addChatCopyEditing') &&
        <AddChatCopyEditing showModal={showModal} handleClose={handleClose} submissionId={submissionId}
          handleFileUploadModelRequest={handleFileUploadModelRequest}
                           selectDiscussionId={selectDiscussionId}/>}

      {showModelType === "addDiscussion" &&
      <AddDiscussionCopyEditing showModal={showModal} handleClose={handleClose} participantLists={participantList}
                                submissionId={props.submissionId} copyEditingDiscussionList={copyEditingDiscussionList}
                                setCopyEditingDiscussionList={setCopyEditingDiscussionList}/>}

      {loading || loadingLocal ? <LoaderMain/> : null}
      <div className="col-9 px-3 position-relative" style={{borderRight: '1px solid #d9d9d9'}}>
        <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
          <h3 className='ms-2' style={{border: "none", paddingBottom: "0"}}>Copy-editing Ready Files</h3>
          <button className='btn custom-btn pd-3' style={{marginBottom: "10px"}} onClick={() => {
            setShowModal(true);
            setModelWorkingType("CopyEditingDraftFiles")
          }}>
            UPLOAD FILE
          </button>
        </div>
        <div className='b-bottom b-top'>
          {copyEditingDraftFilesList?.length > 0 ? <div className="d-flex px-3">
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
                  <th scope="col" className="hand">
                    Action(s)
                  </th>
                </tr>
                </thead>
                <tbody>

                {copyEditingDraftFilesList?.map((item, index) => (
                  <tr key={index}>
                    <td>{index + 1}</td>
                    <td>{item?.file}</td>
                    <td>
                      {upperCase(item?.fileType?.name)}
                    </td>
                    <td>{upperCase(getFileExtension(item?.file))}</td>
                    <td>
                      <a
                        // onClick={() => downloadFile(item?.fileEndPoint, item?.file)}
                        target="_blank"
                        rel="noopener noreferrer"
                      >
                        <FontAwesomeIcon icon="cloud-arrow-down" className="fa-cloud-arrow-down"/>
                      </a>
                    </td>
                    <td>
                      <FontAwesomeIcon icon="trash" className="fa-trash"
                                       // onClick={() => handleDelete(item.id)}
                      />
                    </td>
                  </tr>
                ))}
                </tbody>
              </table>
            </div> :
            <div className='text-center'>
              <div className='py-4 text-center'>Copy-editing Ready Files is Empty</div>
            </div>
          }
          <div className="article-details text-end">
            <button className="article-btn"
                    // onClick={downloadAllFile}
            >Download All</button>
          </div>
        </div>
        <div
          className={`${expandedItem.length === 0 ? 'b-bottom' : ''}  b-top pt-3 d-flex justify-content-between align-items-center `}>
          <h3 className='ms-2 my-3' style={{border: "none", paddingBottom: "0"}}>
           Discussions
          </h3>
          <Button
            className={`custom-btn pd-3`}
            onClick={() => {
              setShowModal(true);
              setShowModelType("addDiscussion");
            }}
          >
            ADD DISCUSSION
          </Button>
        </div>
        {copyEditingDiscussionList?.length !== 0 ?  <table className="table table-borderless b-bottom">
            <tbody>
            {copyEditingDiscussionList?.map((item, index) => (
              <React.Fragment key={index}>
                <tr>
                  <td className="align-middle pb-2" style={{width: '80%'}} onClick={() => {
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
                      {/*<ul className="dropdown-menu p-0 dropdown-menu-end">*/}
                      {/*  <li>*/}
                      {/*    <a className="dropdown-item">Complete</a>*/}
                      {/*  </li>*/}
                      {/*  <li>*/}
                      {/*    <a className="dropdown-item">Terminate</a>*/}
                      {/*  </li>*/}
                      {/*</ul>*/}

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
                                      // onClick={() => downloadFile(file?.fileEndPoint, file?.file)}
                                    >
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

                {/*{expandedItem.includes(item.topic) &&*/}
                {/*  item?.chat?.map((detail, indexNumber) => subdetails(detail, indexNumber))*/}
                {/*}*/}

                {expandedItem.includes(item.topic) && (
                  <>
                    {item?.discussionMessages.map((detail, indexNumber) => (

                      subdetails(detail, indexNumber)

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
                                setModelWorkingType('addChatCopyEditing');
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
          :    <div className='text-center'>
            <div className='py-4 text-center'>Discussion Not Yet Started</div>
          </div>}

      </div>
      <CopyEditingButtonPermissions submissionId={submissionId} participantList={participantList}
                                    setParticipantList={setParticipantList}
                                    copyEditingDiscussionList={copyEditingDiscussionList}
                                    setCopyEditingDiscussionList={setCopyEditingDiscussionList}
                                    copyEditingDraftFilesList={copyEditingDraftFilesList}
      />
    </div>

  )
    ;
};

export default CopyEditingDiscussion;
