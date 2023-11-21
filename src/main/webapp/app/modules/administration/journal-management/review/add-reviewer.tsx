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
import {Translate,translate} from "react-jhipster";

interface ErrorState {
  userListError?: string;
  description_error?: string;
  update_duedate_error?: string;
  review_duedate_error?: string;
  reviewTypeError?: string;
  file_type_error?: string;
  file_upload_error?: string;
}

const AddReviewer = (props) => {
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
      errorsValidate.description_error = (translate("ADD_Reviewer_title.Required_Description"))
    }
    if (responseDueDate.length === 0) {
      errorsValidate.update_duedate_error = (translate("ADD_Reviewer_title.Required_Response_DueDate"))
    }
    if (reviewDueDate.length === 0) {
      errorsValidate.review_duedate_error = (translate("ADD_Reviewer_title.Required_Review_DueDate"))
    }
    if (reviewType === '') {
      errorsValidate.reviewTypeError = (translate("ADD_Reviewer_title.Required_Review_Type"))
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
                <h5 className="m-0"><Translate contentKey="ADD_Reviewer_title.ADD_Reviewer"></Translate></h5>
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

            <Select
              options={inputValue.map((data) => ({
                value: data.userId,
                label: data.fullName,
              }))}
              placeholder={translate("ADD_Reviewer_title.Select_the_Reviewer")}
              onChange={handleSelectChange}

            />
            <div className="pt-3">
              <label className="form-label">
                <Translate contentKey="ADD_Reviewer_title.Message"></Translate><span className="error_class">*</span>
                {errors.description_error &&
                  <span className="error"> - {errors.description_error}</span>}
              </label>
              <RichText
                value={discussionDescription}
                placeHolderText={translate("ADD_Reviewer_title.Discussion_Description")}
                onValueChange={discussionDescriptionChange}
              />
            </div>


            <div className="col-12 pb-3 " id="custom-form-input">
              <div className='row mt-3'>
                <div className="col-6">

                  <label className="form-label"><Translate contentKey="ADD_Reviewer_title.Response_Due_Date"></Translate><span className="error_class">*</span>
                    {errors.update_duedate_error && <span className="error">{errors.update_duedate_error}</span>}
                  </label>
                  <input className="form-control" placeholder="dd/mm/yyyy" type="date"
                         value={responseDueDate.slice(0, 10)}
                         onChange={handleResponseDateChange}
                         min={getCurrentDate()}
                  />

                </div>


                <div className="col-6">
                  <label className="form-label"><Translate contentKey="ADD_Reviewer_title.Review_Due_Date"></Translate><span className="error_class">*</span>
                    {errors.review_duedate_error && <span className="error">{errors.review_duedate_error}</span>}
                  </label>
                  <input className="form-control" placeholder="dd/mm/yyyy" type="date"
                         value={reviewDueDate.slice(0, 10)}
                         onChange={handleReviewDateChange}
                         min={getCurrentDate()}
                  />

                </div>
              </div>
            </div>

            <div className="col-12 pb-3 " id="custom-form-input">
              <label className="form-label"><Translate contentKey="ADD_Reviewer_title.Review_Type"></Translate></label>
              <div className="custom-radio pe-2">
                <input
                  className={`form-check-input form-radio-input`}
                  type="radio"
                  value="ANONYMOUS_REVIEWER_AND_ANONYMOUS_AUTHOR"
                  checked={reviewType === 'ANONYMOUS_REVIEWER_AND_ANONYMOUS_AUTHOR'}
                  onChange={handleReviewTypeChange}
                />
                <label>Anonymous Reviewer/Anonymous Author</label>
              </div>

              <div className="custom-radio">
                <input
                  className={`form-check-input form-radio-input`}
                  type="radio"
                  value="ANONYMOUS_REVIEWER_AND_DISCLOSED_AUTHOR"
                  checked={reviewType === 'ANONYMOUS_REVIEWER_AND_DISCLOSED_AUTHOR'}
                  onChange={handleReviewTypeChange}/>
                <label> Anonymous Reviewer/Disclosed Author</label>
              </div>
              <div className="custom-radio">
                <input
                  className={`form-check-input form-radio-input`}
                  type="radio"
                  value="OPEN"
                  checked={reviewType === 'OPEN'}
                  onChange={handleReviewTypeChange}/>
                <label>Open</label>
              </div>
            </div>


            <div className="col-12 pb-3" id="custom-form-input">
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <label className="form-label"><Translate contentKey="ADD_Reviewer_title.Choose_the_File"></Translate></label>
                </div>
                {showUploadFile === false && <div>
                  <div className='form-label text-end text-primary'><a onClick={() => setShowUploadFile(true)}>
                    <Translate contentKey="ADD_Reviewer_title.+Upload_the_File"></Translate></a></div>
                </div>}
              </div>
              {reviewFiles.map((data, index) => (
                <div key={index} className="custom-radio pe-2">
                  <input
                    className={`form-check-input form-radio-input`}
                    type="checkbox"
                    value={data.id}
                    checked={selectfile.includes(data.id)}
                    onChange={(event) => handleCheckboxChange(event, data.id)}
                  />
                  <label>{data.file}</label>
                </div>
              ))}
            </div>


            {showUploadFile && <>
              <div className="col-12 pb-3 " id="custom-form-input">
                <div className='row mt-3'>
                  <div className="col-4">
                    <label className="form-label">
                      <Translate contentKey="ADD_Reviewer_title.Select_File_Type"></Translate><span className="error_class">*</span>
                    </label>
                    <Select placeholder={translate("ADD_Reviewer_title.Select_file_type")}
                            onChange={(values: any[]) => {
                              delete errors.file_type_error;
                              set_fileType(values)
                            }} value={fileType}
                            options={fileTypeList?.map(({id, name}) => ({
                              value: id,
                              label: name,
                            }))}/>
                  </div>
                  <div className="col-8">
                    <label className="form-label">
                      <Translate contentKey="ADD_Reviewer_title.Upload_File"></Translate><span className="error_class">*</span>
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
                      <Translate contentKey="ADD_Reviewer_title.File_Name"></Translate>
                    </th>
                    <th scope="col" className="hand">
                      <Translate contentKey="ADD_Reviewer_title.File_Type"></Translate>
                    </th>
                    <th scope="col" className="hand">
                      <Translate contentKey="ADD_Reviewer_title.File_Format"></Translate>
                    </th>
                    <th scope="col" className="hand">
                      <Translate contentKey="ADD_Reviewer_title.Action"></Translate>
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
                <div className='pb-4 pb-3 text-center'><Translate contentKey="ADD_Reviewer_title.Upload_files_not_found."></Translate></div>
              }
              <div className="modal-footer justify-content-end">
                <button
                  className="btn btn--cancel"
                  type="button"
                  onClick={() => {
                    setShowUploadFile(false);
                    setSelectedFiles([]);
                  }}
                >
                  <Translate contentKey="ADD_Reviewer_title.CANCEL"></Translate>
                </button>
                <button type={"button"} onClick={HandleFileSubmit} className="btn btn--cancel">
                  <Translate contentKey="ADD_Reviewer_title.UPLOAD_FILE"></Translate>
                </button>
              </div>
            </>
            }

            <div className="modal-footer">
              <button type={"submit"} onClick={HandleSubmit} className="btn btn--cancel">
                <Translate contentKey="ADD_Reviewer_title.ADD"></Translate>
              </button>
              <button
                className="btn btn--cancel"
                type="button" onClick={handleCancel}>
                <Translate contentKey="ADD_Reviewer_title.CANCEL"></Translate>
              </button>
            </div>
          </div>
        </div>
      </Modal>
    </>
  )
}

export default AddReviewer;
