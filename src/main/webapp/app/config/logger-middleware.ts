/* eslint no-console: off */
export default () => next => action => {
  if (DEVELOPMENT) {
    const { type, payload, meta, error } = action;

    console.groupCollapsed(type);
    if (error) {
      console.log('Error:', error);
    }
    console.groupEnd();
  }

  return next(action);
};
