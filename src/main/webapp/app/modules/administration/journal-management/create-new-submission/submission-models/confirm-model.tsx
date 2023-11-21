import React from 'react';
import {Modal} from 'reactstrap';
import {useAppSelector} from "app/config/store";
const ConFirmModel = ({showConfirmModal,handleClose,confirmSubmit}) => {
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const handleCancel = () =>{
    handleClose();
  }

  return (
    <Modal id="AddContributorModel" isOpen={showConfirmModal} toggle={handleClose} backdrop="static" autoFocus={false}>
      <div className="modal-header">
        <div className="d-flex">
          <div className="line"></div>
          <h5 className="m-0">FINAL CONFIRMATION</h5>
        </div>
        <button type="button" className="btn-close" onClick={handleCancel} data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div className="modal-body ps-4">
        <form>
          <p>Your submission for <b>{journals?.title} Journal Review</b> has been uploaded and is ready to be sent.
            You may go back to review and adjust any of the information you have entered before continuing. When you are ready, click "Final Submission".</p>
          <div className="modal-footer">
            <button
              className="btn custom-btn-secondary save_draft_btn"
              type="button" onClick={handleCancel}>
              CANCEL
            </button>

            <button className="custom-btn" style={{border:"none"}} type="button" onClick={()=>confirmSubmit()}>
              FINISH SUBMISSION
            </button>
          </div>
        </form>
      </div>
    </Modal>
  );
};

export default ConFirmModel;
