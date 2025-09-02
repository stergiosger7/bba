import axios from "axios";

const AUTH_REST_API_BASE_URL = "http://localhost:8080/api/auth";

export const registerAPICall = (registerObj) => {
  return axios.post(AUTH_REST_API_BASE_URL + '/register', registerObj, {
    headers: {
      Authorization: getToken(),   // took it immediately from local storage
      'Content-Type': 'application/json'
    }
  });
};

export const loginAPICall = (usernameOrPhone, password) => 
  axios.post(AUTH_REST_API_BASE_URL + '/login', { usernameOrPhone, password });

export const storeToken = (token) => localStorage.setItem("barberToken", token);

export const getToken = () => localStorage.getItem("barberToken");

export const saveLoggedInUser = (username, role) => {
  sessionStorage.setItem("authenticatedBarber", username);
  sessionStorage.setItem("barberRole", role);
}

export const isUserLoggedIn = () => {

    const username = sessionStorage.getItem("authenticatedBarber");

    if(username == null) {
        return false;
    }    
    else {
        return true;
    }   
}

export const getLoggedInUser = () => {
  return sessionStorage.getItem("authenticatedBarber");
}

export const logout = () => {
    localStorage.clear();
    sessionStorage.clear();
    window.location.reload();

}

export const isAdminBarber = () => {

  let role = sessionStorage.getItem("barberRole");

  if(role!== null && role === "ROLE_ADMIN") {
    return true;
  }else {
    return false;
  }
  
}