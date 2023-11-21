import React, { useEffect, useState } from 'react';
import * as Yup from 'yup';
import { FormikProps, useFormik } from 'formik';
import Select from 'react-select';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import Breadcrumb from "app/shared/breadcrumb/breadcrumb";
import { translate, Translate } from "react-jhipster";
import {
  getCountryList,
  userCreate,
  userGetdata,
  userUpdate
} from "app/modules/administration/user-management/user-management/user-management.reducer";
import { useLocation, useNavigate } from "react-router-dom";
import "./user-management.scss"
import LoaderMain from "app/shared/Loader/loader-main";
import JournalRoleSelect from "app/modules/administration/user-management/user-management/Journal-role-select";


const customStyles = {
  control: (provided) => ({
    ...provided,
    fontSize: '14px',
    borderColor: '#9198B0',
    Color: '#9198B0'
  }),
};

export interface UserCreateModel {
  fullName?: string;
  email?: string;
  addressLine1?: string;
  addressLine2?: string;
  countryId?: any;
  city?: string;
  stateProvince?: string;
  pinCode?: string;
  mobileNumber?: string;
  phoneCode?: string;
  orcid?: string;
  password?: string;
  gender?: string;
  activated?:Boolean;
}

interface RoleSelectModel {
  id: number;
  editorialRole: { value: number; label: string };
  roleType: { value: string; label: string };
  selectedJournal: { value: number; label: string };
}

interface StateData {
  id: number;
  type: string;
  journalName: string;
  UserId: number
}

