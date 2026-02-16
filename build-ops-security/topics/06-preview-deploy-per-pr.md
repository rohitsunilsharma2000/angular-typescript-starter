# 06) Preview deploy per PR

PR খুললেই preview URL পাওয়া গেলে QA দ্রুত patients/appointments ফিচার যাচাই করতে পারে।

## Why this matters (real-world)
- QA/PM লেটেন্সি কমে।
- “It works on my machine” কমে।

## Concepts
### Beginner
- Vercel/Netlify preview deploy basics; GitHub Action integration।
### Intermediate
- Env separation: preview uses staging API; secrets scoped।
### Advanced
- Comment bot: preview link পোস্ট; teardown policy; protected branches।

## Copy-paste Example
```yaml
# github-actions-ci.yml excerpt for Vercel
      - name: Deploy Preview
        uses: amondnet/vercel-action@v25
        with:
          vercel-token: ${{ secrets.VERCEL_TOKEN }}
          vercel-org-id: ${{ secrets.VERCEL_ORG_ID }}
          vercel-project-id: ${{ secrets.VERCEL_PROJECT_ID }}
          working-directory: .
          scope: hms
```

## Try it
- Beginner: preview deploy action যোগ করে PR এ URL দেখুন।
- Advanced: preview env এ staging API_BASE সেট করুন; prod secret থেকে আলাদা।

## Common mistakes
- Preview তে prod secret ব্যবহার।
- PR বন্ধ হলে deploy না মুছে resource লিক।

## Interview points
- Preview benefits; env separation; secret scoping।

## Done when…
- PR এ preview URL আসে।
- Preview env staging API ব্যবহার করে।
- Teardown/retention নীতি উল্লেখ।
