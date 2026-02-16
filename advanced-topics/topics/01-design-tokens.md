# 01) Design tokens

লেম্যান-বাংলা: কালার/স্পেসিং/রেডিয়াসকে টোকেন করে রাখুন, কম্পোনেন্ট সেগুলো খাবে। হাডকোডেড কালার নয়।

## Hands-on (step-by-step)
1) ডেমো চালান:
   ```bash
   cd advanced-topics/demos/design-tokens-demo
   npm install
   npm run demo
   npm run typecheck
   ```
2) আউটপুটে টোকেন JSON এবং button/card style দেখুন।
3) primary কালার বা spacing ফ্যাক্টর বদলে আবার চালিয়ে effect দেখুন।

## Copy snippet (demo থেকে)
```ts
const tokens = { colors: { primary: '#2563eb', onPrimary: '#ffffff', surface: '#f8fafc', text: '#0f172a' }, radius: '8px', spacing: (n:number)=>`${n*4}px` };
const button = `background:${tokens.colors.primary};color:${tokens.colors.onPrimary};padding:${tokens.spacing(2)} ${tokens.spacing(3)};border-radius:${tokens.radius}`;
```

## Common mistakes
- হাডকোডেড কালার/স্পেসিং।
- টোকেন বদলালে কম্পোনেন্ট আপডেট না হওয়া (single source রাখুন)।

## Interview points
- Tokens → theme → component pipeline ব্যাখ্যা করুন; কেন হাডকোডেড CSS খারাপ।

## Done when…
- টোকেন JSON আছে; কম্পোনেন্টে টোকেন ব্যবহার; primary বদলালে UI রঙ বদলায়।
