# 13) Quality gates checklist

লেম্যান-বাংলা: রিলিজ/PR আগে ছোট ছোট গেট পাস করুন—lint, typecheck, tests, a11y, bundle-size। নিচের CLI ডেমো দিয়ে অনুশীলন করুন।

## Things to learn (beginner → intermediate → advanced)
- Beginner: lint + typecheck + unit tests বাধ্যতামূলক, circular/import boundary ধরুন।
- Intermediate: a11y/contrast checks, bundle-size budget, mock CI matrix (separate steps)।
- Advanced: contract tests, visual diff (Percy), flaky-test quarantine, per-feature quality bar।

## Hands-on (commands + কী দেখবেন)
1) রেডি ডেমো চালান:
   ```bash
   cd architecture-and-state/demos/quality-gates-checklist-demo
   npm install
   npm run demo                 # সব গেট pass
   FAIL=lint,tests npm run demo # ফেল সিমুলেট করুন
   npm run typecheck            # tsc --noEmit
   ```
2) Expected আউটপুট: সবুজ চেক; FAIL সেট করলে সংশ্লিষ্ট গেট ❌ হবে এবং exit code 1।
3) Break/fix: `src/gates.ts` এ নতুন gate (e.g., e2e) যোগ করুন; বা `bundle-size` গেট fail করুন দেখে CI ম্যাট্রিক্স ভাবুন।

## Demos (copy-paste)
`architecture-and-state/demos/quality-gates-checklist-demo/src/` থেকে মূল কোড:
```ts
// gates.ts
export type Gate = { name: string; description: string; run: () => Promise<{ gate: string; status: 'pass'|'fail'; details?: string }> };
const maybeFail = (name: string) => (process.env.FAIL?.split(',').map(s=>s.trim().toLowerCase()) ?? []).includes(name.toLowerCase());
export const gates: Gate[] = [
  { name: 'lint', description: 'ESLint/import-boundary rules', run: async () => ({ gate:'lint', status: maybeFail('lint')?'fail':'pass', details:'mocked ESLint run' }) },
  { name: 'typecheck', description: 'tsc --noEmit', run: async () => ({ gate:'typecheck', status: maybeFail('typecheck')?'fail':'pass', details:'mocked tsc run' }) },
  { name: 'tests', description: 'unit/integration tests', run: async () => ({ gate:'tests', status: maybeFail('tests')?'fail':'pass', details:'mocked test suite' }) },
  { name: 'a11y', description: 'basic accessibility checks (aria/contrast)', run: async () => ({ gate:'a11y', status: maybeFail('a11y')?'fail':'pass', details:'mocked axe run' }) },
  { name: 'bundle-size', description: 'bundle size budget', run: async () => ({ gate:'bundle-size', status: maybeFail('bundle-size')?'fail':'pass', details:'mocked budget check' }) }
];
```
```ts
// main.ts
import { gates } from './gates';
async function run() {
  console.log('Quality gates demo (set FAIL=lint,tests to simulate failures)');
  const results = [] as Awaited<ReturnType<(typeof gates)[number]['run']>>[];
  for (const gate of gates) {
    const res = await gate.run();
    results.push(res);
    const icon = res.status === 'pass' ? '✅' : '❌';
    console.log(`${icon} ${gate.name} — ${gate.description}` + (res.details ? ` (${res.details})` : ''));
  }
  const failed = results.filter(r => r.status === 'fail');
  console.log('\nSummary:', failed.length ? `${failed.length} gate(s) failed` : 'all gates passed');
  if (failed.length) {
    console.log('Failed gates:', failed.map(f => f.gate).join(', '));
    process.exitCode = 1;
  }
}
run();
```

## Ready-to-run demo (repo bundle)
- Path: `architecture-and-state/demos/quality-gates-checklist-demo`
- Commands: উপরে “Hands-on” সেকশনে দেওয়া আছে।
- Test ideas: FAIL env দিয়ে আউটপুট/exit কোড যাচাই; নতুন gate যোগ; gate description আপডেট করুন CI স্টেপের সঙ্গে মিলিয়ে।

## Common mistakes
- সব গেট এক কমান্ডে বেঁধে ফেললে কোনটা ফেল করেছে বোঝা কঠিন; স্টেপ আলাদা রাখুন।
- a11y/ bundle-size উপেক্ষা করা, শুধু lint/test এ থেমে যাওয়া।
- ফেইলিং গেটের exit code 0 রেখে CI সবুজ বানানো।

## Interview points
- “Quality bar” বলতে কোন গেট চালান ও কেন—lint/typecheck/tests/a11y/budget/contract।
- প্রতি PR-এ ছোট, দ্রুত গেট; nightly তে ভারী গেট (visual diff, e2e)।
- Fail fast + clear message + owner signal (which team fixes)।

## Quick practice
- `FAIL=tests npm run demo` দিয়ে fail scenario দেখুন।
- নিজের প্রজেক্টের আসল কমান্ড ম্যাপ করুন: lint → `npm run lint`, typecheck → `npx tsc --noEmit`, a11y → `npm run a11y` ইত্যাদি।
- CI তে parallel matrix ভাবুন (lint/typecheck/tests আলাদা জবে)।
