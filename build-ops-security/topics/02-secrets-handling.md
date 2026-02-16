# 02) Secrets handling (GitHub Actions/Vercel/Netlify)

হাসপাতাল API কী বা Sentry auth টোকেন কখনো ফ্রন্টএন্ড রিপোতে কমিট করা যাবে না। env var দিয়ে ইনজেক্ট করুন।

## Why this matters (real-world)
- ফাঁস হলে PHI লিক ঝুঁকি; কমপ্লায়েন্স ভঙ্গ।
- Rotation সহজ রাখতে env ভেরিয়েবল দরকার।

## Concepts
### Beginner
- GitHub Actions secrets, Vercel/Netlify env var সেট।
- .env.local ignore করা।
### Intermediate
- Secret scope: build বনাম runtime; preview vs prod আলাদা মান।
- Do/Don’t তালিকা (secret কখনো bundle হয় না)।
### Advanced
- Secret rotation policy; audit logs; CI masking; dotenv-flow for local।

## Copy-paste Example
```bash
# .gitignore excerpt
.env
.env.local
```
```yaml
# .github/workflows/github-actions-ci.yml (env usage excerpt)
      - name: Build
        run: npm run build
        env:
          API_BASE: ${{ secrets.API_BASE }}
          SENTRY_DSN: ${{ secrets.SENTRY_DSN }}
```
```bash
# Vercel env set
vercel env add API_BASE
vercel env add SENTRY_DSN
```
```md
Do:
- API base, feature flags non-secret => runtime config
- Secret tokens => env vars in CI/deploy target
Don’t:
- Commit .env
- Put secrets in assets/config.json
```

## Try it
- Beginner: .env.local ignore করুন; dummy secret Git-এ কমিট করার চেষ্টা করে দেখুন pre-commit hook আটকায় কিনা।
- Advanced: Secret rotation চেকলিস্ট লিখুন (owner, rotation frequency, revoke process)।

## Common mistakes
- build-time replace করে secret bundle করা।
- Preview env এ prod secret ব্যবহার।

## Interview points
- Secrets never in bundle; CI env + deploy provider env; rotation policy।

## Done when…
- Secrets policy ডক; .env ignore; CI env reference সেট।
- Preview/prod env আলাদা মান নথিভুক্ত।
