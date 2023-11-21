import React, {useEffect, useState} from 'react';
import {useFormik, FormikProps} from 'formik';
import * as Yup from 'yup';
import {useLocation} from 'react-router-dom';
import {useAppDispatch, useAppSelector} from 'app/config/store';
import {getJournalDetails, publishDetailsUpdate} from './journals-settings.reducer';
import JournalTitle, {JournalTitleModel} from 'app/modules/administration/journal-management/journal-management-list/JournalTitle';
import {Translate, translate} from 'react-jhipster';

export interface ReRRModel {
  id: number;
  publishedBy: string;
  onlineIssn: string;
  printIssn: string;
  description: string;
  editorChief: string;
  key: string;
  title: string;
}

export const PublishingDetails = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const publishDetailsUpdateSuccess = useAppSelector(state => state.settingsManagement.publishDetailsUpdateSuccess);

  const [titleValueProps] = useState<JournalTitleModel>({
    title: 'Journal Details',
    sub_title: translate('publishing.sub_title'),
  });

  useEffect(() => {
    if (publishDetailsUpdateSuccess) {
      dispatch(getJournalDetails(location.pathname.match(/\d/g).join('')));
    }
  }, [publishDetailsUpdateSuccess]);

  const formik: FormikProps<ReRRModel> = useFormik<ReRRModel>({
    enableReinitialize: true,
    initialValues: {
      id: journals?.id,
      publishedBy: journals?.publishedBy != null ? journals?.publishedBy : '',
      onlineIssn: journals?.onlineIssn != null ? journals?.onlineIssn : '',
      printIssn: journals?.printIssn != null ? journals?.printIssn : '',
      description: journals?.description != null ? journals?.description : '',
      editorChief: journals?.editorChief ? journals?.editorChief : '',
      title: journals?.title ? journals?.title : '',
      key: journals?.key ? journals?.key : '',
    },
    validationSchema: Yup.object({
      id: Yup.string(),
      publishedBy: Yup.string(),
      onlineIssn: Yup.string().matches(/^\d{4}-\d{4}$/, 'Invalid format. Use 1234-5678 format.')
        .required("Online ISSN is required"),
      printIssn: Yup.string().matches(/^\d{4}-\d{4}$/, 'Invalid format. Use 1234-5678 format.')
        .required("Print ISSN is required"),
      editorChief: Yup.string().required('Editor(s)-in-Chief is Required'),
      title: Yup.string().required('Journal Title is Required'),
      key: Yup.string().required('Journal Key is Required'),
      description: Yup.string(),
    }),
    onSubmit(e) {
      dispatch(publishDetailsUpdate(e));
    },
  });

  const resetFormValues = () => {
    formik.resetForm();
  };

  return (
    <>
      <div className="row height-100">
        <div className="col-12 pt-3 px-4">
          <JournalTitle value={titleValueProps}/>
          <form id="custom-form-input" className="position-relative " onSubmit={formik.handleSubmit}>
            <div className="row journal_details pb-4">
              <div className="col-6 py-3">
                <label className="form-label">
                  Journal Title<span
                  className="error_class">*</span>
                </label>

                <input
                  name="title"
                  id="title"
                  placeholder="Enter Journal Title"
                  data-cy="title"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('title')}
                />
                {formik.touched.title && formik.errors.title ?
                  <div className="error">{formik.errors.title}</div> : null}
              </div>

              <div className="col-6 py-3">
                <label className="form-label">
                  Journal Key<span
                  className="error_class">*</span>
                </label>

                <input
                  name="key"
                  id="key"
                  placeholder="Enter Journal Key"
                  data-cy="key"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('key')}
                />

                {formik.touched.key && formik.errors.key ?
                  <div className="error">{formik.errors.key}</div> : null}
              </div>

              <div className="col-4 py-3">
                <label className="form-label">
                  <Translate contentKey="publishing.online_issn">Online ISSN</Translate><span
                  className="error_class">*</span>
                </label>
                <input
                  name="onlineIssn"
                  id="onlineIssn"
                  placeholder="Enter Online ISSN"
                  data-cy="onlineIssn"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('onlineIssn')}
                />
                {formik.touched.onlineIssn && formik.errors.onlineIssn ? (
                  <div className="error">{formik.errors.onlineIssn}</div>
                ) : null}
              </div>

              <div className="col-4 py-3">
                <label className="form-label">
                  <Translate contentKey="publishing.print_issn">Print ISSN</Translate><span
                  className="error_class">*</span>
                </label>

                <input
                  name="printIssn"
                  id="printIssn"
                  placeholder={translate('publishing.print_issn_placeholder')}
                  data-cy="printIssn"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('printIssn')}
                />
                {formik.touched.printIssn && formik.errors.printIssn ?
                  <div className="error">{formik.errors.printIssn}</div> : null}
              </div>

              <div className="col-4 py-3">
                <label className="form-label">
                  Editor(s)-in-Chief<span className="error_class">*</span>
                </label>
                <input
                  name="editorChief"
                  id="editorChief"
                  placeholder='Enter the Editor(s)-in-Chief'
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('editorChief')}
                />
              </div>

              <div className="col-12 pt-3">
                <label className="form-label">
                  Journal Description
                </label>

                <textarea
                  name="description"
                  id="description"
                  placeholder="Enter Journal Description"
                  data-cy="description"
                  className="form-control"
                  {...formik.getFieldProps('description')}
                />
                {formik.touched.description && formik.errors.description ? (
                  <div className="error">{formik.errors.description}</div>
                ) : null}
              </div>
            </div>
            <div className="d-flex b-top pt-4 justify-content-end">
              <div className="pe-2">
                <button
                  className="btn custom-btn-secondary"
                  type="button"
                  onClick={resetFormValues}
                >
                  RESET
                </button>
              </div>
              <div className="px-2">
                <button className="btn custom-btn" type="submit">
                  SAVE & UPDATE
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default PublishingDetails;
