# VS Code + Angular CLI সেটআপ (বিগিনার স্টেপ-বাই-স্টেপ)

1) Node.js 18+ ইনস্টল করুন.
2) Angular CLI ইনস্টল (গ্লোবাল)
```bash
npm install -g @angular/cli
```
3) নতুন প্রজেক্ট তৈরি (standalone, routing on, strict on)
```bash
ng new hms-demo --routing --style=scss --standalone
cd hms-demo
```
4) VS Code-এ ফোল্ডার খুলুন
```bash
code .
```
5) ডেভ সার্ভার চালান
```bash
ng serve
```
- ডিফল্ট: `http://localhost:4200`

6) এই রিপোর ডেমো ফাইল কপি করুন
- `angular-fundamentals/demos/hms-appointments/*` → আপনার `src/app/` ফোল্ডারে পেস্ট করুন।
- `app.routes.ts` খুলে নীচের রুট যোগ করুন (যদি না থাকে)।

7) Live reload যাচাই
- `src/app/hms-appointments.component.ts` এ টেক্সট বদলান; ব্রাউজার auto-reload হয়ে যাবে।

### দ্রুত রান (StackBlitz বিকল্প)
- `https://stackblitz.com/fork/angular` খুলে `hms-appointments` ফাইলগুলো আপলোড/পেস্ট করুন। কোনো ইনস্টলেশন লাগবে না।

### Debugging টিপস
- VS Code Run & Debug এ Chrome/Edge profile চালান; `sourceMap` ডিফল্টে true থাকে।
- Network ট্যাবে API কল (jsonplaceholder/dummyjson) রেসপন্স চেক করুন।
