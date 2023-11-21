import React from 'react';
import '../journal-management-list/journal-management.scss';
import * as Yup from 'yup';
import {useFormik, FormikProps} from 'formik';
import {JCmodel} from 'app/modules/administration/journal-management/journal-management-list/journal.model';
import LoaderMain from 'app/shared/Loader/loader-main';
import {translate, Translate} from "react-jhipster";
import CustomTextarea from "app/shared/component/Input-fields/customTextArea";
import {useAppSelector} from "app/config/store";

const CreateJournalStep1 = ({stepOneData, setStepOneData, stepRoute, setStepActivate, stepActivate}) => {
  const loading = useAppSelector(state => state.journalManagement.loading);

  const formik: FormikProps<JCmodel> = useFormik<JCmodel>({
    enableReinitialize: true,
    initialValues: {
      title: stepOneData?.title ? stepOneData?.title : '',
      key: stepOneData?.key ? stepOneData?.key : '',
      description: stepOneData?.description ? stepOneData?.description : '',
      onlineIssn: stepOneData?.onlineIssn ? stepOneData?.onlineIssn : '',
      printIssn: stepOneData?.printIssn ? stepOneData?.printIssn : '',
      editorChief: stepOneData?.editorChief ? stepOneData?.editorChief : '',
    },
    validationSchema: Yup.object({
      title: Yup.string().required(translate("publishing.JournalTitleRequired"))
        .min(2, translate("publishing.JournalTitleMust2Characters"))
        .max(200, translate("publishing.JournalTitleMust200Characters")),
      key: Yup.string()
        .required(translate("publishing.keyRequired"))
        .max(5, translate("publishing.MaximumLength5characters"))
        .matches(/^[A-Za-z]+$/, translate("publishing.KeyOnlyAlphabets")),
      onlineIssn: Yup.string().required(translate("publishing.online_issn_required")).matches(/^\d{4}-\d{4}$/, translate("publishing.InvalidFormat1234-5678")),
      printIssn: Yup.string().required(translate("publishing.print_issn_required")).matches(/^\d{4}-\d{4}$/, translate("publishing.InvalidFormat1234-5678")),
      editorChief: Yup.string().required(translate("publishing.Editor_in_ChiefRequired")),
      description: Yup.string()
    }),
    onSubmit(e) {
      setStepOneData(e)
      setStepActivate({
        ...stepActivate,
        stepOne: true
      })
      stepRoute(2)
    },
  });

  return (
    <div>
      <div className="b-bottom">
        <div className="py-2 pb-3">
          <div className="d-flex">
            <div className="line"></div>
            <h6 className="heading mb-0"><Translate contentKey="journal_breadcrumb.addNewJournal"></Translate></h6>
          </div>
          <p className="title-description"><Translate contentKey="journal.fillMandatory"></Translate></p>
        </div>
      </div>
      <form onSubmit={formik.handleSubmit} id="custom-form-input" className="journal_create_new">
        <div className="position-relative">
          {loading ? <LoaderMain/> : null}
          <div className="row">
            <div className="col-8 py-3">
              <label className="form-label">
                <Translate contentKey="journalCreateWorkflow.journalTitle"></Translate><span
                className="error_class">*</span>
              </label>
              <input
                id="title"
                name="title"
                placeholder={translate("journalPlaceholder.journalTitle")}
                type="text"
                className="form-control"
                {...formik.getFieldProps('title')}
              />
              {formik.touched.title && formik.errors.title ? (
                <div className="error_class">{formik.errors.title}</div>) : null}
            </div>
            <div className="col-4 py-3">
              <label className="form-label">
                <Translate contentKey="journal.key"></Translate><span className="error_class">*</span>
              </label>
              <input
                id="key"
                name="key"
                placeholder={translate("publishing.keyPlaceholder")}
                data-cy="key"
                type="text"
                className="form-control"
                {...formik.getFieldProps('key')}
              />
              {formik.touched.key && formik.errors.key ? <div className="error_class">{formik.errors.key}</div> : null}
            </div>
            <div className="col-4 py-3">
              <label className="form-label">
                <Translate contentKey="publishing.online_issn">Online ISSN</Translate><span
                className="error_class">*</span>
              </label>
              <input
                name="onlineIssn"
                id="onlineIssn"
                placeholder={translate("publishing.online_issn_placeholder")}
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
                <Translate contentKey="publishing.Editor_in_Chief"></Translate><span className="error_class">*</span>
              </label>
              <input
                name="editorChief"
                id="editorChief"
                placeholder={translate("publishing.Editor_in_ChiefPlaceholder")}
                type="text"
                className="form-control"
                {...formik.getFieldProps('editorChief')}
              />
              {formik.touched.editorChief && formik.errors.editorChief ? (
                <div className="error">{formik.errors.editorChief}</div>
              ) : null}
            </div>
            <div className="col-12 pb-3">
              <label className="form-label">
                <Translate contentKey="publishing.journal_description"></Translate>
              </label>
              <CustomTextarea
                id="description"
                name="description"
                placeholder={translate("publishing.journal_description_placeholder")}
                dataCy="access_type"
                field={formik.getFieldProps('description')}
                form={formik}
                height={"200px"}
                disabled={false}
              />
            </div>
          </div>
        </div>
        <div className="d-flex flex-row-reverse">
          <div className="pe-2 m-top">
            <button className="btn custom-btn" type="submit">
              <Translate contentKey="buttons.Save&Next"></Translate>
            </button>
          </div>
        </div>
      </form>
    </div>
  );
};

export default CreateJournalStep1;
