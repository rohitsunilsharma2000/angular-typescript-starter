নিচে আমি তোমার ভিডিওর জন্য **একটা “read-while-recording” স্ক্রিপ্ট + পুরো অ্যাপের কোড** দিলাম — একই Classroom অ্যাপটা এবার **Spring Boot backend + Angular (TS) + Tailwind** দিয়ে।

তুমি ভিডিওতে এগুলো **লাইন বাই লাইন পড়বে**, সাথে টাইপ করবে।
আমি স্ক্রিপ্টে তোমার বলা স্টাইলেও রেখেছি: **“এখন আমার backend ta baniye ni”** টাইপ লাইনে।

---

## 0) ভিডিও প্ল্যান (তুমি যা রেকর্ড করবে)

**ভিডিও টাইমলাইন (Beginner-friendly):**

1. Intro + Demo UI (1 min)
2. Backend setup (Spring Boot) + API run (10–12 min)
3. Frontend setup (Angular + Tailwind) (8–10 min)
4. Login + Role dashboard wiring (8–10 min)
5. CRUD + Assign + Timetable validation (12–15 min)
6. Wrap-up (1 min)

---

# PART A — Backend (Spring Boot)

## 1) স্ক্রিপ্ট: Backend শুরু

ভিডিওতে বলো:

> “Assalamu Alaikum / Namaskar.
> আজকে আমরা একটা Classroom dashboard বানাবো — Principal/Teacher/Student role সহ।
> **এখন আমার backend ta baniye ni**, Spring Boot দিয়ে।
> Backend থেকে আমরা login, user/classroom CRUD, assignment, timetable — সব API দিবো।”

---

## 2) Spring Boot Project Create (Step-by-step)

### (A) Spring Initializr

* Project: **Maven**
* Java: **17**
* Dependencies:

  * Spring Web
  * Spring Data JPA
  * Validation
  * H2 Database
  * Lombok

### (B) Run command

```bash
mvn -v
```

---

## 3) Backend folder structure

```
backend/
  src/main/java/com/example/classroom/
    ClassroomApplication.java
    config/
      CorsConfig.java
    controller/
      AuthController.java
      ClassroomController.java
      UserController.java
      TimetableController.java
    dto/
      LoginRequest.java
      LoginResponse.java
      UserCreateRequest.java
      UserResponse.java
      ClassroomCreateRequest.java
      ClassroomResponse.java
      AssignTeacherRequest.java
      AssignStudentRequest.java
      PeriodRequest.java
      PeriodResponse.java
    model/
      Role.java
      User.java
      Classroom.java
      Period.java
    repo/
      UserRepo.java
      ClassroomRepo.java
      PeriodRepo.java
    service/
      AuthService.java
      UserService.java
      ClassroomService.java
      TimetableService.java
    util/
      ApiError.java
```

---

## 4) application.properties

`backend/src/main/resources/application.properties`

```properties
server.port=8080

spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:classroomdb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## 5) Core Models (Entity) — কোড

### Role enum

`model/Role.java`

```java
package com.example.classroom.model;

public enum Role {
  PRINCIPAL, TEACHER, STUDENT
}
```

### User entity

`model/User.java`

```java
package com.example.classroom.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

  @Id
  private String id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  // For teacher/student
  private String classroomId;

  // For student
  private String teacherId;
}
```

### Classroom entity

`model/Classroom.java`

```java
package com.example.classroom.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="classrooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Classroom {
  @Id
  private String id;

  @Column(nullable=false)
  private String name;

  // store as JSON-ish string for simplicity (beginner friendly)
  // Example: Mon=12:00-18:00;Tue=12:00-18:00
  @Column(nullable=false, length=1000)
  private String dayWindows;

  // teacher assigned
  private String teacherId;
}
```

### Period (Timetable entry)

`model/Period.java`

```java
package com.example.classroom.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="periods")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Period {
  @Id
  private String id;

  @Column(nullable=false)
  private String classroomId;

  @Column(nullable=false)
  private String day;        // Mon/Tue...

  @Column(nullable=false)
  private String subject;

  @Column(nullable=false)
  private String startTime;  // HH:mm

  @Column(nullable=false)
  private String endTime;    // HH:mm
}
```

---

## 6) Repositories

`repo/UserRepo.java`

```java
package com.example.classroom.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.classroom.model.User;

public interface UserRepo extends JpaRepository<User, String> {
  Optional<User> findByEmail(String email);
}
```

`repo/ClassroomRepo.java`

```java
package com.example.classroom.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.classroom.model.Classroom;

public interface ClassroomRepo extends JpaRepository<Classroom, String> {}
```

`repo/PeriodRepo.java`

```java
package com.example.classroom.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.classroom.model.Period;

public interface PeriodRepo extends JpaRepository<Period, String> {
  List<Period> findByClassroomId(String classroomId);
  List<Period> findByClassroomIdAndDay(String classroomId, String day);
}
```

---

## 7) Dummy Auth (token in memory) — Beginner friendly

### DTO

`dto/LoginRequest.java`

```java
package com.example.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
  @NotBlank private String email;
  @NotBlank private String password;
}
```

`dto/LoginResponse.java`

```java
package com.example.classroom.dto;

import lombok.*;

@Data @AllArgsConstructor
public class LoginResponse {
  private String token;
  private String userId;
  private String role;
  private String name;
}
```

### AuthService

`service/AuthService.java`

```java
package com.example.classroom.service;

import java.util.*;
import org.springframework.stereotype.Service;
import com.example.classroom.dto.LoginResponse;
import com.example.classroom.model.User;
import com.example.classroom.repo.UserRepo;

@Service
public class AuthService {

  private final UserRepo userRepo;

  // token -> userId
  private final Map<String, String> sessions = new HashMap<>();

  public AuthService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  public LoginResponse login(String email, String password) {
    User u = userRepo.findByEmail(email)
        .filter(x -> x.getPassword().equals(password))
        .orElseThrow(() -> new IllegalArgumentException("Invalid email/password"));

    String token = UUID.randomUUID().toString();
    sessions.put(token, u.getId());

    return new LoginResponse(token, u.getId(), u.getRole().name(), u.getName());
  }

  public User requireUser(String token) {
    if (token == null || token.isBlank()) throw new IllegalArgumentException("Missing token");
    String userId = sessions.get(token);
    if (userId == null) throw new IllegalArgumentException("Invalid token");
    return userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
  }

  public void logout(String token) {
    if (token != null) sessions.remove(token);
  }
}
```

### AuthController

`controller/AuthController.java`

```java
package com.example.classroom.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.classroom.dto.*;
import com.example.classroom.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
    return ResponseEntity.ok(authService.login(req.getEmail(), req.getPassword()));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestHeader("X-Auth-Token") String token) {
    authService.logout(token);
    return ResponseEntity.noContent().build();
  }
}
```

---

## 8) Seed Dummy Data (Principal/Teacher/Student)

`ClassroomApplication.java`

```java
package com.example.classroom;

import java.util.UUID;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.classroom.model.*;
import com.example.classroom.repo.*;

