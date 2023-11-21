import React, {useEffect} from "react";
import {Modal} from "reactstrap";
import {useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";


const deleteParticipants = (props) => {
  const {showModal, handleClose, handleParticipantDelete} = props;
  const participantDeleteSuccess = useAppSelector(state => state.production.participantDeleteSuccess);
  const loading = useAppSelector(state => state.production.loading);

  useEffect(() => {
    if (participantDeleteSuccess) {
      handleCancel()
    }
  }, [participantDeleteSuccess])

  const handleCancel = () => {
    handleClose()
  }

  const HandleSubmit = () => {
    handleParticipantDelete()
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
                <h5 className="m-0">REMOVE PARTICIPANTS</h5>
                <span className="request-head"></span>
              </div>
            </div>
            <button
              type="button"
              className="btn-close"
              onClick={handleCancel}
            ></button>
          </div>
          <div className="modal-body ps-4 position-relative">
            {loading ? <LoaderMain /> : null}
            <div className="pt-3 d-flex flex-column">
              <h6 className="mb-3">You are about to remove this participant from all stages?</h6>

              <div className="modal-footer d-flex justify-content-end">
                <button className="custom-btn" style={{ border: "none" }} type="button" onClick={handleCancel}>
                  CANCEL
                </button>
                <button type="submit" onClick={HandleSubmit} style={{ border: "none" }} className="custom-btn">
                  REMOVE
                </button>
              </div>
            </div>
          </div>
        </div>
      </Modal>
    </>
  )
}

export default deleteParticipants;
