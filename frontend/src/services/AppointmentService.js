import axios from "axios";

const APPOINTMENT_REST_API_BASE_URL = "http://localhost:8080/api/appointments";

//export const storeToken = (token) => localStorage.setItem("barberToken", token);

console.log('Current token:', localStorage.getItem("barberToken"));

// Get available dates for a barber
export const getAvailableDatesAPICall = (barberId, month) => 
  axios.get(APPOINTMENT_REST_API_BASE_URL + '/available-dates', {
    params: { barberId, month }
  });

// Get available time slots for a specific date
export const getAvailableSlotsAPICall = (barberId, date) => 
  axios.get(APPOINTMENT_REST_API_BASE_URL + '/available-slots', {
    params: { barberId, date }
  });

// Create new appointment
export const createAppointmentAPICall = (createAppointmentDto) => 
  axios.post(APPOINTMENT_REST_API_BASE_URL, createAppointmentDto);

export const confirmAppointmentAPICall = (appointmentId) => {
  return axios.patch(
    `${APPOINTMENT_REST_API_BASE_URL}/${appointmentId}/confirm`, 
      {}, //body parameters are not needed for this endpoint
      {
        headers: {
          'Authorization': `${localStorage.getItem('barberToken')}`,
          'Content-Type': 'application/json'
        }
      }
    );
  };

export const deleteAppointmentAPICall = (appointmentId) => {
  return axios.delete(
    `${APPOINTMENT_REST_API_BASE_URL}/${appointmentId}`, 
    {
      headers: {
        'Authorization': `${localStorage.getItem('barberToken')}`,
        'Content-Type': 'application/json'
      }
    }
  );
};

