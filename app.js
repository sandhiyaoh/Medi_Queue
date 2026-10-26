/**
 * MediQueue — Interactive Web Application
 * Design Pattern: Abstract Factory Pattern (JavaScript Engine)
 * Course: ISWE404L — Design Pattern | VIT
 * Team: Sandhiya O H (23MIS0114) & Deepak Kumar S (23MIS0191)
 */

// ============================================================
// 1. ABSTRACT FACTORY PATTERN ARCHITECTURE
// ============================================================

// --- ABSTRACT FACTORY CONTRACT ---
class HospitalFactory {
  createQueueManager() { throw new Error("Method must be implemented"); }
  createAppointmentScheduler() { throw new Error("Method must be implemented"); }
  createNotificationService() { throw new Error("Method must be implemented"); }
  getDepartmentName() { throw new Error("Method must be implemented"); }
}

// --- CONCRETE FACTORY 1: GENERAL HOSPITAL FACTORY ---
class GeneralHospitalFactory extends HospitalFactory {
  createQueueManager() { return new GeneralQueue(); }
  createAppointmentScheduler() { return new GeneralScheduler(); }
  createNotificationService() { return new SMSNotification(); }
  getDepartmentName() { return "General Department"; }
  getDepartmentCode() { return "GEN"; }
}

// --- CONCRETE FACTORY 2: EMERGENCY HOSPITAL FACTORY ---
class EmergencyHospitalFactory extends HospitalFactory {
  createQueueManager() { return new EmergencyQueue(); }
  createAppointmentScheduler() { return new EmergencyScheduler(); }
  createNotificationService() { return new EmergencyAlertNotification(); }
  getDepartmentName() { return "Emergency Department"; }
  getDepartmentCode() { return "EMG"; }
}

// --- CONCRETE FACTORY 3: VIP HOSPITAL FACTORY ---
class VIPHospitalFactory extends HospitalFactory {
  createQueueManager() { return new PremiumQueue(); }
  createAppointmentScheduler() { return new PriorityScheduler(); }
  createNotificationService() { return new PremiumNotification(); }
  getDepartmentName() { return "VIP Department"; }
  getDepartmentCode() { return "VIP"; }
}

// --- QUEUE MANAGERS (PRODUCT A) ---
class GeneralQueue {
  constructor() {
    this.queue = [];
    this.counter = 1;
  }
  addPatient(patient) {
    const token = {
      tokenId: `TKN-GEN-${String(this.counter).padStart(3, '0')}`,
      tokenNumber: this.counter++,
      status: "WAITING"
    };
    patient.token = token;
    const position = this.queue.length + 1;
    const waitTime = Math.max(0, (position - 1) * 15);
    const entry = { patient, token, position, waitTime, priority: 3 };
    this.queue.push(entry);
    return entry;
  }
  serveNext() {
    if (this.queue.length === 0) return null;
    const served = this.queue.shift();
    served.token.status = "COMPLETED";
    this.queue.forEach((item, idx) => {
      item.position = idx + 1;
      item.waitTime = Math.max(0, idx * 15);
    });
    return served;
  }
  getEntries() { return [...this.queue]; }
}

class EmergencyQueue {
  constructor() {
    this.queue = [];
    this.counter = 1;
  }
  addPatient(patient) {
    const token = {
      tokenId: `TKN-EMG-${String(this.counter).padStart(3, '0')}`,
      tokenNumber: this.counter++,
      status: "WAITING"
    };
    patient.token = token;
    // Priority 1: STAT Immediate Triage (Always sorted to the top)
    const entry = { patient, token, position: 1, waitTime: 0, priority: 1 };
    this.queue.unshift(entry);
    this.queue.forEach((item, idx) => { item.position = idx + 1; });
    return entry;
  }
  serveNext() {
    if (this.queue.length === 0) return null;
    const served = this.queue.shift();
    served.token.status = "COMPLETED";
    this.queue.forEach((item, idx) => { item.position = idx + 1; });
    return served;
  }
  getEntries() { return [...this.queue]; }
}

