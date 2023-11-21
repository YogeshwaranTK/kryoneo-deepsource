import React, {useEffect, useState} from 'react';
import {Button} from "reactstrap";
import JButton from "app/shared/component/button/button";
import {
  deleteParticipantUser,
  getParticipantsListRaw, stateFalse
} from "app/modules/administration/journal-management/submission/submission.reducer";
import AddDiscussionSubmission
  from "app/modules/administration/journal-management/submission/add-discussion-submission";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import AssignSubmission from "app/modules/administration/journal-management/submission/assign-submission";
import {useNavigate, useParams} from "react-router-dom";
import AcceptSubmissionMoveReview
  from "app/modules/administration/journal-management/submission/accept-submission-and-move-review";
import DeleteParticipant from "app/modules/administration/journal-management/submission/delete-participants";
import AcceptSkipSubmission from './accept-skip-submission';

const SubmissionButtonPermissions = (props) => {
  const {
    participantList,
    setParticipantList,
    submissionDiscussionList,
    setSubmissionDiscussionList,
    submissionReadyFilesList
  } = props
  const dispatch = useAppDispatch();
  const [isLoading, setIsLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [showModelType, setShowModelType] = useState('');
  const [contributorId, setContributorId] = useState(0);
  const participantDeleteSuccess = useAppSelector(state => state.submission.participantDeleteSuccess);
  const assignUserSuccess = useAppSelector(state => state.submission.assignUserSuccess);
  const postAcceptFileAndMoveReviewSuccess = useAppSelector(state => state.submission.postAcceptFileAndMoveReviewSuccess);
  const loading = useAppSelector(state => state.production.loading);
  const navigate = useNavigate();
  const routeParams = useParams();
  const Jo_id = Object.values(routeParams)[0];

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
    if (postAcceptFileAndMoveReviewSuccess === true) {
      navigate(`/journal/${Jo_id}`)
      dispatch(stateFalse())
    }
    if (participantDeleteSuccess === true) {
      getParticipantListData()
      dispatch(stateFalse())
      handleClose()
    }
    if (assignUserSuccess === true) {
      getParticipantListData()
      dispatch(stateFalse())
      handleClose()
    }
  }, [postAcceptFileAndMoveReviewSuccess, participantDeleteSuccess, assignUserSuccess])

  const handleClose = () => {
    setShowModal(false);
    setShowModelType('')
  };

  const handleParticipantDelete = () => {
    const paramsDetails = {
      "submissionId": props.submissionId,
      "contributorId":  contributorId
    }
    dispatch(deleteParticipantUser(paramsDetails))
  }

  return (
    <>
      {loading || isLoading ? <LoaderMain/> : null}
      <div className="col-3 b-left  border-0">

        <Button className={`custom-btn-secondary`} onClick={() => {
          setShowModal(true);
          setShowModelType("acceptSubmissionMoveReview")
        }}>
          SEND TO REVIEW
        </Button>
        <Button className={`custom-btn-secondary`} onClick={() => {
          setShowModal(true);
          setShowModelType("skipreview")
        }}>
          ACCEPT & SKIP REVIEW
        </Button>

        <JButton JbuttonValue='DECLINE SUBMISSION' type={"submit"} className={"custom-btn-secondary"}/>

        <div className='border-top'>
          <ul className="list-group mt-3">
            <li className="list-group-item " aria-disabled="true"
                style={{backgroundColor: 'lightgrey', textAlign: 'center'}}>
              <div className="d-flex  justify-content-between">
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
                      className={`d-flex align-items-center justify-content-center journal-profile color-${participant && participant.fullName ? participant.fullName?.slice(0, 2).toUpperCase() : null}`}
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
                      // onClick={() => handleParticipantDelete(participant?.id)}
                      onClick={() => {
                        setShowModal(true);
                        setShowModelType("deleteParticipants");
                        setContributorId(participant?.id); // Remove the curly braces
                      }}

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

      </div>
      {showModelType === "assignParticipants" &&
        <AssignSubmission showModal={showModal} handleClose={handleClose}
                          setParticipantList={setParticipantList} participantList={participantList}
                          submissionId={props.submissionId}/>}

      {showModelType === "deleteParticipants" &&
        <DeleteParticipant showModal={showModal} handleClose={handleClose}
                          setParticipantList={setParticipantList} participantList={participantList}
                          submissionId={props.submissionId} handleParticipantDelete={handleParticipantDelete}/>}

      {showModelType === "addDiscussion" &&
        <AddDiscussionSubmission showModal={showModal} handleClose={handleClose} participantLists={participantList}
                                 submissionId={props.submissionId} submissionDiscussionList={submissionDiscussionList}
                                 setSubmissionDiscussionList={setSubmissionDiscussionList}/>}

      {showModelType === "acceptSubmissionMoveReview" &&
        <AcceptSubmissionMoveReview showModal={showModal} handleClose={handleClose}
                                    submissionId={props.submissionId}
                                    submissionReadyFilesList={submissionReadyFilesList}
        />}
        {showModelType === "skipreview" &&

        <AcceptSkipSubmission showModal={showModal} handleClose={handleClose}
        submissionId={props.submissionId}
        submissionReadyFilesList={submissionReadyFilesList}/>
        }

    </>
  );
};

export default SubmissionButtonPermissions;
