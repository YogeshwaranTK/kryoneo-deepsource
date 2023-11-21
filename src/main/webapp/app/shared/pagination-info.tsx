import React from 'react';
import {translate, Translate} from "react-jhipster";
function calculateRange(currentPage, itemsPerPage, totalItems) {
  const startRange = (currentPage - 1) * itemsPerPage + 1;
  const endRange = Math.min(currentPage * itemsPerPage, totalItems);
  return { startRange, endRange };
}
const PaginationInfo = ({ currentPage, itemsPerPage, totalItems ,pageSize ,article}) => {
  const { startRange, endRange } = calculateRange(currentPage, itemsPerPage, totalItems);
  return (
    <div className="me-auto px-2 table_sort">
      <div className="d-flex">
        <p className="pe-2 m-0"><Translate contentKey="show_result">iShowing results for</Translate></p>
        <select name="cars" id="cars" onChange={pageSize}>
          <option value="10">10</option>
          <option value="20">20</option>
          <option value="50">50</option>
          <option value="100">100</option>
        </select>
        <p className="px-2 m-0">

          <Translate contentKey="items">Items</Translate>
        </p>
      </div>
      <div className="text-color">
        {article?.length > 0 ?
          <div className="text-color pt-1"><p>{startRange} <Translate contentKey="to">to</Translate> {endRange} <Translate contentKey="of">of</Translate> <span>{totalItems} <Translate contentKey="entries">entries</Translate></span></p></div>
          :''}
      </div>
    </div>
  );
};

export default PaginationInfo;
