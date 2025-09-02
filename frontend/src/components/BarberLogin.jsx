import React, { useState } from 'react';
import { loginAPICall, saveLoggedInUser, storeToken } from '../services/AuthService';
import { useNavigate } from 'react-router-dom';
import { Form, Button, Alert } from 'react-bootstrap';

const BarberLogin = () => {
  const [usernameOrPhone, setUsernameOrPhone] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();


  /** 
    async function handleLoginForm(e){

        e.preventDefault();
        try {
          const response = await loginAPICall(usernameOrPhone, password);
          console.log('Login response:' ,response.data);

          if (!response.data?.accessToken) {
            throw new Error('No access token received');
          }
          if (!response.data?.role) {
            throw new Error('No role received');
          }
            ....
          }catch (error) {
          setError("Invalid username or password!")
          console.error(error);
        }
  **/
 
  function handleLoginForm(e){

        e.preventDefault();

        loginAPICall(usernameOrPhone, password)
          .then((response) => {
            console.log('Login response:' ,response.data);

            if (!response.data?.accessToken) {
              throw new Error('No access token received');
            }

            if (!response.data?.role) {
              throw new Error('No role received');
            }

            //const token = 'Basic ' + window.btoa(username + ":" + password);
            const token = 'Bearer ' + response.data.accessToken;

            const role = response.data.role;
            storeToken(token);

            saveLoggedInUser(usernameOrPhone,role);
            navigate("/barbers/dashboard")

            window.location.reload(false);
        }).catch(error => {
            setError("Invalid username or password!")
            console.error(error);
        })
  }

  return (
    <div className="bg-white" style={{ minHeight: '100vh', padding: '40px 20px' }}>
      {/* Back Button - Above Card */}
      <div className="container" style={{ maxWidth: '500px', marginBottom: '20px' }}>
        <Button className="fw-bold"
          variant="link" 
          onClick={() => navigate('/')}
          style={{
            padding: '0',
            color: '#0b1432ff',
            textDecoration: 'none',
            fontWeight: '500'
          }}
        >
          ← Back to Home
        </Button>
      </div>

    <div className="container d-flex justify-content-center">
      <div className="card shadow p-4" style={{ maxWidth: '460px', width: '100%'}}>
        <div className="card-body text-center"> 
          <div className="icon-d-flex justify-content-center mb-3">
                  <img 
                    src="/barber-login.png" 
                    alt="Barber Login" 
                    className="img-fluid rounded-circle"
                    style={{ width: '100px', height: '100px', overflow:'hidden', objectFit: 'cover', margin: '0 auto'}}
                  />
          </div>
          <h2 className="card-title text-center mb-4">Barber Login</h2>
          <p className="fw-bold mb-1">Welcome Back</p>
          <p className="text-center text-muted mb-4">Sign in to access your dashboard</p>

          {error && <Alert variant="danger">{error}</Alert>}

          <Form onSubmit={handleLoginForm}>
            <Form.Group className="mb-3 text-start">
              <Form.Label className="fw-semibold">Username or Phone</Form.Label>
              <Form.Control
                type="text"
                name="usernameOrPhone"
                value={usernameOrPhone}
                onChange={(e) => setUsernameOrPhone(e.target.value)}
                placeholder="Enter username or phone"
                required
            />
          </Form.Group>

          <Form.Group className="mb-3 text-start">
            <Form.Label className="fw-semibold">Password</Form.Label>
              <Form.Control
                type="password"
                name="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter password"
                required
            />
          </Form.Group>

          <Button variant="primary" type="submit" className="w-100 mt-3"
              style={{
                width: '100%',
                backgroundColor: '#0b1432ff', 
                border: 'none',
                padding: '10px',
                borderRadius: '5px',
                fontWeight: '600',
                fontSize: '16px'
              }}
          >
            Sign In
          </Button>
        </Form>
      </div>
    </div>
  </div>
</div>
  )
};

export default BarberLogin;