class PremiumQueue {
  constructor() {
    this.queue = [];
    this.counter = 1;
  }
  addPatient(patient) {
    const token = {
      tokenId: `TKN-VIP-${String(this.counter).padStart(3, '0')}`,
      tokenNumber: this.counter++,
      status: "WAITING"
    };
    patient.token = token;
    const position = this.queue.length + 1;
    const waitTime = Math.max(0, (position - 1) * 5);
    const entry = { patient, token, position, waitTime, priority: 2 };
    this.queue.push(entry);
    return entry;
  }
  serveNext() {
    if (this.queue.length === 0) return null;
    const served = this.queue.shift();
    served.token.status = "COMPLETED";
    this.queue.forEach((item, idx) => {
      item.position = idx + 1;
      item.waitTime = Math.max(0, idx * 5);
    });
    return served;
  }
  getEntries() { return [...this.queue]; }
}

// --- SCHEDULERS (PRODUCT B) ---
class GeneralScheduler {
  constructor() {
    this.appointments = [];
    this.counter = 1;
  }
  schedule(patient, doctor, timeSlot) {
    const apt = {
      id: `APT-GEN-${String(this.counter++).padStart(3, '0')}`,
      patient, doctor, timeSlot,
      dept: "GENERAL",
      status: "SCHEDULED"
    };
    this.appointments.push(apt);
    return apt;
  }
}

class EmergencyScheduler {
  constructor() {
    this.appointments = [];
    this.counter = 1;
  }
  schedule(patient, doctor, timeSlot) {
    const apt = {
      id: `APT-EMG-${String(this.counter++).padStart(3, '0')}`,
      patient, doctor,
      timeSlot: timeSlot || "IMMEDIATE",
      dept: "EMERGENCY",
      status: "SCHEDULED"
    };
    this.appointments.push(apt);
    return apt;
  }
}

class PriorityScheduler {
  constructor() {
    this.appointments = [];
    this.counter = 1;
  }
  schedule(patient, doctor, timeSlot) {
    const apt = {
      id: `APT-VIP-${String(this.counter++).padStart(3, '0')}`,
      patient, doctor, timeSlot,
      dept: "VIP",
      status: "SCHEDULED"
    };
    this.appointments.push(apt);
    return apt;
  }
}

// --- NOTIFICATION SERVICES (PRODUCT C) ---
class SMSNotification {
  notifyPatient(patient, msg) {
    logNotification(`[SMS ➔ ${patient.contact}] Dear ${patient.name}: ${msg}`, "feed-sms");
  }
  notifyDoctor(doctor, msg) {
    logNotification(`[SMS ➔ Dr. ${doctor.name}] ${msg}`, "feed-sms");
  }
  getChannel() { return "SMS"; }
}

class EmergencyAlertNotification {
  notifyPatient(patient, msg) {
    logNotification(`🚨 [EMERGENCY DISPATCH] Patient ${patient.name}: ${msg}`, "feed-emergency");
  }
  notifyDoctor(doctor, msg) {
    logNotification(`🚨 [STAT DOCTOR CALL] Dr. ${doctor.name}: ${msg}`, "feed-emergency");
  }
  broadcast(msg) {
    logNotification(`🚨🚨 [HOSPITAL-WIDE TRAUMA BROADCAST] ${msg}`, "feed-emergency");
  }
  getChannel() { return "SMS + EMERGENCY ALERT"; }
}

class PremiumNotification {
  notifyPatient(patient, msg) {
    logNotification(`✨ [VIP CONCIERGE ➔ ${patient.email}] Valued Guest ${patient.name}: ${msg}`, "feed-vip");
  }
  notifyDoctor(doctor, msg) {
    logNotification(`✨ [VIP CONSULTANT DESK] Dr. ${doctor.name}: ${msg}`, "feed-vip");
  }
  getChannel() { return "EMAIL + SMS + CONCIERGE"; }
}

// --- CLIENT CLASS: HospitalDepartment ---
class HospitalDepartment {
  constructor(factory) {
    this.factory = factory;
    this.queueManager = factory.createQueueManager();
    this.scheduler = factory.createAppointmentScheduler();
    this.notificationService = factory.createNotificationService();
    this.departmentName = factory.getDepartmentName();
    this.departmentCode = factory.getDepartmentCode();
    this.doctors = [];
    this.patients = [];
  }

