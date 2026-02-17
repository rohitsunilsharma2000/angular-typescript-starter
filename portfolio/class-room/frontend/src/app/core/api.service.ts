import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AuthService } from './auth.service';

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

  post<T>(path: string, body: unknown): Observable<T> {
    return this.http.post<T>(this.baseUrl + path, body, { headers: this.headers() });
  }

  put<T>(path: string, body: unknown): Observable<T> {
    return this.http.put<T>(this.baseUrl + path, body, { headers: this.headers() });
  }

  del<T>(path: string): Observable<T> {
    return this.http.delete<T>(this.baseUrl + path, { headers: this.headers() });
  }
}
