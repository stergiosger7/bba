import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { registerAPICall } from '../services/AuthService'
import { Alert, Button } from 'react-bootstrap';

const BarberRegisterComponent = () => {
  const [error, setError] = useState('')   
  const [successMessage, setSuccessMessage] = useState('') // success state
  const navigate = useNavigate();
  const [barberRegister, setBarberRegister] = useState({
    name: '',
    username: '',
    phone: '',
    password: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setBarberRegister(prev => ({
      ...prev,
      [name]: value
    }));
  };
  

  function handleRegisterForm(e) {
    e.preventDefault()

    const register = { name: barberRegister.name, username: barberRegister.username, phone: barberRegister.phone, password:barberRegister.password }
    console.log(register)

    registerAPICall(register)
      .then((response) => {
        console.log("Barber registered:", response.data)
        setSuccessMessage("Barber registered successfully!") // success feedback
        setError('') // reset error
      })
      .catch((error) => {
        console.error("Registration failed:", error)

        if (error.response && error.response.data && error.response.data.message) {
          setError(error.response.data.message) //message from backend
        } else {
          setError("Invalid username or password!")
          console.error(error);
        }
        setSuccessMessage('') // reset success when is error
      })
  }

  return (
    <div className="bg-white" style={{ minHeight: '100vh', padding: '40px 20px' }}>
      {/* Back Button - Above Card */}
      <div className="container" style={{ maxWidth: '500px', marginBottom: '20px' }}>
        <Button className="fw-bold"
          variant="link" 
          onClick={() => navigate('/barbers/dashboard')}
          style={{
            padding: '0',
            color: '#0b1432ff',
            textDecoration: 'none',
            fontWeight: '500'
          }}
        >
          ← Back to DashBoard
        </Button>
      </div>
      
      <div className="card shadow-lg p-4" style={{ width: '420px', borderRadius: '16px' }}>
        <h2 className="text-center mb-4">Register Barber</h2>

        {/* Error Alert */}
        {error && <Alert variant="danger">{error}</Alert>}

        {/* Success Alert */}
        {successMessage && (
          <div className="alert alert-success text-center" role="alert">
            {successMessage}
          </div>
        )}

        <form onSubmit={handleRegisterForm}>
          <div className="mb-3">
            <label className="form-label">Barber Name</label>
            <input
              type="text"
              name="name"
              value={barberRegister.name}
              onChange={handleChange}
              className="form-control"
              placeholder="Enter barber's name"
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Username</label>
            <input
              type="text"
              name="username"
              value={barberRegister.username}
              onChange={handleChange}
              className="form-control"
              placeholder="Enter username"
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Phone</label>
            <input
              type="tel"
              name= "phone"
              value={barberRegister.phone}
              onChange={handleChange}
              className="form-control"
              placeholder="Enter phone number"
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Password</label>
            <input
              type="password"
              name="password"
              value={barberRegister.password}
              onChange={handleChange}
              className="form-control"
              placeholder="Enter password"
              required
            />
          </div>

          <button type="submit" className="btn btn-dark w-100 mt-2"
            style={{ color:'#ffffffff',backgroundColor: '#0b1432' }}>
            Register
          </button>
        </form>
      </div>
    </div>
  )
}

export default BarberRegisterComponent