@SpringBootApplication
public class ClassroomApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClassroomApplication.class, args);
  }

  @Bean
  CommandLineRunner seed(UserRepo userRepo, ClassroomRepo classroomRepo, PeriodRepo periodRepo) {
    return args -> {
      // principal
      userRepo.save(User.builder()
          .id("p1").role(Role.PRINCIPAL).name("Principal")
          .email("principal@classroom.com").password("Admin")
          .build());

      // classroom c1
      classroomRepo.save(Classroom.builder()
          .id("c1")
          .name("Classroom 1")
          .dayWindows("Mon=12:00-18:00;Tue=12:00-18:00;Wed=12:00-18:00;Thu=12:00-18:00;Fri=12:00-18:00;Sat=12:00-16:00")
          .teacherId("t1")
          .build());

      // teachers
      userRepo.save(User.builder().id("t1").role(Role.TEACHER).name("Teacher One")
          .email("t1@classroom.com").password("t1").classroomId("c1").build());
      userRepo.save(User.builder().id("t2").role(Role.TEACHER).name("Teacher Two")
          .email("t2@classroom.com").password("t2").classroomId(null).build());

      // students
      userRepo.save(User.builder().id("s1").role(Role.STUDENT).name("Student A")
          .email("s1@classroom.com").password("s1").classroomId("c1").teacherId("t1").build());
      userRepo.save(User.builder().id("s2").role(Role.STUDENT).name("Student B")
          .email("s2@classroom.com").password("s2").classroomId("c1").teacherId("t1").build());
      userRepo.save(User.builder().id("s3").role(Role.STUDENT).name("Student C")
          .email("s3@classroom.com").password("s3").classroomId(null).teacherId(null).build());

      // timetable
      periodRepo.save(Period.builder().id("tt1").classroomId("c1").day("Mon").subject("Math").startTime("12:30").endTime("13:30").build());
      periodRepo.save(Period.builder().id("tt2").classroomId("c1").day("Mon").subject("English").startTime("14:00").endTime("15:00").build());
    };
  }
}
```

---

## 9) Core APIs (Users/Classrooms/Assignments/Timetable)

### User DTOs

`dto/UserCreateRequest.java`

```java
package com.example.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateRequest {
  @NotNull private String role; // PRINCIPAL/TEACHER/STUDENT
  @NotBlank private String name;
  @NotBlank private String email;
  @NotBlank private String password;
}
```

`dto/UserResponse.java`

```java
package com.example.classroom.dto;

import lombok.*;

@Data @AllArgsConstructor
public class UserResponse {
  private String id;
  private String role;
  private String name;
  private String email;
  private String classroomId;
  private String teacherId;
}
```

### UserController

`controller/UserController.java`

```java
package com.example.classroom.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.classroom.dto.*;
import com.example.classroom.model.*;
import com.example.classroom.repo.UserRepo;
import com.example.classroom.service.AuthService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

  private final UserRepo userRepo;
  private final AuthService auth;

  public UserController(UserRepo userRepo, AuthService auth) {
    this.userRepo = userRepo;
    this.auth = auth;
  }

  @GetMapping
  public List<UserResponse> list(@RequestHeader("X-Auth-Token") String token) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) throw new IllegalArgumentException("Principal only");

    return userRepo.findAll().stream().map(this::toRes).toList();
  }

  @PostMapping
  public UserResponse create(@RequestHeader("X-Auth-Token") String token,
                             @Valid @RequestBody UserCreateRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) throw new IllegalArgumentException("Principal only");

    if (userRepo.findByEmail(req.getEmail()).isPresent()) throw new IllegalArgumentException("Email exists");

    User u = User.builder()
        .id(req.getRole().equals("TEACHER") ? "t" + System.nanoTime() : "s" + System.nanoTime())
        .role(Role.valueOf(req.getRole()))
        .name(req.getName())
        .email(req.getEmail())
        .password(req.getPassword())
        .classroomId(null)
        .teacherId(null)
        .build();

    userRepo.save(u);
    return toRes(u);
  }

  @PutMapping("/{id}")
  public UserResponse update(@RequestHeader("X-Auth-Token") String token,
                             @PathVariable String id,
                             @Valid @RequestBody UserCreateRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) throw new IllegalArgumentException("Principal only");

    User u = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

    u.setName(req.getName());
    u.setEmail(req.getEmail());
    u.setPassword(req.getPassword());
    userRepo.save(u);

    return toRes(u);
  }

  @DeleteMapping("/{id}")
  public void delete(@RequestHeader("X-Auth-Token") String token, @PathVariable String id) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) throw new IllegalArgumentException("Principal only");
    if (id.equals(me.getId())) throw new IllegalArgumentException("Cannot delete self");

    userRepo.deleteById(id);
  }

  private UserResponse toRes(User u) {
    return new UserResponse(u.getId(), u.getRole().name(), u.getName(), u.getEmail(), u.getClassroomId(), u.getTeacherId());
  }
}
```

> (ClassroomController + Assign + TimetableController কোডটা Frontend অংশে ব্যবহার করার জন্য দরকার — কিন্তু এখানে এক রিপ্লাইতে খুব বড় হয়ে যাবে।
> তুমি চাইলে আমি পরের রিপ্লাইতে **Backend remaining controllers (classroom + assignment + timetable overlap validation)** পুরোটা দিয়ে দেবো।)

তবে ভিডিওর জন্য এখনই Frontend শুরু করতে পারো — কারণ login + list users দিয়েই UI দাঁড়িয়ে যাবে।

---

# PART B — Frontend (Angular + Tailwind)

## 1) স্ক্রিপ্ট: Frontend শুরু

ভিডিওতে বলো:

> “Backend basic ready.
> **এখন আমার frontend ta baniye ni** Angular + Tailwind দিয়ে।
> আমাদের UI হবে role-based dashboard: Principal/Teacher/Student.”

---

## 2) Angular project create

```bash
npm i -g @angular/cli
ng new classroom-ui --routing --style=css
cd classroom-ui
```

---

## 3) Tailwind setup

```bash
npm i -D tailwindcss postcss autoprefixer
npx tailwindcss init
```

`tailwind.config.js`

```js
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: { extend: {} },
  plugins: [],
}
```

`src/styles.css`

```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

---

## 4) Pages + Services generate

```bash
ng g c pages/login
ng g c pages/dashboard
ng g s core/api
ng g s core/auth
ng g guard core/auth
```

---

## 5) Angular Routing

`src/app/app-routing.module.ts`

```ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AuthGuard } from './core/auth.guard';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
```

---

## 6) AuthService (token store)

`src/app/core/auth.service.ts`

```ts
import { Injectable } from '@angular/core';

export type Role = 'PRINCIPAL' | 'TEACHER' | 'STUDENT';

export interface Session {
  token: string;
  userId: string;
  role: Role;
  name: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private key = 'classroom_session';

  get session(): Session | null {
    const raw = localStorage.getItem(this.key);
    return raw ? JSON.parse(raw) : null;
  }

  set session(val: Session | null) {
    if (!val) localStorage.removeItem(this.key);
    else localStorage.setItem(this.key, JSON.stringify(val));
  }

  isLoggedIn(): boolean {
    return !!this.session?.token;
  }

  logout() {
    this.session = null;
  }
}
```

---

## 7) API Service (Spring Boot call)

`src/app/core/api.service.ts`

```ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient, private auth: AuthService) {}

  private headers(): HttpHeaders {
    const token = this.auth.session?.token || '';
    return new HttpHeaders({ 'X-Auth-Token': token });
  }

  post<T>(path: string, body: any): Observable<T> {
    return this.http.post<T>(this.baseUrl + path, body, { headers: this.headers() });
  }

  get<T>(path: string): Observable<T> {
    return this.http.get<T>(this.baseUrl + path, { headers: this.headers() });
  }
}
```

Add HttpClient in `app.module.ts`:

```ts
import { HttpClientModule } from '@angular/common/http';
...
imports: [BrowserModule, AppRoutingModule, HttpClientModule]
```

---

## 8) AuthGuard

`src/app/core/auth.guard.ts`

```ts
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}
  canActivate(): boolean {
    if (this.auth.isLoggedIn()) return true;
    this.router.navigate(['/login']);
    return false;
  }
}
```

---

## 9) Login Page UI + Logic (Tailwind)

`src/app/pages/login/login.component.ts`

```ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../core/api.service';
import { AuthService, Session } from '../../core/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  email = 'principal@classroom.com';
  password = 'Admin';
  err = '';

  constructor(private api: ApiService, private auth: AuthService, private router: Router) {}

  quick(email: string, pass: string) {
    this.email = email; this.password = pass;
  }

  login() {
    this.err = '';
    this.api.post<Session>('/auth/login', { email: this.email, password: this.password })
      .subscribe({
        next: (res) => { this.auth.session = res; this.router.navigate(['/dashboard']); },
        error: (e) => { this.err = e?.error?.message || 'Login failed'; }
      });
  }
}
```

`src/app/pages/login/login.component.html`

