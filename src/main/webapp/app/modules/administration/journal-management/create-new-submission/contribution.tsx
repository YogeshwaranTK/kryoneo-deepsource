import React, {useEffect, useState} from 'react';
import * as XLSX from 'xlsx';
import './work-flow.scss';
import {useAppDispatch, useAppSelector} from "app/config/store";
import JButton from "app/shared/component/button/button";
import LoaderMain from "app/shared/Loader/loader-main";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import AddContributorModel
  from "app/modules/administration/journal-management/create-new-submission/submission-models/add-contributor-model";
import {
  getSubmissionDetailsRaw, ContributerUpdateRequest, contributorUpdateTrigger, GetSampleExcelFile
} from "app/modules/administration/journal-management/create-new-submission/work-flow.reducer";
import {saveAs} from 'file-saver';
import {translate, Translate} from "react-jhipster";

export const Contribution = ({onTabChange, routeState}) => {
  const dispatch = useAppDispatch();
  const [loadingLocal, setLoadingLocal] = useState(false);
  const [deletedData, setDeletedData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [contributorValue, setContributorValue] = useState([]);
  const contributorUpdateRequestStatus = useAppSelector(state => state.workflow.contributorUpdateRequestStatus);
  const [editId, setEditId] = useState(0)
  const [errors, setErrors] = useState<{ contributor_error?: string; }>({});
  const [nextOrDraft, setNextOrDraft] = useState('');
  const [editNewOld, setEditNewOld] = useState('');

  useEffect(() => {
    contributorList()
  }, [contributorUpdateRequestStatus]);

  const contributorList = () => {
    setLoadingLocal(true)
    getSubmissionDetailsRaw(parseInt(routeState, 10)).then(response => {
      dispatch(contributorUpdateTrigger());
      setContributorValue(response.data.authors);
      setLoadingLocal(false);
      if (contributorUpdateRequestStatus && nextOrDraft === 'next') {
        setDeletedData([]);
        onTabChange('Upload_File_tab');
      }
    })
      .catch(error => {
        console.error(error);
      });
  }


  const validateForm = (length) => {
    const errorsValidate: { contributor_error?: string; } = {};
    if (length === 0) {
      errorsValidate.contributor_error = ' - Contributor is required';
    }
    setErrors(errorsValidate);
    return Object.keys(errorsValidate).length === 0;
  };

  const HandleSubmit = (e) => {
    setNextOrDraft(e)
    const data = {
      "submissionAuthors": [...contributorValue, ...deletedData],
      "submissionId": parseInt(routeState, 10)
    }

    if (validateForm(contributorValue.length)) {
      dispatch(ContributerUpdateRequest(data))
    }
  }

  const handleCheckboxChange = (index) => {
    const updatedData = [...contributorValue];
    updatedData[index].primary = !updatedData[index].primary;
    setContributorValue(updatedData);
  };

  const handleDelete = (id) => {
    const itemToDelete = contributorValue.find((item) => item.id === id);
    if (itemToDelete) {
      setDeletedData([...deletedData, {...itemToDelete, actionType: "DELETE"}]);
      const updatedData = contributorValue.filter((item) => item.id !== id);
      setContributorValue(updatedData);
    }
  };


  const handleEdit = (id, type) => {
    setEditId(id)
    setEditNewOld(type)
    setShowModal(true);
  };
  const handleClose = () => {
    setEditId(0)
    setShowModal(false);
  };

  const RoleSetFunction = (e) => {
    if (e === 'AUTHOR') {
      return 'Author'
    }
    if (e === 'TRANSLATOR') {
      return 'Translator'
    }
  }

  const [excelData, setExcelData] = useState([]);
  const [error, setError] = useState(null);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      const reader = new FileReader();

      reader.onload = (e) => {
        const data = e.target.result
        const workbook = XLSX.read(data, {type: 'array'});

        const sheetName = workbook.SheetNames[0];
        const sheet = workbook.Sheets[sheetName];

        // const jsonData = XLSX.utils.sheet_to_json(sheet, { header: 1 });
        const jsonData: any[][] = XLSX.utils.sheet_to_json(sheet, {header: 1});

        // Transform the array of arrays into an array of objects
        const headerRow = jsonData[0];
        const dataRows = jsonData.slice(1);
        const formattedData = dataRows.map((row, index) => {

          const obj: any = {
            id: ""
          };
          headerRow.forEach((header, index) => {
            const formattedHeader = header.replace(/\s+/g, '');
            obj[formattedHeader] = row[index];
          });
          obj.id = index + 1
          return obj;
        });
        setExcelData(formattedData);
      }
      reader.readAsArrayBuffer(selectedFile);
    }
  };


  useEffect(() => {
    const isValid = excelData?.every(item => item.FirstName !== undefined && item.SurName !== undefined && item.Email !== undefined && item.Role !== undefined);
    if (isValid) {
      setError(null);
      const res = excelData.map((items) => ({
        prefix: items.prefix ? items.prefix : "",
        firstName: items.FirstName,
        surName: items.SurName,
        middleName: items.MiddleName ? items.MiddleName : "",
        email: items.Email,
        orcidId: items.Orcid ? items.Orcid : "",
        role: items.Role?.toUpperCase(),
        browserList: items.ShowBrowserList === "yes",
        affiliations: [],
        primary: items.Role === "author" && items.Primary === "yes",
      }))
      const myArray = [...contributorValue, ...res]
      setContributorValue(myArray)
    } else {
      setError("Missing required fields");
    }
  }, [excelData])

  const sampleExcelFile = () => {
    GetSampleExcelFile()
      .then(response => {
        saveAs(response.data, 'contributor-add-sample-file.xlsx');
      })
      .catch(error => {
        console.error(error);
      });
  }
  return (
    <>
      <AddContributorModel editId={editId}
                           showModal={showModal}
                           handleClose={handleClose}
                           controbuterValue={contributorValue}
                           setControbuterValue={setContributorValue}
                           editNewOld={editNewOld}/>

      <div className='contribution'>
        {loadingLocal && <LoaderMain/>}
        <div className="col-12">
          <div className="row pt-4 contribution_top">
            <div className='col-6'><h3>List of Contributors
              <small>{errors.contributor_error &&
                <span className="error">{errors.contributor_error}</span>}</small></h3></div>
            <div className="col-6 d-flex justify-content-end">
              <div className='me-3'>
                <label htmlFor="file-upload" className="custom-file-upload-button btn custom-btn">
                  <Translate contentKey="Contribution.Upload_Excel">Upload Excel File</Translate>
                </label>
                <input
                  type="file"
                  id="file-upload"
                  className="excel-file-upload"
                  accept=".xlsx"
                  onChange={handleFileChange}
                  style={{display: 'none'}} // Hide the file input
                />
                {error && <div className="error_class">{error}</div>}
              </div>
              <div className='me-3'>
                <button className="btn custom-btn" onClick={sampleExcelFile}>
                  <Translate contentKey="Contribution.Sample_Excel">Sample Excel</Translate>
                </button>
              </div>
              <div className='pt-1'><a onClick={() => setShowModal(true)}>+
                Add Contributor</a></div>
            </div>
            <div className='col-12'>
              <div className='position-relative pt-3'>

                {contributorValue?.length > 0 ?
                  <table className="table work-flow_table">
                    <thead>
                    <tr>
                      <th scope="col" className="hand custom_sno_th">
                        #
                      </th>
                      <th scope="col" className="hand">
                        <Translate contentKey="Contribution.First_Name">First Name</Translate>
                      </th>
                      <th scope="col" className="hand">
                        <Translate contentKey="Contribution.Surname">Surname</Translate>
                      </th>
                      <th scope="col" className="hand">
                        <Translate contentKey="Contribution.Email">Email ID</Translate>
                      </th>
                      <th scope="col" className="hand">
                        <Translate contentKey="Contribution.Contributor_Role">Contributor Role</Translate>
                      </th>
                      <th scope="col" className="hand">
                        <Translate contentKey="Contribution.Primary_Contact">Primary Contact</Translate>
                      </th>
                      <th scope="col" className="hand">
                        <Translate contentKey="Contribution.Action">Action(s)</Translate>
                      </th>
                    </tr>
                    </thead>
                    <tbody>
                    {contributorValue?.map((contributorDetails, index) => (
                      <tr key={index}>
                        <td className="custom_sno_td">{index + 1}</td>
                        <td>{contributorDetails.firstName}</td>
                        <td>{contributorDetails.surName ? contributorDetails.surName : "-"}</td>
                        <td>{contributorDetails.email ? contributorDetails.email : '-'}</td>
                        <td>{RoleSetFunction(contributorDetails.role)}</td>
                        <td>
                          <input onChange={() => handleCheckboxChange(index)} className="form-check-input"
                                 type="checkbox" name={contributorDetails.name} checked={contributorDetails.primary}
                                 disabled/>
                        </td>
                        <td>
                          <FontAwesomeIcon icon="trash" className="fa-trash"
                                           onClick={() => handleDelete(contributorDetails.id)}/>
                          <FontAwesomeIcon icon="edit" className="edit ms-2"
                                           onClick={() => handleEdit(contributorDetails?.id ? contributorDetails?.id : contributorDetails?.newId, contributorDetails?.id ? "old" : "new")}/>
                        </td>
                      </tr>
                    ))}
                    </tbody>
                  </table>
                  : <div className='pb-4 pb-3 text-center'>Contributors not found.</div>}
                <div className="d-flex b-top pt-4">
                  <div className="pe-2 m-top">
                    <button className="btn custom-btn btn me-2" type="button"
                            onClick={() => onTabChange('Basic_Details_Tab')}>
                      <Translate contentKey="buttons.Previous">PREVIOUS</Translate>
                    </button>
                    <button className="btn custom-btn-secondary" type="button" onClick={() => HandleSubmit("next")}>
                      <Translate contentKey="buttons.Save&Next"> SAVE & NEXT</Translate>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <JButton onclick={() => HandleSubmit("draft")} JbuttonValue={translate("buttons.Save_Draft")} type={"button"}
                 className={"btn custom-btn-secondary save_draft_btn"}/>
      </div>
    </>
  );
};
export default Contribution;

