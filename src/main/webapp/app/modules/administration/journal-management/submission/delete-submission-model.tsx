import React, {useEffect} from 'react';
import { Modal } from 'reactstrap';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import {useAppDispatch, useAppSelector} from "app/config/store";
import {
  ArticleDeleteModelRequest
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";

const validationSchema = Yup.object().shape({
  reason: Yup.string().required('Reason is required'),
});

const DeleteSubmissionModel = (props) => {
  const articleDeleteStatus = useAppSelector(state => state.workflow.articleDeleteStatus);
  const { showModal, handleClose ,journalId} = props;
  const dispatch = useAppDispatch();
  const handleCancel = () => {
    handleClose();
  };

  const handleSubmit = (values) => {
    const data = {
      "id": journalId,
      "deletedRemarks": values.reason,
    }
    dispatch(ArticleDeleteModelRequest(data))
      .then((resultAction)=>{
        if(ArticleDeleteModelRequest.fulfilled.match(resultAction)){
          handleClose();
        }
      })
  };


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
            <h5 className="m-0">Delete Article</h5>
          </div>
          <button type="button" className="btn-close" onClick={handleCancel}></button>
        </div>
        <div className="modal-body ps-4">
          <p className="delete-warning">Are you sure you want to the  Delete Article ?</p>
          <Formik
            initialValues={{ reason: '' }}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}
          >
            {({ handleChange, values }) => (
              <Form>
                <div className="pb-3">
                  <label className="form-label">Reason for Delete Article<span className="error_class">*</span></label>
                  <Field
                    className="form-control"
                    component="textarea"
                    style={{ border: '1px solid #ccc' }}
                    name="reason"
                    placeholder="Enter your resson"
                  />
                  <ErrorMessage  name="reason" component="div" className="delete-error-message mt-1" />
                </div>
                <div className="modal-footer">
                  <button className="btn btn--cancel" type="submit">
                    SUBMIT
                  </button>
                  <button className="btn btn--cancel" type="button" onClick={handleCancel}>
                    CANCEL
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

export default DeleteSubmissionModel;


