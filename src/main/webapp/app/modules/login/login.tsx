import React, {useState, useEffect} from 'react';
import {Navigate, useLocation, useNavigate} from 'react-router-dom';
import {useAppDispatch, useAppSelector} from 'app/config/store';
import {translate, Translate} from 'react-jhipster';
import {authenticate, clearAuthToken, getSession, userData, validateToken} from 'app/shared/reducers/authentication';
import LoaderMain from "app/shared/Loader/loader-main";
import {useFormik, FormikProps} from 'formik';
import * as Yup from 'yup';
import {Alert, Col} from 'reactstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import './login.scss';
import {handleForgotPasswordEmailVerify} from "app/modules/account/forgot-password/password-reset.reducer";


export const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const loginError = useAppSelector(state => state.authentication.loginError);
  const loading = useAppSelector(state => state.authentication.loading);
  const [Loading, setLoading] = useState(true);
  const [isActive, setIsActive] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleTogglePassword = (e) => {
    e.preventDefault()
    setShowPassword(!showPassword);
  };
  const handleChange = () => {
    setIsActive(!isActive);
  };
  useEffect(() => {
    if (loading) {
      setLoading(true);
    } else {
      setLoading(false);
    }
  }, [loading]);

  const {from} = (location.state as any) || {from: {pathname: '/journal', search: location.search}};

  const forgot_pass_route = () => {
    navigate('/account/forgotpassword');
  };

  interface LoginModel {
    username: string;
    password: string;
  }

  const login_model = e => {
    clearAuthToken()
    userData.username = e.username;
    userData.password = e.password;
    userData.rememberMe = e.rememberMe;
    dispatch(authenticate(e)).then((resultAction) => {
      if (authenticate.fulfilled.match(resultAction)) {
        const {status, data} = resultAction.payload;
        if (data?.passwordExpired) {
          dispatch(handleForgotPasswordEmailVerify({email: e.username})).then((resultAction) => {
            if (handleForgotPasswordEmailVerify.fulfilled.match(resultAction)) {
              navigate("/account/forgotpassword", {state: {registeredEmail: e.username}});
            }
          })
        } else if (status === 200) {
          validateToken(data);
          dispatch(getSession());
        }
      }
    });
  }
  const emailRegex = /^[A-Z0-9._%+-]{1,64}@[A-Z0-9.-]{1,255}\.[A-Z]{2,}$/i;
  const formik: FormikProps<LoginModel> = useFormik<LoginModel>({
    initialValues: {
      username: '',
      password: '',
    },
    validationSchema: Yup.object({
      username: Yup.string()
        .required('Email Address is required')
        .max(320, 'Maximum length should be 320 characters')
        .matches(emailRegex, 'Invalid Email Address'),
      password: Yup.string()
        .required('Password is required')
        .min(8, 'Password must be 8-12 length')
        .max(12, 'Password must be 8-12 length')
        .matches(/[0-9]/, ' Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character')
        .matches(/[a-z]/, ' Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character')
        .matches(/[A-Z]/, ' Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character')
        .matches(/[^\w]/, ' Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character'),
    }),
    onSubmit(e) {
      e['rememberMe'] = isActive;
      login_model(e)
    },
  });

  if (isAuthenticated) {
    return <Navigate to={from} replace/>;
  }

  return (
    <>
      {
        Loading ? <LoaderMain/> :
          <div className="login-flow-bg">
            <div className="logo_header">
              <img alt='Kryoneo logo' src='content/images/kryoneo-logo-icon.png'/>
            </div>
            <div className="card card_login login p-0">
              <div className="card-body p-0">
                <div className="row  mt-2">
                  <div className="col-7 pe-5 border-end pb-5 position-relative">
                    {loading ? <LoaderMain/> : null}
                    <form onSubmit={formik.handleSubmit} className="row">
                      <div id="login-title" data-cy="loginTitle" className="mb-3">
                        <Translate contentKey="login.title">
                          <b>Login</b>
                        </Translate>
                      </div>
                      <div>
                        <Col md="12">
                          {loginError ? (
                            <Alert color="danger loginError" data-cy="loginError" className='p-2'>
                              <Translate contentKey="login.messages.error.authentication">
                                <strong>Failed to sign in!</strong> Please check your credentials and try again.
                              </Translate>
                            </Alert>
                          ) : null}
                        </Col>
                        <div className="col-12 pb-3">
                          <label id="usernameLabel"
                                 className="form-label"><span>{translate('global.form.username.label')}</span>
                            <span className="error_class">*</span></label>
                          <input
                            name="username"
                            id="username"
                            placeholder='Enter your Email Address'
                            data-cy="username"
                            type="text"
                            className="form-control"
                            {...formik.getFieldProps('username')}
                          />
                          {formik.touched.username && formik.errors.username ?
                            <div className="error_class">{formik.errors.username}</div> : null}
                        </div>

                        <div className="col-12 pb-3">

                          <label id="usernameLabel"
                                 className="form-label"><span>{translate('login.form.password')}</span><span
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
                            {formik.values.password.length > 0 ?
                              <button type='button' className="toggle-password-button" onClick={handleTogglePassword}>
                                <FontAwesomeIcon style={{fontSize: '13px'}} icon={showPassword ? "eye" : "eye-slash"}/>
                              </button> : ''}
                          </div>
                          {formik.touched.password && formik.errors.password ?
                            <div className="error_class">{formik.errors.password}</div> : null}
                        </div>
                        <div className="row">
                          <div className="col-6">
                            <a onClick={forgot_pass_route} data-cy="forgetYourPasswordSelector"
                               className="forgot_pass_class">
                              <Translate contentKey="login.password.forgot">Did you forget your password?</Translate>
                            </a>
                          </div>
                          <div className="col-6 text-end_rememberMe text-end">
                            <input name="rememberMe" type="checkbox" className="form-check-input" checked={isActive}
                                   onChange={handleChange}/>
                            <label className="rememberMe-label">
                              {translate('login.form.rememberMe')}
                            </label>
                          </div>
                          <div className="col-12 mt-4 mb-2">
                            <button className="btn btn--primary  w-100" type="submit" data-cy="submit">
                              <Translate contentKey="login.form.button">Sign In</Translate>
                            </button>
                          </div>
                        </div>
                      </div>
                    </form>
                  </div>

                  <div className="col-5 register_img pt-4 text-center">
                    <img src='content/images/login.png' alt="register"/>

                    <h4>
                      <Translate contentKey="global.experience.experience">
                        <span>Experience the User friendly Platform</span>
                      </Translate>
                    </h4>
                    <p>
                      <Translate contentKey="global.experience.experience_content">
                        <span>KJMS is an excellent Journal Management platform.</span>
                      </Translate>
                    </p>

                  </div>
                </div>
              </div>
            </div>
          </div>
      }
    </>
  );
};

export default Login;
