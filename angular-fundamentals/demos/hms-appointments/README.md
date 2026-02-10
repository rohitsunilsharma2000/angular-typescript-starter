# HMS Appointments Demo (Angular standalone)

এই ফোল্ডারের ফাইলগুলো যে কোনো Angular 15+ CLI প্রজেক্টের `src/app/` এ কপি করুন। এরপর `ng serve` চালিয়ে ব্রাউজারে দেখুন।

## ফাইল
- `hms-appointments.component.ts`
- `hms-appointments.component.html`
- `hms-appointments.component.scss`
- `app.routes.ts` (উদাহরণ রুট কনফিগ)

## যোগ করার ধাপ
1) নতুন প্রজেক্ট বানান (আগের setup গাইড দেখুন)।
2) এই চারটি ফাইল `src/app/` এ কপি করুন। বিদ্যমান `app.routes.ts` থাকলে content merge করুন।
3) নিশ্চিত করুন `main.ts` এ `provideRouter(routes)` কল আছে (standalone default থাকে)।
4) রান করুন: `ng serve` → http://localhost:4200

## কী আছে
- Standalone component
- Reactive form দিয়ে অ্যাপয়েন্টমেন্ট তৈরি
- HttpClient দিয়ে dummy API কল (`jsonplaceholder` POST, `dummyjson` GET)
- Tailwind-friendly utility class (ইচ্ছা করলে CDN বা build time যোগ করুন)
- Simple login + `authGuard` (token localStorage এ), `/appointments` রুট প্রটেক্টেড

## Tailwind দ্রুত যোগ (দুটি অপশন)
- **CDN (দ্রুত ডেমো):** `src/index.html` এর `<head>` এর মধ্যে যোগ করুন  
  `<script src="https://cdn.tailwindcss.com"></script>`
- **Proper build (recommended):**  
  ```
  npm install -D tailwindcss postcss autoprefixer
  npx tailwindcss init -p
  ```
  `tailwind.config.js` এ `content: ["./src/**/*.{html,ts}"]` সেট করুন।  
  `src/styles.scss` এ যোগ করুন:  
  ```scss
  @tailwind base;
  @tailwind components;
  @tailwind utilities;
  ```
