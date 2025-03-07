import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import { UserContext } from './UserDashboard';
import 'bootstrap/dist/css/bootstrap.min.css';

const UserShowTransactions = () => {
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const { user } = useContext(UserContext);

    useEffect(() => {
        fetchTransactions();
    }, []);

    const fetchTransactions = async () => {
        setLoading(true);
        setError(null);
        try {
            const userDetails = JSON.parse(localStorage.getItem('userDetails'));
            if (userDetails) {
                const token = userDetails.token;
                const response = await axios.get(`http://localhost:8765/transactions/account/${userDetails.id}`, {
                    headers: { 'Authorization': `Bearer ${token}` }
                });

                // Convert all transaction dates to formatted "YYYY-MM-DD HH:mm:ss"
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
        } finally {
            setLoading(false);
        }
    };

    // Function to format date as "YYYY-MM-DD HH:mm:ss"
    const formatDate = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
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
                    {loading && <p>Loading transactions...</p>}
                    {error && <p className="text-danger">{error}</p>}
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <th>Transaction ID</th>
                                <th>Type</th>
                                <th>Amount</th>
                                <th>Target Account ID</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            {transactions.map((transaction) => (
                                <tr key={transaction.transactionId}>
                                    <td>{transaction.transactionId}</td>
                                    <td>{transaction.type}</td>
                                    <td>{transaction.amount}</td>
                                    <td>{transaction.targetAccountId}</td>
                                    <td>{transaction.date}</td> {/* Already formatted */}
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default UserShowTransactions;