  registerDoctor(doc) { this.doctors.push(doc); }
  getDoctors() { return this.doctors; }

  admitPatient(patient) {
    if (!this.patients.includes(patient)) this.patients.push(patient);
    const entry = this.queueManager.addPatient(patient);
    this.notificationService.notifyPatient(
      patient,
      `You are admitted to the ${this.departmentName} queue. Token #${String(entry.token.tokenNumber).padStart(3, '0')}.`
    );
    return entry;
  }

  bookAppointment(patient, doctor, timeSlot) {
    const apt = this.scheduler.schedule(patient, doctor, timeSlot);
    this.notificationService.notifyPatient(patient, `Your appointment is confirmed for ${timeSlot}.`);
    this.notificationService.notifyDoctor(doctor, `New appointment booked with ${patient.name} at ${timeSlot}.`);
    return apt;
  }

  serveNext() {
    const served = this.queueManager.serveNext();
    if (served) {
      if (this.notificationService.notifyPatientTurn) {
        this.notificationService.notifyPatientTurn(served.patient, served.token.tokenNumber);
      } else {
        this.notificationService.notifyPatient(served.patient, `Now Serving! Please proceed to consultation room (Token #${String(served.token.tokenNumber).padStart(3, '0')}).`);
      }
    }
    return served;
  }

  broadcastAlert(msg) {
    if (this.notificationService.broadcast) {
      this.notificationService.broadcast(msg);
    }
  }
}

// ============================================================
// 2. SYSTEM STATE INITIALIZATION
// ============================================================

let generalDept;
let emergencyDept;
let vipDept;
let allRegisteredPatients = [];
let allBookedAppointments = [];
let patientIdCounter = 1;

function initializeSystem() {
  generalDept = new HospitalDepartment(new GeneralHospitalFactory());
  emergencyDept = new HospitalDepartment(new EmergencyHospitalFactory());
  vipDept = new HospitalDepartment(new VIPHospitalFactory());

  // Default Specialist Doctors
  generalDept.registerDoctor({ id: "DOC-GEN-001", name: "Ramesh Kumar", spec: "General Physician", dept: "GENERAL" });
  emergencyDept.registerDoctor({ id: "DOC-EMG-002", name: "Priya Menon", spec: "Emergency Specialist", dept: "EMERGENCY" });
  vipDept.registerDoctor({ id: "DOC-VIP-003", name: "Arjun Nair", spec: "Senior Consultant", dept: "VIP" });

  allRegisteredPatients = [];
  allBookedAppointments = [];
  patientIdCounter = 1;

  populateDoctorSelect();
  updateUI();
  logNotification("System Initialized with 3 Department Factories (General, Emergency, VIP).", "item-info");
}

function getDeptByCode(code) {
  if (code === "EMERGENCY") return emergencyDept;
  if (code === "VIP") return vipDept;
  return generalDept;
}

// ============================================================
// 3. UI RENDERING & EVENT HANDLERS
// ============================================================

document.addEventListener("DOMContentLoaded", () => {
  initializeSystem();
  setupEventListeners();
  updateActiveFactoryIndicator("GENERAL");
});

