import React, {useEffect, useState} from 'react';
import {Modal} from 'reactstrap';
import {useAppDispatch, useAppSelector} from "app/config/store";
import {
  getMailActionDetails, postMailActionDetails
} from "app/modules/administration/journal-management/journal-developer-settings/journal-developer.reducer";
import RichText from "app/shared/rich-text/rich-text";
import JButton from "app/shared/component/button/button";
import Select from "react-select";
import makeAnimated from "react-select/animated";

const EditEmailConfigurationModel = ({mailActionId, showModal, handleClose, mailActionDetails}) => {
  const [errors, setErrors] = useState<{ contentError?: string; mailToError?: string; }>({});
  const dispatch = useAppDispatch();
  const mailActionDetailsPayload = useAppSelector(state => state.journalDeveloperSettings.mailActionDetailsPayload);
  const postMailActionDetailsSuccess = useAppSelector(state => state.journalDeveloperSettings.postMailActionDetailsSuccess);
  const [mailTemplate, setMailTemplate] = useState('')
  const [subject, setSubject] = useState('')
  const [mailToList, setMailToList] = useState([])
  const [mailTo, setMailTo] = useState([])
  const animatedComponents = makeAnimated();

  const handleCancel = () => {
    handleClose();
    setMailTemplate('')
  }

  useEffect(() => {
    if (postMailActionDetailsSuccess) {
      handleCancel()
    }
  }, [postMailActionDetailsSuccess])

  const validateForm = () => {
    const errorsValidate: { contentError?: string; mailToError?: string; } = {};
    if (mailTemplate === '') {
      errorsValidate.contentError = 'Enter Content Below';
    }
    if (mailTo.length === 0) {
      errorsValidate.mailToError = 'Select User';
    }
    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };


  useEffect(() => {
    if (showModal) {
      dispatch(getMailActionDetails(mailActionId))
    }
  }, [showModal])


  const mailTemplateChange = (value: string) => {
    setMailTemplate(value);
  };

  useEffect(() => {
    if (mailActionDetailsPayload !== undefined) {
      setMailTemplate(mailActionDetailsPayload?.html);
      const mailActionDetailsObject = mailActionDetails?.find(item => item.id === mailActionId);
      setMailToList(mailActionDetailsObject?.mailToVariables);
      const outputArray = mailActionDetailsPayload?.mailToVariables?.map(value => ({value, label: value}));
      setMailTo(outputArray)
    }
  }, [mailActionDetailsPayload])

  const handleSubmit = () => {
    if (validateForm()) {
      const valuesArray = mailTo.map(item => item.value);
      const data =
        {
          subject,
          actionId: mailActionId,
          html: mailTemplate,
          active: true,
          mailToVariables: valuesArray
        }
      dispatch(postMailActionDetails(data))
    }
  }


  const handleSubjectChange = (event) => {
    const value = event.target.value;
    setSubject(value);
  };

  return (
    <Modal id='AddContributorModel' style={{maxWidth: '1300px'}} className="neee" isOpen={showModal}
           toggle={handleClose}
           backdrop="static" autoFocus={false}>

      <div className="modal-header">
        <div className="d-flex">
          <div className="line"></div>
          <h5 className="m-0">Email Configuration</h5>
        </div>
        <button type="button" className="btn-close" onClick={handleCancel} data-bs-dismiss="modal"
                aria-label="Close"></button>
      </div>
      <div className="d-flex mt-3 Submittd_list" style={{border: 'none !important', height: '600px'}}>
        <div className="col-3 px-3 position-relative"
             style={{borderRight: '1px solid #d9d9d9', maxHeight: '550px', overflowY: 'auto'}}>
          <ul className="list-group mt-3">
            <li className="list-group-item disabled" aria-disabled="true"
                style={{backgroundColor: 'lightgrey', textAlign: 'center'}}>
              <div className="d-flex align-items-center">
                <div className="flex-grow-1" style={{
                  color: '#323232',
                  fontSize: '16px',
                  fontWeight: 700
                }}>
                  Keywords
                </div>
              </div>
            </li>
            {mailActionDetailsPayload?.mailActionVariables?.length !== 0 ? mailActionDetailsPayload?.mailActionVariables?.map((keyword, index) => (
                <li key={index} className="list-group-item" aria-disabled="true">
                  <div className="d-flex align-items-center">
                    <div className="flex-grow-1" style={{marginLeft: '8px'}}>
                      <span className="article-details m-1 me-3">{keyword}</span>
                    </div>
                    <img alt='copy-clipboard' onClick={async () => {
                      if ("clipboard" in navigator) {
                        await navigator.clipboard.writeText(keyword);
                      } else {
                        document.execCommand("copy", true, keyword);
                      }
                    }} src="content/images/icons/copy-regular.svg"
                         style={{width: '15px', cursor: 'pointer', fontSize: '12px'}}/>
                    {/*<img src="content/images/icons/copy-solid.svg" style={{width:'15px'}}/>*/}
                  </div>
                </li>
              )) :
              <li className="list-group-item" aria-disabled="true">
                <div className="d-flex align-items-center">
                  <div className="flex-grow-1" style={{marginLeft: '34px'}}>
                    <span className="article-details m-1 me-3">No Keyword Found</span>
                  </div>
                </div>
              </li>
            }
          </ul>
        </div>
        <div className="col-9 px-3 position-relative">

          <div className="col-12 py-2">
            <label className="form-label">
              Mail To<span
              className="error_class">*</span>
            </label>{errors.mailToError &&
            <span className="error"> - {errors.mailToError}</span>}
            <Select
              value={mailTo}
              options={mailToList?.map((list) => ({
                value: list,
                label: list,
              }))}
              placeholder='Select Mail List'
              closeMenuOnSelect={false}
              components={animatedComponents}
              isMulti onChange={(value) => {
              setMailTo(value)
            }}
            />
          </div>

          <div className="col-12 py-2">
            <label className="form-label ">Subject<span
              className="error_class">*</span></label>
            <input
              name="subject"
              id="subject"
              placeholder='Enter Subject'
              data-cy="subject"
              type="text"
              className="form-control"
              value={subject}
              onChange={handleSubjectChange}
            />
          </div>

          <div className="col-12 py-2">
            <label className="form-label mt-1">
              Mail Template<span className="error_class">*</span>{errors.contentError &&
              <span className="error"> - {errors.contentError}</span>}
            </label>
            <RichText
              value={mailTemplate}
              placeHolderText="Create Mail Template"
              onValueChange={mailTemplateChange}
              height='320px'
            />
          </div>
          <div className="pe-2 m-top text-end">
            <JButton JbuttonValue="CANCEL"
                     onclick={handleCancel}
                     type={"button"}
                     className={"btn custom-btn-secondary  me-2"}/>
            <JButton JbuttonValue="SAVE"
                     onclick={handleSubmit}
                     type={"button"}
                     className={"btn custom-btn"}/>
          </div>
        </div>
      </div>
    </Modal>
  );
};

export default EditEmailConfigurationModel;
