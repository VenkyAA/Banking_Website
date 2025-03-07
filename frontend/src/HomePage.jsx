import React from "react";
import { Link } from "react-router-dom";

const HomePage = () => {
  return (
    <div
      className="container-fluid d-flex flex-column justify-content-center align-items-center"
      style={{
        height: "100vh",
        backgroundColor: "#f8f9fa", // Light background color to maintain contrast
      }}
    >
      {/* Bank Name with Logo at the Top */}
      <div
        className="d-flex align-items-center mb-4"
        style={{ gap: "15px" }}
      >
        <img
          src="VistaTrust_logo.jpg"
          alt="VistaTrust Logo"
          style={{ width: "60px", height: "60px" }}
        />
        <h1
          style={{
            fontFamily: "'Playfair Display', serif",
            fontSize: "4rem",
            fontWeight: "bold",
            textShadow: "2px 2px 4px rgba(0, 0, 0, 0.3)", // Subtle shadow for better readability
            color: "black",
          }}
        >
          VistaTrust Bank
        </h1>
      </div>

      {/* Welcome Message */}
      <h2
        className="mb-4"
        style={{
          fontFamily: "'Roboto', sans-serif",
          fontSize: "1.8rem",
          fontWeight: "lighter",
          textAlign: "center",
          color: "black",
        }}
      >
        Welcome to VistaTrust Bank – Secure. Simple. Trusted.
      </h2>

      {/* New Button-Based Layout */}
      <div
        className="d-flex flex-column gap-3"
        style={{
          backgroundColor: "rgba(255, 255, 255, 0.9)", // Light background for buttons
          padding: "20px 30px",
          borderRadius: "15px",
          boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.3)",
        }}
      >
        <p style={{ fontSize: "1.1rem", textAlign: "center", color: "black" }}>
          Create an account or log in to access our banking services.
        </p>
        <Link to="/register" className="btn btn-primary btn-lg">
          Register
        </Link>
        <Link to="/login" className="btn btn-secondary btn-lg">
          Login
        </Link>
      </div>
    </div>
  );
};

export default HomePage;






