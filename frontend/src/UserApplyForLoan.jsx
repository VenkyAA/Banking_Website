import React, { useState, useEffect } from 'react';
import axios from 'axios';

const UserApplyForLoan = () => {
  const [principalAmount, setPrincipalAmount] = useState('');
  const [numberOfYears, setNumberOfYears] = useState('');
  const [interestRate, setInterestRate] = useState('');
  const [amountToBeRepaid, setAmountToBeRepaid] = useState('');
  const [amountPaid, setAmountPaid] = useState('');
  const [loanType, setLoanType] = useState('');
  const [cibilScore, setCibilScore] = useState('');
  const [isRepaymentUpdate, setIsRepaymentUpdate] = useState(false); 
  const [showAlert, setShowAlert] = useState(false); 
  const [errorMessage, setErrorMessage] = useState(''); 
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (cibilScore && principalAmount && numberOfYears) {
      // Include your calculation logic here
    }
  }, [cibilScore, principalAmount, numberOfYears]);

  const generateLoanId = () => {
    return Math.floor(Math.random() * 100000) + 1;
  };

  const handleLoan = async () => {
    if (!loanType || !principalAmount || !numberOfYears || !cibilScore) {
      setErrorMessage('Please fill in all required fields.');
      return;
    }

    setIsLoading(true);
    setErrorMessage('');
    try {
      const userDetails = JSON.parse(localStorage.getItem('userDetails'));
      if (userDetails) {
        const token = userDetails.token;
        const loanData = {
          loanId: generateLoanId(), 
          id: userDetails.id, 
          principalAmount,
          numberOfYears,
          interestRate,
          amountToBeRepaid,
          amountPaid,
          loanType,
          cibilScore,
        };

        const response = isRepaymentUpdate
          ? await axios.put(`http://localhost:8765/loans/repay/${loanData.loanId}`, loanData, {
              headers: { Authorization: `Bearer ${token}` },
            })
          : await axios.post('http://localhost:8765/loans', loanData, {
              headers: { Authorization: `Bearer ${token}` },
            });

        const updatedLoan = response.data;

        setInterestRate(updatedLoan.interestRate);
        setAmountToBeRepaid(updatedLoan.amountToBeRepaid);
        setAmountPaid(updatedLoan.amountPaid);

        setShowAlert(true); 
        setTimeout(() => setShowAlert(false), 3000); 

        setIsRepaymentUpdate(false); 
      }
    } catch (error) {
      console.error('There was an error saving the loan!', error);
      setErrorMessage('An error occurred while saving the loan. Please try again later.');
    } finally {
      setIsLoading(false);
      resetForm();
    }
  };

  const resetForm = () => {
    setLoanType('');
    setPrincipalAmount('');
    setNumberOfYears('');
    setCibilScore('');
    setInterestRate('');
    setAmountToBeRepaid('');
    setAmountPaid('');
    setIsRepaymentUpdate(false); 
  };

  return (
    <div className="container">
      <div className="card mt-5">
        <div className="card-body">
          <h2>Apply for Loan</h2>
          {errorMessage && (
            <div className="alert alert-danger" role="alert">
              {errorMessage}
            </div>
          )}
          <form>
            <div className="form-group">
              <label>Loan Type</label>
              <input
                type="text"
                className="form-control"
                required
                value={loanType}
                onChange={(e) => setLoanType(e.target.value)}
              />
            </div>
            <div className="form-group">
              <label>Principal Amount</label>
              <input
                type="text"
                className="form-control"
                required
                value={principalAmount}
                onChange={(e) => setPrincipalAmount(e.target.value)}
              />
            </div>
            <div className="form-group">
              <label>Number of Years</label>
              <input
                type="text"
                className="form-control"
                required
                value={numberOfYears}
                onChange={(e) => setNumberOfYears(e.target.value)}
              />
            </div>
            <div className="form-group">
              <label>CIBIL Score</label>
              <input
                type="text"
                className="form-control"
                required
                value={cibilScore}
                onChange={(e) => setCibilScore(e.target.value)}
              />
            </div>
            <div className="form-group">
              <label>Interest Rate</label>
              <input
                type="text"
                className="form-control"
                required
                value={interestRate}
                readOnly
              />
            </div>
            <div className="form-group">
              <label>Amount To Be Repaid</label>
              <input
                type="text"
                className="form-control"
                required
                value={amountToBeRepaid}
                readOnly
              />
            </div>
            <div className="form-group">
              <label>Amount Paid</label>
              <input
                type="text"
                className="form-control"
                required
                value={amountPaid}
                onChange={(e) => setAmountPaid(e.target.value)}
              />
            </div>
            <button
              type="button"
              className="btn btn-info"
              onClick={handleLoan}
              disabled={isLoading}
            >
              {isLoading ? 'Applying...' : 'Apply'}
            </button>
            <button
              type="reset"
              className="btn btn-secondary"
              onClick={resetForm}
              disabled={isLoading}
            >
              Cancel
            </button>
          </form>
        </div>
      </div>
      <br />

      {showAlert && (
        <div className="alert alert-success alert-dismissible fade show" role="alert">
          Loan applied successfully
          <button type="button" className="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
      )}
    </div>
  );
};

export default UserApplyForLoan;

