import React, {ChangeEvent, useEffect, useState, useRef} from 'react';
import './work-flow.scss';
import {
  AricleFinalRequest,
  getSubmissionDetailsRaw,
  ArticleUploadRequest
} from "app/modules/administration/journal-management/create-new-submission/work-flow.reducer";
import Select from "react-select";
import {useAppDispatch, useAppSelector} from "app/config/store";
import JButton from "app/shared/component/button/button";
import LoaderMain from "app/shared/Loader/loader-main";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {upperCase} from "lodash";
import ConFirmModel
  from "app/modules/administration/journal-management/create-new-submission/submission-models/confirm-model";
import {useNavigate, useParams} from "react-router-dom";
import {
  getJournalFiletypeRequest
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import {translate, Translate} from "react-jhipster";

export interface ModalErrors {
  fileUploadError?: string;
}

export const UploadFile = ({
                             routeState,
                           }) => {
  const dispatch = useAppDispatch();
  const [fileTypeList, setFileTypeList] = useState([]);
  const [fileType, setFileType] = useState<any>([]);
  const ArticleUploadRequestStatus = useAppSelector(state => state.workflow.ArticleUploadRequestStatus);
  const journalFileTypeList = useAppSelector(state => state.workflow.journalFileTypeList);
  const loading = useAppSelector(state => state.workflow.loading);
  const [loadingLocal, setLoadingLocal] = useState(false);
  const fileInputRef = useRef(null);
  const [UploadError, setUploadError] = useState(false);
  const [errors, setErrors] = useState<ModalErrors>({});
  const [finalSubmit, setFinalSubmit] = useState('');
  const finalSubmitFinish = useAppSelector(state => state.workflow.finalSubmit);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const finishArticle_status = useAppSelector(state => state.workflow.finishArticle_status);
  const navigate = useNavigate();
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]

  useEffect(() => {
    if (finalSubmitFinish && (finalSubmit === 'final')) {
      setFinalSubmit('finish')
      setShowConfirmModal(true)
    }
  }, [finalSubmitFinish, ArticleUploadRequestStatus, finalSubmit])

  useEffect(()=>{
    setFileTypeList(journalFileTypeList)
  }, [journalFileTypeList])

  useEffect(() => {
    JournalList();
    if (finishArticle_status && (finalSubmit === 'finish')) {
      setFinalSubmit('');
      setShowConfirmModal(false);
      navigate(`/journal/${parseInt(Jo_id, 10)}`);
    }
  }, [ArticleUploadRequestStatus, finalSubmitFinish, finishArticle_status]);

  const errorCheck = () => {
    const errorsValidate: { submissionLanguage?: string; files?: string; } = {};
    if (!selectedFiles?.some((item) => item?.fileType?.name === "Article Text")) {
      setUploadError(true);
      errorsValidate.files = 'error';
    } else {
      setUploadError(false);
    }
    return Object.keys(errorsValidate).length === 0;
  }
  const [selectedFiles, setSelectedFiles] = useState([]);
  const fetchArticleFileType = (articleId) => {
    const filteredItem = fileTypeList.find(item => item?.id === articleId);
    return filteredItem?.name
  }

  const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
    const updatedReactData = {...errors};
    delete updatedReactData["fileUploadError"];
    setErrors(updatedReactData);
    const inputElement = event.target as HTMLInputElement;
    const files = inputElement.files;
    if (files) {
      const updatedFiles = Array.from(files).map((file) => ({
        newId: Math.floor(Math.random() * (Number.MAX_SAFE_INTEGER - 100000)) + 100000,
        fileType: {
          id: fileType.value,
          name: fetchArticleFileType(fileType.value)
        },
        file: file?.name,
        fileEndPoint: file,
      }));
      setSelectedFiles([...selectedFiles, ...updatedFiles]);
    }
    inputElement.value = '';
  };

  const uploadSubmit = (e) => {
    if (validateForm) {
      const formData = new FormData();
      selectedFiles.forEach((fileDetails) => {
        const filename = fileDetails.fileType.id + '-' + fileDetails.file
        formData.append(filename, fileDetails.fileEndPoint);
      });

      const idArray = selectedFiles
        .filter(item => item?.id !== undefined)
        .map(item => item?.id);

      if (idArray.length !== 0) {
        formData.append('oldFileIds', idArray.join(','));
      } else {
        formData.append('oldFileIds', '');
      }

      const requestData = {
        formData,
        "submissionId": parseInt(routeState, 10)
      };
      dispatch(ArticleUploadRequest(requestData)).then((resultAction) => {
        if (ArticleUploadRequest.fulfilled.match(resultAction)) {
          const {status} = resultAction.payload;
          if (status === 200 && e === 'final') {
            if (errorCheck()) {
              setFinalSubmit("final")
            }
          }
        }
      });
    }
  };

  const JournalList = () => {
    setLoadingLocal(true)
    getSubmissionDetailsRaw(parseInt(routeState, 10)).then(response => {

      setLoadingLocal(false)
      if (response?.data?.submissionFiles !== null && response?.data?.submissionFiles !== undefined) {
        setSelectedFiles(response?.data?.submissionFiles)
      }
    })
      .catch(error => {
        console.error(error);
      });
  };

  const validateForm = () => {
    const errorsValidate: ModalErrors = {};

    if (selectedFiles.length === 0) {
      errorsValidate.fileUploadError = 'Choose file';
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
  };

  const handleDelete = (id, type) => {
    if (type === "old") {
      const itemToDelete = selectedFiles.map((item) => {
        if (item.id === id) {
          return null;
        }
        return item;
      });

      const updatedData = itemToDelete.filter((deleteFiles) => deleteFiles !== null);
      setSelectedFiles(updatedData);
    } else if (type === "new") {
      const itemToDelete = selectedFiles.map((item) => {
        if (item.newId === id) {
          return null;
        }
        return item;
      });

      const updatedData = itemToDelete.filter((deleteFiles) => deleteFiles !== null);
      setSelectedFiles(updatedData);
    }
  };

  const saveAndDraft = () => {
    uploadSubmit("draft");
  };

  const saveAndFinal = () => {
    uploadSubmit("final");
  };

  const handleReset = () => {
    JournalList();
  };

  const handleClose = () => {
    setShowConfirmModal(false);
  };

  const confirmSubmit = () => {
    setShowConfirmModal(false);
    if (finalSubmit === 'finish')
      dispatch(AricleFinalRequest(parseInt(routeState, 10)));
  };

  return (
    <>
      <ConFirmModel showConfirmModal={showConfirmModal}
                    handleClose={handleClose}
                    confirmSubmit={confirmSubmit}/>

      <div className='upload_datails'>
        <div className="col-12 position-relative ">
          {loadingLocal || loading ? <LoaderMain/> : null}
          <div className="row">
            <div className="col-12 py-3" id="custom-form-input">
              <div className='row mt-3'>
                <div className="col-4">
                  <label className="form-label">
                    <Translate contentKey="Upload_files.Select_File_Type">Select File Type</Translate><span
                    className="error_class">*</span>
                  </label>
                  <Select placeholder={translate("Upload_files.Select_File_Type")}
                          options={fileTypeList?.map(({id, name}) => ({
                            value: id,
                            label: name,
                          }))}
                          onChange={(values: any[]) => {
                            setFileType(values)
                          }} value={fileType}
                  />
                </div>
                <div className="col-8">
                  <label className="form-label">
                    <Translate contentKey="Upload_files.Upload_Files">Upload Files</Translate><span
                    className="error_class">*</span>
                  </label>
                  <input multiple ref={fileInputRef} name="file" id="file" type="file"
                         className="form-control" onChange={handleFileChange} disabled={fileType.length === 0}/>
                  {errors.fileUploadError && <span className="error">{errors.fileUploadError}</span>}
                </div>
              </div>
            </div>
            <div className="col-4"></div>
            <div className='position-relative'>
              {UploadError &&
                <span className="error"><Translate contentKey="Upload_files.Article_Text_Required">Article text is required</Translate></span>}
              {selectedFiles?.length > 0 ?
                <table className="table work-flow_table">
                  <thead>
                  <tr>
                    <th scope="col" className="hand custom_sno_th">
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
                      Download
                    </th>
                    <th scope="col" className="hand">
                      Action(s)
                    </th>
                  </tr>
                  </thead>
                  <tbody>
                  {selectedFiles?.map((item, index) => (
                    <tr key={index}>
                      <td className="custom_sno_td">{index + 1}</td>
                      <td>{item?.file}</td>
                      <td>{upperCase(item?.fileType?.name)}</td>
                      <td>{upperCase(getFileExtension(item?.file))}</td>
                      <td>
                        <a href={item.fileEndPoint} target='_blank' rel='noopener noreferrer'>
                          <FontAwesomeIcon icon="cloud-arrow-down" className="fa-cloud-arrow-down"/>
                        </a>
                      </td>
                      <td>
                        <FontAwesomeIcon icon="trash" className="fa-trash"
                                         onClick={() => handleDelete(item?.id ? item?.id : item?.newId, item?.id ? "old" : "new")}/>
                      </td>
                    </tr>
                  ))}
                  </tbody>
                </table> :
                <div className='text-center'>
                  <div className='pb-4 pb-3 text-center'><Translate contentKey="File_Not_Found">Upload files not
                    found.</Translate></div>
                </div>
              }

              <hr/>
              <div className="d-flex pt-4">
                <div className="pe-2 m-top">
                  <button className="btn custom-btn" type="button" onClick={() => handleReset()}>
                    <Translate contentKey="reset">RESET</Translate>
                  </button>
                </div>
                <div className="pe-2 m-top">
                  <button className="btn custom-btn-secondary" type="submit" onClick={saveAndFinal}>
                    <Translate contentKey="buttons.Finish">SAVE & FINISH</Translate>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <JButton JbuttonValue={translate("buttons.Save_Draft")} onclick={saveAndDraft} type={"submit"}
                 className={"btn custom-btn-secondary save_draft_btn"}/>
      </div>
    </>
  )
};
export default UploadFile;
