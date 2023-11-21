import React from 'react';
import { Link } from 'react-router-dom';
import './breadcrumb.scss';

export const Breadcrumb = ({ props }) => {
  return (
    <>
      <nav aria-label="breadcrumb" id="custom-breadcrumb">
        <ol className="breadcrumb m-0 pb-0 ps-0">
          {props?.map((route, index) => (
            <li key={index} className={`breadcrumb-item${index === props.length - 1 ? ' active' : ''}`}>
              {index === props.length - 1 ? <span>{route.name}</span> : <Link to={route.path}>{route.name}</Link>}
            </li>
          ))}
        </ol>
      </nav>
    </>
  );
};

export default Breadcrumb;
