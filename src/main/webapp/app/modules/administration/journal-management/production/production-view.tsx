import React, {useState} from 'react';
import {useAppSelector} from "app/config/store";
import Breadcrumb from "app/shared/breadcrumb/breadcrumb";
import {useLocation, useParams} from "react-router-dom";
import ProductionDiscussion from "app/modules/administration/journal-management/production/production-discussion";
import ProductionGalley from "app/modules/administration/journal-management/production/production-galley";
import {truncateString} from "app/config/journals.config";
import SubmissionDetails
  from "app/modules/administration/journal-management/submission/right-sidebar-submission-details/submisssion-details";

const ProductionViewList = () => {
  const journals = useAppSelector(state => state.settingsManagement.journalDetails);
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]
  const path = `/journal/${parseInt(Jo_id, 10)}`;
  const articleDetails = useAppSelector(state => state.articleDetailManagement.articleData);
  const location = useLocation()

  const BreadCrumbRoutes = [
    {name: 'Home', path: '/journal'},
    {name: 'Journals', path: '/journal'},
    {name: `${journals.key ? journals.key : ''}`, path},
    {name: 'Production', path: '/journal/' + Jo_id + '?submissionListRequestType=PRODUCTION'},
    {name: `${articleDetails.title ? truncateString(journals.title) : ''}`, path: ''},
  ];

  const [productionToggle, setProductionToggle] = useState(false);
  const toggleProduction = () => {
    setProductionToggle(!productionToggle);
  }
  return (<>
      <div className="row work_flow height-100">
        <div className="col-12 pt-3 ps-2">
          <div className='d-flex px-3 '>
            <Breadcrumb props={BreadCrumbRoutes}/>
            <p className=' submission_detail ms-auto' onClick={toggleProduction}>Submission Details</p>
            <SubmissionDetails isOpen={productionToggle} onClose={toggleProduction} submissionId={location.state}/>
            <div className={productionToggle ? 'overlay' : ''} onClick={toggleProduction}></div>
          </div>
          <div className="d-flex pb-3">
            <div className="p-2 w-100">
              <div className="tab-content" id="nav-tabContent">
                <ProductionDiscussion submissionId={location.state}/>
                <ProductionGalley submissionId={location.state}/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default ProductionViewList;
