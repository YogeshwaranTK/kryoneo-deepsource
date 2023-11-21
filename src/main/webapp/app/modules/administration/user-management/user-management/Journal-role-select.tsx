import React, {useEffect, useState} from 'react';
import Select from 'react-select';
import {getJournalList} from "app/modules/administration/user-management/user-management/user-management.reducer";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {Storage, translate, Translate} from "react-jhipster";
import AsyncSelect from "react-select/async";
import {getJournalRoles} from "app/modules/administration/journal-management/contributors/contributors.reducer";


function JournalRoleSelect({roles, setRoles, journalId}) {
  const handleRemoveGroup = (id) => {
    if (id === 1) {
      return;
    }
    const updatedRoleList = roles.filter(role => role.id !== id);
    setRoles(updatedRoleList);
  };

  const preDefinedRoles = [
    {
      value: 'AUTHOR',
      label: 'Author'
    },
    {
      value: 'REVIEWER',
      label: 'Reviewer'
    },
    {
      value: 'EDITORIAL_USER',
      label: 'Editorial User'
    }
  ]

  const loadOptions = async (inputValue) => {
    try {
      const response = await getJournalList({
        page: 0,
        size: 30,
        searchText: inputValue,
      });
      const data = await response.data;

      return data.map((item) => ({
        value: item.id,
        label: item.title,
      }));

    } catch (error) {
      console.error('Error fetching options:', error);
      return [];
    }
  };

  const [defaultOptions, setDefaultOptions] = useState([])
  const [editorialRules, setEditorialRules] = useState([])

  useEffect(() => {
    getJournalList({
      page: 0,
      size: 30,
      searchText: '',
    }).then(response => {
      const data = response.data;
      if (data !== null) {
        const valueData = data.map((item) => ({
          value: item.id,
          label: item.title,
        }));
        setDefaultOptions(valueData);
      }
    })
      .catch(errorDetails => {
        console.error(errorDetails);
      });

  }, []);

  const updateSelectedJournal = (roleId: number, updatedJournal) => {
    const updatedRoles = roles.map((role) => {
      if (role.id === roleId) {
        return {...role, selectedJournal: updatedJournal};
      }
      return role;
    });
    setRoles(updatedRoles);
    Storage.local.set('journal_id', updatedJournal.value);
    getJournalRoles().then(response => {
      const data = response.data;
      if (data !== null) {
        const valueData = data.map((item) => ({
          value: item.id,
          label: item.roleName,
        }));
        setEditorialRules(valueData)
      }
    })
      .catch(errorDetails => {
        console.error(errorDetails);
      });
  };

  useEffect(()=>{
    if (journalId){
      Storage.local.set('journal_id', journalId);
      getJournalRoles().then(response => {
        const data = response.data;
        if (data !== null) {
          const valueData = data.map((item) => ({
            value: item.id,
            label: item.roleName,
          }));
          setEditorialRules(valueData)
        }
      })
        .catch(errorDetails => {
          console.error(errorDetails);
        });
    }
  },[])
  const handleRoleSelectChange = (roleId: number, updatedJournal) => {
    const updatedRoles = roles.map((role) => {
      if (role.id === roleId) {
        return {...role, roleType: updatedJournal};
      }
      return role;
    });
    setRoles(updatedRoles);
  };

  const handleEditorialRoleSelectChange = (roleId: number, updatedJournal) => {
    console.log(updatedJournal)
    const updatedRoles = roles.map((role) => {
      if (role.id === roleId) {
        return {...role, editorialRole: updatedJournal};
      }
      return role;
    });
    setRoles(updatedRoles);
  };

  const handleAddGroup = () => {
    const maxId = Math.max(...roles.map((role) => role.id), 0);

    const newId = maxId + 1;
    setRoles([...roles, {
      id: newId,
      editorialRole: {value: 0, label: ''}, roleType: {value: '', label: ''}, selectedJournal: {value: 0, label: ''}
    }]);
  };
  console.log(roles)
  return (
    <div>
      {roles?.map((role, index) => (
        <div className='row pt-2 title_class_want' key={role.id}>
          <div className="col-8 pb-2">
            <label id={`roleLabel${role.id}`} className="form-label">
              <Translate contentKey="userManagementCreateUser.Journal"></Translate> - {role.id}
            </label>
            <AsyncSelect
              value={role?.selectedJournal?.label !== '' ?  role?.selectedJournal :''}
              defaultOptions={defaultOptions}
              loadOptions={loadOptions}
              onChange={(value) =>
                updateSelectedJournal(role.id, value)
              }
              placeholder='Select Journal'
              isDisabled={!!journalId}
            />
          </div>
          <div className="col-3 pb-2">
            <label id={`userLabel${role.id}`} className="form-label">
              Journal Roles</label>
            <Select
              menuPlacement="top"
              placeholder={translate('PlaceHolders.SelectJournalGroup')}
              options={preDefinedRoles}
              value={role.roleType.value !== '' ? role.roleType :''}
              onChange={(selectedOptions) => handleRoleSelectChange(role.id, selectedOptions)}
              isDisabled={!!journalId}
            />
          </div>
          {!journalId && <div className="col-1 pb-2 pt-4 mt-2 px-0">
            {role.id !== 1 && (
              <button type='button' className='btn btn--primary dy_journal_group_add'
                      onClick={() => handleRemoveGroup(role.id)}>
                <FontAwesomeIcon icon="trash"/>
              </button>
            )}
            {index === roles.length - 1 && (
              <button type='button' className='btn btn--primary dy_journal_add' onClick={handleAddGroup}>
                <FontAwesomeIcon icon="plus"/>
              </button>
            )}
          </div>}

          {role.roleType.value === 'EDITORIAL_USER' && <div className="col-5 pb-2">
            <label id={`roleLabel${role.id}`} className="form-label">
              Editorial Roles
            </label>
            <Select
              menuPlacement="top"
              placeholder='Select Editorial Users'
              options={editorialRules}
              value={role.editorialRole.label !== '' ? role.editorialRole:''}
              onChange={(selectedOptions) => handleEditorialRoleSelectChange(role.id, selectedOptions)}
            />
          </div>}
        </div>
      ))}
    </div>
  );
}

export default JournalRoleSelect;
