import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import HomePage from "./HomePage";
import RegistrationPage from "./RegistrationPage";
import Dashboard from "./Dashboard";
import Profile from "./Profile";
import LoginPage from "./LoginPage";
import Accounts from "./Accounts";
import TransactionManage from "./TransactionManage.jsx";
import ProfileManage from "./ProfileManage.jsx";
import LoanManage from "./LoanManage.jsx";
import UserDashboard from "./UserDashboard";
import UserProfile from "./UserProfile.jsx";
import UserMakeTransaction from "./UserMakeTransaction.jsx";
import UserShowTransactions from "./UserShowTransactions.jsx";
import UserApplyForLoan from "./UserApplyForLoan.jsx";
import UserShowAllLoans from "./UserShowAllLoans.jsx";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/register" element={<RegistrationPage />} />
        <Route path="/login" element={<LoginPage />} />
        
        {/* Dashboard for regular users */}
        <Route path="/dashboard" element={<Dashboard />}>
          <Route path="profile" element={<Profile />} />
          <Route path="accounts" element={<Accounts />} />
          <Route path="transactionmanage" element={<TransactionManage />} />
          <Route path="profilemanage" element={<ProfileManage />} />
          <Route path="loanmanage" element={<LoanManage />} />
        </Route>

        {/* UserDashboard for USER role */}
        <Route path="/userdashboard" element={<UserDashboard />}>
          <Route path="usermaketransaction" element={<UserMakeTransaction />} />
          <Route path="usershowtransaction" element={<UserShowTransactions />} />
          <Route path="userprofile" element={<UserProfile />} />
          <Route path="userapplyloan" element={<UserApplyForLoan/>} />
          <Route path="usershowloans" element={<UserShowAllLoans/>} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;







