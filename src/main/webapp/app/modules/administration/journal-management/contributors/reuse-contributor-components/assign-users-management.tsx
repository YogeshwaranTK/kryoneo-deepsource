import React, {useEffect, useState} from 'react';
import {Modal} from 'reactstrap';
import {useAppSelector} from 'app/config/store';
import {
  fetchAllUserSuggestions,
  handleAddUserForSelect
} from "app/modules/administration/journal-management/contributors/contributor-utils";
import LoaderMain from "app/shared/Loader/loader-main";
import {useNavigate} from "react-router-dom";
import {Translate,translate} from "react-jhipster";


const AssignUser = (props) => {
  const assignAuthorRolePostSuccess = useAppSelector(state => state.journalContributors.assignAuthorRolePostSuccess);
  const assignReviewerRolePostSuccess = useAppSelector(state => state.journalContributors.assignReviewerRolePostSuccess);
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const loading = useAppSelector(state => state.journalContributors.loading);
  const {handleClose, handleAssignUserRequest} = props;
  const navigate = useNavigate();


  const handleCancel = () => {
    handleClose();
    setSuggestions([]);
    setInputValue('');
    setSelectedUsers([]);
  };

  interface User {
    id: string;
    fullName: string;
    email: string;
  }

  const [selectedUsers, setSelectedUsers] = useState<any[]>([]);
  const [inputValue, setInputValue] = useState('');
  const [suggestions, setSuggestions] = useState<User[]>([]);
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [Loading, setLoading] = useState<boolean>(false);


  useEffect(() => {

    if (assignAuthorRolePostSuccess || assignReviewerRolePostSuccess) {
      setSuggestions([]);
      setInputValue('');
      setSelectedUsers([]);
      handleClose();
    }
  }, [assignAuthorRolePostSuccess, assignReviewerRolePostSuccess]);

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const {value} = event.target;
    setInputValue(value);
    if (value) {
      fetchAllUserSuggestions(value, selectedUsers, setSuggestions, setLoading)
      setSuggestions([]);
    }
  };

  const handleRemoveUser = (userId: string) => {
    setSelectedUsers(prevUsers => prevUsers.filter(user => user.id !== userId));
  };

  const handleAddUser = (user: User) => {
    handleAddUserForSelect(user, selectedUsers, setSelectedUsers, setInputValue, setErrorMessage, setSuggestions);
  }


  const handleSubmit = () => {
    const usersId = selectedUsers.map((user) => {
      return user.id
    })
    const data = {
      "userIds": usersId
    }
    handleAssignUserRequest(data)
  }

  return (
    <>
      <Modal id="GroupUserCreate" isOpen={props.showModal} toggle={handleClose} backdrop="static" autoFocus={false}>
        {loading ? <LoaderMain/> : null}
        <div className="modal-header">
          <div className="d-flex">
            <div className="line"></div>
            <h5 className="m-0"><Translate contentKey="Add_User_Details.Add_User"></Translate> </h5>
          </div>
          <button type="button" className="btn-close" onClick={handleCancel}></button>
        </div>

        <div className="modal-body position-relative">
          <label className="form-label w-100">
            <Translate contentKey="Add_User_Details.Add_User"></Translate><span className="error_class">*</span> <small
            className="text-danger float-end">{errorMessage}</small>
          </label>
          <div className="autotext-box">
            <input
              type="text"
              value={inputValue}
              onChange={handleInputChange}
              className="form-control"
              placeholder={translate("Add_User_Details.Search_User")}
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
              ))}{' '}
            </div>
          ) : null}

          <div className="modal-footer pt-4">
            <button
              className="btn btn--primary"
              type="button"
              onClick={handleSubmit}
              disabled={selectedUsers.length === 0}
            >
              <Translate contentKey="Add_User_Details.SUBMIT"></Translate>
            </button>
            <button className="btn btn--cancel" type="button" onClick={handleCancel}>
              <Translate contentKey="Add_User_Details.CANCEL"></Translate>
            </button>

            <div className="ml-auto pt-1" style={{fontSize: '15px', color: 'blue', marginLeft: '176px'}}
                 onClick={() => {
                   const stateData = {
                     id: props.Jo_id,
                     type: props.fromModel,
                     journalName: journals?.title
                   };
                   navigate('/journal/user-management/create', {state: stateData});
                 }}>
              <a><Translate contentKey="Add_User_Details.+ Create_User"></Translate></a>
            </div>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default AssignUser;


