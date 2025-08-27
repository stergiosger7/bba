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

// Store selected barber in session storage
export const storeSelectedBarber = (barber) => 
  sessionStorage.setItem("selectedBarber", JSON.stringify(barber));

// Get selected barber from session storage
export const getSelectedBarber = () => {
  const barber = sessionStorage.getItem("selectedBarber");
  return barber ? JSON.parse(barber) : null;
}

// Store selected date in session storage
export const storeSelectedDate = (date) => 
  sessionStorage.setItem("selectedDate", date);

// Get selected date from session storage
export const getSelectedDate = () => 
  sessionStorage.getItem("selectedDate");

// Store selected time in session storage
export const storeSelectedTime = (time) => 
  sessionStorage.setItem("selectedTime", time);

// Get selected time from session storage
export const getSelectedTime = () => 
  sessionStorage.getItem("selectedTime");

// Clear appointment data from storage
export const clearAppointmentData = () => {
  sessionStorage.removeItem("selectedBarber");
  sessionStorage.removeItem("selectedDate");
  sessionStorage.removeItem("selectedTime");
}