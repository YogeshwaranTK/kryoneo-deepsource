import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import {Button} from "reactstrap";
import ProductionButtonPermissions
  from "app/modules/administration/journal-management/production/production-buttons-permission";
import {upperCase} from "lodash";
import UploadFilesProduction from "app/modules/administration/journal-management/production/upload-file-production";
import {
  fileDownloadUrlRequest,
  getProductionDiscussionListRaw,
  getProductionList,
  getProductionReadyFilesListRaw,
  postAddChatDiscussionRequest,
  postProductionReadyFilesUpload,
  stateFalse,updateProductionDiscussionStatus,deleteProductionFile
} from "app/modules/administration/journal-management/production/production.reducer";
import AddChatProduction from "app/modules/administration/journal-management/production/add-chat-production";
import AddDiscussionProduction from "app/modules/administration/journal-management/production/add-discussion";
import DeleteProduction from "app/modules/administration/journal-management/production/deleteProductionFile";

const ProductionDiscussion = (props) => {
  const {submissionId} = props
  const dispatch = useAppDispatch()
  const fileUploadProductionReadyFilesSuccess = useAppSelector(state => state.production.fileUploadProductionReadyFilesSuccess);
  const addChatDiscussionSuccess = useAppSelector(state => state.production.addChatDiscussionSuccess);
  const updateProductionDiscussStatusSucess = useAppSelector(state => state.production.updateProductionDiscussStatusSucess);
  const loading = useAppSelector(state => state.production.loading);
  const productionDetails = useAppSelector(state => state.production.productionDetails);
  const [showModal, setShowModal] = useState(false);
  const articleDetails = useAppSelector(state => state.articleDetailManagement.articleData);
  const [richTextVisibility, setRichTextVisibility] = useState({});
  const [expandedItem, setExpandedItem] = useState([]);
  const [productionReadyFilesList, setProductionReadyFilesList] = useState([])
  const [participantList, setParticipantList] = useState([]);

  useEffect(() => {
    if (fileUploadProductionReadyFilesSuccess) {
      setLoadingLocal(true);
      getProductionReadyFilesListRaw(submissionId).then(response => {
        setProductionReadyFilesList(response.data);
        setLoadingLocal(false);
      })
        .catch(error => {
          setLoadingLocal(false);
          console.error(error);
        });
      handleClose()
    } else if (addChatDiscussionSuccess) {

      setLoadingLocal(true)
      getProductionDiscussionListRaw(submissionId).then(response => {
        setProductionDiscussionList(response.data)
        setLoadingLocal(false)
      })
        .catch(error => {
          setLoadingLocal(false)
          console.error(error);
        });
      handleClose()
      dispatch(stateFalse())
    }
  }, [fileUploadProductionReadyFilesSuccess, addChatDiscussionSuccess])


  const subDetails = (details, index) => {

    return (
      <tr key={index}>
        <td colSpan={6}>
          <div style={{border: '1px solid rgb(217, 217, 217)'}}>
            <div style={{padding: '10px 10px 0px 10px'}}>

              {details?.productionDiscussionMessageFiles.length !== 0}
              <>
                <div className="sub-title mx-2"
                     style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                  <span>{details?.userFullName}:</span>

                </div>
                <div className="p-2 bg-light border border-light mb-2"
                     dangerouslySetInnerHTML={{__html: details?.message}}></div>
              </>
              {details?.productionDiscussionMessageFiles.length !== 0 && <>
                <div className="sub-title mx-2 pb-0">
                  Attachments
                </div>
                <ul className="ps-3 m-0 pb-0">
                  {details?.productionDiscussionMessageFiles?.map((file, fileIndex) => (
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

  const downloadFile = (requestUrl, fileName) => {

    fileDownloadUrlRequest(requestUrl).then(response => {
      // Create a download link and trigger the download
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const a = document.createElement('a');
      a.href = url;
      a.download = fileName; // Replace with the file name
      document.body.appendChild(a);
      a.click();

      // Clean up
      window.URL.revokeObjectURL(url);
    })
      .catch(error => {
        console.error('Error downloading the file:', error);
      });
  };

  const handleDiscussionStatus = (discussionId, status) => {
    dispatch(updateProductionDiscussionStatus({submissionId, discussionId, status}));
  };

  const handleProductionFileDelete = () => {
    dispatch(deleteProductionFile({submissionId, productionFileId}));
  };




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
    setProductionFileId(0)
  };


  const downloadAllFile = () => {
    const link = document.createElement('a');
    link.href = articleDetails?.allFilePath;
    link.download = '';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
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
    if (modelWorkingType === 'productionReadyFiles') {
      dispatch(postProductionReadyFilesUpload(requestData))
    } else if (modelWorkingType === 'addChatProduction') {
      dispatch(postAddChatDiscussionRequest(requestData))
    }
  }


  const handleDelete = (id) => {
    // const itemToDelete = JournalListData.find((item) => item.id === id);
    // if (itemToDelete) {
    //   setDeletedData([...deletedData, {...itemToDelete, actionType: "DELETE"}]);
    //   const updatedData = JournalListData.filter((item) => item.id !== id);
    //   SetJournalList(updatedData);
    // }
  };
  const [productionFileId, setProductionFileId]= useState(0)
  const [loadingLocal, setLoadingLocal] = useState(false)

  useEffect(() => {
    if (updateProductionDiscussStatusSucess) {
      dispatch(getProductionList(submissionId));

    }
  }, [updateProductionDiscussStatusSucess]);

  useEffect(() => {
    dispatch(getProductionList(submissionId))
  }, [])

  const [productionDiscussionList, setProductionDiscussionList] = useState([])
  const [selectDiscussionId, setSelectDiscussionId] = useState(0)
  useEffect(() => {
    setProductionReadyFilesList(productionDetails?.productionReadyFiles)
    setParticipantList(productionDetails?.productionContributors)
    setProductionDiscussionList(productionDetails?.discussions)
  }, [productionDetails])

  return (
    <div className="d-flex mt-3 Submittd_list" style={{border: 'none !important'}}>
      {(showModal && modelWorkingType === 'productionReadyFiles') &&
        <UploadFilesProduction showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                               handleFileUploadModelRequest={handleFileUploadModelRequest}/>}

      {(showModal && modelWorkingType === 'addChatProduction') &&
        <AddChatProduction showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                           handleFileUploadModelRequest={handleFileUploadModelRequest}
                           selectDiscussionId={selectDiscussionId}/>}

      {(showModal && modelWorkingType === 'deleteProductionFile') &&
        <DeleteProduction showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                          handleProductionFileDelete={handleProductionFileDelete}
                           selectDiscussionId={selectDiscussionId}/>}

      {(showModal && modelWorkingType === 'addDiscussion') &&
        <AddDiscussionProduction showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                           handleFileUploadModelRequest={handleFileUploadModelRequest} productionDiscussionList={productionDiscussionList} setProductionDiscussionList={setProductionDiscussionList}
                           selectDiscussionId={selectDiscussionId}
                                 participantLists={participantList}/>}




      {loading || loadingLocal ? <LoaderMain/> : null}
      <div className="col-9 px-3 position-relative" style={{borderRight: '1px solid #d9d9d9'}}>
        <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
          <h3 className='ms-2' style={{border: "none", paddingBottom: "0"}}>Production Ready Files</h3>
          <button className='btn custom-btn pd-3' style={{marginBottom: "10px"}} onClick={() => {
            setShowModal(true);
            setModelWorkingType("productionReadyFiles")
          }}>
            UPLOAD FILE
          </button>
        </div>
        <div className='b-bottom b-top'>
          {productionReadyFilesList?.length > 0 ? <div className="d-flex px-3">
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

                {productionReadyFilesList?.map((item, index) => (
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


                          {/*<li>*/}
                          {/*  <a className="dropdown-item" onClick={() => handleSubmissionFileDelete(item?.id)}>*/}
                          {/*    Delete*/}
                          {/*  </a>*/}
                          {/*</li>*/}
                          <li>
                            <a className="dropdown-item" onClick={() => {
                              setShowModal(true);
                              setModelWorkingType("deleteProductionFile");
                              setProductionFileId(item?.id);
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
              <div className='py-4 text-center'>Production Ready Files is Empty</div>
            </div>
          }
          <div className="article-details text-end">
            <button className="article-btn" onClick={downloadAllFile}>Download All</button>
          </div>
        </div>
        <div
          className={`${expandedItem.length === 0 ? 'b-bottom' : ''}  b-top p-3 d-flex justify-content-between align-items-center `}>
          <h3 className='ms-2 my-3' style={{border: "none", paddingBottom: "0"}}>
           Discussions
          </h3>
          <Button
            className={`custom-btn pd-3`}
            onClick={() => {
              setShowModal(true);
              setModelWorkingType("addDiscussion");
            }}
          >
            ADD DISCUSSION
          </Button>
        </div>
        {productionDiscussionList?.length !== 0 ? <table className="table table-borderless b-bottom">
            <tbody>
            {productionDiscussionList?.map((item, index) => (
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
                          {richTextVisibility[index] ? '' : (
                            <div className='text-end  mb-2'>
                              <button className='btn custom-btn' onClick={() => {
                                setShowModal(true);
                                setModelWorkingType('addChatProduction');
                                setSelectDiscussionId(item.id);
                              }}>
                                ADD CHAT
                              </button>
                            </div>
                          )}
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
      <ProductionButtonPermissions submissionId={submissionId} participantList={participantList}
                                   setParticipantList={setParticipantList}
                                   />
    </div>

  )
    ;
};

export default ProductionDiscussion;
