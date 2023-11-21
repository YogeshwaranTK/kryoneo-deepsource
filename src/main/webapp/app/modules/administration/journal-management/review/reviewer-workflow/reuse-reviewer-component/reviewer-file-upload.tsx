import React, {useEffect, useRef, useState} from "react";
import {Modal} from "reactstrap";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import {upperCase} from "lodash";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

interface ErrorState {
  file_upload_error?: string;
}

const UploadFilesReviewer = (props) => {
  const {showModal, handleClose, handleUploadFileReviewer} = props;
  const loading = useAppSelector(state => state.reviewer.loading);
  const reviewerReplyUploadFileSuccess = useAppSelector(state => state.reviewer.reviewerReplyUploadFileSuccess);
  const [errors, setErrors] = useState<ErrorState>({});
  const fileInputRef = useRef(null);
  const [selectedFiles, setSelectedFiles] = useState([]);

  const handleCancel = () => {
    setSelectedFiles([])
    handleClose()
  }

  useEffect(() => {
    if (reviewerReplyUploadFileSuccess) {
      handleCancel()
    }
  }, [reviewerReplyUploadFileSuccess])
  const validateForm = () => {
    const errorsValidate: ErrorState = {};

    if (selectedFiles.length === 0) {
      errorsValidate.file_upload_error = 'Upload your file';
    }

    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };

  const getFileExtension = (filename) => {
    const lastDotIndex = filename.lastIndexOf(".");
    if (lastDotIndex !== -1) {
      return filename.substring(lastDotIndex);
    } else {
      return null;
    }
  }

  const handleFileChanges = (e: React.ChangeEvent<HTMLInputElement>) => {
    const inputElement = e.target as HTMLInputElement;
    const files = inputElement.files;
    if (files) {
      const updatedFiles = Array.from(files).map((file) => ({
        name: `${file?.name}`,
        file,
      }));
      setSelectedFiles([...selectedFiles, ...updatedFiles]);
    }
    inputElement.value = '';
  };

  const HandleSubmit = () => {
    if (validateForm()) {
      const formData = new FormData();
      selectedFiles.forEach((file) => {
        const filename = 'file'
        formData.append(filename, file.file);
      });
      handleUploadFileReviewer(formData)
    }
  }

  const handleDelete = (name, type) => {
    const newArray = selectedFiles.filter((file) => file?.name !== name || file?.type !== type);
    setSelectedFiles(newArray);
  };

  return (
    <>
      <Modal
        id="RequestSubmission"
        className="request-submission"
        isOpen={showModal}
        toggle={handleClose}
        backdrop="static"
        autoFocus={false}>

        <div className="modal-content">
          <div className="modal-header">
            <div className="d-flex">
              <div className="line"></div>
              <div className="d-flex">
                <h5 className="m-0">File Upload</h5>
              </div>
            </div>
            <button
              type="button"
              className="btn-close"
              onClick={handleCancel}
            ></button>
          </div>
          <div className="modal-body ps-4 position-relative">

            {loading ? <LoaderMain/> : null}
            <div className="col-12 pb-3 " id="custom-form-input">
              <div className='row mt-3'>
                <div className="col-12">
                  <label className="form-label">
                    Upload File<span className="error_class">*</span> {errors.file_upload_error &&
                    <span className="error"> {errors.file_upload_error}</span>}
                  </label>
                  <input multiple ref={fileInputRef} name="file" id="file" type="file"
                         className="form-control" onChange={handleFileChanges}/>
                </div>
              </div>
            </div>
            {selectedFiles?.length > 0 ?
              <table className="table work-flow_table">
                <thead>
                <tr>
                  <th scope="col" className="hand ">
                    #
                  </th>
                  <th scope="col" className="hand">
                    File Name
                  </th>
                  <th scope="col" className="hand">
                    File Format
                  </th>
                  <th scope="col" className="hand">
                    Action
                  </th>
                </tr>
                </thead>
                <tbody>
                {selectedFiles?.map((file, index) => (
                  <tr key={index}>
                    <td>{index + 1}</td>
                    <td>{file?.name}</td>
                    <td>{upperCase(getFileExtension(file?.name))}</td>
                    <td>
                      <FontAwesomeIcon icon="trash" className="fa-trash"
                                       onClick={() => handleDelete(file?.name, file?.type)}/>
                    </td>
                  </tr>
                ))}
                </tbody>
              </table> :
              <div className='pb-4 pb-3 text-center'>Upload files not found.</div>
            }
            <div className="modal-footer">
              <button type={"submit"} onClick={HandleSubmit} className="btn btn--cancel">
                UPLOAD FILE
              </button>
              <button
                className="btn btn--cancel"
                type="button" onClick={handleCancel}>
                CANCEL
              </button>
            </div>
          </div>
        </div>
      </Modal>
    </>
  )
}

export default UploadFilesReviewer;
