# 10) RxJS basics (switchMap, debounce)

- Observable vs Promise
- Operators: map, filter, tap, switchMap, debounceTime, catchError

**হাসপাতাল সার্চ উদাহরণ**
```ts
search$ = new FormControl('');
results$ = this.search$.valueChanges.pipe(
  debounceTime(300),
  distinctUntilChanged(),
  switchMap(term => this.http.get<any>(`https://dummyjson.com/users/search?q=${term}`)),
  map(res => res.users)
);
```

**Try it**
- `catchError` যোগ করে error হলে খালি array রিটার্ন করুন।
- `finalize` দিয়ে loading false সেট করুন।
