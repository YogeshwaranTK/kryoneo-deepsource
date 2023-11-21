import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {Storage} from 'react-jhipster';
import {useAppDispatch, useAppSelector} from "app/config/store";
import {
  getJournalAccessRole,
  getJournalDetails
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {submissionWorkflowConfig} from "app/config/submission-workflow.config";
import "./top-nav.scss"
import {formatDate} from "app/config/componance-config";
import {truncateString} from "app/config/journals.config";

const TopNav = () => {
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0];
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const journalAccessRole = useAppSelector(state => state.settingsManagement.journalAccessRole);
  const journalAccessSuccess = useAppSelector(state => state.settingsManagement.journalAccessSuccess);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [currentRole, setCurrentRole] = useState(Storage.session.get('role'));

  useEffect(() => {
    dispatch(getJournalDetails(Jo_id));
    dispatch(getJournalAccessRole());
  }, []);

  useEffect(() => {
    if (journalAccessSuccess) {
      const rolePriority = {
        editorialUser: 1,
        author: 2,
        reviewer: 3,
      };

      const sortedRoles = Object.keys(journalAccessRole)
        .filter(role => journalAccessRole[role])
        .sort((a, b) => rolePriority[a] - rolePriority[b]);

      if (sortedRoles.length > 0 && !Storage.session.get('role')) {
        const newRole = sortedRoles[0];

        setCurrentRole(newRole);
        Storage.session.set('role', newRole);
      }
    }
  }, [journalAccessSuccess, journalAccessRole]);

  const [viewEssentialsStatus, setViewEssentialsStatus] = useState(false)

  const handleEssential = () => {
    if (viewEssentialsStatus) {
      setViewEssentialsStatus(false)
    } else {
      setViewEssentialsStatus(true)
    }
  };

  return (
    <>
      <div className='col-12 d-flex top_nav'>
        <a
          className='col-11 top_nav_journal d-flex'
          onClick={handleEssential}
          style={{textDecoration: 'none', paddingRight: '0px', paddingLeft: '0px'}}
        >
          <div className="profile-head-img">
            <div className="journal-profile-copy color-A">
              {journals && journals.title ? journals.title.slice(0, 2).toUpperCase() : ''}
            </div>
          </div>
          <div className="sidebar-head-title ps-1">
            {journals && journals.title ? truncateString(journals.title) : ''}
          </div>
        </a>
        <div className='col-1'>
          <ul className="box">
            <li
              className={`dropdown category`}
              id="defaultDropdown custom-dropdown-help"
              data-bs-toggle="dropdown"
              data-bs-auto-close="true"
              aria-expanded="false"
              style={{cursor: 'pointer'}}
            >
              <img src="content/images/header-image/category-alt.svg"
                   alt="Your Image"/>
            </li>
            <ul aria-labelledby="defaultDropdown" className="dropdown-menu dropdown-menu-end p-0" role="menu">
              <div id="profile">
                <div className="profile_acc profile_acc_lang">
                  <ol className="list-unstyled">
                    <li>
                      <a onClick={() => navigate(`/journal/${parseInt(Jo_id, 10)}/site-settings`)}><FontAwesomeIcon
                        icon="gears" className=""/> Configuration</a>
                    </li>
                    <li>
                      <a
                        onClick={() => navigate(`/journal/${parseInt(Jo_id, 10)}/user-role-settings`)}><FontAwesomeIcon
                        icon="users" className=""/> Users & Roles Settings</a>
                    </li>
                    <li>
                      <a
                        onClick={() => navigate(`/journal/${parseInt(Jo_id, 10)}/developer-settings`)}><FontAwesomeIcon
                        icon="users" className=""/> Developer Settings</a>
                    </li>
                    {(journalAccessRole?.editorialUser && (currentRole === 'reviewer' || currentRole === 'author')) && (
                      <li onClick={() => {
                        setCurrentRole('editorialUser');
                        Storage.session.set('role', 'editorialUser');
                      }}>
                        <a onClick={() => navigate(`/journal/${parseInt(Jo_id, 10)}/submissions?page=1&sort=id,asc`)}>
                          <FontAwesomeIcon icon="users" className=""/> Login as Editorial User
                        </a>
                      </li>
                    )}
                    {(journalAccessRole?.reviewer && currentRole === 'editorialUser') && (
                      <li onClick={() => {
                        setCurrentRole('reviewer');
                        Storage.session.set('role', 'reviewer');
                      }}>
                        <a onClick={() => navigate(`/journal/${parseInt(Jo_id, 10)}/reviewer-dashboard`)}>
                          <FontAwesomeIcon icon="users" className=""/> Login as Reviewer
                        </a>
                      </li>
                    )}
                    {(journalAccessRole?.author && currentRole === 'editorialUser') && (
                      <li onClick={() => {
                        setCurrentRole('author');
                        Storage.session.set('role', 'author');
                      }}>
                        <a onClick={() => navigate(`/journal/${parseInt(Jo_id, 10)}/submissions?page=1&sort=id,asc`)}>
                          <FontAwesomeIcon icon="users" className=""/> Login as Author
                        </a>
                      </li>
                    )}
                  </ol>
                </div>
              </div>
            </ul>
          </ul>
        </div>
      </div>
      {Storage.session.get('role') == 'editorialUser' &&
        <div className='col-12 d-flex top_nav'>
          <div className='col-12 text-end'>
            <ul className="box">
              <li className="box-item"
                  onClick={() => navigate(`/journal/${parseInt(Jo_id, 10)}/submissions?page=1&sort=id,asc`)}>Submissions <span
                className="badge bg-info">4</span>
              </li>
              <li className="box-item"
                  onClick={() => navigate(`/journal/${parseInt(Jo_id, 10)}/reviews?submissionListRequestType=${submissionWorkflowConfig.submissionListPeerReviewType}&page=1&sort=id,asc`)}>Review <span
                className="badge bg-info">4</span>
              </li>
              <li className="box-item"
                  onClick={() => navigate(`/journal/${parseInt(Jo_id, 10)}/copyeditings?submissionListRequestType=${submissionWorkflowConfig.submissionListCopyEditingType}&page=1&sort=id,asc`)}>Copy
                Editing <span className="badge bg-info">4</span>
              </li>
              <li className="box-item  production-item"
                  onClick={() => navigate(`/journal/${parseInt(Jo_id, 10)}/productions?submissionListRequestType=${submissionWorkflowConfig.submissionListProductionType}&page=1&sort=id,asc`)}>Production <span
                className="badge bg-info">4</span>
              </li>
            </ul>
          </div>
        </div>
      }
      {viewEssentialsStatus &&
        <div className="col-12 d-flex ps-2" style={{display: "flex", flexDirection: "column"}}>
          <div className="row bg-light b-bottom " style={{height: "auto"}}>
            <div className="col-4">
              <div style={{display: "flex", marginTop: "5px"}}>
                <p className="article-head">Journal Key: </p>
                <p className="article-details ms-2">{journals.key}</p>
              </div>
            </div>
            <div className="col-4">
              <div style={{display: "flex", marginTop: "5px"}}>
                <p className="article-head">Editor-chief(s): </p>
                <p className="article-details ms-2">{journals.editorChief}</p>
              </div>
            </div>
            <div className="col-4">
              <div style={{display: "flex", marginTop: "5px"}}>
                <p className="article-head">Online ISSN: </p>
                <p className="article-details ms-2">{journals.onlineIssn}</p>
              </div>
            </div>
            <div className="col-4">
              <div style={{display: "flex"}}>
                <p className="article-head">Print ISSN: </p>
                <p className="article-details ms-2">{journals.printIssn}</p>
              </div>
            </div>
            <div className="col-4">
              <div style={{display: "flex"}}>
                <p className="article-head">Created Date: </p>
                <p className="article-details ms-2">{formatDate(journals.createdDate)}</p>
              </div>
            </div>
            <div className="col-4">
              <div style={{display: "flex"}}>
                <p className="article-head">Journal Publishing Status: </p>
                <p className="article-details ms-2">{journals.published ? 'Published' : 'Not Published'}</p>
              </div>
            </div>
          </div>
        </div>
      }
    </>
  );
};

export default TopNav;
