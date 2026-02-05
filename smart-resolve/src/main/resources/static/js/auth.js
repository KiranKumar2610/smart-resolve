   export function getToken() {
     return localStorage.getItem("token");
   }

   export function getRole() {
     try {
       return JSON.parse(atob(getToken().split(".")[1])).role;
     } catch {
       return null;
     }
   }

   export function logout() {
     localStorage.clear();
     window.location.href = "/login.html";
   }
