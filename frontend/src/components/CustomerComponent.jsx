import React, { useState } from 'react';
import { Card, Button, Alert, Form, Row, Col } from 'react-bootstrap';
import { getSelectedBarber, getSelectedDate, getSelectedTime, clearAppointmentData } from '../services/AppointmentService';
import { createAppointmentAPICall } from '../services/AppointmentService';
import { useNavigate } from 'react-router-dom';

const CustomerComponent = () => {

  const SERVICES = [
    { serviceId: 1, serviceName: "Λούσιμο", price: 5.00, durationMinutes: 30 },
    { serviceId: 2, serviceName: "Κούρεμα", price: 10.00, durationMinutes: 30 },
    { serviceId: 3, serviceName: "Γένια", price: 5.00, durationMinutes: 30 }
  ];
  const [customerInfo, setCustomerInfo] = useState({
    firstName: '',
    lastName: '',
    phone: ''
  });
  const [selectedService, setSelectedService] = useState(null);
  const [selectedDate, setSelectedDate] = useState(getSelectedDate());
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const barber = getSelectedBarber();
  const date = getSelectedDate();
  const time = getSelectedTime();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCustomerInfo(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    const appointmentData = {
      barberId: barber.barberId,
      serviceId: selectedService.serviceId,
      appointmentTime: `${date}T${time}`,
      customerFirstName: customerInfo.firstName,
      customerLastName: customerInfo.lastName,
      customerPhone: customerInfo.phone,
      notes: '' // Optional notes
    };

  createAppointmentAPICall(appointmentData)
    .then(() => {
      clearAppointmentData();

      // Debug what you're sending
      console.log('Navigating with state:', { 
        date: date, 
        time: time 
      });

      navigate('/appointment-confirmation', {
        state: {
          date: new Date(date).toLocaleDateString('en-US', {  // Fixed syntax
            weekday: 'long', 
            month: 'long', 
            day: 'numeric' 
          }),
          time: time
        }
      });
    })
    .catch(error => {
      setError(error.response?.data?.message || 'Failed to book appointment');
      console.error(error);
    })
    .finally(() => setLoading(false));
  }

  return (
    <div className="d-flex justify-content-center bg-white">
      <div className="card shadow p-1" style={{ maxWidth: '1000px', width: '90%' }}>
        <div className="card-body">
          
          <h2 className="card-title text-center mb-2">
            <i class="bi bi-info-square me-2"></i>
              Your Information
          </h2>
          <p className="text-muted text-center mb-3">Please provide your contact details</p>

          {error && <Alert variant="danger">{error}</Alert>}

          <Form onSubmit={handleSubmit}>
          {/* First Name and Last Name in the same row */}
          <Row className="mb-3">
            <Col>
              <Form.Group>
                <Form.Label>First Name</Form.Label>
                <Form.Control
                  type="text"
                  name="firstName"
                  value={customerInfo.firstName}
                  onChange={handleChange}
                  required
                />
              </Form.Group>
            </Col>
            <Col>
              <Form.Group>
                <Form.Label>Last Name</Form.Label>
                <Form.Control
                  type="text"
                  name="lastName"
                  value={customerInfo.lastName}
                  onChange={handleChange}
                  required
                />
              </Form.Group>
            </Col>
          </Row>

            <Form.Group className="mb-3">
              <Form.Label>Phone Number</Form.Label>
              <Form.Control
                type="tel"
                name="phone"
                value={customerInfo.phone}
                onChange={handleChange}
                required
              />
            </Form.Group>

            <h5 className="mb-2">Select Service</h5>
            <Row className="g-3 mb-3">
              {SERVICES.map(service => (
                <Col key={service.serviceId} md={4}>
                  <Card 
                    onClick={() => setSelectedService(service)}
                    className={`h-100 cursor-pointer ${selectedService?.serviceId === service.serviceId ? 'border-primary' : ''}`}
                  >
                    <Card.Body className="text-center">
                      <h6>{service.serviceName}</h6>
                      <p className="text-muted mb-0">
                        €{service.price.toFixed(2)} 
                      </p>
                    </Card.Body>
                  </Card>
                </Col>
              ))}
            </Row>

            <Card className="mb-3">
              <Card.Body>
                <h5>Appointment Summary</h5>
                <Row>
                  <Col md={6}>
                    <p><strong>Barber:</strong> {barber?.barberName}</p>
                  </Col>
                  <Col md={6}>
                    {selectedService && (
                    <p><strong>Service:</strong> {selectedService.serviceName} </p>
                    )}                  
                  </Col>
                </Row>
                <Row>
                  <Col md={6}>
                    <p><strong>Date:</strong> {date && new Date(date).toLocaleDateString()}</p>
                  </Col>
                  <Col md={6}>
                    <p><strong>Time:</strong> {time}</p>
                  </Col>
                </Row>
              </Card.Body>
            </Card>

          {/* Button Group - Back (Left) and Submit (Right) */}
          <div className="d-flex justify-content-between">
            <Button 
              variant="secondary"  
              onClick={() => navigate('/select-date')}
              style={{
                color:'#090000ff',
                backgroundColor: '#f8fafdff', 
                width: '120px',
                padding: '10px'
              }}
            >
              Back
            </Button>
            
            <Button 
              type="submit" 
              disabled={loading}
              style={{ 
                backgroundColor: '#0b1432', 
                border: 'none',
                width: '180px',
                padding: '10px'
              }}
            >
              {loading ? '...' : 'Book Appointment'}
            </Button>
          </div>
          </Form>
        </div>
      </div>
    </div>
  )
};

export default CustomerComponent;