```html
<div class="min-h-[calc(100vh-64px)] grid place-items-center p-4">
  <div class="w-full max-w-xl bg-white border rounded-2xl shadow-sm p-5">
    <h1 class="text-xl font-semibold">Login</h1>
    <p class="text-sm text-slate-600 mt-1">Dummy login: Principal / Teacher / Student</p>

    <div class="mt-4 space-y-3">
      <div>
        <label class="text-sm text-slate-600">Email</label>
        <input class="mt-1 w-full px-3 py-2 rounded-lg border"
               [(ngModel)]="email" placeholder="principal@classroom.com">
      </div>

      <div>
        <label class="text-sm text-slate-600">Password</label>
        <input class="mt-1 w-full px-3 py-2 rounded-lg border"
               [(ngModel)]="password" type="password" placeholder="Admin">
      </div>

      <button class="w-full px-4 py-2.5 rounded-xl bg-slate-900 text-white hover:bg-slate-800"
              (click)="login()">Sign in</button>

      <div class="text-sm text-red-600" *ngIf="err">{{err}}</div>

      <div class="rounded-xl border bg-slate-50 p-3 text-sm">
        <div class="font-semibold mb-2">Quick logins</div>
        <div class="grid md:grid-cols-3 gap-2">
          <button class="px-3 py-2 rounded-lg bg-white border hover:bg-slate-100 text-left"
                  (click)="quick('principal@classroom.com','Admin')">
            <div class="font-medium">Principal</div><div class="text-xs text-slate-500">principal@classroom.com</div>
          </button>
          <button class="px-3 py-2 rounded-lg bg-white border hover:bg-slate-100 text-left"
                  (click)="quick('t1@classroom.com','t1')">
            <div class="font-medium">Teacher</div><div class="text-xs text-slate-500">t1@classroom.com</div>
          </button>
          <button class="px-3 py-2 rounded-lg bg-white border hover:bg-slate-100 text-left"
                  (click)="quick('s1@classroom.com','s1')">
            <div class="font-medium">Student</div><div class="text-xs text-slate-500">s1@classroom.com</div>
          </button>
        </div>
      </div>
    </div>
  </div>
</div>
```

> **Important:** `ngModel` ব্যবহার করলে `FormsModule` লাগবে
> `app.module.ts` এ add করো:

```ts
import { FormsModule } from '@angular/forms';
...
imports: [BrowserModule, AppRoutingModule, HttpClientModule, FormsModule]
```

---

## 10) Dashboard — Role based UI skeleton

`dashboard.component.ts`

```ts
import { Component } from '@angular/core';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {
  tab: 'users'|'classrooms'|'assign'|'timetable'|'classmates' = 'users';
  constructor(public auth: AuthService) {}
}
```

`dashboard.component.html`

```html
<div class="p-4 max-w-7xl mx-auto">
  <div class="flex flex-col md:flex-row md:items-end md:justify-between gap-3">
    <div>
      <h2 class="text-2xl font-semibold">{{auth.session?.role}} Dashboard</h2>
      <p class="text-sm text-slate-600">Role scoped view</p>
    </div>
    <div class="flex gap-2 flex-wrap">
      <button *ngIf="auth.session?.role==='PRINCIPAL'" (click)="tab='users'"
        class="px-3 py-2 rounded-xl border hover:bg-slate-100 text-sm">Users</button>
      <button *ngIf="auth.session?.role==='PRINCIPAL'" (click)="tab='classrooms'"
        class="px-3 py-2 rounded-xl border hover:bg-slate-100 text-sm">Classrooms</button>
      <button *ngIf="auth.session?.role==='PRINCIPAL'" (click)="tab='assign'"
        class="px-3 py-2 rounded-xl border hover:bg-slate-100 text-sm">Assignments</button>

      <button (click)="tab='timetable'"
        class="px-3 py-2 rounded-xl border hover:bg-slate-100 text-sm">Timetable</button>

      <button *ngIf="auth.session?.role!=='PRINCIPAL'" (click)="tab='classmates'"
        class="px-3 py-2 rounded-xl border hover:bg-slate-100 text-sm">Classmates</button>
    </div>
  </div>

  <div class="mt-5 bg-white border rounded-2xl shadow-sm p-5">
    <div *ngIf="tab==='users'">Users panel এখানে বসবে</div>
    <div *ngIf="tab==='classrooms'">Classrooms panel এখানে বসবে</div>
    <div *ngIf="tab==='assign'">Assignments panel এখানে বসবে</div>
    <div *ngIf="tab==='timetable'">Timetable panel এখানে বসবে</div>
    <div *ngIf="tab==='classmates'">Classmates panel এখানে বসবে</div>
  </div>
</div>
```

---

# ভিডিও স্ক্রিপ্ট কিউ (তুমি পড়বে)

এগুলো পড়ে তুমি একদম smooth ভিডিও করতে পারবে:

### Intro

> “আজকে আমরা full-stack একটা mini system বানাবো।
> Principal user create করবে, classroom বানাবে, teacher assign করবে, student assign করবে।
> Teacher timetable বানাবে এবং student সেটা দেখবে।”

### Backend cue lines

> “এখন আমি Spring Boot project create করলাম।”
> “এখন entity বানাচ্ছি — User, Classroom, Period।”
> “এখন dummy login করছি token-based, beginner-friendly।”
> “এখন H2 তে seed data দিয়ে দিলাম।”

### Frontend cue lines

> “এখন আমার frontend ta baniye ni Angular দিয়ে।”
> “এখন Tailwind setup করলাম।”
> “এখন login page বানাচ্ছি।”
> “এখন dashboard e role based tabs show করবো।”

---

ঠিক আছে — নিচে আমি **বাকি সবকিছু** (Backend remaining controllers + validation + Angular panels) **copy-paste ready** দিলাম, সাথে **ভিডিও স্ক্রিপ্ট লাইনে লাইনে** (যেটা তুমি রেকর্ডে পড়ে টাইপ করবে)।

> ✅ লক্ষ্য: **একটা কাজ করা Full-Stack App**
> Principal: Users/Classrooms CRUD + Assignments
> Teacher: Classmates + Timetable CRUD (validation: day window + overlap)
> Student: Classmates + Timetable view

---

# 🎬 ভিডিও স্ক্রিপ্ট (এইটা পড়ে রেকর্ড করবে)

## Intro (20–30s)

> “আজকে আমরা Spring Boot + Angular + Tailwind দিয়ে একটা Classroom Dashboard বানাবো।
> Principal/Teacher/Student role থাকবে।
> **এখন আমার backend ta baniye ni**, তাই backend API গুলো complete করি।”

## Backend build cues

> “এখন আমি ClassroomController বানাচ্ছি।”
> “এখন Assignments endpoints বানাচ্ছি — teacher classroom এ assign, student teacher এ assign।”
> “এখন Timetable add/edit করবো, overlap validation সহ।”
> “এখন GlobalExceptionHandler দিয়ে error response clean করবো।”

## Frontend build cues

> “**এখন আমার frontend ta baniye ni**, Angular + Tailwind দিয়ে।”
> “এখন Dashboard tabs এর ভিতরে real panels বসাবো।”
> “এখন Users CRUD connect করলাম।”
> “এখন Timetable add দিলে overlap হলে error দেখাবো।”

---

# ✅ PART A — Backend: Remaining Complete Code (Spring Boot)

## 1) CORS Config

`backend/src/main/java/com/example/classroom/config/CorsConfig.java`

```java
package com.example.classroom.config;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig {
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
          .allowedOrigins("http://localhost:4200")
          .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
          .allowedHeaders("*");
      }
    };
  }
}
```

---

## 2) Global Error Response (Beginner-friendly)

`backend/src/main/java/com/example/classroom/util/ApiError.java`

```java
package com.example.classroom.util;

import lombok.*;

@Data @AllArgsConstructor
public class ApiError {
  private String message;
}
```

`backend/src/main/java/com/example/classroom/util/GlobalExceptionHandler.java`

```java
package com.example.classroom.util;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleBad(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(new ApiError(ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleAny(Exception ex) {
    // production এ log করো
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new ApiError("Server error"));
  }
}
```

