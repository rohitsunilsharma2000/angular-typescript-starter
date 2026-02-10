# 12) Guards & Resolvers

- Guard types: `CanActivate`, `CanMatch`, `CanDeactivate`, `Resolve`.
- Use-cases: auth, role-based, unsaved form warning, preload data।

**হাসপাতাল উদাহরণ**
```ts
@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  canActivate() {
    const loggedIn = !!localStorage.getItem('token');
    return loggedIn || inject(Router).createUrlTree(['/login']);
  }
}
```

**Resolver উদাহরণ**
```ts
@Injectable({ providedIn: 'root' })
export class PatientResolver implements Resolve<Observable<any>> {
  constructor(private http: HttpClient) {}
  resolve(route: ActivatedRouteSnapshot) {
    const id = route.paramMap.get('id');
    return this.http.get(`https://dummyjson.com/users/${id}`);
  }
}
```

**Routes with guard+resolver**
```ts
{ path: 'patients/:id',
  loadComponent: () => import('./patient-details.component').then(m => m.PatientDetailsComponent),
  canActivate: [AuthGuard],
  resolve: { patient: PatientResolver }
}
```

**Interview Q**
- CanMatch বনাম CanActivate পার্থক্য?
- Resolver না পেলে কী হয়? (navigation cancel/error)
