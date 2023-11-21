import React from 'react';
import './manage-sidebar.scss';
import {Link, useLocation} from 'react-router-dom';

const ManageSidebar = () => {
  const location = useLocation();

  function isActiveRoute(path: string): boolean {
    return location.pathname.startsWith(path);
  }

  return (
    <div id="sidebar">
      <div className="accordion" id="accordionExample">
        <div className="accordion-item">
          <div
            id="collapseFour"
            className={`accordion-collapse collapse ${
              location.pathname.includes('user') ? 'show' : 'show'}`}
            aria-labelledby="headingFour"
            data-bs-parent="#accordionExample"
          >
            <ul className="list-unstyled mb-0">
              <li>
                <Link
                  id="sub-items"
                  to="/journal/user-management/user"
                  className={isActiveRoute(`/journal/user-management/user`) ? 'active' : ''}
                >
                  <span className="pe-3">
                    <img src='content/images/header-image/user.svg' alt="Not found"/>
                  </span>
                  Users
                </Link>
              </li>
            </ul>
          </div>
        </div>

        <div className="accordion-item">
          <div
            id="collapseOne"
            className={`accordion-collapse collapse ${
              location.pathname.includes('user') ? 'show' : 'show'}`}
            aria-labelledby="collapseOne"
            data-bs-parent="#accordionExample"
          >
            <ul className="list-unstyled mb-0">
              <li>
                <Link
                  id="sub-items"
                  to="/journal/user-management/developer-settings/email"
                  className={(isActiveRoute(`/journal/user-management/developer-settings/email`) || isActiveRoute(`/journal/user-management/developer-settings`)) ? 'active' : ''}
                >
                  <span className="pe-3">
                    <img src='content/images/header-image/user.svg' alt="Not found"/>
                  </span>
                  Email Settings
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManageSidebar;


