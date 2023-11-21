import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {getSortState, JhiPagination, Translate, translate} from "react-jhipster";
import {Button} from "reactstrap";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {overridePaginationStateWithQueryParams} from "app/shared/util/entity-utils";
import {ASC, DESC, ITEMS_PER_PAGE} from "app/shared/util/pagination.constants";
import {
  getReviewerSubmissionListPagination
} from "app/modules/administration/journal-management/review/reviewer-workflow/reviewer-list/reviewer-reducer";
import {reviewWorkflowConfig} from "app/config/review.config";
import {capitalize} from "lodash";
import {formatDate} from "app/config/componance-config";
import PaginationInfo from "app/shared/pagination-info";
import {Link, useNavigate, useParams} from "react-router-dom";
import LoaderMain from "app/shared/Loader/loader-main";
import {
  blindTypeSet
} from "app/modules/administration/journal-management/review/reviewer-workflow/reuse-reviewer-component/reviewer.model";


export const ReviewerSubmissionList = (props) => {
  const {reviewerStatus} = props
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'createdAt', 'desc'), location.search)
  );
  const reviewerSubmissionDetails = useAppSelector(state => state.reviewer.reviewerSubmissionDetails);
  const loading = useAppSelector(state => state.reviewer.loading);
  const reviewerSubmissionDetailsTotalItems = useAppSelector(state => state.reviewer.reviewerSubmissionDetailsTotalItems);
  const [search, setSearch] = useState('');
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]


  const getUsersFromProps = () => {
    dispatch(
      getReviewerSubmissionListPagination({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: search,
        status: reviewerStatus
      })
    )
    const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;

    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };


  // pagination

  useEffect(() => {
    getUsersFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort, pagination.itemsPerPage]);


  const searchValues = (event: any) => {
    setSearch(event.target.value);
    dispatch(
      getReviewerSubmissionListPagination({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: event.target.value,
        status: reviewerStatus
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

  const pageSize = (event: any) => {
    setPagination({
      ...pagination,
      itemsPerPage: event.currentTarget.value,
    });
  };

  const handlePagination = currentPage =>
    setPagination({
      ...pagination,
      activePage: currentPage,
    });

  const reviewStatusChange = (status) => {
    if (status === "REQUEST_SENT") {
      return "Waiting for Reviewer Response"
    } else if (status === "REQUEST_ACCEPTED") {
      return "Request Accepted"
    }else if (status === "REQUEST_DECLINED") {
      return "Request Declined"
    }else if(status === 'COMPLETED'){
      return "Review Completed"
    }
  }


  return (
    <>
      <div className='position-relative ps-3 pe-3 pt-3'>
        <div className="d-flex  b-bottom page-title">
          <div className="me-auto p-2 ps-0">
            <div className="d-flex">
              <div className="line"></div>
              <h6
                className="heading pb-0 mb-0">{reviewerStatus === reviewWorkflowConfig.reviewerStatusPending ? 'Pending Reviews' : 'Completed Reviews'}
              </h6>
            </div>
            <p className="title-description">
              {reviewerStatus === reviewWorkflowConfig.reviewerStatusPending ? 'List of Reviews pending in our platform' : 'List of Reviews completed in our platform'}
            </p>
          </div>
          <div className="p-2 m-top w-25 input-icons">
            <input className="form-control me-2 search-box" type="text" placeholder={translate('search')}
                   onChange={searchValues}
            />
            <img src='content/images/header-image/search-datatable.svg' className="search_datatable_icon"
                 alt={"search"}/>
          </div>
        </div>
        <div className='position-relative'>
          {loading ? <LoaderMain/> : null}
          <div id="custom-table" style={{minHeight: '50vh'}}>
            {reviewerSubmissionDetails?.length > 0 ?
              <>
                <table className="table">
                  <thead>
                  <tr>
                    <th scope="col" className="hand custom_sno_th">
                      #
                    </th>
                    <th scope="col" className="hand" onClick={sort('title')}>
                      Assigner Name
                      <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                    </span>
                    </th>
                    <th style={{width: '9%'}} scope="col" className="hand" onClick={sort('key')}>
                      Review Type
                      <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                    </th>
                    <th style={{width: '14%'}} scope="col" className="hand" onClick={sort('createdAt')}>
                      Response Due Date
                      <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                    </th>
                    <th style={{width: '14%'}} scope="col" className="hand" onClick={sort('createdAt')}>
                      Review Due Date
                      <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                    </th>
                    <th style={{width: '20%'}} scope="col" className="hand" onClick={sort('createdAt')}>
                      Status
                      <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                    </th>
                    <th style={{width: '8%'}} scope="col">
                      Action(s)
                    </th>
                  </tr>
                  </thead>
                  <tbody>
                  {reviewerSubmissionDetails?.map((submissionDetails, i) => (
                    <tr key={`submission-${i}`}>
                      <td className="custom_sno_td">
                        {pagination.activePage * pagination.itemsPerPage + i - pagination.itemsPerPage + 1}
                      </td>
                      <td>
                        {capitalize(submissionDetails?.fullName)}
                      </td>
                      <td>{blindTypeSet(submissionDetails?.reviewerReviewType)}</td>
                      <td>{formatDate(submissionDetails?.reviewDueDate)}</td>
                      <td>{formatDate(submissionDetails?.responseDueDate)}</td>
                      <td>{reviewStatusChange(submissionDetails?.reviewStatus)}</td>
                      <td>
                        <div className="dropdown custom-dropdown">
                          <Button color="" className="td-dot-btn" data-bs-toggle="dropdown" aria-expanded="false">
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size"/>
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size px-1"/>
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size "/>
                          </Button>
                          <ul className="dropdown-menu p-0 dropdown-menu-end">
                            <li>
                              <Link className="dropdown-item" to={`/journal/${Jo_id}/reviewer-workflow`}
                                    state={submissionDetails.id}>
                                View
                              </Link>
                            </li>
                            <li>
                              <a className="dropdown-item"
                                // onClick={() => journalDelete(submissionDetails.id, submissionDetails.key)}
                              >
                                Delete
                              </a>
                            </li>
                          </ul>
                        </div>
                      </td>
                    </tr>
                  ))}
                  </tbody>
                </table>
                <div className="d-flex">
                  <PaginationInfo article={reviewerSubmissionDetails} pageSize={pageSize}
                                  currentPage={pagination.activePage}
                                  itemsPerPage={pagination.itemsPerPage}
                                  totalItems={reviewerSubmissionDetailsTotalItems}/>
                  <div className="pe-2">
                    <div id="custom-pagination">
                      <div className="d-flex">
                        <JhiPagination
                          activePage={pagination.activePage}
                          onSelect={handlePagination}
                          maxButtons={5}
                          itemsPerPage={pagination.itemsPerPage}
                          totalItems={reviewerSubmissionDetailsTotalItems}
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </>
              : (!loading) && (
              search.length > 0 ?
                <p className="text-center mb-0  mt-5"><Translate contentKey="table.NoResultsFound"></Translate></p> :
                <p className="text-center mb-0  mt-5">No Pending Submissions Found.
                </p>)
            }
          </div>
        </div>
      </div>
    </>
  )
}


export default ReviewerSubmissionList
