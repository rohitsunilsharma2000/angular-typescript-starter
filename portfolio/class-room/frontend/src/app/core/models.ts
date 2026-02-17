export type Role = 'PRINCIPAL' | 'TEACHER' | 'STUDENT';

export interface User {
  id: string;
  role: Role;
  name: string;
  email: string;
  classroomId?: string | null;
  teacherId?: string | null;
}

export interface Classroom {
  id: string;
  name: string;
  dayWindows: string;
  teacherId?: string | null;
}

export interface Period {
  id: string;
  classroomId: string;
  day: string;
  subject: string;
  startTime: string;
  endTime: string;
}
