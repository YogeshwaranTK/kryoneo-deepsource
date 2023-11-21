import {useAppDispatch, useAppSelector} from 'app/config/store';
import {Link, useLocation, useNavigate, useParams} from 'react-router-dom';
import React, {useEffect, useState} from 'react';
import {overridePaginationStateWithQueryParams} from 'app/shared/util/entity-utils';
import {getSortState, JhiPagination, Translate, translate} from 'react-jhipster';
import {ASC, DESC, ITEMS_PER_PAGE, SORT} from 'app/shared/util/pagination.constants';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import LoaderMain from 'app/shared/Loader/loader-main';
import Select, {components} from 'react-select';
import Breadcrumb from 'app/shared/breadcrumb/breadcrumb';
import {formatDate, SelectCustomStyle} from 'app/config/componance-config';
import '../journal-management-list/journal-management.scss';
import '../create-new-submission/work-flow.scss';
import {Button} from "reactstrap";
import {
  getSubmissionList
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import {createNewArticle} from "app/modules/administration/journal-management/create-new-submission/work-flow.reducer";
import SubmissionButtonDropList from "app/modules/administration/journal-management/submission/submissionButtonDropList";
import PaginationInfo from "app/shared/pagination-info";
import {submissionWorkflowConfig} from "app/config/submission-workflow.config";

export const SubmissionList = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const navigate = useNavigate();
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]
  const article = useAppSelector(state => state.settingsManagement.article);
  const ArticleDeleteStatus = useAppSelector(state => state.settingsManagement.ArticleDeleteStatus);
  const totalItems = useAppSelector(state => state.settingsManagement.totalItems);
  const loading = useAppSelector(state => state.settingsManagement.loading);
  const createNewArticleStatus = useAppSelector(state => state.workflow.createNewArticleStatus);
  const submissionId = useAppSelector(state => state.workflow.submissionId);
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);

  const BreadCrumbRoutes = [
    {name: translate('journal_breadcrumb.home'), path: '/journal'},
    {name: translate('journal_breadcrumb.journals'), path: `/journal`},
    {name: `${journals.key ? journals.key : ''}`},
  ];

  const options = [
    {value: 'Filter by All', label: translate('filter.filter')},
    {value: 'All Active List', label: translate('filter.active_list')},
    {value: 'My Article Queue List', label: translate('filter.article_queue')},
    {value: 'Unassigned Articles', label: translate('filter.unassigned_articles')},
    {value: 'Archived Articles', label: translate('filter.archived_articles')},
  ];

  const CaretDownIcon = () => {
    return <img src='content/images/header-image/filter.svg' alt="Not Found"/>;
  };

  const DropdownIndicator = props => {
    return (
      <components.DropdownIndicator {...props}>
        <CaretDownIcon/>
      </components.DropdownIndicator>
    );
  };

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(
      getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const [search, setSearch] = useState('');
  const [loadingDownload, setLoadingDownload] = useState(false);
  const getUsersFromProps = () => {
    dispatch(
      getSubmissionList({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: ``,
        searchText: search,
        submissionListRequestType: submissionWorkflowConfig.submissionListSubmissionType
      })
    );
    const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {

    getUsersFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort, ArticleDeleteStatus]);

  useEffect(() => {
    if (createNewArticleStatus) {
      navigate(`/journal/${Jo_id}/workflow`, {
        state: submissionId
      });
    }
  }, [createNewArticleStatus]);

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


  const sort = p => () =>
    setPagination({
      ...pagination,
      order: pagination.order === ASC ? DESC : ASC,
      sort: p,
    });

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
      getSubmissionList({
        page: 0,
        size: event.currentTarget.value,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: search,
        submissionListRequestType: submissionWorkflowConfig.submissionListSubmissionType
      })
    );
  };

  const searchValues = (event: any) => {
    setSearch(event.target.value);
    dispatch(
      getSubmissionList({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: event.target.value,
        submissionListRequestType: submissionWorkflowConfig.submissionListSubmissionType
      })
    );
  };

  const handleJournalPdf = () => {
    setLoadingDownload(true);
  };

  const handleJournalExcel = () => {
    setLoadingDownload(true);
  };

  const CreateNewArticle = () => {
    dispatch(createNewArticle({title: 'Untitled'}));
  };

  const statusSetFunction = (e) => {
    if (e === 'SUBMITTED') {
      return <> <span className="green-dot"></span>Article Submitted</>
    } else if (e === 'DRAFTED') {
      return <> <span className="red-dot"></span>Saved Draft</>
    } else if (e === 'REVISION_REQUESTED') {
      return <> <span className="yellow-dot"></span>Technical Review Requested</>
    } else if (e === 'REVISION_REQUEST_ACCEPTED') {
      return <> <span className="green-dot"></span>Technical Review Request Accepted</>
    } else if (e === 'REVISION_REQUEST_FILE_SUBMITTED') {
      return <> <span className="green-dot"></span>Technical Review Request Submitted</>
    } else if (e === 'REVISION_REQUEST_REJECTED') {
      return <> <span className="red-dot"></span>Technical Review Request Rejected</>
    } else if (e === 'SUBMISSION_REVIEWER_REJECTED') {
      return <> <span className="red-dot"></span>Submission Rejected</>
    } else if (e === 'SUBMISSION_REVIEWER_ACCEPTED') {
      return <> <span className="green-dot"></span>Submission Accepted</>
    } else if (e === 'REVISION_REQUEST_ACCEPTED_BY_REVIEWER') {
      return <> <span className="green-dot"></span>Verified Technical Review Request</>
    } else if (e === 'REVISION_REQUEST_REJECTED_BY_REVIEWER') {
      return <> <span className="green-dot"></span>Rejected Technical Review Request</>
    }
  };

  return (
    <>
      <div className="row">
        <div className="col-12 pt-2 ps-4">
          <Breadcrumb props={BreadCrumbRoutes}/>
          {loading || loadingDownload ? <LoaderMain/> : null}
          <div className='work_flow'>
            <div className="tab-content" id="nav-tabContent">
              <div className="d-flex  b-bottom pb-2 pt-2">
                <div className="me-auto p-2 ps-0">
                  <div className="d-flex">
                    <div className="line"></div>
                    <h6 className="heading pb-0 mb-0">
                      <Translate contentKey="submissionFlowName.Submissions"></Translate>
                    </h6>
                  </div>
                  <p className="title-description">
                    <Translate contentKey="article.article_sub_title">List of Articles submitted in our
                      platform</Translate>
                  </p>
                </div>
                <div className="p-2 m-top w-25 input-icons">
                  <input className="form-control me-2 search-box" type="text" placeholder={translate('search')}
                         onChange={searchValues}/>
                  <img alt={'search-data-table'} src='content/images/header-image/search-datatable.svg'
                       className="search_datatable_icon"/>
                </div>
                <div className="p-2 m-top d-none" id="filter-input">
                  <Select
                    options={options}
                    defaultValue={options[0]}
                    styles={SelectCustomStyle}
                    components={{DropdownIndicator}}
                    placeholder={null}
                  />
                </div>
                <div className="p-2 m-top d-none">
                  <div className="dropdown custom-dropdown">
                    <button className="dot-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                      <FontAwesomeIcon icon="dot-circle" className="dot-icon-size"/>
                      <FontAwesomeIcon icon="dot-circle" className="dot-icon-size px-1"/>
                      <FontAwesomeIcon icon="dot-circle" className="dot-icon-size"/>
                    </button>
                    <ul className="dropdown-menu p-0">
                      <li onClick={handleJournalPdf}>
                        <a className="dropdown-item">
                          <Translate contentKey="export_pdf">Export as PDF</Translate>
                        </a>
                      </li>
                      <li onClick={handleJournalExcel}>
                        <a className="dropdown-item">
                          <Translate contentKey="export_excel">Export as Excel</Translate>
                        </a>
                      </li>
                    </ul>
                  </div>
                </div>
                <div className="p-2 m-top">
                  <Button onClick={CreateNewArticle} className="btn btn--primary">
                    <Translate contentKey="buttons.create"></Translate>
                  </Button>
                </div>
              </div>
              <div id="custom-table">
                {article.length > 0 ? (
                  <>
                    <table className="table">
                      <thead>
                      <tr>
                        <th scope="col" className="hand custom_sno_th">#</th>
                        <th scope="col" className="hand" onClick={sort('title')}>
                          <Translate contentKey="submissionList.ArticleTitle"></Translate>
                          <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                        </th>
                        <th scope="col" className="hand" onClick={sort('lastModifiedDate')} style={{width: '14%'}}>
                          <Translate contentKey="table.UpdatedOn"></Translate>
                          <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                        </th>
                        <th scope="col" className="hand" style={{width: '28%'}} onClick={sort('status')}>
                          <Translate contentKey="table.Status"></Translate>
                          <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                        </th>
                        <th scope="col" style={{width: '8%'}}>
                          <Translate contentKey="table.Action"></Translate>

                        </th>
                      </tr>
                      </thead>
                      <tbody>
                      {article?.map((journal, i) => (
                        <tr id={journal.id} key={`user-${i}`}>
                          <td
                            className="custom_sno_td">{pagination.activePage * pagination.itemsPerPage + i - pagination.itemsPerPage + 1}</td>
                          <td>

                            {
                              journal.status !== 'DRAFTED' ?
                                <Link className="ar-details_list" to={`/journal/${Jo_id}/submission-workflow`}
                                      state={journal.id}>
                                  {journal.title}
                                </Link> :
                                <Link
                                  to={journal.status === 'DRAFTED'
                                    ? `/journal/${Jo_id}/workflow` : ``}
                                  state={journal.id} className="ar-details_list">
                                  {journal.title}
                                </Link>
                            }
                          </td>
                          <td>{formatDate(journal.lastModifiedDate)}</td>
                          <td>{statusSetFunction(journal.status)}</td>
                          <td>
                            <SubmissionButtonDropList journal={journal} Jo_id={Jo_id}
                            />
                          </td>
                        </tr>
                      ))}
                      </tbody>
                    </table>
                    <div className="d-flex">
                      <PaginationInfo article={article} pageSize={pageSize} currentPage={pagination.activePage}
                                      itemsPerPage={pagination.itemsPerPage} totalItems={totalItems}/>
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
                  </>
                ) : (!loading) && (
                  search.length > 0 ?
                    <p className="text-center mb-0  mt-5"><Translate contentKey="table.NoResultsFound"></Translate>
                    </p> :
                    <p className="text-center mb-0  mt-5"><Translate
                      contentKey="submissionArticle.NoArticlesCreated"></Translate></p>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  )
};


export default SubmissionList;
