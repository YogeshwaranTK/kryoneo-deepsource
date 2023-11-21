import React, {useEffect, useState} from "react";
import {Modal} from "reactstrap";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import {
  postAcceptFileAndMoveReview,
} from "app/modules/administration/journal-management/submission/submission.reducer";

interface ErrorState {
  submissionFilesError?: string;
}

const AcceptSubmissionMoveReview = (props) => {
  const dispatch = useAppDispatch();
  const {
    showModal,
    handleClose,
    submissionId,
    submissionReadyFilesList
  } = props;
  const postAcceptFileAndMoveReviewSuccess = useAppSelector(state => state.submission.postAcceptFileAndMoveReviewSuccess);
  const loading = useAppSelector(state => state.production.loading);
  const [selectedFinalFileList, setSelectedFinalFileList] = useState([])
  const [errors, setErrors] = useState<ErrorState>({});

  const handleCancel = () => {
    setSelectedFinalFileList([])
    handleClose()
  }


  useEffect(() => {
    if (postAcceptFileAndMoveReviewSuccess === true) {
      handleCancel();
    }
  }, [postAcceptFileAndMoveReviewSuccess]);


  const HandleChange = (e, value) => {
    if (e.target.checked) {
      setSelectedFinalFileList([...selectedFinalFileList, value]);
    } else {
      setSelectedFinalFileList(selectedFinalFileList.filter((id) => id !== value));
    }
  }

  const validateForm = () => {
    const errorsValidate: ErrorState = {};

    if (selectedFinalFileList.length === 0) {
      errorsValidate.submissionFilesError = 'Select at least one file';
    }

    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };

  const HandleSubmit = () => {

    const data = {
      selectedFinalFileList,
      submissionId
    }

    if (validateForm()) {
      dispatch(postAcceptFileAndMoveReview(data))
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
                <h5 className="m-0">SEND TO REVIEW</h5>
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
            <p className="request-sub-head m-0">Select files below to send them to the review stage<span
              className="error_class">*</span> {errors.submissionFilesError &&
              <span className="error"> - {errors.submissionFilesError}</span>}</p>
            {
              submissionReadyFilesList?.map((value) => (
                <div className="custom-checkbox" key={value.id}>
                  <input className={`form-check-input`} onChange={(e) => HandleChange(e, value.id,)}
                         type="checkbox" name="agree"/>
                  <label><span><b>{value?.fileType?.name}</b> -  </span>
                    {value?.file}</label>
                </div>
              ))
            }
            <div className="modal-footer">
              <button type={"submit"} onClick={HandleSubmit} className="btn btn--cancel">
                SEND TO REVIEW
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

export default AcceptSubmissionMoveReview;
