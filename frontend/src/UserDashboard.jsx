import React, { useState, useEffect, createContext } from "react";
import { Link, Outlet, useNavigate } from "react-router-dom";
import axios from "axios";

export const UserContext = createContext();

const UserDashboard = () => {
  const [balance, setBalance] = useState(0);
  const [user, setUser] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const userDetails = JSON.parse(localStorage.getItem("userDetails"));
        if (userDetails) {
          const token = userDetails.token;
          const response = await axios.get(
            `http://localhost:8765/accounts/${userDetails.id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
          const accountData = response.data;
          setUser({
            id: accountData.id,
            accountHolderName: accountData.accountHolder,
            balance: accountData.balance,
            role: accountData.role,
          });
          setBalance(accountData.balance);
        }
      } catch (error) {
        console.error("Error fetching account data:", error);
      }
    };

    fetchUserData();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("userDetails");
    navigate("/login");
  };

  return (
    <UserContext.Provider value={{ balance, setBalance }}>
      <div className="d-flex" style={{ height: "100vh" }}>
        {/* Left Sidebar */}
        <nav
          className="d-flex flex-column bg-dark text-white p-3"
          style={{ width: "250px" }}
        >
          {/* Logo and Title at Top-Left Corner */}
          <div className="mb-4">
            <div className="d-flex align-items-center" style={{ gap: "10px" }}>
              <img
                src="https://t4.ftcdn.net/jpg/05/02/14/03/360_F_502140379_ygmHbJXlWbkjelmMtrD4onnEshvcj5ag.jpg"
                alt="VistaTrust Logo"
                style={{ width: "40px", height: "40px" }}
              />
              <h2
                style={{
                  fontFamily: "'Playfair Display', serif",
                  fontSize: "1.5rem",
                  color: "#ffffff",
                  margin: 0,
                }}
              >
                VistaTrust Bank
              </h2>
            </div>
          </div>

          {/* Navigation Links */}
          <ul className="nav flex-column">
            <li className="nav-item">
              <Link to="usermaketransaction" className="nav-link text-white">
                Make A Transaction
              </Link>
            </li>
            <li className="nav-item">
              <Link to="usershowtransaction" className="nav-link text-white">
                Show All Transactions
              </Link>
            </li>
            <li className="nav-item">
              <Link to="userprofile" className="nav-link text-white">
                Profile
              </Link>
            </li>
            <li className="nav-item">
              <Link to="userapplyloan" className="nav-link text-white">
                Apply For Loan
              </Link>
            </li>
            <li className="nav-item">
              <Link to="usershowloans" className="nav-link text-white">
                Show All Loans
              </Link>
            </li>
          </ul>
          <button
            onClick={handleLogout}
            className="btn btn-outline-danger mt-auto"
          >
            Log Out
          </button>
        </nav>

        {/* Main Content */}
        <div className="flex-grow-1 p-4">
          <div className="card">
            <div className="card-body">
              <h2>User Details</h2>
              <p>
                <strong>Account ID:</strong> {user.id}
              </p>
              <p>
                <strong>Account Holder Name:</strong> {user.accountHolderName}
              </p>
              <p>
                <strong>Current Balance:</strong> {balance}
              </p>
            </div>
          </div>
          <Outlet />
        </div>
      </div>
    </UserContext.Provider>
  );
};

export default UserDashboard;
