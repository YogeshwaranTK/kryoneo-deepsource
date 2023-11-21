// ErrorModal.tsx
import React, {useEffect} from 'react';
import {Modal, ModalBody, ModalFooter, Button, Progress} from 'reactstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

interface ErrorModalProps {
  errorCode:number;
  isOpen: boolean;
  message: string;
  onClose: () => void;
}



const ErrorModal: React.FC<ErrorModalProps> = ({errorCode, isOpen, message, onClose }) => {
  useEffect(() => {
    setTimeout(() => {
      onClose();
    }, 950000); // Adjust the time as needed (in milliseconds).
  }, []);
  return (

  <Modal id="sweet_alert" isOpen={isOpen} toggle={onClose} backdrop="static" autoFocus={false}>
    <div className="alert_header">
      <FontAwesomeIcon icon="circle-xmark" onClick={()=>onClose()} />
    </div>
    <div className="modal-body text-center">
      {
        errorCode === 403? <img src="content/images/error-icon.png" />:  <img src="content/images/error-icon.png" />
      }

      <h5 className='mb-3'>{message}</h5>
      {/*<p className="delete-warning mb-0 pt-2 pb-2 mb-2">{message}</p>*/}
    </div>
    {/*{modalVisible ? ( <Progress value={progress} />      ) : null}*/}
  </Modal>
  );
};

export default ErrorModal;
