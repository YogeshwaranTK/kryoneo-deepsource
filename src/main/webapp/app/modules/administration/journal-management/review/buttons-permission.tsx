import React, {useEffect, useState} from 'react';
import {Button} from "reactstrap";
import {useAppDispatch, useAppSelector} from "app/config/store";
import ReviewAssignProduction from './assign-review';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import LoaderMain from 'app/shared/Loader/loader-main';
import {deleteParticipantUser, getParticipantsListRaw, stateFalse} from './reviewReducer';
import AcceptSendProduction from './accept-send-production';
import {Translate} from "react-jhipster";

const ButtonPermissionsReview = (props) => {
  const dispatch = useAppDispatch()
  const {
    participantList,
    setParticipantList,
    reviewRoundId,
    reviewFiles,
    submissionId,
    reviewDiscussionList,
    setReviewDiscussionList
  } = props
  const [isLoading, setIsLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [showModelType, setShowModelType] = useState('');
  const loading = useAppSelector(state => state.review.loading)
  const participantDeleteSuccess = useAppSelector(state => state.review.participantDeleteSuccess);

  const handleClose = () => {
    setShowModal(false);
  };

  const handleParticipantDelete = (participantId) => {
    const paramsDetails = {
      "reviewRoundId": reviewRoundId,
      "contributorId": participantId
    }
    dispatch(deleteParticipantUser(paramsDetails))
    stateFalse()
  }

  const getParticipantListData = () => {
    setIsLoading(true)
    getParticipantsListRaw(submissionId, reviewRoundId).then(response => {
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

  return (
    <>
      {loading || isLoading ? <LoaderMain/> : null}
      <div className="col-3 b-left  border-0">
        <Button
          className={`custom-btn-secondary`}
          onClick={() => {
            setShowModal(true)
            setShowModelType("reviewAcceptSkip")
          }}
        >
          <Translate contentKey="button-permission.ACCEPT_&_SEND_PRODUCTION"></Translate>
        </Button>
        <div className='border-top'>
          <ul className="list-group mt-3">
            <li className="list-group-item" aria-disabled="true"
                style={{backgroundColor: 'lightgrey', textAlign: 'center'}}>
              <div className="d-flex  justify-content-between">
                <div style={{
                  color: '#848484',
                  fontSize: '16px',
                  fontWeight: 700,
                  display: 'flex',
                  alignItems: 'center'
                }}>
                  <Translate contentKey="button-permission.Participants"></Translate>
                </div>
                <div className={`assign-btn`} onClick={() => {
                  setShowModal(true);
                  setShowModelType("review-assignParticipants")
                }}><Translate contentKey="button-permission.ASSIGN"></Translate>
                </div>
              </div>
            </li>
            {participantList?.length !== 0 ?
              participantList?.map((participant, index) => (
                <li key={index} className="list-group-item" aria-disabled="true">
                  <div className="d-flex align-items-center">
                    <div
                      className={`journal-profile color-${participant && participant.fullName ? participant.fullName?.slice(0, 2).toUpperCase() : null}`}
                    >
                      {participant.fullName?.slice(0, 2).toUpperCase()}
                    </div>
                    <div className="flex-grow-1" style={{marginLeft: '34px'}}>
                      <span className="article-details m-1 me-3">{participant.fullName}</span>
                    </div>
                    <FontAwesomeIcon
                      icon="trash"
                      className="fa-trash"
                      style={{cursor: 'pointer', fontSize: '12px'}}
                      onClick={() => handleParticipantDelete(participant?.id)}
                    />
                  </div>
                </li>
              )) :
              <li className="list-group-item" aria-disabled="true">
                <div className="d-flex align-items-center">
                  <div className="flex-grow-1" style={{marginLeft: '20px'}}>
                    <span className="article-details m-1 me-3"><Translate
                      contentKey="button-permission.No_Participants_Found"></Translate></span>
                  </div>
                </div>
              </li>
            }
          </ul>
        </div>
      </div>

      {showModelType === "review-assignParticipants" &&
        <ReviewAssignProduction showModal={showModal} handleClose={handleClose}
                                setParticipantList={setParticipantList}
                                participantList={participantList}
                                reviewRoundId={reviewRoundId}
                                submissionId={submissionId}/>}

      {showModelType === "reviewAcceptSkip" &&
        <AcceptSendProduction showModal={showModal} handleClose={handleClose}
                              setParticipantList={setParticipantList}
                              participantList={participantList}
                              reviewRoundId={reviewRoundId}
                              submissionId={submissionId}
                              reviewFiles={reviewFiles}/>}
    </>
  );
};

export default ButtonPermissionsReview;
