import React, {useEffect, useRef, useState} from "react";
import {Modal} from "reactstrap";
import {useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import Select from "react-select";
import {upperCase} from "lodash";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
  getJournalFiletypeRequest
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import RichText from "app/shared/rich-text/rich-text";

interface ErrorState {
  chatMsgError?: string;
}

const AddChatSubmission = (props) => {
  const {showModal, handleClose, handleFileUploadModelRequest, submissionId, selectDiscussionId} = props;
  const loading = useAppSelector(state => state.submission.loading);
  const [errors, setErrors] = useState<ErrorState>({});
  const [message, setMessage] = useState("");
  const fileInputRef = useRef(null);
  const [fileType, set_fileType] = useState<any>([]);
  const [fileTypeList, setFileTypeList] = useState<any>([]);
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [isLoading, setIsLoading] = useState(false)


  const handleCancel = () => {
    setSelectedFiles([])
    setMessage("")
    handleClose()
  }

  const getJournalFileTypeArticle = () => {
    setIsLoading(true)
    getJournalFiletypeRequest()
      .then(response => {
        setFileTypeList(response.data)
        setIsLoading(false)
      })
      .catch(error => {
        setIsLoading(false)
        console.error(error);
      });
  };

  useEffect(() => {
    if (showModal === true) {
      getJournalFileTypeArticle()
    }
  }, [showModal])


  const MessageChange = (value: string) => {
    setMessage(value);
  };

  const validateForm = () => {
    const errorsValidate: ErrorState = {};

    if (message === '') {
      errorsValidate.chatMsgError = 'Chat Message is required';
    }

    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };

  const getFileExtension = (filename) => {
    const lastDotIndex = filename.lastIndexOf(".");
    if (lastDotIndex !== -1) {
      return filename.substring(lastDotIndex);
    } else {
      return null;
    }
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
    inputElement.value = '';
  };

  const fetchArticleFileType = (articleId) => {
    const filteredItem = fileTypeList.find(item => item?.id === articleId);
    return filteredItem?.name
  }

  const HandleSubmit = () => {
    if (validateForm()) {
      const formData = new FormData();
      selectedFiles.forEach((file) => {
        const filename = file.type + '-' + file.name
        formData.append(filename, file.file);
      });
      formData.append('message', message);
      formData.append('submissionId', submissionId);
      const requestData = {
        formData,
        submissionId,
        "discussionId": selectDiscussionId
      }
      handleFileUploadModelRequest(requestData)
    }
  }

  const handleDelete = (name, type) => {
    const newArray = selectedFiles.filter((file) => file?.name !== name || file?.type !== type);
    setSelectedFiles(newArray);
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
                <h5 className="m-0">Add Chat</h5>
              </div>
            </div>
            <button
              type="button"
              className="btn-close"
              onClick={handleCancel}
            ></button>
          </div>
          <div className="modal-body ps-4 position-relative">

            {loading || isLoading ? <LoaderMain/> : null}
            <div className="col-12 pb-3 " id="custom-form-input">


              <div className="pt-3">
                <label className="form-label">
                  Chat<span className="error_class">*</span>{errors.chatMsgError &&
                  <span className="error"> - {errors.chatMsgError}</span>}
                </label>
                <RichText
                  value={message}
                  placeHolderText="Write a chat"
                  onValueChange={MessageChange}
                />
              </div>

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
                ADD CHAT
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

export default AddChatSubmission;
