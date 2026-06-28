/* ============================================================
   Hospital Management – Shared JS Utilities
   Used by: admin-dashboard, doctor-dashboard, patient-dashboard
   NOT used by: login.html and register.html (they handle auth inline)
   ============================================================ */

const API_BASE = '/api';

/* ── Auth storage helpers ── */
const Auth = {
  save(data)   { localStorage.setItem('hms_user', JSON.stringify(data)); },
  get()        { return JSON.parse(localStorage.getItem('hms_user') || 'null'); },
  token()      { return Auth.get()?.token || ''; },
  role() {
    const roles = Auth.get()?.roles || [];
    if (roles.includes('ROLE_ADMIN'))   return 'ADMIN';
    if (roles.includes('ROLE_DOCTOR'))  return 'DOCTOR';
    if (roles.includes('ROLE_PATIENT')) return 'PATIENT';
    return null;
  },
  logout() {
    localStorage.removeItem('hms_user');
    window.location.href = '/login.html';
  },
  requireRole(expected) {
    const user = Auth.get();
    if (!user) { window.location.href = '/login.html'; return false; }
    if (expected && Auth.role() !== expected) {
      window.location.href = '/login.html'; return false;
    }
    return true;
  }
};

/* ── Authenticated HTTP helpers ── */
async function apiRequest(method, path, body) {
  const headers = {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + Auth.token()
  };
  const opts = { method, headers };
  if (body !== undefined && body !== null) opts.body = JSON.stringify(body);
  const res  = await fetch(API_BASE + path, opts);
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.message || 'Request failed (' + res.status + ')');
  return data;
}

/* Named helpers used across all dashboard pages */
function get(path)        { return apiRequest('GET',    path, null); }
function post(path, body) { return apiRequest('POST',   path, body); }
function put(path, body)  { return apiRequest('PUT',    path, body); }
function del(path)        { return apiRequest('DELETE', path, null); }

/* ── Toast notifications ── */
function showToast(message, type) {
  type = type || 'success';
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    container.style.cssText =
      'position:fixed;top:1rem;right:1rem;z-index:99999;display:flex;flex-direction:column;gap:.5rem;';
    document.body.appendChild(container);
  }
  const colors = { success: '#198754', danger: '#dc3545', warning: '#e0a800', info: '#0d6efd' };
  const color  = colors[type] || colors.success;
  const toast  = document.createElement('div');
  toast.style.cssText =
    'background:#fff;border-left:4px solid ' + color + ';border-radius:8px;' +
    'padding:.75rem 1.25rem;box-shadow:0 4px 12px rgba(0,0,0,.15);' +
    'font-size:.875rem;max-width:340px;animation:hmsSlideIn .3s ease;min-width:200px;';
  toast.textContent = message;
  container.appendChild(toast);
  setTimeout(() => { toast.style.animation = 'hmsSlideOut .3s ease'; setTimeout(() => toast.remove(), 300); }, 3500);
}

/* ── Status badge ── */
function statusBadge(status) {
  const map = {
    PENDING:   'bg-warning text-dark',
    APPROVED:  'bg-success',
    REJECTED:  'bg-danger',
    CANCELLED: 'bg-secondary',
    COMPLETED: 'bg-primary'
  };
  return '<span class="badge ' + (map[status] || 'bg-secondary') + '">' + status + '</span>';
}

/* ── Populate topbar user info ── */
function populateTopbar() {
  const user = Auth.get();
  if (!user) return;
  const nameEl = document.getElementById('user-name');
  const roleEl = document.getElementById('user-role');
  if (nameEl) nameEl.textContent = (user.firstName || '') + ' ' + (user.lastName || '');
  if (roleEl) roleEl.textContent = Auth.role();
}

/* ── Animations ── */
(function injectStyles() {
  const s = document.createElement('style');
  s.textContent =
    '@keyframes hmsSlideIn{from{opacity:0;transform:translateX(60px)}to{opacity:1;transform:translateX(0)}}' +
    '@keyframes hmsSlideOut{from{opacity:1;transform:translateX(0)}to{opacity:0;transform:translateX(60px)}}';
  document.head.appendChild(s);
})();
