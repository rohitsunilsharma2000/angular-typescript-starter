# 14) Monorepo (Nx/Turbo) basics

একাধিক অ্যাপ (admin/patient-portal) ও লাইব (ui/state/api) থাকলে monorepo productivity কাজে আসে।

## Why this matters (real-world)
- কোডশেয়ার (UI, state) সহজ; boundary guard দিয়ে স্থায়ী আর্কিটেকচার।
- Affected builds/tests দিয়ে CI দ্রুত।

## Concepts
### Beginner
- Nx/Turbo overview; apps/ libs separation।
- Path alias, tsconfig base।
### Intermediate
- Implicit/explicit dep graph; lint boundary rules।
- Affected commands: `nx affected:test --base=origin/main --head=HEAD`।
### Advanced
- Remote cache, distributed tasks; generators for scaffolding।

## Copy-paste Example
```txt
apps/
  admin/
  patient-portal/
libs/
  ui/
  state/
  api/
```
```json
// tsconfig.base.json (paths excerpt)
{
  "compilerOptions": {
    "paths": {
      "@hms/ui": ["libs/ui/src/index.ts"],
      "@hms/state": ["libs/state/src/index.ts"],
      "@hms/api": ["libs/api/src/index.ts"]
    }
  }
}
```
```bash
# Nx init
npx create-nx-workspace@latest hms-monorepo --preset=apps
# Affected tests
nx affected:test --base=origin/main --head=HEAD
```

## Try it
- Beginner: path alias দিয়ে shared UI import করুন।
- Advanced: affected:test CI স্টেপ যোগ করুন এবং boundary lint rule চালান।

## Common mistakes
- libs থেকে apps import করা (উল্টো দিক) → circular risk।
- tsconfig paths sync না রাখা।

## Interview points
- apps/libs separation; path alias; affected builds; remote cache।

## Done when…
- Monorepo layout ডক; path aliases সেট।
- Affected build/test কমান্ড জানা।
