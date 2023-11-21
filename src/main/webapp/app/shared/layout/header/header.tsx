import './header.scss';
import React, {useEffect, useState} from 'react';
import { Storage, translate } from 'react-jhipster';
import { isRTL } from 'app/config/translation';
import {useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';
import HeaderProfile from './header-profile';
import Locallang from './localelang';
import { Translate } from 'react-jhipster';
import NotificationComponent from "app/shared/layout/Notification/Notification";

export interface IHeaderProps {
  isAuthenticated: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
}

const Header = (props: IHeaderProps) => {
  useEffect(() => document.querySelector('html').setAttribute('dir', isRTL(Storage.session.get('locale')) ? 'rtl' : 'ltr'));
  const account = useAppSelector(state => state.authentication.account);
  const navigate = useNavigate();
  const [isNotificationOpen, setNotificationOpen] = useState(false);
  const notifications = useAppSelector(state => state.notificationManagement.notifications);
  const toggleNotification = () => {
    setNotificationOpen(!isNotificationOpen);
  };

  return (
    <>
    <NotificationComponent isOpen={isNotificationOpen} onClose={toggleNotification}/>
    <div className={isNotificationOpen?'overlay':''} onClick={toggleNotification}></div>
    <div id="app-header">
      <nav className="container-fluid d-flex">
            <div className="col-2">
              <a className="navbar-brand">
                <img src='content/images/kryoneo-logo.png' className="header-icon" alt="" onClick={() => navigate('/journal')} />
            </a>
            </div> <div className="p-2 me-auto"></div>
            <div className="p-2 search-input d-none">
              <input className="form-control me-2" type="text" placeholder={translate('search')} />
              <img src='content/images/header-image/search.svg' className="icon" alt="" />
            </div>
            <div className="dropdown d-none" id="custom-dropdown-help">
              <div data-bs-toggle="dropdown" aria-expanded="false">
                <div className="image-padding">
                  <img src='content/images/header-image/help.svg' className="header-icon" alt="" />
                </div>
              </div>
              <ul className="dropdown-menu dropdown-menu-end p-0" role="menu">
                <li>
                  <a className="dropdown-item first-item" id="borderline">
                    <span><Translate contentKey="header.site_map">Site Map</Translate></span>
                  </a>
                </li>
                <li>
                  <a className="dropdown-item" id="borderline">
                  <Translate contentKey="header.fqa">FAQ</Translate>
                  </a>
                </li>
                <li>
                  <a className="dropdown-item last-item">
                    <Translate contentKey="header.contact_us">Contact Us</Translate></a>
                </li>
              </ul>
            </div>
            <div className="dropdown" id="custom-dropdown-help">
              <div data-bs-toggle="dropdown" aria-expanded="false" data-bs-auto-close="true">
                <div className="image-padding">
                  <img src='content/images/header-image/setting.svg' className="header-icon" alt="" />
                </div>
              </div>
              <ul className="dropdown-menu dropdown-menu-end p-0" role="menu">
                <Locallang currentLocale={props.currentLocale} />
              </ul>
            </div>
            <div className="image-padding position-relative header_notification" onClick={toggleNotification}>
              <img src='content/images/header-image/bell.svg' className="header-icon" alt="" />
              <span className="position-absolute translate-middle rounded-circle">
                {notifications?.length}
                <span className="visually-hidden">New alerts</span>
              </span>
            </div>
            <div className="dropdown" id="custom-dropdown-help">
              <div data-bs-toggle="dropdown" aria-expanded="false" data-bs-auto-close="true">
                <div className=" logout_li">
                  <div className="journal-profile-header">{account.fullName.slice(0, 2).toUpperCase()}</div>
                </div>
              </div>
              <div className="dropdown-menu profile-dropdown dropdown-menu-end p-0" role="menu">
                <HeaderProfile />
              </div>
            </div>
      </nav>
    </div>
    </>
  );
};

export default Header;
