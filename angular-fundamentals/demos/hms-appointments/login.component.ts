import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'hms-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]],
  });

  message = '';

  submit() {
    if (this.form.invalid) return;
    // Fake auth: accept any email/pass, set token
    localStorage.setItem('token', 'demo-jwt-token');
    this.message = 'Logged in (mock token saved)';
    this.router.navigateByUrl('/appointments');
  }
}
