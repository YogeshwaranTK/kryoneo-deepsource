import {useAppDispatch, useAppSelector} from 'app/config/store';
import {Link, useLocation, useNavigate, useParams} from 'react-router-dom';
import React, {useEffect, useState} from 'react';
import {overridePaginationStateWithQueryParams} from 'app/shared/util/entity-utils';
import {getSortState, JhiPagination, Translate, translate} from 'react-jhipster';
import {ASC, DESC, ITEMS_PER_PAGE, SORT} from 'app/shared/util/pagination.constants';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import LoaderMain from 'app/shared/Loader/loader-main';
import Select, {components} from 'react-select';
import {formatDate, SelectCustomStyle} from 'app/config/componance-config';
import '../journal-management-list/journal-management.scss';
import {
  getSubmissionList, getJournalDetails,
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import SubmissionButtonDropList from "app/modules/administration/journal-management/submission/submissionButtonDropList";
import PaginationInfo from "app/shared/pagination-info";
import {submissionWorkflowConfig} from "app/config/submission-workflow.config";
import Breadcrumb from "app/shared/breadcrumb/breadcrumb";

export const ProductionList = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const navigate = useNavigate();
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]
  const article = useAppSelector(state => state.settingsManagement.article);
  const ArticleDeleteStatus = useAppSelector(state => state.settingsManagement.ArticleDeleteStatus);
  const totalItems = useAppSelector(state => state.settingsManagement.totalItems);
  const loading = useAppSelector(state => state.settingsManagement.loading);
  // const account = useAppSelector(state => state.authentication.account);
  // const joPermissions = account?.journalPermissions?.[Jo_id] || [];
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);


  const options = [
    {value: 'Filter by All', label: translate('filter.filter')},
    {value: 'All Active List', label: translate('filter.active_list')},
    {value: 'My Article Queue List', label: translate('filter.article_queue')},
    {value: 'Unassigned Articles', label: translate('filter.unassigned_articles')},
    {value: 'Archieved Articles', label: translate('filter.archived_articles')},
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
  const [loading_downlaod, setloading_downlaod] = useState(false);
  const getSubmissionFromProps = () => {
    dispatch(
      getSubmissionList({
        submissionListRequestType: submissionWorkflowConfig.submissionListProductionType,
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: search,
      })
    );

    const endURL = `?submissionListRequestType=${submissionWorkflowConfig.submissionListProductionType}&page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    getSubmissionFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort, ArticleDeleteStatus]);

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
        submissionListRequestType: submissionWorkflowConfig.submissionListProductionType,
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
      getSubmissionList({
        submissionListRequestType: submissionWorkflowConfig.submissionListProductionType,
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: event.target.value,
      })
    );
  };

  const handleJournalPdf = () => {
    setloading_downlaod(true);
  };

  const handleJournalExcel = () => {
    setloading_downlaod(true);
  };

  const statusSetFunction = (e) => {
    if (e === 'MOVED_TO_PRODUCTION') {
      return <> <span className="green-dot"></span>Moved To Production</>
    }
    if (e === 'PEER_REVIEW_REQUESTED') {
      return <> <span className="yellow-dot"></span>Peer Review Requested</>
    }
    return <> <span className="green-dot"></span>Not Implemented</>
  }

  useEffect(() => {
    dispatch(getJournalDetails(Jo_id));
  }, []);

  const BreadCrumbRoutes = [
    {name: translate('journal_breadcrumb.home'), path: '/journal'},
    {name: translate('journal_breadcrumb.journals'), path: `/journal`},
    {name: `${journals.key ? journals.key : ''}`},
  ];

  return (
    <>
      {/*<div>*/}
      {/*  {activeAlert && <SweetalertWarning alertTitle={alertTitle} alertSubtitle={alertSubtitle} alertModal={alertModal}*/}
      {/*                                     buttonHandle={buttonHandle} buttonName='Cancel'*/}
      {/*                                     AlertHandleClose={AlertHandleClose}*/}
      {/*                                     alertHandleConfirm={alertHandleConfirm}/>}*/}
      {/*</div>*/}
      <div className="col-12 pt-2 ps-4">
        <Breadcrumb props={BreadCrumbRoutes}/>
        <div className='work_flow'>
          <div className="tab-content" id="nav-tabContent">
            <div className="d-flex  b-bottom pb-2 pt-2">
              <div className="me-auto p-2 ps-0">
                <div className="d-flex">
                  <div className="line"></div>
                  <h6 className="heading pb-0 mb-0">
                    Production
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
                <img alt={'search-tables'} src='content/images/header-image/search-datatable.svg'
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
            </div>

            <div id="custom-table">
              {loading_downlaod ? <LoaderMain/> : null}
              {loading ? <LoaderMain/> : null}
              {article.length > 0 ? (
                <>
                  <table className="table">
                    <thead>
                    <tr>
                      <th scope="col" className="hand custom_sno_th">#</th>

                      <th scope="col" className="hand" onClick={sort('title')}>
                        Article Title
                        <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                      </th>
                      <th scope="col" className="hand" onClick={sort('lastModifiedDate')} style={{width: '14%'}}>
                        Updated on
                        <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                      </th>
                      <th scope="col" className="hand" onClick={sort('status')} style={{width: '28%'}}>
                        Status
                        <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                      </th>
                      <th scope="col" style={{width: '8%'}}>
                        Action(s)
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
                            <Link className="ar-details_list" to={`/journal/${Jo_id}/production`} state={journal.id}>
                              {journal.title}
                            </Link>
                          }
                        </td>
                        <td>{formatDate(journal.lastModifiedDate)}</td>
                        <td>{statusSetFunction(journal.status)}</td>
                        <td>
                          <SubmissionButtonDropList journal={journal} Jo_id={Jo_id}
                                                    ownSubmission={journal.ownSubmission}/>
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
                search.length > 0 ? <p className="text-center mb-0  mt-5"> No Results Found</p> :
                  <p className="text-center mb-0  mt-5">There are no articles created yet. </p>

              )}
            </div>
          </div>
        </div>
      </div>


    </>
  )
};


export default ProductionList;
