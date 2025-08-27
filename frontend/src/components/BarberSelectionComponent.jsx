import React, { useState, useEffect } from 'react';
import { Card, Button, Alert, Row, Col, Badge } from 'react-bootstrap';
import { storeSelectedBarber } from '../services/AppointmentService';
import { getAllBarbersAPICall } from '../services/BarberService';
import { useNavigate } from 'react-router-dom';

const BarberSelectionComponent = () => {
  const [barbers, setBarbers] = useState([]);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    getAllBarbersAPICall().then(response => {
      console.log('Barber response:', response);

      setBarbers(response.data);
    }).catch(error => {
      setError('Failed to load barbers');
      console.error(error);
    });
  }, []);

  const handleBarberSelect = (barber) => {
    storeSelectedBarber(barber);
    navigate('/select-date');
  };

  return (
    <div className="bg-white"
      style={{ minHeight: '100vh', paddingTop: '2rem', paddingBottom: '2rem' }}>
      <div className="card-body">
                <Button 
                className="mb-3 p-1 fw-bold"
                variant="outline-secondary" 
                onClick={() => navigate('/')}    
                  style={{
                  padding: '0',
                  color: '#0b1432ff',
                  textDecoration: 'none',
                  fontWeight: '500'
                }} >
                  ←Back to Home
                </Button>
      <div className="card shadow p-4" style={{ maxWidth: '800px', width: '100%' }}>
        <div className="card-body">
          <h2 className="card-title text-center mb-2">
            <i class="bi bi-file-earmark-person me-2"></i>
            Choose Your Barber
          </h2>
          <p className="text-muted text-center mb-5">Select from our experienced barbers</p>

          {error && <Alert variant="danger">{error}</Alert>}

          <Row className="g-4">
            {barbers.map((barber) => (
              <Col key={barber.barberId} md={6}>
                <Button 
                variant="secondary" 
                onClick={() => handleBarberSelect(barber)}
                className="w-100 py-3 d-flex flex-column align-items-center"
                style={{ 
                  backgroundColor: '#0b1432', 
                  color: '#ffffffff',
                  borderRadius: '10px',
                  boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
                }}
              >
                {/* Uncomment if you want to add barber images
                <img 
                  src={barber.imageUrl || '/608.jpg'} 
                  alt={barber.barberName}
                  className="rounded-circle mb-2"
                  style={{ width: '80px', height: '80px', objectFit: 'cover' }}
                />
                */}
                <span className="fs-5">{barber.barberName}</span>
              </Button>
              </Col>
            ))}
          </Row>
        </div>
      </div>
      </div>
    </div>
  );
};

export default BarberSelectionComponent;
