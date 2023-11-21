import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {useAppDispatch, useAppSelector} from 'app/config/store';
import {useLocation, useNavigate, useParams} from 'react-router-dom';
import {overridePaginationStateWithQueryParams} from 'app/shared/util/entity-utils';
import {ASC, DESC, ITEMS_PER_PAGE, SORT} from 'app/shared/util/pagination.constants';
import {getSortState, JhiPagination, Translate} from 'react-jhipster';
import {
  deleteAssignedReviewer,
  getReviewerRoleList,
  postReviewerRoleAdd,
} from "app/modules/administration/journal-management/contributors/contributors.reducer";
import AssignUser
  from "app/modules/administration/journal-management/contributors/reuse-contributor-components/assign-users-management";
import LoaderMain from "app/shared/Loader/loader-main";
import DeleteAssignContributors
  from "app/modules/administration/journal-management/contributors/reuse-contributor-components/delete-assigned-contributors";
import PaginationInfo from "app/shared/pagination-info";

const ReviewerContributors = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id', 'desc'), location.search)
  );
  const assignReviewerRolePayload = useAppSelector(state => state.journalContributors.assignReviewerRolePayload);
  const reviewerDetailsTotalItems = useAppSelector(state => state.journalContributors.reviewerDetailsTotalItems);
  const deletedReviewerSuccess = useAppSelector(state => state.journalContributors.deletedReviewerSuccess);
  const loading = useAppSelector(state => state.journalContributors.loading);
  const assignReviewerRolePostSuccess = useAppSelector(state => state.journalContributors.assignReviewerRolePostSuccess);
  const [reviewerUserList, setReviewerUserList] = useState([])
  const [showModal, setShowModal] = useState(false);
  const [deleteModal, setDeleteModal] = useState(false);
  const [userId, setUserId] = useState('');

  useEffect(() => {
    if (assignReviewerRolePayload.length !== 0) {
      setReviewerUserList(assignReviewerRolePayload)
    }
  }, [assignReviewerRolePayload]);


  const handleClose = () => {
    setShowModal(false);
  };
  const deleteHandleClose = () => {
    setDeleteModal(false);
  };

  const userDelete = id => {
    setDeleteModal(true);
    setUserId(id);
  };

  const handleAssignUserRequest = (data) => {
    dispatch(postReviewerRoleAdd(data));
  }

  const handleDeleteUserRequest = (data) => {
    dispatch(deleteAssignedReviewer(data));
  }

  // Pagination

  const [search, setSearch] = useState('');
  const navigate = useNavigate();

  const getUsersFromProps = () => {
    dispatch(
      getReviewerRoleList({
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

  const pageSize = (event: any) => {
    setPagination({
      ...pagination,
      itemsPerPage: event.currentTarget.value,
    });
    dispatch(
      getReviewerRoleList({
        page: 0,
        size: event.currentTarget.value,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: search,
      })
    );
  };

  const sort = p => () => {
    setPagination({
      ...pagination,
      order: pagination.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  // const searchValues = (event: any) => {
  //   setSearch(event.target.value);
  //   dispatch(
  //     getAuthorRoleList({
  //       page: pagination.activePage - 1,
  //       size: pagination.itemsPerPage,
  //       sort: `${pagination.sort},${pagination.order}`,
  //       searchText: event.target.value,
  //     })
  //   );
  // };

  const handlePagination = currentPage =>
    setPagination({
      ...pagination,
      activePage: currentPage,
    });

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

  useEffect(() => {
    getUsersFromProps();
  }, [pagination.activePage, pagination.order,
    pagination.sort,
    assignReviewerRolePostSuccess, deletedReviewerSuccess]);

  const roleTypeRefactor = (roleType) => {
    if (roleType === "reviewer") {
      return 'Reviewer'
    }
  }

  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0];

  return (
    <>
      <AssignUser showModal={showModal} handleClose={handleClose}
                  handleAssignUserRequest={handleAssignUserRequest} fromModel={'reviewer'} Jo_id={Jo_id}
      />
      <DeleteAssignContributors showModal={deleteModal} userId={userId}
                                handleClose={deleteHandleClose} handleDeleteUserRequest={handleDeleteUserRequest}/>

      <div className="row height-100">
        <div className="col-12 pt-3 ps-4">
          <div className="p-2">
            <div className="d-flex">
              <div className="line"></div>
              <h6 className="group-heading pb-0 mb-0"><Translate contentKey="Reviewers_Details.Reviewers"></Translate></h6>
            </div>
            <p className="description"><Translate contentKey="Reviewers_Details.List_of_Reviewers_assigned_for_contribution"></Translate></p>
          </div>

          <div className="d-flex b-bottom">
            <div className="me-auto p-2">
              <p className="group-member pb-0 mb-0"><Translate contentKey="Reviewers_Details.Reviewer_Members"></Translate></p>
              <h2 className="group-member-count">
                {reviewerDetailsTotalItems}
              </h2>
            </div>
            {/*{administrationPermissions?.includes('ADD_USER_TO_JOURNAL_CONTRIBUTOR_GROUP') ?*/}
            <div className="p-2 m-top">
              <button className="btn custom-btn" type="button" onClick={() => setShowModal(true)}>
                <Translate contentKey="Reviewers_Details.ADD_USER"></Translate>
              </button>
            </div>
            {/*: null}*/}
          </div>

          <div id="custom-table">
            {loading ? <LoaderMain/> : null}
            {reviewerUserList?.length > 0 ?
              <>
                <table className="table">
                  <thead>
                  <tr>
                    <th scope="col" className="hand custom_sno_th">
                      #
                    </th>
                    <th scope="col" className="hand"
                        onClick={sort('user.fullName')}
                    >
                      <Translate contentKey="Reviewers_Details.User_Name"></Translate>
                      <span className="ps-1">
                      <FontAwesomeIcon icon="sort" className="icon-size"/>
                    </span>
                    </th>
                    <th scope="col" className="hand">
                      <Translate contentKey="Reviewers_Details.Role_Type"></Translate>
                    </th>
                    <th scope="col"><Translate contentKey="Reviewers_Details.Action"></Translate></th>
                  </tr>
                  </thead>
                  <tbody>
                  {reviewerUserList?.map((group, i) => (
                    <tr id={group?.userId} key={`${i}`}>
                      <td
                        className="custom_sno_td">{pagination.activePage * pagination.itemsPerPage + i - pagination.itemsPerPage + 1}</td>
                      <td>{group?.fullName}</td>
                      <td>{roleTypeRefactor(group?.rootRoleType)}</td>
                      <td>
                        <div className="dropdown custom-dropdown">
                          <div>
                            <button className="td-dot-btn" type="button" data-bs-toggle="dropdown"
                                    aria-expanded="false">
                              <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size"/>
                              <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size px-1"/>
                              <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size "/>
                            </button>
                            <ul className="dropdown-menu p-0 dropdown-menu-end">
                              <li>
                                <a className="dropdown-item" onClick={() => userDelete(group?.userId)}>
                                  <Translate contentKey="Reviewers_Details.Remove_User"></Translate>
                                </a>
                              </li>
                            </ul>
                          </div>
                        </div>
                      </td>
                    </tr>
                  ))}
                  </tbody>
                </table>
                <div className="d-flex">
                  <PaginationInfo article={reviewerUserList} pageSize={pageSize} currentPage={pagination.activePage}
                                  itemsPerPage={pagination.itemsPerPage} totalItems={reviewerDetailsTotalItems}/>
                  <div className="pe-2">
                    <div id="custom-pagination">
                      <div className="d-flex">
                        <JhiPagination
                          activePage={pagination.activePage}
                          onSelect={handlePagination}
                          maxButtons={5}
                          itemsPerPage={pagination.itemsPerPage}
                          totalItems={reviewerDetailsTotalItems}
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </>
              : (!loading) && (
              <p className="text-center mb-0 mt-5">
                <Translate contentKey="Reviewers_Details.No_result_found."></Translate>
              </p>
            )
            }
          </div>
        </div>
      </div>
    </>
  );
};

export default ReviewerContributors;
