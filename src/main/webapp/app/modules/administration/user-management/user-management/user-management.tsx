import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import LoaderMain from 'app/shared/Loader/loader-main';
import { getSortState, JhiPagination, translate, Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { formatDate } from 'app/config/componance-config';
import Breadcrumb from "app/shared/breadcrumb/breadcrumb";
import { getUsers } from "app/modules/administration/user-management/user-management/user-management.reducer";
import { capitalize } from "lodash";
import "./user-management.scss"
import UserManagementDeleteDialog
  from "app/modules/administration/user-management/user-management/user-management-delete-dialog";
import { Button } from 'reactstrap';
import PaginationInfo from "app/shared/pagination-info";
import ManageSidebar from "app/modules/administration/user-management/manage-sidebar-config/manage-sidebar";

export const UserManagement: React.FC = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const navigate = useNavigate();
  const users = useAppSelector(state => state.userManagement.users);
  const totalItems = useAppSelector(state => state.userManagement.totalItems);
  const loading = useAppSelector(state => state.userManagement.loading);
  const createdUser = useAppSelector(state => state.userManagement.createdUser);
  const updatedUser = useAppSelector(state => state.userManagement.updatedUser);
  const userRemoveStatus = useAppSelector(state => state.userManagement.userremoveStatus);
  const [userId, setUserId] = useState();
  const [deleteModal, setDeleteModal] = useState(false);

  const userDelete = id => {
    setDeleteModal(true);
    setUserId(id);
  };

  const deleteHandleClose = () => {
    setDeleteModal(false);
  };

  const BreadCrumbRoutes = [
    { name: translate('userManagementBreadcrumb.Home'), path: '/journal' },
    { name: translate('userManagementBreadcrumb.Manage'), path: `/journal/user-management/user` },
    { name: translate('userManagementSideBar.Users'), path: '' },
  ];

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id', 'desc'), location.search)
  );
  const [search, setSearch] = useState('');
  const getUsersFromProps = () => {
    dispatch(
      getUsers({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: search,
      })
    );
    const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    getUsersFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort, updatedUser, createdUser, userRemoveStatus]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sortParam = params.get(SORT);
    if (page && sortParam) {
      const sortSplit = sortParam.split(',');
      setPagination({
        ...pagination,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPagination({
      ...pagination,
      order: pagination.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPagination({
      ...pagination,
      activePage: currentPage,
    });

  const pageSize = (event: any) => {
    setPagination({
      ...pagination,
      itemsPerPage: event.currentTarget.value,
    });
    dispatch(
      getUsers({
        page: 0,
        size: event.currentTarget.value,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: search,
      })
    );
  };

  const searchValues = (event: any) => {
    setSearch(event.target.value);
    dispatch(
      getUsers({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: event.target.value,
      })
    );
  };

  return (
    <>
      <UserManagementDeleteDialog userId={userId} deleteModal={deleteModal} deleteHandleClose={deleteHandleClose} />
      <div className="row height-100">
        <div className="col-2 p-0 m-0">
          <ManageSidebar />
        </div>
        <div className="col-10 pt-3 ps-4">
          <Breadcrumb props={BreadCrumbRoutes} />

          <div className="d-flex  b-bottom pb-3">
            <div className="me-auto p-2">
              <div className="d-flex">
                <div className="line"></div>
                <h6 className="heading pb-0 mb-0"><Translate contentKey="userManagementSideBar.Users">Users</Translate>
                </h6>
              </div>
              <p className="title-description"><Translate
                contentKey="userManagementUsers.ListOfUsersRegisteredPlatform">List of Users registered in our
                platform</Translate>
              </p>
            </div>

            <div className="p-2 m-top w-25 input-icons">
              <input className="form-control me-2 search-box" type="text" placeholder={translate('search')}
                onChange={searchValues} />
              <img alt={'search table'} src='content/images/header-image/search-datatable.svg'
                className="search_datatable_icon" />
            </div>

            <div className="p-2 m-top d-none">
              <div className="dropdown custom-dropdown">
                <button className="dot-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                  <FontAwesomeIcon icon="dot-circle" className="dot-icon-size" />
                  <FontAwesomeIcon icon="dot-circle" className="dot-icon-size px-1" />
                  <FontAwesomeIcon icon="dot-circle" className="dot-icon-size" />
                </button>
                <ul className="dropdown-menu p-0">
                  <li>
                    <a className="dropdown-item">Export as PDF</a>
                  </li>
                  <li>
                    <a className="dropdown-item">Export as Excel</a>
                  </li>
                </ul>
              </div>
            </div>
            <div className="p-2 m-top">
              <Button className="btn custom-btn" type="button" tag={Link} to="/journal/user-management/create">
                <Translate contentKey="buttons.create"></Translate>
              </Button>
            </div>
          </div>

          <div id="custom-table">
            {loading ? <LoaderMain /> : null}
            {users.length > 0 ? <>
              <table className="table">
                <thead>
                  <tr>
                    <th scope="col" className="hand custom_sno_th">
                      #
                    </th>
                    <th scope="col" className="hand" onClick={sort('fullName')}>
                      <Translate contentKey="table.UserName">User Name</Translate>
                      <span className="ps-1">
                        <FontAwesomeIcon icon="sort" className="icon-size" />
                      </span>
                    </th>
                    <th scope="col" className="hand" onClick={sort('createDate')}>
                      <Translate contentKey="table.CreatedOn">Created on</Translate>
                      <span className="ps-1">
                        <FontAwesomeIcon icon="sort" className="icon-size" />
                      </span>
                    </th>
                    <th scope="col" className="hand" onClick={sort('activated')}>
                      <Translate contentKey="table.Status">Status</Translate>
                      <span className="ps-1">
                        <FontAwesomeIcon icon="sort" className="icon-size" />
                      </span>
                    </th>
                    <th scope="col">
                      <Translate contentKey="table.Action">Action(s)</Translate>
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {users?.map((user, i) => (
                    <tr id={user.id} key={`group-${i}`}>
                      <td
                        className="custom_sno_td">{pagination.activePage * pagination.itemsPerPage + i - pagination.itemsPerPage + 1}</td>
                      <td>
                        <div className={`journal-profile color-${user.fullName.slice(0, 1).toUpperCase()}`}>
                          {user.fullName.slice(0, 2).toUpperCase()}
                        </div>
                        <div className="ps-4 ms-3">
                          <Link className="user-name user-name_fc" to={`/journal/user-management/create`} state={{
                                  UserId: user.id,
                                  type: 'users'
                                }}>
                            {capitalize(user.fullName)}
                          </Link>
                          <div className="email">{user.email}</div>
                        </div>
                      </td>
                      <td>{formatDate(user.createdDate)}</td>
                      <td>
                        {user.activated === true ? <>
                          <span className="green-dot"></span>
                          <Translate contentKey="status.Active">Active</Translate>
                        </> : <><span className="red-dot"></span>
                          <Translate contentKey="status.Inactive">Inactive</Translate></>}
                      </td>
                      <td>
                        <div className="dropdown custom-dropdown">
                          <button className="td-dot-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size" />
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size px-1" />
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size " />
                          </button>
                          <ul className="dropdown-menu p-0 dropdown-menu-end">
                            <li>
                              <Link
                                className="dropdown-item"
                                to={`/journal/user-management/create`}
                                state={{
                                  UserId: user.id,
                                  type: 'users'
                                }}
                              >
                                <Translate contentKey="dropdowns.Edit">Edit</Translate>
                              </Link>
                            </li>
                            {user.invitedUser === true
                              ?
                              <li>
                                <a className="dropdown-item" onClick={() => userDelete(user.id)}>
                                  <Translate contentKey="dropdowns.Delete">Delete</Translate>
                                </a>
                              </li> : ''}
                          </ul>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <div className="d-flex">
                <PaginationInfo article={users} pageSize={pageSize} currentPage={pagination.activePage}
                  itemsPerPage={pagination.itemsPerPage} totalItems={totalItems} />
                <div className="pe-2">
                  <div id="custom-pagination">
                    <div className="d-flex">
                      <JhiPagination
                        activePage={pagination.activePage}
                        onSelect={handlePagination}
                        maxButtons={5}
                        itemsPerPage={pagination.itemsPerPage}
                        totalItems={totalItems}
                      />
                    </div>
                  </div>
                </div>
              </div>
            </> : (!loading) && (
              <p className="text-center mb-0 mt-5">
                <Translate contentKey="table.NoResultsFound">No Results Found</Translate>
              </p>
            )
            }
          </div>
        </div>
      </div>
    </>
  );
};

export default UserManagement;
