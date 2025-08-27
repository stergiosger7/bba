import { Routes, Route, Navigate } from 'react-router-dom';
import HomePage from './components/HomePage';
import BarberLogin from './components/BarberLogin';
import BarberDashboardComponent from './components/BarberDashboardComponent';
import { isUserLoggedIn } from './services/AuthService'
import BarberSelectionComponent from './components/BarberSelectionComponent';
import DateSelectionComponent from './components/DateSelectionComponent';
import CustomerComponent from './components/CustomerComponent';
import ConfirmationPageComponent from './components/ConfirmationPageComponent';
import BarberRegisterComponent from './components/BarberRegisterComponent';


function App() {
  
  function AuthenticatedRoute({ children }) {
  return isUserLoggedIn() ? children : <Navigate to="/barber-login" />;
}

  return (
    //ensure that the routes are wrapped in a AuthenticatedRouter
    //for the pages to go in a row(barber->dates>info)
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/barber-login" element={<BarberLogin />} />
      <Route path="/select-barber" element={<BarberSelectionComponent/>} />
      <Route path="/select-date" element={<DateSelectionComponent />} />
      <Route path="/customer-info" element={<CustomerComponent />} />
      <Route path="/appointment-confirmation" element={<ConfirmationPageComponent />} />
      
      {/* Protected Barber Dashboard Route */}
      <Route path="/barbers/dashboard" 
        element={
          <AuthenticatedRoute>
            <BarberDashboardComponent />
          </AuthenticatedRoute>
        } />

      {/* Protected Register Route */}
      <Route path="/register-barber" 
        element={
          <AuthenticatedRoute>
            <BarberRegisterComponent />
          </AuthenticatedRoute>
        } />
    </Routes>
  );
}

export default App;