
document.getElementById("registerForm").addEventListener("submit", async e => {
    e.preventDefault();

    const fullName = document.getElementById("fullName").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    if (!fullName || !email || !password) {
        showToast("All fields are required", true);
        return;
    }

    try {
        const res = await fetch("/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ fullName, email, password })
        });

        if (!res.ok) {
            showToast("Registration failed", true);
            return;
        }

        showToast("Account created successfully");

        setTimeout(() => {
            window.location.href = "/login.html";
        }, 900);

    } catch (err) {
        console.error(err);
        showToast("Server error", true);
    }
});

/* ===== TOAST ===== */
function showToast(msg, error = false) {
    const t = document.getElementById("toast");
    t.innerText = msg;
    t.style.background = error ? "#dc2626" : "#020617";
    t.classList.add("show");
    setTimeout(() => t.classList.remove("show"), 2500);
}
