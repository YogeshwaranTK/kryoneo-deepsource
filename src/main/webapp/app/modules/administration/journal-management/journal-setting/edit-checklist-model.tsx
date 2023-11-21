import React, {useEffect, useState} from 'react';
import {Modal} from 'reactstrap';
import {useFormik, FormikProps} from 'formik';
import * as Yup from 'yup';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {translate, Translate} from "react-jhipster";

export interface CreateContributor {

  firstName?: string,
  surName?: string,
  middleName?: string,
  email?: string,
  orcidId: string
  primary?: boolean,
  actionType?: string,
  role?: string,
  browserList?: boolean
}

export interface ModelErrors {
  roleError?: string;
}

const EditCheckListModel = ({
                               showModal,
                               handleClose,
                             }) => {

  const [errors, setErrors] = useState<ModelErrors>({});
  const emailRegex = /^[A-Z0-9._%+-]{1,64}@[A-Z0-9.-]{1,255}\.[A-Z]{2,}$/i;
  const [foundObject, setfoundObjects] = useState<any>();
  const [modelTitle, setmodelTitle] = useState<any>('');
  const [roleType, setRoleType] = useState('');

  const customStyles = {
    option: (provided, state) => ({
      ...provided,
      fontSize: '12px',
    }),
    singleValue: (provided, state) => ({
      ...provided,
      fontSize: '12px',
    }),
  };

  const handleCancel = () => {
    setRoleType('')
    setAffiliationsList([])
    handleClose();
  }

  const validateForm = () => {

    const errorsValidate: ModelErrors = {};
    if (!roleType) {

      errorsValidate.roleError = translate("Contribution_add.Contributor_Role_Required");
    }

    setErrors(errorsValidate);

    return Object.keys(errorsValidate).length === 0;
  };


  useEffect(() => {
    // if (seteditId !== 0 && editNewOld === "old") {
    //   setmodelTitle('Update Contributor')
    //   const foundObjectValue = controbuterValue?.find(item => item.id === seteditId);
    //   setfoundObjects(foundObjectValue)
    //
    // } else if (seteditId !== 0 && editNewOld === "new") {
    //   setmodelTitle('Update Contributor')
    //   const foundObjectValue = controbuterValue?.find(item => item.newId === seteditId);
    //   setfoundObjects(foundObjectValue)
    // } else {
    //   setmodelTitle(translate("Contribution_add.Title"))
    //   setRoleType("")
    //   setfoundObjects({})
    // }

  }, [showModal])
// Step 1: Initialize the state with an array containing the default affiliation
  const [affiliationsList, setAffiliationsList] = useState([]);

  // Step 2: Create a state variable for the new affiliation input
  const [newAffiliation, setNewAffiliation] = useState('');

  useEffect(() => {
    setRoleType(foundObject?.role)
    if (foundObject?.affiliations) {
      setAffiliationsList(foundObject?.affiliations)
    }
  }, [foundObject?.affiliations])

  const formik: FormikProps<CreateContributor> = useFormik<CreateContributor>({
    enableReinitialize: true,
    initialValues: {
      firstName: foundObject?.firstName ? foundObject?.firstName : '',
      surName: foundObject?.surName ? foundObject?.surName : '',
      middleName: foundObject?.middleName ? foundObject?.middleName : '',
      email: foundObject?.email ? foundObject?.email : '',
      orcidId: foundObject?.orcidId ? foundObject?.orcidId : '',
      primary: foundObject?.primary ? foundObject?.primary : false,
      role: foundObject?.role ? foundObject?.role : '',
      browserList: foundObject?.browserList ? foundObject.browserList : '',
    },

    validationSchema: Yup.object({
      firstName: Yup.string().required(translate("Contribution_add.First_Name_Required")),
      surName: Yup.string().required(translate("Contribution_add.Surname_Required")),
      email: Yup.string()
        .required(translate("inputValidations.emailRequired"))
        .max(320, translate("inputValidations.MaxLen320"))
        .matches(emailRegex, translate("inputValidations.emailInvalid")),
    }),

    onSubmit(e) {
      if (validateForm()) {
        e['role'] = roleType
        if (e["role"] === "TRANSLATOR") {
          e["primary"] = false
        }
      }
    }
  });


  const handleNewAffiliationChange = (e) => {
    setNewAffiliation(e.target.value);
  };

  const handleAddAffiliation = () => {
    if (newAffiliation.trim() !== '') {
      const affiliationObject = {
        affiliation: newAffiliation,
      };
      setAffiliationsList([...affiliationsList, affiliationObject]);
      setNewAffiliation('');
    }
  };

  return (
    <Modal id="AddContributorModel" isOpen={showModal} toggle={handleClose} backdrop="static" autoFocus={false}>
      <div className="modal-header">
        <div className="d-flex">
          <div className="line"></div>
          <h5 className="m-0">{modelTitle}</h5>
        </div>
        <button type="button" className="btn-close" onClick={handleCancel} data-bs-dismiss="modal"
                aria-label="Close"></button>
      </div>
      <div className="modal-body ps-4">
        <form onSubmit={formik.handleSubmit}>
          <div className='row'>

            <div className='col-4 mb-3'>
              <label className="form-label">
                <Translate contentKey="Contribution_add.First_Name">First Name</Translate><span
                className="error_class">*</span>
              </label>
              <input
                id="firstName"
                name="firstName"
                type="text"
                placeholder={translate("Contribution_add.Enter_First_Name")}
                data-cy="name"
                {...formik.getFieldProps('firstName')}
                className="form-control"/>
              {formik.touched.firstName && formik.errors.firstName ?
                <div className="error_class">{formik.errors.firstName}</div> : null}
            </div>
            <div className='col-4 pt-3'>
              <div className={`d-flex pt-4 ${roleType !== 'AUTHOR' ? 'd-none' : ''}`}>
                <input
                  id="primary"
                  name="primary"
                  type='checkbox'
                  data-cy="primary"
                  checked={formik.values.primary}
                  value={formik.values.primary}
                  {...formik.getFieldProps('primary')}
                  className={`form-check-input`}/>
                {formik.touched.primary && formik.errors.primary ?
                  <div className="error_class">{formik.errors.primary}</div> : null}
                <p className="form-label fw-700 px-2">
                  <Translate contentKey="Contribution_add.Primary_Contact">Primary Contact</Translate>
                </p>
              </div>
            </div>
            <div className='col-4 mb-3  mt-3'>
              <button className='btn btn-sm btn--primary' style={{marginTop: '34px'}} type='button'
                      onClick={handleAddAffiliation}>
                <Translate contentKey="Contribution_add.Add_Affiliation">Add Affiliation</Translate>
              </button>
            </div>
          </div>
          <div className="modal-footer float-end pt-0">
            <button className="btn btn--cancel" type="button" onClick={handleCancel}>
              <Translate contentKey="buttons.cancel"/>
            </button>
          </div>
        </form>
      </div>
    </Modal>
  );
};

export default EditCheckListModel;
