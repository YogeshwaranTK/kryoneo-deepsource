import React, {useEffect} from 'react';
import './sidebar.scss';
import {useParams} from 'react-router-dom';
import {useAppDispatch} from "app/config/store";
import {
  getJournalDetails
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";


const CopyEditingSidebar = (props) => {
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0]
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getJournalDetails(Jo_id));
  }, []);

  return (
    <div id="sidebar" className="" style={{height:'100%'}}>
      <div className="accordion " id="accordionExample">
        <div className="accordion-item">
          <h2 className="accordion-header" id="headingOne">
            <button
              className={`accordion-button collapsed side_nav_parent_active`}
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#collapseOne"
              aria-expanded="false"
              aria-controls="collapseOne"
            >
              Copy Editing
            </button>
          </h2>

          <div
            id="collapseOne"
            className={`accordion-collapse collapse show`}
            aria-labelledby="headingOne"
            data-bs-parent="#accordionExample"
          >
            <ul className="list-unstyled mb-0">
              <li>
                <a
                  id="sub-items"
                  onClick={() => {
                    props.handleTabChange('Submitted_List')
                  }}
                  className={`${props.activeTab === 'Submitted_List' ? 'active' : ''}`}
                >

                  <span className="pe-3">
                    <img src='content/images/header-image/publish.svg' alt="Not found"/>
                  </span>
                  Submission Details
                </a>
              </li>
              <li>
                <a
                  id="sub-items"
                  onClick={() => {
                    props.handleTabChange('Copy-editing')
                  }}
                  className={`${props.activeTab === 'Copy-editing' ? 'active' : ''}`}
                >

                <span className="pe-3">
                    <img src='content/images/header-image/summary.svg' alt="Not found"/>
                  </span>
                  Copy-editing Workflow
                </a>
              </li>
              <li>
                <a
                  id="sub-items"
                  onClick={() => {
                    props.handleTabChange('Copy-editingGalley')
                  }}
                  className={`${props.activeTab === 'Copy-editingGalley' ? 'active' : ''}`}
                >

                <span className="pe-3">
                    <img src='content/images/header-image/summary.svg' alt="Not found"/>
                  </span>
                  Copy-editing Galley Files
                </a>
              </li>
              {/*<li>*/}
              {/*  <a*/}
              {/*    id="sub-items"*/}
              {/*    onClick={() => {*/}
              {/*      props.handleTabChange('History')*/}
              {/*    }}*/}
              {/*    className={`${props.activeTab === 'History' ? 'active' : ''}`}*/}
              {/*  >*/}
              {/*  <span className="pe-3">*/}
              {/*      <img src='content/images/header-image/category.svg' alt="Not found"/>*/}
              {/*    </span>*/}
              {/*    History*/}
              {/*  </a>*/}
              {/*</li>*/}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CopyEditingSidebar;
