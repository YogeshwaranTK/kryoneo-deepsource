import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import CopyEditingButtonPermissions
  from "app/modules/administration/journal-management/copy-editing/copy-editing-buttons-permission";
import {upperCase} from "lodash";
import UploadFilesCopyEditing from "app/modules/administration/journal-management/copy-editing/upload-file-copy-editing";
import {
  fileDownloadUrlRequest, getCopyEditedFilesRequest, postCopyEditingReadyFilesUpload,
} from "app/modules/administration/journal-management/copy-editing/copy-editing.reducer";
import { stateFalse } from '../copy-editing/copy-editing.reducer';


const CopyEditingGalley = (props) => {
  const {submissionId} = props
  const dispatch = useAppDispatch()
  const addCompletedFileSuccess = useAppSelector(state => state.copyEditing.addCompletedFileSuccess);
  const postCompletedFilePayload = useAppSelector(state => state.copyEditing.postCompletedFilePayload);


  const copyEditedFilesDetails = useAppSelector(state => state.copyEditing.copyEditedFilesDetails);
  const fileUploadCopyEditingReadyFilesSuccess = useAppSelector(state => state.copyEditing.fileUploadCopyEditingReadyFilesSuccess)
  const postCopyEditingFilesUploadSuccessPayload = useAppSelector(state =>  state.copyEditing.postCopyEditingFilesUploadSuccessPayload)

  const loading = useAppSelector(state => state.production.loading);
  const [showModal, setShowModal] = useState(false);
  const [copyEditingCompletedFiles, setCopyEditingCompletedFiles] = useState([])
  const [participantList, setParticipantList] = useState([]);

  const [copyEditingCompletedFilesList, setCopyEditingCompletedFilesList] = useState([])

  useEffect(()=>{

    setCopyEditingCompletedFilesList(copyEditedFilesDetails)
  },[copyEditedFilesDetails])


  useEffect(() => {
    if (addCompletedFileSuccess === true) {
      setCopyEditingCompletedFiles([...copyEditingCompletedFiles, ...postCompletedFilePayload])
      handleClose()
    }

    else if (fileUploadCopyEditingReadyFilesSuccess){
      const mergedArray = [...copyEditingCompletedFilesList ,...postCopyEditingFilesUploadSuccessPayload]
      setCopyEditingCompletedFilesList(mergedArray)
        dispatch(stateFalse())
    }
  }, [addCompletedFileSuccess,fileUploadCopyEditingReadyFilesSuccess])


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

  const handleClose = () => {
    setShowModal(false);
    setModelWorkingType('')
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
    if (modelWorkingType === 'copyEditingCompletedFiles') {
      dispatch(postCopyEditingReadyFilesUpload(requestData))
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


  useEffect(() => {
    dispatch(getCopyEditedFilesRequest(submissionId))
  }, [])


  useEffect(() => {
    setCopyEditingCompletedFiles(copyEditedFilesDetails)
  }, [copyEditedFilesDetails])

  return (
    <div className="d-flex mt-3 Submittd_list" style={{border: 'none !important'}}>
      {(showModal && modelWorkingType === 'copyEditingCompletedFiles') &&
        <UploadFilesCopyEditing showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                               handleFileUploadModelRequest={handleFileUploadModelRequest}/>}

      {loading ? <LoaderMain/> : null}
      <div className="col-9 px-3 position-relative" style={{borderRight: '1px solid #d9d9d9'}}>
        <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
          <h3 className='ms-2' style={{border: "none", paddingBottom: "0"}}>Copy-editing Completed Files</h3>
          <button className='btn custom-btn pd-3' style={{marginBottom: "10px"}} onClick={() => {
            setShowModal(true);
            setModelWorkingType("copyEditingCompletedFiles")
          }}>
            UPLOAD FILE
          </button>
        </div>
        <div className='b-bottom b-top'>
          {copyEditingCompletedFilesList?.length > 0 ?
          <>
              <div className="d-flex px-3">
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

                  {copyEditingCompletedFilesList?.map((item, index) => (
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
                        <FontAwesomeIcon icon="trash" className="fa-trash" onClick={() => handleDelete(item.id)}/>
                      </td>
                    </tr>
                  ))}
                  </tbody>
                </table>
              </div>
              <div className="article-details text-end">
                <button className="article-btn"
                        // onClick={downloadAllFile}
                >Download All</button>
              </div>
            </>
            :
            <div className='text-center'>
              <div className='py-4 text-center'>Copy-editing Completed Files is Empty</div>
            </div>
          }
        </div>
      </div>
      {/* <CopyEditingButtonPermissions submissionId={submissionId} participantList={participantList}
                                   setParticipantList={setParticipantList}
      /> */}
    </div>

  );
};

export default CopyEditingGalley;
