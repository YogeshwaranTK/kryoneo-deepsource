import {Link} from "react-router-dom";
import React, {useEffect, useRef, useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import DeleteSubmissionModel from "app/modules/administration/journal-management/submission/delete-submission-model";
import {journalStatusConfig} from "app/config/journal-status";


const SubmissionButtonDropList = (props: any) => {
  const {
    journal,
    Jo_id,
  } = props

  const ulRef = useRef(null);
  const [declineShowModal, setDeclineShowModal] = useState(false);
  const [isEmpty, setIsEmpty] = useState(true);
  const declineHandleClose = () => setDeclineShowModal(false);

  useEffect(() => {
    if (ulRef.current && ulRef.current.children.length === 0) {
      setIsEmpty(true);
    } else {
      setIsEmpty(false);
    }
  }, []);

  return (

    <div className={`dropdown custom-dropdown ${(isEmpty) ? 'd-none' : ''}`}>
      <button className="td-dot-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size"/>
        <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size px-1"/>
        <FontAwesomeIcon icon="dot-circle" className="td-dot-icon-size "/>
      </button>
      <ul ref={ulRef} className={`dropdown-menu p-0 ${(isEmpty) ? 'd-none' : ''}`}>

        {journal.status !== journalStatusConfig.draft
          && (
            <li>
              <Link className="dropdown-item"
                    to={journal.status === journalStatusConfig.movedToProduction ? `/journal/${Jo_id}/production` : journal.status === journalStatusConfig.movedToCopyEditing ? `/journal/${Jo_id}/copy-editing` : journal.status !== journalStatusConfig.moveToPeerReview ? `/journal/${Jo_id}/submission-workflow` : `/journal/${Jo_id}/review`}
                    state={journal.id}>
                View
              </Link>
            </li>
          )
        }
        {journal.status === 'DRAFTED'
          && (
            <>
              <li>
                <Link className="dropdown-item" to={`/journal/${Jo_id}/workflow`} state={journal.id}>
                  Edit
                </Link>
              </li>
              <li>
                <a className="dropdown-item" onClick={() => setDeclineShowModal(true)}>
                  Delete
                </a>
                <DeleteSubmissionModel journalId={journal.id} showModal={declineShowModal}
                                       handleClose={declineHandleClose}/>
              </li>
            </>
          )}

      </ul>
    </div>
  )
}

export default SubmissionButtonDropList
