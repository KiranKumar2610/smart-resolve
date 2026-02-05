const token = localStorage.getItem("token");
if (!token) logout();

const payload = JSON.parse(atob(token.split(".")[1]));
if (payload.role !== "USER") logout();

const editId = localStorage.getItem("editComplaintId");

document.addEventListener("DOMContentLoaded", async () => {
  const form = document.getElementById("complaintForm");
  const titleInput = document.getElementById("title");
  const descInput = document.getElementById("description");

  if (editId) {
    document.querySelector("h1").innerText = "Edit Complaint";
    document.getElementById("submitBtn").innerText = "Update Complaint";

    const res = await fetch("/api/complaints/my", {
      headers: { Authorization: "Bearer " + token }
    });

    const complaints = await res.json();
    const c = complaints.find(x => x.id == editId);

    if (!c || c.status === "RESOLVED") {
      localStorage.removeItem("editComplaintId");
      return window.location.href = "/complaints.html";
    }

    titleInput.value = c.title;
    descInput.value = c.description;
  }

  form.addEventListener("submit", async e => {
    e.preventDefault();

    const body = {
      title: titleInput.value.trim(),
      description: descInput.value.trim()
    };

    if (!body.title || !body.description) {
      showToast("All fields required", true);
      return;
    }

    const url = editId
      ? `/api/complaints/${editId}`
      : `/api/complaints`;

    const method = editId ? "PUT" : "POST";

    const res = await fetch(url, {
      method,
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
      },
      body: JSON.stringify(body)
    });

    if (!res.ok) {
      showToast("Operation failed", true);
      return;
    }

    localStorage.removeItem("editComplaintId");
    showToast("Saved successfully");
    setTimeout(() => window.location.href = "/complaints.html", 800);
  });
});

function logout() {
  localStorage.clear();
  window.location.href = "/login.html";
}

function showToast(msg, error = false) {
  const t = document.getElementById("toast");
  t.innerText = msg;
  t.style.background = error ? "#dc2626" : "#020617";
  t.classList.add("show");
  setTimeout(() => t.classList.remove("show"), 2500);
}
