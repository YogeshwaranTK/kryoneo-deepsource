import React from 'react';
import './first-sidebar.scss';
import {Link, useLocation, useParams} from 'react-router-dom';
import {Translate} from "react-jhipster";

const FirstSidebar = () => {
  const location = useLocation();
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]

  return (
    <div id="sidebar">
      <div  id="accordionExample">
        <div className="accordion-item">
          <h2 className="accordion-header" id="headingTwo">
            <button
              className={`accordion-button collapsed ${
                location.pathname.includes('developer-settings') ? 'side_nav_parent_active' : ''}`}
              type="button"
            >
              <Translate contentKey="userManagementSideBar.DeveloperSettings">Developer Settings</Translate>
            </button>
          </h2>
          <div
           >
            <ul className="list-unstyled mb-0">
              <li>
                <Link
                  id="sub-items"
                  to={`/journal/${parseInt(Jo_id, 10)}/developer-settings/email`}
                >
                  <span className="pe-3">
                    <img src='content/images/header-image/setting-gray.svg' alt="Not found"/>
                  </span>
                  <Translate contentKey="userManagementSideBar.EmailSetting">Email Setting</Translate>
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default FirstSidebar;


