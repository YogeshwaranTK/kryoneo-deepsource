import React, {useEffect, useRef, useState} from "react";
import {Modal} from "reactstrap";
import RichText from "app/shared/rich-text/rich-text";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import Select from "react-select";
import {upperCase} from "lodash";
import {
  getJournalFiletypeRequest
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import {
  getSubmissionDetails,
  postAddDiscussionRequest
} from "app/modules/administration/journal-management/submission/submission.reducer";

interface ErrorState {
  userListError?: string;
  subjectInputValueError?: string;
  description_error?: string;
}

const AddDiscussionSubmission = (props) => {
  const dispatch = useAppDispatch();
  const {
    showModal,
    handleClose,
    participantLists,
    submissionId,
    setSubmissionDiscussionList,
    submissionDiscussionList
  } = props;
  const addDiscussionSuccess = useAppSelector(state => state.submission.addDiscussionSuccess);
  const postAddDiscussionPayload = useAppSelector(state => state.submission.postAddDiscussionPayload);
  const loading = useAppSelector(state => state.submission.loading);
  const [participantList, setParticipantList] = useState([])
  const [errors, setErrors] = useState<ErrorState>({});
  const [discussionDescription, setDiscussionDescription] = useState("");

  const handleCancel = () => {
    setDiscussionDescription('')
    setParticipantList([])
    setSubjectInputValue('')
    setSelectedFiles([])
    set_fileType([])
    handleClose()
  }


  useEffect(() => {
    if (addDiscussionSuccess === true) {
      const updatedDiscussionList = [...submissionDiscussionList, postAddDiscussionPayload];
      setSubmissionDiscussionList(updatedDiscussionList);
      dispatch(getSubmissionDetails(submissionId))
      handleCancel();
    }
  }, [addDiscussionSuccess]);


  const HandleChange = (e, value, type) => {
    if (type === "userSelect") {
      if (e.target.checked) {
        setParticipantList([...participantList, value]);
      } else {
        setParticipantList(participantList.filter((id) => id !== value));
      }
    }
  }


  const discussionDescriptionChange = (value: string) => {
    setDiscussionDescription(value);
  };

  const validateForm = () => {
    const errorsValidate: ErrorState = {};
    if (participantList.length === 0) {
      errorsValidate.userListError = 'Select at least one participant';
    }
    if (subjectInputValue === '') {
      errorsValidate.subjectInputValueError = 'Subject is required';
    }
    if (discussionDescription.length === 0) {
      errorsValidate.description_error = 'Required Description';
    }
    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };

  const HandleSubmit = () => {
    const formData = new FormData();

    selectedFiles.forEach((file) => {
      const filename = file.type + '-' + file.name;
      formData.append(filename, file.file);
    });
    const joinedString = participantList.join(',');
    formData.append('members', joinedString);
    formData.append('topic', subjectInputValue);
    formData.append('description', discussionDescription);
    const data = {
      formData,
      submissionId
    }

    if (validateForm()) {
      dispatch(postAddDiscussionRequest(data));
      handleCancel();
    }
  }


  const fileInputRef = useRef(null);
  const [fileType, set_fileType] = useState<any>([]);
  const [fileTypeList, setFileTypeList] = useState<any>([]);
  const [selectedFiles, setSelectedFiles] = useState([]);

  const fetchArticleFileType = (articleId) => {
    const filteredItem = fileTypeList.find(item => item?.id === articleId);
    return filteredItem?.name
  }


  const handleFileChanges = (e: React.ChangeEvent<HTMLInputElement>) => {
    const inputElement = e.target as HTMLInputElement;
    const files = inputElement.files;

    if (files) {
      const updatedFiles = Array.from(files).map((file) => ({
        type: fileType.value,
        name: `${file?.name}`,
        file,
      }));

      setSelectedFiles([...selectedFiles, ...updatedFiles]);
    }
  };

  const [subjectInputValue, setSubjectInputValue] = useState('');
  const handleSubjectInputChange = (event) => {
    setSubjectInputValue(event.target.value);
  };


  const getFileExtension = (filename) => {
    const lastDotIndex = filename.lastIndexOf(".");
    if (lastDotIndex !== -1) {
      return filename.substring(lastDotIndex);
    } else {
      return null;
    }
  }

  const handleDelete = (name, type) => {
    const newArray = selectedFiles.filter((file) => file?.name !== name || file?.type !== type);
    setSelectedFiles(newArray);
  };

  const getJournalFileTypeArticle = () => {
    getJournalFiletypeRequest()
      .then(response => {
        setFileTypeList(response.data)
      })
      .catch(error => {
        console.error(error);
      });
  };

  useEffect(() => {
    if (showModal === true) {
      getJournalFileTypeArticle()
    }
  }, [showModal])

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
                <h5 className="m-0">ADD DISCUSSION</h5>
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
            <p className="request-sub-head m-0">Participants<span
              className="error_class">*</span> {errors.userListError &&
              <span className="error"> - {errors.userListError}</span>}</p>
            {
              participantLists?.map((value) => (
                <div className="custom-checkbox" key={value.id}>
                  <input className={`form-check-input`} onChange={(e) => HandleChange(e, value.id, "userSelect")}
                         type="checkbox" name="agree"/>
                  <label>{value.fullName}</label>
                </div>
              ))
            }

            <div className="pt-3">
              <label className="form-label article_custom_label">Subject<span className="error_class">*</span></label>
              <input
                name="title"
                id="title"
                placeholder="Enter Subject"
                data-cy="title"
                type="text"
                className="form-control"
                value={subjectInputValue}
                onChange={handleSubjectInputChange}
              />
              {errors.subjectInputValueError &&
                <span className="error"> {errors.subjectInputValueError}</span>}
            </div>
            <div className="pt-3">
              <label className="form-label">
                Message<span className="error_class">*</span>
                {errors.description_error &&
                  <span className="error"> - {errors.description_error}</span>}
              </label>
              <RichText
                value={discussionDescription}
                placeHolderText="Discussion Description"
                onValueChange={discussionDescriptionChange}
              />
            </div>

            <div className="col-12 pb-3 " id="custom-form-input">
              <div className='row mt-3'>
                <div className="col-4">
                  <label className="form-label">
                    Select File Type
                  </label>

                  <Select placeholder='Select file type'

                          onChange={(values: any[]) => {
                            set_fileType(values)
                          }} value={fileType}

                          options={fileTypeList?.map(({id, name}) => ({
                            value: id,
                            label: name,
                          }))}/>
                </div>


                <div className="col-8">
                  <label className="form-label">
                    Upload File
                  </label>
                  <input multiple ref={fileInputRef} name="file" id="file" type="file"
                         className="form-control" onChange={handleFileChanges} disabled={fileType.length === 0}/>
                </div>
              </div>


            </div>

            {selectedFiles?.length > 0 ?
              <table className="table work-flow_table">
                <thead>
                <tr>
                  <th scope="col" className="hand ">
                    #
                  </th>
                  <th scope="col" className="hand">
                    File Name
                  </th>
                  <th scope="col" className="hand">
                    File Type
                  </th>
                  <th scope="col" className="hand">
                    File Format
                  </th>
                  <th scope="col" className="hand">
                    Action
                  </th>
                </tr>
                </thead>
                <tbody>

                {selectedFiles?.map((file, index) => (
                  <tr key={index}>
                    <td>{index + 1}</td>
                    <td>{file?.name}</td>
                    <td>{fetchArticleFileType(file?.type)}</td>
                    <td>{upperCase(getFileExtension(file?.name))}</td>
                    <td>
                      <FontAwesomeIcon icon="trash" className="fa-trash"
                                       onClick={() => handleDelete(file?.name, file?.type)}/>
                    </td>
                  </tr>
                ))}
                </tbody>
              </table> :
              <div className='pb-4 pb-3 text-center'>Upload files not found.</div>
            }


            <div className="modal-footer">
              <button type={"submit"} onClick={HandleSubmit} className="btn btn--cancel">
                ADD DISCUSSION
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

export default AddDiscussionSubmission;
