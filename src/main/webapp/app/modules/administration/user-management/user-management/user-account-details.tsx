import React from 'react'
import Breadcrumb from "app/shared/breadcrumb/breadcrumb";
import {useAppSelector} from "app/config/store";
import {formatDateTime} from "app/config/componance-config";
import {Translate, translate} from "react-jhipster";

const UserAccountDetails = () =>{
  const account = useAppSelector(state => state.authentication.account);
  const BreadCrumbRoutes = [
    { name: translate("userdetails_Beadcrumb.Home"), path: '/journal' },
    { name: translate("userdetails_Beadcrumb.User_Details"), path: '' },
  ];

  return( <>
    <div className="pt-2">
      <Breadcrumb props={BreadCrumbRoutes} />
    </div>
    <div className="b-bottom">
      <div className="py-2 pb-3">
        <div className="d-flex">
          <div className="line"></div>
          <h6 className="heading mb-0"> <Translate contentKey="userdetails.User_Details_title"></Translate></h6>
        </div>
        <p className="title-description"> <Translate contentKey="userdetails.User_Details_subtitle"></Translate></p>
      </div>
    </div>

      <div className='col-12'>


              {/*<div className="col-lg-4">*/}
              {/*  <div className="card mb-4">*/}
              {/*    <div className="card-body text-center">*/}

              {/*        <h5 className="my-3">{account?.fullName}</h5>*/}
              {/*        <p className="text-muted mb-1">{account?.email}</p>*/}
              {/*        <p className="text-muted mb-4">Bay Area, San Francisco, CA</p>*/}
              {/*        <div className="d-flex justify-content-center mb-2">*/}
              {/*          <button type="button" className="btn btn-primary">Follow</button>*/}
              {/*          <button type="button" className="btn btn-outline-primary ms-1">Message</button>*/}
              {/*        </div>*/}
              {/*    </div>*/}
              {/*  </div>*/}

              {/*</div>*/}

              <div className="col-lg-8 mt-3">
                <div className="card mb-4">
                  <div className="card-body">
                    <div className="row">
                      <div className="col-sm-3">
                        <p className="mb-0"><Translate contentKey="userdetails.Full_Name"></Translate></p>
                      </div>
                      <div className="col-sm-9">
                        <p className="text-muted mb-0">{account?.fullName}</p>
                      </div>
                    </div>
                    <hr/>
                    <div className="row">
                      <div className="col-sm-3">
                        <p className="mb-0"><Translate contentKey="userdetails.Email_Address"></Translate></p>
                      </div>
                      <div className="col-sm-9">
                        <p className="text-muted mb-0">{account?.email}</p>
                      </div>
                    </div>
                    <hr/>
                    <div className="row">
                      <div className="col-sm-3">
                        <p className="mb-0"><Translate contentKey="userdetails.Created_Date_And_Time"></Translate></p>
                      </div>
                      <div className="col-sm-9">
                        <p className="text-muted mb-0">{formatDateTime(account.createdDate)}</p>
                      </div>
                    </div>
                    <hr/>
                      <div className="row">
                        <div className="col-sm-3">
                          <p className="mb-0"><Translate contentKey="userdetails.Created_By"></Translate></p>
                        </div>
                        <div className="col-sm-9">
                          <p className="text-muted mb-0">{account?.createdBy}</p>
                        </div>
                      </div>
                      <hr/>
                        <div className="row">
                          <div className="col-sm-3">
                            <p className="mb-0"><Translate contentKey="userdetails.Activated"></Translate></p>
                          </div>
                          <div className="col-sm-9">
                            <p className="text-muted mb-0">{account?.activated?'Yes':'No'}</p>
                          </div>
                        </div>


                  </div>
                </div>

              </div>



      </div>


  </>)

}

export default UserAccountDetails
