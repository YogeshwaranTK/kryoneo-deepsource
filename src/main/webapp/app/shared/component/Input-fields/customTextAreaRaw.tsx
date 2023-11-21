import React from 'react';

const CustomTextareaRaw = (props) => {
  return (
    <textarea
      name={props.name}
      id={props.id}
      placeholder={props.placeholder}
      data-cy={props.dataCy}
      className="form-control common-textarea"
      style={{height: `${props.height ? props.height : '200px'}`}}
      value={props.value}
      onChange={(e) => props.onChange(e.target.value)}
    />
  )
}

export default CustomTextareaRaw;
