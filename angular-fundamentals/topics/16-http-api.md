# 16) HTTP + API Integration (Must) – বাংলা + হাসপাতাল উদাহরণ (ডামি API)

## কী শিখবেন
- HttpClient বেসিক: GET / POST / PUT / DELETE
- Headers, params
- টাইপড রেসপন্স `Observable<ApiResponse<User>>`
- Interceptor: JWT token attach + global error handler
- Retry / timeout (বেসিক)

Startup প্রশ্ন: “Interceptor কেন?”, “token attach কিভাবে?”

## Dummy API রেফারেন্স
- Patients list: `https://dummyjson.com/users?limit=5`
- Create appointment (mock): `https://jsonplaceholder.typicode.com/posts`
- Update record (PUT mock): `https://jsonplaceholder.typicode.com/posts/1`
- Delete (fake): `https://jsonplaceholder.typicode.com/posts/1`

## পূর্ণ রানযোগ্য ন্যূনতম কোড (folder tree + files)
**ট্রি (`src/app/`):**
```
src/app/
  app.routes.ts
  app.component.ts
  app.component.html
  services/api.service.ts
  interceptors/auth.interceptor.ts
```

### app.routes.ts
```ts
import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
export const routes: Routes = [{ path: '', component: AppComponent }];
```

### interceptors/auth.interceptor.ts
```ts
import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptorFn: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  const cloned = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;
  return next(cloned);
};
```

### services/api.service.ts
```ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError, timeout, retry } from 'rxjs';

export interface ApiResponse<T> { data: T; message?: string; }
export interface PatientDto { id: number; firstName: string; lastName: string; email: string; }

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);

  getPatients(limit = 5): Observable<{ users: PatientDto[] }> {
    const params = new HttpParams().set('limit', limit);
    return this.http.get<{ users: PatientDto[] }>('https://dummyjson.com/users', { params }).pipe(
      timeout(3000),
      retry(1),
      catchError(err => this.handle(err))
    );
  }

  createAppointment(payload: any) {
    return this.http.post('https://jsonplaceholder.typicode.com/posts', payload).pipe(
      catchError(err => this.handle(err))
    );
  }

  updateRecord(id: number, payload: any) {
    return this.http.put(`https://jsonplaceholder.typicode.com/posts/${id}`, payload).pipe(
      catchError(err => this.handle(err))
    );
  }

  deleteRecord(id: number) {
    return this.http.delete(`https://jsonplaceholder.typicode.com/posts/${id}`).pipe(
      catchError(err => this.handle(err))
    );
  }

  private handle(err: any) {
    console.error('API error', err);
    return throwError(() => err);
  }
}
```

### app.component.ts
```ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ApiService, PatientDto } from './services/api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './app.component.html',
})
export class AppComponent {
  loading = false;
  error = '';
  patients: PatientDto[] = [];
  message = '';

  constructor(private api: ApiService) {}

  setToken() { localStorage.setItem('token', 'demo-token'); this.message = 'Token set'; }
  clearToken() { localStorage.removeItem('token'); this.message = 'Token cleared'; }

  loadPatients() {
    this.loading = true; this.error = '';
    this.api.getPatients(6).subscribe({
      next: res => { this.patients = res.users; this.loading = false; },
      error: () => { this.error = 'Load failed'; this.loading = false; }
    });
  }

  createAppt() {
    this.api.createAppointment({ title: 'Checkup', patientId: 101, slot: new Date().toISOString() })
      .subscribe({
        next: res => this.message = 'Created (mock id: ' + (res as any).id + ')',
        error: () => this.message = 'Create failed'
      });
  }

  update() {
    this.api.updateRecord(1, { title: 'Updated note' }).subscribe({
      next: res => this.message = 'Updated ' + (res as any).id,
      error: () => this.message = 'Update failed'
    });
  }

  remove() {
    this.api.deleteRecord(1).subscribe({
      next: () => this.message = 'Deleted (mock)',
      error: () => this.message = 'Delete failed'
    });
  }
}
```

### app.component.html
```html
<div class="p-4 space-y-3">
  <div class="flex gap-2">
    <button class="btn w-auto px-3 py-2" (click)="setToken()">Set Token</button>
    <button class="btn w-auto px-3 py-2 bg-slate-500 hover:bg-slate-600" (click)="clearToken()">Clear Token</button>
    <span class="text-xs text-slate-500">{{ message }}</span>
  </div>

  <div class="flex gap-2">
    <button class="btn w-auto px-3 py-2" (click)="loadPatients()" [disabled]="loading">
      <span *ngIf="loading" class="animate-spin h-4 w-4 border-2 border-white border-t-transparent rounded-full inline-block mr-2"></span>
      GET Patients
    </button>
    <button class="btn w-auto px-3 py-2" (click)="createAppt()">POST Appointment</button>
    <button class="btn w-auto px-3 py-2" (click)="update()">PUT Update</button>
    <button class="btn w-auto px-3 py-2 bg-rose-600 hover:bg-rose-700" (click)="remove()">DELETE</button>
  </div>

  <div *ngIf="error" class="text-rose-600 text-sm">Error: {{ error }}</div>

  <ul class="divide-y divide-slate-200">
    <li *ngFor="let p of patients" class="py-2 flex justify-between">
      <span>{{ p.firstName }} {{ p.lastName }}</span>
      <span class="text-xs text-slate-500">{{ p.email }}</span>
    </li>
  </ul>
