import React, {useEffect, useState} from 'react';
import * as Yup from 'yup';
import {Link, useLocation, useNavigate} from 'react-router-dom';
import {useFormik, FormikProps, Formik, Field, Form as Cform, ErrorMessage} from 'formik';
import {useAppDispatch, useAppSelector} from 'app/config/store';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import {forgotpassModel} from 'app/shared/model/forgotPass.model';
import {
  ForgotresendEmailOtp,
  handleForgotChangePassword,
  handleForgotOtpEmailVerify,
  handleForgotPasswordEmailVerify,
  reset
} from './password-reset.reducer';
import {Alert} from "reactstrap";
import Sweetalert from "app/config/sweet-alert";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export const PasswordResetInit = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const location = useLocation()
  const [timer, setTimer] = useState(180); // Initial timer value in seconds
  const [isTimerActive, setIsTimerActive] = useState(false); // Timer active state
  const [otpemail, setemail] = useState(''); // Timer active state
  const [userNotFound, setUserNotFound] = useState(null)
  const [emailValue, setEmailValue] = useState('');
  const [otpNotFound, setOtpNotFound] = useState(null)
  const [otpValue, setOTPValue] = useState('');
  const [otpAttempts, setOtpAttempts] = useState(0);
  const [showLimitError, setShowLimitError] = useState(true);
  const emailRegex = /^[A-Z0-9._%+-]{1,64}@[A-Z0-9.-]{1,255}\.[A-Z]{2,}$/i;
  useEffect(
    () => () => {
      dispatch(reset());
    },
    []
  );
  const loading = useAppSelector(state => state.passwordReset.loading);
  const successMessage = useAppSelector(state => state.passwordReset.successMessage);
  const verifyemailcondition = useAppSelector(state => state.passwordReset.verifyemailcondition);
  const otpverifycondution = useAppSelector(state => state.passwordReset.otpverifycondution);
  const passwordverifycondution = useAppSelector(state => state.passwordReset.passwordverifycondution);
  const passwordverifykey = useAppSelector(state => state.passwordReset.passwordverifykey);
  const newpasschangeSussfully = useAppSelector(state => state.passwordReset.newpasschangeSussfully);

  const [verifyEmailSuccess, setVerifyEmailSuccess] = useState(verifyemailcondition)
  const [verifyOtpSuccess, setVerifyOtpSuccess] = useState(otpverifycondution)
  const [passwordVerifyCondition, setPasswordVerifyCondition] = useState(passwordverifycondution)

  const registeredEmail = (location.state as { registeredEmail: string })?.registeredEmail;


  const minutes = Math.floor(timer / 60);
  const seconds = timer % 60;
  const formattedTimer = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;


  const [alertModal, setalertModal] = useState(false);
  const [alertTitle, setalertTitle] = useState('Password Changed!');
  const [alertSubtitle, sealertSubtitle] = useState('Your password has been changed successfully.');
  const [showPassword, setShowPassword] = useState(false);
  const AlertHandleClose = () => {
    setalertModal(false);
  };

  const handleTogglePassword = (e) => {
    e.preventDefault()
    setShowPassword(!showPassword);
  };

  useEffect(() => {
    if (successMessage) {
      setIsTimerActive(true);
    }
    if (newpasschangeSussfully) {
      setalertModal(true);
      setTimeout(() => {
        AlertHandleClose();
        navigate('/login');
      }, 2800); // Adjust the time as needed (in milliseconds).

    }
  }, [successMessage, newpasschangeSussfully]);

  useEffect(() => {
    let intervalId;
    if (isTimerActive) {
      intervalId = setInterval(() => {
        setTimer(prevTimer => prevTimer - 1);
      }, 1000);
    }
    if (timer === 0) {
      clearInterval(intervalId);
    }
    return () => {
      clearInterval(intervalId);
    };
  }, [isTimerActive, timer]);

  useEffect(() => {
    if (registeredEmail !== undefined) {
      setemail(registeredEmail)
      setVerifyEmailSuccess(true)
      setVerifyOtpSuccess(true)
      setPasswordVerifyCondition(false)
    }
  }, [])


  const validationemilSchemaForm1 = Yup.object().shape({
    email: Yup.string()
      .required('Email Address is required')
      .max(320, 'Maximum length should be 320 characters')
      .matches(emailRegex, 'Invalid Email Address'),
  });

  const validationSchemaForm1 = Yup.object().shape({
    otp: Yup.string().required('OTP is required')
      .max(6, 'Maximum length should be 6 numbers')
      .min(6, 'Minimum length should be 6 numbers')
      .matches(/^\d+$/, 'Allow only numbers'),
  });

  const verfiyemailSubmit = (values, {resetForm}) => {
    setemail(values.email);
    dispatch(handleForgotPasswordEmailVerify(values))
      .then((resultAction) => {
        if (handleForgotPasswordEmailVerify.fulfilled.match(resultAction)) {
          setVerifyEmailSuccess(true)
          setVerifyOtpSuccess(true)
          setPasswordVerifyCondition(false)
        } else if (handleForgotPasswordEmailVerify.rejected.match(resultAction)) {
          if (resultAction.error["response"].data.detail) {
            setUserNotFound(resultAction.error["response"].data.detail)
          } else {
            setUserNotFound(resultAction.error["response"].data.title)
          }

        }
      })
      .catch((error) => {
        console.error("An unexpected error occurred:", error);
      });
  };

  const verfiyotpSubmit = (values, {resetForm}) => {
    values['email'] = otpemail;
    if (otpAttempts + 1 <= 10) {
      dispatch(handleForgotOtpEmailVerify(values))
        .then((resultAction) => {
          if (handleForgotOtpEmailVerify.fulfilled.match(resultAction)) {
            setVerifyEmailSuccess(true)
            setVerifyOtpSuccess(false)
            setPasswordVerifyCondition(true)
          } else if (handleForgotOtpEmailVerify.rejected.match(resultAction)) {
            if (resultAction.error["response"].data.detail) {
              setOtpNotFound(resultAction.error["response"].data.detail)
            } else {
              setOtpNotFound(resultAction.error["response"].data.title)
            }
            setOtpAttempts(prevAttempts => prevAttempts + 1);
          }
        })
        .catch((error) => {
        });
    } else {
      setTimer(0)
      setShowLimitError(false);
    }
  };

  const formik: FormikProps<forgotpassModel> = useFormik<forgotpassModel>({
    initialValues: {
      password: '',
      confirmPassword: '',
      email: '',
      verifyKey: '',
    },
    validationSchema: Yup.object({
      password: Yup.string()
        .required('Password is required')
        .min(8, 'Password must be 8-12 length')
        .max(12, 'Password must be 8-12 length')
        .matches(/[0-9]/, 'Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character')
        .matches(/[a-z]/, 'Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character')
        .matches(/[A-Z]/, 'Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character')
        .matches(/[^\w]/, 'Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 Special Character'),
      confirmPassword: Yup.string()
        .required('Confirm Password is required')
        .oneOf([Yup.ref('password'), null], 'Password does not match'),
    }),
    onSubmit(values, {resetForm}) {
      values['email'] = otpemail;
      values['verifyKey'] = passwordverifykey.verifyKey;
      dispatch(handleForgotChangePassword(values));
    },
  });

  const handleEmailChange = (e) => {
    setEmailValue(e.target.value);
    setUserNotFound(null);
  };

  const handleOTPChange = (e) => {
    if (/^\d*$/.test(e.target.value)) {
      setOTPValue(e.target.value);
    }
    setOtpNotFound(null);
  };

  const handleResentOTP = () => {
    dispatch(ForgotresendEmailOtp({email: otpemail})).then((resultAction) => {
      if (ForgotresendEmailOtp.fulfilled.match(resultAction)) {
        setOTPValue("")
        setTimer(180);
        setOtpAttempts(0)
        setShowLimitError(true)
        setOtpNotFound(null)
      }
    })
  }


  return (
    <div className='login-flow-bg'>

      <Sweetalert alertTitle={alertTitle} alertSubtitle={alertSubtitle} alertModal={alertModal}
                  AlertHandleClose={AlertHandleClose}/>

      <div className="logo_header">
        <img src='content/images/kryoneo-logo-icon.png'/>
      </div>
      <div className="card card_login login p-0">
        <div className="card-body p-0">
          <div className="row  mt-2">
            <div className="col-7 pe-5 border-end pb-5">
              <div id="login-title" data-cy="loginTitle" className="mb-2">
                <div>{verifyOtpSuccess && <p>Verify Your OTP</p>} </div>
                <div>{!verifyEmailSuccess && <p>Forgot Password</p>} </div>
                <div>{passwordVerifyCondition && <p>Change Password</p>} </div>
              </div>
              {verifyEmailSuccess ? null : (
                <Formik enableReinitialize initialValues={{email: emailValue}}
                        validationSchema={validationemilSchemaForm1} onSubmit={verfiyemailSubmit}>
                  <Cform>
                    <div className="col-12 pb-2">
                      <label className="form-label form-label-descrip">
                        Please provide your registered email address, and we'll send you an OTP to proceed with the
                        password reset.

                      </label>
                      <label id="usernameLabel" className="form-label">
                        Email Address<span className="error_class">*</span>
                      </label>
                      <Field type="text"
                             id="email"
                             name="email" className="form-control"
                             placeholder="Enter your Email Address"
                             onChange={handleEmailChange}
                             value={emailValue}/>
                      {
                        userNotFound == null ? (
                          <ErrorMessage name="email" component="div" className="error_class"/>
                        ) : null
                      }
                      {userNotFound != null ? (
                        <div className="error_class">{userNotFound}</div>
                      ) : null}
                    </div>
                    <div className="col-12">
                      <div className="row mt-3">
                        <div className="col-12">
                          {loading ? (
                            <button className="btn btn--primary w-100" disabled type="submit" data-cy="submit">
                              <span>Verify</span>
                            </button>
                          ) : (
                            <button className="btn btn--primary w-100" type="submit" data-cy="submit">
                              <span>Verify</span>
                            </button>
                          )}
                        </div>
                      </div>
                    </div>

                    <div className="col-12 mt-3 text-center new_register mb-3">
                      <div className="dont_have_acc">
                        <span>Have you got the password?</span>
                        <Link to="/login"> Back to login</Link>
                      </div>
                    </div>
                  </Cform>
                </Formik>
              )}

              {verifyOtpSuccess ? (
                <Formik enableReinitialize initialValues={{otp: otpValue}} validationSchema={validationSchemaForm1}
                        onSubmit={verfiyotpSubmit}>
                  <Cform>
                    <div className="col-12 pb-2">
                      {!showLimitError && (
                        <Alert color="danger loginError" data-cy="loginError" className='p-2'>
                          You have reached the maximum number of OTP verification attempts.
                        </Alert>
                      )}
                      <label id="usernameLabel" className="form-label font-w-500">
                        Please enter the One-Time Password (OTP) that was sent to your registered
                        email <b>{otpemail}</b>. Once verified, you can proceed to set up a new password.
                      </label>
                      {/*<label id="usernameLabel" className="form-label">*/}
                      {/*  Enter OTP <span className="error_class">*</span>*/}
                      {/*</label>*/}
                      <Field maxLength={6}
                             type="text"
                             id="otp"
                             name="otp"
                             className="form-control"
                             placeholder="Enter OTP*"
                             onChange={handleOTPChange}
                             value={otpValue}
                      />

                    </div>
                    <div className="d-flex">
                      <div>
                        {
                          otpNotFound == null ? (
                            <ErrorMessage name="otp" component="div" className="error_class"/>
                          ) : null
                        }
                        {showLimitError === true && otpNotFound != null ? (
                          <div className="error_class">{otpNotFound}</div>
                        ) : null}
                      </div>
                      <div className="ms-auto">
                        {timer === 0 ? (
                          <p
                            className="resent_opt text-end" onClick={handleResentOTP}>
                            <span>Resent OTP</span>
                          </p>
                        ) : (
                          <>
                            {' '}
                            {isTimerActive && (
                              <p className="text-end">
                                Resend OTP in <b>{formattedTimer}</b> seconds
                              </p>
                            )}
                          </>
                        )}
                      </div>
                    </div>

                    <div className="clearfix"></div>

                    <div className="col-12">
                      <div className="row mt-3">
                        <div className="col-12">
                          {!showLimitError ? (
                            <button className="btn btn--primary w-100" disabled type="submit" data-cy="submit">
                              <span>Submit</span>
                            </button>
                          ) : (
                            <button className="btn btn--primary w-100" type="submit" data-cy="submit">
                              <span>Submit</span>
                            </button>
                          )}
                        </div>
                      </div>
                    </div>
                    <div className="col-12 mt-3 text-center new_register mb-3">
                      <div className="dont_have_acc">
                        <span>Have you got the password?</span>
                        <Link to="/login"> Back to login</Link>
                      </div>
                    </div>
                  </Cform>
                </Formik>
              ) : null}

              {passwordVerifyCondition ? (
                <form onSubmit={formik.handleSubmit}>
                  <div className="col-12 pb-2">
                    <label id="addressLine1" className="form-label">
                      New Password<span className="error_class">*</span>
                    </label>
                    <div className="input-with-icon">
                      <input
                        name="password"
                        id="password"
                        placeholder="Enter your new password"
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

                  {formik.dirty && formik.errors.confirmPassword ?
                    <PasswordStrengthBar password={formik.values.password}/> : null}

                  <div className="col-12 pb-2">
                    <label id="confirmPassword" className="form-label">
                      Confirm Password<span className="error_class">*</span>
                    </label>
                    <input
                      name="confirmPassword"
                      id="confirmPassword"
                      placeholder="Enter your confirm password"
                      data-cy="confirmPassword"
                      type="password"
                      className="form-control"
                      {...formik.getFieldProps('confirmPassword')}
                    />
                    {formik.touched.confirmPassword && formik.errors.confirmPassword ? (
                      <div className="error_class">{formik.errors.confirmPassword}</div>
                    ) : null}
                  </div>

                  <div className="col-12">
                    <div className="row mt-3">
                      <div className="col-12">
                        <button className="btn btn--primary w-100" type="submit" data-cy="submit">
                          <span>Submit</span>
                        </button>
                      </div>
                      <div className="col-4"></div>
                    </div>
                  </div>
                  <div className="col-12 mt-3 text-center new_register mb-3">
                    <div className="dont_have_acc">
                      <span>Have you got the password?</span>
                      <Link to="/login"> Back to login</Link>
                    </div>
                  </div>
                </form>
              ) : null}
            </div>
            <div className="col-5 register_img pt-4 text-center pb-5">
              <img src='content/images/login.png' alt="register"/>

              <h4>Experience the User friendly Platform</h4>
              <p>KJMS is an excellent Journal Management platform. Experience the simple steps to sign in and access the
                authorization.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PasswordResetInit;
