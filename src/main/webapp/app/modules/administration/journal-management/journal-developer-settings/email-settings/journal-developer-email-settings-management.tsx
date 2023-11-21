import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import {translate} from 'react-jhipster';
import "../developer-settings-management.scss"
import DataTable from 'react-data-table-component';
import {useAppDispatch, useAppSelector} from "app/config/store";
import {
  getMailActions
} from "../journal-developer.reducer";
import EditEmailConfigurationModel
  from "app/modules/administration/journal-management/journal-developer-settings/email-settings/edit-email-configuration";
import FirstSidebar
  from 'app/modules/administration/journal-management/journal-developer-settings/first-sidebar-config/first-sidebar';
import {Breadcrumb} from "reactstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";




export const JournalDeveloperSettings: React.FC = () => {
  const dispatch = useAppDispatch();
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0];
  const mailActionsPayload = useAppSelector(state => state.journalDeveloperSettings.mailActionsPayload);
  const postMailActionDetailsSuccess = useAppSelector(state => state.journalDeveloperSettings.postMailActionDetailsSuccess);
  const BreadCrumbRoutes = [
    {name: translate('userManagementBreadcrumb.Home'), path: '/journal'},
    {name: "Developer Settings", path: `/journal/${parseInt(Jo_id, 10)}/developer-settings`},
    {name: "Developer Settings", path: ''},
  ];

  useEffect(() => {
    dispatch(getMailActions())
  }, [postMailActionDetailsSuccess])

  const [mailActionDetails, setMailActionDetails] = useState([]);


  useEffect(() => {
    if (mailActionsPayload !== undefined) {
      console.log(mailActionsPayload)
      const transformedArray = mailActionsPayload?.map(item => ({
        id: item.id,
        name: item.name,
        mailToVariables: item.mailToVariables
      }));
      setMailActionDetails(transformedArray);
    }
  }, [mailActionsPayload]);


  const [showModal, setShowModal] = useState(false);
  const [mailActionId, setMailActionId] = useState('');
  const handleEdit = (actionlId) => {
    setShowModal(true)
    setMailActionId(actionlId)
  };

  const handleClose = () => {
    setShowModal(false)
  };

  const columns = [
    {
      name: '#',
      selector: (row, index) => index + 1,
      sortable: true,
      width: '100px',
      style: { fontWeight: 'bold' },

    },
    {
      name: 'Email Type',
      selector: row => row.id,
      sortable: true,
      width: '400px',
      style: { fontWeight: 'bold' },

    },
    {
      name: 'Title',
      selector: row => row.name,
      sortable: true,
      width: '600px',
      style: { fontWeight: 'bold' },

    },
    {
      name: 'Action',
      cell: (row) => (
        <FontAwesomeIcon
          icon="edit"
          className="fa-edit"
          style={{ cursor: 'pointer', fontSize: '16px' }}
           onClick={() => handleEdit(row.id)}
        />
      ),
      width: '100px',
      style: { fontWeight: 'bold' },

    },
  ];


  const customStyles = {
    headCells: {
      style: {
        fontWeight: 'bold',
      },
    },
  };


  return (
    <>
      <div className="row height-100 px-2">

        <EditEmailConfigurationModel mailActionId={mailActionId}
                                     showModal={showModal}
                                     handleClose={handleClose}
                                     mailActionDetails={mailActionDetails}
        />

        <div className="col-2 p-0 m-0">
          <FirstSidebar/>
        </div>
        <div className="col-10 pt-3 ps-4">
          <Breadcrumb props={BreadCrumbRoutes}/>
          <div className="d-flex  b-bottom pb-3">
            <div className="me-auto p-2">
              <div className="d-flex">
                <div className="line"></div>
                <h6 className="heading pb-0 mb-0">Email Settings
                </h6>
              </div>
              <p className="title-description">List of Email settings in our journals
              </p>
            </div>

          </div>
          <div className="row position-relative">
            {/*{loadingLocal ? <LoaderMain/> : null}*/}
            <div className="col-12 pt-1 ">
              <div>
                <DataTable
                  data={mailActionDetails}
                  columns={columns}
                  pagination
                  customStyles={customStyles}
                />
              </div>
            </div>
            <div className="clearfix"></div>
          </div>
        </div>
      </div>
    </>
  );
};

export default JournalDeveloperSettings;
