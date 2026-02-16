# 12) Dependency auditing & lockfile discipline

HMS অ্যাপে লাইব্রেরি আপডেট না করলে vuln থেকে ঝুঁকি বাড়ে; audit ও lockfile ঠিক রাখুন।

## Why this matters (real-world)
- পুরনো vuln প্যাকেজে ডেটা লিক হতে পারে।
- Reproducible build দরকার; lockfile drift হলে bug।

## Concepts
### Beginner
- `npm audit` / `npm audit fix --force` সতর্কতা।
- lockfile commit; no manual edits।
### Intermediate
- `npx depcheck` unused deps ধরা।
- Severity policy (high/critical only)।
### Advanced
- CI step: audit/depcheck; allowlist with expiry; renovate/depbot কনফিগ।

## Copy-paste Example
```bash
npm audit --omit=dev
npx depcheck
```
```yaml
# github-actions-ci.yml excerpt
      - run: npm audit --omit=dev --audit-level=high
      - run: npx depcheck
```
```md
Policy:
- High/Critical vuln fix before release
- Moderate logged; patch in next sprint
- Lockfile must be committed; no `npm install --no-save`
```

## Try it
- Beginner: depcheck চালিয়ে unused dependency নোট করুন।
- Advanced: audit fail করলে PR ব্লক করার CI rule যোগ করুন।

## Common mistakes
- audit ignore ফাইল বানিয়ে ভুলে যাওয়া।
- lockfile ম্যানুয়ালি সম্পাদনা।

## Interview points
- audit level policy; depcheck; lockfile discipline।

## Done when…
- Audit/depcheck কমান্ড CI-তে আছে।
- Policy নথিভুক্ত; lockfile কমিটেড।
