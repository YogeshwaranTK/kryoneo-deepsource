import React, {useEffect, useState} from 'react';
import {Button} from "reactstrap";
import JButton from "app/shared/component/button/button";
import AssignProduction from "app/modules/administration/journal-management/production/assign-production";
import {
  deleteProductionParticipantUser,
  getParticipantsListRaw, stateFalse
} from "app/modules/administration/journal-management/production/production.reducer";
import AddDiscussionProduction from "app/modules/administration/journal-management/production/add-discussion";
import {useAppDispatch, useAppSelector} from "app/config/store";
import LoaderMain from "app/shared/Loader/loader-main";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import DeleteParticipant from "app/modules/administration/journal-management/production/delete-participants";

const ProductionButtonPermissions = (props) => {
  const {participantList, setParticipantList} = props
  const dispatch = useAppDispatch()
  const [isLoading, setIsLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [showModelType, setShowModelType] = useState('');
  const productionParticipantDeleteSuccess = useAppSelector(state => state.production.productionParticipantDeleteSuccess);
  const loading = useAppSelector(state => state.production.loading)
  const [contributorId, setcontributorId] = useState(0);

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
    if (productionParticipantDeleteSuccess) {
      getParticipantListData()
      handleClose()
      dispatch(stateFalse())
    }

  }, [productionParticipantDeleteSuccess])

  const handleClose = () => {
    setShowModal(false);
    setShowModelType('')
  };

  const handleParticipantDelete = () => {
    const paramsDetails = {
      "submissionId": props.submissionId,
      "contributorId": contributorId
    }
    dispatch(deleteProductionParticipantUser(paramsDetails))

  }

  return (
    <>
      {loading || isLoading ? <LoaderMain/> : null}
      <div className="col-3 b-left  border-0">

        <Button className={`custom-btn-secondary`}>
          ACCEPT SUBMISSION
        </Button>
        <JButton JbuttonValue='DECLINE REQUEST REVIEW' type={"submit"} className={"custom-btn-secondary"}/>
        <JButton
          JbuttonValue="ACCEPT REVIEW REQUEST & NEXT"

          type="submit"
          className="custom-btn-secondary mb-4"
        />


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
                      onClick={() => {
                        setShowModal(true);
                        setShowModelType("deleteParticipants");
                        setcontributorId(participant?.id); // Remove the curly braces
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
      {/*<CancelRevisionModel showModal={CancelRevisionShowModel} handleClose={handleClose}></CancelRevisionModel>*/}
      {showModelType === "assignParticipants" &&
        <AssignProduction showModal={showModal} handleClose={handleClose}
                          setParticipantList={setParticipantList} participantList={participantList}
                          submissionId={props.submissionId}/>}

      {showModelType === "deleteParticipants" &&
        <DeleteParticipant showModal={showModal} handleClose={handleClose}
                         handleParticipantDelete={handleParticipantDelete}/>}


    </>
  );
};

export default ProductionButtonPermissions;
