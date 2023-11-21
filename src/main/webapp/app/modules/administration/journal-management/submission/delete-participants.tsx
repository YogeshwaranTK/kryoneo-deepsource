import React, {useEffect, useState} from "react";
import {Modal} from "reactstrap";
import RichText from "app/shared/rich-text/rich-text";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import {
  getAuthorRoleListRaw,
  getEditorialRoleListRaw,
  getReviewerRoleListRaw,
} from "app/modules/administration/journal-management/contributors/contributors.reducer";

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
const deleteParticipants = (props) => {
  const dispatch = useAppDispatch();
  const {showModal, handleClose,setParticipantList,handleParticipantDelete,participantList,submissionId} = props;
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
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [selectReviewer, setSelectedReviewer] = useState(null);
  const [showInput, setShowInput] = useState(false);
  const options = [
    // { value: 'REVIEWERS', label: 'Reviewers' },
    { value: 'EDITORIAL', label: 'Editorial' },
    { value: 'AUTHOR', label: 'Author' },
  ]
  useEffect(() => {
    if (postAssignUserSuccessPayload.length !== 0) {
      setParticipantList([...participantList, ...postAssignUserSuccessPayload])
    }
    if (assignUserSuccess) {
      handleCancel()
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

    handleParticipantDelete()
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
      // if (selectReviewer.value === "REVIEWERS") {
      //   response = await getReviewerRoleListRaw({
      //     page: 0,
      //     size: 30,
      //     searchText: value,
      //     journalId: submissionId
      //   });
      // }
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
    const {value} = event.target;
    setInputValue(value)
    if (value) {
      fetchSuggestions(value);
      setSuggestions([]);
    }
    setShowSuggestions(suggestions.length !== 0 && value.trim() !== '')
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
      setInputValue('');
      setErrorMessage('');
      setSuggestions([]);
      setShowSuggestions(false)
    }
  };

  useEffect(()=>{
    setShowSuggestions(inputValue !== '')
  },[inputValue])

  const handleSelectChange = (selectedOption) => {
    setSelectedReviewer(selectedOption);
    if (selectedOption) {
      setShowInput(true);
    } else {
      setShowInput(false);
      setInputValue('');
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
                <h5 className="m-0">REMOVE PARTICIPANTS</h5>
                <span className="request-head"></span>
              </div>
            </div>
            <button
              type="button"
              className="btn-close"
              onClick={handleCancel}
            ></button>
          </div>
          {/*<div className="modal-body ps-4 position-relative">*/}
          {/*  {loading ? <LoaderMain/> : null}*/}
          {/*  <div className="pt-3">*/}
          {/*    <h6>You are about to remove this participant from all stages ? </h6>*/}


          {/*  </div>*/}


          {/*  <div className="modal-footer">*/}

          {/*    <button*/}
          {/*      className="custom-btn" style={{border:"none"}}*/}
          {/*      type="button" onClick={handleCancel}>*/}
          {/*      CANCEL*/}
          {/*    </button>*/}
          {/*    <button type={"submit"} onClick={HandleSubmit} style={{border:"none"}} className="custom-btn">*/}
          {/*      REMOVE*/}
          {/*    </button>*/}
          {/*  </div>*/}
          {/*</div>*/}

          <div className="modal-body ps-4 position-relative">
            {loading ? <LoaderMain /> : null}
            <div className="pt-3 d-flex flex-column">
              <h6 className="mb-3">You are about to remove this participant from all stages?</h6>

              <div className="modal-footer d-flex justify-content-end">
                <button className="custom-btn" style={{ border: "none" }} type="button" onClick={handleCancel}>
                  CANCEL
                </button>
                <button type="submit" onClick={HandleSubmit} style={{ border: "none" }} className="custom-btn">
                  REMOVE
                </button>
              </div>
            </div>
          </div>





        </div>
      </Modal>
    </>
  )
}

export default deleteParticipants;
