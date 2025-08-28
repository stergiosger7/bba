# Barbershop Management System

A **Spring Boot** full-stack application for managing barbershop appointments, with **JWT authentication** and **role-based access control**.  
Designed to let barbers manage their schedules and customers book appointments easily.

---

# Backend Features

###  Authentication & Security
- **JWT-based authentication** using Spring Security.
- **Role-based access control**:
  - `ADMIN` → barbershop owner / manager.
  - `BARBER` → individual barbers.

###  Roles & Permissions
- **Admin**
  - Predefined admin user initialized at startup.
  - Manage barbers (add / delete).
  - View the list of registered barbers.
  - View their own appointments.
  - Confirm or cancel their own appointments.
- **Barber**
  - Login and access personal dashboard.
  - View their own appointments.
  - Confirm or cancel their own appointments.

### Appointments
- Customers can create appointments with:
  - Selected barber
  - Selected service (e.g., haircut, wash, beard trim)
  - Specific date & time slot
- Automatic check for **availability of time slots**.
- Appointment lifecycle: `PENDING`, `CONFIRMED`.

### Services
- Predefined barbershop services initialized at startup:
  
---

## Tech Stack

- **DTO Pattern**
- **Rest Api**
- **Backend:** Spring Boot 3, Spring Data JPA, Hibernate  
- **Security:** Spring Security 6, JWT  
- **Database:** MySQL  
- **Testing:** JUnit 5, Mockito  
- **Build tool:** Maven  

---

## Testing
- Unit tests implemented with JUnit 5 & Mockito.
- Coverage includes:
  - Barber and Appointment services (happy path and some edge cases)

---

# Frontend Features

- Barber login with JWT authentication.
- Protected routes (only authenticated users can access certain pages).
- Role-based access (e.g., only `ADMIN` can register new barbers).
- Customer appointment flow:
  - Select barber
  - Select date & time
  - Enter customer info
  - Appointment confirmation
- Barber dashboard with authenticated access.

## Tech Stack
- React  
- React Router – navigation & route protection  
- Axios – API requests  
- Bootstrap / React-Bootstrap – UI styling  
- JWT authentication (token stored in `localStorage`)
- Build Toolchain: Vite

## Roadmap / Future Improvements

- Email or SMS notifications for confirmed appointments.
- Barber history of appointments (customers etc).
- Monthly calculations of income per barber.
- Availability to close a day for barbers (days off).





