
import { Link, useNavigate } from 'react-router-dom';
import React, { useState, useEffect, useContext } from 'react';

import { capitalize } from 'lodash';
import { Storage } from 'react-jhipster';
import { locales, languages } from 'app/config/translation';
import { isRTL } from 'app/config/translation';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { setLocale } from 'app/shared/reducers/locale';
import { CurrentUserContext, CurrentUserContextType } from './ThemeContext';

import { Translate } from 'react-jhipster';
export const HeaderProfile = ({ currentLocale }: { currentLocale: string }) => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [context, setContext] = useContext(CurrentUserContext);

  // const account = useAppSelector(state => state.authentication.account);
  // const userPermissions = account?.administrationPermissions;

  const handleLocaleChange = langKey => {
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
    setContext(langKey);
    document.querySelector('html').setAttribute('dir', isRTL(langKey) ? 'rtl' : 'ltr');
  };
  return (
    <div id="profile">
      <div className="profile_acc profile_acc_lang">
        <ol className="list-unstyled">
          {/*{userPermissions?.includes('ADMIN_SETTING') ?*/}
            <li>
              <a onClick={() => navigate('/journal/user-management/user')}>Manage</a>
            </li>
            {/*:null}*/}
          <li>

            <ul className="cus-dropdown-submenu ps-0">
              <li>
                <a>
                  {currentLocale ? languages[currentLocale].name : ''}</a>
                <span className="pe-2 ">
                  <FontAwesomeIcon icon="angle-right" />
                </span>
                <ul className="dropdown-menu cus-dropdown-menu p-0">
                  {locales.map(locale => (
                    <li key={locale} value={locale}>
                      <a onClick={() => handleLocaleChange(locale)}> {languages[locale].name}</a>
                    </li>
                  ))}
                </ul>
              </li>
            </ul>

          </li>
        </ol>
      </div>
    </div>
  );
};
export default HeaderProfile;
