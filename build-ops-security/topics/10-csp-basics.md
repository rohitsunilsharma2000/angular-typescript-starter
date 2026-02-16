# 10) CSP basics (report-only rollout)

HMS SPA তে third-party script থাকলে CSP না থাকলে XSS ঝুঁকি বাড়ে। report-only দিয়ে শুরু করুন।

## Why this matters (real-world)
- Inline script ইনজেকশন ঠেকাতে CSP কার্যকর।
- report-only দিয়ে বাস্তব break ছাড়া লগ পাওয়া যায়।

## Concepts
### Beginner
- CSP header structure: default-src, script-src, style-src।
- report-only বনাম enforce।
### Intermediate
- nonce/sha256 for inline; connect-src API domain; img-src CDN।
### Advanced
- report-uri/report-to; strict-dynamic; per-route CSP।

## Copy-paste Example
```http
# CSP header (report-only) example
Content-Security-Policy-Report-Only: default-src 'self'; script-src 'self' https://cdn.jsdelivr.net; style-src 'self' 'unsafe-inline'; img-src 'self' data: https://cdn.example.com; connect-src 'self' https://api.hms.example.com; report-uri https://csp-report.mycompany.com/report
```
```yaml
# netlify.toml header
[[headers]]
  for = "/*"
  [headers.values]
    Content-Security-Policy-Report-Only = "default-src 'self'; script-src 'self'; connect-src 'self' https://api.hms.example.com; report-uri https://csp-report.mycompany.com/report"
```

## Try it
- Beginner: report-only CSP যোগ করে devtools network এ report পাঠানো হচ্ছে কিনা দেখুন।
- Advanced: nonce-based inline script policy লিখুন; inline সরান।

## Common mistakes
- overly strict প্রথম দিনেই break; report-only দিয়ে শুরু না করা।
- connect-src এ API domain যোগ না করা → API ব্যর্থ।

## Interview points
- report-only rollout; connect-src; nonce/sha hash।

## Done when…
- CSP policy draft সেট; report-only হেডার যোগ।
- API/CDN domains যুক্ত।
- Report endpoint নথিভুক্ত।
