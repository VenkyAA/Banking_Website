import React, { useState, useEffect } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

const UserProfile = () => {
    const [address, setAddress] = useState("");
    const [emailId, setEmailId] = useState("");
    const [employment, setEmployment] = useState("");
    const [govtId, setGovtId] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [data, setData] = useState([]);
    const [editing, setEditing] = useState(false);
    const [currentProfileId, setCurrentProfileId] = useState(null);
    const [profileCreated, setProfileCreated] = useState(false);
    const [errorMessage, setErrorMessage] = useState(''); 
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        getProfiles();
    }, []);

    const getProfiles = async () => {
        try {
            const userDetails = JSON.parse(localStorage.getItem("userDetails"));
            if (userDetails) {
                const token = userDetails.token;
                const response = await axios.get(
                    `http://localhost:8765/profiles/${userDetails.id}`,
                    { headers: { Authorization: `Bearer ${token}` } }
                );
                setData([response.data]);
                if(response.data) setProfileCreated(true);
            }
        } catch (error) {
            console.error("There was an error getting the profiles!", error);
        }
    };

    const handleProfile = async () => {
        if (!address || !emailId || !employment || !govtId || !phoneNumber) {
            setErrorMessage('Please fill in all required fields.');
            return;
        }
        setIsLoading(true);
        setErrorMessage('');
        try {
            const userDetails = JSON.parse(localStorage.getItem("userDetails"));
            if (userDetails) {
                const token = userDetails.token;
                const profileData = {
                    id: userDetails.id,
                    govtId,
                    employment,
                    address,
                    phoneNumber,
                    emailId,
                };
                if (editing) {
                    await axios.put(
                        `http://localhost:8765/profiles/${currentProfileId}`,
                        profileData,
                        { headers: { Authorization: `Bearer ${token}` } }
                    );
                } else {
                    await axios.post("http://localhost:8765/profiles", profileData, {
                        headers: { Authorization: `Bearer ${token}` },
                    });
                    setProfileCreated(true);
                }
                getProfiles();
                setEditing(false);
                setCurrentProfileId(null);
            }
        } catch (error) {
            console.error("There was an error saving the profile!", error);
            setErrorMessage('An error occurred while saving the profile. Please try again later.');
        } finally {
            setIsLoading(false);
            setAddress("");
            setEmailId("");
            setEmployment("");
            setGovtId("");
            setPhoneNumber("");
        }
    };

    const handleUpdate = (profile) => {
        setAddress(profile.address);
        setEmailId(profile.emailId);
        setEmployment(profile.employment);
        setGovtId(profile.govtId);
        setPhoneNumber(profile.phoneNumber);
        setEditing(true);
        setCurrentProfileId(profile.id);
        setErrorMessage('');
    };

    const handleCancel = () => {
        setAddress("");
        setEmailId("");
        setEmployment("");
        setGovtId("");
        setPhoneNumber("");
        setEditing(false);
        setCurrentProfileId(null);
        setErrorMessage('');
    };

    return (
        <div className="container">
            <div className="card mt-5">
                <div className="card-body">
                    <h2>Profile</h2>
                    {errorMessage && (
                        <div className="alert alert-danger" role="alert">
                            {errorMessage}
                        </div>
                    )}
                    {!profileCreated || editing ? (
                        <form>
                            <div className="form-group">
                                <label>Address</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    required
                                    value={address}
                                    onChange={(e) => setAddress(e.target.value)}
                                />
                            </div>
                            <div className="form-group">
                                <label>Email</label>
                                <input
                                    type="email"
                                    className="form-control"
                                    required
                                    value={emailId}
                                    onChange={(e) => setEmailId(e.target.value)}
                                />
                            </div>
                            <div className="form-group">
                                <label>Employment</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    required
                                    value={employment}
                                    onChange={(e) => setEmployment(e.target.value)}
                                />
                            </div>
                            <div className="form-group">
                                <label>Government ID</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    required
                                    value={govtId}
                                    onChange={(e) => setGovtId(e.target.value)}
                                />
                            </div>
                            <div className="form-group">
                                <label>Phone Number</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    required
                                    value={phoneNumber}
                                    onChange={(e) => setPhoneNumber(e.target.value)}
                                />
                            </div>
                            <button
                                type="button"
                                className="btn btn-info"
                                onClick={handleProfile}
                                disabled={isLoading}
                            >
                                {isLoading ? 'Saving...' : 'Save'}
                            </button>
                            <button
                                type="reset"
                                className="btn btn-secondary"
                                onClick={handleCancel}
                                disabled={isLoading}
                            >
                                Cancel
                            </button>
                        </form>
                    ) : (
                        <div>
                            <table className="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Address</th>
                                        <th>Email</th>
                                        <th>Employment</th>
                                        <th>Government ID</th>
                                        <th>Phone Number</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {data.map((profile, index) => (
                                        <tr key={index}>
                                            <td>{profile.address}</td>
                                            <td>{profile.emailId}</td>
                                            <td>{profile.employment}</td>
                                            <td>{profile.govtId}</td>
                                            <td>{profile.phoneNumber}</td>
                                            <td>
                                                <button
                                                    className="btn btn-warning"
                                                    onClick={() => handleUpdate(profile)}
                                                >
                                                    UPDATE
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default UserProfile;



