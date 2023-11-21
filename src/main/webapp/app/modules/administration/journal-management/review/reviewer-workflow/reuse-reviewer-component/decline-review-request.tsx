import React, {useState} from "react";
import {Modal} from "reactstrap";
import RichText from "app/shared/rich-text/rich-text";
import {useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";


interface ErrorState {
  description?: string;
}

const RejectReviewer = (props) => {
  const {
    showModal,
    handleClose,
    requestSendFunction,
    userId
  } = props;

  const loading = useAppSelector(state => state.production.loading);
  const [errors, setErrors] = useState<ErrorState>({});
  const [discussionDescription, setDiscussionDescription] = useState("");

  const handleCancel = () => {
    setDiscussionDescription('')
    handleClose()
  }


  const discussionDescriptionChange = (value: string) => {
    setDiscussionDescription(value);
  };


  const cancelReviewer = () => {
    if (validateForm()){
      requestSendFunction({userId, discussionDescription})
    }
  };


  const validateForm = () => {
    const errorsValidate: ErrorState = {};
    if (discussionDescription.length === 0) {
      errorsValidate.description = 'Description Required';
    }
    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
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
                <h5 className="m-0">Cancel Reviewer</h5>
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
            <div className="pt-3">
              <label className="form-label">
                Description<span className="error_class">*</span>
                {errors.description &&
                  <span className="error"> - {errors.description}</span>}
              </label>
              <RichText
                value={discussionDescription}
                placeHolderText="Enter your Description"
                onValueChange={discussionDescriptionChange}
              />
            </div>

            <div className="modal-footer">
              <button type={"button"} onClick={() => cancelReviewer()} className="btn btn--cancel">
                Submit
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

export default RejectReviewer;
