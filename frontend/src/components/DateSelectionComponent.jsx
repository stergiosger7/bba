import React, { useState, useEffect } from 'react';
import { Card, Button, Alert } from 'react-bootstrap';
import { getSelectedBarber, storeSelectedDate, storeSelectedTime } from '../services/AppointmentService';
import { getAvailableDatesAPICall, getAvailableSlotsAPICall } from '../services/AppointmentService';
import { useNavigate } from 'react-router-dom';

const DateSelectionComponent = () => {
  const [availableDates, setAvailableDates] = useState([]);
  const [availableSlots, setAvailableSlots] = useState([]);
  const [selectedDate, setSelectedDate] = useState('');
  const [selectedTime, setSelectedTime] = useState('');
  const [error, setError] = useState('');
  const [currentMonth, setCurrentMonth] = useState(new Date().toISOString().slice(0, 7)); // YYYY-MM format
  const navigate = useNavigate();
  const barber = getSelectedBarber();

  useEffect(() => {
    if (barber) {
      getAvailableDatesAPICall(barber.barberId, currentMonth)
        .then(response => setAvailableDates(response.data))
        .catch(error => {
          setError('Failed to load available dates');
          console.error(error);
        });
    }
  }, [barber, currentMonth]);

  useEffect(() => {
    if (selectedDate && barber) {
      getAvailableSlotsAPICall(barber.barberId, selectedDate)
        .then(response => setAvailableSlots(response.data))
        .catch(error => {
          setError('Failed to load available time slots');
          console.error(error);
        });
    }
  }, [selectedDate, barber]);

  const handleDateSelect = (date) => {
    setSelectedDate(date);
    storeSelectedDate(date);
    setSelectedTime('');
  };

  const handleTimeSelect = (time) => {
    setSelectedTime(time);
    storeSelectedTime(time);
  };

  const handleNext = () => {
    navigate('/customer-info');
  };

  const changeMonth = (increment) => {
    const [year, month] = currentMonth.split('-').map(Number); // Split into year and month 
    let newMonth = month + increment; // Increment month by 1 or -1
    let newYear = year; // Keep the same year initially
    
    if (newMonth > 12) {
      newMonth = 1;
      newYear++;
    } else if (newMonth < 1) {
      newMonth = 12;
      newYear--;
    }
    
    setCurrentMonth(`${newYear}-${String(newMonth).padStart(2, '0')}`); // Format to YYYY-MM
    setSelectedDate(''); // Reset selected date when changing month
    setSelectedTime(''); // Reset selected time when changing month
  };

  const formatMonthYear = (monthString) => { // Takes a string like "2025-01"
    const [year, month] = monthString.split('-'); // Splits into ["2025", "01"]
    const date = new Date(year, month - 1); // Creates Date object (month is 0-indexed)
    return date.toLocaleDateString('en-US', { month: 'long', year: 'numeric' });  // Formats to "January 2025"
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString); // Convert string to Date object
    const day = date.getDate().toString().padStart(2, '0'); 
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); 
    const year = date.getFullYear(); 
    const weekday = date.toLocaleDateString('en-US', { weekday: 'short' }); // Get short weekday name (e.g., "Mon", "Tue")
    
    return `${weekday}, ${day}/${month}/${year}`;
  };

  return (
    <div className="bg-white" 
         style={{ minHeight: '100vh', paddingTop: '2rem', paddingBottom: '2rem' }}>
        <Button 
          className="mb-3 p-1 fw-bold"
          variant="outline-secondary" 
          onClick={() => navigate('/select-barber')}    
            style={{
            padding: '0',
            color: '#0b1432ff',
            textDecoration: 'none',
            fontWeight: '500'
          }} >
            ←Back to Barbers 
          </Button>
      <div className="card shadow p-4" style={{ maxWidth: '800px', width: '100%' }}>
        <div className="card-body">

          <h2 className="card-title text-center mb-2">
            <i class="bi bi-calendar-check me-2"></i>
            Choose Date & Time
          </h2>
          <p className="text-muted text-center mb-5">Select your preferred appointment slot</p>

          {error && <Alert variant="danger">{error}</Alert>}

          {/* Month selection controls */}
          <div className="d-flex justify-content-between align-items-center mb-4">
            <Button variant="secondary" onClick={() => changeMonth(-1)}
              style={{ color:'#ffffffff',backgroundColor: '#0b1432' }}>
              &lt; Previous Month
            </Button>
            <h4 className="mb-0">{formatMonthYear(currentMonth)}</h4>
            <Button variant="secondary" onClick={() => changeMonth(1)}
              style={{ color:'#ffffffff',backgroundColor: '#0b1432' }}>
              Next Month &gt;
            </Button>
          </div>

          <h4>Select Date</h4>
          <div className="d-flex flex-wrap mb-4">
            {availableDates.length > 0 ? (
              availableDates.map(date => (
                <Button
                  key={date}
                  variant={date === selectedDate ? 'primary' : 'outline-primary'}
                  className="me-2 mb-2"
                  onClick={() => handleDateSelect(date)}
                  style={{ 
                    backgroundColor: date === selectedDate ? '#0b1432' : '#f8fafd', 
                    color: date === selectedDate ? '#ffffff' : '#090000ff',
                    width: '110px',
                    padding: '5px'
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
            <>
              <h4>Available Times for {formatDate(selectedDate)}</h4>
              <div className="d-flex flex-wrap mb-4">
                {availableSlots.map(slot => (
                  <Button
                    key={slot}
                    variant={slot === selectedTime ? 'primary' : 'outline-primary'}
                    className="me-2 mb-2"
                    onClick={() => handleTimeSelect(slot)}
                    style={{ 
                      backgroundColor: slot === selectedTime ? '#0b1432' : '#f8fafd', 
                      color: slot === selectedTime ? '#ffffff' : '#090000ff',
                      width: '100px',
                      padding: '5px'
                    }}
                  >
                    {slot}
                  </Button>
                ))}
              </div>
            </>
          )}

          {selectedTime && (
            <Button variant="primary" onClick={handleNext} className="w-100" style={{ 
                backgroundColor: '#0b1432', 
                border: 'none',
                width: '180px',
                padding: '10px'
              }}>
              Next: Your Information
            </Button>
          )}
        </div>
      </div>
    </div>
  );
};

export default DateSelectionComponent;