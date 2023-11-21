import {getUsersRaw} from "app/modules/administration/user-management/user-management/user-management.reducer";

export const fetchAllUserSuggestions = async (value: string, selectedUsers, setSuggestions, setLoading) => {
  setLoading(true);
  try {
    const response = await getUsersRaw({
      page: 0,
      size: 30,
      searchText: value,
    });
    const filteredSuggestions = response.data.filter(
      (user) => !selectedUsers.find((selectedUser) => selectedUser.id === user.id)
    );
    setLoading(false);
    setSuggestions(filteredSuggestions);
  } catch (error) {
    setLoading(false);
  }
};

export const handleAddUserForSelect = (user, selectedUsers, setSelectedUsers, setInputValue, setErrorMessage, setSuggestions) => {
  const existingUser = selectedUsers.find(element => element.id === user.id);
  if (existingUser) {
    setErrorMessage('User already added');
  } else {
    const newUser = {
      id: user.id,
      fullName: user.fullName
    };

    setSelectedUsers(prevUsers => [...prevUsers, newUser]);
    setInputValue('');
    setErrorMessage('');
    setSuggestions([]);
  }
};

