# 09) HttpClient + Interceptor (JWT, error)

- HttpClient observable রিটার্ন করে; টাইপ যোগ করুন `http.get<ApiResponse<Patient[]>>()`.
- Interceptor: request/response pipeline; JWT attach, global error/logging।

**JWT attach interceptor**
```ts
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = localStorage.getItem('token');
    const authReq = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;
    return next.handle(authReq).pipe(
      catchError(err => {
        console.error('API error', err);
        return throwError(() => err);
      })
    );
  }
}
```

**Provider (standalone bootstrap)**
```ts
bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(withInterceptors([authInterceptorFn]))
  ]
});
```

**Fake hospital API**
- GET patients: `https://dummyjson.com/users?limit=5`
- POST appointment: `https://jsonplaceholder.typicode.com/posts`

**Interview Q**
- Interceptor order? (provider array order)
- clone vs mutate request difference?
