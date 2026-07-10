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


/* ── Consultation message helpers ── */
const MessageAPI = {
  patientSend(doctorId, message) {
    return post('/messages/patient/send', { doctorId: parseInt(doctorId), message: message });
  },
  doctorReply(patientId, message) {
    return post('/messages/doctor/reply', { patientId: parseInt(patientId), message: message });
  },
  patientMessages() { return get('/messages/patient'); },
  doctorMessages() { return get('/messages/doctor'); },
  conversation(params) {

    const query = new URLSearchParams();

    if (params && params.doctorId) {
        query.set("doctorId", params.doctorId);
        return get("/messages/conversation?" + query.toString());
    }

    if (params && params.patientId) {
        query.set("patientId", params.patientId);
        return get("/messages/doctor/conversation?" + query.toString());
    }

    throw new Error("Invalid conversation parameters");
}
  // conversation(params) {
  //   const query = new URLSearchParams();
  //   if (params && params.doctorId) query.set('doctorId', params.doctorId);
  //   if (params && params.patientId) query.set('patientId', params.patientId);
  //   const suffix = query.toString() ? '?' + query.toString() : '';
  //   return get('/messages/conversation' + suffix);
  // }
};

function messageText(message) { return (message && (message.message || message.content || message.text || message.reply)) || ''; }

function messageSenderRole(message) {
    return (
        (
            message &&
            (
                message.sender ||
                message.senderRole ||
                message.role ||
                message.sentBy
            )
        ) || ''
    ).toString().toUpperCase();
}
// function messageSenderRole(message) { return ((message && (message.senderRole || message.role || message.sentBy)) || '').toString().toUpperCase(); }
function messageTime(message) {
  const raw = message && (message.createdAt || message.sentAt || message.timestamp || message.dateTime);
  if (!raw) return '';
  const date = new Date(raw);
  if (Number.isNaN(date.getTime())) return raw;
  return date.toLocaleString([], { day: '2-digit', month: 'short', hour: '2-digit', minute: '2-digit' });
}
function escapeHtml(value) {
  return String(value == null ? '' : value)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#039;');
}

/* ── Payment helpers ── */
const PaymentAPI = {
  pay(doctorId, amount, paymentMethod) {
    return post('/payment/pay', {
      doctorId: parseInt(doctorId),
      amount: parseFloat(amount),
      paymentMethod: paymentMethod
    });
  },
  history() {
    return get('/payment/history');
  },
  details(paymentId) {
    return get('/payment/' + paymentId);
  }
};

function formatCurrency(amount) {
  const value = Number(amount || 0);
  return '₹' + value.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function formatDateTime(value) {
  if (!value) return '—';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleString('en-IN', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

function paymentStatusBadge(status) {
  const value = (status || 'PENDING').toString().toUpperCase();
  const map = {
    SUCCESS: 'bg-success',
    PENDING: 'bg-warning text-dark',
    FAILED: 'bg-danger',
    REFUNDED: 'bg-info text-dark'
  };
  return '<span class="badge ' + (map[value] || 'bg-secondary') + '">' + value + '</span>';
}

function paymentMethodLabel(method) {
  const value = (method || '').toString().toUpperCase();
  const map = {
    CARD: 'Card',
    UPI: 'UPI',
    CASH: 'Cash',
    NET_BANKING: 'Net Banking'
  };
  return map[value] || value || '—';
}

/* ── Animations ── */
(function injectStyles() {
  const s = document.createElement('style');
  s.textContent =
    '@keyframes hmsSlideIn{from{opacity:0;transform:translateX(60px)}to{opacity:1;transform:translateX(0)}}' +
    '@keyframes hmsSlideOut{from{opacity:1;transform:translateX(0)}to{opacity:0;transform:translateX(60px)}}';
  document.head.appendChild(s);
})();
