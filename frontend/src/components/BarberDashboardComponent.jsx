import React, { useState, useEffect } from 'react';
import { Card, Button, Alert, Table, Navbar, Nav, Container } from 'react-bootstrap';
import { getCurrentBarberAPICall, getBarberAppointmentsByDateAPICall, getAllBarbersAPICall, deleteBarberAPICall } from '../services/BarberService';
import { getAvailableDatesAPICall, confirmAppointmentAPICall, deleteAppointmentAPICall } from '../services/AppointmentService'; 
import { useNavigate } from 'react-router-dom';
import { isAdminBarber, logout } from '../services/AuthService';

const BarberDashboard = () => {
  const [availableDates, setAvailableDates] = useState([]);
  const [appointments, setAppointments] = useState([]);
  const [selectedDate, setSelectedDate] = useState('');
  const [barbers, setBarbers] = useState([]);
  const [error, setError] = useState('');
  const [currentMonth, setCurrentMonth] = useState(new Date().toISOString().slice(0, 7));
  const [showStaffManagement, setShowStaffManagement] = useState(false);
  const navigate = useNavigate();
  const isAdmin = isAdminBarber();

  useEffect(() => {
    fetchBarberData();
  }, [currentMonth]);

  useEffect(() => {
    getAllBarbersAPICall()
      .then(response => {
        console.log('Barber response:', response);
        setBarbers(response.data);
      })
      .catch(err => {
        setError('Failed to load barbers');
        console.error(err);
      });
  }, []);

  // For appointments management to fetch available dates and appointments
  const fetchBarberData = async () => {
    try {
      const barberResponse = await getCurrentBarberAPICall();
      const barberId = barberResponse.data.barberId;
      
      const datesResponse = await getAvailableDatesAPICall(barberId, currentMonth);
      setAvailableDates(datesResponse.data);
    } catch (error) {
      setError('Failed to load barber data');
      console.error(error);
    }
  };

  const handleDateSelect = async (date) => {
    setSelectedDate(date);
    try {
      const response = await getBarberAppointmentsByDateAPICall(date);
      setAppointments(response.data);
    } catch (error) {
      setError('Failed to load appointments');
      console.error(error);
    }
  };

  const handleDeleteAppointment = async (appointmentId) => {
    try {
      await deleteAppointmentAPICall(appointmentId);
      setAppointments(prevAppointments => 
        prevAppointments.filter(app => app.appointmentId !== appointmentId)
      );
    } catch (error) {
      console.error("Failed to delete appointment:", error);
    }
  };

  const handleDeleteBarber = async (barberId) => {
    try {
      await deleteBarberAPICall(barberId);
      setBarbers(prevBarbers => 
        prevBarbers.filter(app => app.barberId!== barberId)
      );
    } catch (error) {
      console.error("Failed to delete appointment:", error);
    }
  };

  const handleConfirmAppointment = async (appointmentId) => {
    try {
      await confirmAppointmentAPICall(appointmentId);
      setAppointments(prevAppointments => 
        prevAppointments.map(app => app.appointmentId === appointmentId ? {...app, status: 'CONFIRMED' } : app
        ));
      } catch (error) {
      console.error("Failed to confirm appointment:", error);
      }
  };

  const handleLogOut = () => {
    logout();
    navigate('/barber-login');
  }

  const changeMonth = (increment) => {
    const [year, month] = currentMonth.split('-').map(Number);
    let newMonth = month + increment;
    let newYear = year;
    
    if (newMonth > 12) {
      newMonth = 1;
      newYear++;
    } else if (newMonth < 1) {
      newMonth = 12;
      newYear--;
    }
    
    setCurrentMonth(`${newYear}-${String(newMonth).padStart(2, '0')}`);
    setSelectedDate('');
    setAppointments([]);
  };

  const formatMonthYear = (monthString) => {
    const [year, month] = monthString.split('-');
    const date = new Date(year, month - 1);
    return date.toLocaleDateString('en-US', { month: 'long', year: 'numeric' });
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    const weekday = date.toLocaleDateString('en-US', { weekday: 'short' });
    
    return `${weekday}, ${day}/${month}/${year}`;
  };

  const formatAppointmentTime = (dateTimeString) => {
    if (!dateTimeString) return 'N/A';
    try {
      return dateTimeString.split('T')[1].substring(0, 5);
    } catch (e) {
     console.error('Error formatting time:', e);
     return 'Invalid time';
    }
  };
  
  const sortedAppointments = [...appointments].sort((a, b) => {
    return new Date(a.appointmentTime) - new Date(b.appointmentTime);
  });

  return (
    <div className="bg-white" style={{ minHeight: '100vh' }}>
      {/* Top Navigation Bar */}
      <Navbar expand="lg" 
              fixed="top"
              style={{ minHeight: '15vh' ,background: 'linear-gradient(135deg, #2c3e50 0%, #1a1a2e 100%)' }}
              className="px-4 py-3">
        <Navbar.Brand className="fw-bold text-white fs-4 ">
          <i className="bi bi-scissors me-2"></i>
            Barber Dashboard
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav" className="justify-content-end">
          <Nav className="align-items-center">
            {isAdmin && (
              <Button 
                style={{ backgroundColor: '#2c3551ff', border: 'none', padding: '5px', fontSize: '15px'}} 
                className="me-3 text-white"
                onClick={() => setShowStaffManagement(!showStaffManagement)}
                size="sm"
              >
                <i className="bi bi-people-fill me-1"></i>
                {showStaffManagement ? "Hide Staff" : "Show Staff"}
              </Button>
            )}
            <Button 
              style={{ backgroundColor: '#2c3551ff', border: 'none', padding: '5px', fontSize: '15px' }}
              className='text-white'
              onClick={handleLogOut}
              size="sm"
           >
              <i className="bi bi-box-arrow-right me-1"></i>
                Log Out
            </Button>
          </Nav>
        </Navbar.Collapse>
      </Navbar>



      <Container className="py-4"
        style={{ marginTop: '100px', marginBottom: '40px' }}>
        {/* Staff Management Section (Conditional) */}
        {showStaffManagement && isAdmin && (
          <Card className="mb-4 shadow-sm">
            <Card.Body>
              <div className="d-flex justify-content-between align-items-center mb-1">
                <h5 className="card-title mb-0">
                  <i className="bi bi-people-fill me-2"></i>
                  Staff Management
                </h5>
                <Button 
                  style={{ color:'#ffffffff',backgroundColor: '#0b1432' }}
                  variant="none"
                  onClick={() => navigate('/register-barber')}
                >
                  <i className="bi bi-person-plus me-1"></i>
                  Add New Barber
                </Button>
              </div>
              <p className="text-muted">Manage your barber team members</p>
              <Table striped bordered hover>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Phone</th>
                    <th>Username</th>
                  </tr>
                </thead>
                <tbody>
  {barbers.length > 0 ? (
    barbers.map((barber) => (
      <tr key={barber.barberId}>
        <td>{barber.barberName}</td>
        <td>{barber.phone}</td>
        <td>{barber.username}</td>
        <td>
          <Button 
            variant="outline-danger" 
            size="sm"
            onClick={() => handleDeleteBarber(barber.barberId)}
            className="ms-2">
            Delete
          </Button>
        </td>
      </tr>
    ))
  ) : (
    <tr>
      <td colSpan="4" className="text-center text-muted">No barbers found</td>
    </tr>
  )}
</tbody>

              </Table>
            </Card.Body>
          </Card>
        )}

        {/* Appointments Dashboard Card */}
        <Card className="shadow p-4" style={{ maxWidth: '800px', width: '100%' }}>
          <Card.Body>
            <div className="d-flex justify-content-between align-items-center mb-4">
              <div>
                <h4 className="card-title mb-2">
                  <i className="bi bi-calendar-event me-2"></i>
                  Appointments
                </h4>
                <p className="text-muted">View and manage your appointments</p>
              </div>
            </div>

            {error && <Alert variant="danger">{error}</Alert>}

            {/* Month selection controls */}
            <div className="d-flex justify-content-between align-items-center mb-4">
              <Button 
                variant="secondary" 
                onClick={() => changeMonth(-1)}
                style={{ color:'#ffffffff', backgroundColor: '#0b1432' }}
              >
                &lt; Previous Month
              </Button>
              <h5 className="mb-0">{formatMonthYear(currentMonth)}</h5>
              <Button 
                variant="secondary" 
                onClick={() => changeMonth(1)}
                style={{ color:'#ffffffff', backgroundColor: '#0b1432' }}
              >
                Next Month &gt;
              </Button>
            </div>

            <h5>Select Date</h5>
            <div className="d-flex flex-wrap gap-2 mb-4">
              {availableDates.length > 0 ? (
                availableDates.map(date => (
                  <Button
                    key={date}
                    variant={date === selectedDate ? 'primary' : 'outline-primary'}
                    onClick={() => handleDateSelect(date)}
                    style={{ 
                      backgroundColor: date === selectedDate ? '#0b1432' : '#f8fafd', 
                      color: date === selectedDate ? '#ffffff' : '#090000ff',
                      minWidth: '110px',
                      padding: '8px 5px'
                    }}  
                  >
                    {formatDate(date)}
                  </Button>
                ))
              ) : (
                <p className="text-muted">No available dates for this month</p>
              )}
            </div>

            {selectedDate && (
              <div className="mt-4">
                <h5>Appointments for {formatDate(selectedDate)}</h5>
                {appointments.length > 0 ? (
                  <Table striped bordered hover responsive>
                    <thead>
                      <tr>
                        <th>Time</th>
                        <th>Customer Name</th>
                        <th>Service</th>
                        <th>Status</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {sortedAppointments.map((appointment) => (
                        <tr key={appointment.appointmentId}>
                          <td>{formatAppointmentTime(appointment.appointmentTime)}</td>
                          <td>{appointment.customerFirstName + ' ' + appointment.customerLastName}</td>
                          <td>{appointment.serviceName}</td>
                          <td>
                            <span className={`badge ${
                              appointment.status === 'CONFIRMED' ? 'bg-success' : 'bg-warning'
                            }`}>
                              {appointment.status}
                            </span>
                          </td>
                          <td>
                            <Button 
                              variant="outline-success" 
                              size="sm"
                              onClick={() => handleConfirmAppointment(appointment.appointmentId)}
                              disabled={appointment.status === 'CONFIRMED'}
                            >
                              {appointment.status === 'CONFIRMED' ? 'Confirmed' : 'Confirm'}
                            </Button>

                            <Button 
                              variant="outline-danger" 
                              size="sm"
                              onClick={() => handleDeleteAppointment(appointment.appointmentId)}
                              className="ms-2"
                            >
                              Delete
                            </Button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </Table>
                ) : (
                  <p className="text-muted">No appointments for this date</p>
                )}
              </div>
            )}
          </Card.Body>
        </Card>
      </Container>
    </div>
  );
};

export default BarberDashboard;