---

## 3) DTOs for Classroom + Assignment + Period

`dto/ClassroomCreateRequest.java`

```java
package com.example.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClassroomCreateRequest {
  @NotBlank private String name;

  // Example: Mon=12:00-18:00;Tue=12:00-18:00;Sat=12:00-16:00
  @NotBlank private String dayWindows;
}
```

`dto/ClassroomResponse.java`

```java
package com.example.classroom.dto;

import lombok.*;

@Data @AllArgsConstructor
public class ClassroomResponse {
  private String id;
  private String name;
  private String dayWindows;
  private String teacherId;
}
```

`dto/AssignTeacherRequest.java`

```java
package com.example.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignTeacherRequest {
  @NotBlank private String teacherId;
  @NotBlank private String classroomId;
}
```

`dto/AssignStudentRequest.java`

```java
package com.example.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignStudentRequest {
  @NotBlank private String studentId;
  @NotBlank private String teacherId;
}
```

`dto/PeriodRequest.java`

```java
package com.example.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PeriodRequest {
  @NotBlank private String classroomId;
  @NotBlank private String day;       // Mon/Tue...
  @NotBlank private String subject;
  @NotBlank private String startTime; // HH:mm
  @NotBlank private String endTime;   // HH:mm
}
```

`dto/PeriodResponse.java`

```java
package com.example.classroom.dto;

import lombok.*;

@Data @AllArgsConstructor
public class PeriodResponse {
  private String id;
  private String classroomId;
  private String day;
  private String subject;
  private String startTime;
  private String endTime;
}
```

---

## 4) ClassroomService

`service/ClassroomService.java`

```java
package com.example.classroom.service;

import java.util.*;
import org.springframework.stereotype.Service;
import com.example.classroom.model.Classroom;
import com.example.classroom.repo.ClassroomRepo;

@Service
public class ClassroomService {

  private final ClassroomRepo classroomRepo;

  public ClassroomService(ClassroomRepo classroomRepo) {
    this.classroomRepo = classroomRepo;
  }

  public List<Classroom> list() { return classroomRepo.findAll(); }

  public Classroom get(String id) {
    return classroomRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Classroom not found"));
  }

  public Classroom create(String name, String dayWindows) {
    validateDayWindows(dayWindows);
    Classroom c = Classroom.builder()
      .id("c" + System.nanoTime())
      .name(name)
      .dayWindows(dayWindows)
      .teacherId(null)
      .build();
    return classroomRepo.save(c);
  }

  public Classroom update(String id, String name, String dayWindows) {
    validateDayWindows(dayWindows);
    Classroom c = get(id);
    c.setName(name);
    c.setDayWindows(dayWindows);
    return classroomRepo.save(c);
  }

  public void delete(String id) {
    classroomRepo.deleteById(id);
  }

  // very simple validation
  private void validateDayWindows(String dayWindows) {
    // must contain at least one day=HH:mm-HH:mm
    if (dayWindows == null || dayWindows.isBlank()) throw new IllegalArgumentException("dayWindows required");
    String[] parts = dayWindows.split(";");
    boolean any = false;
    for (String p : parts) {
      p = p.trim();
      if (p.isEmpty()) continue;
      if (!p.contains("=") || !p.contains("-")) throw new IllegalArgumentException("Invalid dayWindows format");
      String[] a = p.split("=");
      String[] b = a[1].split("-");
      if (b.length != 2) throw new IllegalArgumentException("Invalid dayWindows time range");
      // basic HH:mm check
      if (!b[0].matches("^([01]\\d|2[0-3]):[0-5]\\d$")) throw new IllegalArgumentException("Invalid time: " + b[0]);
      if (!b[1].matches("^([01]\\d|2[0-3]):[0-5]\\d$")) throw new IllegalArgumentException("Invalid time: " + b[1]);
      any = true;
    }
    if (!any) throw new IllegalArgumentException("At least one day window required");
  }
}
```

---

## 5) TimetableService (Window + Overlap Validation)

`service/TimetableService.java`

```java
package com.example.classroom.service;

import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;

import com.example.classroom.model.*;
import com.example.classroom.repo.*;

@Service
public class TimetableService {

  private final ClassroomRepo classroomRepo;
  private final PeriodRepo periodRepo;

  public TimetableService(ClassroomRepo classroomRepo, PeriodRepo periodRepo) {
    this.classroomRepo = classroomRepo;
    this.periodRepo = periodRepo;
  }

  public List<Period> listByClassroom(String classroomId) {
    return periodRepo.findByClassroomId(classroomId);
  }

  public Period add(Period p) {
    Classroom c = classroomRepo.findById(p.getClassroomId())
      .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));

    validatePeriod(c, null, p.getDay(), p.getStartTime(), p.getEndTime());
    p.setId("tt" + System.nanoTime());
    return periodRepo.save(p);
  }

  public Period update(String periodId, Period newP) {
    Period existing = periodRepo.findById(periodId)
      .orElseThrow(() -> new IllegalArgumentException("Period not found"));

    Classroom c = classroomRepo.findById(existing.getClassroomId())
      .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));

    // prevent moving to other classroom in beginner version
    if (!existing.getClassroomId().equals(newP.getClassroomId()))
      throw new IllegalArgumentException("Cannot change classroomId");

    validatePeriod(c, periodId, newP.getDay(), newP.getStartTime(), newP.getEndTime());

    existing.setDay(newP.getDay());
    existing.setSubject(newP.getSubject());
    existing.setStartTime(newP.getStartTime());
    existing.setEndTime(newP.getEndTime());

    return periodRepo.save(existing);
  }

  public void delete(String periodId) {
    periodRepo.deleteById(periodId);
  }

  private void validatePeriod(Classroom c, String editingId, String day, String start, String end) {
    // HH:mm parse
    LocalTime s = parse(start);
    LocalTime e = parse(end);
    if (!s.isBefore(e)) throw new IllegalArgumentException("Start must be before end");

    // day window from dayWindows string
    Map<String, String[]> windows = parseWindows(c.getDayWindows());
    String[] w = windows.get(day);
    if (w == null) throw new IllegalArgumentException("This day is not enabled in classroom");

    LocalTime ws = parse(w[0]);
    LocalTime we = parse(w[1]);
    if (s.isBefore(ws) || e.isAfter(we))
      throw new IllegalArgumentException("Must fit within " + day + " window " + w[0] + "-" + w[1]);

    // overlap check
    List<Period> sameDay = periodRepo.findByClassroomIdAndDay(c.getId(), day);
    for (Period x : sameDay) {
      if (editingId != null && x.getId().equals(editingId)) continue;
      LocalTime xs = parse(x.getStartTime());
      LocalTime xe = parse(x.getEndTime());
      if (overlaps(s, e, xs, xe)) {
        throw new IllegalArgumentException("Overlaps with " + x.getSubject() + " (" + x.getStartTime() + "-" + x.getEndTime() + ")");
      }
    }
  }

  private boolean overlaps(LocalTime aS, LocalTime aE, LocalTime bS, LocalTime bE) {
    return aS.isBefore(bE) && bS.isBefore(aE);
  }

  private LocalTime parse(String hhmm) {
    try { return LocalTime.parse(hhmm); }
    catch (Exception ex) { throw new IllegalArgumentException("Invalid time: " + hhmm); }
  }

  private Map<String, String[]> parseWindows(String dw) {
    Map<String, String[]> map = new HashMap<>();
    for (String p : dw.split(";")) {
      p = p.trim();
      if (p.isEmpty()) continue;
      String[] a = p.split("=");
      String day = a[0].trim();
      String[] b = a[1].trim().split("-");
      map.put(day, new String[]{b[0], b[1]});
    }
    return map;
  }
}
```

---

## 6) Controllers: Classroom + Assignment + Timetable

### ClassroomController

`controller/ClassroomController.java`

