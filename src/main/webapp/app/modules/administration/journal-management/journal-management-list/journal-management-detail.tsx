import React from 'react';
import { Link } from 'react-router-dom';

const JournalManagementDetail = () => {
  return (
    <>
      <nav aria-label="breadcrumb" id="custom-breadcrumb">
        <ol className="breadcrumb m-0 p-0">
          <li className="breadcrumb-item">
            <a href="#">Home</a>
          </li>
          <li className="breadcrumb-item">
            <a href="#">User Management</a>
          </li>
          <li className="breadcrumb-item active" aria-current="page">
            View User
          </li>
        </ol>
      </nav>

      <div className="d-flex  b-bottom">
        <div className="me-auto p-2">
          <div className="d-flex">
            <div className="line"></div>
            <h6 className="heading">View User</h6>
          </div>
          <p className="title-description">View the profile details of user</p>
        </div>

        <div className="p-2 m-top">
          <p className="updated-time">Updated on 13 May, 2023</p>
        </div>
        <div className="p-2 m-top">
          <Link to="id/edit">
            <button className="btn custom-btn" type="submit">
              EDIT DETAILS
            </button>
          </Link>
        </div>
      </div>

      <div className="user-details">
        <div className="row pt-2">
          <div className="col-2">
            <p>Full Name</p>
          </div>
          <div className="col-2">
            <p>
              <span>Surya Prakash</span>
            </p>
          </div>
        </div>
        <div className="row pb-2">
          <div className="col-2">
            <p>Email Address</p>
          </div>
          <div className="col-2">
            <p>
              <span>suryaprakash@gmail.com</span>
            </p>
          </div>
        </div>
        <div className="row pb-2">
          <div className="col-2">
            <p>Phone Number</p>
          </div>
          <div className="col-2">
            <p>
              <span>+91 - 9876543210</span>
            </p>
          </div>
        </div>

        <div className="row pb-2">
          <div className="col-2">
            <p>Password</p>
          </div>
          <div className="col-2">
            <p>
              <span>***********</span>
            </p>
          </div>
        </div>

        <div className="row pb-2">
          <div className="col-2">
            <p>Address Line 1</p>
          </div>
          <div className="col-2">
            <p>
              <span> 1/23, Vivekanandhar Street Kirshbaai Main Road Kunoor.</span>
            </p>
          </div>
        </div>

        <div className="row pb-2">
          <div className="col-2">
            <p>State / Province</p>
          </div>
          <div className="col-2">
            <p>
              <span>Tamil Nadu</span>
            </p>
          </div>
        </div>

        <div className="row pb-2">
          <div className="col-2">
            <p>Country</p>
          </div>
          <div className="col-2">
            <p>
              <span>India</span>
            </p>
          </div>
        </div>
      </div>
    </>
  );
};

export default JournalManagementDetail;
