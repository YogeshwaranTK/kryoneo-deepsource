import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';
import { capitalize } from 'lodash';
import { Translate } from 'react-jhipster';

const HeaderProfile = () => {

  const account = useAppSelector(state => state.authentication.account);
  const navigate = useNavigate();

  return (
    <div id="profile">
      <div className="d-flex profile-header">
        <div className="journal-profile-header">{account.fullName.slice(0, 2).toUpperCase()}</div>
        <div className="px-2">
          <div className="user-name">{capitalize(account.fullName)}</div>
          <div className="user-email">
            <a>{capitalize(account.email)}</a>
          </div>
        </div>
      </div>
      <div className="py-3 px-2 profile_acc">
        <h6><Translate contentKey="header.manage_account">MANAGE MY ACCOUNT</Translate></h6>

        <ol className="list-unstyled">
          <li>
            <a  onClick={() => navigate('/journal/userdetails')}>
              <span className="pe-3">
                <img src='content/images/profile/user.svg' alt="Not found" />
              </span>
              <Translate contentKey="header.profile">Profile & Account</Translate>
            </a>
          </li>
          <li>
            <a>
              <span className="pe-3">
                <img src='content/images/profile/support.svg' alt="Not found" />
              </span>
              <Translate contentKey="header.chat_support">Chat & Support</Translate>
            </a>
          </li>
        </ol>

        <button className="btn custom-btn" type="button" onClick={() => navigate('/logout')}>
        <Translate contentKey="header.signout">SIGN OUT</Translate>
        </button>
      </div>
    </div>
  );
};

export default HeaderProfile;
