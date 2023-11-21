import React, {useRef, useState} from "react";
import {Modal} from "reactstrap";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import Select from "react-select";
import {upperCase} from "lodash";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {deleteSubmissionFile} from "app/modules/administration/journal-management/submission/submission.reducer";

interface ErrorState {
  file_upload_error?: string;
}

const DeleteSubmissionFile = (props) => {
  const dispatch = useAppDispatch()
  const {showModal, handleClose,handleSubmissionFileDelete, submissionId,revisionFileDetails} = props;
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
                <h5 className="m-0">Delete Submission</h5>
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
                <h6 className="m-0">Are you sure want to delete ?</h6>

              </div>
            </div>

            <div className="modal-footer">
              <button type={"button"} onClick={() => handleSubmissionFileDelete()} className="btn btn--cancel">
                DELETE
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

export default DeleteSubmissionFile;
