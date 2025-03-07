import React, { useState, useEffect } from 'react';
import axios from 'axios';

const UserShowAllLoans = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selectedLoan, setSelectedLoan] = useState(null);
  const [amountPaid, setAmountPaid] = useState('');

  useEffect(() => {
    fetchLoans();
  }, []);

  const fetchLoans = async () => {
    setLoading(true);
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails) {
        const token = userDetails.token;
        const response = await axios.get(`http://localhost:8765/loans/${userDetails.id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setData(response.data);
      }
    } catch (error) {
      setError('There was an error fetching the loans!');
    } finally {
      setLoading(false);
    }
  };

  

  const handleUpdate = (loan) => {
    setSelectedLoan(loan);
    setAmountPaid(loan.amountPaid);
  };

  const handleRepay = async () => {
    const userDetails = JSON.parse(localStorage.getItem('userDetails'));
    if (userDetails) {
      const token = userDetails.token;
      const updatedLoan = {
        ...selectedLoan,
        amountPaid,
      };

      try {
        await axios.put(`http://localhost:8765/loans/repay/${selectedLoan.loanId}`, updatedLoan, {
          headers: { Authorization: `Bearer ${token}` },
        });
        fetchLoans();
        setSelectedLoan(null);
      } catch (error) {
        setError('There was an error updating the loan!');
      }
    }
  };

  const handleCancel = () => {
    setSelectedLoan(null);
    setAmountPaid('');
  };

  return (
    <div className="container">
      <div className="card mt-5">
        <div className="card-body">
          <h2>ALL LOAN DETAILS:</h2>
          {error && <div className="alert alert-danger">{error}</div>}
          {loading ? (
            <p>Loading...</p>
          ) : (
            <table className="table table-striped">
              <thead>
                <tr>
                  <th>Loan ID</th>
                  <th>Loan Type</th>
                  <th>Principal Amount</th>
                  <th>Number of Years</th>
                  <th>CIBIL Score</th>
                  <th>Interest Rate</th>
                  <th>Amount To Be Repaid</th>
                  <th>Amount Paid</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {data.map((loan, index) => (
                  <tr key={index}>
                    <td>{loan.loanId}</td>
                    <td>{loan.loanType}</td>
                    <td>{loan.principalAmount}</td>
                    <td>{loan.numberOfYears}</td>
                    <td>{loan.cibilScore}</td>
                    <td>{loan.interestRate}</td>
                    <td>{loan.amountToBeRepaid}</td>
                    <td>{loan.amountPaid}</td>
                    <td>
                      <button className="btn btn-warning" onClick={() => handleUpdate(loan)}>
                        UPDATE
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      {selectedLoan && (
        <div className="card mt-5">
          <div className="card-body">
            <h2>Update Loan Repayment</h2>
            <form>
              <div className="form-group">
                <label>Amount Paid</label>
                <input
                  type="text"
                  className="form-control"
                  value={amountPaid}
                  onChange={(e) => setAmountPaid(e.target.value)}
                />
              </div>
              <button type="button" className="btn btn-primary" onClick={handleRepay}>
                Save
              </button>
              <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                Cancel
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserShowAllLoans;