function setupEventListeners() {
  // Tabs switching
  document.querySelectorAll(".tab-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"));
      document.querySelectorAll(".tab-pane").forEach(p => p.classList.remove("active"));
      btn.classList.add("active");
      const tabId = btn.getAttribute("data-tab");
      document.getElementById(tabId)?.classList.add("active");
    });
  });

  // Department Radio Selection in Patient Form
  document.querySelectorAll("input[name='deptChoice']").forEach(radio => {
    radio.addEventListener("change", (e) => {
      document.querySelectorAll(".dept-option").forEach(opt => opt.classList.remove("active"));
      e.target.closest(".dept-option").classList.add("active");
      const deptCode = e.target.value;
      const emgCheck = document.getElementById("isEmergencyCheck");
      if (deptCode === "EMERGENCY") {
        emgCheck.checked = true;
      } else if (!emgCheck.getAttribute("data-user-touched")) {
        emgCheck.checked = false;
      }
      updateActiveFactoryIndicator(deptCode);
      populateDoctorSelect(deptCode);
    });
  });

  document.getElementById("isEmergencyCheck")?.addEventListener("change", (e) => {
    e.target.setAttribute("data-user-touched", "true");
    if (e.target.checked) {
      const emgRadio = document.querySelector("input[name='deptChoice'][value='EMERGENCY']");
      if (emgRadio) {
        emgRadio.checked = true;
        emgRadio.dispatchEvent(new Event("change"));
      }
    }
  });

  // Form: Admit Patient
  document.getElementById("formAdmitPatient")?.addEventListener("submit", (e) => {
    e.preventDefault();
    const name = document.getElementById("patientName").value.trim();
    const age = parseInt(document.getElementById("patientAge").value) || 25;
    const contact = document.getElementById("patientContact").value.trim() || "9876543210";
    const email = document.getElementById("patientEmail").value.trim() || `${name.toLowerCase().replace(/\s+/g, '')}@hospital.com`;
    const deptCode = document.querySelector("input[name='deptChoice']:checked").value;
    const isEmergency = document.getElementById("isEmergencyCheck").checked;

    const patient = {
      id: `PAT-${String(patientIdCounter++).padStart(3, '0')}`,
      name, age, contact, email,
      deptCode, isEmergency
    };
    allRegisteredPatients.push(patient);

    const dept = getDeptByCode(deptCode);
    const entry = dept.admitPatient(patient);

    updateTokenPassCard(entry, dept);
    updateUI();

    // Reset some fields
    document.getElementById("patientName").value = "";
    document.getElementById("patientAge").value = "";
    document.getElementById("patientContact").value = "";
    document.getElementById("patientEmail").value = "";
  });

  // Form: Book Appointment
  document.getElementById("formBookAppointment")?.addEventListener("submit", (e) => {
    e.preventDefault();
    const patientId = document.getElementById("apptPatientSelect").value;
    const doctorId = document.getElementById("apptDoctorSelect").value;
    const timeSlot = document.getElementById("apptTimeSlot").value;

    const patient = allRegisteredPatients.find(p => p.id === patientId);
    if (!patient) return alert("Please select a registered patient.");

    const dept = getDeptByCode(patient.deptCode);
    const doctor = dept.getDoctors().find(d => d.id === doctorId) || dept.getDoctors()[0];

    const apt = dept.bookAppointment(patient, doctor, timeSlot);
    allBookedAppointments.push(apt);
    updateAppointmentsTable();
    updateUI();
    alert(`Appointment confirmed for ${patient.name} with Dr. ${doctor.name} at ${timeSlot}!`);
  });

  // Form: Emergency Broadcast
  document.getElementById("formEmergencyBroadcast")?.addEventListener("submit", (e) => {
    e.preventDefault();
    const msg = document.getElementById("emergencyMessage").value.trim();
    if (msg) {
      emergencyDept.broadcastAlert(msg);
      alert("Emergency Alert Broadcasted across all departments!");
    }
  });

  // Serve Buttons
  document.getElementById("btnServeEmergency")?.addEventListener("click", () => {
    const served = emergencyDept.serveNext();
    if (served) {
      alert(`Emergency Patient Called: ${served.patient.name} (Token #${served.token.tokenNumber}) to Trauma Bay.`);
    } else {
      alert("No patients in the Emergency queue.");
    }
    updateUI();
  });

  document.getElementById("btnServeVIP")?.addEventListener("click", () => {
    const served = vipDept.serveNext();
    if (served) {
      alert(`VIP Guest Called: ${served.patient.name} (Token #${served.token.tokenNumber}) to Executive Lounge.`);
    } else {
      alert("No guests in the VIP Lounge queue.");
    }
    updateUI();
  });

  document.getElementById("btnServeGeneral")?.addEventListener("click", () => {
    const served = generalDept.serveNext();
    if (served) {
      alert(`General OPD Patient Called: ${served.patient.name} (Token #${served.token.tokenNumber}) to Consultation Room.`);
    } else {
      alert("No patients in General OPD queue.");
    }
    updateUI();
  });

  // Load Test Data
  document.getElementById("btnLoadTestData")?.addEventListener("click", () => {
    loadPreconfiguredTestData();
  });

  // Reset
  document.getElementById("btnResetSystem")?.addEventListener("click", () => {
    if (confirm("Reset all patient queues and appointments?")) {
      initializeSystem();
    }
  });

  // Clear Feed
  document.getElementById("btnClearFeed")?.addEventListener("click", () => {
    const feed = document.getElementById("notificationsFeed");
    if (feed) feed.innerHTML = "";
  });
}

