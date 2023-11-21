import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import ProductionButtonPermissions
  from "app/modules/administration/journal-management/production/production-buttons-permission";
import {upperCase} from "lodash";
import UploadFilesProduction from "app/modules/administration/journal-management/production/upload-file-production";
import {
  fileDownloadUrlRequest,
  getProductionList, getProductionReadyFilesListRaw,
  postUploadCompletedFileRequest
} from "app/modules/administration/journal-management/production/production.reducer";


const ProductionGalley = (props) => {
  const {submissionId} = props
  const dispatch = useAppDispatch()
  const addCompletedFileSuccess = useAppSelector(state => state.production.addCompletedFileSuccess);
  const postCompletedFilePayload = useAppSelector(state => state.production.postCompletedFilePayload);
  const loading = useAppSelector(state => state.production.loading);
  const productionDetails = useAppSelector(state => state.production.productionDetails);
  const [showModal, setShowModal] = useState(false);
  const articleDetails = useAppSelector(state => state.articleDetailManagement.articleData);
  const [productionCompletedFiles, setProductionCompletedFiles] = useState([])
  const [participantList, setParticipantList] = useState([]);

  useEffect(() => {
    if (addCompletedFileSuccess === true) {
      setProductionCompletedFiles([...productionCompletedFiles, ...postCompletedFilePayload])
      handleClose()
    }

  }, [addCompletedFileSuccess])


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
    if (modelWorkingType === 'productionCompletedFiles') {
      dispatch(postUploadCompletedFileRequest(requestData))
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
    dispatch(getProductionList(submissionId))
  }, [])


  useEffect(() => {
    setProductionCompletedFiles(productionDetails?.productionCompletedFiles)
  }, [productionDetails])

  return (
    <div className="d-flex mt-3 Submittd_list" style={{border: 'none !important'}}>
      {(showModal && modelWorkingType === 'productionCompletedFiles') &&
        <UploadFilesProduction showModal={showModal} handleClose={handleClose} submissionId={submissionId}
                               handleFileUploadModelRequest={handleFileUploadModelRequest}/>}

      {loading ? <LoaderMain/> : null}
      <div className="col-9 px-3 position-relative" style={{borderRight: '1px solid #d9d9d9'}}>
        <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
          <h3 className='ms-2' style={{border: "none", paddingBottom: "0"}}>Production Completed Files</h3>
          <button className='btn custom-btn pd-3' style={{marginBottom: "10px"}} onClick={() => {
            setShowModal(true);
            setModelWorkingType("productionCompletedFiles")
          }}>
            UPLOAD FILE
          </button>
        </div>
        <div className='b-bottom b-top'>
          {productionCompletedFiles?.length > 0 ? <>
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

                  {productionCompletedFiles?.map((item, index) => (
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
                <button className="article-btn" onClick={downloadAllFile}>Download All</button>
              </div>
            </>
            :
            <div className='text-center'>
              <div className='py-4 text-center'>Production Completed Files is Empty</div>
            </div>
          }
        </div>
      </div>
      {/*<ProductionButtonPermissions submissionId={submissionId} participantList={participantList}*/}
      {/*                             setParticipantList={setParticipantList}*/}
      {/*/>*/}
    </div>

  );
};

export default ProductionGalley;
