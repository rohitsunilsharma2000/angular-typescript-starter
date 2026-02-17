import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { ApiService } from '../../core/api.service';
import { AuthService, Session } from '../../core/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = 'principal@classroom.com';
  password = 'Admin';
  err = '';

  constructor(private api: ApiService, private auth: AuthService, private router: Router) {}

  quick(email: string, pass: string) {
    this.email = email;
    this.password = pass;
  }

  login() {
    this.err = '';
    this.api.post<Session>('/auth/login', { email: this.email, password: this.password }).subscribe({
      next: (res) => {
        this.auth.session = res;
        this.router.navigate(['/dashboard']);
      },
      error: (e) => {
        this.err = e?.error?.message || 'Login failed';
      }
    });
  }
}
