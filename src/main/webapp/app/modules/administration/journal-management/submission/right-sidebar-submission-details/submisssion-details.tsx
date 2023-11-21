import React, {useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {Modal} from "reactstrap";
import LoaderMain from "app/shared/Loader/loader-main";
import {Translate} from "react-jhipster";
import {getSubmissionDetails} from "app/modules/administration/journal-management/submission/submission.reducer";


const SubmissionDetails = (props) => {
  const isOpen = props.isOpen
  const onClose = props.onClose
  const {submissionId} = props
  const dispatch = useAppDispatch();
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const journalLoading = useAppSelector(state => state.settingsManagement.loading);
  const submissionDetails = useAppSelector(state => state.submission.submissionDetails);
  const {submissionLanguage, keywords} = submissionDetails;
  const [showModel, setShowModel] = useState(false);

  const handleClose = () => {
    setShowModel(false);
  };

  useEffect(() => {
    if (submissionId) {
      dispatch(getSubmissionDetails(submissionId));
    }
  }, [submissionId]);

  const category = journals?.categories?.map((item, index) => <span key={index}
                                                                    className="article-details-tages">{item.name}</span>);

  const keyword = keywords?.map((item, index) => {
    if (index === keywords?.length - 1) {
      return <span key={index} className="article-details-tages">{item}</span>
    } else {
      return <span key={index} className="article-details-tages">{item}</span>
    }
  })

  const authors = submissionDetails?.authors?.filter(author => author.role === "AUTHOR");
  const translators = submissionDetails?.authors?.filter(author => author.role === "TRANSLATOR");

  const downloadAllFile = () => {
    const link = document.createElement('a');
    link.href = submissionDetails?.allFilePath;
    link.download = '';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <>
      <div className={`submissioncheck ${isOpen ? 'open' : 'closed'}`}>
        <div className="d-flex mt-3 Submittd_list position-relative">
          {journalLoading ? <LoaderMain/> : null}
          <div className="col-12 px-3 ">

            <div className='b-bottom'>
              <div className="d-flex justify-content-between b-bottom">
                <h3 className='ms-2 pt-1' style={{borderBottom: 'none'}}><Translate contentKey="Submission_Details_Title.Submission_Details"></Translate></h3>
                <button type="button" className="btn-close" onClick={onClose}></button>
              </div>

              <div className="d-flex pt-3 px-3">
                <div className="col-4">
                  <p className="article-head"><Translate contentKey="Submission_Details_Title.Journal_Name"></Translate></p>
                </div>
                <div className="col-8">
                  <div className={`journal-profile color-${submissionDetails.title?.slice(0, 1).toUpperCase()}`}>
                    {submissionDetails?.title?.slice(0, 2).toUpperCase()}
                  </div>
                  <div className="ps-3 ms-3">
                    <p className="article-details m-1">{submissionDetails?.title}</p>
                  </div>
                </div>
              </div>

              <div className="d-flex px-3">
                <div className="col-4">
                  <p className="article-head"><Translate contentKey="Submission_Details_Title.Article_Name"></Translate></p>
                </div>
                <div className="col-8">
                  <p className="article-details">{submissionDetails?.title}</p>
                </div>
              </div>
              <div className="d-flex px-3">
                <div className="col-4">
                  <p className="article-head"><Translate contentKey="Submission_Details_Title.Sub_Title"></Translate></p>
                </div>
                <div className="col-8">
                  <p className="article-details">{submissionDetails?.subTitle ? submissionDetails?.subTitle : '-'}</p>
                </div>
              </div>

              <div className="d-flex px-3">
                <div className="col-4">
                  <p className="article-head"><Translate contentKey="Submission_Details_Title.Submission_Language"></Translate></p>
                </div>
                <div className="col-8">
                  <div className="article-details">
                    {submissionLanguage && submissionLanguage.name && (
                      <p>{submissionLanguage.name}</p>
                    )}
                  </div>
                </div>
              </div>

              <div className="d-flex px-3">
                <div className="col-4">
                  <p className="article-head"><Translate contentKey="Submission_Details_Title.Abstract"></Translate></p>
                </div>
                <div className="col-8">
                  <div className="article-details">
                    {submissionDetails?.description !== '<p><br></p>' && submissionDetails?.description !== null ?
                      <button className="article-btn" onClick={() => setShowModel(true)}><Translate contentKey="Submission_Details_Title.View"></Translate></button>
                      : '-'}
                    <Modal isOpen={showModel} toggle={handleClose} backdrop="static" autoFocus={false}>
                      <div className="modal-content">
                        <div className="modal-header">
                          <div className="d-flex">
                            <div className="line"></div>
                            <h5 className="m-0"><Translate contentKey="Submission_Details_Title.Abstract"></Translate></h5>
                          </div>
                          <button type="button" className="btn-close" onClick={handleClose}></button>
                        </div>
                        <div className="modal-body ps-4">
                          <div dangerouslySetInnerHTML={{__html: submissionDetails?.description}}/>
                        </div>
                        <div className="modal-footer">
                          <button className="btn btn--primary" type="submit" onClick={handleClose}>
                            <Translate contentKey="Submission_Details_Title.CLOSE"></Translate>
                          </button>
                        </div>
                      </div>
                    </Modal>
                  </div>
                </div>
              </div>
            </div>
            <div className='b-bottom pt-3'>
              <div className="d-flex px-3">
                <div className="col-4">
                  <p className="article-head"><Translate contentKey="Submission_Details_Title.Category"></Translate></p>
                </div>
                <div className="col-8">
                  {journals?.categories?.length > 0 ? category : '-'}
                </div>
              </div>
              <div className="d-flex px-3">
                <div className="col-4">
                  <p className="article-head"><Translate contentKey="Submission_Details_Title.Keywords/Tags"></Translate></p>
                </div>
                <div className="col-8">
                  {keywords?.length > 0 ? keyword : '-'}
                </div>
              </div>
              <div className="d-flex px-3">
                <div className="col-4">
                  <p className="article-head"><Translate contentKey="Submission_Details_Title.Uploaded_Files"></Translate></p>
                </div>
                <div className="col-8">
                  <ul className="ps-3 m-0">
                    {submissionDetails?.submissionFiles?.map((file) => (
                      <li className="pt-2" key={file?.id}>
                        <span><b>{file?.fileType?.name}</b> -  </span>
                        <a className="atricle-files article-details" href={file?.fileEndPoint} download={file?.file}>
                          {file?.file}
                        </a>
                      </li>
                    ))}
                    <br/>
                  </ul>

                  <div className="article-details">
                    <button className="article-btn" onClick={downloadAllFile}><Translate contentKey="Submission_Details_Title.Download_All"></Translate></button>
                  </div>
                </div>
              </div>
            </div>

            <div className='b-bottom pt-3'>
              <div className="d-flex px-3">
                <div className="col-4">
                  <p className="article-head"><Translate contentKey="Submission_Details_Title.Contributors"></Translate></p>
                </div>
                <div className="col-8">
                  {authors?.length !== 0 ? (
                    <div className="d-flex">
                      <div className="w-12">
                        <p className="article-head"><Translate contentKey="Submission_Details_Title.Authors:"></Translate></p>
                      </div>
                      <div className="w-88 d-flex flex-wrap">
                        {authors?.map((author) => (
                          <div key={author.id} className='mb-2'>
                            <div
                              className={`journal-profile color-${author && author.firstName ? author.firstName?.slice(0, 2).toUpperCase() : null}`}>
                              {author.firstName?.slice(0, 2).toUpperCase()}
                            </div>
                            <div className="ps-3 ms-3">
                              <span className="article-details m-1 me-3">{author.firstName}{author?.primary ?
                                <span className="error_class">*</span> : ''}</span>
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>
                  ) : null}

                  {translators?.length !== 0 ? (
                    <div className="d-flex">
                      <div className="w-12">
                        <p className="article-head"><Translate contentKey="Submission_Details_Title.Translators:"></Translate></p>
                      </div>
                      <div className="w-88 d-flex flex-wrap">
                        {translators?.map((translator) => (
                          <div key={translator.id} className='mb-2'>
                            <div
                              className={`journal-profile color-${translator && translator.firstName ? translator.firstName?.slice(0, 2).toUpperCase() : null}`}>
                              {translator.firstName?.slice(0, 2).toUpperCase()}
                            </div>
                            <div className="ps-3 ms-3">
                              <span className="article-details m-1 me-3">{translator.firstName}</span>
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>
                  ) : null}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

export default SubmissionDetails;
