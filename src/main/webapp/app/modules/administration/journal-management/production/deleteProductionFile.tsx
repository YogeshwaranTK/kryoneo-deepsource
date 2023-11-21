import React, {useState} from "react";
import {Modal} from "reactstrap";
import {useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";

const DeleteProductionFile = (props) => {
  const {showModal, handleClose, handleProductionFileDelete, submissionId, revisionFileDetails} = props;
  const loading = useAppSelector(state => state.submission.loading);

  const handleCancel = () => {
    handleClose()
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
            {loading ? <LoaderMain/> : null}
            <div className="col-12 pb-3 " id="custom-form-input">
              <div className='row mt-3'>
                <h6 className="m-0">Are you sure want to delete ?</h6>

              </div>
            </div>

            <div className="modal-footer">
              <button type={"button"} onClick={() => handleProductionFileDelete()} className="btn btn--cancel">
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

export default DeleteProductionFile;
