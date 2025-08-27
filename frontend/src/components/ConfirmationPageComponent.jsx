import React, { use } from 'react';
import { Container, Card, Button } from 'react-bootstrap';
import { CheckCircleFill } from 'react-bootstrap-icons';
import { useLocation, useNavigate } from 'react-router-dom';

const ConfirmationPageComponent = () => {

    const location = useLocation();
    const navigate = useNavigate();
    const { date = '', time = '' } = location.state || {};

    // Debug output
    console.log('Received state:', { date, time });

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
      <Card className="text-center p-4 shadow" style={{ width: '100%', maxWidth: '600px', border: 'none' }}>
        <div className="mb-4">
          <CheckCircleFill color="#0b1432ff" size={64} />
        </div>
        
        <h2 className="mb-3" style={{ color: '#0b1432' }}>Appointment Confirmed!</h2>
        
        <div className="mb-4 p-3" style={{ 
          backgroundColor: '#f8f9fa', 
          borderRadius: '8px',
        }}>
          <p className="h5 mb-1">Your appointment has been scheduled for:</p>
          <p className="h4" style={{ fontWeight: '600' }}>
            {date} at {time}
            </p>
        </div>

        <div className="d-flex justify-content-center gap-3">
            <Button 
                variant="secondary" 
                onClick={() => navigate('/')}
                style={{ 
                  backgroundColor: '#0b1432', 
                  color: '#ffffffff'
                }}     
          >
            Back to Home
          </Button>
        </div>
      </Card>
    </Container>
  );
};



export default ConfirmationPageComponent;