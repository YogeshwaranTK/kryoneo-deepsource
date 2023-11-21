import React from 'react';

interface ButtonInterFace{
  JbuttonValue?:string
  type?:"reset" | "button" | "submit",
  onclick?:()=>void,
  className?:string
}


export  const  JButton = (props:ButtonInterFace) =>{
  const  {JbuttonValue,type,onclick,className} = props;
  return(
    <>
      <button onClick={onclick} className={className} type={type}>
        {JbuttonValue}
      </button>
    </>
  )
}

export  default JButton