// ============================================================
// 4. LOAD PRECONFIGURED TEST DATA (Matches test_data.json)
// ============================================================

function loadPreconfiguredTestData() {
  initializeSystem();
  logNotification("Loading Test Data from test_data.json...", "item-info");

  const testPatients = [
    { name: "Sandhiya", age: 22, contact: "9000000001", email: "sandhiya@email.com", dept: "GENERAL", emg: false },
    { name: "Deepak Kumar", age: 23, contact: "9000000002", email: "deepak@email.com", dept: "GENERAL", emg: false },
    { name: "Aravind", age: 45, contact: "9000000003", email: "aravind@email.com", dept: "EMERGENCY", emg: true },
    { name: "Meena", age: 38, contact: "9000000004", email: "meena@email.com", dept: "VIP", emg: false }
  ];

  let lastEntry = null;
  let lastDept = null;

  testPatients.forEach(tp => {
    const p = {
      id: `PAT-${String(patientIdCounter++).padStart(3, '0')}`,
      name: tp.name,
      age: tp.age,
      contact: tp.contact,
      email: tp.email,
      deptCode: tp.dept,
      isEmergency: tp.emg
    };
    allRegisteredPatients.push(p);
    const d = getDeptByCode(tp.dept);
    const entry = d.admitPatient(p);
    lastEntry = entry;
    lastDept = d;
  });

  // Book sample appointments
  const sandhiya = allRegisteredPatients[0];
  const aravind = allRegisteredPatients[2];
  const meena = allRegisteredPatients[3];

  allBookedAppointments.push(generalDept.bookAppointment(sandhiya, generalDept.getDoctors()[0], "10:00 AM"));
  allBookedAppointments.push(emergencyDept.bookAppointment(aravind, emergencyDept.getDoctors()[0], "IMMEDIATE"));
  allBookedAppointments.push(vipDept.bookAppointment(meena, vipDept.getDoctors()[0], "02:00 PM"));

  if (lastEntry && lastDept) {
    updateTokenPassCard(lastEntry, lastDept);
  }

  updateUI();
  updateAppointmentsTable();
  logNotification("✓ Test data loaded: 4 patients admitted & 3 appointments scheduled across departments.", "item-info");
}

// ============================================================
// 5. DOM UPDATERS
// ============================================================

function updateUI() {
  updateStatsRibbon();
  renderQueueLists();
  populatePatientSelect();
  updateAppointmentsTable();
}

function updateStatsRibbon() {
  const genEntries = generalDept.queueManager.getEntries();
  const emgEntries = emergencyDept.queueManager.getEntries();
  const vipEntries = vipDept.queueManager.getEntries();

  const totalInQueue = genEntries.length + emgEntries.length + vipEntries.length;
  const emergencyCount = allRegisteredPatients.filter(p => p.isEmergency).length;

  document.getElementById("statTotalPatients").textContent = allRegisteredPatients.length;
  document.getElementById("statInQueue").textContent = totalInQueue;
  document.getElementById("statEmergencyCount").textContent = emergencyCount;
  document.getElementById("statAppointmentsCount").textContent = allBookedAppointments.length;

  document.getElementById("countGeneral").textContent = genEntries.length;
  document.getElementById("countEmergency").textContent = emgEntries.length;
  document.getElementById("countVIP").textContent = vipEntries.length;
  document.getElementById("appointmentsTotalBadge").textContent = `${allBookedAppointments.length} Appointments`;
}

function renderQueueLists() {
  renderDeptQueue("queueGeneralList", generalDept.queueManager.getEntries(), "No patients in OPD queue");
  renderDeptQueue("queueEmergencyList", emergencyDept.queueManager.getEntries(), "Emergency ward is clear");
  renderDeptQueue("queueVIPList", vipDept.queueManager.getEntries(), "VIP lounge queue is clear");
}

