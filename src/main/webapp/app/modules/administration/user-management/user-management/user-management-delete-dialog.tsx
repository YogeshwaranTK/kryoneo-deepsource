import React from 'react';
import { Modal } from 'reactstrap';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import {userRemove} from "app/modules/administration/user-management/user-management/user-management.reducer";
import * as Yup from "yup";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import {translate, Translate} from "react-jhipster";

const validationSchema = Yup.object().shape({
  reason: Yup.string().required(translate("userManagementUsers.ReasonRequired")),
});
const UserManagementDeleteDialog = (props) => {
  const loading = useAppSelector(state => state.userManagement.loadinguserRemove);
  const { deleteHandleClose ,userId} = props;
  const dispatch = useAppDispatch();
  const handleCancel = () => {
    deleteHandleClose();
  };



  const handleSubmit = (e) => {
    e['userId'] = userId
    dispatch(userRemove(e))
      .then((resultAction)=>{
        if(userRemove.fulfilled.match(resultAction)){
          deleteHandleClose();
        }
      })


  };

  return (
    <Modal id="userManagementDelete" isOpen={props.deleteModal} toggle={deleteHandleClose} backdrop="static" autoFocus={false}>
    <div className="modal-content position-relative">
      {/*{!loading ? <LoaderMain /> : null}*/}
        <div className="modal-header">
          <div className="d-flex">
            <div className="line"></div>
            <h5 className="m-0"><Translate contentKey="userManagementUsers.DeleteUser">Delete User</Translate></h5>
          </div>
          <button type="button" className="btn-close" onClick={handleCancel}></button>
        </div>
        <div className="modal-body ps-4">
          <p className="delete-warning"><Translate contentKey="userManagementUsers.ConfirmDeleteDes">Are you sure you want to delete the user?</Translate></p>
          <Formik
            initialValues={{ reason: '' }}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}
          >
            {({ handleChange, values }) => (
              <Form>
                <div className="pb-3">
                  <label className="form-label"><Translate contentKey="userManagementUsers.ReasonDeleteUser"></Translate><span className="error_class"> *</span></label>
                  <Field
                    className="form-control"
                    component="textarea"
                    style={{ border: '1px solid #ccc' }}
                    name="reason"
                    placeholder={translate('userManagementUsers.EnterReasonPH')}
                  />
                  <ErrorMessage  name="reason" component="div" className="delete-error-message mt-2" />
                </div>
                <div className="modal-footer">
                  <button className="btn btn--cancel" type="submit">
                    <Translate contentKey="buttons.submit"></Translate>
                  </button>
                  <button className="btn btn--cancel" type="button" onClick={handleCancel}>
                    <Translate contentKey="buttons.cancel"></Translate>
                  </button>
                </div>
              </Form>
            )}
          </Formik>
        </div>
      </div>
    </Modal>
  );
};

export default UserManagementDeleteDialog;
