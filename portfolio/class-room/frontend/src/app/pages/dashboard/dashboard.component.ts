import { Component, OnInit } from '@angular/core';

import { ApiService } from '../../core/api.service';
import { AuthService } from '../../core/auth.service';
import { Classroom, Period, User } from '../../core/models';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  tab: 'users' | 'classrooms' | 'assign' | 'timetable' | 'classmates' = 'users';

  users: User[] = [];
  classrooms: Classroom[] = [];
  classmates: User[] = [];
  periods: Period[] = [];

  selectedClassroomId: string | null = null;

  newUser = { role: 'TEACHER', name: '', email: '', password: '' };
  newClassroom = { name: '', dayWindows: 'Mon=12:00-18:00;Tue=12:00-18:00;Sat=12:00-16:00' };
  assignTeacher = { teacherId: '', classroomId: '' };
  assignStudent = { studentId: '', teacherId: '' };
  newPeriod = { classroomId: '', day: 'Mon', subject: '', startTime: '12:00', endTime: '13:00' };

  toast = '';

  get teachers(): User[] {
    return this.users.filter((u) => u.role === 'TEACHER');
  }

  get students(): User[] {
    return this.users.filter((u) => u.role === 'STUDENT');
  }

  constructor(public auth: AuthService, private api: ApiService) {}

  ngOnInit() {
    if (this.auth.session?.role === 'PRINCIPAL') {
      this.tab = 'users';
    } else if (this.auth.session?.role === 'TEACHER') {
      this.tab = 'classmates';
    } else {
      this.tab = 'timetable';
    }

    this.refreshAll();
  }

  show(msg: string) {
    this.toast = msg;
    setTimeout(() => {
      this.toast = '';
    }, 2500);
  }

  refreshAll() {
    this.api.get<Classroom[]>('/classrooms').subscribe({
      next: (cs) => {
        this.classrooms = cs;
        if (!this.selectedClassroomId) {
          this.selectedClassroomId = cs[0]?.id || null;
        }

        if (this.auth.session?.role === 'PRINCIPAL') {
          this.loadPeriods(this.selectedClassroomId);
        } else {
          this.loadClassmatesAndInferPeriods();
        }
      }
    });

    if (this.auth.session?.role === 'PRINCIPAL') {
      this.api.get<User[]>('/users').subscribe({
        next: (u) => {
          this.users = u;
          if (this.teachers[0]) {
            this.assignTeacher.teacherId = this.teachers[0].id;
            this.assignStudent.teacherId = this.teachers[0].id;
          }
          if (this.students[0]) {
            this.assignStudent.studentId = this.students[0].id;
          }
          if (this.classrooms[0]) {
            this.assignTeacher.classroomId = this.classrooms[0].id;
          }
        }
      });
    }
  }

  createUser() {
    this.api.post<User>('/users', this.newUser).subscribe({
      next: () => {
        this.show('User created');
        this.newUser = { role: 'TEACHER', name: '', email: '', password: '' };
        this.refreshAll();
      },
      error: (e) => this.show(e?.error?.message || 'Create failed')
    });
  }

  deleteUser(id: string) {
    this.api.del<void>('/users/' + id).subscribe({
      next: () => {
        this.show('User deleted');
        this.refreshAll();
      },
      error: (e) => this.show(e?.error?.message || 'Delete failed')
    });
  }

  createClassroom() {
    this.api.post<Classroom>('/classrooms', this.newClassroom).subscribe({
      next: () => {
        this.show('Classroom created');
        this.refreshAll();
      },
      error: (e) => this.show(e?.error?.message || 'Create failed')
    });
  }

  deleteClassroom(id: string) {
    this.api.del<void>('/classrooms/' + id).subscribe({
      next: () => {
        this.show('Classroom deleted');
        this.refreshAll();
      },
      error: (e) => this.show(e?.error?.message || 'Delete failed')
    });
  }

  assignTeacherToClassroom() {
    this.api.post<void>('/assign/teacher-classroom', this.assignTeacher).subscribe({
      next: () => {
        this.show('Teacher assigned');
        this.refreshAll();
      },
      error: (e) => this.show(e?.error?.message || 'Assign failed')
    });
  }

  assignStudentToTeacher() {
    this.api.post<void>('/assign/student-teacher', this.assignStudent).subscribe({
      next: () => {
        this.show('Student assigned');
        this.refreshAll();
      },
      error: (e) => this.show(e?.error?.message || 'Assign failed')
    });
  }

  loadPeriods(classroomId: string | null) {
    if (!classroomId) {
      return;
    }
    this.api.get<Period[]>('/timetable/' + classroomId).subscribe({
      next: (p) => {
        this.periods = p;
        this.newPeriod.classroomId = classroomId;
      },
      error: (e) => this.show(e?.error?.message || 'Load timetable failed')
    });
  }

  addPeriod() {
    this.api.post<Period>('/timetable', this.newPeriod).subscribe({
      next: () => {
        this.show('Period added');
        this.loadPeriods(this.newPeriod.classroomId);
      },
      error: (e) => this.show(e?.error?.message || 'Add failed')
    });
  }

  deletePeriod(id: string) {
    this.api.del<void>('/timetable/' + id).subscribe({
      next: () => {
        this.show('Period deleted');
        this.loadPeriods(this.newPeriod.classroomId);
      },
      error: (e) => this.show(e?.error?.message || 'Delete failed')
    });
  }

  loadClassmatesAndInferPeriods() {
    this.api.get<User[]>('/timetable/classmates').subscribe({
      next: (list) => {
        this.classmates = list;
        const cid = list[0]?.classroomId || null;
        if (cid) {
          this.selectedClassroomId = cid;
          this.loadPeriods(cid);
        }
      },
      error: (e) => this.show(e?.error?.message || 'Load classmates failed')
    });
  }

  logout() {
    this.auth.logout();
    location.href = '/login';
  }
}