function renderDeptQueue(elementId, entries, emptyMsg) {
  const container = document.getElementById(elementId);
  if (!container) return;

  if (entries.length === 0) {
    container.innerHTML = `<div class="empty-state">${emptyMsg}</div>`;
    return;
  }

  container.innerHTML = entries.map(entry => `
    <div class="patient-chip">
      <div class="chip-main">
        <span class="chip-token-badge">#${String(entry.token.tokenNumber).padStart(3, '0')}</span>
        <div class="chip-info">
          <span class="chip-name">${escapeHtml(entry.patient.name)}</span>
          <span class="chip-sub">${entry.patient.id} • ${entry.patient.contact}</span>
        </div>
      </div>
      <div class="chip-wait">
        ${entry.waitTime === 0 ? '<span style="color:#f87171;font-weight:700;">STAT (0m)</span>' : `${entry.waitTime} mins`}
      </div>
    </div>
  `).join("");
}

function updateTokenPassCard(entry, dept) {
  const passCard = document.getElementById("tokenPassCard");
  if (!passCard) return;

  document.getElementById("ticketDeptName").textContent = dept.departmentName;
  document.getElementById("ticketTokenNumber").textContent = `#${String(entry.token.tokenNumber).padStart(3, '0')}`;
  document.getElementById("ticketTokenId").textContent = entry.token.tokenId;
  document.getElementById("ticketPatientName").textContent = entry.patient.name;
  document.getElementById("ticketPatientId").textContent = entry.patient.id;
  document.getElementById("ticketQueuePos").textContent = `#${entry.position}`;
  document.getElementById("ticketWaitTime").textContent = `${entry.waitTime} mins`;
  document.getElementById("ticketChannelTag").textContent = `Channel: ${dept.notificationService.getChannel()}`;
  document.getElementById("ticketTimestamp").textContent = `Issued: ${new Date().toLocaleTimeString()}`;

  const badge = document.getElementById("ticketPriorityBadge");
  if (entry.priority === 1) {
    badge.textContent = "CRITICAL STAT";
    badge.style.background = "#fef2f2";
    badge.style.color = "#b91c1c";
    badge.style.borderColor = "#fecaca";
  } else if (entry.priority === 2) {
    badge.textContent = "VIP CONCIERGE";
    badge.style.background = "#f5f3ff";
    badge.style.color = "#6d28d9";
    badge.style.borderColor = "#ddd6fe";
  } else {
    badge.textContent = "REGULAR FIFO";
    badge.style.background = "#ecfdf5";
    badge.style.color = "#047857";
    badge.style.borderColor = "#a7f3d0";
  }
}

function updateActiveFactoryIndicator(deptCode) {
  const pill = document.getElementById("activeFactoryPill");
  const qVal = document.getElementById("productQueueVal");
  const sVal = document.getElementById("productSchedulerVal");
  const nVal = document.getElementById("productNotifVal");

  if (deptCode === "EMERGENCY") {
    pill.textContent = "EmergencyHospitalFactory";
    pill.style.color = "#b91c1c";
    qVal.textContent = "EmergencyQueue (Priority Heap)";
    sVal.textContent = "EmergencyScheduler (STAT Slots)";
    nVal.textContent = "EmergencyAlertNotification (Alarm)";
  } else if (deptCode === "VIP") {
    pill.textContent = "VIPHospitalFactory";
    pill.style.color = "#6d28d9";
    qVal.textContent = "PremiumQueue (Expedited Lounge)";
    sVal.textContent = "PriorityScheduler (Concierge)";
    nVal.textContent = "PremiumNotification (Multi-channel)";
  } else {
    pill.textContent = "GeneralHospitalFactory";
    pill.style.color = "#1e40af";
    qVal.textContent = "GeneralQueue (FIFO)";
    sVal.textContent = "GeneralScheduler (Standard)";
    nVal.textContent = "SMSNotification (SMS)";
  }
}

function populatePatientSelect() {
  const select = document.getElementById("apptPatientSelect");
  if (!select) return;
  const currentVal = select.value;
  select.innerHTML = '<option value="">-- Choose a registered patient --</option>';
  allRegisteredPatients.forEach(p => {
    const opt = document.createElement("option");
    opt.value = p.id;
    opt.textContent = `${p.name} (${p.id}) [${p.deptCode}]`;
    select.appendChild(opt);
  });
  if (currentVal) select.value = currentVal;
}

