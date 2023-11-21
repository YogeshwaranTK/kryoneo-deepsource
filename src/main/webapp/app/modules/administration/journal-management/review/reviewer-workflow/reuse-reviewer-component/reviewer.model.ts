

export const blindTypeSet = (type) => {
  if (type === "ANONYMOUS_REVIEWER_AND_ANONYMOUS_AUTHOR") {
    return "Double Blind"
  } else if (type === "OPEN") {
    return "Open"
  }
}


export const getFileExtension = (filename) => {
  const lastDotIndex = filename.lastIndexOf(".");
  if (lastDotIndex !== -1) {
    return filename.substring(lastDotIndex);
  } else {
    return null; // or any other value to indicate no extension found
  }
}
