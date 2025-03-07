import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";

const RegistrationPage = () => {
  const [formData, setFormData] = useState({
    id: "",
    accountHolder: "",
    balance: "",
    role: "",
    username: "",
    password: "",
  });

  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    // Generate a random ID when the component is mounted
    const randomId = Math.floor(Math.random() * 1000000).toString();
    setFormData((prevFormData) => ({ ...prevFormData, id: randomId }));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.id) newErrors.id = "ID is required";
    if (!formData.accountHolder)
      newErrors.accountHolder = "Account holder name is required";
    if (formData.role !== "ADMIN" && formData.balance <= 0)
      newErrors.balance = "Initial balance must be a positive number";
    if (!formData.role) newErrors.role = "Role is required";
    if (!formData.username) newErrors.username = "Username is required";
    if (!formData.password) {
      newErrors.password = "Password is required";
    } else if (formData.password.length > 12) {
      newErrors.password = "Password must not exceed 12 characters";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      const formattedData = {
        accountDTO: {
          id: formData.id,
          accountHolder: formData.accountHolder,
          balance: formData.role !== "ADMIN" ? formData.balance : undefined, // Exclude balance if role is ADMIN
          role: formData.role,
        },
        userDTO: {
          username: formData.username,
          password: formData.password,
          role: formData.role,
        },
      };
      try {
        const response = await axios.post(
          "http://localhost:8765/accounts/create",
          formattedData
        );
        console.log("Registration response:", response.data);
        navigate("/login");
      } catch (error) {
        console.error("Registration failed:", error);
      }
    }
  };

  return (
    <div
      className="container-fluid d-flex flex-column align-items-center justify-content-center"
      style={{
        minHeight: "100vh",
        backgroundColor: "#f8f9fa", // Soft background color
      }}
    >
      {/* Navbar with Centered Logo and Title */}
      <nav
        className="navbar navbar-light bg-dark w-100 mb-4 d-flex justify-content-center"
        style={{ padding: "10px 20px" }}
      >
        <div className="d-flex align-items-center" style={{ gap: "10px" }}>
          <img
            src="VistaTrust_logo.jpg"
            alt="VistaTrust Logo"
            style={{ width: "50px", height: "50px" }}
          />
          <span
            className="navbar-brand mb-0 h1 text-white"
            style={{ fontFamily: "'Playfair Display', serif", fontSize: "2rem" }}
          >
            VistaTrust Bank
          </span>
        </div>
      </nav>

      {/* Registration Form Section */}
      <div
        className="p-4 rounded shadow bg-white"
        style={{ maxWidth: "500px", width: "100%" }}
      >
        <h2 className="text-center mb-4">Register</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">ID</label>
            <input
              type="text"
              name="id"
              className="form-control"
              value={formData.id}
              onChange={handleChange}
              readOnly // Make the ID field read-only
            />
            {errors.id && <span className="text-danger">{errors.id}</span>}
          </div>
          <div className="mb-3">
            <label className="form-label">Account Holder Name</label>
            <input
              type="text"
              name="accountHolder"
              className="form-control"
              value={formData.accountHolder}
              onChange={handleChange}
              required
            />
            {errors.accountHolder && (
              <span className="text-danger">{errors.accountHolder}</span>
            )}
          </div>
          <div className="mb-3">
            <label className="form-label">Role</label>
            <select
              name="role"
              className="form-control"
              value={formData.role}
              onChange={handleChange}
              required
            >
              <option value="" disabled>
                Choose Role
              </option>
              <option value="ADMIN">ADMIN</option>
              <option value="USER">USER</option>
            </select>
            {errors.role && <span className="text-danger">{errors.role}</span>}
          </div>
          {formData.role !== "ADMIN" && (
            <div className="mb-3">
              <label className="form-label">Initial Balance</label>
              <input
                type="number"
                name="balance"
                className="form-control"
                value={formData.balance}
                onChange={handleChange}
                required
              />
              {errors.balance && (
                <span className="text-danger">{errors.balance}</span>
              )}
            </div>
          )}
          <div className="mb-3">
            <label className="form-label">Username</label>
            <input
              type="text"
              name="username"
              className="form-control"
              value={formData.username}
              onChange={handleChange}
              required
            />
            {errors.username && (
              <span className="text-danger">{errors.username}</span>
            )}
          </div>
          <div className="mb-3">
            <label className="form-label">Password</label>
            <input
              type="password"
              name="password"
              className="form-control"
              value={formData.password}
              onChange={handleChange}
              required
            />
            {errors.password && (
              <span className="text-danger">{errors.password}</span>
            )}
          </div>
          <div className="text-center">
            <Link to="/login" className="text-decoration-none mb-3 d-block">
              Already have an account?
            </Link>
            <button type="submit" className="btn btn-primary px-4">
              Register
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RegistrationPage;














