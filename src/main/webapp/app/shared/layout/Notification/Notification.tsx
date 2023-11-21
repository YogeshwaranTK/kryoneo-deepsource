import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import './Notification.scss'
import {getNotification, removeNotification} from "app/shared/layout/Notification/Notification.reducer";
import {useAppDispatch, useAppSelector} from "app/config/store";
import {Translate} from "react-jhipster";

const NotificationComponent = ({isOpen, onClose}) =>{

  const dispatch = useAppDispatch()
  const notificationsMain = useAppSelector(state => state.notificationManagement.notifications);
  const removeStatus = useAppSelector(state => state.notificationManagement.removeNotify);
  const [nodata, setNoData] = useState([]);

  useEffect(() => {
    if (removeStatus){
      dispatch(getNotification())
    }
  }, [removeStatus]);

  useEffect(() => {
    if (notificationsMain.length>0){
      setNoData([...notificationsMain])
    }
  }, [notificationsMain]);

  const clearNotifications =()=>{
    const Ids = nodata.map(item => item.id);
    dispatch(removeNotification({ notificationIds:Ids }));
    setNoData([])
  }

  const RemoveNotification =(id,index)=>{
    setNoData((prevData) =>
      prevData.map((notify, i) => (i === index ? { ...notify, removing: true } : notify))
    );
    setTimeout(() => {
      dispatch(removeNotification({ notificationIds: [id] }));
    }, 300); // Adjust the delay time as needed
  }

  return(
        <div id="notification" className=" border">
          <div className={`notify-header ${isOpen ? 'open' : 'closed'}`}>
          <div className="d-flex p-2 b-bottom">
            <h4 className="heading m-0"><Translate contentKey="notification.Notification"></Translate></h4>
            <a className="ms-auto" onClick={clearNotifications}><Translate contentKey="buttons.clearAllCamel"></Translate></a>
          </div>
          </div>
          <div className={`notify-body ${isOpen ? 'open' : 'closed'}`}>
          {nodata.length === 0 ?
            <div className=" d-flex justify-content-center px-2 pb-2 mt-3">
              <div className="padding-size ps-3"><p className="notify-name mb-3"><span><Translate contentKey="notification.NoNotifyFound"></Translate></span></p></div>
            </div>:null}

            {nodata?.map((notify, i) => (
              <div
                className={`notification-item d-flex px-2 pb-2 b-bottom mt-3 ${notify.removing ? 'removing' : ''}`}
                key={`notify-${i}`}
                onAnimationEnd={() => {
                  // Remove the notification from state after the animation ends
                  if (notify.removing) {
                    const updatedNodata = [...nodata];
                    updatedNodata.splice(i, 1);
                    setNoData(updatedNodata);
                  }
                }}
              >
                <div className={`journal-profile color-${notify.title.slice(0, 1).toUpperCase()}`}>
                  {notify.title.slice(0, 2).toUpperCase()}
                </div>
                <div className="padding-size">
                  <p className="notify-name">
                      <span>
                        {notify.title} “{notify.desc}”
                      </span>
                  </p>
                  <div className="d-flex footer">
                    <p>{notify.formattedCreatedDate}</p>
                    <p className="ps-2">Author</p>
                  </div>
                </div>
                <div className="">
                  <FontAwesomeIcon
                    onClick={() => RemoveNotification(notify.id, i)}
                    icon="trash"
                    className="px-1 mb-0"
                  />
                </div>
              </div>
            ))}
          </div>
   </div>
  )
}

export default NotificationComponent;