</div>
```

## রান স্টেপ (VS Code + Chrome)
1) `ng new hms-http --standalone --routing --style=scss` → `cd hms-http`
2) `src/index.html` এ Tailwind CDN: `<script src="https://cdn.tailwindcss.com"></script>`
3) উপরের ফাইলগুলো `src/app/` তে তৈরি করে কোড পেস্ট করুন।
4) `ng serve` → http://localhost:4200
5) টেস্ট চেকলিস্ট:
   - Token set/clear করে Network → Request Headers এ Authorization দেখুন।
   - GET ক্লিক করলে patients তালিকা আপডেট; retry/timeout এর জন্য নেট স্লো করলে দেখুন।
   - POST/PUT/DELETE ক্লিক করে mock response id/মেসেজ দেখুন।
   - error হলে লাল টেক্সট দেখায়।

## দ্রুত সারাংশ: কী তৈরি করবেন, কোথায় পেস্ট করবেন
**প্রজেক্ট বানান**
```bash
ng new hms-http --standalone --routing --style=scss
cd hms-http
```
**Tailwind (দ্রুত)**
`src/index.html` → `<head>` এ `<script src="https://cdn.tailwindcss.com"></script>`

**ফাইল ও লোকেশন (`src/app/`):**
```
app.routes.ts                 // Routes array
app.component.ts              // UI + buttons to call API
app.component.html            // Template
services/api.service.ts       // HttpClient GET/POST/PUT/DELETE + retry/timeout
interceptors/auth.interceptor.ts // JWT header attach
```
সব ফাইলের কোড উপরের সেকশনগুলিতে কপি-পেস্ট করুন।

**চালান ও টেস্ট করুন**
```bash
ng serve
```
- Chrome → http://localhost:4200  
- “Set Token” চাপুন → Network Request Headers এ Authorization দেখুন।  
- GET/POST/PUT/DELETE বোতামগুলো ক্লিক করে UI ও Network ট্যাবে রেসপন্স দেখুন।  
- Token মুছে error হ্যান্ডলিং যাচাই করুন।

## ব্রাউজার কনসোল টেস্টের ৬টি ছোট উদাহরণ (HttpClient ছাড়া fetch/Promise)
```ts
// 1) GET patients (fetch)
fetch('https://dummyjson.com/users?limit=3').then(r=>r.json()).then(d=>console.log(d.users));

// 2) POST appointment (jsonplaceholder)
fetch('https://jsonplaceholder.typicode.com/posts',{
  method:'POST',
  headers:{'Content-Type':'application/json'},
  body:JSON.stringify({title:'Checkup', patientId:101})
}).then(r=>r.json()).then(console.log);

// 3) PUT update
fetch('https://jsonplaceholder.typicode.com/posts/1',{
  method:'PUT',
  headers:{'Content-Type':'application/json'},
  body:JSON.stringify({title:'Updated note'})
}).then(r=>r.json()).then(console.log);

// 4) DELETE
fetch('https://jsonplaceholder.typicode.com/posts/1',{method:'DELETE'}).then(r=>r.status);

// 5) Headers + params (manual)
const params = new URLSearchParams({ limit:'2' });
fetch('https://dummyjson.com/users?'+params,{ headers:{'X-Demo':'true'} }).then(r=>r.json()).then(console.log);

// 6) Retry-like manual (simple)
async function getWithRetry(url, retries=2){
  for(let i=0;i<=retries;i++){
    try { return await fetch(url).then(r=>r.json()); }
    catch(err){ if(i===retries) throw err; }
  }
}
getWithRetry('https://dummyjson.com/users?limit=1').then(console.log);
```

## Interview রিক্যাপ
- Interceptor কেন? → cross-cutting (token, error/logging, retry trigger)
- token attach কিভাবে? → interceptor clone + setHeaders
- Retry/timeout কোথায়? → pipe(timeout, retry)
- Params/headers? → HttpParams, headers বা options-এ সরাসরি object
- Typed response? → `http.get<ApiResponse<User>>()`
