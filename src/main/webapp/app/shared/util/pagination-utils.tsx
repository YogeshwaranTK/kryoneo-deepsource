export const handlePagination = (currentPage, setPagination, pagination) =>
  setPagination({
    ...pagination,
    activePage: currentPage,
  });
