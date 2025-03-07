import React, { useState, useContext } from 'react';
import axios from 'axios';
import { AccountContext } from './Dashboard';
import 'bootstrap/dist/css/bootstrap.min.css';

const MakeTransaction = () => {
    const [type, setType] = useState('');
    const [otherType, setOtherType] = useState('');
    const [amount, setAmount] = useState('');
    const [targetAccountId, setTargetAccountId] = useState('');
    const { balance, setBalance } = useContext(AccountContext);
    const [showModal, setShowModal] = useState(false);
    const [errorMessage, setErrorMessage] = useState(''); 
    const [isLoading, setIsLoading] = useState(false);

    const handleTransaction = async () => {
        if (!type || !amount || (!targetAccountId && type.toLowerCase() !== 'others')) {
            setErrorMessage('Please fill in all required fields.');
            return;
        }

        setIsLoading(true);
        setErrorMessage('');

        try {
            const userDetails = JSON.parse(localStorage.getItem('userDetails'));
            if (userDetails) {
                const token = userDetails.token;
                const transactionId = Math.floor(Math.random() * 100000) + 1;

                const transactionData = {
                    transactionId,
                    id: userDetails.id,
                    type: type.toLowerCase() === 'others' ? otherType : type,
                    amount: parseFloat(amount),
                    targetAccountId: type.toLowerCase() === 'withdraw' || type.toLowerCase() === 'deposit' ? userDetails.id : targetAccountId
                };

                let response;
                if (type.toLowerCase() === 'transfer') {
                    response = await axios.post('http://localhost:8765/transactions/transfer', transactionData, {
                        headers: { 'Authorization': `Bearer ${token}` }
                    });
                } else if (type.toLowerCase() === 'withdraw') {
                    response = await axios.post('http://localhost:8765/transactions/withdraw', transactionData, {
                        headers: { 'Authorization': `Bearer ${token}` }
                    });
                } else if (type.toLowerCase() === 'deposit') {
                    response = await axios.post('http://localhost:8765/transactions/deposit', transactionData, {
                        headers: { 'Authorization': `Bearer ${token}` }
                    });
                } else {
                    response = await axios.post('http://localhost:8765/transactions', transactionData, {
                        headers: { 'Authorization': `Bearer ${token}` }
                    });
                }

                const newBalance = balance + (type.toLowerCase() === 'deposit' ? parseFloat(amount) : -parseFloat(amount));
                setBalance(newBalance);
                setShowModal(true); // Show success modal

                setType('');
                setOtherType('');
                setAmount('');
                setTargetAccountId('');
            }
        } catch (error) {
            console.error('Error during transaction:', error);
            setErrorMessage('An error occurred during the transaction. Please try again later.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleTypeChange = (e) => {
        const newType = e.target.value;
        setType(newType);

        if (newType.toLowerCase() === 'withdraw' || newType.toLowerCase() === 'deposit') {
            const userDetails = JSON.parse(localStorage.getItem('userDetails'));
            if (userDetails) {
                setTargetAccountId(userDetails.id);
            }
        } else {
            setTargetAccountId('');
        }
        setErrorMessage('');
    };

    return (
        <div className="container">
            <div className="card mt-5">
                <div className="card-body">
                    <h2>Make a Transaction</h2>
                    {errorMessage && (
                        <div className="alert alert-danger" role="alert">
                            {errorMessage}
                        </div>
                    )}
                    <form>
                        <div className="form-group">
                            <label>Type</label>
                            <select className="form-control" value={type} onChange={handleTypeChange}>
                                <option value="">Select Type</option>
                                <option value="Transfer">Transfer</option>
                                <option value="Withdraw">Withdraw</option>
                                <option value="Deposit">Deposit</option>
                                <option value="Others">Others</option>
                            </select>
                        </div>
                        {type.toLowerCase() === 'others' && (
                            <div className="form-group">
                                <label>Other Type</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    value={otherType}
                                    onChange={(e) => setOtherType(e.target.value)}
                                />
                            </div>
                        )}
                        <div className="form-group">
                            <label>Amount</label>
                            <input
                                type="number"
                                className="form-control"
                                value={amount}
                                onChange={(e) => setAmount(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label>Target Account ID</label>
                            <input
                                type="text"
                                className="form-control"
                                value={targetAccountId}
                                onChange={(e) => setTargetAccountId(e.target.value)}
                                readOnly={type.toLowerCase() === 'withdraw' || type.toLowerCase() === 'deposit'}
                            />
                        </div>
                        <button
                            type="button"
                            className="btn btn-info"
                            onClick={handleTransaction}
                            disabled={isLoading}
                        >
                            {isLoading ? 'Processing...' : `Make ${type || 'Transaction'}`}
                        </button>
                    </form>
                </div>
            </div>

            {/* Modal */}
            <div className={`modal fade ${showModal ? 'show' : ''}`} style={{ display: showModal ? 'block' : 'none' }} tabIndex="-1" role="dialog">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">Transaction Successful</h5>
                            <button type="button" className="close" onClick={() => setShowModal(false)}>
                                <span>&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <p>Your transaction has been completed successfully!</p>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>
                                Close
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            {showModal && <div className="modal-backdrop fade show"></div>}
        </div>
    );
};

export default MakeTransaction;



















