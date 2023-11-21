import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import {translate} from 'react-jhipster';
import "../developer-settings-management.scss"
import DataTable from 'react-data-table-component';
import {useAppDispatch, useAppSelector} from "app/config/store";
import {
  getMailActionsUser
} from "../developer-settings.reducer";
import {Breadcrumb} from "reactstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import ManageSidebar from "app/modules/administration/user-management/manage-sidebar-config/manage-sidebar";
import EditEmailConfigurationUser
  from "app/modules/administration/user-management/developer-settings/email-settings/edit-email-configuration";


export const DeveloperEmailSettings: React.FC = () => {
  const dispatch = useAppDispatch();
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0];
  const mailActionsPayload = useAppSelector(state => state.developerSettings.mailActionsPayload);
  const postMailActionDetailsSuccess = useAppSelector(state => state.developerSettings.postMailActionDetailsSuccess);

  useEffect(() => {
    dispatch(getMailActionsUser())
  }, [postMailActionDetailsSuccess]);

  const [mailActionDetails, setMailActionDetails] = useState([]);


  useEffect(() => {
    if (mailActionsPayload !== undefined) {
      const transformedArray = mailActionsPayload.map(item => ({
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
      style: {fontWeight: 'bold'},

    },
    {
      name: 'Email Type',
      selector: row => row.id,
      sortable: true,
      width: '400px',
      style: {fontWeight: 'bold'},

    },
    {
      name: 'Title',
      selector: row => row.name,
      sortable: true,
      width: '600px',
      style: {fontWeight: 'bold'},

    },
    {
      name: 'Action',
      cell: (row) => (
        <FontAwesomeIcon
          icon="edit"
          className="fa-edit"
          style={{cursor: 'pointer', fontSize: '16px'}}
          onClick={() => handleEdit(row.id)}
        />
      ),
      width: '100px',
      style: {fontWeight: 'bold'},

    },
  ];


  const customStyles = {
    headCells: {
      style: {
        fontWeight: 'bold',
      },
    },
  };

  const BreadCrumbRoutes = [
    { name: translate('userManagementBreadcrumb.Home'), path: '/journal' },
    { name: translate('userManagementBreadcrumb.Manage'), path: `/journal/user-management/user` },
    { name: translate('userManagementSideBar.Users'), path: '' },
  ];

  return (
    <>
      <div className="row height-100">

        <EditEmailConfigurationUser mailActionId={mailActionId}
                                    showModal={showModal}
                                    handleClose={handleClose}
                                    mailActionDetails={mailActionDetails}
        />

        <div className="col-2 p-0 m-0">
          <ManageSidebar/>
        </div>
        <div className="col-10 pt-3 ps-4">
          <Breadcrumb props={BreadCrumbRoutes} />
          <div className="d-flex  b-bottom ">
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

export default DeveloperEmailSettings;
