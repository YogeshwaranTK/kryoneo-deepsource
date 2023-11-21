import React, { useEffect, useState } from 'react';
import './sidebar.scss';
import {Link, useLocation, useNavigate, useParams} from 'react-router-dom';
import { Translate } from 'react-jhipster';
import {useAppDispatch, useAppSelector} from "app/config/store";
import {  getJournalDetails} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";

const Sidebar = () => {
  const location = useLocation();
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  // const account = useAppSelector(state => state.authentication.account);
  // const administrationPermissions = account?.administrationPermissions;

  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  function isActiveRoute(path: string): boolean {
    return location.pathname.startsWith(path);
  }

  useEffect(() => {
    dispatch(getJournalDetails(Jo_id));
  },[]);

  const journaltitle = (e) =>{
    if (e.length > 18) {
      return e.slice(0, 18) + '...';
    }else{
      return e
    }
  }

  return (
    <div id="sidebar" className="">
      <div className="sidebar-head ps-3 pt-1 pt-3 custom_he_sidebar">
        <div className="profile-head-img">
          <div className="journal-profile color-A">
             { journals && journals.title ? journals.title.slice(0, 2).toUpperCase() : '' }
          </div>
        </div>
        <div className="sidebar-head-title  ps-5" onClick={()=>{navigate(`/journal/${parseInt(Jo_id, 10)}`)}}>
           { journals && journals.title ? journaltitle(journals.title) : '' }
         </div>
      </div>
      <div className="accordion d-none" id="accordionExample">
        <div className="accordion-item">
          <h2 className="accordion-header" id="headingOne">
            <button
              className={`accordion-button collapsed ${
                location.pathname.includes('journal-summary') ||
                location.pathname.includes('publishing-details') ||
                location.pathname.includes('guidelines') ||
                location.pathname.includes('category') ? 'side_nav_parent_active' : '' }`}
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#collapseOne"
              aria-expanded="false"
              aria-controls="collapseOne"
            >
            Journal Settings
            </button>
          </h2>

          <div
            id="collapseOne"
            className={`accordion-collapse collapse ${
              location.pathname.includes('journal-summary') ||
              location.pathname.includes('publishing-details') ||
              location.pathname.includes('guidelines') ||
              location.pathname.includes('category') ? 'show' : 'show' }`}
            aria-labelledby="headingOne"
            data-bs-parent="#accordionExample"
          >
            <ul className="list-unstyled mb-0">
              <li>
                <Link
                  id="sub-items"
                  to={`/journal/${Jo_id}/publishing-details`}
                  className={isActiveRoute(`/journal/${Jo_id}/publishing-details`) ? 'active' : ''}
                >
                  <span className="pe-3">
                    <img src='content/images/header-image/publish.svg' alt="Not found" />
                  </span>
                  <Translate contentKey="sidemenu.journal.publishing_details">Publishing Details</Translate>
                </Link>
              </li>
              <li>
                <Link
                  id="sub-items"
                  to={`/journal/${Jo_id}/journal-summary`}
                  className={isActiveRoute(`/journal/${Jo_id}/journal-summary`) ? 'active' : ''}
                >
                  <span className="pe-3">
                    <img src='content/images/header-image/summary.svg' alt="Not found" />
                  </span>
                  <Translate contentKey="sidemenu.journal.journal_summary">Journal Summary</Translate>
                </Link>
              </li>
              <li>
                <Link
                  id="sub-items"
                  to={`/journal/${Jo_id}/guidelines`}
                  className={isActiveRoute(`/journal/${Jo_id}/guidelines`) ? 'active' : ''}
                >
                  {' '}
                  <span className="pe-3">
                    <img className='img-custom-width' src='content/images/header-image/guideline.svg' alt="Not found" />
                  </span>{' '}
                  <Translate contentKey="sidemenu.journal.guidelines">Guidelines</Translate>
                </Link>
              </li>
              <li
                // className= {administrationPermissions?.includes('READ_ANY_JOURNAL') || administrationPermissions?.includes('WRITE_ANY_JOURNAL')? '':'d-none'}
              >
                <Link
                  id="sub-items"
                  to={`/journal/${Jo_id}/category`}
                  className={isActiveRoute(`/journal/${Jo_id}/category`) ? 'active' : ''}
                >
                  <span className="pe-3">
                    <img className='img-custom-width' src='content/images/header-image/category.svg' alt="Not found" />
                  </span>{' '}
                  <Translate contentKey="sidemenu.journal.category">Category</Translate>
                </Link>
              </li>
            </ul>
          </div>
        </div>

        <div className="accordion-item">
          <h2 className="accordion-header" id="headingFour">
            <button
              className={`accordion-button collapsed ${
                location.pathname.includes('user') || location.pathname.includes('group') ? 'side_nav_parent_active' : ''  }`}
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#collapseFour"
              aria-expanded="false"
              aria-controls="collapseFour"
            >
              <Translate contentKey="sidemenu.contributors.contributors">Contributors</Translate>
            </button>
          </h2>
          <div
            id="collapseFour"
            className={`accordion-collapse collapse ${
              location.pathname.includes('user') || location.pathname.includes('group') ? 'show' : 'show'  }`}
            aria-labelledby="headingFour"
            data-bs-parent="#accordionExample"
          >
            <ul className="list-unstyled mb-0">
              <li>
                <Link
                  id="sub-items"
                  to={`/journal/${Jo_id}/contributor/user`}
                  className={isActiveRoute(`/journal/${Jo_id}/contributor/user`) ? 'active' : ''}
                >
                  <span className="pe-3">
                    <img src='content/images/header-image/user.svg' alt="Not found" />
                  </span>
                   Users
                </Link>
              </li>

              <li>
                <Link
                  id="sub-items"
                  to={`/journal/${Jo_id}/contributor/group`}
                  className={isActiveRoute(`/journal/${Jo_id}/contributor/group`) ? 'active' : ''}
                >
                  <span className="pe-3">
                    <img className='img-custom-width' src='content/images/header-image/group.svg' alt="Not found" />
                  </span>
                  Groups
                </Link>
              </li>

            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
