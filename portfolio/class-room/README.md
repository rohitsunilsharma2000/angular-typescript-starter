Sure — below is a **single-page (one HTML file)** app using **TailwindCSS + plain JavaScript** with **dummy data** that matches the screening assignment roles + dashboard scopes (Principal/Teacher/Student). 

> ✅ Features included (dummy):

* Login as **Principal / Teacher / Student**
* **Role-based dashboard**
* Principal: create classroom, assign teacher to classroom (1 classroom per teacher), assign students to teacher, CRUD (basic edit/delete)
* Teacher: see students in their classroom, edit/delete student, add timetable (simple validation: within classroom time + no overlaps)
* Student: see classmates + timetable (if added)

---

```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Classroom Dashboard (Dummy) • Tailwind + JS</title>
  <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-slate-50 text-slate-900">
  <!-- Top Bar -->
  <header class="sticky top-0 z-10 bg-white/80 backdrop-blur border-b border-slate-200">
    <div class="max-w-7xl mx-auto px-4 py-3 flex items-center justify-between">
      <div class="flex items-center gap-3">
        <div class="w-9 h-9 rounded-xl bg-slate-900 text-white grid place-items-center font-bold">C</div>
        <div>
          <div class="font-semibold leading-tight">Classroom (Dummy)</div>
          <div class="text-xs text-slate-500">Tailwind + Vanilla JS • Single page</div>
        </div>
      </div>
      <div class="flex items-center gap-3">
        <div id="whoami" class="text-sm text-slate-600 hidden"></div>
        <button id="logoutBtn" class="hidden px-3 py-1.5 rounded-lg border border-slate-300 hover:bg-slate-100 text-sm">
          Logout
        </button>
      </div>
    </div>
  </header>

  <main class="max-w-7xl mx-auto px-4 py-6">
    <!-- Login -->
    <section id="loginView" class="max-w-xl mx-auto">
      <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-5">
        <h1 class="text-xl font-semibold">Login</h1>
        <p class="text-sm text-slate-600 mt-1">
          Dummy login for Principal / Teacher / Student.
        </p>

        <div class="mt-4 space-y-3">
          <div>
            <label class="text-sm text-slate-600">Email</label>
            <input id="email" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-slate-900"
                   placeholder="principal@classroom.com" />
          </div>
          <div>
            <label class="text-sm text-slate-600">Password</label>
            <input id="password" type="password" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-slate-900"
                   placeholder="Admin" />
          </div>

          <button id="loginBtn"
                  class="w-full px-4 py-2.5 rounded-xl bg-slate-900 text-white font-medium hover:bg-slate-800">
            Sign in
          </button>

          <div class="rounded-xl border border-slate-200 bg-slate-50 p-3 text-sm">
            <div class="font-semibold mb-2">Quick logins</div>
            <div class="grid md:grid-cols-3 gap-2">
              <button class="quick px-3 py-2 rounded-lg bg-white border hover:bg-slate-100 text-left"
                      data-email="principal@classroom.com" data-pass="Admin">
                <div class="font-medium">Principal</div>
                <div class="text-xs text-slate-500">principal@classroom.com</div>
              </button>
              <button class="quick px-3 py-2 rounded-lg bg-white border hover:bg-slate-100 text-left"
                      data-email="t1@classroom.com" data-pass="t1">
                <div class="font-medium">Teacher</div>
                <div class="text-xs text-slate-500">t1@classroom.com</div>
              </button>
              <button class="quick px-3 py-2 rounded-lg bg-white border hover:bg-slate-100 text-left"
                      data-email="s1@classroom.com" data-pass="s1">
                <div class="font-medium">Student</div>
                <div class="text-xs text-slate-500">s1@classroom.com</div>
              </button>
            </div>
          </div>

          <div id="loginError" class="hidden text-sm text-red-600"></div>
        </div>
      </div>
    </section>

    <!-- App -->
    <section id="appView" class="hidden">
      <!-- Tabs header -->
      <div class="flex flex-col md:flex-row md:items-end md:justify-between gap-3">
        <div>
          <h2 id="dashTitle" class="text-2xl font-semibold">Dashboard</h2>
          <p id="dashSub" class="text-sm text-slate-600">Role scoped view</p>
        </div>

        <div class="flex gap-2 flex-wrap">
          <button id="tabUsers" class="tabBtn hidden px-3 py-2 rounded-xl border border-slate-300 hover:bg-slate-100 text-sm">Users</button>
          <button id="tabClassrooms" class="tabBtn hidden px-3 py-2 rounded-xl border border-slate-300 hover:bg-slate-100 text-sm">Classrooms</button>
          <button id="tabAssignments" class="tabBtn hidden px-3 py-2 rounded-xl border border-slate-300 hover:bg-slate-100 text-sm">Assignments</button>
          <button id="tabTimetable" class="tabBtn hidden px-3 py-2 rounded-xl border border-slate-300 hover:bg-slate-100 text-sm">Timetable</button>
          <button id="tabClassmates" class="tabBtn hidden px-3 py-2 rounded-xl border border-slate-300 hover:bg-slate-100 text-sm">Classmates</button>
        </div>
      </div>

      <!-- Alerts -->
      <div id="toast" class="hidden mt-4 rounded-xl border border-slate-200 bg-white p-3 text-sm"></div>

      <!-- Panels -->
      <div class="mt-5 grid grid-cols-1 gap-4">
        <div id="panelUsers" class="panel hidden"></div>
        <div id="panelClassrooms" class="panel hidden"></div>
        <div id="panelAssignments" class="panel hidden"></div>
        <div id="panelTimetable" class="panel hidden"></div>
        <div id="panelClassmates" class="panel hidden"></div>
      </div>
    </section>
  </main>

<script>
/** -----------------------------
 *  Dummy In-Memory "DB"
 * ------------------------------ */
const db = {
  users: [
    { id: 'p1', role: 'PRINCIPAL', name: 'Principal', email: 'principal@classroom.com', password: 'Admin' },

    { id: 't1', role: 'TEACHER', name: 'Teacher One', email: 't1@classroom.com', password: 't1', classroomId: 'c1' },
    { id: 't2', role: 'TEACHER', name: 'Teacher Two', email: 't2@classroom.com', password: 't2', classroomId: null },

    { id: 's1', role: 'STUDENT', name: 'Student A', email: 's1@classroom.com', password: 's1', classroomId: 'c1', teacherId: 't1' },
    { id: 's2', role: 'STUDENT', name: 'Student B', email: 's2@classroom.com', password: 's2', classroomId: 'c1', teacherId: 't1' },
    { id: 's3', role: 'STUDENT', name: 'Student C', email: 's3@classroom.com', password: 's3', classroomId: null, teacherId: null },
  ],
  classrooms: [
    {
      id: 'c1',
      name: 'Classroom 1',
      // schedule by weekday: { start: "HH:MM", end: "HH:MM" }
      days: {
        Mon: { start: '12:00', end: '18:00' },
        Tue: { start: '12:00', end: '18:00' },
        Wed: { start: '12:00', end: '18:00' },
        Thu: { start: '12:00', end: '18:00' },
        Fri: { start: '12:00', end: '18:00' },
        Sat: { start: '12:00', end: '16:00' },
      },
      teacherId: 't1',
      timetable: [
        // { id, day, subject, start, end }
        { id: 'tt1', day: 'Mon', subject: 'Math', start: '12:30', end: '13:30' },
        { id: 'tt2', day: 'Mon', subject: 'English', start: '14:00', end: '15:00' },
      ],
    }
  ]
};

let session = { userId: null };

/** -----------------------------
 *  Helpers
 * ------------------------------ */
const $ = (id) => document.getElementById(id);

function uid(prefix='id') {
  return prefix + '_' + Math.random().toString(16).slice(2) + '_' + Date.now().toString(16);
}

function toast(msg, type='info') {
  const el = $('toast');
  el.classList.remove('hidden');
  el.className = 'mt-4 rounded-xl border p-3 text-sm ' + (
    type === 'error'
      ? 'border-red-200 bg-red-50 text-red-700'
      : type === 'success'
        ? 'border-emerald-200 bg-emerald-50 text-emerald-800'
        : 'border-slate-200 bg-white text-slate-700'
  );
  el.textContent = msg;
  setTimeout(() => el.classList.add('hidden'), 2600);
}

function getUser() {
  return db.users.find(u => u.id === session.userId) || null;
}

function fmtRole(role) {
  if (role === 'PRINCIPAL') return 'Principal';
  if (role === 'TEACHER') return 'Teacher';
  if (role === 'STUDENT') return 'Student';
  return role;
}

function toMins(hhmm) {
  const [h,m] = hhmm.split(':').map(Number);
  return h*60 + m;
}

function overlaps(aStart, aEnd, bStart, bEnd) {
  // [aStart,aEnd) overlaps [bStart,bEnd)
  return Math.max(aStart, bStart) < Math.min(aEnd, bEnd);
}

function classroomOfUser(u) {
  if (!u) return null;
  return db.classrooms.find(c => c.id === u.classroomId) || null;
}

function classroomById(id) {
  return db.classrooms.find(c => c.id === id) || null;
}

function teacherById(id) {
  return db.users.find(u => u.id === id && u.role === 'TEACHER') || null;
}

/** -----------------------------
 *  Auth
 * ------------------------------ */
function login(email, password) {
  const found = db.users.find(u => u.email.toLowerCase() === email.toLowerCase() && u.password === password);
  if (!found) return { ok:false, error:'Invalid email/password (dummy).' };
  session.userId = found.id;
  return { ok:true };
}

function logout() {
  session.userId = null;
  render();
}

/** -----------------------------
 *  UI Render
 * ------------------------------ */
function setActivePanel(panelId) {
  document.querySelectorAll('.panel').forEach(p => p.classList.add('hidden'));
  $(panelId).classList.remove('hidden');

  // button style
  document.querySelectorAll('.tabBtn').forEach(b => {
    b.classList.remove('bg-slate-900','text-white','border-slate-900');
    b.classList.add('border-slate-300','hover:bg-slate-100');
  });
}

function showTab(btnId, show=true) {
  $(btnId).classList.toggle('hidden', !show);
}

function render() {
  const u = getUser();

  // views
  $('loginView').classList.toggle('hidden', !!u);
  $('appView').classList.toggle('hidden', !u);

  // top bar
  $('logoutBtn').classList.toggle('hidden', !u);
  $('whoami').classList.toggle('hidden', !u);
  if (u) $('whoami').textContent = `${fmtRole(u.role)} • ${u.name}`;

  if (!u) return;

  // role header
  $('dashTitle').textContent = `${fmtRole(u.role)} Dashboard`;
  $('dashSub').textContent =
    u.role === 'PRINCIPAL' ? 'Manage users, classrooms, and assignments.'
    : u.role === 'TEACHER' ? 'Manage students in your classroom and timetable.'
    : 'View classmates and your classroom timetable.';

  // tab visibility per role
  showTab('tabUsers', u.role === 'PRINCIPAL');
  showTab('tabClassrooms', u.role === 'PRINCIPAL');
  showTab('tabAssignments', u.role === 'PRINCIPAL');
  showTab('tabTimetable', u.role === 'PRINCIPAL' || u.role === 'TEACHER' || u.role === 'STUDENT');
  showTab('tabClassmates', u.role === 'TEACHER' || u.role === 'STUDENT');

  // default panel
  if (u.role === 'PRINCIPAL') {
    renderUsers();
    renderClassrooms();
    renderAssignments();
    renderTimetable(); // principal can view/edit timetables
    setActivePanel('panelUsers');
    $('tabUsers').classList.add('bg-slate-900','text-white','border-slate-900');
    $('tabUsers').classList.remove('border-slate-300');
  } else if (u.role === 'TEACHER') {
    renderClassmates();
    renderTimetable();
    setActivePanel('panelClassmates');
    $('tabClassmates').classList.add('bg-slate-900','text-white','border-slate-900');
    $('tabClassmates').classList.remove('border-slate-300');
  } else {
    renderClassmates();
    renderTimetable();
    setActivePanel('panelTimetable');
    $('tabTimetable').classList.add('bg-slate-900','text-white','border-slate-900');
    $('tabTimetable').classList.remove('border-slate-300');
  }
}

/** -----------------------------
 *  Principal: Users Panel
 * ------------------------------ */
function renderUsers() {
  const u = getUser();
  if (!u || u.role !== 'PRINCIPAL') return;

  const teachers = db.users.filter(x => x.role === 'TEACHER');
  const students = db.users.filter(x => x.role === 'STUDENT');

  $('panelUsers').innerHTML = `
    <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-5">
      <div class="flex items-start justify-between gap-3 flex-col md:flex-row">
        <div>
          <h3 class="text-lg font-semibold">Users</h3>
          <p class="text-sm text-slate-600">Teachers and students (dummy CRUD).</p>
        </div>
        <div class="flex gap-2 flex-wrap">
          <button class="px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
                  onclick="openCreateUser('TEACHER')">+ Create Teacher</button>
          <button class="px-3 py-2 rounded-xl border border-slate-300 text-sm hover:bg-slate-100"
                  onclick="openCreateUser('STUDENT')">+ Create Student</button>
        </div>
      </div>

      <div class="mt-5 grid grid-cols-1 lg:grid-cols-2 gap-4">
        ${userTable('Teachers', teachers)}
        ${userTable('Students', students)}
      </div>

      <div id="userFormWrap" class="hidden mt-5 rounded-xl border border-slate-200 bg-slate-50 p-4"></div>
    </div>
  `;
}

function userTable(title, rows) {
  return `
    <div class="rounded-xl border border-slate-200 overflow-hidden">
      <div class="px-4 py-3 bg-slate-50 border-b border-slate-200 flex items-center justify-between">
        <div class="font-semibold">${title}</div>
        <div class="text-xs text-slate-500">${rows.length} total</div>
      </div>
      <div class="overflow-auto">
        <table class="w-full text-sm">
          <thead class="bg-white">
            <tr class="text-left text-slate-500">
              <th class="px-4 py-2">Name</th>
              <th class="px-4 py-2">Email</th>
              <th class="px-4 py-2">Info</th>
              <th class="px-4 py-2">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y">
            ${
              rows.map(r => {
                const info = r.role === 'TEACHER'
                  ? `Classroom: ${r.classroomId ? classroomById(r.classroomId)?.name : '—'}`
                  : `Teacher: ${r.teacherId ? teacherById(r.teacherId)?.name : '—'}`;
                return `
                  <tr class="bg-white">
                    <td class="px-4 py-2 font-medium">${escapeHtml(r.name)}</td>
                    <td class="px-4 py-2">${escapeHtml(r.email)}</td>
                    <td class="px-4 py-2 text-slate-600">${escapeHtml(info || '—')}</td>
                    <td class="px-4 py-2">
                      <div class="flex gap-2">
                        <button class="px-2 py-1 rounded-lg border text-xs hover:bg-slate-100"
                                onclick="openEditUser('${r.id}')">Edit</button>
                        <button class="px-2 py-1 rounded-lg border text-xs hover:bg-slate-100"
                                onclick="deleteUser('${r.id}')">Delete</button>
                      </div>
                    </td>
                  </tr>
                `;
              }).join('')
            }
          </tbody>
        </table>
      </div>
    </div>
  `;
}

function openCreateUser(role) {
  const wrap = $('userFormWrap');
  wrap.classList.remove('hidden');

  wrap.innerHTML = `
    <div class="flex items-start justify-between gap-3 flex-col md:flex-row">
      <div>
        <div class="font-semibold">Create ${fmtRole(role)}</div>
        <div class="text-xs text-slate-600">Dummy create: email+password, basic fields.</div>
      </div>
      <button class="px-2 py-1 rounded-lg border text-xs hover:bg-white"
              onclick="closeUserForm()">Close</button>
    </div>

    <div class="mt-3 grid md:grid-cols-4 gap-3">
      <div class="md:col-span-2">
        <label class="text-xs text-slate-600">Name</label>
        <input id="uf_name" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" placeholder="Full name"/>
      </div>
      <div class="md:col-span-2">
        <label class="text-xs text-slate-600">Email</label>
        <input id="uf_email" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" placeholder="user@classroom.com"/>
      </div>
      <div class="md:col-span-2">
        <label class="text-xs text-slate-600">Password</label>
        <input id="uf_pass" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" placeholder="password"/>
      </div>
      <div class="md:col-span-2 flex items-end">
        <button class="w-full px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
                onclick="createUser('${role}')">Create</button>
      </div>
    </div>
  `;
}

function openEditUser(userId) {
  const user = db.users.find(u => u.id === userId);
  if (!user) return;

  const wrap = $('userFormWrap');
  wrap.classList.remove('hidden');

  wrap.innerHTML = `
    <div class="flex items-start justify-between gap-3 flex-col md:flex-row">
      <div>
        <div class="font-semibold">Edit ${fmtRole(user.role)}</div>
        <div class="text-xs text-slate-600">Update name/email/password (dummy).</div>
      </div>
      <button class="px-2 py-1 rounded-lg border text-xs hover:bg-white"
              onclick="closeUserForm()">Close</button>
    </div>

    <div class="mt-3 grid md:grid-cols-4 gap-3">
      <div class="md:col-span-2">
        <label class="text-xs text-slate-600">Name</label>
        <input id="uf_name" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" value="${escapeAttr(user.name)}"/>
      </div>
      <div class="md:col-span-2">
        <label class="text-xs text-slate-600">Email</label>
        <input id="uf_email" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" value="${escapeAttr(user.email)}"/>
      </div>
      <div class="md:col-span-2">
        <label class="text-xs text-slate-600">Password</label>
        <input id="uf_pass" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" value="${escapeAttr(user.password)}"/>
      </div>
      <div class="md:col-span-2 flex items-end">
        <button class="w-full px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
                onclick="saveUser('${user.id}')">Save</button>
      </div>
    </div>
  `;
}

function closeUserForm() {
  $('userFormWrap').classList.add('hidden');
  $('userFormWrap').innerHTML = '';
}

function createUser(role) {
  const name = $('uf_name').value.trim();
  const email = $('uf_email').value.trim();
  const pass = $('uf_pass').value.trim();
  if (!name || !email || !pass) return toast('All fields are required.', 'error');
  if (db.users.some(u => u.email.toLowerCase() === email.toLowerCase())) return toast('Email already exists.', 'error');

  const id = uid(role === 'TEACHER' ? 't' : 's');
  db.users.push({
    id,
    role,
    name,
    email,
    password: pass,
    classroomId: null,
    teacherId: null
  });

  toast(`${fmtRole(role)} created (dummy).`, 'success');
  renderUsers();
  renderAssignments();
}

function saveUser(id) {
  const user = db.users.find(u => u.id === id);
  if (!user) return;
  const name = $('uf_name').value.trim();
  const email = $('uf_email').value.trim();
  const pass = $('uf_pass').value.trim();
  if (!name || !email || !pass) return toast('All fields are required.', 'error');

  const collision = db.users.find(u => u.id !== id && u.email.toLowerCase() === email.toLowerCase());
  if (collision) return toast('Another user already uses that email.', 'error');

  user.name = name;
  user.email = email;
  user.password = pass;

  toast('User updated (dummy).', 'success');
  renderUsers();
  renderAssignments();
  renderClassmates();
}

function deleteUser(id) {
  const user = db.users.find(u => u.id === id);
  if (!user) return;

  // protect principal
  if (user.role === 'PRINCIPAL') return toast('Cannot delete principal in dummy.', 'error');

  // cleanup references
  if (user.role === 'TEACHER') {
    // detach classroom assignment + students
    db.classrooms.forEach(c => { if (c.teacherId === id) c.teacherId = null; });
    db.users.forEach(s => { if (s.teacherId === id) s.teacherId = null; });
  }
  if (user.role === 'STUDENT') {
    // nothing special
  }

  db.users = db.users.filter(u => u.id !== id);
  toast('User deleted (dummy).', 'success');
  renderUsers();
  renderClassrooms();
  renderAssignments();
  renderClassmates();
}

/** -----------------------------
 *  Principal: Classrooms Panel
 * ------------------------------ */
function renderClassrooms() {
  const u = getUser();
  if (!u || u.role !== 'PRINCIPAL') return;

  $('panelClassrooms').innerHTML = `
    <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-5">
      <div class="flex items-start justify-between gap-3 flex-col md:flex-row">
        <div>
          <h3 class="text-lg font-semibold">Classrooms</h3>
          <p class="text-sm text-slate-600">Create classroom and define days/time windows.</p>
        </div>
        <button class="px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
                onclick="openCreateClassroom()">+ Create Classroom</button>
      </div>

      <div class="mt-5 grid md:grid-cols-2 gap-4">
        ${db.classrooms.map(c => classroomCard(c)).join('')}
      </div>

      <div id="classroomFormWrap" class="hidden mt-5 rounded-xl border border-slate-200 bg-slate-50 p-4"></div>
    </div>
  `;
}

function classroomCard(c) {
  const t = c.teacherId ? teacherById(c.teacherId) : null;
  return `
    <div class="rounded-2xl border border-slate-200 bg-white p-4">
      <div class="flex items-start justify-between gap-3">
        <div>
          <div class="font-semibold">${escapeHtml(c.name)}</div>
          <div class="text-sm text-slate-600 mt-1">Teacher: ${t ? escapeHtml(t.name) : '—'}</div>
        </div>
        <div class="flex gap-2">
          <button class="px-2 py-1 rounded-lg border text-xs hover:bg-slate-100"
                  onclick="openEditClassroom('${c.id}')">Edit</button>
          <button class="px-2 py-1 rounded-lg border text-xs hover:bg-slate-100"
                  onclick="deleteClassroom('${c.id}')">Delete</button>
        </div>
      </div>
      <div class="mt-3 text-xs text-slate-500">Schedule windows</div>
      <div class="mt-2 grid grid-cols-2 gap-2">
        ${Object.entries(c.days).map(([day, w]) => `
          <div class="px-2 py-1 rounded-lg bg-slate-50 border border-slate-200 text-sm">
            <div class="font-medium">${day}</div>
            <div class="text-slate-600">${w.start} → ${w.end}</div>
          </div>
        `).join('')}
      </div>
      <div class="mt-3 text-xs text-slate-500">Timetable entries: ${c.timetable.length}</div>
    </div>
  `;
}

function openCreateClassroom() {
  const wrap = $('classroomFormWrap');
  wrap.classList.remove('hidden');

  wrap.innerHTML = `
    <div class="flex items-start justify-between gap-3 flex-col md:flex-row">
      <div>
        <div class="font-semibold">Create Classroom</div>
        <div class="text-xs text-slate-600">Add name + time windows per weekday (dummy).</div>
      </div>
      <button class="px-2 py-1 rounded-lg border text-xs hover:bg-white"
              onclick="closeClassroomForm()">Close</button>
    </div>

    <div class="mt-3 grid md:grid-cols-2 gap-3">
      <div>
        <label class="text-xs text-slate-600">Classroom name</label>
        <input id="cf_name" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" placeholder="e.g., Classroom 2"/>
      </div>
      <div class="rounded-xl border border-slate-200 bg-white p-3">
        <div class="text-sm font-medium">Weekday windows</div>
        <div class="text-xs text-slate-600 mb-2">Fill start/end for days you want enabled.</div>
        ${dayRow('Mon')}
        ${dayRow('Tue')}
        ${dayRow('Wed')}
        ${dayRow('Thu')}
        ${dayRow('Fri')}
        ${dayRow('Sat')}
      </div>
    </div>

    <div class="mt-3">
      <button class="px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
              onclick="createClassroom()">Create</button>
    </div>
  `;
}

function dayRow(day) {
  return `
    <div class="mt-2 grid grid-cols-3 gap-2 items-center">
      <div class="text-sm">${day}</div>
      <input id="cf_${day}_start" class="px-2 py-1 rounded-lg border border-slate-300 text-sm" placeholder="HH:MM" />
      <input id="cf_${day}_end" class="px-2 py-1 rounded-lg border border-slate-300 text-sm" placeholder="HH:MM" />
    </div>
  `;
}

function openEditClassroom(id) {
  const c = classroomById(id);
  if (!c) return;

  const wrap = $('classroomFormWrap');
  wrap.classList.remove('hidden');

  wrap.innerHTML = `
    <div class="flex items-start justify-between gap-3 flex-col md:flex-row">
      <div>
        <div class="font-semibold">Edit Classroom</div>
        <div class="text-xs text-slate-600">Update name and weekday windows (dummy).</div>
      </div>
      <button class="px-2 py-1 rounded-lg border text-xs hover:bg-white"
              onclick="closeClassroomForm()">Close</button>
    </div>

    <div class="mt-3 grid md:grid-cols-2 gap-3">
      <div>
        <label class="text-xs text-slate-600">Classroom name</label>
        <input id="cf_name" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" value="${escapeAttr(c.name)}"/>
      </div>
      <div class="rounded-xl border border-slate-200 bg-white p-3">
        <div class="text-sm font-medium">Weekday windows</div>
        <div class="text-xs text-slate-600 mb-2">Only days with valid start/end are enabled.</div>
        ${dayRowWithValues('Mon', c.days.Mon)}
        ${dayRowWithValues('Tue', c.days.Tue)}
        ${dayRowWithValues('Wed', c.days.Wed)}
        ${dayRowWithValues('Thu', c.days.Thu)}
        ${dayRowWithValues('Fri', c.days.Fri)}
        ${dayRowWithValues('Sat', c.days.Sat)}
      </div>
    </div>

    <div class="mt-3">
      <button class="px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
              onclick="saveClassroom('${c.id}')">Save</button>
    </div>
  `;
}

function dayRowWithValues(day, w) {
  const s = w?.start || '';
  const e = w?.end || '';
  return `
    <div class="mt-2 grid grid-cols-3 gap-2 items-center">
      <div class="text-sm">${day}</div>
      <input id="cf_${day}_start" class="px-2 py-1 rounded-lg border border-slate-300 text-sm" value="${escapeAttr(s)}" placeholder="HH:MM" />
      <input id="cf_${day}_end" class="px-2 py-1 rounded-lg border border-slate-300 text-sm" value="${escapeAttr(e)}" placeholder="HH:MM" />
    </div>
  `;
}

function closeClassroomForm() {
  $('classroomFormWrap').classList.add('hidden');
  $('classroomFormWrap').innerHTML = '';
}

function createClassroom() {
  const name = $('cf_name').value.trim();
  if (!name) return toast('Classroom name required.', 'error');

  const days = {};
  for (const d of ['Mon','Tue','Wed','Thu','Fri','Sat']) {
    const s = ($('cf_'+d+'_start').value || '').trim();
    const e = ($('cf_'+d+'_end').value || '').trim();
    if (!s && !e) continue;
    if (!isHHMM(s) || !isHHMM(e) || toMins(s) >= toMins(e)) return toast(`Invalid window for ${d}.`, 'error');
    days[d] = { start:s, end:e };
  }
  if (Object.keys(days).length === 0) return toast('At least one day window required.', 'error');

  db.classrooms.push({
    id: uid('c'),
    name,
    days,
    teacherId: null,
    timetable: []
  });

  toast('Classroom created (dummy).', 'success');
  renderClassrooms();
  renderAssignments();
}

function saveClassroom(id) {
  const c = classroomById(id);
  if (!c) return;

  const name = $('cf_name').value.trim();
  if (!name) return toast('Classroom name required.', 'error');

  const days = {};
  for (const d of ['Mon','Tue','Wed','Thu','Fri','Sat']) {
    const s = ($('cf_'+d+'_start').value || '').trim();
    const e = ($('cf_'+d+'_end').value || '').trim();
    if (!s && !e) continue;
    if (!isHHMM(s) || !isHHMM(e) || toMins(s) >= toMins(e)) return toast(`Invalid window for ${d}.`, 'error');
    days[d] = { start:s, end:e };
  }
  if (Object.keys(days).length === 0) return toast('At least one day window required.', 'error');

  c.name = name;
  c.days = days;

  // also ensure timetable entries still valid; if not, keep but warn
  toast('Classroom updated (dummy).', 'success');
  renderClassrooms();
  renderTimetable();
}

function deleteClassroom(id) {
  const c = classroomById(id);
  if (!c) return;
  // detach teacher + students
  if (c.teacherId) {
    const t = teacherById(c.teacherId);
    if (t) t.classroomId = null;
  }
  db.users.forEach(s => { if (s.role==='STUDENT' && s.classroomId===id) s.classroomId = null; });

  db.classrooms = db.classrooms.filter(x => x.id !== id);
  toast('Classroom deleted (dummy).', 'success');
  renderClassrooms();
  renderUsers();
  renderAssignments();
  renderTimetable();
  renderClassmates();
}

/** -----------------------------
 *  Principal: Assignments Panel
 * ------------------------------ */
function renderAssignments() {
  const u = getUser();
  if (!u || u.role !== 'PRINCIPAL') return;

  const teachers = db.users.filter(x => x.role === 'TEACHER');
  const students = db.users.filter(x => x.role === 'STUDENT');

  $('panelAssignments').innerHTML = `
    <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-5">
      <h3 class="text-lg font-semibold">Assignments</h3>
      <p class="text-sm text-slate-600 mt-1">Assign classroom to teacher (one classroom per teacher) and students to teachers.</p>

      <div class="mt-5 grid lg:grid-cols-2 gap-4">
        <div class="rounded-2xl border border-slate-200 p-4">
          <div class="font-semibold">Assign Classroom → Teacher</div>
          <div class="text-xs text-slate-600">Rule: a teacher can only be assigned to one classroom.</div>

          <div class="mt-3 grid grid-cols-1 md:grid-cols-3 gap-2">
            <select id="as_teacher" class="px-3 py-2 rounded-lg border border-slate-300 text-sm">
              ${teachers.map(t => `<option value="${t.id}">${escapeHtml(t.name)} (${t.classroomId ? 'Assigned' : 'Free'})</option>`).join('')}
            </select>

            <select id="as_classroom" class="px-3 py-2 rounded-lg border border-slate-300 text-sm">
              ${db.classrooms.map(c => `<option value="${c.id}">${escapeHtml(c.name)} (${c.teacherId ? 'Has teacher' : 'No teacher'})</option>`).join('')}
            </select>

            <button class="px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
                    onclick="assignTeacherToClassroom()">Assign</button>
          </div>

          <div class="mt-4 text-sm">
            ${teachers.map(t => `
              <div class="flex justify-between py-1 border-b last:border-b-0">
                <div>${escapeHtml(t.name)}</div>
                <div class="text-slate-600">${t.classroomId ? escapeHtml(classroomById(t.classroomId)?.name) : '—'}</div>
              </div>
            `).join('')}
          </div>
        </div>

        <div class="rounded-2xl border border-slate-200 p-4">
          <div class="font-semibold">Assign Student → Teacher</div>
          <div class="text-xs text-slate-600">Student will also inherit the teacher's classroom (dummy rule).</div>

          <div class="mt-3 grid grid-cols-1 md:grid-cols-3 gap-2">
            <select id="as_student" class="px-3 py-2 rounded-lg border border-slate-300 text-sm">
              ${students.map(s => `<option value="${s.id}">${escapeHtml(s.name)} (${s.teacherId ? 'Assigned' : 'Unassigned'})</option>`).join('')}
            </select>

            <select id="as_teacher2" class="px-3 py-2 rounded-lg border border-slate-300 text-sm">
              ${teachers.map(t => `<option value="${t.id}">${escapeHtml(t.name)}</option>`).join('')}
            </select>

            <button class="px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
                    onclick="assignStudentToTeacher()">Assign</button>
          </div>

          <div class="mt-4 text-sm">
            ${students.map(s => `
              <div class="flex justify-between py-1 border-b last:border-b-0">
                <div>${escapeHtml(s.name)}</div>
                <div class="text-slate-600">
                  ${s.teacherId ? escapeHtml(teacherById(s.teacherId)?.name) : '—'}
                  <span class="text-xs text-slate-400">•</span>
                  ${s.classroomId ? escapeHtml(classroomById(s.classroomId)?.name) : '—'}
                </div>
              </div>
            `).join('')}
          </div>
        </div>
      </div>
    </div>
  `;
}

function assignTeacherToClassroom() {
  const teacherId = $('as_teacher').value;
  const classroomId = $('as_classroom').value;

  const t = teacherById(teacherId);
  const c = classroomById(classroomId);
  if (!t || !c) return;

  // detach previous classroom for teacher
  if (t.classroomId && t.classroomId !== classroomId) {
    const old = classroomById(t.classroomId);
    if (old && old.teacherId === t.id) old.teacherId = null;
  }

  // detach existing teacher from classroom
  if (c.teacherId && c.teacherId !== teacherId) {
    const otherT = teacherById(c.teacherId);
    if (otherT) otherT.classroomId = null;
  }

  // assign
  t.classroomId = classroomId;
  c.teacherId = teacherId;

  // move students assigned to teacher into this classroom (dummy)
  db.users.forEach(s => {
    if (s.role==='STUDENT' && s.teacherId===teacherId) s.classroomId = classroomId;
  });

  toast('Teacher assigned to classroom (dummy).', 'success');
  renderUsers();
  renderClassrooms();
  renderAssignments();
  renderTimetable();
  renderClassmates();
}

function assignStudentToTeacher() {
  const studentId = $('as_student').value;
  const teacherId = $('as_teacher2').value;

  const s = db.users.find(u => u.id === studentId && u.role==='STUDENT');
  const t = teacherById(teacherId);
  if (!s || !t) return;

  s.teacherId = teacherId;

  // inherit classroom if teacher has one (dummy)
  s.classroomId = t.classroomId || null;

  toast('Student assigned to teacher (dummy).', 'success');
  renderUsers();
  renderAssignments();
  renderClassmates();
}

/** -----------------------------
 *  Timetable Panel
 *  - Principal can choose classroom to view/edit
 *  - Teacher can edit own classroom
 *  - Student can view own classroom
 * ------------------------------ */
function renderTimetable() {
  const u = getUser();
  if (!u) return;

  let classroom = null;
  let canEdit = false;

  if (u.role === 'PRINCIPAL') {
    classroom = db.classrooms[0] || null;
    canEdit = true;
  } else if (u.role === 'TEACHER') {
    classroom = classroomOfUser(u);
    canEdit = true;
  } else {
    classroom = classroomOfUser(u);
    canEdit = false;
  }

  const selector = (u.role === 'PRINCIPAL')
    ? `
      <div class="mt-3">
        <label class="text-xs text-slate-600">Select classroom</label>
        <select id="tt_selectClassroom" class="mt-1 w-full md:w-80 px-3 py-2 rounded-lg border border-slate-300 text-sm"
                onchange="onSelectTimetableClassroom()">
          ${db.classrooms.map(c => `<option value="${c.id}">${escapeHtml(c.name)}</option>`).join('')}
        </select>
      </div>
    ` : '';

  const emptyState = `
    <div class="rounded-2xl border border-slate-200 bg-white p-5">
      <h3 class="text-lg font-semibold">Timetable</h3>
      <p class="text-sm text-slate-600 mt-1">No classroom assigned.</p>
    </div>
  `;

  if (!classroom && u.role !== 'PRINCIPAL') {
    $('panelTimetable').innerHTML = emptyState;
    return;
  }

  // Principal default: first classroom
  const c = classroom || db.classrooms[0] || null;
  if (!c) {
    $('panelTimetable').innerHTML = emptyState;
    return;
  }

  $('panelTimetable').innerHTML = `
    <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-5">
      <div class="flex items-start justify-between gap-3 flex-col md:flex-row">
        <div>
          <h3 class="text-lg font-semibold">Timetable</h3>
          <p class="text-sm text-slate-600">Classroom: <span class="font-medium">${escapeHtml(c.name)}</span></p>
          <div class="text-xs text-slate-500 mt-1">Rules (dummy): period must be within day window; no overlaps.</div>
        </div>
        <div class="flex gap-2">
          ${canEdit ? `
            <button class="px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
                    onclick="openAddPeriod('${c.id}')">+ Add period</button>
          ` : ''}
        </div>
      </div>

      ${selector}

      <div class="mt-5 grid md:grid-cols-2 gap-4">
        ${renderTimetableDays(c)}
      </div>

      <div id="periodFormWrap" class="hidden mt-5 rounded-xl border border-slate-200 bg-slate-50 p-4"></div>
    </div>
  `;
}

function onSelectTimetableClassroom() {
  const id = $('tt_selectClassroom').value;
  // rerender panel using chosen classroom
  const u = getUser();
  if (!u || u.role !== 'PRINCIPAL') return;
  const c = classroomById(id);
  if (!c) return;

  // rebuild using selected classroom by temporarily setting first
  // simpler: render panel then set c in DOM via replace
  // We'll just rebuild directly:
  const canEdit = true;

  $('panelTimetable').innerHTML = `
    <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-5">
      <div class="flex items-start justify-between gap-3 flex-col md:flex-row">
        <div>
          <h3 class="text-lg font-semibold">Timetable</h3>
          <p class="text-sm text-slate-600">Classroom: <span class="font-medium">${escapeHtml(c.name)}</span></p>
          <div class="text-xs text-slate-500 mt-1">Rules (dummy): period must be within day window; no overlaps.</div>
        </div>
        <div class="flex gap-2">
          ${canEdit ? `
            <button class="px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
                    onclick="openAddPeriod('${c.id}')">+ Add period</button>
          ` : ''}
        </div>
      </div>

      <div class="mt-3">
        <label class="text-xs text-slate-600">Select classroom</label>
        <select id="tt_selectClassroom" class="mt-1 w-full md:w-80 px-3 py-2 rounded-lg border border-slate-300 text-sm"
                onchange="onSelectTimetableClassroom()">
          ${db.classrooms.map(x => `<option value="${x.id}" ${x.id===c.id?'selected':''}>${escapeHtml(x.name)}</option>`).join('')}
        </select>
      </div>

      <div class="mt-5 grid md:grid-cols-2 gap-4">
        ${renderTimetableDays(c)}
      </div>

      <div id="periodFormWrap" class="hidden mt-5 rounded-xl border border-slate-200 bg-slate-50 p-4"></div>
    </div>
  `;
}

function renderTimetableDays(c) {
  const days = Object.keys(c.days);
  return days.map(day => {
    const w = c.days[day];
    const items = c.timetable.filter(p => p.day === day).sort((a,b) => toMins(a.start) - toMins(b.start));
    return `
      <div class="rounded-2xl border border-slate-200 p-4">
        <div class="flex items-start justify-between">
          <div>
            <div class="font-semibold">${day}</div>
            <div class="text-sm text-slate-600">${w.start} → ${w.end}</div>
          </div>
          <div class="text-xs text-slate-500">${items.length} periods</div>
        </div>
        <div class="mt-3 space-y-2">
          ${items.length ? items.map(p => periodRow(c.id, p)).join('') : `
            <div class="text-sm text-slate-500">No periods.</div>
          `}
        </div>
      </div>
    `;
  }).join('');
}

function periodRow(classroomId, p) {
  const u = getUser();
  const canEdit = u && (u.role === 'PRINCIPAL' || u.role === 'TEACHER');
  return `
    <div class="rounded-xl border border-slate-200 bg-white p-3 flex items-center justify-between gap-3">
      <div>
        <div class="font-medium">${escapeHtml(p.subject)}</div>
        <div class="text-sm text-slate-600">${p.start} → ${p.end}</div>
      </div>
      ${canEdit ? `
        <div class="flex gap-2">
          <button class="px-2 py-1 rounded-lg border text-xs hover:bg-slate-100"
                  onclick="openEditPeriod('${classroomId}','${p.id}')">Edit</button>
          <button class="px-2 py-1 rounded-lg border text-xs hover:bg-slate-100"
                  onclick="deletePeriod('${classroomId}','${p.id}')">Delete</button>
        </div>
      ` : ''}
    </div>
  `;
}

function openAddPeriod(classroomId) {
  const c = classroomById(classroomId);
  if (!c) return;

  const wrap = $('periodFormWrap');
  wrap.classList.remove('hidden');

  wrap.innerHTML = `
    <div class="flex items-start justify-between gap-3 flex-col md:flex-row">
      <div>
        <div class="font-semibold">Add period</div>
        <div class="text-xs text-slate-600">Must fit within classroom window and not overlap.</div>
      </div>
      <button class="px-2 py-1 rounded-lg border text-xs hover:bg-white" onclick="closePeriodForm()">Close</button>
    </div>

    <div class="mt-3 grid md:grid-cols-5 gap-3">
      <div class="md:col-span-2">
        <label class="text-xs text-slate-600">Subject</label>
        <input id="pf_subject" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" placeholder="e.g., Science"/>
      </div>
      <div>
        <label class="text-xs text-slate-600">Day</label>
        <select id="pf_day" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300 text-sm">
          ${Object.keys(c.days).map(d => `<option value="${d}">${d}</option>`).join('')}
        </select>
      </div>
      <div>
        <label class="text-xs text-slate-600">Start</label>
        <input id="pf_start" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" placeholder="HH:MM"/>
      </div>
      <div>
        <label class="text-xs text-slate-600">End</label>
        <input id="pf_end" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" placeholder="HH:MM"/>
      </div>
    </div>

    <div class="mt-3">
      <button class="px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
              onclick="addPeriod('${classroomId}')">Add</button>
    </div>
  `;
}

function openEditPeriod(classroomId, periodId) {
  const c = classroomById(classroomId);
  if (!c) return;
  const p = c.timetable.find(x => x.id === periodId);
  if (!p) return;

  const wrap = $('periodFormWrap');
  wrap.classList.remove('hidden');

  wrap.innerHTML = `
    <div class="flex items-start justify-between gap-3 flex-col md:flex-row">
      <div>
        <div class="font-semibold">Edit period</div>
        <div class="text-xs text-slate-600">Validation rules still apply.</div>
      </div>
      <button class="px-2 py-1 rounded-lg border text-xs hover:bg-white" onclick="closePeriodForm()">Close</button>
    </div>

    <div class="mt-3 grid md:grid-cols-5 gap-3">
      <div class="md:col-span-2">
        <label class="text-xs text-slate-600">Subject</label>
        <input id="pf_subject" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" value="${escapeAttr(p.subject)}"/>
      </div>
      <div>
        <label class="text-xs text-slate-600">Day</label>
        <select id="pf_day" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300 text-sm">
          ${Object.keys(c.days).map(d => `<option value="${d}" ${d===p.day?'selected':''}>${d}</option>`).join('')}
        </select>
      </div>
      <div>
        <label class="text-xs text-slate-600">Start</label>
        <input id="pf_start" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" value="${escapeAttr(p.start)}"/>
      </div>
      <div>
        <label class="text-xs text-slate-600">End</label>
        <input id="pf_end" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" value="${escapeAttr(p.end)}"/>
      </div>
    </div>

    <div class="mt-3">
      <button class="px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
              onclick="savePeriod('${classroomId}','${periodId}')">Save</button>
    </div>
  `;
}

function closePeriodForm() {
  $('periodFormWrap').classList.add('hidden');
  $('periodFormWrap').innerHTML = '';
}

function addPeriod(classroomId) {
  const c = classroomById(classroomId);
  if (!c) return;

  const subject = $('pf_subject').value.trim();
  const day = $('pf_day').value;
  const start = $('pf_start').value.trim();
  const end = $('pf_end').value.trim();
  const err = validatePeriod(c, null, { day, subject, start, end });
  if (err) return toast(err, 'error');

  c.timetable.push({ id: uid('tt'), day, subject, start, end });
  toast('Period added (dummy).', 'success');

  renderTimetable();
  renderClassmates();
}

function savePeriod(classroomId, periodId) {
  const c = classroomById(classroomId);
  if (!c) return;

  const p = c.timetable.find(x => x.id === periodId);
  if (!p) return;

  const subject = $('pf_subject').value.trim();
  const day = $('pf_day').value;
  const start = $('pf_start').value.trim();
  const end = $('pf_end').value.trim();
  const err = validatePeriod(c, periodId, { day, subject, start, end });
  if (err) return toast(err, 'error');

  p.subject = subject;
  p.day = day;
  p.start = start;
  p.end = end;

  toast('Period updated (dummy).', 'success');
  renderTimetable();
  renderClassmates();
}

function deletePeriod(classroomId, periodId) {
  const c = classroomById(classroomId);
  if (!c) return;
  c.timetable = c.timetable.filter(x => x.id !== periodId);
  toast('Period deleted (dummy).', 'success');
  renderTimetable();
  renderClassmates();
}

function validatePeriod(classroom, editingId, p) {
  if (!p.subject) return 'Subject required.';
  if (!classroom.days[p.day]) return 'Invalid day for this classroom.';
  if (!isHHMM(p.start) || !isHHMM(p.end)) return 'Time must be HH:MM.';
  const s = toMins(p.start), e = toMins(p.end);
  if (s >= e) return 'Start must be before end.';

  const w = classroom.days[p.day];
  const ws = toMins(w.start), we = toMins(w.end);
  if (s < ws || e > we) return `Must fit within ${p.day} window (${w.start}–${w.end}).`;

  // overlap check in same day
  const sameDay = classroom.timetable.filter(x => x.day === p.day && x.id !== editingId);
  for (const x of sameDay) {
    if (overlaps(s, e, toMins(x.start), toMins(x.end))) {
      return `Overlaps with "${x.subject}" (${x.start}–${x.end}).`;
    }
  }
  return null;
}

/** -----------------------------
 *  Teacher/Student: Classmates Panel
 * ------------------------------ */
function renderClassmates() {
  const u = getUser();
  if (!u) return;

  let classroomId = null;

  if (u.role === 'TEACHER') {
    classroomId = u.classroomId;
  } else if (u.role === 'STUDENT') {
    classroomId = u.classroomId;
  } else {
    // principal doesn't need this
    $('panelClassmates').innerHTML = '';
    return;
  }

  const c = classroomById(classroomId);
  if (!c) {
    $('panelClassmates').innerHTML = `
      <div class="rounded-2xl border border-slate-200 bg-white p-5">
        <h3 class="text-lg font-semibold">Classmates</h3>
        <p class="text-sm text-slate-600 mt-1">No classroom assigned.</p>
      </div>
    `;
    return;
  }

  const classmates = db.users.filter(x => x.role==='STUDENT' && x.classroomId===c.id);

  const canEditStudents = (u.role === 'TEACHER'); // per assignment teacher can edit/delete student
  $('panelClassmates').innerHTML = `
    <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-5">
      <h3 class="text-lg font-semibold">${u.role==='TEACHER' ? 'Students in your classroom' : 'Your classmates'}</h3>
      <p class="text-sm text-slate-600 mt-1">
        Classroom: <span class="font-medium">${escapeHtml(c.name)}</span>
      </p>

      <div class="mt-4 rounded-xl border border-slate-200 overflow-auto">
        <table class="w-full text-sm">
          <thead class="bg-slate-50">
            <tr class="text-left text-slate-600">
              <th class="px-4 py-2">Name</th>
              <th class="px-4 py-2">Email</th>
              <th class="px-4 py-2">Assigned Teacher</th>
              ${canEditStudents ? `<th class="px-4 py-2">Actions</th>` : ''}
            </tr>
          </thead>
          <tbody class="divide-y bg-white">
            ${classmates.map(s => `
              <tr>
                <td class="px-4 py-2 font-medium">${escapeHtml(s.name)}</td>
                <td class="px-4 py-2">${escapeHtml(s.email)}</td>
                <td class="px-4 py-2 text-slate-600">${s.teacherId ? escapeHtml(teacherById(s.teacherId)?.name) : '—'}</td>
                ${canEditStudents ? `
                  <td class="px-4 py-2">
                    <div class="flex gap-2">
                      <button class="px-2 py-1 rounded-lg border text-xs hover:bg-slate-100"
                              onclick="openTeacherEditStudent('${s.id}')">Edit</button>
                      <button class="px-2 py-1 rounded-lg border text-xs hover:bg-slate-100"
                              onclick="teacherDeleteStudent('${s.id}')">Delete</button>
                    </div>
                  </td>
                ` : ''}
              </tr>
            `).join('')}
          </tbody>
        </table>
      </div>

      <div id="teacherStudentForm" class="hidden mt-4 rounded-xl border border-slate-200 bg-slate-50 p-4"></div>
    </div>
  `;
}

function openTeacherEditStudent(studentId) {
  const u = getUser();
  if (!u || u.role !== 'TEACHER') return;
  const s = db.users.find(x => x.id === studentId && x.role==='STUDENT');
  if (!s) return;

  // teacher can only edit student in own classroom
  if (s.classroomId !== u.classroomId) return toast('Not in your classroom.', 'error');

  const wrap = $('teacherStudentForm');
  wrap.classList.remove('hidden');
  wrap.innerHTML = `
    <div class="flex items-start justify-between">
      <div>
        <div class="font-semibold">Edit student</div>
        <div class="text-xs text-slate-600">Teacher scoped edit (dummy).</div>
      </div>
      <button class="px-2 py-1 rounded-lg border text-xs hover:bg-white"
              onclick="closeTeacherStudentForm()">Close</button>
    </div>

    <div class="mt-3 grid md:grid-cols-3 gap-3">
      <div>
        <label class="text-xs text-slate-600">Name</label>
        <input id="ts_name" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" value="${escapeAttr(s.name)}"/>
      </div>
      <div>
        <label class="text-xs text-slate-600">Email</label>
        <input id="ts_email" class="mt-1 w-full px-3 py-2 rounded-lg border border-slate-300" value="${escapeAttr(s.email)}"/>
      </div>
      <div class="flex items-end">
        <button class="w-full px-3 py-2 rounded-xl bg-slate-900 text-white text-sm hover:bg-slate-800"
                onclick="teacherSaveStudent('${s.id}')">Save</button>
      </div>
    </div>
  `;
}

function closeTeacherStudentForm() {
  const wrap = $('teacherStudentForm');
  if (!wrap) return;
  wrap.classList.add('hidden');
  wrap.innerHTML = '';
}

function teacherSaveStudent(studentId) {
  const u = getUser();
  if (!u || u.role !== 'TEACHER') return;
  const s = db.users.find(x => x.id === studentId && x.role==='STUDENT');
  if (!s) return;

  const name = $('ts_name').value.trim();
  const email = $('ts_email').value.trim();
  if (!name || !email) return toast('Name/email required.', 'error');

  const collision = db.users.find(x => x.id !== s.id && x.email.toLowerCase() === email.toLowerCase());
  if (collision) return toast('Email already in use.', 'error');

  s.name = name;
  s.email = email;

  toast('Student updated (dummy).', 'success');
  renderClassmates();
  if (getUser().role === 'PRINCIPAL') renderUsers();
}

function teacherDeleteStudent(studentId) {
  const u = getUser();
  if (!u || u.role !== 'TEACHER') return;
  const s = db.users.find(x => x.id === studentId && x.role==='STUDENT');
  if (!s) return;

  if (s.classroomId !== u.classroomId) return toast('Not in your classroom.', 'error');

  db.users = db.users.filter(x => x.id !== studentId);
  toast('Student deleted (dummy).', 'success');
  renderClassmates();
}

/** -----------------------------
 *  Utilities: validation + escaping
 * ------------------------------ */
function isHHMM(v) {
  // 00:00 - 23:59
  return /^([01]\d|2[0-3]):([0-5]\d)$/.test(v);
}

function escapeHtml(s) {
  return String(s)
    .replaceAll('&','&amp;')
    .replaceAll('<','&lt;')
    .replaceAll('>','&gt;')
    .replaceAll('"','&quot;')
    .replaceAll("'","&#039;");
}
function escapeAttr(s) { return escapeHtml(s).replaceAll('\n',' '); }

/** -----------------------------
 *  Wire up events
 * ------------------------------ */
$('loginBtn').addEventListener('click', () => {
  $('loginError').classList.add('hidden');
  const email = $('email').value.trim();
  const pass = $('password').value.trim();
  const res = login(email, pass);
  if (!res.ok) {
    $('loginError').textContent = res.error;
    $('loginError').classList.remove('hidden');
    return;
  }
  toast('Logged in (dummy).', 'success');
  render();
});

document.querySelectorAll('.quick').forEach(btn => {
  btn.addEventListener('click', () => {
    $('email').value = btn.dataset.email;
    $('password').value = btn.dataset.pass;
  });
});

$('logoutBtn').addEventListener('click', logout);

// tabs
$('tabUsers').addEventListener('click', () => { setActivePanel('panelUsers'); $('tabUsers').classList.add('bg-slate-900','text-white','border-slate-900'); $('tabUsers').classList.remove('border-slate-300'); });
$('tabClassrooms').addEventListener('click', () => { setActivePanel('panelClassrooms'); $('tabClassrooms').classList.add('bg-slate-900','text-white','border-slate-900'); $('tabClassrooms').classList.remove('border-slate-300'); });
$('tabAssignments').addEventListener('click', () => { setActivePanel('panelAssignments'); $('tabAssignments').classList.add('bg-slate-900','text-white','border-slate-900'); $('tabAssignments').classList.remove('border-slate-300'); });
$('tabTimetable').addEventListener('click', () => { setActivePanel('panelTimetable'); $('tabTimetable').classList.add('bg-slate-900','text-white','border-slate-900'); $('tabTimetable').classList.remove('border-slate-300'); });
$('tabClassmates').addEventListener('click', () => { setActivePanel('panelClassmates'); $('tabClassmates').classList.add('bg-slate-900','text-white','border-slate-900'); $('tabClassmates').classList.remove('border-slate-300'); });

// initial
render();
</script>
</body>
</html>
```

**How to run**

1. Create a file: `index.html`
2. Paste the code
3. Open in browser (double click)

If you want, I can also make the **same UI but with localStorage persistence** (still no backend) so refresh করলে data থাকবে.
