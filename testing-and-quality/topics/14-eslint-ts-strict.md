# 14) Static quality: ESLint + TS strictness

TS strict + ESLint rules না থাকলে runtime বাগ বাড়ে। HMS ফর্মে টাইপ ত্রুটি ধরা যাবে আগেই।

## Why this matters (real-world)
- Null/any থেকে প্রড বাগ এড়ানো।
- CI তে দ্রুত ফিডব্যাক।

## Concepts
### Beginner
- ESLint Angular plugin, template lint।
- strict mode (`strict`, `noImplicitAny`, `strictNullChecks`).
### Intermediate
- lint rules: unused imports, eqeqeq, rxjs/no-ignored-subscription।
- typed forms (`TypedFormGroup`).
### Advanced
- lint-staged + format on commit; custom rule waivers with expiry।

## Copy-paste Example
```json
// .eslintrc.json (excerpt)
{
  "extends": ["plugin:@angular-eslint/recommended"],
  "rules": {
    "@typescript-eslint/no-explicit-any": "error",
    "@angular-eslint/component-class-suffix": "error",
    "rxjs/no-ignored-subscription": "warn"
  }
}
```
```json
// tsconfig.json (excerpt)
{
  "compilerOptions": {
    "strict": true,
    "noImplicitAny": true,
    "strictNullChecks": true
  }
}
```

## Try it
- Beginner: lint চালিয়ে প্রথম error ঠিক করুন।
- Advanced: `no-explicit-any` violation এ small typed alias যোগ করুন; waiver হলে comment এ expiry লিখুন।

## Common mistakes
- strictNullChecks বন্ধ রেখে runtime NPE।
- lint warning উপেক্ষা; CI gate না রাখা।

## Interview points
- Strict flags + ESLint rules prevent runtime bugs; lint-staged mention।

## Done when…
- ESLint চলমান; strict true।
- CI তে lint gate আছে।
- Waiver থাকলে expiry নথিভুক্ত।
