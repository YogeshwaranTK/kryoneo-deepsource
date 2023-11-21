import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {getSortState, JhiPagination, Storage, Translate, translate} from 'react-jhipster';
import React, {useEffect, useState} from 'react';
import {useAppDispatch, useAppSelector} from 'app/config/store';
import {Link, useLocation, useNavigate} from 'react-router-dom';
import {overridePaginationStateWithQueryParams} from 'app/shared/util/entity-utils';
import {ASC, DESC, ITEMS_PER_PAGE, SORT} from 'app/shared/util/pagination.constants';
import './journal-management.scss';
import {
  getJournals,
  getJournalsPdf,
  getJournalsExcel
} from 'app/modules/administration/journal-management/journal-management-list/journal-management.reducer';
import {saveAs} from 'file-saver';
import LoaderMain from 'app/shared/Loader/loader-main';
import JournalDelete from './journal-delete';
import {Button} from 'reactstrap';
import {capitalize} from 'lodash';
import {formatDate} from 'app/config/componance-config';
import Breadcrumb from "app/shared/breadcrumb/breadcrumb";
import PaginationInfo from "app/shared/pagination-info";
import {getJournalAccessRole} from '../journal-setting/journals-settings.reducer';


export const JournalManagement = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const navigate = useNavigate();
  const journals = useAppSelector(state => state.journalManagement.journals);
  const totalItems = useAppSelector(state => state.journalManagement.totalItems);
  const loading = useAppSelector(state => state.journalManagement.loading);
  const settingManagementLoading = useAppSelector(state => state.settingsManagement.loading);
  const journalDeleteStatus = useAppSelector(state => state.journalManagement.journalDeleteStatus);
  const successfullyDeletedJournal = useAppSelector(state => state.journalManagement.successfullyDeletedJournal);
  const [showModal, setShowModal] = useState(false);
  const [journalId, setJournalId] = useState();
  const [journalKey, setJournalKey] = useState();
  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'createdAt', 'desc'), location.search)
  );
  const [search, setSearch] = useState('');
  const [loadingDownload, setLoadingDownload] = useState(false);
  const journalAccessSuccess = useAppSelector(state => state.settingsManagement.journalAccessSuccess)
  const journalAccessRole = useAppSelector(state => state.settingsManagement.journalAccessRole)
  const [currentJournalId, setCurrentJournalId] = useState(0)

  const BreadCrumbRoutes = [
    {name: translate('journal_breadcrumb.home'), path: '/journal'},
    {name: translate('journal_breadcrumb.journals'), path: '/journal'}
  ];

  const getUsersFromProps = () => {
    dispatch(
      getJournals({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: search,
      })
    )
    const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;

    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    getUsersFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort, pagination.itemsPerPage, journalDeleteStatus, successfullyDeletedJournal]);

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
  };

  const searchValues = (event: any) => {
    setSearch(event.target.value);
    dispatch(
      getJournals({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
        searchText: event.target.value,
      })
    );
  };

  const handleJournalPdf = () => {
    setLoadingDownload(true);
    getJournalsPdf()
      .then(response => {
        saveAs(response.data, 'Journal-list.pdf');
        setLoadingDownload(false);
      })
      .catch(error => {
        console.error(error);
      });
  };


  useEffect(() => {
    if (currentJournalId !== 0) {
      if (journalAccessSuccess) {
        if ((journalAccessRole?.editorialUser || journalAccessRole?.author) && (Storage.session.get('role') !== "reviewer")) {
          navigate(`/journal/${currentJournalId}/submissions?page=1&sort=id,asc`, {state: {workFlowType: 'submission'}})
        } else if (journalAccessRole?.reviewer || Storage.session.get('role') === "reviewer") {
          navigate(`/journal/${currentJournalId}/reviewer-dashboard`)
        }
      }
    }
  }, [journalAccessSuccess]);

  const journalClickIdBased = id => {
    setCurrentJournalId(id)
    dispatch(getJournalAccessRole());
    Storage.local.set('journal_id', id);
  };

  const handleJournalExcel = () => {
    setLoadingDownload(true);
    getJournalsExcel()
      .then(response => {
        saveAs(response.data, 'Total-journal-list.xlsx');
        setLoadingDownload(false);
      })
      .catch(error => {
        console.error(error);
      });
  };

  const handleClose = () => {
    setShowModal(false);
  };

  const journalDelete = (id, key) => {
    setShowModal(true);
    setJournalId(id);
    setJournalKey(key);
  };

  return (
    <>
      <div className="pt-2 ps-3 pe-3">
        <Breadcrumb props={BreadCrumbRoutes}/>
      </div>
      <div className='position-relative ps-3 pe-3'>
        <JournalDelete journakey={journalKey} journalid={journalId} showModal={showModal} handleClose={handleClose}/>
        <div className="d-flex  b-bottom page-title">
          <div className="me-auto p-2 ps-0">
            <div className="d-flex">
              <div className="line"></div>
              <h6 className="heading pb-0 mb-0">
                <Translate contentKey="journal.title">Journals</Translate>
              </h6>
            </div>
            <p className="title-description">
              <Translate contentKey="journal.sub_title">List of journals created in our platform</Translate>
            </p>
          </div>
          <div className="p-2 m-top w-25 input-icons">
            <input className="form-control me-2 search-box" type="text" placeholder={translate('search')}
                   onChange={searchValues}/>
            <img src='content/images/header-image/search-datatable.svg' className="search_datatable_icon"
                 alt={"search"}/>
          </div>
          <div className="p-2 m-top d-none">
            <div className="dropdown custom-dropdown download_pdf">
              <Button color="" className="dot-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                <FontAwesomeIcon icon="dot-circle" className="dot-icon-size"/>
                <FontAwesomeIcon icon="dot-circle" className="dot-icon-size px-1"/>
                <FontAwesomeIcon icon="dot-circle" className="dot-icon-size"/>
              </Button>
              <ul className="dropdown-menu p-0 dropdown-menu-end">
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
            <Button tag={Link} to="/journal/new" color="-primary">
              <Translate contentKey="buttons.create">CREATE</Translate>
            </Button>
          </div>
        </div>
        <div className='position-relative'>
          {(loadingDownload || loading || settingManagementLoading) && <LoaderMain />}
          <div id="custom-table" style={{ minHeight: '50vh' }}>
            {journals?.length > 0 ?
              <>
                <table className="table">
                  <thead>
                  <tr>
                    <th scope="col" className="hand custom_sno_th">
                      #
                    </th>
                    <th scope="col" className="hand" onClick={sort('title')}>
                      <Translate contentKey="journal.journal_name">Journal Name</Translate>
                      <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                    </th>
                    <th style={{width: '5%'}} scope="col">
                    </th>
                    <th style={{width: '9%'}} scope="col" className="hand" onClick={sort('key')}>
                      <Translate contentKey="journal.key">Key</Translate>
                      <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                    </th>
                    <th style={{width: '14%'}} scope="col" className="hand" onClick={sort('createdAt')}>
                      <Translate contentKey="journal.createdate">Created Date</Translate>
                      <span className="ps-1">
                          <FontAwesomeIcon icon="sort" className="icon-size"/>
                        </span>
                    </th>
                    <th style={{width: '8%'}} scope="col">
                      <Translate contentKey="journal.actions">Created Date</Translate>
                    </th>
                  </tr>
                  </thead>
                  <tbody>
                  {journals?.map((journal, i) => (
                    <tr key={`user-${i}`}>
                      <td className="custom_sno_td">
                        {pagination.activePage * pagination.itemsPerPage + i - pagination.itemsPerPage + 1}
                      </td>
                      <td>
                        <div className={`journal-profile color-${journal.title.slice(0, 1).toUpperCase()}`}>
                          {journal.title.slice(0, 2).toUpperCase()}
                        </div>
                        <div className="ps-4 ms-3">
                          <a className="journal_link_page" onClick={() => journalClickIdBased(journal.id)}>
                            {capitalize(journal.title)}
                          </a>
                        </div>
                      </td>
                      <td></td>
                      <td>{journal.key}</td>
                      <td>{formatDate(journal.createdDate)}</td>
                      <td>
                        <div className="dropdown custom-dropdown">
                          <Button color="" className="td-dot-btn" data-bs-toggle="dropdown" aria-expanded="false">
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size"/>
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size px-1"/>
                            <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size "/>
                          </Button>
                          <ul className="dropdown-menu p-0 dropdown-menu-end">
                            {
                              (
                                journal?.completedStep === 3 ? <li>
                                    <a className="dropdown-item" onClick={() => journalClickIdBased(journal.id)}>
                                      View
                                    </a>
                                  </li> :
                                  <Link className="dropdown-item" to={`/journal/new`} state={journal.id}>
                                    Edit
                                  </Link>
                              )}
                            <li>
                              <a className="dropdown-item" onClick={() => journalDelete(journal.id, journal.key)}>
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
                  <PaginationInfo article={journals} pageSize={pageSize} currentPage={pagination.activePage}
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
              : (!loading) && (
              search.length > 0 ?
                <p className="text-center mb-0  mt-5"><Translate contentKey="table.NoResultsFound"></Translate></p> :
                <p className="text-center mb-0  mt-5"><Translate contentKey="journal.journalEmptyWarning"></Translate>
                </p>)
            }
          </div>
        </div>
      </div>
    </>);
};

export default JournalManagement;