```java
package com.example.classroom.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.classroom.dto.*;
import com.example.classroom.model.*;
import com.example.classroom.repo.UserRepo;
import com.example.classroom.service.*;

@RestController
@RequestMapping("/api/classrooms")
@CrossOrigin
public class ClassroomController {

  private final AuthService auth;
  private final ClassroomService classroomService;
  private final UserRepo userRepo;

  public ClassroomController(AuthService auth, ClassroomService classroomService, UserRepo userRepo) {
    this.auth = auth;
    this.classroomService = classroomService;
    this.userRepo = userRepo;
  }

  @GetMapping
  public List<ClassroomResponse> list(@RequestHeader("X-Auth-Token") String token) {
    auth.requireUser(token);
    return classroomService.list().stream()
      .map(c -> new ClassroomResponse(c.getId(), c.getName(), c.getDayWindows(), c.getTeacherId()))
      .toList();
  }

  @PostMapping
  public ClassroomResponse create(@RequestHeader("X-Auth-Token") String token,
                                 @Valid @RequestBody ClassroomCreateRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) throw new IllegalArgumentException("Principal only");

    Classroom c = classroomService.create(req.getName(), req.getDayWindows());
    return new ClassroomResponse(c.getId(), c.getName(), c.getDayWindows(), c.getTeacherId());
  }

  @PutMapping("/{id}")
  public ClassroomResponse update(@RequestHeader("X-Auth-Token") String token,
                                 @PathVariable String id,
                                 @Valid @RequestBody ClassroomCreateRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) throw new IllegalArgumentException("Principal only");

    Classroom c = classroomService.update(id, req.getName(), req.getDayWindows());
    return new ClassroomResponse(c.getId(), c.getName(), c.getDayWindows(), c.getTeacherId());
  }

  @DeleteMapping("/{id}")
  public void delete(@RequestHeader("X-Auth-Token") String token, @PathVariable String id) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) throw new IllegalArgumentException("Principal only");

    // detach relations
    userRepo.findAll().forEach(u -> {
      if (id.equals(u.getClassroomId())) u.setClassroomId(null);
      userRepo.save(u);
    });

    classroomService.delete(id);
  }
}
```

### AssignmentController

`controller/AssignmentController.java`

```java
package com.example.classroom.controller;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.classroom.dto.*;
import com.example.classroom.model.*;
import com.example.classroom.repo.*;
import com.example.classroom.service.AuthService;

@RestController
@RequestMapping("/api/assign")
@CrossOrigin
public class AssignmentController {

  private final AuthService auth;
  private final UserRepo userRepo;
  private final ClassroomRepo classroomRepo;

  public AssignmentController(AuthService auth, UserRepo userRepo, ClassroomRepo classroomRepo) {
    this.auth = auth;
    this.userRepo = userRepo;
    this.classroomRepo = classroomRepo;
  }

  // teacher -> classroom (one classroom per teacher)
  @PostMapping("/teacher-classroom")
  public void assignTeacher(@RequestHeader("X-Auth-Token") String token,
                            @Valid @RequestBody AssignTeacherRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) throw new IllegalArgumentException("Principal only");

    User t = userRepo.findById(req.getTeacherId())
      .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
    if (t.getRole() != Role.TEACHER) throw new IllegalArgumentException("Not a teacher");

    Classroom c = classroomRepo.findById(req.getClassroomId())
      .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));

    // detach old classroom of teacher
    if (t.getClassroomId() != null && !t.getClassroomId().equals(c.getId())) {
      classroomRepo.findById(t.getClassroomId()).ifPresent(old -> {
        if (req.getTeacherId().equals(old.getTeacherId())) {
          old.setTeacherId(null);
          classroomRepo.save(old);
        }
      });
    }

    // detach old teacher of classroom
    if (c.getTeacherId() != null && !c.getTeacherId().equals(t.getId())) {
      userRepo.findById(c.getTeacherId()).ifPresent(oldT -> {
        oldT.setClassroomId(null);
        userRepo.save(oldT);
      });
    }

    // assign
    t.setClassroomId(c.getId());
    c.setTeacherId(t.getId());
    userRepo.save(t);
    classroomRepo.save(c);

    // move students of that teacher into this classroom (dummy)
    userRepo.findAll().forEach(s -> {
      if (s.getRole() == Role.STUDENT && t.getId().equals(s.getTeacherId())) {
        s.setClassroomId(c.getId());
        userRepo.save(s);
      }
    });
  }

  // student -> teacher (inherits classroom)
  @PostMapping("/student-teacher")
  public void assignStudent(@RequestHeader("X-Auth-Token") String token,
                            @Valid @RequestBody AssignStudentRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() != Role.PRINCIPAL) throw new IllegalArgumentException("Principal only");

    User s = userRepo.findById(req.getStudentId())
      .orElseThrow(() -> new IllegalArgumentException("Student not found"));
    if (s.getRole() != Role.STUDENT) throw new IllegalArgumentException("Not a student");

    User t = userRepo.findById(req.getTeacherId())
      .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
    if (t.getRole() != Role.TEACHER) throw new IllegalArgumentException("Not a teacher");

    s.setTeacherId(t.getId());
    s.setClassroomId(t.getClassroomId()); // inherit
    userRepo.save(s);
  }
}
```

### TimetableController

`controller/TimetableController.java`

```java
package com.example.classroom.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.classroom.dto.*;
import com.example.classroom.model.*;
import com.example.classroom.repo.UserRepo;
import com.example.classroom.service.*;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin
public class TimetableController {

  private final AuthService auth;
  private final TimetableService timetable;
  private final UserRepo userRepo;

  public TimetableController(AuthService auth, TimetableService timetable, UserRepo userRepo) {
    this.auth = auth;
    this.timetable = timetable;
    this.userRepo = userRepo;
  }

  @GetMapping("/{classroomId}")
  public List<PeriodResponse> list(@RequestHeader("X-Auth-Token") String token, @PathVariable String classroomId) {
    User me = auth.requireUser(token);

    // access rule (simple):
    // Principal: any
    // Teacher: only own classroom
    // Student: only own classroom
    if (me.getRole() == Role.TEACHER || me.getRole() == Role.STUDENT) {
      if (me.getClassroomId() == null || !me.getClassroomId().equals(classroomId))
        throw new IllegalArgumentException("Not allowed");
    }

    return timetable.listByClassroom(classroomId)
      .stream().map(p -> new PeriodResponse(p.getId(), p.getClassroomId(), p.getDay(), p.getSubject(), p.getStartTime(), p.getEndTime()))
      .toList();
  }

  @PostMapping
  public PeriodResponse add(@RequestHeader("X-Auth-Token") String token, @Valid @RequestBody PeriodRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() == Role.STUDENT) throw new IllegalArgumentException("Student cannot edit timetable");

    // teacher can only add for own classroom
    if (me.getRole() == Role.TEACHER && (me.getClassroomId() == null || !me.getClassroomId().equals(req.getClassroomId())))
      throw new IllegalArgumentException("Teacher can edit only own classroom");

    Period p = Period.builder()
      .classroomId(req.getClassroomId())
      .day(req.getDay())
      .subject(req.getSubject())
      .startTime(req.getStartTime())
      .endTime(req.getEndTime())
      .build();

    Period saved = timetable.add(p);
    return new PeriodResponse(saved.getId(), saved.getClassroomId(), saved.getDay(), saved.getSubject(), saved.getStartTime(), saved.getEndTime());
  }

  @PutMapping("/{periodId}")
  public PeriodResponse update(@RequestHeader("X-Auth-Token") String token, @PathVariable String periodId,
                               @Valid @RequestBody PeriodRequest req) {
    User me = auth.requireUser(token);
    if (me.getRole() == Role.STUDENT) throw new IllegalArgumentException("Student cannot edit timetable");

    if (me.getRole() == Role.TEACHER && (me.getClassroomId() == null || !me.getClassroomId().equals(req.getClassroomId())))
      throw new IllegalArgumentException("Teacher can edit only own classroom");

    Period p = Period.builder()
      .classroomId(req.getClassroomId())
      .day(req.getDay())
      .subject(req.getSubject())
      .startTime(req.getStartTime())
      .endTime(req.getEndTime())
      .build();

    Period saved = timetable.update(periodId, p);
    return new PeriodResponse(saved.getId(), saved.getClassroomId(), saved.getDay(), saved.getSubject(), saved.getStartTime(), saved.getEndTime());
  }

  @DeleteMapping("/{periodId}")
  public void delete(@RequestHeader("X-Auth-Token") String token, @PathVariable String periodId) {
    User me = auth.requireUser(token);
    if (me.getRole() == Role.STUDENT) throw new IllegalArgumentException("Student cannot edit timetable");
    timetable.delete(periodId);
  }

  // classmates API (teacher/student view)
  @GetMapping("/classmates")
  public List<UserResponse> classmates(@RequestHeader("X-Auth-Token") String token) {
    User me = auth.requireUser(token);
    if (me.getRole() == Role.PRINCIPAL) throw new IllegalArgumentException("Principal does not have classmates view");

    if (me.getClassroomId() == null) throw new IllegalArgumentException("No classroom assigned");

    return userRepo.findAll().stream()
      .filter(u -> u.getRole() == Role.STUDENT && me.getClassroomId().equals(u.getClassroomId()))
      .map(u -> new UserResponse(u.getId(), u.getRole().name(), u.getName(), u.getEmail(), u.getClassroomId(), u.getTeacherId()))
      .toList();
  }
}
```

