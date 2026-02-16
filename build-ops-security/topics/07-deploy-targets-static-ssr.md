# 07) Deploy targets: static SPA + SSR container

HMS অ্যাপ static (Netlify/Vercel) বা SSR (Angular Universal) দুইভাবেই ডেপ্লয় হতে পারে; কোন ফিচারে কী দরকার জানুন।

## Why this matters (real-world)
- Public booking page SEO চাইলে SSR দরকার।
- Admin dashboard সাধারণত static SPA যথেষ্ট।

## Concepts
### Beginner
- Static SPA deploy (build → dist → upload; SPA fallback)।
### Intermediate
- SSR build (Angular Universal), Node 22 container; Dockerfile basics।
### Advanced
- Healthcheck, log output, memory limit, start command; blue-green/rollback ধারণা।

## Copy-paste Example
```toml
# netlify.toml (SPA fallback)
[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```
```Dockerfile
# Dockerfile.ssr
FROM node:22-slim
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build:ssr
CMD ["node", "dist/server/main.js"]
EXPOSE 4000
```

## Try it
- Beginner: Netlify redirect যোগ করুন; SPA routing কাজ করছে কিনা দেখুন।
- Advanced: Dockerfile.ssr বিল্ড করে container রান করুন; `/health` endpoint expose করুন।

## Common mistakes
- SPA fallback না দিয়ে deep link 404।
- Node version mismatch (16 বনাম 22) SSR ক্র্যাশ।

## Interview points
- Static vs SSR trade-off; SPA fallback; Docker basics।

## Done when…
- Static deploy config নথিভুক্ত।
- SSR Dockerfile আছে (Node 22)।
- Healthcheck/rollback স্ট্রাটেজি উল্লেখ।
