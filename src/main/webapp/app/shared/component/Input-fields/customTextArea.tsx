import React from 'react';
import '../common-fields.scss'

const CustomTextarea = ({id, name, placeholder, height, disabled, dataCy, field, form}) => {
  const {touched, errors} = form;
  const isError = touched[field.name] && errors[field.name];
  return (
    <>
      <textarea
        id={id}
        name={name}
        placeholder={placeholder}
        data-cy={dataCy}
        className={`form-control common-textarea ${isError ? 'is-invalid' : ''}`}
        style={{height: `${height ? height : '200px'}`}}
        disabled={disabled}
        {...field}
      />
      {isError && <div className="error_class">{errors[field.name]}</div>}
    </>
  );
};


export default CustomTextarea;
