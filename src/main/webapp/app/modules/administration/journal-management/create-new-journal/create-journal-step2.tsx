import React, {useState, useRef} from 'react';
import {useFormik, FormikProps} from 'formik';
import {useAppSelector} from 'app/config/store';
import * as Yup from 'yup';
import LoaderMain from 'app/shared/Loader/loader-main';
import {Translate, translate} from 'react-jhipster';
import CustomTextarea from "app/shared/component/Input-fields/customTextArea";

export interface stepTwoModel {
  summary: string;
}

interface ModalErrors {
  fileUploadError?: string;
}

const CreateJournalStep2 = ({prevStep, stepTwoData, setStepTwoData, stepRoute, setStepActivate, stepActivate}) => {
  const loading = useAppSelector(state => state.settingsManagement.loading);
  const fileInputRef = useRef(null);
  const [errors, setErrors] = useState<ModalErrors>({});

  const validateForm = () => {
    const errorsValidate: ModalErrors = {};

    if (stepTwoData.file === null) {
      errorsValidate.fileUploadError = 'Journal cover image required';
    }
    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };


  const formik: FormikProps<stepTwoModel> = useFormik<stepTwoModel>({
    enableReinitialize: true,
    initialValues: {
      summary: stepTwoData?.summary !== '' ? stepTwoData?.summary : '',
    },
    validationSchema: Yup.object({
      summary: Yup.string().required(translate('journal_summary.journal_summary_required'))
    }),
    onSubmit(e) {
      if (validateForm()) {

        setStepTwoData({
          ...stepTwoData,
          summary: e.summary,
        });

        setStepActivate({
          ...stepActivate,
          stepTwo: true
        })
        stepRoute(3)
      }
    },
  });

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        setStepTwoData({
          ...stepTwoData,
          fileEndPoint : e.target.result,
          file
        });
      };
      reader.readAsDataURL(file);
    } else {
      setStepTwoData({
        ...stepTwoData,
        file: null,
        fileEndPoint : null
      });
    }
  };

  return (
    <div>
      <div className="b-bottom">
        <div className="py-2 pb-3">
          <div className="d-flex">
            <div className="line"></div>
            <h6 className="heading mb-0"><Translate contentKey="journal_summary.journal_title"></Translate></h6>
          </div>
          <p className="title-description"><Translate contentKey='journal_summary.journal_fillMandatory'></Translate>
          </p>
        </div>
      </div>

      <form className='row' onSubmit={formik.handleSubmit}>
        <div className="col-8 position-relative" id="custom-form-input">
          {loading ? <LoaderMain/> : null}
          <div className="col-12 py-3 d">
            <label className="form-label">
              <Translate contentKey="journal_summary.journal_cover_image">Journal Cover Image</Translate><span
              className="error_class">*</span>
            </label>
            <input accept="image/png, image/jpeg"
                   ref={fileInputRef} name="file" id="file" type="file" className="form-control"
                   onChange={handleFileChange}/>
            {errors.fileUploadError && <span className="error">{errors.fileUploadError}</span>}
          </div>

          <div className="col-12 pt-3">
            <label className="form-label">
              <Translate contentKey="journal_summary.journal_title">Journal Summary </Translate><span
              className="error_class">*</span>
            </label>
            <CustomTextarea
              id="summary"
              name="summary"
              placeholder={translate("journal_summary.journal_summary_placeholder")}
              dataCy="summary"
              field={formik.getFieldProps('summary')}
              form={formik}
              height={"200px"}
              disabled={false}
            />
          </div>
        </div>
        <div className="col-4 journalSummaryimg">
          {stepTwoData.fileEndPoint && <img src={stepTwoData.fileEndPoint} alt="Image Preview"/>}
        </div>

        <div className="d-flex b-top pt-4 flex-row-reverse">
          <div className="pe-2 m-top">
            <button className="btn custom-btn" type="submit" onClick={() => validateForm()}>
              <Translate contentKey="buttons.Save&Next"></Translate>
            </button>
          </div>
          <div className="px-2 m-top">
            <button className="btn custom-btn-secondary" type="button" onClick={prevStep}>
              <Translate contentKey="buttons.Previous"></Translate>
            </button>
          </div>
        </div>
      </form>
    </div>
  );
};

export default CreateJournalStep2;
