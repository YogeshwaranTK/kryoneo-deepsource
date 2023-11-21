import React, {useEffect, useState} from 'react';
import 'react-quill/dist/quill.snow.css';
import {useAppDispatch, useAppSelector} from "app/config/store";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {Button} from "reactstrap";
import CheckListCreateUpdate from "app/modules/administration/journal-management/journal-setting/checklist-create";
import {
  getCheckList
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import CheckListDelete from "app/modules/administration/journal-management/journal-setting/checklist-delete";

export const CheckList: React.FC = () => {
  const dispatch = useAppDispatch();
  const checkListDetails = useAppSelector(state => state.settingsManagement.checkListDetails);
  const deleteCheckListSuccess = useAppSelector(state => state.settingsManagement.deleteCheckListSuccess);
  const putCheckListSuccess = useAppSelector(state => state.settingsManagement.putCheckListSuccess);
  const postCheckListSuccess = useAppSelector(state => state.settingsManagement.postCheckListSuccess);
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const [deleteItemId, setDeleteItemId] = useState(0);
  const [createShowModal, setCreateShowModal] = useState(false);
  const [itemId, setItemId] = useState(0);

  useEffect(() => {
    if (putCheckListSuccess || postCheckListSuccess || deleteCheckListSuccess) {
      dispatch(getCheckList());
    }
  }, [putCheckListSuccess, postCheckListSuccess, deleteCheckListSuccess]);

  useEffect(() => {
    dispatch(getCheckList());
  }, []);

  const handleDelete = (id) => {
    setDeleteItemId(id);
    setShowDeleteConfirmation(true);
  };

  const confirmDelete = () => {
    setShowDeleteConfirmation(false);
  };

  const cancelDelete = () => {
    setShowDeleteConfirmation(false);
  };

  const declineHandleClose = () => {
    setCreateShowModal(false);
    setShowDeleteConfirmation(false);
    setItemId(0);
  };

  return (
    <>
      <div className="row height-100">
        <div className="col-12 pt-3 px-4">
          <CheckListCreateUpdate showModal={createShowModal} itemId={itemId}
                                 handleClose={declineHandleClose} checkListDetails={checkListDetails}/>
          <CheckListDelete
            delItemId={deleteItemId}
            showModal={showDeleteConfirmation}
            message="Are you sure you want to delete this item?"
            handleClose={declineHandleClose}
            onConfirm={confirmDelete}
            onCancel={cancelDelete}
          />
          <div className="d-flex  b-bottom pb-2 pt-2">
            <div className="me-auto p-2 ps-0">
              <div className="d-flex">
                <div className="line"></div>
                <h6 className="heading pb-0 mb-0">
                  Checklist
                </h6>
              </div>
              <p className="title-description">
                List of Checklist
              </p>
            </div>
            <div className="p-2 m-top">
              <Button className="btn btn--primary" onClick={() => setCreateShowModal(true)}>
                ADD ITEM
              </Button>
            </div>
          </div>
          <div className="row position-relative">
            {checkListDetails?.length > 0 ? <div className="col-12 pt-1 ">
                <ul className="list-group list-group-flush b-bottom">
                  {checkListDetails.map((checkList, index) => (
                    <li className="list-group-item d-flex" key={checkList.id}>
                      {index + 1}&nbsp;&nbsp;&nbsp;
                      <div className='col-11' dangerouslySetInnerHTML={{__html: checkList.journalCheckListItem}}></div>
                      <div className='col-1'>
                        <FontAwesomeIcon
                          icon="edit"
                          className="fa-edit"
                          style={{cursor: 'pointer', fontSize: '14px', marginRight: '8px', marginBottom: '2px'}}
                          onClick={() => {
                            setCreateShowModal(true);
                            setItemId(checkList.id);
                          }}
                        />
                        <FontAwesomeIcon
                          icon="trash"
                          className="fa-trash"
                          style={{cursor: 'pointer', fontSize: '12px'}}
                          onClick={() => handleDelete(checkList.id)}
                        />
                      </div>
                    </li>
                  ))}
                </ul>
              </div> :
              <div className='text-center'>
                <div className='py-4 text-center'>Checklist files are Empty</div>
              </div>
            }
          </div>
        </div>
      </div>
    </>
  );
};

export default CheckList;
