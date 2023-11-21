import React, { useEffect, useState } from 'react';
import { Button } from "reactstrap";
import JButton from "app/shared/component/button/button";
import AssignCopyEditing from "app/modules/administration/journal-management/copy-editing/assign-copy-editing";
// import {
//   deleteParticipantUser,
//   getParticipantsListRaw, postAssignUserProduction, stateFalse
// } from "app/modules/administration/journal-management/copy-editing/copy-editing.reducer";
import {
  deleteParticipantUser, getParticipantsListRaw, stateFalse
} from "app/modules/administration/journal-management/copy-editing/copy-editing.reducer";
import AddDiscussionCopyEditing from "app/modules/administration/journal-management/copy-editing/add-discussion-copy-editing";
import { useAppDispatch, useAppSelector } from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import AcceptSkipCopyEditing from './accept-skip-copy-editing';

const CopyEditingButtonPermissions = (props) => {
  const { participantList, setParticipantList, copyEditingDiscussionList, setCopyEditingDiscussionList, copyEditingDraftFilesList } = props
  const dispatch = useAppDispatch()
  const [isLoading, setIsLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [showModelType, setShowModelType] = useState('');
  // const [participantList, setParticipantList] = useState([]);
  const participantDeleteSuccess = useAppSelector(state => state.production.participantDeleteSuccess);
  const loading = useAppSelector(state => state.production.loading)


  const getParticipantListData = () => {
    setIsLoading(true)
    getParticipantsListRaw(props.submissionId).then(response => {
      setParticipantList(response.data)
      setIsLoading(false)
    })
      .catch(error => {
        setIsLoading(false)
        console.error(error);
      });
  }


  useEffect(() => {
    if (participantDeleteSuccess === true) {
      getParticipantListData()
    }
    dispatch(stateFalse())
  }, [participantDeleteSuccess])

  const handleClose = () => {
    setShowModal(false);
    setShowModelType('')
  };

  const handleParticipantDelete = (participantId) => {
    const paramsDetails = {
      "submissionId": props.submissionId,
      "contributorId": participantId
    }
    dispatch(deleteParticipantUser(paramsDetails))
    stateFalse()
  }

  return (
    <>
      {loading || isLoading ? <LoaderMain /> : null}
      <div className="col-3 b-left  border-0">


        <Button className={`custom-btn-secondary`} onClick={() => {
          setShowModal(true);
          setShowModelType("copyeditingacceptskip")
        }}>
          SEND TO PRODUCTION
        </Button>
        <div className='border-top'>
          <ul className="list-group mt-3">
            <li className="list-group-item" aria-disabled="true"
              style={{ backgroundColor: 'lightgrey', textAlign: 'center' }}>
              <div className="d-flex justify-content-between">
                <div style={{
                  color: '#848484',
                  fontSize: '16px',
                  fontWeight: 700,
                  display: 'flex',
                  alignItems: 'center'
                }}>
                  Participants
                </div>
                <div className={`assign-btn`} onClick={() => {
                  setShowModal(true);
                  setShowModelType("assignParticipants")
                }}>ASSIGN
                </div>
              </div>
            </li>

            {participantList?.length !== 0 ?
              (participantList?.length !== 0) && participantList?.map((participant, index) => (
              <li key={index} className="list-group-item" aria-disabled="true">
                <div className="d-flex align-items-center">
                  <div
                    className={`journal-profile color-${participant && participant.fullName ? participant.fullName?.slice(0, 2).toUpperCase() : null}`}
                  >
                    {participant.fullName?.slice(0, 2).toUpperCase()}
                  </div>
                  <div className="flex-grow-1" style={{ marginLeft: '34px' }}>
                    <span className="article-details m-1 me-3">{participant.fullName}</span>
                  </div>
                  <FontAwesomeIcon
                    icon="trash"
                    className="fa-trash"
                    style={{ cursor: 'pointer', fontSize: '12px' }}
                    onClick={() => handleParticipantDelete(participant?.id)}
                  />
                </div>
              </li>
               ))
               : <li className="list-group-item" aria-disabled="true">
                 <div className="d-flex align-items-center">
                   <div className="flex-grow-1" style={{marginLeft: '34px'}}>
                     <span className="article-details m-1 me-3">No Participants Found</span>
                   </div>
                 </div>
               </li>}

          </ul>
        </div>

      </div >
      {/*<CancelRevisionModel showModal={CancelRevisionShowModel} handleClose={handleClose}></CancelRevisionModel>*/}
      {
        showModelType === "assignParticipants" &&
        <AssignCopyEditing showModal={showModal} handleClose={handleClose}
          setParticipantList={setParticipantList} participantList={participantList}
          submissionId={props.submissionId} />
      }


      {
        showModelType === "copyeditingacceptskip" &&
        <AcceptSkipCopyEditing showModal={showModal} handleClose={handleClose}
          setParticipantList={setParticipantList} participantList={participantList}
          submissionId={props.submissionId} copyEditingDraftFilesList={copyEditingDraftFilesList} />
      }

      {/*<DeclineSubmissionModel  showModal={declineShowModal} handleClose={declineHandleClose} />*/}
      {/*<AcceptRequestModel ArticleId={id}  showConfirmModal={showConfirmModal} handleClose={handleClose}/>*/}

    </>
  );
};

export default CopyEditingButtonPermissions;