export const UserManagementCreate = () => {
  const location = useLocation();
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const countryOptions = useAppSelector(state => state.userManagement.countryData);
  const createdUser = useAppSelector(state => state.userManagement.createdUser);
  const loading = useAppSelector(state => state.userManagement.loading);
  const updatedUser = useAppSelector(state => state.userManagement.updatedUser)
  const [country, setCountry] = useState([]);
  const [selectCountry, setSelectCountry] = useState([]);
  const [selectedGender, setSelectedGender] = useState(null);
  const [userdata, setUserdata] = useState<UserCreateModel>({});
  const [errors, setErrors] = useState<{ selectRole_error?: string; selectCountry_error?: string; }>({});
  const emailRegex = /^[A-Z0-9._%+-]{1,64}@[A-Z0-9.-]{1,255}\.[A-Z]{2,}$/i;
  const stateData = location.state as StateData;
  const journalId = stateData?.id;
  const fromModel = stateData?.type;
  const journalName = stateData?.journalName;
  const userId = stateData?.UserId;
  const [isActiveJournalPermission, setIsActiveJournalPermission] = useState(!!journalId);

  const genders = [
    { value: 'MALE', label: 'Male' },
    { value: 'FEMALE', label: 'Female' },
    { value: 'OTHERS', label: 'Others' }
  ];

  useEffect(() => {
    if (userId) {
      userGetdata(userId)
        .then(response => {
          setUserdata(response.data)
        })
        .catch(error => {
          console.error(error);
        });
    }
  }, [userId])

  useEffect(()=>{
    if(updatedUser){
      navigate(`/journal/user-management/user`);
    }
  },[updatedUser])

  useEffect(() => {
    if (userdata) {
      const initialSelectCountry = code_filter.find(option => option.value === userdata.phoneCode);
      setSelectCountry(initialSelectCountry ? [initialSelectCountry] : []);

      const selectedCountry = con_filter.find((option) => option.id === userdata.countryId);
      setCountry(selectedCountry ? [selectedCountry] : []);

      const initialSelectedGender = genders.find((option) => option.value === userdata?.gender);
      setSelectedGender(initialSelectedGender ? [initialSelectedGender] : []);
    }
  }, [userdata]);

  const [roles, setRoles] = useState<RoleSelectModel[]>(journalId !== null && journalId !== undefined ? [
    {
      id: 1,
      editorialRole: { value: 0, label: '' },
      roleType: {
        value: `${fromModel === 'author' ? 'AUTHOR' : fromModel === 'reviewer' ? 'REVIEWER' : 'EDITORIAL_USER'}`,
        label: `${fromModel === 'author' ? 'Author' : fromModel === 'reviewer' ? 'Reviewer' : 'Editorial User'}`
      },
      selectedJournal: { value: journalId, label: journalName }
    }
  ] : [
    {
      id: 1,
      editorialRole: { value: 0, label: '' },
      roleType: { value: '', label: '' },
      selectedJournal: { value: 0, label: '' }
    }
  ]
  );

  const handleChange = () => {
    setIsActiveJournalPermission(!isActiveJournalPermission);
  };



  useEffect(() => {
    dispatch(getCountryList());
  }, []);

  const con_filter = countryOptions?.map(({ iso, name, id, iso3, niceName, numberCode, phoneCode }) => ({
    value: iso,
    label: name,
    id,
    iso3,
    niceName,
    numberCode,
    phoneCode,
  }));
  const code_filter = countryOptions?.map(({ iso, name, id, iso3, niceName, numberCode, phoneCode }) => ({
    iso,
    name,
    id,
    iso3,
    niceName,
    label: phoneCode,
    value: numberCode,
  }));

  const BreadCrumbRoutes = [
    { name: translate('userManagementBreadcrumb.Home'), path: '/journal' },
    { name: translate('userManagementBreadcrumb.Manage'), path: `/journal/user-management/user` },
    { name: !userId ? translate('userManagementCreateUser.CreateUser'):translate('userManagementCreateUser.UpdateUser'), path: '' },
  ];

  const validateForm = () => {
    const errorsValidate: { selectCountry_error?: string; } = {};

    if (country.length === 0) {

      errorsValidate.selectCountry_error = translate('PlaceHolders.Country');
    }
    setErrors(errorsValidate);
    console.log(Object.keys(errorsValidate).length)
    return Object.keys(errorsValidate).length === 0;
  };




  const formik: FormikProps<UserCreateModel> = useFormik<UserCreateModel>({
    enableReinitialize: true,
    initialValues: {
      fullName: userdata?.fullName ? userdata?.fullName : '',
      email: userdata?.email ? userdata?.email : '',
      addressLine1: userdata?.addressLine1 ? userdata?.addressLine1 : '',
      addressLine2: userdata?.addressLine2 ? userdata?.addressLine2 : '',
      countryId: userdata?.countryId ? userdata?.countryId : '',
      city: userdata?.city ? userdata?.city : '',
      stateProvince: userdata?.stateProvince ? userdata?.stateProvince : '',
      pinCode: userdata?.pinCode ? userdata?.pinCode : '',
      phoneCode: userdata?.phoneCode ? userdata?.phoneCode : '',
      mobileNumber: userdata?.mobileNumber ? userdata?.mobileNumber : '',
      orcid: userdata?.orcid ? userdata?.orcid : '',
      password: ''
    },
    validationSchema: Yup.object({
      fullName: Yup.string().required(translate('inputValidations.fullNameRequired')),
      email: Yup.string()
        .required(translate('inputValidations.emailRequired'))
        .max(320, translate('inputValidations.MaxLen320'))
        .matches(emailRegex, translate('inputValidations.emailInvalid')),
      mobileNumber: Yup.string().required(translate('inputValidations.MobileNumberRequired'))
        .matches(/^\d+$/, translate('inputValidations.allowedOnlyNumbers')),
      addressLine1: Yup.string(),
      addressLine2: Yup.string(),
      countryId: Yup.string(),
      city: Yup.string(),
      stateProvince: Yup.string(),
      pinCode: Yup.string().matches(/^\d+$/, translate('inputValidations.allowedOnlyNumbers')),
      phoneCode: Yup.string(),
      orcid: Yup.string(),
      password: userId
        ? Yup.string().notRequired()
        : Yup.string()
          .required('Password is required')
          .min(8, 'Password must be 8-12 length')
          .max(12, 'Password must be 8-12 length')
          .matches(/[0-9]/, 'Password must contain at least 1 digit')
          .matches(/[a-z]/, 'Password must contain at least 1 lowercase letter')
          .matches(/[A-Z]/, 'Password must contain at least 1 uppercase letter')
          .matches(/[^\w]/, 'Password must contain at least 1 special character'),
    }),

    onSubmit: handleSubmit,
  });

  async function handleSubmit(e) {
    if (validateForm()) {
      if (userId) {
        e['id'] = userId
      }
      e['countryId'] = country[0].id;
      e['phoneCode'] = selectCountry[0].value;
      console.log(selectedGender)

      if (selectedGender.length !== 0  && selectedGender !== null &&  selectedGender[0].value !== null ) {
        if (selectedGender[0].value !== '') {
          e['gender'] = selectedGender[0].value;
        }
      }

      if (userId) {
        const updatedData = { ...e };
        delete updatedData.password;
        updatedData['activated'] = userdata?.activated
        dispatch(userUpdate(updatedData));

      } else {
        e['roles'] = roles.map((item) => {
          if (item.roleType.value === 'EDITORIAL_USER') {
            return {
              roleId: item.editorialRole.value,
              journalId: item.selectedJournal.value,
              roleType: 'EDITORIAL_USER',
            };
          } else {
            return {
              journalId: item.selectedJournal.value,
              roleType: item.roleType.value,
            };
          }
        });
        dispatch(userCreate(e));
      }
    }
  }

  useEffect(() => {
    if (createdUser) {
      if (journalId) {
        navigate(`/journal/${journalId}/user-role-settings`);
      } else {
        navigate('/journal/user-management');
      }
    }
  }, [createdUser]);

  const handleCancel = () => {
    if (journalId) {
      navigate(`/journal/${journalId}/user-role-settings`);
    } else {
      navigate('/journal/user-management');
    }
  }

  return (
    <>
      <div className="pt-2">
        <Breadcrumb props={BreadCrumbRoutes} />
      </div>
      <div className="b-bottom">
        <div className="py-2 pb-3">
          <div className="d-flex">
            <div className="line"></div>
            <h6 className="heading mb-0">
              {!userId ?
              <Translate contentKey="userManagementCreateUser.CreateUser">Create
              User</Translate>:
              <Translate contentKey="userManagementCreateUser.UpdateUser">Update User</Translate>
              }
              </h6>
          </div>
          <p className="title-description">
            <Translate contentKey="userManagementCreateUser.FillMandatoryDescription">Fill the mandatory fields
              mentioned below</Translate>
          </p>
        </div>
      </div>
      <form onSubmit={formik.handleSubmit} className='mb-4'>
        <div className='col-12'>
          <div className="position-relative pb-3">
            {loading ? <LoaderMain /> : null}
            <div className="row mt-3">
              <div className="col-4 pb-3">
                <label className="form-label">
                  <Translate contentKey="inputFields.FullName">Full Name</Translate>
                  <span className="error_class">*</span>
                </label>
                <input
                  id="fullName"
                  name="fullName"
                  placeholder={translate('PlaceHolders.fullName')}
                  data-cy="fullName"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('fullName')}
                />
                {formik.touched.fullName && formik.errors.fullName ?
                  <div className="error_class">{formik.errors.fullName}</div> : null}
              </div>
              <div className="col-4 pb-3">
                <label className="form-label">
                  <Translate contentKey="inputFields.Email">Email</Translate>
                  <span className="error_class">*</span>
                </label>
                <input
                  id="email"
                  name="email"
                  placeholder={translate('PlaceHolders.Email')}
                  data-cy="email"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('email')}
                />
                {formik.touched.email && formik.errors.email ?
                  <div className="error_class">{formik.errors.email}</div> : null}
              </div>
              <div className="col-4 pb-3">
                <label id="usernameLabel" className="form-label">
                  <Translate contentKey="inputFields.Gender">Gender</Translate>
                </label>
                <Select
                  styles={customStyles}
                  placeholder={translate('PlaceHolders.Gender')}
                  options={genders}
                  value={selectedGender}
                  onChange={selectedOption => {
                    setSelectedGender([selectedOption]);
                  }}
                />
              </div>
              <div className="col-4 pb-3">
                <label className="form-label">
                  <Translate contentKey="inputFields.AddressLineOne">Address Line 1</Translate>
                </label>
                <input
                  id="addressLine1"
                  name="addressLine1"
                  placeholder={translate('PlaceHolders.AddressLineOne')}
                  data-cy="addressLine1"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('addressLine1')}

                />
              </div>
              <div className="col-4 pb-3">
                <label className="form-label">
                  <Translate contentKey="inputFields.AddressLineTwo">Address Line 2</Translate>
                </label>
                <input
                  id="addressLine2"
                  name="addressLine2"
                  placeholder={translate('PlaceHolders.AddressLineTwo')}
                  data-cy="addressLine2"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('addressLine2')}
                />
              </div>

              <div className="col-4 pb-3">
                <label id="usernameLabel" className="form-label">
                  <Translate contentKey="inputFields.Country">Country</Translate>
                  <span className="error_class">*</span>
                </label>
                <Select
                  options={con_filter}
                  styles={customStyles}
                  value={country.find(option => option) || ''}
                  placeholder={translate('PlaceHolders.Country')}
                  onChange={selectedOption => {
                    setCountry([selectedOption]);
                    const value_filter = [selectedOption].map(({
                      value,
                      label,
                      id,
                      niceName,
                      numberCode,
                      phoneCode
                    }) => ({
                      iso: value,
                      name: label,
                      id,
                      niceName,
                      label: phoneCode,
                      value: numberCode,
                    }));
                    setSelectCountry(value_filter);
                    delete errors.selectCountry_error;
                  }}
                />
                {errors.selectCountry_error && <span className="error">{errors.selectCountry_error}</span>}
              </div>
              <div className="col-4 pb-3">
                <label className="form-label">
                  <Translate contentKey="inputFields.City">City</Translate>
                </label>
                <input
                  id="city"
                  name="city"
                  placeholder={translate('PlaceHolders.City')}
                  data-cy="city"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('city')}
                />
              </div>
              <div className="col-4 pb-3">
                <div className="row">
                  <div className="col-8 pe-0">
                    <label className="form-label">
                      <Translate contentKey="inputFields.State">State</Translate>
                    </label>
                    <input
                      id="stateProvince"
                      name="stateProvince"
                      placeholder={translate('PlaceHolders.State')}
                      data-cy="stateProvince"
                      type="text"
                      className="form-control"
                      {...formik.getFieldProps('stateProvince')}
                    />
                  </div>
                  <div className="col-4 ps-1">
                    <label className="form-label">
                      <Translate contentKey="inputFields.PinCode">Pin Code</Translate>
                    </label>
                    <input
                      id="pinCode"
                      name="pinCode"
                      placeholder={translate('PlaceHolders.PinCode')}
                      data-cy="pinCode"
                      type="text"
                      className="form-control"
                      {...formik.getFieldProps('pinCode')}
                    />
                    {formik.touched.pinCode && formik.errors.pinCode ?
                      <div className="error_class">{formik.errors.pinCode}</div> : null}
                  </div>
                </div>
              </div>

              <div className="col-4 pb-3">
                <label id="usernameLabel" className="form-label me-3">
                  <Translate contentKey="inputFields.MobileNumber">Mobile Number</Translate>
                  <span className="error_class">*</span>
                </label>
                <div className="row">
                  <div className="col-4 pe-0">
                    <Select
                      defaultValue={selectCountry[0]}
                      options={code_filter}
                      styles={customStyles}
                      value={selectCountry.find(option => option) || ''}
                      placeholder={translate('PlaceHolders.PostCode')}
                      onChange={selectedOption => setSelectCountry([selectedOption])} />

                  </div>
                  <div className="col-8 ps-1">
                    <input
                      name="mobileNumber"
                      id="mobileNumber"
                      placeholder={translate('PlaceHolders.MobileNumber')}
                      data-cy="mobileNumber"
                      type="text"
                      className="form-control "
                      {...formik.getFieldProps('mobileNumber')}
                    />
                    {formik.touched.mobileNumber && formik.errors.mobileNumber ?
                      <div className="error_class">{formik.errors.mobileNumber}</div> : null}
                  </div>
                </div>
              </div>
              <div className="col-4 pb-3">
                <label className="form-label">
                  Enter ORCID
                </label>
                <input
                  id="orcid"
                  name="orcid"
                  placeholder='Enter Your ORCID'
                  data-cy="orcid"
                  type="text"
                  className="form-control"
                  {...formik.getFieldProps('orcid')}
                />
              </div>
              {!userId ?
                <div className="col-4 pb-3">
                  <label className="form-label">
                    User Password <span className="error_class">*</span>
                  </label>
                  <input
                    id="password"
                    name="password"
                    placeholder='Enter User Password'
                    data-cy="password"
                    type="text"
                    className="form-control"
                    {...formik.getFieldProps('password')}
                  />
                  {formik.touched.password && formik.errors.password ?
                    <div className="error_class">{formik.errors.password}</div> : null}
                </div> : ''}
              {!userId ?
                <div className="col-12  mb-2 mt-3 toggle-padding d-flex align-items-center justify-content-between">
                  <div>
                    <label id="usernameLabel" className="form-label me-2">
                      Do you want to give access to the Journals?
                    </label>
                    <div className="form-check form-check-inline">
                      <input
                        className="form-check-input"
                        type="radio"
                        name="accessJournals"
                        id="noRadio"
                        value="no"
                        checked={isActiveJournalPermission === false}
                        onChange={handleChange}
                        disabled={!!journalId}
                      />
                      <label className="form-check-label" htmlFor="noRadio">
                        <Translate contentKey="no">No</Translate>
                      </label>
                    </div>
                    <div className="form-check form-check-inline">
                      <input
                        className="form-check-input"
                        type="radio"
                        name="accessJournals"
                        id="yesRadio"
                        value="yes"
                        checked={isActiveJournalPermission === true}
                        onChange={handleChange}
                      />
                      <label className="form-check-label" htmlFor="yesRadio">
                        <Translate contentKey="yes">Yes</Translate>
                      </label>
                    </div>
                  </div>
                </div> : ''}
              {isActiveJournalPermission && (
                <JournalRoleSelect roles={roles} setRoles={setRoles} journalId={journalId} />
              )}
            </div>
          </div>
        </div>
        <div className="d-flex b-top pt-3">
          <div className="pe-2 m-top">
            <button className="btn custom-btn" type="submit" onClick={() => validateForm()}>
              {!userId ? <Translate contentKey="buttons.create">CREATE</Translate> :
                <Translate contentKey="buttons.update">UPDATE</Translate>
              }
            </button>
          </div>
          <div className="pe-2 m-top">
            <button className="btn custom-btn-secondary" type="button" onClick={handleCancel}>
              <Translate contentKey="buttons.cancel">CANCEL</Translate>
            </button>
          </div>
        </div>
      </form>
    </>
  );
};

export default UserManagementCreate;


