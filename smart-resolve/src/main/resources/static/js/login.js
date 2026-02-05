
document.getElementById("loginBtn").addEventListener("click", async () => {
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value.trim();

  if (!email || !password) {
    showToast("Email and password required", true);
    return;
  }

  try {
    const res = await fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });

    if (!res.ok) {
      showToast("Invalid credentials", true);
      return;
    }

    const data = await res.json();
    localStorage.setItem("token", data.token);

    const role = JSON.parse(atob(data.token.split(".")[1])).role;
    window.location.href =
      role === "ADMIN" ? "/admin.html" : "/complaints.html";

  } catch (err) {
    console.error(err);
    showToast("Server error", true);
  }
});

function showToast(msg, error = false) {
  const t = document.getElementById("toast");
  t.innerText = msg;
  t.style.background = error ? "#dc2626" : "#020617";
  t.classList.add("show");
  setTimeout(() => t.classList.remove("show"), 2500);
}
