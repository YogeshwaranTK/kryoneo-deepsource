import React, {useEffect, useState} from 'react';
import {Modal} from 'reactstrap';
import {Translate, translate} from "react-jhipster";
import {
  getCountryList,
  getRoles,
  userGet,
  userUpdate,
  userPasswordRest
} from "app/modules/administration/user-management/user-management/user-management.reducer";
import {useAppDispatch, useAppSelector} from "app/config/store";

const UserManagementResetPasswordDialog = (props) => {
  const dispatch = useAppDispatch();
  const resetPassData = useAppSelector(state => state.userManagement.resetpassdata);
  const [massage, setMassage] = useState(false)

  useEffect(() => {
    if (Object.keys(resetPassData).length > 0) {
      setMassage(true)
    }
  }, [resetPassData])
  const resetPassSubmit = () => {
    dispatch(userPasswordRest(props.data.id));
  }
  const closeEvent =()=>{
    props.deleteHandleClose();
    setMassage(false)
  }
  return (
    <>
      <Modal id="userManagementDelete" isOpen={props.deleteModal} backdrop="static" autoFocus={false}>
        <div className="modal-content position-relative">
          <div className="modal-header">
            <div className="d-flex">
              <div className="line"></div>
              <h5 className="m-0"><Translate contentKey="buttons.ResetPassword"></Translate></h5>
            </div>
            <button type="button" onClick={() => closeEvent()} className="btn-close"></button>
          </div>
          <div className="modal-body ps-4">
            {!massage ?
              <div>
                <p><Translate contentKey="PasswordResetWarning"></Translate></p>
                <div className="text-end">
                  <button className="btn custom-btn-secondary me-2" type="button"
                          onClick={() => closeEvent()}>
                    <Translate contentKey="buttons.no"></Translate>
                  </button>
                  <button className="btn custom-btn" type="button" onClick={() => resetPassSubmit()}>
                    <Translate contentKey="buttons.yes"></Translate>
                  </button>
                </div>
              </div>
              :
              <p>
                {resetPassData["x-kjmsapp-params"]} <Translate contentKey={resetPassData["x-kjmsapp-alert"]}></Translate>
              </p>
            }
          </div>
        </div>
      </Modal>
    </>
  );
};

export default UserManagementResetPasswordDialog;
