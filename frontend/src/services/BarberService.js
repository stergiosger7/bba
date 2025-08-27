import axios from "axios";

const BARBER_REST_API_BASE_URL = "http://localhost:8080/api/barbers";

//export const storeToken = (token) => localStorage.setItem("barberToken", token);

//console.log('Current token:', localStorage.getItem("barberToken"));

// Create Axios instance with default headers
const barberApi = axios.create({
  baseURL: BARBER_REST_API_BASE_URL,
  headers: {
    "Authorization": `${localStorage.getItem("barberToken")}`
  }
});

// Get all barbers (public endpoint)
export const getAllBarbersAPICall = () => 
  axios.get(BARBER_REST_API_BASE_URL + '/all');

// Get current authenticated barber details
export const getCurrentBarberAPICall = () =>
  barberApi.get("/me");

// Get all appointments for current barber
export const getBarberAppointmentsAPICall = () =>
  barberApi.get("/appointments");

// Get appointments for current barber by specific date
export const getBarberAppointmentsByDateAPICall = (date) =>
  barberApi.get(`/appointments/${date}`);

export const deleteBarberAPICall = (barberId) => {
  return axios.delete(
    `${BARBER_REST_API_BASE_URL}/${barberId}`, 
    {
      headers: {
        'Authorization': `${localStorage.getItem('barberToken')}`,
        'Content-Type': 'application/json'
      }
    }
  );
};

// Close day for current barber
export const closeDayAPICall = (date) =>
  barberApi.post("/close-day", null, { params: { date } });

// Barber storage functions (same pattern as your appointment service)
export const storeSelectedBarber = (barber) => 
  sessionStorage.setItem("selectedBarber", JSON.stringify(barber));

export const getSelectedBarber = () => {
  const barber = sessionStorage.getItem("selectedBarber");
  return barber ? JSON.parse(barber) : null;
}

export const storeSelectedDate = (date) => 
  sessionStorage.setItem("selectedDate", date);

export const getSelectedDate = () => 
  sessionStorage.getItem("selectedDate");

export const storeSelectedTime = (time) => 
  sessionStorage.setItem("selectedTime", time);

export const getSelectedTime = () => 
  sessionStorage.getItem("selectedTime");

export const clearAppointmentData = () => {
  sessionStorage.removeItem("selectedBarber");
  sessionStorage.removeItem("selectedDate");
  sessionStorage.removeItem("selectedTime");
}