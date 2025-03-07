import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import Modal from 'react-modal'; // Assuming you're using react-modal
import { AccountContext } from './Dashboard';
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap CSS

Modal.setAppElement('#root'); // Set the app element for accessibility

const Accounts = () => {
  const [accounts, setAccounts] = useState([]);
  const [selectedAccount, setSelectedAccount] = useState(null);
  const [searchAccountId, setSearchAccountId] = useState('');
  const [searchResult, setSearchResult] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const { balance, setBalance } = useContext(AccountContext);

  useEffect(() => {
    fetchAllAccounts();
  }, []);

  const fetchAllAccounts = async () => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails) {
        const token = userDetails.token;
        const response = await axios.get('http://localhost:8765/accounts', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setAccounts(response.data);
        setSearchResult(null); // Clear search result when fetching all accounts
      }
    } catch (error) {
      console.error('Error fetching all accounts:', error);
    }
  };

  const deleteAccount = async () => {
    if (!selectedAccount) return;

    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails) {
        const token = userDetails.token;
        await axios.delete(`http://localhost:8765/accounts/${selectedAccount.id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        closeModal(); // Close the modal after deletion
        fetchAllAccounts(); // Refresh the list of accounts after deletion
      }
    } catch (error) {
      console.error('Error deleting account:', error);
    }
  };

  const fetchAccountById = async (accountId) => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails && accountId) {
        const token = userDetails.token;
        const response = await axios.get(`http://localhost:8765/accounts/${accountId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setSearchResult(response.data ? [response.data] : []); // Ensure search result is an array
      }
    } catch (error) {
      console.error('Error fetching account:', error);
    }
  };

  const openModal = (account) => {
    setSelectedAccount(account);
  };

  const closeModal = () => {
    setSelectedAccount(null);
    setShowDeleteModal(false);
  };

  const openDeleteModal = () => {
    setShowDeleteModal(true);
  };

  return (
    <div>
      <br />
      <div>
        <input
          type="text"
          placeholder="Enter Account ID"
          value={searchAccountId}
          onChange={(e) => setSearchAccountId(e.target.value)}
          style={{
            borderRadius: '4px',
            padding: '5px',
            marginRight: '5px', // Add some space between input and button
            border: '1px solid #ccc',
            boxSizing: 'border-box'
          }}
        />
        <button
          onClick={() => fetchAccountById(searchAccountId)}
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
        <div className="account-buttons d-flex flex-wrap gap-3">
          {searchResult.map((account) => (
            <button key={account.id} onClick={() => openModal(account)} className="btn btn-primary">
              {account.role} ID: {account.id}
            </button>
          ))}
        </div>
      ) : (
        accounts.length > 0 && (
          <div className="account-buttons d-flex flex-wrap gap-3">
            {accounts.map((account) => (
              <button key={account.id} onClick={() => openModal(account)} className="btn btn-primary">
                {account.role} ID: {account.id}
              </button>
            ))}
          </div>
        )
      )}
      <Modal
        isOpen={!!selectedAccount}
        onRequestClose={closeModal}
        contentLabel="Account Details"
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
        {selectedAccount && (
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
            <h5 className="modal-title">{selectedAccount.role} ID: {selectedAccount.id}</h5>
            <p><strong>Account Holder:</strong> {selectedAccount.accountHolder}</p>
            <p><strong>Balance:</strong> {selectedAccount.balance.toFixed(2)}</p>
            <button
              onClick={openDeleteModal}
              className="btn btn-danger"
              style={{ userSelect: 'none' }}
            >
              Delete Account
            </button>
          </div>
        )}
      </Modal>

      {/* Bootstrap Modal for Delete Confirmation */}
      <div className={`modal fade ${showDeleteModal ? 'show' : ''}`} style={{ display: showDeleteModal ? 'block' : 'none' }} tabIndex="-1" role="dialog">
        <div className="modal-dialog" role="document">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title">Confirm Delete</h5>
              <button type="button" className="close" onClick={closeModal}>
                <span>&times;</span>
              </button>
            </div>
            <div className="modal-body">
              <p>Do you want to delete this account?</p>
            </div>
            <div className="modal-footer">
              <button type="button" className="btn btn-secondary" onClick={closeModal}>
                Cancel
              </button>
              <button type="button" className="btn btn-danger" onClick={deleteAccount}>
                Yes
              </button>
            </div>
          </div>
        </div>
      </div>
      {showDeleteModal && <div className="modal-backdrop fade show"></div>}
    </div>
  );
};

export default Accounts;





