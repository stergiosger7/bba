import React from 'react';
import { Link } from 'react-router-dom'; 


function HomePage() {
  return (
    <div className="homepage">
      {/* Hero Section */}
      <section 
      className="hero-section text-center py-5 text-white position-relative"
      style={{
            background: 'linear-gradient(135deg, #2c3e50 0%, #1a1a2e 100%)',
            minHeight: '50vh', 
            width: '99.5vw',
            marginBottom: '-32px', // Fixes potential 1px browser gap
            position: 'relative',
            left: '50%',
            right: '50%',
            marginLeft: '-50vw',
            marginRight: '-50vw'
        }}  
      >
        {/* Staff Login Button */}
        <div className="position-absolute top-0 end-0 m-4">
          <Link to="/barber-login" className="btn btn-outline-light btn-sm"
          style={{
              width: '100%',
              backgroundColor: '#2c3551ff', 
              padding: '5px',
               fontSize: '15px'
             }}>
             <i className="bi bi-person-fill-gear me-1"></i>
            <i className="bi bi-box-arrow-in-right me-1"></i> 
             Barber Login
          </Link>
        </div>
        
        <div className="container">
          <h1 className="display-4 fw-bold">Stergios Barbershop</h1>
          <p className="lead fs-4">Experience the finest grooming services</p>
          <Link 
            to="/select-barber" 
            className="btn btn-light btn-lg px-5 py-3"
            style={{ borderRadius: '50px' }}
          >
          <i className="bi bi-calendar-check me-1"></i> Book Appointment
          </Link>
        </div>
      </section>

      {/* Why Choose Us Section */}
      <section className="py-5 bg-white">
        <div className="container">
          <h2 className="text-center mb-5 fw-bold">Why Choose Us</h2>
          <div className="row g-4 ">

            <div className="col-md-6 ">
              <div className="p-4 text-center bg-white rounded-3 h-100 shadow-lg">
                <div className="icon-wrapper bg-primary bg-opacity-10 rounded-circle p-3 d-inline-block mb-3">
                  <img 
                    src="/expert-cuts.png" 
                    alt="Expert Cuts" 
                    className="img-fluid rounded-circle"
                    style={{ width: '100px', height: '100px', objectFit: 'cover' }}
                  />
                </div>
                <h3 className="fw-bold">Expert Cuts</h3>
                <p className="text-muted">
                  Our master barbers provide precision haircuts tailored to your style.
                </p>
              </div>
            </div>

            <div className="col-md-6">
              <div className="p-4 text-center bg-white rounded-3 h-100 shadow-lg">
                <div className="icon-wrapper bg-primary bg-opacity-10 rounded-circle p-3 d-inline-block mb-3">
                  <img 
                    src="/premium-services.webp" 
                    alt="Premium Service" 
                    className="img-fluid rounded-circle"
                    style={{ width: '100px', height: '100px', objectFit: 'cover' }}
                  />
                </div>
                <h3 className="fw-bold">Premium Service</h3>
                <p className="text-muted">
                  Enjoy luxury grooming with premium products and relaxing atmosphere.
                </p>
              </div>
            </div>
            
          </div>
        </div>
      </section>

      {/* Visit Our Shop Section */}
      <section className="py-5 bg-white">
        <div className="container">
          <h2 className="text-center mb-5 fw-bold">Visit Our Shop</h2>
          
          <div className="row g-4 justify-content-center">
            {/* Κουτί 1: Location */}
            <div className="col-md-5">
              <div className="p-4 text-center bg-white rounded-3 h-100 shadow-lg">
                <div className="icon-wrapper bg-primary bg-opacity-10 rounded-circle p-3 d-inline-block mb-3">
                  <i className="bi bi-geo-alt-fill text-dark fs-2"></i>
                </div>
                <h3 className="fw-bold">Location</h3>
                <p className="text-muted">
                  Leof. Dodonis 100<br />
                  Ioannina, 45500
                </p>
              </div>
            </div>
            
            {/* Κουτί 2: Contact */}
            <div className="col-md-5">
              <div className="p-4 text-center bg-white rounded-3 h-100 shadow-lg">
                <div className="icon-wrapper bg-primary bg-opacity-10 rounded-circle p-3 d-inline-block mb-3">
                  <i className="bi bi-telephone-fill text-dark fs-2"></i>
                </div>
                <h3 className="fw-bold">Contact</h3>
                <p className="text-muted">
                  (+30) 6945842316<br />
                  stergios@barbershop.com
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Call-to-Action Section */}
      <section 
      className="hero-section text-center py-5 bg-dark text-white position-relative"
      style={{
            background: 'linear-gradient(135deg, #2c3e50 0%, #1a1a2e 100%)',
            minHeight: '50vh', 
            width: '99.5vw',
            marginBottom: '-32px', // Fixes potential 1px browser gap
            position: 'relative',
            left: '50%',
            right: '50%',
            marginLeft: '-50vw',
            marginRight: '-50vw'
        }}
        >
        <div className="container text-center">
          <h2 className="display-5 fw-bold mb-4">Ready for a Fresh Look?</h2>
          <p className="lead mb-4">
            Book your appointment today and experience premium grooming
          </p>
          <Link 
            to="/select-barber" 
            className="btn btn-light btn-lg px-5 py-3"
            style={{ borderRadius: '50px' }}
          >
            <i className="bi bi-calendar-check me-1"></i> Book Now
        </Link>
        </div>
      </section>
    </div>
  );
}

export default HomePage;