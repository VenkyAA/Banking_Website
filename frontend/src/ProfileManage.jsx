import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import { AccountContext } from './Dashboard';

Modal.setAppElement('#root');

const ProfileManage = () => {
  const [profiles, setProfiles] = useState([]);
  const [selectedProfile, setSelectedProfile] = useState(null);
  const [editProfile, setEditProfile] = useState(null);
  const [searchProfileId, setSearchProfileId] = useState('');
  const [searchResult, setSearchResult] = useState(null);
  const { balance, setBalance } = useContext(AccountContext);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [profileToDelete, setProfileToDelete] = useState(null);

  useEffect(() => {
    fetchAllProfiles();
  }, []);

  const fetchAllProfiles = async () => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails) {
        const token = userDetails.token;
        const response = await axios.get('http://localhost:8765/profiles', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setProfiles(response.data);
        setSearchResult(null); // Clear search result when fetching all profiles
      }
    } catch (error) {
      console.error('Error fetching all profiles:', error);
    }
  };

  const deleteProfile = async (profileId) => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails && profileId) {
        const token = userDetails.token;
        await axios.delete(`http://localhost:8765/profiles/${profileId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setSelectedProfile(null);
        fetchAllProfiles();
      }
    } catch (error) {
      console.error('Error deleting profile:', error);
    }
  };

  const updateProfile = async () => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails && editProfile) {
        const token = userDetails.token;
        await axios.put(`http://localhost:8765/profiles/${editProfile.id}`, editProfile, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setEditProfile(null);
        setSelectedProfile(null);
        fetchAllProfiles(); // Refresh the profile list after updating
      }
    } catch (error) {
      console.error('Error updating profile:', error);
    }
  };

  const fetchProfileById = async (profileId) => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails && profileId) {
        const token = userDetails.token;
        const response = await axios.get(`http://localhost:8765/profiles/${profileId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setSearchResult(response.data ? [response.data] : []); // Ensure search result is an array
      }
    } catch (error) {
      console.error('Error fetching profile:', error);
    }
  };

  const openModal = (profile) => {
    setSelectedProfile(profile);
    setEditProfile(null);
  };

  const closeModal = () => {
    setSelectedProfile(null);
    setEditProfile(null);
  };

  const openDeleteModal = (profile) => {
    setProfileToDelete(profile);
    setShowDeleteModal(true);
  };

  const closeDeleteModal = () => {
    setProfileToDelete(null);
    setShowDeleteModal(false);
  };

  const confirmDelete = async () => {
    if (profileToDelete) {
      await deleteProfile(profileToDelete.id);
      setProfileToDelete(null);
      closeDeleteModal();
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditProfile({ ...editProfile, [name]: value });
  };

  return (
    <div>
      <br />
      <div>
        <input
          type="text"
          placeholder="Enter Profile ID"
          value={searchProfileId}
          onChange={(e) => setSearchProfileId(e.target.value)}
          style={{
            borderRadius: '4px',
            padding: '5px',
            marginRight: '5px', // Add some space between input and button
            border: '1px solid #ccc',
            boxSizing: 'border-box'
          }}
        />
        <button
          onClick={() => fetchProfileById(searchProfileId)}
          className="btn btn-primary"
          style={{
            borderRadius: '4px', // Matching the shape of the input field
            padding: '5px 10px'
          }}
        >
          Search
        </button>
      </div>
      <br />
      {searchResult && searchResult.length > 0 ? (
        <div className="d-flex flex-wrap gap-3">
          {searchResult.map((profile) => (
            <button key={profile.id} onClick={() => openModal(profile)} className="btn btn-primary">
              ID: {profile.id} Profile
            </button>
          ))}
        </div>
      ) : (
        profiles.length > 0 && (
          <div className="d-flex flex-wrap gap-3">
            {profiles.map((profile) => (
              <button key={profile.id} onClick={() => openModal(profile)} className="btn btn-primary">
                ID: {profile.id} Profile
              </button>
            ))}
          </div>
        )
      )}
      <Modal
        isOpen={!!selectedProfile}
        onRequestClose={closeModal}
        contentLabel="Profile Details"
        style={{
          content: {
            width: '300px',
            height: 'fit-content',
            margin: 'auto',
            borderRadius: '8px',
            padding: '20px',
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)'
          }
        }}
      >
        {selectedProfile && (
          <div>
            <button
              onClick={closeModal}
              style={{
                position: 'absolute',
                top: '10px',
                right: '10px',
                border: 'none',
                background: 'none',
                fontSize: '20px',
                fontWeight: 'bold',
                cursor: 'pointer'
              }}
            >
              &times;
            </button>
            {!editProfile ? (
              <div>
                <h5 className="modal-title">ID {selectedProfile.id} Profile</h5>
                <p><strong>Govt ID:</strong> {selectedProfile.govtId}</p>
                <p><strong>Employment:</strong> {selectedProfile.employment}</p>
                <p><strong>Address:</strong> {selectedProfile.address}</p>
                <p><strong>Phone Number:</strong> {selectedProfile.phoneNumber}</p>
                <p><strong>Email ID:</strong> {selectedProfile.emailId}</p>
                <button
                  onClick={() => setEditProfile(selectedProfile)}
                  className="btn btn-warning mr-2"
                >
                  Update Profile
                </button>
                <button
                  onClick={() => openDeleteModal(selectedProfile)}
                  className="btn btn-danger"
                >
                  Delete Profile
                </button>
              </div>
            ) : (
              <div>
                <h5 className="modal-title">Update Profile ID: {editProfile.id}</h5>
                <div className="form-group">
                  <label>Govt ID:</label>
                  <input
                    type="text"
                    name="govtId"
                    value={editProfile.govtId}
                    onChange={handleInputChange}
                    className="form-control"
                  />
                </div>
                <div className="form-group">
                  <label>Employment:</label>
                  <input
                    type="text"
                    name="employment"
                    value={editProfile.employment}
                    onChange={handleInputChange}
                    className="form-control"
                  />
                </div>
                <div className="form-group">
                  <label>Address:</label>
                  <input
                    type="text"
                    name="address"
                    value={editProfile.address}
                    onChange={handleInputChange}
                    className="form-control"
                  />
                </div>
                <div className="form-group">
                  <label>Phone Number:</label>
                  <input
                    type="text"
                    name="phoneNumber"
                    value={editProfile.phoneNumber}
                    onChange={handleInputChange}
                    className="form-control"
                  />
                </div>
                <div className="form-group">
                  <label>Email ID:</label>
                  <input
                    type="email"
                    name="emailId"
                    value={editProfile.emailId}
                    onChange={handleInputChange}
                    className="form-control"
                  />
                </div>
                <button onClick={updateProfile} className="btn btn-success mt-3">Save Changes</button>
              </div>
            )}
          </div>
        )}
      </Modal>
      
      <Modal
        isOpen={showDeleteModal}
        onRequestClose={closeDeleteModal}
        contentLabel="Confirm Delete"
        style={{
          content: {
            width: '300px',
            height: 'fit-content',
            margin: 'auto',
            borderRadius: '8px',
            padding: '20px',
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)'
          }
        }}
      >
        <div>
          <h5 className="modal-title">Confirm Delete</h5>
          <button
            onClick={closeDeleteModal}
            style={{
              position: 'absolute',
              top: '10px',
              right: '10px',
              border: 'none',
              background: 'none',
              fontSize: '20px',
              fontWeight: 'bold',
              cursor: 'pointer'
            }}
          >
            &times;
          </button>
          <p>Do you really want to delete this profile?</p>
          <button type="button" className="btn btn-secondary" onClick={closeDeleteModal}>
            Cancel
          </button>
          <button
            type="button"
            className="btn btn-danger"
            onClick={confirmDelete}
          >
            Yes
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default ProfileManage;





