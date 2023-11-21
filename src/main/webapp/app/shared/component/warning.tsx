import React from "react";
import {Modal} from "reactstrap";
import {useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";


const WarningModel = (props) => {
  const {showModal, handleClose, warningRequestData, title, subTitle} = props;
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
                <h5 className="m-0">{title}</h5>
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
                <h6 className="m-0">{subTitle}</h6>
              </div>
            </div>

            <div className="modal-footer justify-content-end">
              <button
                className="btn btn--cancel"
                type="button" onClick={handleCancel}>
                CLOSE
              </button>
              <button type={"button"} onClick={() => warningRequestData()} className="btn custom-btn">
                YES
              </button>
            </div>
          </div>
        </div>
      </Modal>
    </>
  )
}

export default WarningModel;
