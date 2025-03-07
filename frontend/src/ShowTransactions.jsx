import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import { AccountContext } from './Dashboard';
import 'bootstrap/dist/css/bootstrap.min.css';

const ShowTransactions = () => {
    const [transactions, setTransactions] = useState([]);
    const { user } = useContext(AccountContext);
    const [transactionToDelete, setTransactionToDelete] = useState(null);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        fetchTransactions();
    }, []);

    const fetchTransactions = async () => {
        try {
            const userDetails = JSON.parse(localStorage.getItem('userDetails'));
            if (userDetails) {
                const token = userDetails.token;
                const response = await axios.get(`http://localhost:8765/transactions/account/${userDetails.id}`, {
                    headers: { 'Authorization': `Bearer ${token}` }
                });

                const transactionsWithFormattedDate = response.data.map((transaction) => {
                    return {
                        ...transaction,
                        date: formatDate(new Date(transaction.date))
                    };
                });

                setTransactions(transactionsWithFormattedDate);
            }
        } catch (error) {
            console.error('Error fetching transactions:', error);
        }
    };

    const handleDelete = (transaction) => {
        setTransactionToDelete(transaction);
        setShowModal(true);
    };

    const confirmDelete = async () => {
        try {
            const userDetails = JSON.parse(localStorage.getItem('userDetails'));
            if (userDetails) {
                const token = userDetails.token;
                await axios.delete(`http://localhost:8765/transactions/${transactionToDelete.transactionId}`, {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                fetchTransactions();
                setShowModal(false);
            }
        } catch (error) {
            console.error('Error deleting transaction:', error);
        }
    };

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
        <div className="container">
            <div className="card mt-5">
                <div className="card-body">
                    <h2>Transactions</h2>
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <th>Transaction ID</th>
                                <th>Type</th>
                                <th>Amount</th>
                                <th>Target Account ID</th>
                                <th>Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {transactions.map((transaction) => (
                                <tr key={transaction.transactionId}>
                                    <td>{transaction.transactionId}</td>
                                    <td>{transaction.type}</td>
                                    <td>{transaction.amount}</td>
                                    <td>{transaction.targetAccountId}</td>
                                    <td>{transaction.date}</td>
                                    <td>
                                        <button
                                            className="btn btn-danger"
                                            onClick={() => handleDelete(transaction)}
                                        >
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            {transactionToDelete && (
                <div className={`modal fade ${showModal ? 'show' : ''}`} style={{ display: showModal ? 'block' : 'none' }} tabIndex="-1" role="dialog">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Confirm Delete</h5>
                                <button type="button" className="close" onClick={() => setShowModal(false)}>
                                    <span>&times;</span>
                                </button>
                            </div>
                            <div className="modal-body">
                                <p>Do you really want to delete this transaction?</p>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>
                                    Cancel
                                </button>
                                <button type="button" className="btn btn-danger" onClick={confirmDelete}>
                                    Yes
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
            {showModal && <div className="modal-backdrop fade show"></div>}
        </div>
    );
};

export default ShowTransactions;
