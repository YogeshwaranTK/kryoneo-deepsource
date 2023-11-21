import React, {useEffect, useState} from 'react';
import {Modal} from 'reactstrap';
import {useAppDispatch} from "app/config/store";
import {
  CreateChecklistRequest, putChecklistRequest
} from "app/modules/administration/journal-management/journal-setting/journals-settings.reducer";
import RichText from "app/shared/rich-text/rich-text";

const CheckListCreateUpdate = (props) => {
  const {showModal, handleClose, checkListDetails, itemId} = props;
  const dispatch = useAppDispatch();
  const [inputText, setInputText] = useState('');
  const [inputPosition, setInputPosition] = useState('');

  const handleCancel = () => {
    handleClose();
  };

  useEffect(() => {
    if (itemId !== 0) {
      const foundObjectValue = checkListDetails?.find(item => item.id === itemId);
      setInputText(foundObjectValue.journalCheckListItem)
      setInputPosition(foundObjectValue.displayPosition)
    }
    if (showModal === false) {
      setInputText('')
    }
  }, [showModal])

  const handleInputChange = (value: string) => {
    setInputText(value);
  };

  const handlePositionChange = (e) => {
    setInputPosition(e.target.value);

  };

  const handleSubmit = () => {
    if (itemId !== 0) {
      const data = {
        "journalCheckListItem": inputText,
        "id": itemId,
        "displayPosition": inputPosition
      }

      dispatch(putChecklistRequest(data))
        .then((resultAction) => {
          if (putChecklistRequest.fulfilled.match(resultAction)) {
            handleClose();

          }
        })

    } else {
      const data = {
        "journalCheckListItem": inputText,
        "displayPosition": inputPosition
      }

      dispatch(CreateChecklistRequest(data))
        .then((resultAction) => {
          if (CreateChecklistRequest.fulfilled.match(resultAction)) {
            handleClose();
          }
        })
    }
  }


  return (
    <Modal
      id="AddContributorModel"
      isOpen={showModal}
      toggle={handleClose}
      backdrop="static"
      autoFocus={false}
    >
      <div className="modal-content">
        <div className="modal-header">
          <div className="d-flex">
            <div className="line"></div>
            <h5 className="m-0">Create Item</h5>
          </div>
          <button type="button" className="btn-close" onClick={handleCancel}></button>
        </div>
        <div className="modal-body ps-4">
          <form>
            <div className="pb-3">
              <label className='form-label'>
                Display Position<span className="error_class"></span>
              </label>
              <input
                onChange={handlePositionChange}
                type="number" // Use type="text" to allow only integer input
                id="title"
                name="title"
                placeholder="Enter The Position"
                className="form-control"
                value={inputPosition}
                style={{WebkitAppearance: 'none'}}
                step="1"
              />
              <label className='form-label mt-2'>
                Item<span className="error_class">*</span>
              </label>

              {/*<textarea*/}
              {/*  onChange={handleInputChange}*/}
              {/*  id="title"*/}
              {/*  name="title"*/}
              {/*  placeholder="Enter Item"*/}
              {/*  className="form-control"*/}
              {/*  value={inputText}*/}
              {/*/>*/}

              <RichText
                value={inputText}
                placeHolderText="Enter Item"
                onValueChange={handleInputChange}
              />

            </div>
            <div className="modal-footer justify-content-end">
              <button className="btn custom-btn-secondary" type="button" onClick={handleCancel}>
                CANCEL
              </button>
              <button className="btn custom-btn-secondary" type="button" onClick={handleSubmit}>SUBMIT</button>
            </div>
          </form>
        </div>
      </div>
    </Modal>
  );
};

export default CheckListCreateUpdate;


