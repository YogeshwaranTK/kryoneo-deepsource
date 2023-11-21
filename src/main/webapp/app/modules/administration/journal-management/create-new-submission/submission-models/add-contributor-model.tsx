import React, {useEffect, useState} from 'react';
import {Modal} from 'reactstrap';
import {useFormik, FormikProps} from 'formik';
import * as Yup from 'yup';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {translate, Translate} from "react-jhipster";

export interface CreateContributor {
  prefix?: string,
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
const AddContributorModel = ({
                               editId,
                               showModal,
                               handleClose,
                               controbuterValue,
                               setControbuterValue,
                               editNewOld
                             }) => {

  const [errors, setErrors] = useState<ModelErrors>({});
  const emailRegex = /^[A-Z0-9._%+-]{1,64}@[A-Z0-9.-]{1,255}\.[A-Z]{2,}$/i;
  const [foundObject, setfoundObjects] = useState<any>();
  const [modelTitle, setmodelTitle] = useState<any>('');
  const [roleType, setRoleType] = useState('');

  const handleCancel = () => {
    setRoleType('')
    setAffiliationsList([])
    handleClose();
  }

  const handleContributorRoleChange = (event) => {
    setRoleType(event.target.value);
    setErrors({roleError: ''});
  };

  const validateForm = () => {

    const errorsValidate: ModelErrors = {};
    if (!roleType) {

      errorsValidate.roleError = translate("Contribution_add.Contributor_Role_Required");
    }

    setErrors(errorsValidate);

    return Object.keys(errorsValidate).length === 0;
  };


  useEffect(() => {
    if (editId !== 0 && editNewOld === "old") {
      setmodelTitle('Update Contributor')
      const foundObjectValue = controbuterValue?.find(item => item.id === editId);
      setfoundObjects(foundObjectValue)

    } else if (editId !== 0 && editNewOld === "new") {
      setmodelTitle('Update Contributor')
      const foundObjectValue = controbuterValue?.find(item => item.newId === editId);
      setfoundObjects(foundObjectValue)
    } else {
      setmodelTitle(translate("Contribution_add.Title"))
      setRoleType("")
      setfoundObjects({})
    }

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
      prefix: foundObject?.prefix ? foundObject?.prefix : '',
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

    onSubmit(e, {resetForm}) {
      if (validateForm()) {
        e['role'] = roleType
        if (e["role"] === "TRANSLATOR") {
          e["primary"] = false
        }
        if (editId !== 0 && editNewOld === "old") {
          e['id'] = editId
          e['affiliations'] = affiliationsList
          const indexToUpdate = controbuterValue.findIndex(item => item.id === editId);
          if (indexToUpdate !== -1) {
            const newDataArray = [...controbuterValue];
            newDataArray[indexToUpdate] = e;

            setControbuterValue(newDataArray);

          }
          handleClose();
          setAffiliationsList([])
          resetForm()


        } else if (editId !== 0 && editNewOld === "new") {
          e['newId'] = editId
          e['affiliations'] = affiliationsList
          const indexToUpdate = controbuterValue.findIndex(item => item.newId === editId);
          if (indexToUpdate !== -1) {
            const newDataArray = [...controbuterValue];
            newDataArray[indexToUpdate] = e;

            setControbuterValue(newDataArray);

          }
          handleClose();
          setAffiliationsList([])
          resetForm()

        } else {
          e['newId'] = Math.floor(Math.random() * (Number.MAX_SAFE_INTEGER - 100000)) + 100000;
          e['affiliations'] = affiliationsList
          const myarray = [...controbuterValue, e]
          setControbuterValue(myarray)
          setAffiliationsList([])
          handleClose();
          resetForm();

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

  const handleRemoveAffiliation = (affiliation) => {
    const updatedAffiliations = affiliationsList.map((affiliations) => {
      if (affiliations.affiliation === affiliation) {

        return null;
      }
      return affiliations;
    });

    const filteredAffiliations = updatedAffiliations.filter((affiliations) => affiliations !== null);
    setAffiliationsList(filteredAffiliations);
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
                <Translate contentKey="Contribution_add.Prefix">Prefix</Translate>
              </label>
              <input
                id="prefix"
                name="prefix"
                type="text"
                placeholder={translate("Contribution_add.Enter_Title")}
                data-cy="name"
                {...formik.getFieldProps('prefix')}
                className="form-control"/>
            </div>
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
            <div className='col-4 mb-3'>
              <label className="form-label">
                <Translate contentKey="Contribution_add.Surname">Surname</Translate><span
                className="error_class">*</span>
              </label>
              <input
                id="surName"
                name="surName"
                type="text"
                placeholder={translate("Contribution_add.Enter_Surname")}
                data-cy="name"
                {...formik.getFieldProps('surName')}
                className="form-control"/>
              {formik.touched.surName && formik.errors.surName ?
                <div className="error_class">{formik.errors.surName}</div> : null}
            </div>
            <div className='col-4 mb-3'>
              <label className="form-label">
                <Translate contentKey="Contribution_add.Middle_Name">Middle Name</Translate>
              </label>
              <input
                id="middleName"
                name="middleName"
                type="text"
                placeholder={translate("Contribution_add.Enter_Middle_Name")}
                data-cy="name"
                {...formik.getFieldProps('middleName')}
                className="form-control"/>
            </div>


            <div className='col-4 mb-3'>
              <label className="form-label">
                <Translate contentKey="Contribution_add.Email">Email ID</Translate><span
                className="error_class">*</span>
              </label>
              <input
                id="email"
                name="email"
                type="text"
                placeholder={translate("Contribution_add.Enter_Email")}
                data-cy="name"
                {...formik.getFieldProps('email')}
                className="form-control"/>
              {formik.touched.email && formik.errors.email ?
                <div className="error_class">{formik.errors.email}</div> : null}
            </div>
            <div className='col-4 mb-3'>
              <label className="form-label">
                <Translate contentKey="Contribution_add.ORCID">ORCID</Translate>
              </label>
              <input
                id="orcidId"
                name="orcidId"
                type="text"
                placeholder={translate("Contribution_add.Enter_ORCID")}
                {...formik.getFieldProps('orcidId')}
                className="form-control"/>
              {formik.touched.orcidId && formik.errors.orcidId ?
                <div className="error_class">{formik.errors.orcidId}</div> : null}
            </div>

            <div className='col-4 mb-3'>
              <label className="form-label pt-3 pb-2 m-0">
                <Translate contentKey="Contribution_add.Contributor_Role">Contributor Role</Translate><span
                className="error_class">*</span></label>
              <div className="d-flex">
                <div className="custom-radio pe-3">

                  <input
                    className={`form-check-input form-radio-input me-1`}
                    type="radio"
                    value="AUTHOR"
                    checked={roleType === 'AUTHOR'}
                    onChange={handleContributorRoleChange}/>

                  <label> <Translate contentKey="Contribution_add.Author">Author</Translate></label>
                </div>
                <div className="custom-radio">

                  <input
                    className={`form-check-input form-radio-input me-1`}
                    type="radio"
                    value="TRANSLATOR"
                    checked={roleType === 'TRANSLATOR'}
                    onChange={handleContributorRoleChange}/>
                  <label> <Translate contentKey="Contribution_add.Translator">Translator</Translate></label>
                </div>
              </div>
              {errors.roleError && <span className="error">{errors.roleError}</span>}
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

            <div className='col-12 pt-3'>
              <div className={`d-flex`}>
                <input
                  id="browserList"
                  name="browserList"
                  type='checkbox'
                  data-cy="browserList"
                  checked={formik.values.browserList}
                  value={formik.values.browserList}
                  {...formik.getFieldProps('browserList')}
                  className={`form-check-input`}/>
                {formik.touched.browserList && formik.errors.browserList ?
                  <div className="error_class">{formik.errors.browserList}</div> : null}
                <p className="form-label fw-700 px-2">
                  <Translate contentKey="Contribution_add.Browse_List">Include this contributor in browse
                    lists?</Translate>
                </p>
              </div>
            </div>

            <div className='col-8 mb-3 mt-3'>
              <label className="form-label">
                <Translate contentKey="Contribution_add.Affiliations">Affiliations</Translate>
              </label>
              <div>
                <input
                  placeholder={translate("Contribution_add.Enter_Affiliations")}
                  className="form-control"
                  type="text"
                  id="newAffiliationInput"
                  value={newAffiliation}
                  onChange={handleNewAffiliationChange}
                />


                <ol className="list-group list-group-numbered mt-3">
                  {affiliationsList.map((item, index) => (

                    <li key={index} className="list-group-item d-flex justify-content-between align-items-start">
                      <div className="ms-2 me-auto">
                        <div>{item.affiliation}</div>
                      </div>
                      <FontAwesomeIcon icon="trash" className="fa-trash pt-1"
                                       onClick={() => handleRemoveAffiliation(item.affiliation)}/>
                    </li>

                  ))}
                </ol>
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
            <button className="btn btn--cancel" type="submit" onClick={validateForm}>
              {editId !== 0 ? translate("buttons.update")
                : translate("buttons.submit")}
            </button>
            <button className="btn btn--cancel" type="button" onClick={handleCancel}>
              <Translate contentKey="buttons.cancel"/>
            </button>
          </div>
        </form>
      </div>
    </Modal>
  );
};

export default AddContributorModel;
