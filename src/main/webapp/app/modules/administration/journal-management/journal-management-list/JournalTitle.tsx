import React from 'react';

export interface JournalTitleModel {
  title?: string;
  sub_title?: string;
}

interface BreasTitleModel {
  value?: JournalTitleModel;
}

export const JournalTitle = (props: BreasTitleModel) => {
  const { value } = props;
  return (
    <>
      <div className="b-bottom">
        <div className="p-2">
          <div className="d-flex">
            <div className="line"></div>
            <h6 className="heading pb-0 mb-0">{value.title}</h6>
          </div>
          <p className="title-description">{value.sub_title}</p>
        </div>
      </div>
    </>
  );
};

export default JournalTitle;
