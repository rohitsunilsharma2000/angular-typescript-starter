# config-runtime demo

দ্রুত runtime config লোডার উদাহরণ।

Steps
1) `runtime-config-loader.ts` কপি করে `src/main.ts` শুরুর দিকে ব্যবহার করুন।
2) `assets/config.json` এ apiBase/featureFlags/sentryDsn লিখুন।
3) Deploy এ প্রতি env অনুযায়ী config.json পরিবেশন করুন (CDN/cache-bust)।

Checks
- Console এ APP_CONFIG ঠিক আছে কিনা।
- Build env মান runtime দ্বারা ওভাররাইড হচ্ছে কিনা।