function populateDoctorSelect(filterDept) {
  const select = document.getElementById("apptDoctorSelect");
  if (!select) return;
  select.innerHTML = '<option value="">-- Choose a doctor --</option>';

  const allDocs = [
    ...generalDept.getDoctors(),
    ...emergencyDept.getDoctors(),
    ...vipDept.getDoctors()
  ];

  const docsToShow = filterDept ? allDocs.filter(d => d.dept === filterDept) : allDocs;

  docsToShow.forEach(doc => {
    const opt = document.createElement("option");
    opt.value = doc.id;
    opt.textContent = `Dr. ${doc.name} (${doc.spec}) [${doc.dept}]`;
    select.appendChild(opt);
  });
}

function updateAppointmentsTable() {
  const tbody = document.getElementById("appointmentsTableBody");
  if (!tbody) return;

  if (allBookedAppointments.length === 0) {
    tbody.innerHTML = '<tr class="empty-row"><td colspan="7">No appointments booked yet.</td></tr>';
    return;
  }

  tbody.innerHTML = allBookedAppointments.map((apt, idx) => `
    <tr>
      <td><strong>${apt.id}</strong></td>
      <td>${escapeHtml(apt.patient.name)} <small style="color:var(--text-dim);">(${apt.patient.id})</small></td>
      <td>Dr. ${escapeHtml(apt.doctor.name)}</td>
      <td><span class="count-pill ${apt.dept === 'EMERGENCY' ? 'emg-pill' : (apt.dept === 'VIP' ? 'vip-pill' : 'gen-pill')}">${apt.dept}</span></td>
      <td><strong>${apt.timeSlot}</strong></td>
      <td><span class="status-badge status-${apt.status.toLowerCase()}">${apt.status}</span></td>
      <td>
        ${apt.status !== 'CANCELLED' ? `
          <button type="button" class="btn btn-xs btn-secondary" onclick="rescheduleAppointmentPrompt(${idx})">Reschedule</button>
          <button type="button" class="btn btn-xs btn-ghost" style="color:#f87171;" onclick="cancelAppointment(${idx})">Cancel</button>
        ` : '<span style="color:var(--text-dim);font-size:0.75rem;">Closed</span>'}
      </td>
    </tr>
  `).join("");
}

window.rescheduleAppointmentPrompt = function(index) {
  const apt = allBookedAppointments[index];
  if (!apt) return;
  const newTime = prompt(`Enter new time slot for ${apt.patient.name} (current: ${apt.timeSlot}):`, "03:30 PM");
  if (newTime && newTime.trim()) {
    apt.timeSlot = newTime.trim();
    apt.status = "RESCHEDULED";
    const dept = getDeptByCode(apt.dept);
    dept.notificationService.notifyPatient(apt.patient, `Your appointment ${apt.id} has been rescheduled to ${newTime.trim()}.`);
    updateAppointmentsTable();
  }
};

window.cancelAppointment = function(index) {
  const apt = allBookedAppointments[index];
  if (!apt) return;
  if (confirm(`Cancel appointment ${apt.id} for ${apt.patient.name}?`)) {
    apt.status = "CANCELLED";
    const dept = getDeptByCode(apt.dept);
    dept.notificationService.notifyPatient(apt.patient, `Your appointment ${apt.id} has been cancelled.`);
    updateAppointmentsTable();
  }
};

function logNotification(content, typeClass = "item-info") {
  const feed = document.getElementById("notificationsFeed");
  if (!feed) return;
  const time = new Date().toLocaleTimeString();
  const div = document.createElement("div");
  div.className = `feed-item ${typeClass}`;
  div.innerHTML = `
    <span class="feed-time">${time}</span>
    <div class="feed-content">${content}</div>
  `;
  feed.prepend(div);
}

function escapeHtml(str) {
  return String(str || "").replace(/[&<>"']/g, (m) => {
    switch (m) {
      case '&': return '&amp;';
      case '<': return '&lt;';
      case '>': return '&gt;';
      case '"': return '&quot;';
      case "'": return '&#39;';
      default: return m;
    }
  });
}
