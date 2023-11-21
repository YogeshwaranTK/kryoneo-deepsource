import React, {useEffect, useState} from 'react';
import * as Yup from 'yup';
import {useFormik, FormikProps} from 'formik';
import Select from 'react-select';
import {useAppDispatch, useAppSelector} from 'app/config/store';
import Breadcrumb from "app/shared/breadcrumb/breadcrumb";
import {log, translate, Translate} from "react-jhipster";
import {
  getCountryList,
  getRoles,
  userGet,
  userUpdate,
  userPasswordRest
} from "app/modules/administration/user-management/user-management/user-management.reducer";
import {Link, useLocation, useNavigate} from "react-router-dom";
import "./user-management.scss"
import LoaderMain from "app/shared/Loader/loader-main";
import UserManagementResetPasswordDialog from "app/modules/administration/user-management/user-management/user-reaset-password-dialog";
import Sweetalert from "app/config/sweet-alert"

const customStyles = {
  control: (provided) => ({
    ...provided,
    fontSize: '14px',
    borderColor: '#9198B0',
  }),
};

export interface UserUpdateModel {
  fullName: string;
  email: string;
  addressLine1: string;
  addressLine2: string;
  countryId: any;
  city: string;
  stateProvince: string;
  phoneCode: string;
  mobileNumber: string;
  pinCode: string;
  orcid: string;
}

