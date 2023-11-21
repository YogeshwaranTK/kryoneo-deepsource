import React, { useState, useEffect } from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { Modal, Progress } from 'reactstrap';
const Sweetalert = (props) => {
  const { AlertHandleClose, setCloseButton, buttonHandle, buttonName} = props;
  const [modalVisible, setModalVisible] = useState(true);
  const [progress, setProgress] = useState(0);
  const handleCancel = () => {
    AlertHandleClose();
  };
  useEffect(() => {
    const interval = setInterval(() => {
      if (progress < 100) {
        setProgress(progress + 1);
      }
    }, 20); // Adjust the interval speed as needed (in milliseconds).
    return () => clearInterval(interval); // Cleanup interval on unmount.
  }, [progress]);

  // useEffect(() => {
  //   setTimeout(() => {
  //     AlertHandleClose();
  //   }, 2800); // Adjust the time as needed (in milliseconds).
  // }, []);

  const handleButton = () => {
    AlertHandleClose();
  }

  return (
    // <Modal id="sweet_alert" isOpen={props.alertModal} toggle={AlertHandleClose} backdrop="static" autoFocus={false}>

    <Modal id="sweet_alert" isOpen={props.alertModal} toggle={AlertHandleClose} backdrop="static" autoFocus={false}>
        <div className="alert_header">
          <FontAwesomeIcon icon="circle-xmark" onClick={handleCancel} />
        </div>
        <div className="modal-body text-center">
          <img src="content/images/checked.png" />
          <h4 className='mb-0'>{props.alertTitle}</h4>
          <p className="delete-warning mb-0 pt-2 pb-2 mb-2" dangerouslySetInnerHTML={{ __html: props.alertSubtitle }}></p>
          {buttonHandle ? <button className={'btn custom-btn'} onClick={handleButton}>{buttonName? buttonName : 'Done'}</button> : ''}

        </div>
      {modalVisible ? ( <Progress value={progress} />      ) : null}
    </Modal>
  );
};

export default Sweetalert;
