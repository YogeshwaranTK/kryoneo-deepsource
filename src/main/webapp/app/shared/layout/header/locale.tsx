import React from 'react';
import { DropdownItem } from 'reactstrap';
import { NavDropdown } from './menu-components';
import { locales, languages } from 'app/config/translation';
import { isRTL } from 'app/config/translation';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Storage } from 'react-jhipster';
export const LocaleMenu = ({ currentLocale }: { currentLocale: string }) => {
  const dispatch = useAppDispatch();

  const handleLocaleChange = langKey => {
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
    document.querySelector('html').setAttribute('dir', isRTL(langKey) ? 'rtl' : 'ltr');
  };

  {
    /* <NavDropdown icon="global" name={currentLocale ? languages[currentLocale].name : undefined}>
      {locales.map(locale => (
        <DropdownItem key={locale} value={locale} onClick={onClick}>
          {languages[locale].name}
        </DropdownItem>
      ))}
    </NavDropdown> */
  }
  return Object.keys(languages).length > 1 ? (
    <ul className="cus-dropdown-submenu">
      <li>
        {currentLocale ? languages[currentLocale].name : undefined}
        <ul className="cus-dropdown-menu">
          {locales.map(locale => (
            <li key={locale} value={locale}>
              <a onClick={() => handleLocaleChange(locale)}> {languages[locale].name}</a>
            </li>
          ))}
        </ul>
      </li>
    </ul>
  ) : null;
};