export const UserManagementUpdate = () => {

  const location = useLocation();
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const countryOptions = useAppSelector(state => state.userManagement.countryData);
  const loader = useAppSelector(state => state.userManagement.loading);
  const [country, setCountry] = useState([]);
  const [selectCountry, setSelectCountry] = useState([]);
  const [userData, setUserData] = useState(useAppSelector(state => state.userManagement.user))
  const [selectRole, setSelectRole] = useState<any[]>([]);
  const userRole = useAppSelector(state => state.roleManagement.roleListDetail);
  const [disableButton, setDisableButton] = useState(false)
  const [isActive, setIsActive] = useState(false);
  const [selectedGender, setSelectedGender] = useState(null);
  const [roleId, setRoleId] = useState(null)
  const [MobNumber, setMobNumber] = useState('');
  const emailRegex = /^[A-Z0-9._%+-]{1,64}@[A-Z0-9.-]{1,255}\.[A-Z]{2,}$/i;
  const [CusLoader, setCusLoader] = useState(false);
  const [resetPassModal, setResetPassModal] = useState(false);
  // const resetPassData = useAppSelector(state => state.userManagement.resetpassdata);
  const [InicialRole, setInicialRole] = useState([]);

  const handleChange = () => {
    setIsActive(!isActive);
  };
  const [errors, setErrors] = useState<{  selectCountry_error?: string; }>({});

  const genders = [
    {value: 'MALE', label: 'Male'},
    {value: 'FEMALE', label: 'Female'},
    {value: 'OTHERS', label: 'Others'}
  ]

  const Roles = userRole?.map((values) => ({
    value: values.id,
    label: values.name,
  }));


  useEffect(() => {

    setCusLoader(true)
    if (typeof location.state === 'string') {
      userGet(location.state)
        .then(response => {
          setCusLoader(false)
          setUserData(response.data)
          setIsActive(response.data.activated);
        })
        .catch(error => {
          console.error(error);
        });
    }
  }, [location.state]);



  useEffect(() => {
    if (userData) {
      const initialSelectCountry = code_filter.find(option => option.value === userData.phoneCode);
      setSelectCountry(initialSelectCountry ? [initialSelectCountry] : []);

      const selectedCountry = con_filter.find((option) => option.id === userData.countryId);
      setCountry(selectedCountry ? [selectedCountry] : []);
      setSelectRole(userData?.roles.map((values) => ({
        value: values.id,
        label: values.name,
      })))

      setInicialRole(userData?.roles?.map((values) => ({
        value: values.id,
        label: values.name,
      })))

      const initialSelectedgender = genders.find((option) => option.value === userData.gender);
      setSelectedGender(initialSelectedgender ? [initialSelectedgender] : []);


      if (userData?.roleId) {
        const initialSelectedRole = Roles.find(role => role.value === userData.roleId);
        setSelectRole(initialSelectedRole ? [initialSelectedRole] : []);
      }
      setMobNumber(userData.mobileNumber)

    }
  }, [userData]);

  useEffect(() => {
    dispatch(getCountryList());
    dispatch(getRoles());
  }, []);

  const con_filter = countryOptions?.map(({iso, name, id, iso3, niceName, numberCode, phoneCode}) => ({
    value: iso,
    label: name,
    id,
    iso3,
    niceName,
    numberCode,
    phoneCode,
  }));

  const code_filter = countryOptions?.map(({iso, name, id, iso3, niceName, numberCode, phoneCode}) => ({
    iso,
    name,
    id,
    iso3,
    niceName,
    label: phoneCode,
    value: numberCode,
  }));

  const BreadCrumbRoutes = [
    {name: translate('userManagementBreadcrumb.Home'), path: '/journal'},
    {name: translate('userManagementBreadcrumb.Manage'), path: `/journal/user-management/user`},
    {name: translate('userManagementCreateUser.UpdateUser'), path: ''},
  ];

  const resetValue = () => {
    formik.resetForm()
    setCountry([])
    setSelectCountry([])
    setSelectRole([])
    setDisableButton(false)
    if (typeof location.state === 'string') {
      userGet(location.state)
        .then(response => {
          setUserData(response.data)
        })
        .catch(error => {
          console.error(error);
        });
    }
  }


  const validateForm = () => {
    const errorsvalidate: {selectCountry_error?: string; } = {};


    if (country.length === 0) {
      errorsvalidate.selectCountry_error = translate('PlaceHolders.Country');
    }

    setErrors(errorsvalidate);
    return Object.keys(errorsvalidate).length === 0;
  };


  const formik: FormikProps<UserUpdateModel> = useFormik<UserUpdateModel>({
    enableReinitialize: true,
    initialValues: {
      fullName: userData?.fullName?userData?.fullName:'',
      email: userData?.email?userData?.email:'',
      stateProvince: userData?.stateProvince?userData?.stateProvince:'',
      countryId: userData?.countryId?userData?.countryId:'',
      phoneCode: userData?.phoneCode?userData?.phoneCode:'',
      addressLine1: userData?.addressLine1?userData?.addressLine1:'',
      addressLine2: userData?.addressLine2?userData?.addressLine2:'',
      city: userData?.city?userData?.city:'',
      mobileNumber: userData?.mobileNumber?userData?.mobileNumber:'',
      pinCode: userData?.pinCode?userData?.pinCode:'',
      orcid: userData?.orcid?userData?.orcid:'',
    },
    validationSchema: Yup.object({
      fullName: Yup.string().required(translate('inputValidations.fullNameRequired')),
      email: Yup.string()
        .required(translate('inputValidations.emailRequired'))
        .max(320, translate('inputValidations.MaxLen320'))
        .matches(emailRegex,  translate('inputValidations.emailInvalid')),
      mobileNumber: Yup.string().required(translate('inputValidations.MobileNumberRequired'))
        .matches(/^\d+$/, translate('inputValidations.allowedOnlyNumbers')),
      addressLine1: Yup.string(),
      addressLine2: Yup.string(),
      countryId: Yup.string(),
      city: Yup.string(),
      stateProvince: Yup.string(),
      pinCode: Yup.string().matches(/^\d+$/, translate('inputValidations.allowedOnlyNumbers')),
      orcid: Yup.string(),
    }),
    onSubmit: handleSubmit,

  });

  const handleMobChange = (e) => {
    if (/^\d*$/.test(e.target.value)) {
      setMobNumber(e.target.value);
    }
  };

  async function handleSubmit(e, {resetForm}) {
    if (validateForm())
           e['countryId'] = country[0].id;
      e['phoneCode'] = selectCountry[0].value;
      const result = InicialRole.map((category1) => {
        const existsInArray2 = selectRole.some((category2) => category2.value === category1.value);
        if (!existsInArray2) {
          return { ...category1, actionType: 'DELETE' };
        }
        return category1;
      });
      selectRole.forEach((category2) => {
        const existsInArray1 = result.some((category1) => category1.value === category2.value);
        if (!existsInArray1) {
          result.push({ ...category2, actionType: 'CREATE' });
        }
      });
      e['roles'] = result.map((items)=>({
        roleId:items.value,
        actionType : items.actionType === undefined ? 'DEFAULT' : items.actionType
      }));
      e['activated'] = isActive;


      if(selectedGender.length > 0){
        e['gender'] = selectedGender[0].value;
      }
      e['id'] = location.state;
      dispatch(userUpdate(e))
      .then((resultAction)=>{
        if(userUpdate.fulfilled.match(resultAction)){
          navigate('/journal/user-management/user')
        }
      });
  }

  const passwordReset = () => {
    setResetPassModal(true)
  }

  const deleteHandleClose = () => {
    setResetPassModal(false);
  };

  return (
    <>
      <UserManagementResetPasswordDialog data={userData}  deleteModal={resetPassModal}
                                         deleteHandleClose={deleteHandleClose}/>
      <div className="pt-2">
        <Breadcrumb props={BreadCrumbRoutes}/>
      </div>
      <div className="d-flex b-bottom">

        <div className="me-auto p-2">
          <div className="d-flex">
            <div className="line"></div>
            <h6 className="heading pb-0 mb-0"><Translate contentKey="userManagementCreateUser.UpdateUser"></Translate></h6>
          </div>
          <p className="title-description"><Translate contentKey="userManagementCreateUser.FillMandatoryDescription"></Translate></p>
        </div>

        {userData?.invitedUser ?
          <div className={"text-end py-3"}>
            <button className="btn custom-btn" onClick={() => passwordReset()} type="button">
              <Translate contentKey="buttons.ResetPassword"></Translate>
            </button>
          </div>:''}
      </div>
      <form onSubmit={formik.handleSubmit} className='mb-4'>
        <div className={"row"}>
          <div className='col-12'>
            <div className="position-relative pb-3">
              {loader || CusLoader ? <LoaderMain/> : null}
              <div className="row">
                <div className="col-4 py-3">
                  <label className="form-label">
                    <Translate contentKey="inputFields.FullName">Full Name</Translate><span className="error_class">*</span>
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

                <div className="col-4 py-3">
                  <label className="form-label">
                    <Translate contentKey="inputFields.Email">Email</Translate><span className="error_class">*</span>
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

                <div className="col-4 py-3">
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

                <div className="col-4 pb-2">
                  <label id="usernameLabel" className="form-label">
                    <Translate contentKey="inputFields.Country">Country</Translate>
                  </label><span className="error_class">*</span>


                  <Select
                    options={con_filter}
                    styles={customStyles}
                    value={country.find(option => option) || ''} // Provide a default value when country.find() is undefined
                    placeholder={translate('PlaceHolders.Country')}
                    onChange={selectedOption => {
                      setCountry([selectedOption]);
                      const value_filter = [selectedOption].map(({
                                                                   value,
                                                                   label,
                                                                   id,
                                                                   iso3,
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
                <div className="col-4 pb-3">
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

                <div className="col-4 pb-2">
                  <label id="usernameLabel" className="form-label">
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
                        onChange={selectedOption => {
                          setSelectCountry([selectedOption]);
                        }}
                      />

                    </div>
                    <div className="col-8 ps-1">
                      <input
                        name="mobileNumber"
                        id="mobileNumber"
                        placeholder={translate('PlaceHolders.MobileNumber')}
                        data-cy="mobileNumber"
                        type="text"
                        className="form-control "
                        onChange={handleMobChange}
                        value={MobNumber}
                        {...formik.getFieldProps('mobileNumber')}
                      />
                      {formik.touched.mobileNumber && formik.errors.mobileNumber ?
                        <div className="error_class">{formik.errors.mobileNumber}</div> : null}
                    </div>
                  </div>
                </div>

                <div className="col-8 pb-2 ">
                  <label className="form-label"> <Translate contentKey="userManagementSideBar.Roles">Roles</Translate></label>

                  <Select isDisabled={!userData?.invitedUser}
                    menuPlacement="top"
                    styles={customStyles}
                    options={Roles}
                    value={selectRole}
                    placeholder={translate('PlaceHolders.Roles')}
                    onChange={(selectedOption: any[]) => {
                      setSelectRole(selectedOption);
                    }}
                    isMulti
                  />

                </div>

                <div className="col-4 pb-3">
                  <label className="form-label">
                    ORCID
                  </label>
                  <input
                    id="orcid"
                    name="orcid"
                    placeholder={translate('PlaceHolders.EnterYourORCID')}
                    data-cy="orcid"
                    type="text"
                    className="form-control"
                    {...formik.getFieldProps('orcid')}

                  />
                </div>
                <div className="col-2 toggle-padding d-flex align-items-center justify-content-between">
                  <div>
                    <label id="usernameLabel" className="form-label">
                      <Translate contentKey="table.Status"></Translate>
                    </label>
                    <div className="d-flex ">
                      <label className="toggle-switch">
                        <input disabled={!userData?.invitedUser} type="checkbox" checked={isActive} onChange={handleChange}/>
                        <span className="slider"></span>
                      </label>
                      <div className="px-2">
                        {isActive === true ?
                          <p><Translate contentKey="status.Active"></Translate></p> : <p><Translate contentKey="status.Inactive"></Translate></p>
                        }
                      </div>
                    </div>
                  </div>
                </div>


                {userData?.journalGroups.length > 0 ?
                  <div className="col-12 mt-2">
                <div className="row mt-3 title_class_wandt">
                  <div className="col-12 mb-3">
                    <div className="card   p-0">
                      <div className="card-header">
                       <b><Translate contentKey="userManagementCreateUser.CanAccessJournalAndGroups"></Translate></b>
                      </div>
                  {userData?.journalGroups.map((item,index)=>(


                    <div key={index} className="card-body">
                      <b>{item.journalTitle}</b>
                        {item?.journalGroupNames.map((itemGroup,indexGroup)=>(
                          <span key={indexGroup} className="me-2 mb-3">{itemGroup}</span>
                        ))}
                   </div>
                  ))}
                    </div>
                  </div>
                </div>
                </div>
                  : ''}
              </div>
            </div>
          </div>

        </div>

        <div className="d-flex b-top py-3">
          <div className="pe-2 m-top">
            <button className="btn custom-btn" type="submit">
              <Translate contentKey="save_update"><Translate contentKey="buttons.update"></Translate></Translate>
            </button>
          </div>
          <div className="pe-2 m-top">
            <button className="btn custom-btn-secondary" type="button" onClick={resetValue}>
              <Translate contentKey="buttons.reset"></Translate>
            </button>
          </div>
        </div>
      </form>
    </>

  );
};

export default UserManagementUpdate;

