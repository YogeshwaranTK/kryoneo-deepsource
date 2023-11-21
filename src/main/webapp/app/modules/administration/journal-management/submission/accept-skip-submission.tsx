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
  movetoProduction,
  postSubmissionReadyFilesUpload,
  stateFalse
} from "app/modules/administration/journal-management/submission/submission.reducer";
import {useNavigate, useParams} from "react-router-dom";

interface ErrorState {
  file_type_error?: string;
  file_upload_error?: string;
  description_error?: string;
  fileSelectError?: string;
}

const AcceptSkipSubmission = (props) => {
  const dispatch = useAppDispatch();
  const {
    showModal,
    handleClose,
    submissionReadyFilesList,
    submissionId,
  } = props;
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]
  const navigate = useNavigate()
  const moveToSubmissionSuccess = useAppSelector(state => state.submission.moveToSubmissionSuccess)
  const loading = useAppSelector(state => state.production.loading);
  const [errors, setErrors] = useState<ErrorState>({});
  const [discussionDescription, setDiscussionDescription] = useState("");
  const [emailType, setEmailType] = useState('SEND_EMAIL_NOTIFICATION');
  const [showUploadFile, setShowUploadFile] = useState(false)
  const [selectFile, setSelectFile] = useState([])
  const [file, setFile] = useState([])

  const handleCancel = () => {
    setDiscussionDescription('')
    setSelectedFiles([])
    set_fileType([])
    setShowUploadFile(false);
    handleClose()
  }

  useEffect(() => {
    setFile(submissionReadyFilesList)
  }, [submissionReadyFilesList])


  const discussionDescriptionChange = (value: string) => {
    setDiscussionDescription(value);
  };

  const validateForm = () => {
    const errorsValidate: ErrorState = {};

    if (selectFile.length === 0) {
      errorsValidate.fileSelectError = 'At leased select One file'
    }
    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };


  useEffect(() => {
    if (moveToSubmissionSuccess) {
      navigate(`/journal/${Jo_id}`)
      dispatch(stateFalse())
    }
  }, [moveToSubmissionSuccess])

  const HandleSubmit = () => {
    if (validateForm()) {
      const requestData = {
        submissionId,
        "from": "SUBMISSION",
        "file": selectFile,
        "skipReviewMail": discussionDescription
      }
      dispatch(movetoProduction(requestData));
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


  const handleEmailTypeChange = (event) => {
    setEmailType(event.target.value);
  };

  const handleCheckboxChange = (event, fileId) => {
    const {checked} = event.target;
    if (checked) {
      setSelectFile([...selectFile, fileId]);
    } else {
      setSelectFile(selectFile.filter(id => id !== fileId));
    }
  };


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
      }
      dispatch(postSubmissionReadyFilesUpload(requestData))
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
                <h5 className="m-0">ACCEPT & SKIP REVIEW</h5>
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

            <div className="col-12 pb-3 " id="custom-form-input">
              <label className="form-label">Send Email </label>
              <div className="custom-radio pe-2">
                <input
                  className={`form-check-input form-radio-input`}
                  type="radio"
                  value="SEND_EMAIL_NOTIFICATION"
                  checked={emailType === 'SEND_EMAIL_NOTIFICATION'}
                  onChange={handleEmailTypeChange}
                />
                <label>Send an email notification to the author(s):admin admin</label>
              </div>

              <div className="custom-radio">
                <input
                  className={`form-check-input form-radio-input`}
                  type="radio"
                  value="DONT_SEND_NOTIFICATION"
                  checked={emailType === 'DONT_SEND_NOTIFICATION'}
                  onChange={handleEmailTypeChange}
                />
                <label> Do not send an email notification</label>
              </div>
            </div>

            {emailType !== "DONT_SEND_NOTIFICATION" &&
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
              </div>}

            <div className="pt-3">
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <label className="form-label">Choose the File</label><span
                  className="error_class">*</span>
                  {errors.fileSelectError &&
                    <span className="error"> - {errors.fileSelectError}</span>}
                </div>
                {showUploadFile === false && <div>
                  <div className='form-label text-end text-primary'><a onClick={() => setShowUploadFile(true)}>
                    +Upload the File</a></div>
                </div>}
              </div>
              {file.map((data, index) => (
                <div key={index} className="custom-radio pe-2">
                  <input
                    className={`form-check-input form-radio-input`}
                    type="checkbox"
                    value={data.id}
                    checked={data.checked || selectFile.includes(data.id)}
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
                      Select File Type<span className="error_class">*</span>
                      {errors.file_type_error &&
                        <span className="error"> - {errors.file_type_error}</span>}
                    </label>
                    <Select placeholder='Select file type'
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
                      Upload File<span className="error_class">*</span>
                      {errors.file_upload_error &&
                        <span className="error"> - {errors.file_upload_error}</span>}
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
                    <th scope="col" className="hand">
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
              <div className="modal-footer justify-content-end">
                <button
                  className="btn btn--cancel"
                  type="button"
                  onClick={() => {
                    setShowUploadFile(false);
                    setSelectedFiles([]);
                    set_fileType([]);
                  }}
                >
                  CANCEL
                </button>
                <button type={"button"} onClick={HandleFileSubmit} className="btn btn--cancel">
                  UPLOAD FILE
                </button>
              </div>
            </>
            }

            <div className="modal-footer">
              <button type="button" onClick={HandleSubmit} className="btn btn--cancel">
                SUBMIT
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

export default AcceptSkipSubmission;
