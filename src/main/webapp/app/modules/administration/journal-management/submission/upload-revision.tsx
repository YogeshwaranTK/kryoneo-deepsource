import React, {useRef, useState} from "react";
import {Modal} from "reactstrap";
import {useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import Select from "react-select";
import {upperCase} from "lodash";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

interface ErrorState {
  file_upload_error?: string;
}

const UploadRevision = (props) => {
  const {showModal, handleClose, handleFileUploadModelRequest, submissionId,revisionFileDetails} = props;
  const loading = useAppSelector(state => state.submission.loading);
  const [errors, setErrors] = useState<ErrorState>({});
  const fileInputRef = useRef(null);
  const [selectedFiles, setSelectedFiles] = useState<any>({});
  const [isLoading, setIsLoading] = useState(false)

  const handleCancel = () => {
    setSelectedFiles({})
    handleClose()
  }

  const validateForm = () => {
    const errorsValidate: ErrorState = {};

    if (selectedFiles?.fileEndPoint === undefined) {
      errorsValidate.file_upload_error = 'Upload your file';
    }


    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };

  const getFileExtension = (filename) => {
    if (filename !== undefined) {
      const lastDotIndex = filename.lastIndexOf(".");
      if (lastDotIndex !== -1) {
        return filename.substring(lastDotIndex);
      } else {
        return null;
      }
    }
  }

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const updatedFiles = {
        newId: Math.floor(Math.random() * (Number.MAX_SAFE_INTEGER - 100000)) + 100000,
        fileType: {
          id: revisionFileDetails.id,
          name: revisionFileDetails.otherProperty
        },
        file: file?.name,
        fileEndPoint: file,
      }
      setSelectedFiles(updatedFiles);
    }
  };

  const HandleSubmit = () => {
    if (validateForm()) {
      const formData = new FormData();
      formData.append("file", selectedFiles?.fileEndPoint);
      formData.append("originalFileId", revisionFileDetails?.id);
      const requestData = {
        formData,
        submissionId
      }
      handleFileUploadModelRequest(requestData)
      setIsLoading(false)
    }
  }

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
                <h5 className="m-0">Upload Revision</h5>
              </div>
            </div>
            <button
              type="button"
              className="btn-close"
              onClick={handleCancel}
            ></button>
          </div>
          <div className="modal-body ps-4 position-relative">
            {loading || isLoading ? <LoaderMain/> : null}
            <div className="col-12 pb-3 " id="custom-form-input">
              <div className='row mt-3'>
                <div className="col-4">
                  <label className="form-label">
                    File Type<span className="error_class"></span>
                  </label>
                  <Select
                    placeholder='Select file type'
                    isDisabled={true}
                    value={{
                      value: revisionFileDetails.id,
                      label: revisionFileDetails.otherProperty,
                    }}
                  />
                </div>
                <div className="col-8">
                  <label className="form-label">
                    Upload File<span className="error_class">*</span> {errors.file_upload_error &&
                    <span className="error"> {errors.file_upload_error}</span>}
                  </label>
                  <input accept="image/png, image/jpeg"
                         ref={fileInputRef} name="file" id="file" type="file" className="form-control"
                         onChange={handleFileChange}/>
                </div>
              </div>
            </div>
            {selectedFiles?.fileEndPoint !== undefined ?
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
                    File Type
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
                <tr>
                  <td>1</td>
                  <td>{selectedFiles?.file}</td>
                  <td>{selectedFiles?.fileType?.name}</td>
                  <td>{upperCase(getFileExtension(selectedFiles?.file))}</td>
                  <td>
                    <FontAwesomeIcon icon="trash" className="fa-trash"
                                     onClick={() => setSelectedFiles({})}
                    />
                  </td>
                </tr>
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

export default UploadRevision;
