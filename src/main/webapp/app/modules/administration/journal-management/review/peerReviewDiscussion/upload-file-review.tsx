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
import {Translate,translate} from "react-jhipster";

interface ErrorState {
  file_type_error?: string;
  file_upload_error?: string;
}

const UploadFilesReview = (props) => {
  const {showModal, handleClose,  submissionId,reviewRoundId,handleFileUploadModelRequest} = props;
  const loading = useAppSelector(state => state.review.loading);

  const [errors, setErrors] = useState<ErrorState>({});
  // const [reviewDescription, setReviewDescription] = useState("");
  const fileInputRef = useRef(null);
  const [fileType, set_fileType] = useState<any>([]);
  const [fileTypeList, setFileTypeList] = useState<any>([]);
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [isLoading, setIsLoading] = useState(false)

  const handleCancel = () => {
    setSelectedFiles([])
    // setReviewDescription("")
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


  // const reviewDescriptionChange = (value: string) => {
  //   setReviewDescription(value);
  // };

  const validateForm = () => {
    const errorsValidate: ErrorState = {};

    if (selectedFiles.length === 0) {
      errorsValidate.file_upload_error = translate("errors_Validate.Upload_your_file");
    }
    if (Object.keys(fileType).length === 0) {
      errorsValidate.file_type_error = translate("errors_Validate.Select_file_type");
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
      // formData.append('note', reviewDescription);
      const requestData = {
        formData,
        submissionId,
        reviewRoundId
      }
      handleFileUploadModelRequest(requestData)
    }
  }

  const handleDelete = (name, type) => {
    const newArray = selectedFiles.filter(item => item?.name !== name && item?.type !== type);
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
                <h5 className="m-0"><Translate contentKey="File_Upload_title.File_Upload"></Translate></h5>
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
              <div className='row mt-3'>
                <div className="col-4">
                  <label className="form-label">
                    <Translate contentKey="File_Upload_title.Select_File_Type"></Translate><span className="error_class">*</span>
                  </label>
                  <Select placeholder= {translate("errors_Validate.Select_file_type")}
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
                    <Translate contentKey="File_Upload_title.Upload_File"></Translate><span className="error_class">*</span> {errors.file_upload_error &&
                    <span className="error"> {errors.file_upload_error}</span>}
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
                    <Translate contentKey="File_Upload_title.File_Name"></Translate>
                  </th>
                  <th scope="col" className="hand">
                    <Translate contentKey="File_Upload_title.File_Type"></Translate>
                  </th>
                  <th scope="col" className="hand">
                    <Translate contentKey="File_Upload_title.File_Format"></Translate>
                  </th>
                  <th scope="col" className="hand">
                    <Translate contentKey="File_Upload_title.Action"></Translate>
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
              <div className='pb-4 pb-3 text-center'><Translate contentKey="File_Upload_title.Upload_files_not_found."></Translate></div>
            }

            <div className="modal-footer">
              <button type={"submit"} onClick={HandleSubmit} className="btn btn--cancel">
                <Translate contentKey="File_Upload_title.UPLOAD_FILE"></Translate>
              </button>
              <button
                className="btn btn--cancel"
                type="button" onClick={handleCancel}>
                <Translate contentKey="File_Upload_title.CANCEL"></Translate>
              </button>
            </div>
          </div>
        </div>
      </Modal>
    </>
  )
}

export default UploadFilesReview;
