import React, { useState } from 'react';
import axios from 'axios';
import Modal from 'react-modal';

Modal.setAppElement('#root'); // Set the app element for accessibility

const TransactionManage = () => {
  const [selectedTransaction, setSelectedTransaction] = useState(null);
  const [searchTransactionId, setSearchTransactionId] = useState('');
  const [searchAccountId, setSearchAccountId] = useState('');
  const [searchResult, setSearchResult] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [message, setMessage] = useState('');

  const deleteTransaction = async (transactionId) => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails && transactionId) {
        const token = userDetails.token;
        await axios.delete(`http://localhost:8765/transactions/${transactionId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setSelectedTransaction(null); // Close the transaction modal after deletion
        closeDeleteModal(); // Close the delete confirmation modal after deletion
        setSearchResult(null); // Clear search result after deletion
        setMessage(''); // Clear any previous messages
      }
    } catch (error) {
      console.error('Error deleting transaction:', error);
    }
  };

  const fetchTransactionByTransactionId = async (transactionId) => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails && transactionId) {
        const token = userDetails.token;
        const response = await axios.get(`http://localhost:8765/transactions/${transactionId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data) {
          setSearchResult([response.data]); // Ensure search result is an array
          setMessage(''); // Clear any previous messages
        } else {
          setSearchResult([]);
          setMessage('No transaction found with this ID.');
        }
      }
    } catch (error) {
      console.error('Error fetching transaction:', error);
      setMessage('No transaction found with this ID.');
    }
  };

  const fetchTransactionsByAccountId = async (accountId) => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails && accountId) {
        const token = userDetails.token;
        const response = await axios.get(`http://localhost:8765/transactions/account/${accountId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data.length > 0) {
          // Format the date for each transaction
          const transactionsWithFormattedDate = response.data.map((transaction) => {
            return {
              ...transaction,
              date: formatDate(new Date(transaction.date)),
            };
          });

          setSearchResult(transactionsWithFormattedDate);
          setMessage(''); // Clear any previous messages
        } else {
          setSearchResult([]);
          setMessage('No transactions made.');
        }
      }
    } catch (error) {
      console.error('Error fetching transactions by account ID:', error);
      setMessage('No transactions made.');
    }
  };

  const openModal = (transaction) => {
    setSelectedTransaction(transaction);
  };

  const closeModal = () => {
    setSelectedTransaction(null);
  };

  const openDeleteModal = () => setShowDeleteModal(true);
  const closeDeleteModal = () => setShowDeleteModal(false);

  // Format date as "YYYY-MM-DD HH:mm:ss"
  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  };

  return (
    <div>
      <br />
      <div style={{ display: 'flex', gap: '10px' }}>
        <div>
          <input
            type="text"
            placeholder="Enter Transaction ID"
            value={searchTransactionId}
            onChange={(e) => setSearchTransactionId(e.target.value)}
            style={{
              borderRadius: '4px',
              padding: '5px',
              border: '1px solid #ccc',
              boxSizing: 'border-box'
            }}
          />
          <button
            onClick={() => fetchTransactionByTransactionId(searchTransactionId)}
            className="btn btn-primary"
            style={{
              borderRadius: '4px', // Matching the shape of the input field
              padding: '5px 10px',
              marginLeft: '5px' // Add some space between input and button
            }}
          >
            Search Transaction
          </button>
        </div>
        <div>
          <input
            type="text"
            placeholder="Enter Account ID"
            value={searchAccountId}
            onChange={(e) => setSearchAccountId(e.target.value)}
            style={{
              borderRadius: '4px',
              padding: '5px',
              border: '1px solid #ccc',
              boxSizing: 'border-box'
            }}
          />
          <button
            onClick={() => fetchTransactionsByAccountId(searchAccountId)}
            className="btn btn-primary"
            style={{
              borderRadius: '4px', // Matching the shape of the input field
              padding: '5px 10px',
              marginLeft: '5px' // Add some space between input and button
            }}
          >
            Search Transactions
          </button>
        </div>
      </div>
      <br />
      {message && <p>{message}</p>}
      {searchResult && searchResult.length > 0 ? (
        <div className="transaction-buttons d-flex flex-wrap gap-3">
          {searchResult.map((transaction) => (
            <div key={transaction.transactionId} className="mb-3">
              <button onClick={() => openModal(transaction)} className="btn btn-primary">
                Transaction ID: {transaction.transactionId}
              </button>
            </div>
          ))}
        </div>
      ) : (
        !message && <p>No transactions to display. Please enter a Transaction ID or Account ID to search.</p>
      )}
      <Modal
        isOpen={!!selectedTransaction}
        onRequestClose={closeModal}
        contentLabel="Transaction Details"
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
        {selectedTransaction && (
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
            <h5 className="modal-title">Transaction ID: {selectedTransaction.transactionId}</h5>
            <p><strong>ID:</strong> {selectedTransaction.id}</p>
            <p><strong>Amount:</strong> {selectedTransaction.amount}</p>
            <p><strong>Type:</strong> {selectedTransaction.type}</p>
            <p><strong>Target Account ID:</strong> {selectedTransaction.targetAccountId}</p>
            <p><strong>Date:</strong> {selectedTransaction.date}</p>
            <button
              onClick={openDeleteModal}
              className="btn btn-danger"
              style={{ userSelect: 'none' }}
            >
              Delete Transaction
            </button>
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
          <p>Do you want to delete this transaction?</p>
          <button
            type="button"
            className="btn btn-danger"
            onClick={() => deleteTransaction(selectedTransaction.transactionId)}
          >
            Yes
          </button>
        </div>
      </Modal>
    </div>
  );
};

export default TransactionManage;


           











