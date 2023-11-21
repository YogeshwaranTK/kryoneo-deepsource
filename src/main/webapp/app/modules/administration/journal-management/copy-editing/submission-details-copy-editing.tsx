import React, {useState} from "react";
import {useAppSelector} from "app/config/store";
import {Modal} from "reactstrap";
import LoaderMain from "app/shared/Loader/loader-main";
import {useLocation} from "react-router-dom";


const CopyEditingSubmissionDetails = (props) => {
  const {submissionId} = props
  const isOpen = props.isOpen
  const onClose = props.onClose
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const articleDetails = useAppSelector(state => state.articleDetailManagement.articleData);
  const articleLoading = useAppSelector(state => state.articleDetailManagement.loading);
  const journalLoading = useAppSelector(state => state.settingsManagement.loading);
  const {submissionLanguage, categories, keywords, id} = articleDetails;
  const [showModel, setShowModel] = useState(false);
  const location = useLocation()

  const state = location.state as { id: number } | undefined;

  const loaderIsTrue = articleLoading || journalLoading;

  const handleClose = () => {
    setShowModel(false);
  };

  const category = journals?.categories?.map((item, index) => <span key={index}
                                                                    className="article-details-tages">{item?.name}</span>);

  const keyword = keywords?.map((item, index) => {
    if (index === keywords.length - 1) {
      return <span key={index} className="article-details-tages">{item}</span>
    } else {
      return <span key={index} className="article-details-tages">{item}</span>
    }
  })

  const authors = articleDetails.authors?.filter(author => author.role === "AUTHOR");
  const translators = articleDetails.authors?.filter(author => author.role === "TRANSLATOR");

  const downloadAllFile = () => {
    const link = document.createElement('a');
    link.href = articleDetails?.allFilePath;
    link.download = '';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };


  return (
    <>

    <div className={`submissioncheck ${isOpen ? 'open' : 'closed'}`}>
      <div className="d-flex mt-3 Submittd_list position-relative">
        {(loaderIsTrue) ? <LoaderMain/> : null}
        <div className="col-12 px-3 " style={{borderRight: '1px solid #d9d9d9'}}>

          <div className='b-bottom '>
          <div className="d-flex justify-content-between">
            <h3 className='ms-2'>Submission Details</h3>
            <button type="button" className="btn-close" onClick={onClose}></button>
            </div>
            <div className="d-flex px-3">

              <div className="col-4">
                <p className="article-head">Journaladse Name</p>
              </div>
              <div className="col-8">
                <div className={`journal-profile color-${articleDetails.journalName?.slice(0, 1).toUpperCase()}`}>
                  {articleDetails.journalName?.slice(0, 2).toUpperCase()}
                </div>
                <div className="ps-3 ms-3">
                  <p className="article-details m-1">{articleDetails.journalName}</p>
                </div>
              </div>
            </div>

            <div className="d-flex px-3">
              <div className="col-4">
                <p className="article-head">Article Name</p>
              </div>
              <div className="col-8">
                <p className="article-details">{articleDetails.title}</p>
              </div>
            </div>
            <div className="d-flex px-3">
              <div className="col-4">
                <p className="article-head">Sub Title</p>
              </div>
              <div className="col-8">
                <p className="article-details">{articleDetails?.subTitle ? articleDetails.subTitle : '-'}</p>
              </div>
            </div>

            <div className="d-flex px-3">
              <div className="col-4">
                <p className="article-head">Submission Language</p>
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
                <p className="article-head">Abstract</p>
              </div>
              <div className="col-8">
                <div className="article-details">
                  {articleDetails?.description !== '<p><br></p>' && articleDetails?.description !== null ?
                    <button className="article-btn" onClick={() => setShowModel(true)}>View</button>
                    : '-'}


                  <Modal isOpen={showModel} toggle={handleClose} backdrop="static" autoFocus={false}>
                    <div className="modal-content">
                      <div className="modal-header">
                        <div className="d-flex">
                          <div className="line"></div>
                          <h5 className="m-0">Abstract</h5>
                        </div>
                        <button type="button" className="btn-close" onClick={handleClose}></button>
                      </div>
                      <div className="modal-body ps-4">
                        <div dangerouslySetInnerHTML={{__html: articleDetails.description}}/>
                      </div>
                      <div className="modal-footer">
                        <button className="btn btn--primary" type="submit" onClick={handleClose}>
                          CLOSE
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
                <p className="article-head">Category</p>
              </div>
              <div className="col-8">
                {journals?.categories?.length > 0 ? category : '-'}
              </div>
            </div>
            <div className="d-flex px-3">
              <div className="col-4">
                <p className="article-head">Keywords/Tags</p>
              </div>
              <div className="col-8">
                {keywords?.length > 0 ? keyword : '-'}
              </div>
            </div>
            <div className="d-flex px-3">
              <div className="col-4">
                <p className="article-head">Uploaded Files</p>
              </div>
              <div className="col-8">
                <ul className="ps-3 m-0">
                  {articleDetails.files?.map((file) => (
                    <li className="pt-2" key={file?.id}>
                      <span><b>{file?.submissionArticleTypeName}</b> -  </span>
                      <a className="atricle-files article-details" href={file?.filePath} download={file?.fileName}>
                        {file?.fileName}
                      </a>
                    </li>
                  ))}
                  <br/>
                </ul>

                <div className="article-details">
                  <button className="article-btn" onClick={downloadAllFile}>Download All</button>
                </div>
              </div>
            </div>
          </div>

          <div className='b-bottom pt-3'>
            <div className="d-flex px-3">
              <div className="col-4">
                <p className="article-head">Contributors</p>
              </div>
              <div className="col-8">
                {authors?.length !== 0 ? (
                  <div className="d-flex">
                    <div className="w-12">
                      <p className="article-head">Authors:</p>
                    </div>
                    <div className="w-88 d-flex flex-wrap">
                      {authors?.map((author, index) => (
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
                      <p className="article-head">Translators:</p>
                    </div>
                    <div className="w-88 d-flex flex-wrap">
                      {translators?.map((translator, index) => (
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
        {/*<ProductionButtonPermissions onTabChange={onTabChange}/>*/}
      </div>
      </div>
    </>
  )
}

export default CopyEditingSubmissionDetails;
