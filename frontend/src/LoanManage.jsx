import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import { AccountContext } from './Dashboard';

Modal.setAppElement('#root');

const LoanManage = () => {
  const [loans, setLoans] = useState([]);
  const [selectedLoan, setSelectedLoan] = useState(null);
  const [editLoan, setEditLoan] = useState(null);
  const [searchLoanId, setSearchLoanId] = useState('');
  const [searchAccountId, setSearchAccountId] = useState('');
  const [searchResult, setSearchResult] = useState(null);
  const { balance, setBalance } = useContext(AccountContext);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [loanToDelete, setLoanToDelete] = useState(null);

  useEffect(() => {
    fetchAllLoans();
  }, []);

  const fetchAllLoans = async () => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails) {
        const token = userDetails.token;
        const response = await axios.get('http://localhost:8765/loans', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setLoans(response.data);
        setSearchResult(null); // Clear search result when fetching all loans
      }
    } catch (error) {
      console.error('Error fetching loans:', error);
    }
  };

  const deleteLoan = async (loanId) => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails && loanId) {
        const token = userDetails.token;
        await axios.delete(`http://localhost:8765/loans/${loanId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setSelectedLoan(null);
        fetchAllLoans();
      }
    } catch (error) {
      console.error('Error deleting loan:', error);
    }
  };

  const updateLoan = async () => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails && editLoan) {
        const token = userDetails.token;
        await axios.put(`http://localhost:8765/loans/repay/${editLoan.loanId}`, editLoan, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setEditLoan(null);
        setSelectedLoan(null);
        setSearchLoanId(editLoan.loanId); // Set the search field with the loanId after saving changes
        fetchLoanByLoanId(editLoan.loanId); // Fetch the updated loan details
      }
    } catch (error) {
      console.error('Error updating loan:', error);
    }
  };

  const fetchLoanByLoanId = async (loanId) => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails && loanId) {
        const token = userDetails.token;
        const response = await axios.get(`http://localhost:8765/loans/loan/${loanId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setSearchResult(response.data ? [response.data] : []); // Ensure search result is an array
      }
    } catch (error) {
      console.error('Error fetching loan:', error);
    }
  };

  const fetchLoansByAccountId = async (accountId) => {
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails && accountId) {
        const token = userDetails.token;
        const response = await axios.get(`http://localhost:8765/loans/${accountId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        setSearchResult(response.data);
      }
    } catch (error) {
      console.error('Error fetching loans by account ID:', error);
    }
  };

  const openModal = (loan) => {
    setSelectedLoan(loan);
    setEditLoan(null);
  };

  const closeModal = () => {
    setSelectedLoan(null);
    setEditLoan(null);
  };

  const openDeleteModal = (loan) => {
    setLoanToDelete(loan);
    setShowDeleteModal(true);
  };

  const closeDeleteModal = () => {
    setLoanToDelete(null);
    setShowDeleteModal(false);
  };

  const confirmDelete = async () => {
    if (loanToDelete) {
      await deleteLoan(loanToDelete.loanId);
      setLoanToDelete(null);
      closeDeleteModal();
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditLoan({ ...editLoan, [name]: value });
  };

  return (
    <div>
      <br />
      <div style={{ display: 'flex', gap: '10px' }}>
        <div>
          <input
            type="text"
            placeholder="Enter Loan ID"
            value={searchLoanId}
            onChange={(e) => setSearchLoanId(e.target.value)}
            style={{
              borderRadius: '4px',
              padding: '5px',
              border: '1px solid #ccc',
              boxSizing: 'border-box'
            }}
          />
          <button
            onClick={() => fetchLoanByLoanId(searchLoanId)}
            className="btn btn-primary"
            style={{
              borderRadius: '4px', // Matching the shape of the input field
              padding: '5px 10px',
              marginLeft: '5px' // Add some space between input and button
            }}
          >
            Search Loan
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
            onClick={() => fetchLoansByAccountId(searchAccountId)}
            className="btn btn-primary"
            style={{
              borderRadius: '4px', // Matching the shape of the input field
              padding: '5px 10px',
              marginLeft: '5px' // Add some space between input and button
            }}
          >
            Search Account
          </button>
        </div>
      </div>
      <br />
      {searchResult && searchResult.length > 0 ? (
        <div className="d-flex flex-wrap gap-3">
          {searchResult.map((loan) => (
            <button key={loan.loanId} onClick={() => openModal(loan)} className="btn btn-primary">
              Loan ID: {loan.loanId}
            </button>
          ))}
        </div>
      ) : (
        loans.length > 0 && (
          <div className="d-flex flex-wrap gap-3">
            {loans.map((loan) => (
              <button key={loan.loanId} onClick={() => openModal(loan)} className="btn btn-primary">
                Loan ID: {loan.loanId}
              </button>
            ))}
          </div>
        )
      )}
      <Modal
        isOpen={!!selectedLoan}
        onRequestClose={closeModal}
        contentLabel="Loan Details"
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
        {selectedLoan && (
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
            {!editLoan ? (
              <div>
                <h5 className="modal-title">Loan ID: {selectedLoan.loanId}</h5>
                <p><strong>ID:</strong> {selectedLoan.id}</p>
                <p><strong>Loan Type:</strong> {selectedLoan.loanType}</p>
                <p><strong>Amount Borrowed:</strong> {selectedLoan.principalAmount}</p>
                <p><strong>No. Of Years:</strong> {selectedLoan.numberOfYears}</p>
                <p><strong>Interest Rate:</strong> {selectedLoan.interestRate}</p>
                <p><strong>Amount to Be Repaid:</strong> {selectedLoan.amountToBeRepaid.toFixed(2)}</p>
                <p><strong>Amount Paid So Far:</strong> {selectedLoan.amountPaid.toFixed(2)}</p>
                <button
                  onClick={() => setEditLoan(selectedLoan)}
                  className="btn btn-warning mr-2"
                >
                  Repay Loan
                </button>
                <button
                  onClick={() => openDeleteModal(selectedLoan)}
                  className="btn btn-danger"
                >
                  Delete Loan
                </button>
              </div>
            ) : (
              <div>
                <h5 className="modal-title">Repay Loan ID: {editLoan.loanId}</h5>
                <div className="form-group">
                  <label>Amount Paid So Far:</label>
                  <input
                    type="text"
                    name="amountPaid"
                    value={editLoan.amountPaid}
                    onChange={handleInputChange}
                    className="form-control"
                  />
                </div>
                <button onClick={updateLoan} className="btn btn-success mt-3">
                  Save Changes
                </button>
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
          <p>Do you really want to delete this loan?</p>
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

export default LoanManage;






