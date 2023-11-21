import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { translate } from 'react-jhipster';
import { useFormik, FormikProps } from 'formik';
import * as Yup from 'yup';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import 'app/modules/login/login.scss';
import Select from 'react-select';
import { getCountryList } from '../administration/user-management/user-management/user-management.reducer';
import { authorRegister } from './authorReducer';

export const Author = () => {
    const { journalId } = useParams<{ journalId: string }>();
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const [errors, setErrors] = useState<{ selectRole_error?: string; selectCountry_error?: string; }>({});

    const countryOptions = useAppSelector(state => state.userManagement.countryData);
    const registerAuthorSuccess = useAppSelector(state => state.authorRegister.registerAuthorSuccess);

    const [showPassword, setShowPassword] = useState(false);

    const [selectedGender, setSelectedGender] = useState(null);
    const [country, setCountry] = useState([]);
    const [selectCountry, setSelectCountry] = useState([]);

    useEffect(() => {
        dispatch(getCountryList());
    }, []);


  useEffect(() => {
    if(registerAuthorSuccess){
      navigate('/login')
    }
  }, [registerAuthorSuccess]);

    const validateForm = () => {
        const errorsValidate: { selectCountry_error?: string; } = {};

        if (country.length === 0) {

            errorsValidate.selectCountry_error = translate('PlaceHolders.Country');
        }
        setErrors(errorsValidate);
        return Object.keys(errorsValidate).length === 0;
    };

    const handleTogglePassword = (e) => {
        e.preventDefault()
        setShowPassword(!showPassword);
    };

    const options = [
        { value: 'MALE', label: 'Male' },
        { value: 'FEMALE', label: 'Female' },
        { value: 'OTHERS', label: 'Others' }
    ]

    interface LoginModel {
        fullName: string;
        email: string;
        password: string;
        gender: string;
        addressLine1: string;
        addressLine2: string;
        countryId: any;
        mobileNumber: string;
        city: string;
        stateProvince: string;
        pinCode: string;
        orcid: string;

    }
    async function handleSubmit(e) {
      if (validateForm()) {
        if (selectedGender !== null && selectedGender[0].value !== null) {
          if (selectedGender[0].value !== '') {
            e['gender'] = selectedGender[0].value;
          }
        }
        e['phoneCode'] = selectCountry[0].value;
        e['journalId'] = journalId;
        dispatch(authorRegister(e));
      }
    }

    const emailRegex = /^[A-Z0-9._%+-]{1,64}@[A-Z0-9.-]{1,255}\.[A-Z]{2,}$/i;
    const formik: FormikProps<LoginModel> = useFormik<LoginModel>({
        initialValues: {
            fullName: '',
            password: '',
            email: '',
            gender: '',
            addressLine1: '',
            addressLine2: '',
            countryId: 0,
            city:'',
            mobileNumber: '',
            stateProvince:'',
            pinCode:'',
            orcid:''
        },
        validationSchema: Yup.object({
            fullName: Yup.string()
                .required('First Name is required')
                .max(320, 'Maximum length should be 320 characters'),
            password: Yup.string()
                .required('Password is required')
                .min(8, 'Password must be 8-12 length')
                .max(12, 'Password must be 8-12 length')
                .matches(/[0-9]/, ' Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character')
                .matches(/[a-z]/, ' Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character')
                .matches(/[A-Z]/, ' Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character')
                .matches(/[^\w]/, ' Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character'),
            email: Yup.string()
                .required('Email Address is required')
                .max(320, 'Maximum length should be 320 characters')
                .matches(emailRegex, 'Invalid Email Address'),
            countryId: Yup.string(),
            mobileNumber: Yup.string().required('Mobile Number is required')
        }),
        onSubmit: handleSubmit,
    });

    const customStyles = {
        control: (provided) => ({
            ...provided,
            fontSize: '14px',
            borderColor: '#9198B0',
            Color: '#9198B0'
        }),
    };

    const con_filter = countryOptions?.map(({ iso, name, id, iso3, niceName, numberCode, phoneCode }) => ({
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

    return (
        <>

            <div className="login-flow-bg">
                <div className="logo_header">
                    <img alt='Kryoneo logo' src='content/images/kryoneo-logo-icon.png' />
                </div>
                <div className="card card_login register p-2">
                    <div className="card-body p-2">
                        <div className="row  mt-2">
                            <div className="col-12 pb-5 position-relative">

                                <form onSubmit={formik.handleSubmit} className="row">
                                    <div id="login-title" data-cy="loginTitle" className="text-center mb-3">
                                        <b>Register</b>
                                    </div>
                                    <div className='row'>

                                        <div className="col-4 pb-3">
                                            <label className="form-label"><span>Full Name</span>
                                                <span className="error_class">*</span></label>
                                            <input
                                                name="fullName"
                                                id="fullName"
                                                placeholder='Enter your Full Name'
                                                data-cy="fullName"
                                                type="text"
                                                className="form-control"
                                                {...formik.getFieldProps('fullName')}
                                            />
                                            {formik.touched.fullName && formik.errors.fullName ? <div className="error_class">{formik.errors.fullName}</div> : null}
                                        </div>

                                        <div className="col-4 pb-3">
                                            <label className="form-label"><span>Email</span><span
                                                className="error_class">*</span></label>
                                            <div className="input-with-icon">
                                                <input
                                                    name="email"
                                                    id="email"
                                                    placeholder="Enter your Email"
                                                    data-cy="email"
                                                    type="text"
                                                    className="form-control"
                                                    {...formik.getFieldProps('email')}
                                                />
                                                {formik.touched.email && formik.errors.email ?
                                                    <div className="error_class">{formik.errors.email}</div> : null}
                                            </div>
                                        </div>
                                        <div className="col-4 pb-3 pe-0">
                                            <label className="form-label"><span>{translate('login.form.password')}</span><span
                                                className="error_class">*</span></label>
                                            <div className="input-with-icon">
                                                <input
                                                    name="password"
                                                    id="password"
                                                    placeholder="Enter your Password"
                                                    data-cy="password"
                                                    type={showPassword ? 'text' : 'password'}
                                                    className="form-control"
                                                    {...formik.getFieldProps('password')}
                                                />
                                                {formik.values.password.length > 0 ? <button type='button' className="toggle-password-button" onClick={handleTogglePassword}>
                                                    <FontAwesomeIcon style={{ fontSize: '13px' }} icon={showPassword ? "eye" : "eye-slash"} />
                                                </button> : ''}
                                            </div>
                                            {formik.touched.password && formik.errors.password ? <div className="error_class">{formik.errors.password}</div> : null}
                                        </div>
                                        <div className="col-4 pb-3">
                                            <label className="form-label"><span>Gender</span>
                                            </label>
                                            <Select
                                                name="gender"
                                                styles={customStyles}
                                                placeholder='Select the Gender'
                                                options={options}
                                                value={selectedGender}
                                                onChange={selectedOption => {
                                                    setSelectedGender([selectedOption]);
                                                }}
                                            />
                                        </div>
                                        <div className="col-4 pb-3">
                                            <label className="form-label"><span>Address Line 1</span>
                                            </label>
                                            <input
                                                name="addressLine1"
                                                id="addressLine1"
                                                placeholder='Enter your Address Line 1'
                                                data-cy="addressLine1"
                                                type="text"
                                                className="form-control"
                                                {...formik.getFieldProps('addressLine1')}
                                            />
                                        </div>
                                        <div className="col-4 pb-3 pe-0">
                                            <label  className="form-label"><span>Address Line 2</span>
                                            </label>
                                            <input
                                                name="addressLine2"
                                                id="addressLine2"
                                                placeholder='Enter your Address Line 2'
                                                data-cy="addressLine2"
                                                type="text"
                                                className="form-control"
                                                {...formik.getFieldProps('addressLine2')}
                                            />
                                        </div>
                                        <div className="col-4 pb-3">
                                            <label className="form-label"><span>Country</span>
                                                <span className="error_class">*</span></label>
                                            <Select
                                                options={con_filter}
                                                styles={customStyles}
                                                value={country.find(option => option) || ''} // Provide a default value when country.find() is undefined
                                                placeholder='Select the country'
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
                                                    // delete errors.selectCountry_error;
                                                }}
                                            />
                                            {errors.selectCountry_error && <span className="error">{errors.selectCountry_error}</span>}
                                        </div>
                                        <div className="col-4 pb-3">
                                            <label className="form-label"><span>City</span>
                                            </label>
                                            <input
                                                name="city"
                                                id="city"
                                                placeholder='Enter your Email Address'
                                                data-cy="city"
                                                type="text"
                                                className="form-control"
                                                {...formik.getFieldProps('city')}
                                            />
                                        </div>
                                        <div className="col-4 pb-3 pe-0">
                                            <label className="form-label"><span>State</span>
                                            </label>
                                            <input
                                                name="stateProvince"
                                                id="stateProvince"
                                                placeholder='Enter your Email Address'
                                                data-cy="stateProvince"
                                                type="text"
                                                className="form-control"
                                                {...formik.getFieldProps('stateProvince')}
                                            />
                                        </div>

                                        <div className="col-2 pb-3">
                                            <label className="form-label"><span>Phone Code</span>
                                            </label>
                                            <Select
                                                defaultValue={selectCountry[0]}
                                                options={code_filter}
                                                styles={customStyles}
                                                value={selectCountry.find(option => option) || ''}
                                                placeholder='Select the code'
                                                onChange={selectedOption => setSelectCountry([selectedOption])} />
                                        </div>
                                        <div className="col-2 pb-3">
                                            <label  className="form-label"><span>Mobile Number</span>
                                                <span className="error_class">*</span></label>
                                            <input
                                                name="mobileNumber"
                                                id="mobileNumber"
                                                placeholder='Enter your Number'
                                                data-cy="mobileNumber"
                                                type="text"
                                                className="form-control"
                                                {...formik.getFieldProps('mobileNumber')}
                                            />
                                            {formik.touched.mobileNumber && formik.errors.mobileNumber ?
                                                <div className="error_class">{formik.errors.mobileNumber}</div> : null}
                                        </div>
                                        <div className="col-4 pb-3">
                                            <label  className="form-label"><span>PinCode</span>
                                            </label>
                                            <input
                                                name="pinCode"
                                                id="pinCode"
                                                placeholder='Enter your PinCode'
                                                data-cy="pinCode"
                                                type="text"
                                                className="form-control"
                                                {...formik.getFieldProps('pinCode')}
                                            />
                                        </div>
                                        <div className="col-4 pb-3 pe-0">
                                            <label  className="form-label"><span>Orcid</span>
                                            </label>
                                            <input
                                                name="orcid"
                                                id="orcid"
                                                placeholder='Enter your Orcid'
                                                data-cy="orcid"
                                                type="text"
                                                className="form-control"
                                                {...formik.getFieldProps('orcid')}
                                            />
                                        </div>


                                        <div className="col-12 d-grid d-md-flex justify-content-end p-0">
                                            <button className="btn btn--primary"  data-cy="button" onClick={() => validateForm()} type={'button'}>
                                                Sign In
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default Author;
