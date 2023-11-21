import React, { useEffect, useState } from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

type ChildProps = {
  readonly?:boolean,
  value: string;
  onValueChange: (value: string) => void;
  placeHolderText: string;
};

export const RichText = (props) => {
  const { value, onValueChange, placeHolderText ,readonly} = props
  const [editorHtml, setEditorHtml] = useState(value);

  useEffect(() => {
    setEditorHtml(value);
  }, [value]);

  const handleChange = (content: string) => {
    setEditorHtml(content);
    onValueChange(content); // Invoke the callback function with the new value
  };

  const modules = {
    toolbar: [
      [{ header: [1, 2, 3, 4, 5, 6, false] }],
      ['bold', 'italic', 'underline', 'strike'], // toggled buttons
      ['blockquote', 'code-block'],
      [{ header: 1 }, { header: 2 }], // custom button values
      [{ list: 'ordered' }, { list: 'bullet' }],
      [{ script: 'sub' }, { script: 'super' }], // superscript/subscript
      [{ indent: '-1' }, { indent: '+1' }], // outdent/indent
      [{ direction: 'rtl' }], // text direction
      [{ color: [] }, { background: [] }], // dropdown with defaults
      [{ font: [] }],
      [{ align: [] }],
      ['clean'],
      ['link', 'image', 'video'],
    ],
  };

  const formats = [
    'header',
    'bold',
    'italic',
    'underline',
    'strike',
    'list',
    'script',
    'blockquote',
    'code-block',
    'indent',
    'direction',
    'color',
    'background',
    'font',
    'align',
    'clean',
    'bullet',
    'link',
    'image',
    'video',
  ];

  return (
    <ReactQuill
      readOnly={readonly}
      value={editorHtml}
      onChange={handleChange}
      modules={modules}
      formats={formats}
      style={{height: `${props.height? props.height : '300px'}`}}
      className=  'custom-editor'
      placeholder={placeHolderText}
    />
  );
};

export default RichText;
