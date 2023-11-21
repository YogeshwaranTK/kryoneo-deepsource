import React from 'react';
import {Modal} from 'reactstrap';
import {useAppDispatch} from "app/config/store";
import {
  deleteChecklistRequest
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";

const CheckListDelete = (props) => {
  const {showModal, handleClose, delItemId} = props;
  const dispatch = useAppDispatch();
  const handleCancel = () => {
    handleClose();
  };

  const handleSubmit = () => {
    dispatch(deleteChecklistRequest(delItemId))
      .then((resultAction) => {
        if (deleteChecklistRequest.fulfilled.match(resultAction)) {
          handleClose();
        }
      })
  }


  return (
    <Modal
      id="RejectRevisionRequestModel"
      isOpen={showModal}
      toggle={handleClose}
      backdrop="static"
      autoFocus={false}
    >
      <div className="modal-content">
        <div className="modal-header">
          <div className="d-flex">
            <div className="line"></div>
            <h5 className="m-0">Create Item</h5>
          </div>
          <button type="button" className="btn-close" onClick={handleCancel}></button>
        </div>
        <div className="modal-body ps-4">
          <form>
            <div className="pb-3">
              <label className='form-label'>
                Delete<span className="error_class"></span>
              </label>
              <h6>Are you sure want to delete ?</h6>
            </div>
            <div className="modal-footer justify-content-end">
              <button className="btn custom-btn-secondary" type="button" onClick={handleCancel}>
                CANCEL
              </button>
              <button className="btn custom-btn-secondary" type="button" onClick={handleSubmit}>DELETE</button>

            </div>
          </form>
        </div>
      </div>
    </Modal>
  );
};

export default CheckListDelete;


