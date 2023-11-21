import React, {useEffect, useState} from 'react';
import 'react-quill/dist/quill.snow.css';
import {Translate, translate} from 'react-jhipster';
import JournalTitle, {JournalTitleModel} from "app/modules/administration/journal-management/journal-management-list/JournalTitle";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {
  GuidLineSubmit
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import LoaderMain from "app/shared/Loader/loader-main";
import CustomTextareaRaw from "app/shared/component/Input-fields/customTextAreaRaw";

export const AuthorGuidelines: React.FC = () => {
  const dispatch = useAppDispatch();
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const [articleGuidelines, setArticleGuidelines] = useState('');
  const [articleCopyRights, setArticleCopyRights] = useState('');
  const [errors, setErrors] = useState<{
    guidelinesError?: string;
    copyRightsNoticeError?: string;
  }>({});
  const [loadingLocal, setLoadingLocal] = useState(false);
  // const account = useAppSelector(state => state.authentication.account);
  // const administrationPermissions = account?.administrationPermissions;


  useEffect(() => {
    getJournalGetRes()
  }, []);


  const getJournalGetRes = () => {
    setLoadingLocal(true)
    if (journals?.guidelines !== null) {
      setArticleGuidelines(journals?.guidelines)
      setArticleCopyRights(journals?.copyrightNotice)
    }
    setLoadingLocal(false)
  };
  const authorGuidelinesChange = (value: string) => {
    setArticleGuidelines(value);
  };
  const authorCopyRightsChange = (value: string) => {
    setArticleCopyRights(value);
  };

  const [titleValueProps] = useState<JournalTitleModel>({
    title: translate('guidelines.guidelines_title'),
    sub_title: translate('guidelines.guidelines_subtitle'),
  });

  const validateForm = () => {
    const errorsValidate: {
      guidelinesError?: string;
      copyRightsNoticeError?: string;
    } = {};

    if (articleGuidelines?.length === 0) {
      errorsValidate.guidelinesError = 'Guidelines is required.';
    }

    if (articleCopyRights?.length === 0) {
      errorsValidate.copyRightsNoticeError = 'Copy Rights Notice is required.';
    }

    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };


  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (validateForm()) {
      dispatch(GuidLineSubmit({
        guidelines: articleGuidelines,
        copyrightNotice: articleCopyRights,
      }))
    }
  }
  const resetFunction = () => {
    getJournalGetRes()
  }

  return (
    <>
      <div className="row height-100">
        <div className="col-12 pt-3 px-4">
          <JournalTitle value={titleValueProps}/>
          <form onSubmit={handleSubmit}>
            <div className="row position-relative">
              {loadingLocal ? <LoaderMain/> : null}

              <div className="col-12 pt-3">
                <label className="form-label">
                  <Translate contentKey="guidelines.guidelines">Article Guidelines</Translate><span
                  className="error_class">*</span>
                </label>
                <CustomTextareaRaw
                  id="guidelines"
                  name="guidelines"
                  placeholder={translate('guidelines.guidelines_placeholder')}
                  dataCy="guidelines"
                  value={articleGuidelines}
                  height={"200px"}
                  disabled={false}
                  onChange={authorGuidelinesChange}
                />

                {errors.guidelinesError && <span className="error">{errors.guidelinesError}</span>}
              </div>

              <div className="col-12 pt-3">
                <label className="form-label">
                  Copyright Notice<span
                  className="error_class">*</span>
                </label>
                <CustomTextareaRaw
                  id="copyrightsNotice"
                  name="copyrightsNotice"
                  placeholder="Enter Copyright Notice"
                  dataCy="copyrightsNotice"
                  value={articleCopyRights}
                  height={"200px"}
                  disabled={false}
                  onChange={authorCopyRightsChange}
                />
                {errors.copyRightsNoticeError && <span className="error">{errors.copyRightsNoticeError}</span>}
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

export default AuthorGuidelines;
