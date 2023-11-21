export const SelectCustomStyle = {
  control: (provided, state) => ({
    ...provided,
    fontSize: '13px', // Adjust the font size as per your requirements
    height: '15px',
    padding: '1px 8px',
    paddingLeft: '2px',
    minHeight: '34px',
  }),
};
export const SelectCustomStyleForm = {
  control: (provided, state) => ({
    ...provided,
    fontSize: '14px', // Adjust the font size as per your requirements
    borderColor: '#F0F0F0', // Customize the border color
    background: '#F0F0F0',
  }),
  option: (provided, state) => ({
    ...provided,
    background: state.isSelected ? 'var(--btn-primary-color)' : '#fff', // Text color for options
  }),
};

export const formatDateTime = (databaseDateTime: string): string => {
  if(databaseDateTime !== null) {
    const formattedDateTime = new Date(databaseDateTime).toLocaleString('en-IN', {
      timeZone: 'Asia/Kolkata',
      hour12: true,
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: 'numeric',
      minute: 'numeric',
      // second: 'numeric'
    });
    return formattedDateTime;
  }else {
    return '-'
  }
};

export const formatDate = (databaseDateTime: string): string => {

  if(databaseDateTime !== null) {
    const formattedDateTime = new Date(databaseDateTime).toLocaleString('en-IN', {
      timeZone: 'Asia/Kolkata',
      hour12: true,
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      // hour: 'numeric',
      // minute: 'numeric',
      // second: 'numeric'
    });
    return formattedDateTime;
  }else {
    return '-'
  }
};
