import React, {useEffect, useState} from 'react';
import '../journal-management-list/journal-management.scss';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import 'react-quill/dist/quill.snow.css';
import {Translate, translate} from 'react-jhipster';
import {useAppDispatch, useAppSelector} from "app/config/store";
import {
  CreateJournalCategory,
  getJournalCategoryList,
  getJournalLangListRequest
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import LoaderMain from "app/shared/Loader/loader-main";
import {journalFileTypeListRequest} from "app/modules/administration/journal-management/create-new-submission/work-flow.reducer";
import CreatableSelect from 'react-select/creatable';
import {handleCreateJournalFileType, handleNewItems, updateFileType} from "app/config/utils";

const animatedComponents = makeAnimated();

interface JournalLangListModel {
  value: string;
  label: string;
}

interface ModalErrors {
  fileFormatsListError?: string;
  fileListError?: string;
  articleGuidelinesError?: string;
  journalLangListError?: string;
}

const CreateJournalStep3 = ({prevStep, handleSubmitRequest, setStepThreeData, stepThreeData}) => {
  const dispatch = useAppDispatch();
  const journalLangListPayload = useAppSelector(state => state.settingsManagement.journalLangList);
  const JournalCategoryList = useAppSelector(state => state.settingsManagement.journalCategoryList);
  const fileTypeList = useAppSelector(state => state.settingsManagement.fileTypeList);
  const [stateCategoryList, setStateCategoryList] = useState<JournalLangListModel[]>([]);
  const [errors, setErrors] = useState<ModalErrors>({});
  const loading = useAppSelector(state => state.settingsManagement.loading);
  const journalLoading = useAppSelector(state => state.journalManagement.loading);
  const [fileType, setFileType] = useState<any>([]);
  const [changedCategory, setChangedCategory] = useState('');
  const [changedFileType, setChangedFileType] = useState('');

  useEffect(() => {
    dispatch(getJournalLangListRequest())
    dispatch(getJournalCategoryList())
    dispatch(journalFileTypeListRequest())
  }, []);

  useEffect(() => {
    const filteredData = JournalCategoryList.filter(item => item.name === changedCategory);
    if (filteredData.length !== 0) {
      stateCategoryList.forEach((item) => {
        if (item['__isNew__']) {
          item['value'] = filteredData[0]['id']
          item['__isNew__'] = false;
        }
      });
    }
  }, [JournalCategoryList])

  useEffect(() => {
    handleNewItems(stateCategoryList, setChangedCategory, dispatch, CreateJournalCategory, getJournalCategoryList);
  }, [stateCategoryList]);

  useEffect(() => {
    setFileType(updateFileType(fileType, fileTypeList, changedFileType));
  }, [fileTypeList]);

  useEffect(() => {
    handleCreateJournalFileType(fileType, setChangedFileType, dispatch);
  }, [fileType])


  const articleGuidelinesChange = (event: any) => {
    setStepThreeData({
      ...stepThreeData,
      guidelines: event.target.value,
    });
  };

  const validateForm = () => {
    const errorsValidate: ModalErrors = {};
    if (stepThreeData.fileTypes?.length === 0) {
      errorsValidate.fileListError = 'Select at least one file type';
    }
    if (stepThreeData?.articleSubmissionLanguages?.length === 0) {
      errorsValidate.journalLangListError = translate('guidelines.Select_language_required');
    }
    if (stepThreeData?.guidelines?.length === 0) {
      errorsValidate.articleGuidelinesError = translate('guidelines.Article_Guidelines_required');
    }
    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (validateForm()) {
      const requestThirdData = {
        submissionLanguages: stepThreeData?.articleSubmissionLanguages?.map(item => item.value).join(','),
        categories: stepThreeData?.categories.map(item => item.value).join(','),
        guidelines: stepThreeData?.guidelines,
        fileTypes: stepThreeData.fileTypes?.map(item => item.value).join(',')
      };
      handleSubmitRequest(requestThirdData);
    }
  }

  return (
    <div>
      <div className="b-bottom">
        <div className="py-2 pb-3">
          <div className="d-flex">
            <div className="line"></div>
            <h6 className="heading mb-0"><Translate contentKey="guidelines.guidelines_title"></Translate></h6>
          </div>
          <p className="title-description"><Translate contentKey='journal_summary.journal_fillMandatory'></Translate>
          </p>
        </div>
      </div>

      <form onSubmit={handleSubmit} id="custom-form-input">
        <div className="row position-relative">
          {loading || journalLoading ? <LoaderMain/> : null}
          <div className="col-6 py-3">
            <label className="form-label">
              <Translate contentKey='guidelines.Article_file_type'></Translate><span className="error_class">*</span>
            </label>
            <CreatableSelect
              options={(fileTypeList || []).map(({id, name}) => ({
                value: id,
                label: name,
              }))}
              onChange={(values) => {
                delete errors.fileListError;
                setFileType(values);
                setStepThreeData({
                  ...stepThreeData,
                  fileTypes: values,
                });
              }}
              value={stepThreeData?.fileTypes}
              closeMenuOnSelect={false}
              components={animatedComponents}
              placeholder={translate("guidelines.Select_File_Type")}
              isMulti
              isClearable={false}
            />
            {errors.fileListError && <span className="error">{errors.fileListError}</span>}
          </div>
          <div className="col-6 py-3">
            <label className="form-label">
              <Translate contentKey="guidelines.Add_Category">Add Categories</Translate>
            </label>
            <CreatableSelect
              value={stepThreeData?.categories}
              options={(JournalCategoryList || []).map(({id, name}) => ({
                value: id,
                label: name,
              }))}
              closeMenuOnSelect={false}
              components={animatedComponents}
              isMulti
              onChange={(value) => {
                setStateCategoryList(value as any[]);
                setStepThreeData({
                  ...stepThreeData,
                  categories: value,
                });
              }}
              placeholder={translate("guidelines.Select_Category")}
            />
          </div>

          <div className="col-6 py-3">
            <label className="form-label">
              <Translate contentKey="guidelines.submission_language">Submission Languages</Translate><span
              className="error_class">*</span>
            </label>
            <Select
              // isDisabled={!administrationPermissions.includes('WRITE_ANY_JOURNAL')}
              value={stepThreeData?.articleSubmissionLanguages}
              options={journalLangListPayload?.map(({langKey, name}) => ({
                value: langKey,
                label: name,
              }))}
              placeholder={translate("guidelines.Select_language")}
              closeMenuOnSelect={false}
              components={animatedComponents}
              isMulti onChange={(value: JournalLangListModel[]) => {
              setStepThreeData({
                ...stepThreeData,
                articleSubmissionLanguages: value,
              });
            }}
            />
            {errors.journalLangListError && <span className="error">{errors.journalLangListError}</span>}
          </div>

          <div className="col-12 pt-3 journal_create_new">
            <label className="form-label">
              <Translate contentKey="guidelines.guidelines">Guidelines</Translate>
              <span className="error_class">*</span> {errors.articleGuidelinesError &&
              <span className="error">{errors.articleGuidelinesError}</span>}
            </label>

            <textarea
              onChange={articleGuidelinesChange}
              name="articaleGuidelines"
              placeholder={translate('guidelines.guidelines_placeholder')}
              className="form-control"
              value={stepThreeData?.guidelines}
            />
          </div>
          <div className="clearfix"></div>
        </div>
        <div className="d-flex b-top pt-4 flex-row-reverse">
          <div className="pe-2 m-top">
            <button className="btn custom-btn" type="submit">
              <Translate contentKey="buttons.submit"></Translate>
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

export default CreateJournalStep3;
