import { Routes } from '@angular/router';
import { HmsAppointmentsComponent } from './hms-appointments.component';
import { LoginComponent } from './login.component';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'appointments', component: HmsAppointmentsComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' },
];
