/***********************
 * AUTH + ROLE GUARD
 ***********************/
const token = localStorage.getItem("token");

function logout() {
  localStorage.clear();
  window.location.href = "/login.html";
}

if (!token) logout();

let payload;
try {
  payload = JSON.parse(atob(token.split(".")[1]));
} catch {
  logout();
}

if (payload.role !== "ADMIN") logout();

/***********************
 * STATE
 ***********************/
let page = 0;
const size = 5;

/***********************
 * LOAD COMPLAINTS
 ***********************/
async function loadComplaints() {
  try {
    const res = await fetch(`/api/admin/complaints?page=${page}&size=${size}`, {
      headers: {
        Authorization: "Bearer " + token
      }
    });

    if (!res.ok) {
      showToast("Failed to load complaints", true);
      return;
    }

    const data = await res.json();
    renderTable(data);
    updateMetrics(data);

  } catch (err) {
    console.error(err);
    showToast("Server error", true);
  }
}

/***********************
 * RENDER TABLE (SAFE)
 ***********************/
function renderTable(data) {
  const tbody = document.getElementById("complaintsBody");
  tbody.innerHTML = "";

  if (!data.content || data.content.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6">No complaints</td></tr>`;
    return;
  }

  data.content.forEach(c => {
    tbody.innerHTML += `
      <tr>
        <td>${c.id}</td>
        <td>${c.title}</td>
        <td>${c.userEmail}</td>
        <td>
          <select onchange="updateStatus(${c.id}, this.value)">
            ${["OPEN","IN_PROGRESS","RESOLVED","REJECTED"]
              .map(s => `<option ${s===c.status?"selected":""}>${s}</option>`)
              .join("")}
          </select>
        </td>
        <td><span class="badge ${c.status}">${c.status}</span></td>
      </tr>`;
  });

  document.getElementById("pageInfo").innerText =
    `Page ${data.number + 1} of ${data.totalPages}`;
}

function updateStatus(id, status) {
  fetch(`/api/admin/complaints/${id}/status?status=${status}`, {
    method: "PUT",
    headers: { Authorization: "Bearer " + token }
  }).then(loadComplaints);
}

/***********************
 * METRICS (GLOBAL SAFE)
 ***********************/
function updateMetrics(data) {
  document.getElementById("totalCount").innerText =
    data.totalElements ?? 0;

  let open = 0;
  let resolved = 0;

  data.content.forEach(c => {
    if (c.status === "OPEN") open++;
    if (c.status === "RESOLVED") resolved++;
  });

  document.getElementById("openCount").innerText = open;
  document.getElementById("resolvedCount").innerText = resolved;
}

/***********************
 * PAGINATION
 ***********************/
function nextPage() {
  page++;
  loadComplaints();
}

function prevPage() {
  if (page > 0) page--;
  loadComplaints();
}

/***********************
 * THEME
 ***********************/
function toggleTheme() {
  document.body.classList.toggle("dark");
  localStorage.setItem(
    "theme",
    document.body.classList.contains("dark") ? "dark" : "light"
  );
}

if (localStorage.getItem("theme") === "dark") {
  document.body.classList.add("dark");
}

/***********************
 * TOAST
 ***********************/
function showToast(msg, error = false) {
  const t = document.getElementById("toast");
  t.innerText = msg;
  t.style.background = error ? "#dc2626" : "#020617";
  t.classList.add("show");
  setTimeout(() => t.classList.remove("show"), 2500);
}

/***********************
 * INIT
 ***********************/
loadComplaints();
