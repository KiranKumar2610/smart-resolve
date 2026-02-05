const token = localStorage.getItem("token");
if (!token) logout();

const role = JSON.parse(atob(token.split(".")[1])).role;
if (role !== "USER") logout();

let allComplaints = [];

const tbody = document.getElementById("complaintsTable");
const searchInput = document.getElementById("searchInput");
const statusFilter = document.getElementById("statusFilter");

fetch("/api/complaints/my", {
  headers: { Authorization: "Bearer " + token }
})
  .then(res => res.ok ? res.json() : logout())
  .then(data => {
    allComplaints = data || [];
    applyFilters();
  });

function applyFilters() {
  const q = searchInput.value.toLowerCase();
  const status = statusFilter.value;

  const filtered = allComplaints.filter(c =>
    (c.title.toLowerCase().includes(q) || String(c.id).includes(q)) &&
    (!status || c.status === status)
  );

  render(filtered);
}

function render(list) {
  tbody.innerHTML = "";

  if (list.length === 0) {
    document.getElementById("emptyState").style.display = "block";
    return;
  }

  document.getElementById("emptyState").style.display = "none";

  list.forEach(c => {
    const canEdit = c.status !== "RESOLVED";

    tbody.innerHTML += `
      <tr>
        <td>${c.id}</td>
        <td>${c.title}</td>
        <td><span class="badge ${c.status}">${c.status}</span></td>
        <td>
          ${canEdit
            ? `<button class="btn btn-outline" onclick="editComplaint(${c.id})">Edit</button>`
            : `<span style="color:var(--muted)">Locked</span>`
          }
        </td>
      </tr>`;
  });
}

function editComplaint(id) {
  localStorage.setItem("editComplaintId", id);
  window.location.href = "/createcomplaint.html";
}

searchInput.addEventListener("input", applyFilters);
statusFilter.addEventListener("change", applyFilters);

function logout() {
  localStorage.clear();
  window.location.href = "/login.html";
}