---

## 7) Backend Run

ভিডিওতে বলো:

> “এখন আমি backend run করছি। H2 in-memory DB auto seed হবে।”

Run:

```bash
cd backend
mvn spring-boot:run
```

Test login:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"principal@classroom.com","password":"Admin"}'
```

---

# ✅ PART B — Frontend: Angular Panels (Complete Working)

## 1) Update ApiService: add put/delete

`src/app/core/api.service.ts`

```ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  baseUrl = 'http://localhost:8080/api';
  constructor(private http: HttpClient, private auth: AuthService) {}

  private headers(): HttpHeaders {
    const token = this.auth.session?.token || '';
    return new HttpHeaders({ 'X-Auth-Token': token });
  }

  get<T>(path: string): Observable<T> {
    return this.http.get<T>(this.baseUrl + path, { headers: this.headers() });
  }
  post<T>(path: string, body: any): Observable<T> {
    return this.http.post<T>(this.baseUrl + path, body, { headers: this.headers() });
  }
  put<T>(path: string, body: any): Observable<T> {
    return this.http.put<T>(this.baseUrl + path, body, { headers: this.headers() });
  }
  del<T>(path: string): Observable<T> {
    return this.http.delete<T>(this.baseUrl + path, { headers: this.headers() });
  }
}
```

---

## 2) Models

`src/app/core/models.ts`

```ts
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
  dayWindows: string; // "Mon=12:00-18:00;Tue=..."
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
```

---

## 3) Dashboard: panels with real API

**ভিডিও cue:**

> “এখন dashboard কে API-driven করছি — users, classrooms, assign, timetable, classmates সব আসবে।”

`dashboard.component.ts`

```ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/auth.service';
import { ApiService } from '../../core/api.service';
import { Classroom, Period, User } from '../../core/models';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  tab: 'users'|'classrooms'|'assign'|'timetable'|'classmates' = 'users';

  users: User[] = [];
  classrooms: Classroom[] = [];
  classmates: User[] = [];
  periods: Period[] = [];

  // selections
  selectedClassroomId: string | null = null;

  // forms
  newUser: any = { role:'TEACHER', name:'', email:'', password:'' };
  newClassroom: any = { name:'', dayWindows:'Mon=12:00-18:00;Tue=12:00-18:00;Sat=12:00-16:00' };
  assignTeacher: any = { teacherId:'', classroomId:'' };
  assignStudent: any = { studentId:'', teacherId:'' };
  newPeriod: any = { classroomId:'', day:'Mon', subject:'', startTime:'12:00', endTime:'13:00' };

  toast = '';

  constructor(public auth: AuthService, private api: ApiService) {}

  ngOnInit() {
    // default tab by role
    if (this.auth.session?.role === 'PRINCIPAL') this.tab = 'users';
    else if (this.auth.session?.role === 'TEACHER') this.tab = 'classmates';
    else this.tab = 'timetable';

    this.refreshAll();
  }

  show(msg: string) {
    this.toast = msg;
    setTimeout(()=> this.toast = '', 2500);
  }

  refreshAll() {
    // everyone can fetch classrooms list
    this.api.get<Classroom[]>('/classrooms').subscribe({
      next: (cs) => {
        this.classrooms = cs;
        const role = this.auth.session?.role;
        const meClassroomId = (this.auth.session as any)?.classroomId; // session doesn't include classroomId in our simple login
        // selection fallback
        if (!this.selectedClassroomId) this.selectedClassroomId = cs[0]?.id || null;

        // for timetable: pick by role
        if (role === 'PRINCIPAL') {
          this.loadPeriods(this.selectedClassroomId);
        } else {
          // for teacher/student we need their classroomId from /users list OR classmates endpoint error tells no classroom
          // simplest: load classmates (this implies classroomId)
          this.loadClassmatesAndInferPeriods();
        }
      }
    });

    // principal-only users
    if (this.auth.session?.role === 'PRINCIPAL') {
      this.api.get<User[]>('/users').subscribe({
        next: (u) => {
          this.users = u;
          // init assignment dropdown defaults
          const t = u.find(x=>x.role==='TEACHER');
          const s = u.find(x=>x.role==='STUDENT');
          if (t) this.assignTeacher.teacherId = t.id;
          if (s) this.assignStudent.studentId = s.id;
          if (t) this.assignStudent.teacherId = t.id;
          if (this.classrooms[0]) this.assignTeacher.classroomId = this.classrooms[0].id;
        }
      });
    }
  }

  // -------- Users (Principal) ----------
  createUser() {
    this.api.post<User>('/users', this.newUser).subscribe({
      next: () => { this.show('User created'); this.newUser = { role:'TEACHER', name:'', email:'', password:'' }; this.refreshAll(); },
      error: (e) => this.show(e?.error?.message || 'Create failed')
    });
  }

  deleteUser(id: string) {
    this.api.del<void>('/users/'+id).subscribe({
      next: () => { this.show('User deleted'); this.refreshAll(); },
      error: (e) => this.show(e?.error?.message || 'Delete failed')
    });
  }

  // -------- Classrooms (Principal) ----------
  createClassroom() {
    this.api.post<Classroom>('/classrooms', this.newClassroom).subscribe({
      next: () => { this.show('Classroom created'); this.refreshAll(); },
      error: (e) => this.show(e?.error?.message || 'Create failed')
    });
  }

  deleteClassroom(id: string) {
    this.api.del<void>('/classrooms/'+id).subscribe({
      next: () => { this.show('Classroom deleted'); this.refreshAll(); },
      error: (e) => this.show(e?.error?.message || 'Delete failed')
    });
  }

  // -------- Assignments (Principal) ----------
  assignTeacherToClassroom() {
    this.api.post<void>('/assign/teacher-classroom', this.assignTeacher).subscribe({
      next: () => { this.show('Teacher assigned'); this.refreshAll(); },
      error: (e) => this.show(e?.error?.message || 'Assign failed')
    });
  }

  assignStudentToTeacher() {
    this.api.post<void>('/assign/student-teacher', this.assignStudent).subscribe({
      next: () => { this.show('Student assigned'); this.refreshAll(); },
      error: (e) => this.show(e?.error?.message || 'Assign failed')
    });
  }

  // -------- Timetable ----------
  loadPeriods(classroomId: string | null) {
    if (!classroomId) return;
    this.api.get<Period[]>('/timetable/'+classroomId).subscribe({
      next: (p) => {
        this.periods = p;
        this.newPeriod.classroomId = classroomId;
      },
      error: (e) => this.show(e?.error?.message || 'Load timetable failed')
    });
  }

  addPeriod() {
    this.api.post<Period>('/timetable', this.newPeriod).subscribe({
      next: () => { this.show('Period added'); this.loadPeriods(this.newPeriod.classroomId); },
      error: (e) => this.show(e?.error?.message || 'Add failed')
    });
  }

  deletePeriod(id: string) {
    this.api.del<void>('/timetable/'+id).subscribe({
      next: () => { this.show('Period deleted'); this.loadPeriods(this.newPeriod.classroomId); },
      error: (e) => this.show(e?.error?.message || 'Delete failed')
    });
  }

  // -------- Classmates (Teacher/Student) ----------
  loadClassmatesAndInferPeriods() {
    this.api.get<User[]>('/timetable/classmates').subscribe({
      next: (list) => {
        this.classmates = list;
        // infer classroomId from any classmate
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
```

---

## 4) Dashboard HTML (Complete UI with Tailwind)

`dashboard.component.html`

```html
<div class="max-w-7xl mx-auto p-4">
  <div class="flex items-center justify-between">
    <div>
      <h2 class="text-2xl font-semibold">{{auth.session?.role}} Dashboard</h2>
      <p class="text-sm text-slate-600">Role based dashboard</p>
    </div>
    <button class="px-3 py-2 rounded-xl border hover:bg-slate-100 text-sm" (click)="logout()">Logout</button>
  </div>

  <div *ngIf="toast" class="mt-3 p-3 rounded-xl border bg-white text-sm">{{toast}}</div>

  <div class="mt-4 flex gap-2 flex-wrap">
    <button *ngIf="auth.session?.role==='PRINCIPAL'" (click)="tab='users'"
      class="px-3 py-2 rounded-xl border hover:bg-slate-100 text-sm">Users</button>

    <button *ngIf="auth.session?.role==='PRINCIPAL'" (click)="tab='classrooms'"
      class="px-3 py-2 rounded-xl border hover:bg-slate-100 text-sm">Classrooms</button>

    <button *ngIf="auth.session?.role==='PRINCIPAL'" (click)="tab='assign'"
      class="px-3 py-2 rounded-xl border hover:bg-slate-100 text-sm">Assignments</button>

    <button (click)="tab='timetable'"
      class="px-3 py-2 rounded-xl border hover:bg-slate-100 text-sm">Timetable</button>

    <button *ngIf="auth.session?.role!=='PRINCIPAL'" (click)="tab='classmates'"
      class="px-3 py-2 rounded-xl border hover:bg-slate-100 text-sm">Classmates</button>
  </div>

  <!-- USERS -->
  <div *ngIf="tab==='users' && auth.session?.role==='PRINCIPAL'" class="mt-4 bg-white border rounded-2xl p-5">
    <h3 class="text-lg font-semibold">Users</h3>

    <div class="mt-3 grid md:grid-cols-4 gap-2">
      <select class="px-3 py-2 rounded-lg border" [(ngModel)]="newUser.role">
        <option value="TEACHER">TEACHER</option>
        <option value="STUDENT">STUDENT</option>
      </select>
      <input class="px-3 py-2 rounded-lg border" placeholder="Name" [(ngModel)]="newUser.name">
      <input class="px-3 py-2 rounded-lg border" placeholder="Email" [(ngModel)]="newUser.email">
      <input class="px-3 py-2 rounded-lg border" placeholder="Password" [(ngModel)]="newUser.password">
    </div>
    <button class="mt-3 px-4 py-2 rounded-xl bg-slate-900 text-white" (click)="createUser()">Create</button>

    <div class="mt-4 rounded-xl border overflow-auto">
      <table class="w-full text-sm">
        <thead class="bg-slate-50">
          <tr class="text-left text-slate-600">
            <th class="px-3 py-2">Role</th><th class="px-3 py-2">Name</th><th class="px-3 py-2">Email</th>
            <th class="px-3 py-2">Classroom</th><th class="px-3 py-2">Teacher</th><th class="px-3 py-2">Action</th>
          </tr>
        </thead>
        <tbody class="divide-y">
          <tr *ngFor="let u of users">
            <td class="px-3 py-2">{{u.role}}</td>
            <td class="px-3 py-2 font-medium">{{u.name}}</td>
            <td class="px-3 py-2">{{u.email}}</td>
            <td class="px-3 py-2">{{u.classroomId || '-'}}</td>
            <td class="px-3 py-2">{{u.teacherId || '-'}}</td>
            <td class="px-3 py-2">
              <button class="px-2 py-1 rounded-lg border hover:bg-slate-100 text-xs"
                (click)="deleteUser(u.id)" [disabled]="u.role==='PRINCIPAL'">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- CLASSROOMS -->
  <div *ngIf="tab==='classrooms' && auth.session?.role==='PRINCIPAL'" class="mt-4 bg-white border rounded-2xl p-5">
    <h3 class="text-lg font-semibold">Classrooms</h3>

    <div class="mt-3 grid md:grid-cols-3 gap-2">
      <input class="px-3 py-2 rounded-lg border" placeholder="Classroom name" [(ngModel)]="newClassroom.name">
      <input class="px-3 py-2 rounded-lg border md:col-span-2" placeholder="dayWindows" [(ngModel)]="newClassroom.dayWindows">
    </div>
    <button class="mt-3 px-4 py-2 rounded-xl bg-slate-900 text-white" (click)="createClassroom()">Create</button>

    <div class="mt-4 grid md:grid-cols-2 gap-3">
      <div *ngFor="let c of classrooms" class="p-4 rounded-2xl border">
        <div class="flex items-start justify-between">
          <div>
            <div class="font-semibold">{{c.name}}</div>
            <div class="text-xs text-slate-600 mt-1">Teacher: {{c.teacherId || '-'}}</div>
            <div class="text-xs text-slate-500 mt-2">Windows: {{c.dayWindows}}</div>
          </div>
          <button class="px-2 py-1 rounded-lg border hover:bg-slate-100 text-xs"
            (click)="deleteClassroom(c.id)">Delete</button>
        </div>
      </div>
    </div>
  </div>

  <!-- ASSIGNMENTS -->
  <div *ngIf="tab==='assign' && auth.session?.role==='PRINCIPAL'" class="mt-4 bg-white border rounded-2xl p-5">
    <h3 class="text-lg font-semibold">Assignments</h3>

    <div class="mt-3 grid md:grid-cols-3 gap-2 items-end">
      <div>
        <label class="text-xs text-slate-600">Teacher</label>
        <select class="w-full px-3 py-2 rounded-lg border" [(ngModel)]="assignTeacher.teacherId">
          <option *ngFor="let u of users" [value]="u.id" *ngIf="u.role==='TEACHER'">{{u.name}}</option>
        </select>
      </div>
      <div>
        <label class="text-xs text-slate-600">Classroom</label>
        <select class="w-full px-3 py-2 rounded-lg border" [(ngModel)]="assignTeacher.classroomId">
          <option *ngFor="let c of classrooms" [value]="c.id">{{c.name}}</option>
        </select>
      </div>
      <button class="px-4 py-2 rounded-xl bg-slate-900 text-white" (click)="assignTeacherToClassroom()">Assign</button>
    </div>

    <div class="mt-6 grid md:grid-cols-3 gap-2 items-end">
      <div>
        <label class="text-xs text-slate-600">Student</label>
        <select class="w-full px-3 py-2 rounded-lg border" [(ngModel)]="assignStudent.studentId">
          <option *ngFor="let u of users" [value]="u.id" *ngIf="u.role==='STUDENT'">{{u.name}}</option>
        </select>
      </div>
      <div>
        <label class="text-xs text-slate-600">Teacher</label>
        <select class="w-full px-3 py-2 rounded-lg border" [(ngModel)]="assignStudent.teacherId">
          <option *ngFor="let u of users" [value]="u.id" *ngIf="u.role==='TEACHER'">{{u.name}}</option>
        </select>
      </div>
      <button class="px-4 py-2 rounded-xl bg-slate-900 text-white" (click)="assignStudentToTeacher()">Assign</button>
    </div>
  </div>

  <!-- TIMETABLE -->
  <div *ngIf="tab==='timetable'" class="mt-4 bg-white border rounded-2xl p-5">
    <div class="flex items-start justify-between gap-3 flex-col md:flex-row">
      <div>
        <h3 class="text-lg font-semibold">Timetable</h3>
        <p class="text-sm text-slate-600">Overlap + Window validation backend এ আছে</p>
      </div>

      <div *ngIf="auth.session?.role==='PRINCIPAL'" class="w-full md:w-72">
        <label class="text-xs text-slate-600">Classroom</label>
        <select class="w-full px-3 py-2 rounded-lg border"
          [(ngModel)]="selectedClassroomId" (change)="loadPeriods(selectedClassroomId)">
          <option *ngFor="let c of classrooms" [value]="c.id">{{c.name}}</option>
        </select>
      </div>
    </div>

    <!-- Add period for Principal/Teacher -->
    <div *ngIf="auth.session?.role!=='STUDENT'" class="mt-4 grid md:grid-cols-6 gap-2 items-end">
      <div class="md:col-span-2">
        <label class="text-xs text-slate-600">Subject</label>
        <input class="w-full px-3 py-2 rounded-lg border" [(ngModel)]="newPeriod.subject">
      </div>
      <div>
        <label class="text-xs text-slate-600">Day</label>
        <input class="w-full px-3 py-2 rounded-lg border" [(ngModel)]="newPeriod.day" placeholder="Mon">
      </div>
      <div>
        <label class="text-xs text-slate-600">Start</label>
        <input class="w-full px-3 py-2 rounded-lg border" [(ngModel)]="newPeriod.startTime" placeholder="12:00">
      </div>
      <div>
        <label class="text-xs text-slate-600">End</label>
        <input class="w-full px-3 py-2 rounded-lg border" [(ngModel)]="newPeriod.endTime" placeholder="13:00">
      </div>
      <button class="px-4 py-2 rounded-xl bg-slate-900 text-white" (click)="addPeriod()">Add</button>
    </div>

    <div class="mt-4 rounded-xl border overflow-auto">
      <table class="w-full text-sm">
        <thead class="bg-slate-50">
          <tr class="text-left text-slate-600">
            <th class="px-3 py-2">Day</th><th class="px-3 py-2">Subject</th><th class="px-3 py-2">Time</th>
            <th class="px-3 py-2" *ngIf="auth.session?.role!=='STUDENT'">Action</th>
          </tr>
        </thead>
        <tbody class="divide-y">
          <tr *ngFor="let p of periods">
            <td class="px-3 py-2">{{p.day}}</td>
            <td class="px-3 py-2 font-medium">{{p.subject}}</td>
            <td class="px-3 py-2">{{p.startTime}} - {{p.endTime}}</td>
            <td class="px-3 py-2" *ngIf="auth.session?.role!=='STUDENT'">
              <button class="px-2 py-1 rounded-lg border hover:bg-slate-100 text-xs"
                (click)="deletePeriod(p.id)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- CLASSMATES -->
  <div *ngIf="tab==='classmates' && auth.session?.role!=='PRINCIPAL'" class="mt-4 bg-white border rounded-2xl p-5">
    <h3 class="text-lg font-semibold">Classmates</h3>
    <p class="text-sm text-slate-600">Backend থেকে /timetable/classmates</p>

    <button class="mt-3 px-4 py-2 rounded-xl border hover:bg-slate-100 text-sm" (click)="loadClassmatesAndInferPeriods()">
      Refresh
    </button>

    <div class="mt-4 rounded-xl border overflow-auto">
      <table class="w-full text-sm">
        <thead class="bg-slate-50">
          <tr class="text-left text-slate-600">
            <th class="px-3 py-2">Name</th><th class="px-3 py-2">Email</th><th class="px-3 py-2">Teacher</th>
          </tr>
        </thead>
        <tbody class="divide-y">
          <tr *ngFor="let s of classmates">
            <td class="px-3 py-2 font-medium">{{s.name}}</td>
            <td class="px-3 py-2">{{s.email}}</td>
            <td class="px-3 py-2">{{s.teacherId || '-'}}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>
```

---

## 5) Run Frontend

```bash
cd classroom-ui
ng serve -o
```

Open: `http://localhost:4200`

---

# ✅ Bengali Subtitles: Beginner Setup (YouTube)

## Option A: Auto-caption + Correct (সবচেয়ে সহজ)

ভিডিওতে বলো:

> “আমি Bengali voiceover দিলাম, YouTube auto-caption generate করবে।”

Steps:

1. YouTube Studio → Content → Video → Subtitles
2. “Auto-sync” / “Automatic captions” দেখবে
3. Bengali হলে language Bengali select করো (যদি prompt দেয়)
4. Edit করে ভুলগুলো ঠিক করো
5. Publish subtitles

## Option B: Fast SRT (Script → SRT) সহজ টেকনিক

ভিডিও script যেটা তুমি পড়লে — সেটাই কপি করে
**Subtitle edit screen** এ paste করে timing adjust করো।

---

# ✅ তোমার ভিডিওতে যে ১০টা লাইন “must say”

1. “এখন আমার backend ta baniye ni”
2. “এখন H2 database use করলাম, seed data দিয়ে দিলাম”
3. “এখন login endpoint test করলাম curl দিয়ে”
4. “এখন Principal only routes protect করলাম”
5. “এখন assignment rules implement করলাম”
6. “এখন timetable overlap validation backend এ দিলাম”
7. “এখন Angular + Tailwind setup করলাম”
8. “এখন login UI connect করলাম backend এর সাথে”
9. “এখন timetable add করলে overlap হলে error দেখাবে”
10. “এখন Student শুধু view করতে পারবে edit না”

---

## যদি কোথাও error আসে (সবচেয়ে common)

* Frontend CORS error → `CorsConfig` ঠিক আছে কিনা দেখো
* Angular ngModel error → `FormsModule` import আছে কিনা
* Backend port mismatch → ApiService baseUrl ঠিক করো (`8080`)

---

# ✅ UI Tester Checklist (বাংলা)

এই অংশটা UI tester এর জন্য — step-by-step কীভাবে app test করবে:

## 1) App run করো

### Backend

```bash
cd portfolio/class-room/backend
mvn spring-boot:run
```

### Frontend

```bash
cd portfolio/class-room/frontend
npm install
npm start
```

Open করো: `http://localhost:4200`

---

## 2) Test Accounts (Quick Login)

* Principal: `principal@classroom.com` / `Admin`
* Teacher: `t1@classroom.com` / `t1`
* Student: `s1@classroom.com` / `s1`

---

## 3) Mandatory UI Test Cases

1. **Principal login**

   * Dashboard open হবে
   * Tabs দেখাবে: Users, Classrooms, Assignments, Timetable

2. **Users create/delete**

   * নতুন Teacher/Student create করা যায় কিনা
   * List এ show করে কিনা

3. **Classroom create/delete**

   * Classroom create হয় কিনা
   * dayWindows invalid দিলে backend error toast আসে কিনা

4. **Assignment flow**

   * Teacher → Classroom assign হয় কিনা
   * Student → Teacher assign হলে student classroom auto update হয় কিনা

5. **Timetable add (valid)**

   * Valid time range দিলে period add হয় কিনা

6. **Timetable add (invalid overlap)**

   * Existing period এর সাথে overlap দিলে error message আসে কিনা

7. **Teacher role restriction**

   * Teacher principal-only tabs পায় না
   * নিজের classroom timetable/classmates দেখতে পারে

8. **Student role restriction**

   * Student timetable/classmates দেখতে পারে
   * Student timetable add/delete করতে পারে না

9. **Logout + Guard**

   * Logout এর পর `/dashboard` এ গেলে `/login` এ redirect হয় কিনা

---

## 4) Bug Report Format (Tester friendly)

Bug report করার সময় এই format follow করো:

* **Title:** সংক্ষিপ্ত issue নাম
* **Role:** Principal/Teacher/Student
* **Steps to Reproduce:** 1,2,3...
* **Expected Result:** কী হওয়ার কথা ছিল
* **Actual Result:** আসলে কী হয়েছে
* **Screenshot/Video:** থাকলে attach করো

---
 
