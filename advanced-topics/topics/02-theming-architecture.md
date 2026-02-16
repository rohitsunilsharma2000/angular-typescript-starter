# 02) Theming architecture

লেম্যান-বাংলা: টোকেন আলাদা, থিম = টোকেনের সেট। কম্পোনেন্ট শুধু থিম কনজিউম করবে, কালার হাডকোড নয়।

## Hands-on
1) রান করুন:
   ```bash
   cd advanced-topics/demos/theming-architecture-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে light/dark card ও button style দেখুন।
3) primary/onPrimary বদলে contrast ভাবুন; নতুন high-contrast থিম যোগ করে লগ করুন।

## Snippet
```ts
const light = { bg:'#f8fafc', text:'#0f172a', primary:'#2563eb', onPrimary:'#fff' };
const dark  = { bg:'#0f172a', text:'#e2e8f0', primary:'#60a5fa', onPrimary:'#0b1221' };
```

## Common mistakes
- কম্পোনেন্টে কালার হাডকোড।
- থিম সুইচে state আলাদা রাখা।

## Done when…
- থিম সুইচ করলে স্টাইল বদলায়, কোডে হাডকোডেড কালার নেই।
