import React from 'react'

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
