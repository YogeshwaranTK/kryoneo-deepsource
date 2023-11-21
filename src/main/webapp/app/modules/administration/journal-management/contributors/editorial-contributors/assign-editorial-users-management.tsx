import React, {useEffect, useState} from 'react';
import {Modal} from 'reactstrap';
import Select from "react-select";
import {Translate,translate} from "react-jhipster";
import {useAppSelector} from "app/config/store";
import {
  fetchAllUserSuggestions,
  handleAddUserForSelect
} from "app/modules/administration/journal-management/contributors/contributor-utils";
import LoaderMain from "app/shared/Loader/loader-main";
import {useNavigate} from "react-router-dom";
import {getJournalRoles} from "app/modules/administration/journal-management/contributors/contributors.reducer";

export interface ModelErrors {
  roleError?: string;
}

const AssignEditorialUser = (props) => {
  const {handleClose, showModal, fromModel, Jo_id} = props
  const [selectedUsers, setSelectedUsers] = useState<any[]>([]);
  const [inputValue, setInputValue] = useState('');
  const [suggestions, setSuggestions] = useState<User[]>([]);
  const assignEditorialRolePostSuccess = useAppSelector(state => state.journalContributors.assignEditorialRolePostSuccess);
  const loading = useAppSelector(state => state.journalContributors.loading);
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);


  interface User {
    id: string;
    fullName: string;
    email: string;
  }

  const handleCancel = () => {
    setSuggestions([]);
    setInputValue('');
    setSelectedUsers([]);
    handleClose();
  }

  useEffect(() => {
    if (assignEditorialRolePostSuccess) {
      handleCancel()
    }
  }, [assignEditorialRolePostSuccess])

  const [Loading, setLoading] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [preDefinedRoleList, setPreDefinedRoleList] = useState([])
  const [selectedPredefinedRole, setSelectedPredefinedRole] = useState([])
  const [manualRoleInputValue, setManualRoleInputValue] = useState('')
  const [existingRole, setExistingRole] = useState(true)

  const handleManualRoleInputChange = (event) => {
    setManualRoleInputValue(event.target.value);
  };

  useEffect(()=>{
    getJournalRoles().then(response => {
      const data = response.data;
      if (data !== null) {
        const valueData = data.map((item) => ({
          value: item.id,
          label: item.roleName,
        }));
        setPreDefinedRoleList(valueData);
      }
    })
      .catch(errorDetails => {
        console.error(errorDetails);
      });
  },[]);


  // User select flow

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const {value} = event.target;
    setInputValue(value);
    if (value) {
      fetchAllUserSuggestions(value, selectedUsers, setSuggestions, setLoading)
      setSuggestions([]);
    }
  };

  const handleAddUser = (user: User) => {
    handleAddUserForSelect(user, selectedUsers, setSelectedUsers, setInputValue, setErrorMessage, setSuggestions);
  }

  const handleRemoveUser = (userId: string) => {
    setSelectedUsers(prevUsers => prevUsers.filter(user => user.id !== userId));
  };

  const handleSubmit = () => {
    const userIds = selectedUsers.map((user) => {
      return user.id
    })
    const data = {
      roleId: 0,
      userIds,
      existingRole,
      "roleName": manualRoleInputValue,
      submissionEnabled,
      reviewEnabled,
      copyEditingEnabled,
      productionEnabled
    }
    props.handleAssignUserRequest(data)
  }


  const [submissionEnabled, setSubmissionEnabled] = useState(false);
  const [reviewEnabled, setReviewEnabled] = useState(false);
  const [checkboxValues, setCheckboxValues] = useState({});


  const handleSubmissionCheckboxChange = () => {
    if (submissionEnabled) {
      setSubmissionEnabled(false);
    } else {
      setSubmissionEnabled(true);
    }
  };

  const handleReviewCheckboxChange = () => {
    if (reviewEnabled) {
      setReviewEnabled(false);
    } else {
      setReviewEnabled(true);
    }
  };

  const [copyEditingEnabled, setCopyEditingEnabled] = useState(false)
  const handleCopyEditingCheckboxChange = () => {
    if (copyEditingEnabled) {
      setCopyEditingEnabled(false);
    } else {
      setCopyEditingEnabled(true);
    }
  };

  const [productionEnabled, setProductionEnabled] = useState(false)
  const handleProductionCheckboxChange = () => {
    if (productionEnabled) {
      setProductionEnabled(false);
    } else {
      setProductionEnabled(true);
    }
  };

  const handleIndividualCheckboxChange = (event) => {
    const {id, checked} = event.target;
    setCheckboxValues((prevCheckboxValues) => ({
      ...prevCheckboxValues,
      [id]: checked,
    }));
  };

  const navigate = useNavigate()

  return (
    <Modal id="AddContributorModel" isOpen={showModal} toggle={handleClose} backdrop="static" autoFocus={false}>
      {loading ? <LoaderMain/> : null}
      <div className="modal-header">
        <div className="d-flex">
          <div className="line"></div>
          <h5 className="m-0"><Translate contentKey="EDITORIAL_USERS.ADD_EDITORIAL_USERS"></Translate></h5>
        </div>
        <button type="button" className="btn-close" onClick={handleCancel} data-bs-dismiss="modal"
                aria-label="Close"></button>
      </div>
      <div className="modal-body ps-4">

        <div className='row'>
          <div className='col-12 mb-3'>
            <div className="text-end" style={{fontSize: '15px', color: 'blue'}}
            >
              <a onClick={() => {
                const stateData = {
                  id: props.Jo_id,
                  type: props.fromModel,
                  journalName: journals?.title
                };
                navigate('/journal/user-management/create', {state: stateData});
              }}><Translate contentKey="EDITORIAL_USERS.+ Create_User"></Translate></a>
            </div>
            <label className="form-label">
              <Translate contentKey="EDITORIAL_USERS.Add_Users"></Translate><span
              className="error_class">*</span>
            </label>
            <div className="autotext-box">
              <input
                type="text"
                value={inputValue}
                onChange={handleInputChange}
                className="form-control"
                placeholder={translate("EDITORIAL_USERS.Search_User")}
              />
              {Loading ? <div className="spinner-border text-primary spinner"></div> : null}
              {suggestions.length > 0 ? (
                <div className="autotext-result">
                  <ul className="m-0">
                    {suggestions?.map((user, index) => (
                      <li key={index}>
                        <a
                          onClick={() => handleAddUser(user)}
                        >
                          <div> {user.fullName}</div>
                          <div className="suggest-email">{user.email}</div>
                        </a>

                      </li>
                    ))}
                  </ul>
                </div>
              ) : null}
            </div>

            {selectedUsers.length > 0 ? (
              <div className="pt-3">
                {selectedUsers?.map((user, index) => (
                  <div className="tag" key={index}>
                    <span key={index}> {user.fullName}</span>
                    <button type="button"
                            onClick={() => handleRemoveUser(user.id)}
                    >
                      X
                    </button>
                  </div>
                ))}
              </div>
            ) : null}
          </div>

          <div className={` col-12 mb-1 ${!existingRole ? 'd-none' : ''}`}>
            <label className="form-label">
              <Translate contentKey="EDITORIAL_USERS.Select_Role"></Translate><span
              className="error_class">*</span>
            </label>
            <Select placeholder={translate("EDITORIAL_USERS.Select_Role")}
                    options={preDefinedRoleList}
                    onChange={(values: any[]) => {
                      setSelectedPredefinedRole(values)
                    }} value={selectedPredefinedRole}
            />
          </div>
          <div className={` col-12 mb-1 ${existingRole ? 'd-none' : ''}`}>
            <label className="form-label article_custom_label"><Translate contentKey="EDITORIAL_USERS.Create_New_Role"></Translate><span
              className="error_class">*</span></label>
            <input
              name="title"
              id="title"
              placeholder={translate("EDITORIAL_USERS.Enter_New_Role")}
              data-cy="title"
              type="text"
              className="form-control"
              value={manualRoleInputValue}
              onChange={handleManualRoleInputChange}
            />
          </div>

          <div className={`col-12 mb-3 ${!existingRole ? 'd-none' : ''}`} style={{textAlign: 'end'}}>
            <div><a style={{color: "blue", fontSize: "small"}} onClick={() => setExistingRole(false)}>
              <Translate contentKey="EDITORIAL_USERS.+ Create_New_Role"></Translate></a></div>
          </div>

          <div className={`col-12 mb-3 ${existingRole ? 'd-none' : ''}`} style={{textAlign: 'end'}}>
            <div><a style={{color: "blue", fontSize: "small"}} onClick={() => setExistingRole(true)}>
              <Translate contentKey="EDITORIAL_USERS.+ Select_Default_Roles"></Translate></a></div>
          </div>

          <div className="col-12 mb-3">
            <div className={`card`}>
              <div className="card-header" style={{display: 'flex', justifyContent: 'space-between'}}>
                <h6 className="mb-0"><Translate contentKey="EDITORIAL_USERS.Submission"></Translate></h6>
                <div style={{alignSelf: 'flex-end'}}>
                  <input type="checkbox" id="submissionCheckbox" checked={submissionEnabled}
                         onChange={handleSubmissionCheckboxChange} className={'form-check-input'} style={{height: '16px' , width:'16px'}}
                         disabled={existingRole}/>
                </div>
              </div>

              <div className="card-body">
                <div className="mx-n3">
                  <div style={{maxHeight: '220px', overflowY: 'auto'}} className="px-3">
                    <div className="row">
                      <div className="col-12  d-flex">
                        <div className="form-check form-check-secondary mb-3">

                          <input className="form-check-input" type="checkbox" id="formCheck1"
                                 checked={submissionEnabled}
                                 onChange={handleIndividualCheckboxChange}
                                 disabled={existingRole}/>

                          <label className="form-check-label ms-1" htmlFor="formCheck1">
                            Checkbox 1
                          </label>
                        </div>
                      </div>
                      <div className="col-12  d-flex">
                        <div className="form-check form-check-secondary mb-3">
                          <input className="form-check-input" type="checkbox" id="formCheck2"
                                 checked={submissionEnabled}
                                 onChange={handleIndividualCheckboxChange}
                                 disabled={existingRole}/>
                          <label className="form-check-label ms-1" htmlFor="formCheck2">
                            Checkbox 2
                          </label>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div className="col-12 mb-3">
            <div className={`card`}>
              <div className="card-header" style={{display: 'flex', justifyContent: 'space-between'}}>
                <h6 className="mb-0"><Translate contentKey="EDITORIAL_USERS.Review"></Translate></h6>
                <div style={{alignSelf: 'flex-end'}}>
                  <input type="checkbox" id="submissionCheckbox" checked={reviewEnabled}  className={'form-check-input'} style={{height: '16px' , width:'16px'}}
                         onChange={handleReviewCheckboxChange}
                         disabled={existingRole}/>
                </div>
              </div>

              <div className="card-body">
                <div className="mx-n3">
                  <div style={{maxHeight: '220px', overflowY: 'auto'}} className="px-3">
                    <div className="row">
                      <div className="col-12  d-flex">
                        <div className="form-check form-check-secondary mb-3">

                          <input className="form-check-input" type="checkbox" id="formCheck1" checked={reviewEnabled}
                                 onChange={handleIndividualCheckboxChange}
                                 disabled={existingRole}/>

                          <label className="form-check-label ms-1" htmlFor="formCheck1">
                            Checkbox 1
                          </label>
                        </div>
                      </div>
                      <div className="col-12  d-flex">
                        <div className="form-check form-check-secondary mb-3">
                          <input className="form-check-input" type="checkbox" id="formCheck2" checked={reviewEnabled}
                                 onChange={handleIndividualCheckboxChange}
                                 disabled={existingRole}/>
                          <label className="form-check-label ms-1" htmlFor="formCheck2">
                            Checkbox 2
                          </label>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div className="col-12 mb-3">
            <div className={`card`}>
              <div className="card-header" style={{display: 'flex', justifyContent: 'space-between'}}>
                <h6 className="mb-0"><Translate contentKey="EDITORIAL_USERS.Copy_Editing"></Translate></h6>
                <div style={{alignSelf: 'flex-end'}}>
                  <input type="checkbox" id="submissionCheckbox" checked={copyEditingEnabled} className={'form-check-input'} style={{height: '16px' , width:'16px'}}
                         onChange={handleCopyEditingCheckboxChange}
                         disabled={existingRole}/>
                </div>
              </div>

              <div className="card-body">
                <div className="mx-n3">
                  <div style={{maxHeight: '220px', overflowY: 'auto'}} className="px-3">
                    <div className="row">
                      <div className="col-12  d-flex">
                        <div className="form-check  mb-3">

                          <input className="form-check-input" type="checkbox" id="formCheck1"
                                 checked={copyEditingEnabled}
                                 onChange={handleIndividualCheckboxChange}
                                 disabled={existingRole}/>

                          <label className="form-check-label ms-1" htmlFor="formCheck1">
                            Checkbox 1
                          </label>
                        </div>
                      </div>
                      <div className="col-12  d-flex">
                        <div className="form-check form-check-secondary mb-3">
                          <input className="form-check-input" type="checkbox" id="formCheck2"
                                 checked={copyEditingEnabled}
                                 onChange={handleIndividualCheckboxChange}
                                 disabled={existingRole}/>
                          <label className="form-check-label ms-1" htmlFor="formCheck2">
                            Checkbox 2
                          </label>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div className="col-12 mb-3">
            <div className={`card`}>
              <div className="card-header" style={{display: 'flex', justifyContent: 'space-between'}}>
                <h6 className="mb-0"><Translate contentKey="EDITORIAL_USERS.Production"></Translate></h6>
                <div style={{alignSelf: 'flex-end'}}>
                  <input type="checkbox" id="submissionCheckbox" checked={productionEnabled}
                         onChange={handleProductionCheckboxChange}  className={'form-check-input'} style={{height: '16px' , width:'16px'}}
                         disabled={existingRole}/>
                </div>
              </div>

              <div className="card-body">
                <div className="mx-n3">
                  <div style={{maxHeight: '220px', overflowY: 'auto'}} className="px-3">
                    <div className="row">
                      <div className="col-12  d-flex">
                        <div className="form-check form-check-secondary mb-3">

                          <input className="form-check-input" type="checkbox" id="formCheck1"
                                 checked={productionEnabled}
                                 onChange={handleIndividualCheckboxChange}
                                 disabled={existingRole}/>

                          <label className="form-check-label ms-1" htmlFor="formCheck1">
                            Checkbox 1
                          </label>
                        </div>
                      </div>
                      <div className="col-12  d-flex">
                        <div className="form-check form-check-secondary mb-3">
                          <input className="form-check-input" type="checkbox" id="formCheck2"
                                 checked={productionEnabled}
                                 onChange={handleIndividualCheckboxChange}
                                 disabled={existingRole}/>
                          <label className="form-check-label ms-1" htmlFor="formCheck2">
                            Checkbox 2
                          </label>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </div>
        <div className="modal-footer float-end pt-0">
          <button className="btn btn--cancel" type="submit" onClick={handleSubmit}>
            {/*{seteditId !== 0 ? translate("buttons.update")*/}
            {/*  : translate("buttons.submit")}*/}
            <Translate contentKey="buttons.submit"/>
          </button>
          <button className="btn btn--cancel" type="button" onClick={handleCancel}>
            <Translate contentKey="buttons.cancel"/>
          </button>
        </div>

      </div>
    </Modal>
  );
};

export default AssignEditorialUser;
