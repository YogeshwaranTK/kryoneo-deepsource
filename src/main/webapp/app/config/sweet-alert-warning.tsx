import React, { useState, useEffect } from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { Modal, Progress } from 'reactstrap';
const SweetalertWarning = (props) => {
  const { AlertHandleClose,  buttonHandle, buttonName, alertHandleConfirm} = props;
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

  const handleButton = () => {
    AlertHandleClose();
  }

  const handleConfirmButton = () => {
    alertHandleConfirm();
  }

  return (

    <Modal id="sweet_alert" isOpen={props.alertModal} toggle={AlertHandleClose} backdrop="static" autoFocus={false}>
      <div className="alert_header">
        <FontAwesomeIcon icon="circle-xmark" onClick={handleCancel} />
      </div>
      <div className="modal-body text-center mb-3">
        <img src="content/images/info-circle.png" />
        <h4 className='mb-0'>{props.alertTitle}</h4>
        <p className="delete-warning mb-0 pt-2 pb-2 mb-2" dangerouslySetInnerHTML={{ __html: props.alertSubtitle }}></p>
        {buttonHandle ? <div><button className={'btn  btn-secondary'} onClick={handleButton}>{buttonName? buttonName : 'Done'}</button>
          <button className={'btn btn-primary ms-4'} onClick={handleConfirmButton}>Confirm</button> </div>: ''}

      </div>
      {/*{modalVisible ? ( <Progress value={progress} />      ) : null}*/}
    </Modal>
  );
};

export default SweetalertWarning;
