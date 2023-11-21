import React, {useEffect} from 'react';
import {useAppSelector} from 'app/config/store';
import {FormikProps, useFormik} from 'formik';
import {
  DELETE_GROUP_MODEL,
} from 'app/modules/administration/journal-management/contributors/contributors.reducer';
import * as Yup from 'yup';
import {Modal} from 'reactstrap';
import CustomTextarea from "app/shared/component/Input-fields/customTextArea";

const DeleteAssignContributors = (props) => {
  const deletedEditorialSuccess = useAppSelector(state => state.journalContributors.deletedEditorialSuccess);
  const {handleClose, userId, handleDeleteUserRequest} = props;
  const formik: FormikProps<DELETE_GROUP_MODEL> = useFormik<DELETE_GROUP_MODEL>({
    enableReinitialize: true,
    initialValues: {
      userId,
      deletedRemarks: '',
    },
    validationSchema: Yup.object({
      deletedRemarks: Yup.string().required('Reason for remove is required'),
    }),
    onSubmit(e) {
      // dispatch(deleteAssignedAuthor(e));
      handleDeleteUserRequest(e)
    },
  });

  useEffect(() => {
    if (deletedEditorialSuccess) {
      handleCancel()
    }
  }, [deletedEditorialSuccess])

  const handleCancel = () => {
    formik.resetForm();
    handleClose();
  };

  return (
    <Modal id="GroupDelete" isOpen={props.showModal} toggle={handleClose} backdrop="static" autoFocus={false}>
      <div className="modal-content">
        <div className="modal-header">
          <div className="d-flex">
            <div className="line"></div>
            <h5 className="m-0">Delete User</h5>
          </div>
          <button type="button" className="btn-close" onClick={handleCancel}></button>
        </div>
        <div className="modal-body ps-4">
          <p className="delete-warning">Are you sure you want to delete the User?</p>
          <form onSubmit={formik.handleSubmit}>
            <label className="form-label">
              Delete Reason<span className="error_class">*</span>
            </label>
            <CustomTextarea
              id="deletedRemarks"
              name="deletedRemarks"
              placeholder="Enter Delete Reason"
              dataCy="deletedRemarks"
              field={formik.getFieldProps('deletedRemarks')}
              form={formik}
              height={"100px"}
              disabled={false}
            />
            <div className="modal-footer">
              <button className="btn btn--primary" type="submit">
                CONFIRM
              </button>
              <button className="btn btn--cancel" type="button" onClick={handleCancel}>
                CANCEL
              </button>
            </div>
          </form>
        </div>
      </div>
    </Modal>
  );
};

export default DeleteAssignContributors;
