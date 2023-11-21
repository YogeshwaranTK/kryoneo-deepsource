import React, {useEffect, useState} from "react";
import {Modal} from "reactstrap";
import RichText from "app/shared/rich-text/rich-text";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import {
  getAuthorRoleListRaw,
  getEditorialRoleListRaw,
  getJournalsUserSearch,
} from "app/modules/administration/journal-management/contributors/contributors.reducer";
import {
  postAssignUserProduction
} from 'app/modules/administration/journal-management/production/production.reducer';
import {
  stateFalse
} from "app/modules/administration/journal-management/production/production.reducer";
import Select from "react-select";

interface ErrorState {
  description_error?: string;
  userListError?: string;
  ReviewerError?: string;
}
const customStyles = {
  control: (provided) => ({
    ...provided,
    fontSize: '14px',
    borderColor: '#9198B0',
    Color: '#9198B0'
  }),
};

const AssignProduction = (props) => {
  const dispatch = useAppDispatch();
  const {showModal, handleClose,setParticipantList,participantList,submissionId} = props;
  const assignUserSuccess = useAppSelector(state => state.production.assignUserSuccess);
  const postAssignUserSuccessPayload = useAppSelector(state => state.production.postAssignUserSuccessPayload);
  const loading = useAppSelector(state => state.production.loading);
  const [selectedUsers, setSelectedUsers] = useState<any[]>([]);
  const [inputValue, setInputValue] = useState('');
  const [suggestions, setSuggestions] = useState<User[]>([]);
  const [Loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<ErrorState>({});
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [reviewDescription, setReviewDescription] = useState("");
  const [showSuggestions, setShowSuggestions] = useState(false)
  const [selectReviewer, setSelectedReviewer] = useState(null);
  const [showInput, setShowInput] = useState(false);


  useEffect(() => {
    if (assignUserSuccess && postAssignUserSuccessPayload.length !== 0) {
      setParticipantList([...participantList, ...postAssignUserSuccessPayload])
      handleCancel()
      dispatch(stateFalse())
    }
  }, [postAssignUserSuccessPayload, assignUserSuccess])

  const handleCancel = () => {
    setSelectedUsers([])
    setReviewDescription("")
    handleClose()
  }

  const reviewDescriptionChange = (value: string) => {
    setReviewDescription(value);
  };

  const validateForm = () => {
    const errorsValidate: ErrorState = {};
    if (selectedUsers.length === 0) {
      errorsValidate.userListError = 'Select at least one user';
    }
    if (reviewDescription.length === 0) {
      errorsValidate.description_error = 'Required Description';
    }
    if (selectReviewer === null ) {
      errorsValidate.ReviewerError = 'Required Reviewer Type';
    }
    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };


  const HandleSubmit = () => {
    const usersId = selectedUsers.map((user) => {
      return user.userId
    })
    const data = {
      "userIds": usersId,
      "description": reviewDescription,
      submissionId
    }
    if (validateForm()) {
      dispatch(postAssignUserProduction(data));
      dispatch(stateFalse())
    }
  }

  interface User {
    id: number;
    userEmail: string;
    userName: string;
    userId: string;
    userFullName: string;
    email: string;
    rootRoleType: string;
    fullName: string;

  }


  const fetchSuggestions = async (value: string) => {
    setLoading(true);

    try {
      let response;
      if (selectReviewer.value === "EDITORIAL") {
        response = await getEditorialRoleListRaw({
          page: 0,
          size: 30,
          searchText: value,
          journalId: submissionId
        });
      } else if (selectReviewer.value === "AUTHOR") {
        response = await getAuthorRoleListRaw({
          page: 0,
          size: 30,
          searchText: value,
          journalId: submissionId
        });
      }

      const filteredSuggestions = response.data.filter((user: User) =>
        !selectedUsers.find(selectedUser => selectedUser.userId === user.userId)
      );

      setLoading(false);
      setShowSuggestions(true);
      setSuggestions(filteredSuggestions);
    } catch (error) {
      console.error("An error occurred:", error);
      setLoading(false);
    }
  };

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = event.target;
    setInputValue(value);
    if (value.trim() !== '') {
      fetchSuggestions(value);
    } else {
      setSuggestions([]);
      setShowSuggestions(false);
    }
  };


  const handleRemoveUser = (userId: string) => {
    setSelectedUsers(prevUsers => prevUsers.filter(user => user.userId !== userId));
  };


  const handleAddUser = (user: User) => {
    const existingUser = selectedUsers.find(element => element.userId === user.userId);
    if (existingUser) {
      setErrorMessage('User already added');
    } else {
      const newUser = {
        userId: user.userId,
        fullName: user.fullName
      };

      setSelectedUsers(prevUsers => [...prevUsers, newUser]);
      setInputValue(''); // Clear the input value here
      setErrorMessage('');
      setSuggestions([]);
      setShowSuggestions(false)
    }
  };


  useEffect(()=>{
    setShowSuggestions(inputValue !== '')
  },[inputValue])

  const options = [
    // { value: 'REVIEWERS', label: 'Reviewers' },
    { value: 'EDITORIAL', label: 'Editorial' },
    { value: 'AUTHOR', label: 'Author' },
  ]

  const handleSelectChange = (selectedOption) => {
    setSelectedReviewer(selectedOption);
    if (selectedOption) {
      setShowInput(true);
    } else {
      setShowInput(false);
      setInputValue("");
    }
  };

  return (
    <>
      <Modal
        id="RequestSubmission"
        className="request-submission"
        isOpen={showModal}
        toggle={handleClose}
        backdrop="static"
        autoFocus={false}>
        <div className="modal-content">
          <div className="modal-header">
            <div className="d-flex">
              <div className="line"></div>
              <div className="d-flex">
                <h5 className="m-0">ASSIGN PARTICIPANTS</h5>
                <span className="request-head">- Request Participants</span>
              </div>
            </div>
            <button
              type="button"
              className="btn-close"
              onClick={handleCancel}
            ></button>
          </div>
          <div className="modal-body ps-4 position-relative">
            {loading ? <LoaderMain/> : null}
            <div className="pt-3">
              <label className="form-label">Select User Type<span className="error_class">*</span>
                {errors.ReviewerError &&
                  <span className="error"> - {errors.ReviewerError}</span>}
              </label>

              <Select
                styles={customStyles}
                placeholder='Select the Reviewer Type'
                options={options}
                value={selectReviewer}
                onChange={handleSelectChange}
              />
            </div>
            <div className="pt-3">
              <label className="form-label w-100">
                Assign User<span className="error_class">*</span> <small
                className="text-danger float-end">{errorMessage}</small> {errors.userListError &&
                <span className="error"> - {errors.userListError}</span>}
              </label>
              <div className="autotext-box">

                <input
                  type="text"
                  value={inputValue}
                  onChange={handleInputChange}
                  className="form-control"
                  placeholder="Search Users"
                  disabled={!showInput}
                />
                {Loading ? <div className="spinner-border text-primary spinner"></div> : null}
                {(suggestions.length !== 0 && showSuggestions) && <div className="autotext-result">
                  <ul className="m-0">
                    {suggestions?.map((user, index) => (
                      <li key={index}>
                        <a onClick={() => handleAddUser(user)}>
                          <div> {user.fullName}</div>
                          <div className="suggest-email">{user.rootRoleType}</div>
                        </a>
                      </li>
                    ))}
                  </ul>
                </div>}
              </div>
              {selectedUsers.length > 0 ? (
                <>
                  {selectedUsers.map((user, index) => (
                    <div className="tag mt-3" key={index}>
                      <span>{user?.fullName}</span>
                      <button type="button" onClick={() => handleRemoveUser(user?.userId)}>
                        X
                      </button>
                    </div>
                  ))}
                </>
              ) : null}
            </div>
            <div className="pt-3">
              <label className="form-label">
                Message<span className="error_class">*</span>
                {errors.description_error &&
                  <span className="error"> - {errors.description_error}</span>}
              </label>
              <RichText
                value={reviewDescription}
                placeHolderText="Description"
                onValueChange={reviewDescriptionChange}
              />
            </div>
            <div className="modal-footer">
              <button type={"submit"} onClick={HandleSubmit} className="btn btn--cancel">
                ASSIGN
              </button>
              <button
                className="btn btn--cancel"
                type="button" onClick={handleCancel}>
                CANCEL
              </button>
            </div>
          </div>
        </div>
      </Modal>
    </>
  )
}

export default AssignProduction;
