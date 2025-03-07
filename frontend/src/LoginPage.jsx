import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const LoginPage = () => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    role: "",
  });

  const [errors, setErrors] = useState({});
  const [loginError, setLoginError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.username) newErrors.username = "Username is required";
    if (!formData.password) newErrors.password = "Password is required";
    if (!formData.role) newErrors.role = "Role is required";
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      try {
        console.log("Form data being sent:", formData); // Log form data
        const response = await axios.post(
          "http://localhost:8765/users/login",
          formData
        );
        console.log("Login response:", response.data); // Log response data
        if (response.data) {
          const userDetails = {
            token: response.data.token,
            id: response.data.id,
            role: response.data.role,
          };
          localStorage.setItem("userDetails", JSON.stringify(userDetails));

          if (formData.role.toUpperCase() === "USER") {
            navigate("/userdashboard");
          } else {
            navigate("/dashboard");
          }
        } else {
          setLoginError("Invalid username, password, or role");
        }
      } catch (error) {
        setLoginError("Invalid username, password, or role");
      }
    }
  };

  return (
    <div
      className="container-fluid d-flex flex-column align-items-center justify-content-center"
      style={{ minHeight: "100vh", backgroundColor: "#f8f9fa" }}
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

      {/* Login Form Section */}
      <div className="col-md-10 mx-auto col-lg-5">
        <div className="card mt-3 shadow">
          <div className="card-body">
            <h2 className="text-center mb-4">Login</h2>
            <form
              className="p-4 p-md-5 border rounded-3 bg-body-tertiary"
              onSubmit={handleSubmit}
            >
              <div className="form-floating mb-3">
                <input
                  type="text"
                  name="username"
                  className="form-control"
                  value={formData.username}
                  onChange={handleChange}
                  required
                />
                <label>Username</label>
                {errors.username && (
                  <span className="text-danger">{errors.username}</span>
                )}
              </div>
              <div className="form-floating mb-3">
                <input
                  type="password"
                  name="password"
                  className="form-control"
                  value={formData.password}
                  onChange={handleChange}
                  required
                />
                <label>Password</label>
                {errors.password && (
                  <span className="text-danger">{errors.password}</span>
                )}
              </div>
              <div className="form-floating mb-3">
                <select
                  name="role"
                  className="form-control"
                  value={formData.role}
                  onChange={handleChange}
                  required
                >
                  <option value="" disabled>Select Role</option>
                  <option value="ADMIN">ADMIN</option>
                  <option value="USER">USER</option>
                </select>
                <label>Role</label>
                {errors.role && (
                  <span className="text-danger">{errors.role}</span>
                )}
              </div>
              {loginError && (
                <div className="alert alert-danger">{loginError}</div>
              )}
              <button className="w-100 btn btn-lg btn-primary" type="submit">
                Login
              </button>
              <hr className="my-4" />
              <small className="text-body-secondary">
                By clicking Login, you agree to the terms of use.
              </small>
              <p className="mt-5 mb-3 text-body-secondary">© 2025</p>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
















