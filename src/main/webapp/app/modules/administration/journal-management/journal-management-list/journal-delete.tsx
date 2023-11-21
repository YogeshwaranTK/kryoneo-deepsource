import React from 'react';
import {Modal} from 'reactstrap';
import {FormikProps, useFormik} from 'formik';
import * as Yup from 'yup';
import {deleteJournal} from './journal-management.reducer';
import {useAppDispatch} from 'app/config/store';
import CustomTextarea from "app/shared/component/Input-fields/customTextArea";

export interface JournalDeleteProps {
  showModal: boolean;
  handleClose: () => void;
  journalid: number;
  journakey: string;
}

export interface DELETE_GROUP_MODEL_journal {
  id: number;
  deletedRemarks: string;
  key: string;
}

const JournalDelete = (props: JournalDeleteProps) => {
  const dispatch = useAppDispatch();
  const formik: FormikProps<DELETE_GROUP_MODEL_journal> = useFormik<DELETE_GROUP_MODEL_journal>({
    enableReinitialize: true,
    initialValues: {
      id: props.journalid,
      key: '',
      deletedRemarks: '',
    },
    validationSchema: Yup.object({
      key: Yup.string()
        .oneOf([props.journakey?.toLowerCase(), props.journakey?.toUpperCase()], 'Invalid key value')
        .required('Journal key is required'),
      deletedRemarks: Yup.string().required('Reason for delete is required'),
    }),
    onSubmit(e, {resetForm}) {
      resetForm();
      dispatch(deleteJournal(e));
      props.handleClose();
    },
  });

  const handleCancel = () => {
    props.handleClose();
    formik.resetForm()
  }
  return (
    <Modal id="delete-model" isOpen={props.showModal} toggle={props.handleClose} backdrop="static" autoFocus={false}>
      <div className="modal-header">
        <div className="d-flex">
          <div className="line"></div>
          <h5 className="m-0">Are you sure you want to delete the journal?</h5>
        </div>
        <button type="button" className="btn-close" onClick={handleCancel} data-bs-dismiss="modal"
                aria-label="Close"></button>
      </div>
      <div className="modal-body ps-4">
        <form onSubmit={formik.handleSubmit}>
          <label className="form-label">
            Journal Key<span className="error_class">*</span>
          </label>
          <input
            id="key"
            name="key"
            placeholder="Enter Journal Key"
            data-cy="key"
            className="form-control"
            {...formik.getFieldProps('key')}
          />
          {formik.touched.key && formik.errors.key ? (
            <div className="error_class">{formik.errors.key}</div>
          ) : null}
          <label className="form-label mt-3">
            Reason for Deletion<span className="error_class">*</span>
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
              SUBMIT
            </button>
            <button className="btn btn--cancel" type="button" onClick={handleCancel}>
              CANCEL
            </button>
          </div>
        </form>
      </div>
    </Modal>
  );
};

export default JournalDelete;
