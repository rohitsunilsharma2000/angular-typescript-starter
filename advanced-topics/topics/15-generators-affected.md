# 15) Generators & affected builds

Repeating patient-table component বানাতে জেনারেটর কাজে লাগে; affected কমান্ড CI তে সময় বাঁচায়।

## Why this matters (real-world)
- Boilerplate কমে; naming/structure স্থির থাকে।
- CI দ্রুত, শুধু পরিবর্তিত অংশ টেস্ট।

## Concepts
### Beginner
- Nx generators বা custom schematics basics।
- Affected graph ধারণা।
### Intermediate
- Custom generator config (component with OnPush + testing harness)।
- Affected lint/build/test in CI।
### Advanced
- Workspace-specific templates; code mods; caching with remote cache।

## Copy-paste Example
```bash
# Nx component generator example
nx g @nrwl/angular:component patient-table --project=ui --change-detection=OnPush --standalone
```
```yaml
# CI excerpt with affected
      - run: npx nx affected --target=lint --base=origin/main --head=HEAD
      - run: npx nx affected --target=test --base=origin/main --head=HEAD
      - run: npx nx affected --target=build --base=origin/main --head=HEAD
```
```ts
// generator defaults (nx.json excerpt)
"generators": {
  "@nrwl/angular:component": {
    "changeDetection": "OnPush",
    "style": "scss",
    "standalone": true
  }
}
```

## Try it
- Beginner: generator দিয়ে নতুন UI কম্পোনেন্ট বানান; OnPush সেট কিনা দেখুন।
- Advanced: affected:test স্টেপে cache-hit দেখুন; remote cache কনফিগার করুন।

## Common mistakes
- Generator default না সেট করে inconsistent কম্পোনেন্ট।
- affected কমান্ডে base/head ভুল → সবকিছু রান।

## Interview points
- Generators = consistency; affected = fast CI; remote cache mention।

## Done when…
- Generator default ডক; কমপক্ষে এক কম্পোনেন্ট তৈরি।
- CI তে affected lint/test/build চালু।
