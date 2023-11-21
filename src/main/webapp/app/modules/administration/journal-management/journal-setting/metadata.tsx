import React, {useEffect, useState} from 'react';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import 'react-quill/dist/quill.snow.css';
import {Translate} from 'react-jhipster';
import JournalTitle, {JournalTitleModel} from "app/modules/administration/journal-management/journal-management-list/JournalTitle";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {
  CreateJournalCategory,
  getJournalCategoryList,
  getJournalLangListRequest,
  getJournalSubmissionLangListRequest,
  metadataUpdate
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import LoaderMain from "app/shared/Loader/loader-main";
import {
  journalFileTypeListRequest
} from "app/modules/administration/journal-management/create-new-submission/work-flow.reducer";
import CreatableSelect from 'react-select/creatable';
import {handleCreateJournalFileType, updateFileType} from "app/config/utils";

const animatedComponents = makeAnimated();

interface JournalLanguageListModel {
  value: string;
  label: string;
}

interface ModalErrors {
  fileFormatsListError?: string;
  fileListError?: string;
  articleGuidelinesError?: string;
  journalLangListError?: string;
}

export const MetaData: React.FC = () => {
  const dispatch = useAppDispatch();
  const fileTypeList = useAppSelector(state => state.settingsManagement.fileTypeList);
  const JournalLangList = useAppSelector(state => state.settingsManagement.journalLangList);
  const CategoryListDataList = useAppSelector(state => state.settingsManagement.journalCategoryList)
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const [stateFileTypeList, setStateFileTypeList] = useState<any[]>([]);
  const [stateJournalLangList, setStateJournalLangList] = useState<any[]>([]);
  const [stateCategoryList, setStateCategoryList] = useState<any[]>([]);
  const [fileType, setFileType] = useState<any[]>([]);
  const [errors, setErrors] = useState<ModalErrors>({});
  const [loadingLocal, setLoadingLocal] = useState(false);
  const [changedFileType, setChangedFileType] = useState('');
  const [changedCategory, setChangedCategory] = useState('');

  useEffect(() => {
    getJournalGetRes()
    dispatch(getJournalLangListRequest())
    dispatch(getJournalCategoryList())
    dispatch(journalFileTypeListRequest())
  }, []);

  useEffect(() => {
    setFileType(updateFileType(fileType, fileTypeList, changedFileType));
  }, [fileTypeList]);

  useEffect(() => {
    handleCreateJournalFileType(fileType, setChangedFileType, dispatch);
  }, [fileType])


  useEffect(() => {
    const filteredData = CategoryListDataList.filter(item => item.name === changedCategory);
    if (filteredData.length !== 0) {
      stateCategoryList.forEach((item) => {
        if (item['__isNew__']) {
          item['value'] = filteredData[0]['id']
          item['__isNew__'] = false;
        }
      });
    }
  }, [CategoryListDataList])

  useEffect(() => {
    const isNewItems = stateCategoryList?.filter(item => item['__isNew__'] === true);
    if (isNewItems !== undefined && isNewItems?.length !== 0) {
      const headers = isNewItems[0]['value']
      setChangedCategory(isNewItems[0]['value'])
      dispatch(CreateJournalCategory(headers))
        .then((resultAction) => {
          if (CreateJournalCategory.fulfilled.match(resultAction)) {
            if (resultAction.payload.status === 200) {
              dispatch(getJournalCategoryList())
            }
          }
        })
    }
  }, [stateCategoryList]);

  const getJournalSubmission = () => {
    getJournalSubmissionLangListRequest()
      .then(response => {
        setStateJournalLangList(response.data.map(({langKey, name}) => ({
          value: langKey,
          label: name,
        })))
      })
      .catch(error => {
        console.error(error);
      });
  };

  const getJournalGetRes = () => {
    setLoadingLocal(true)
    setStateCategoryList(journals.categories?.map(({id, name}) => ({
      value: id,
      label: name,
    })))
    setStateJournalLangList(journals.languages?.map((language) => ({
      value: language.langKey,
      label: language.name,
    })));
    setStateFileTypeList(journals.fileTypes?.map(({id, name}) => ({
      value: id,
      label: name,
    })))

    setLoadingLocal(false)
  };

  const [breadTitleValueProps] = useState<JournalTitleModel>({
    title: "Metadata",
    sub_title: "Metadata details"
  });

  const validateForm = () => {
    const errors_validate: ModalErrors = {};

    if (fileType?.length === 0) {
      errors_validate.fileListError = 'Select at least one file type';
    }
    if (stateJournalLangList?.length === 0) {
      errors_validate.journalLangListError = 'Select at least one language';
    }
    setErrors(errors_validate);
    return Object.keys(errors_validate).length === 0;
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (validateForm()) {
      dispatch(metadataUpdate({
        submissionLanguages: stateJournalLangList?.map(item => item.value),
        categories: stateCategoryList?.map(item => item.value),
        fileTypes: fileType?.map(item => item.value),
      }))
    }
  }
  const resetFunction = () => {
    getJournalSubmission()
    getJournalGetRes()
  }


  return (
    <>
      <div className="row height-100">
        <div className="col-12 pt-3 px-4">
          <JournalTitle value={breadTitleValueProps}/>
          <form onSubmit={handleSubmit}>
            <div className="row position-relative">
              {loadingLocal ? <LoaderMain/> : null}
              <div className="col-6 py-3">
                <label className="form-label">
                  Submission File Type<span className="error_class">*</span>
                </label>

                <CreatableSelect
                  // isDisabled={!administrationPermissions.includes('WRITE_ANY_JOURNAL')}
                  options={(fileTypeList).map(({id, name}) => ({
                    value: id,
                    label: name,
                  }))}
                  onChange={(values) => {
                    delete errors.fileListError;
                    setFileType(values);
                  }}
                  value={stateFileTypeList}
                  closeMenuOnSelect={false}
                  components={animatedComponents}
                  placeholder='Select or Create file type'
                  defaultValue={fileType}
                  isMulti
                  isClearable={false}
                />
                {errors.fileListError && <span className="error">{errors.fileListError}</span>}
              </div>


              {/*<div className="col-6 py-3">*/}
              {/*  <label className="form-label">*/}
              {/*    <Translate contentKey="guidelines.submission_format">Submission File Format</Translate><span*/}
              {/*    className="error_class">*</span>*/}
              {/*  </label>*/}
              {/*  <Select*/}
              {/*    // isDisabled={!administrationPermissions.includes('WRITE_ANY_JOURNAL')}*/}
              {/*    onChange={(values: FileFormatListModel[]) => {*/}
              {/*      setStateFileFormatList(values)*/}
              {/*    }}*/}
              {/*    options={fileFormatList?.map(({id, name}) => ({*/}
              {/*      value: id,*/}
              {/*      label: name,*/}
              {/*    }))}*/}
              {/*    placeholder='Select file format'*/}
              {/*    closeMenuOnSelect={false}*/}
              {/*    value={stateFileFormatList}*/}
              {/*    components={animatedComponents}*/}
              {/*    isMulti*/}
              {/*    defaultValue={stateFileFormatList}*/}
              {/*  />*/}
              {/*  {errors.state_fileFormatList && <span className="error">{errors.state_fileFormatList}</span>}*/}
              {/*</div>*/}


              <div className="col-6 py-3">
                <label className="form-label">
                  Add category
                </label>
                <CreatableSelect
                  options={CategoryListDataList?.map(({id, name}) => ({
                    value: id,
                    label: name,
                  }))}
                  isMulti
                  onChange={(value: any[]) => {
                    setStateCategoryList(value);
                  }}
                  value={stateCategoryList}
                  placeholder='Select or Create category'/>
              </div>

              <div className="col-4 py-3">
                <label className="form-label">
                  <Translate contentKey="guidelines.submission_language">Article Submission Language</Translate><span
                  className="error_class">*</span>
                </label>
                <Select
                  // isDisabled={!administrationPermissions.includes('WRITE_ANY_JOURNAL')}
                  value={stateJournalLangList}
                  options={JournalLangList?.map(({langKey, name}) => ({
                    value: langKey,
                    label: name,
                  }))}
                  closeMenuOnSelect={false}
                  components={animatedComponents}
                  isMulti
                  onChange={(value: JournalLanguageListModel[]) => {
                    setStateJournalLangList(value);
                  }}
                  placeholder='Select language'
                />
                {errors.journalLangListError && <span className="error">{errors.journalLangListError}</span>}
              </div>
              <div className="clearfix"></div>
              <div className="d-flex b-top pt-4 mt-4 justify-content-end">

                <div className="pe-2 m-top">
                  <button className="btn custom-btn-secondary" type="button" onClick={resetFunction}
                    // disabled={!administrationPermissions.includes('WRITE_ANY_JOURNAL')}
                  >
                    <Translate contentKey="reset">RESET</Translate>
                  </button>
                </div>
                <div className="pe-2 m-top">
                  <button className="btn custom-btn" type="submit"
                    // disabled={!administrationPermissions.includes('WRITE_ANY_JOURNAL')}
                  >
                    SAVE & UPDATE
                  </button>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default MetaData;
