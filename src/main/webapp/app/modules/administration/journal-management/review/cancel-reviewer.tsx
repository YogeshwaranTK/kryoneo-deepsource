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
import {getReviewerRoleListRaw} from "../contributors/contributors.reducer";
import {cancelReviewerStatus} from "app/modules/administration/journal-management/review/reviewReducer";


interface ErrorState {
  userListError?: string;
  description_error?: string;
  update_duedate_error?: string;
  review_duedate_error?: string;
  reviewTypeError?: string;
  file_type_error?: string;
  file_upload_error?: string;
}

const CancelReviewer = (props) => {
  const dispatch = useAppDispatch();
  const {
    showModal,
    handleClose,
    submissionId,
    reviewRoundId,
    reviewFiles,
    handleFileUploadModelRequest,
    handleAddReviewerFileUpload
  } = props;

  const addReviewSuccess = useAppSelector(state => state.review.addReviewSuccess);
  const fileUploadReviewReadyFilesSuccess = useAppSelector(state => state.review.fileUploadReviewReadyFilesSuccess);
  const loading = useAppSelector(state => state.production.loading);
  const fileUploadReview = useAppSelector(state => state.review.fileuploadReview);
  const [errors, setErrors] = useState<ErrorState>({});
  const [discussionDescription, setDiscussionDescription] = useState("");
  const [responseDueDate, setResponseDueDate] = useState('');
  const [reviewDueDate, setReviewDueDate] = useState('');
  const [reviewType, setReviewType] = useState('ANONYMOUS_REVIEWER_AND_ANONYMOUS_AUTHOR');
  const [inputValue, setInputValue] = useState([]);
  const [selectfile, setselectFile] = useState([])
  const [selectedReviewer, setSelectedReviewer] = useState('')
  const handleCancel = () => {
    setDiscussionDescription('')
    setResponseDueDate('')
    setReviewDueDate('')
    handleClose()
  }
  const [showUploadFile, setShowUploadFile] = useState(false)

  useEffect(() => {
    if (addReviewSuccess === true) {
      // const updatedDiscussionList = [...productionDiscussionList, reviewAddDiscussionPayload];
      // setProductionDiscussionList(updatedDiscussionList);
      handleCancel();
    }
    if (fileUploadReviewReadyFilesSuccess === true) {
      setShowUploadFile(false)
      setSelectedFiles([])
      const idArray = fileUploadReview.map(item => item.id);
      setselectFile([...selectfile, ...idArray]);
    }
  }, [addReviewSuccess, fileUploadReviewReadyFilesSuccess]);


  useEffect(() => {
    fetchSuggestions('')
  }, [])

  const fetchSuggestions = async (value: string) => {
    try {
      const response = await getReviewerRoleListRaw({
        page: 0,
        size: 30,
        searchText: value,
      });
      setInputValue(response.data)
    } catch (error) {
      console.error("An error occurred:", error);
    }
  };


  const discussionDescriptionChange = (value: string) => {
    setDiscussionDescription(value);
  };

  const validateForm = () => {
    const errorsValidate: ErrorState = {};
    if (discussionDescription.length === 0) {
      errorsValidate.description_error = 'Required Description';
    }
    if (responseDueDate.length === 0) {
      errorsValidate.update_duedate_error = 'Required Response DueDate';
    }
    if (reviewDueDate.length === 0) {
      errorsValidate.review_duedate_error = 'Required Review DueDate';
    }
    if (reviewType === '') {
      errorsValidate.reviewTypeError = 'Required Review Type';
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

    formData.append('description', discussionDescription);
    const data = {
      submissionId,
      reviewRoundId,
      "userId": selectedReviewer,
      responseDueDate,
      reviewDueDate,
      "submissionReviewType": reviewType,
      "message": discussionDescription,
      "reviewRoundFileIds": selectfile
    }

    if (validateForm()) {
      dispatch(handleFileUploadModelRequest(data));
    }
  }

  const [fileTypeList, setFileTypeList] = useState<any>([]);
  const [selectedFiles, setSelectedFiles] = useState([]);


  const handleSelectChange = (selectedOption) => {
    if (selectedOption) {
      const selectedId = selectedOption.value;
      const selectedFullName = selectedOption.label;
      setSelectedReviewer(selectedId)

    }
  };


  const handleDelete = (name, type) => {
    const newArray = selectedFiles.filter(item => item?.name !== name && item?.type !== type);
    setSelectedFiles(newArray);
  };

  const cancelReviewer = (userId) => {
    dispatch(cancelReviewerStatus({ reviewRoundId, userId }));
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

  const getCurrentDate = () => {
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  };
  const handleResponseDateChange = (event) => {
    const isoFormattedDate = new Date(event.target.value).toISOString();
    setResponseDueDate(isoFormattedDate);
  };
  const handleReviewDateChange = (event) => {
    const isoFormattedDate = new Date(event.target.value).toISOString();
    setReviewDueDate(isoFormattedDate);
  };
  const handleReviewTypeChange = (event) => {
    setReviewType(event.target.value);
  };
  const handleCheckboxChange = (event, fileId) => {
    const {checked} = event.target;

    if (checked) {

      setselectFile([...selectfile, fileId]);
    } else {
      setselectFile(selectfile.filter(id => id !== fileId));
    }
  };

  const [fileType, set_fileType] = useState<any>([]);
  const fileInputRef = useRef(null);

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
    inputElement.value = '';
  };

  const fetchArticleFileType = (articleId) => {
    const filteredItem = fileTypeList.find(item => item?.id === articleId);
    return filteredItem?.name
  }

  const getFileExtension = (filename) => {
    const lastDotIndex = filename.lastIndexOf(".");
    if (lastDotIndex !== -1) {
      return filename.substring(lastDotIndex);
    } else {
      return null;
    }
  }

  const validateFormFile = () => {
    const errorsValidate: ErrorState = {};

    if (selectedFiles.length === 0) {
      errorsValidate.file_upload_error = 'Upload your file';
    }
    if (Object.keys(fileType).length === 0) {
      errorsValidate.file_type_error = 'Select file type';
    }

    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };

  const HandleFileSubmit = () => {
    if (validateFormFile()) {
      const formData = new FormData();
      selectedFiles.forEach((file) => {
        const filename = file.type + '-' + file.name
        formData.append(filename, file.file);
      });
      const requestData = {
        formData,
        submissionId,
        reviewRoundId
      }
      handleAddReviewerFileUpload(requestData)
    }
  }


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
                <h5 className="m-0">Cancel Reviewer</h5>
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
              <label className="form-label">
                Email to be sent to reviewer<span className="error_class">*</span>
                {errors.description_error &&
                  <span className="error"> - {errors.description_error}</span>}
              </label>
              <RichText
                value={discussionDescription}
                placeHolderText="Enter your message"
                onValueChange={discussionDescriptionChange}
              />
            </div>

            <div className="modal-footer">
              <button type={"submit"} onClick={() => cancelReviewer(8)} className="btn btn--cancel">
              {/*<button type={"submit"}   className="btn btn--cancel">*/}
                Cancel Reviewer
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

export default CancelReviewer;
