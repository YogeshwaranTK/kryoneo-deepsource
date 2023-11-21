import React, {useEffect, useState} from 'react';
import './work-flow.scss';
import JButton from "app/shared/component/button/button";
import {useFormik, FormikProps} from 'formik';
import * as Yup from 'yup';
import Select from "react-select";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {
  basicJournalUpdate, getJournalSubmissionLangListRequest, getSubmissionDetailsRaw
} from "app/modules/administration/journal-management/create-new-submission/work-flow.reducer";
import LoaderMain from "app/shared/Loader/loader-main";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {translate, Translate} from "react-jhipster";
import CustomTextarea from "app/shared/component/Input-fields/customTextArea";
import {PageControl} from "app/config/route-config";

export interface SubmissionBaseDetails {
  title?: string,
  prefix?: string,
  subTitle?: string,
  description?: string
  agree?: boolean
}

const BasicDetails = ({onTabChange, routeState, BasicError, setBasicError}) => {
    const dispatch = useAppDispatch();
    const basicJournalUpdateStatus = useAppSelector(state => state.workflow.basicJournalUpdateStatus);
    const journalSubmissionLangList = useAppSelector(state => state.workflow.JournalLangList);
    const loading = useAppSelector(state => state.workflow.loading);
    const [errors, setErrors] = useState<{ sub_lang_error?: string; }>({});
    const [journalLanguageId, set_journalLanguageId] = useState<any | null>(null);
    const [submissionDetailState, setSubmissionDetailState] = useState<any | string>(null);
    const [nextOrDraft, setNextOrDraft] = useState('');
    const [loadingLocal, setLoadingLocal] = useState(true);
    const [keyValue, setKeyValue] = useState([]);
    const [inputValue, setInputValue] = useState('');
    const [error, setError] = useState('');
    const submissionDetails = useAppSelector(state => state.submission.submissionDetails);

    const handleChange = (e: any) => {
      setInputValue(e.target.value);
    };

    const handleKeyPress = (event) => {
      if (event.key === 'Enter') {
        const trimmedValueSet = new Set(inputValue.split(',').map(keyword => keyword.trim()));
        const trimmedValue = [...trimmedValueSet];

        if (trimmedValue.length === 0) {
          setError('Please enter a value');
        } else {
          setError('');

          const duplicates = trimmedValue.filter(value =>
            keyValue.some(existingValue =>
              existingValue.toLowerCase() === value.toLowerCase()
            )
          );

          if (duplicates.length > 0) {
            setError(`Keywords '${duplicates.join(', ')}' already exist.`);
          } else {
            const updatedKeywords = [...keyValue, ...trimmedValue];
            setKeyValue(updatedKeywords);
            setInputValue('');
          }
        }
      }
    };


    const handleAddTag = () => {
      const syntheticEvent = {
        key: 'Enter',
      };
      handleKeyPress(syntheticEvent);
    };

    const handleDelete = e => {
      setKeyValue(keyValue.filter(value => value !== e));
    };


    useEffect(() => {
      setLoadingLocal(true);
      if (typeof (routeState) === PageControl.number) {
        getSubmissionDetailsRaw(parseInt(routeState, 10)).then(response => {
          setLoadingLocal(false);
          setSubmissionDetailState(response?.data);
          if (response?.data?.submissionLanguage !== null) {
            set_journalLanguageId({
              id: response.data.submissionLanguage.id,
              value: response.data.submissionLanguage.langKey,
              label: response.data?.submissionLanguage.name
            });
          }
        })
      }
    }, []);


    useEffect(() => {
      if (basicJournalUpdateStatus) {
        setBasicError('');
        dispatch(getJournalSubmissionLangListRequest());
      }
    }, [basicJournalUpdateStatus]);

    const validateForm = () => {
      const errorsValidate: { sub_lang_error?: string; } = {};
      if (journalLanguageId === null) {
        errorsValidate.sub_lang_error = 'Select Submission Language';
      }
      setErrors(errorsValidate);
      return Object.keys(errorsValidate).length === 0;
    };


    const formik: FormikProps<SubmissionBaseDetails> = useFormik<SubmissionBaseDetails>({
      enableReinitialize: true,
      initialValues: {
        title: submissionDetailState?.title ? submissionDetailState?.title : '',
        subTitle: submissionDetailState?.subTitle ? submissionDetailState?.subTitle : '',
        agree: submissionDetailState?.agree ? submissionDetailState?.agree : false,
        description: submissionDetailState?.description ? submissionDetailState?.description : ''
      },
      validationSchema: Yup.object({
        title: Yup.string().required().required(translate("Submission_Create_Basic_Details.Article_Title_Required")),
        subTitle: Yup.string(),
        agree: Yup.boolean().oneOf([true], translate("Submission_Create_Basic_Details.Agree_Terms")).required(translate("Submission_Create_Basic_Details.Agree_Terms")),
        description: Yup.string().required().required(translate("Submission_Create_Basic_Details.Abstract_Required"))
      }),
      onSubmit(e) {

        if (validateForm()) {

          if (journalLanguageId !== undefined && journalLanguageId !== null) {
            e['languageId'] = journalLanguageId['id'];
          }
          e['keywords'] = keyValue

          const requestValues = {
            data: e,
            submissionId: parseInt(routeState, 10)
          }
          dispatch(basicJournalUpdate(requestValues))
          if (nextOrDraft === 'next') {
            setTimeout(() => {
              onTabChange('Contribution_tab')
            }, 1500)
          }
        }
      },
    });

    const saveAndDraft = () => {
      validateForm();
      formik.handleSubmit();
      setNextOrDraft('draft');
    };

    const saveAndNext = () => {
      validateForm()
      formik.handleSubmit();
      setNextOrDraft('next');
    };

    useEffect(() => {
      if (BasicError === 'error') {
        validateForm()
      }
    }, [BasicError]);


    return (
      <>
        <div className='basic_datails'>
          {loading || loadingLocal ? <LoaderMain/> : null}
          <div className="col-12">
            <div className="row mt-3">
              <div className="col-6 py-3">
                <label className="form-label">
                  <Translate contentKey="Submission_Create_Basic_Details.Article_Title"></Translate><span
                  className="error_class">*</span><span className="label-info">
                  <Translate contentKey="Submission_Create_Basic_Details.Article_Title_notes">(First letter of each word should be in Upper case)</Translate></span>
                </label>

                <input
                  name="title"
                  id="title"
                  placeholder={translate("Submission_Create_Basic_Details.Enter_Article_Title")}
                  data-cy="title"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('title')}
                />
                {formik.touched.title && formik.errors.title ?
                  <div className="error_class">{formik.errors.title}</div> : null}
              </div>
              <div className="col-6 py-3">
                <label className="form-label "><Translate contentKey="Submission_Create_Basic_Details.Sub_Title">Sub
                  Title</Translate></label>
                <input
                  name="subTitle"
                  id="subTitle"
                  placeholder={translate("Submission_Create_Basic_Details.Enter_Sub_Title")}
                  data-cy="subTitle"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('subTitle')}
                />
                {formik.touched.subTitle && formik.errors.subTitle ?
                  <div className="error_class">{formik.errors.subTitle}</div> : null}
              </div>
              <div className="col-8 py-3" id="keyword-tag">
                <label className="form-label">
                  <Translate contentKey="Submission_Create_Basic_Details.Article_keyword">Add
                    Keywords/tags</Translate>
                  <span className="label-info">
                  <Translate contentKey="Submission_Create_Basic_Details.Article_notes">If any related</Translate>
                </span>
                </label>
                <div className="position-relative">
                  <input
                    type="text"
                    className="form-control"
                    placeholder={translate("Submission_Create_Basic_Details.Enter_Article_keyword")}
                    onChange={handleChange}
                    onKeyDown={handleKeyPress}
                    value={inputValue}
                  />
                  <div className="toggle-plus-icon" onClick={handleAddTag}>
                    {inputValue.length > 0 ? (
                      <div className="toggle-plus-icon" onClick={handleAddTag}>
                        <FontAwesomeIcon style={{fontSize: '13px'}} icon={"plus"}/>
                      </div>
                    ) : null}
                  </div>
                </div>
                <div className="error_class">{error}</div>
                <div className="mt-3">
                  {keyValue?.map((element, index) => (
                    <div className="tag" key={index}>
                      <span key={index}>{element}</span>
                      <button onClick={() => handleDelete(element)}>X</button>
                    </div>
                  ))}
                </div>
              </div>
              <div className="col-4 py-3">
                <label className="form-label">
                  <Translate contentKey="Submission_Create_Basic_Details.Submission_Language">Submission
                    Language</Translate><span
                  className="error_class">*</span><span className="label-info">
                  <Translate contentKey="Submission_Create_Basic_Details.Submission_Language_notes">Choose the Primary Language</Translate></span>
                </label>
                <Select
                  value={journalLanguageId}
                  options={journalSubmissionLangList?.map(({id, langKey, name}) => ({
                    id, value: langKey,
                    label: name,
                  }))}
                  placeholder={translate("Submission_Create_Basic_Details.Select_language")}
                  closeMenuOnSelect={true}
                  onChange={(value) => {
                    delete errors.sub_lang_error;
                    set_journalLanguageId(value);
                  }}
                />
                {errors.sub_lang_error && <span className="error_class">{errors.sub_lang_error}</span>}
              </div>
              <div className="col-12 py-3">
                <label className="form-label">
                  <Translate contentKey="Submission_Create_Basic_Details.Abstract">Abstract</Translate><span
                  className="error_class">*</span>
                </label>
                <CustomTextarea
                  id="description"
                  name="description"
                  placeholder={translate("Submission_Create_Basic_Details.Enter_Abstract")}
                  dataCy="description"
                  field={formik.getFieldProps('description')}
                  form={formik}
                  height={"200px"}
                  disabled={false}
                />
              </div>
              <div className="submission ps-2">
                <input className={`form-check-input ms-0 ${formik.errors.agree ? 'agree_error' : ''}`}
                       type="checkbox" name="agree" checked={formik.values.agree} onChange={formik.handleChange}/>
                <label>
                  <Translate contentKey="Submission_Create_Basic_Details.Article_Agree">I agree to the </Translate><a>
                  <Translate contentKey="Submission_Create_Basic_Details.Article_Terms">Terms of use</Translate></a>
                  <Translate contentKey="Submission_Create_Basic_Details.And">and</Translate>
                  <a><Translate
                    contentKey="Submission_Create_Basic_Details.Article_Policy">Privacy Policy</Translate><span
                    className="error_class">*</span></a>
                </label>
                <br/>
                {formik.touched.agree && formik.errors.agree ?
                  <div className="error_class">{formik.errors.agree}</div> : null
                }
              </div>
              <div className='col-12'>
                <div className="b-bottom w-100 pt-3 mb-3"></div>
              </div>
              <div className="pe-2 m-top">
                <JButton JbuttonValue={translate("buttons.Save_Next")} onclick={saveAndNext} type={"submit"}
                         className={"btn custom-btn-secondary"}/>
              </div>
            </div>
          </div>
          <JButton JbuttonValue={translate("buttons.Save_Draft")} onclick={saveAndDraft} type={"submit"}
                   className={"btn custom-btn-secondary save_draft_btn"}/>
        </div>
      </>
    );

  }
;
export default BasicDetails;
