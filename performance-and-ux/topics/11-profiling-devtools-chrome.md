# 11) Profiling: Angular DevTools + Chrome

Patients list re-render বা billing টেবিল ফ্রেম ড্রপ—কোথায় সময় যাচ্ছে দেখতে টুল দরকার।

## Why this matters (real-world)
- হটপাথ চিহ্নিত না করলে অপ্টিমাইজ ভুল জায়গায় হবে।
- Memory leak/long task ধরতে না পারলে মোবাইলে ক্র্যাশ।
- ইন্টারভিউ: “How do you profile Angular?”

## Concepts
### Beginner
- Angular DevTools: Change Detection, Profiler timeline।
- Chrome Performance panel basics (record, CPU throttle)।
### Intermediate
- Flame chart পড়া; long task (>50ms) চিহ্নিত; Layout thrash শনাক্ত।
- Allocations/heap snapshot দিয়ে leak খোঁজা।
### Advanced
- Custom marks (performance.mark/measure); CPU profile on specific interactions; Coverage tool দিয়ে dead code।

## Copy-paste Example
```ts
// Add custom mark around heavy action
performance.mark('patients-load-start');
// ... load patients
performance.mark('patients-load-end');
performance.measure('patients-load', 'patients-load-start', 'patients-load-end');
```
```bash
# Bundle coverage (Chrome DevTools > Coverage panel) steps
# 1. Open page /patients
# 2. Coverage panel → Start instrumenting
# 3. Interact, stop → note unused bytes
```
```md
<!-- profiling-notes.md -->
Interaction: Scroll patients virtual list
Findings: 30% time in change detection, trackBy missing before fix
Action: enable OnPush + trackBy -> rerun, long tasks < 50ms
```

## Try it
- Beginner: Angular DevTools Profiler চালিয়ে কোন component বেশি re-render হচ্ছে নোট করুন।
- Advanced: Chrome Performance record করে long task >50ms খুঁজুন; ফিক্সের পর আবার তুলনা করুন।

## Common mistakes
- CPU throttle না করে ল্যাব রান করা → ভুল সিদ্ধান্ত।
- Coverage দেখে tree-shaking issue পেলেও action না নেওয়া।

## Interview points
- টুলের নাম + কী দেখেন (renders, long tasks, heap, coverage)।
- Custom marks/measure ব্যবহার করেন উল্লেখ করুন।

## Done when…
- একটি প্রোফাইল রেকর্ড সংরক্ষিত; bottleneck নোট করা।
- Long task সোর্স চিহ্নিত ও action লিস্ট করা।
- Coverage/unused bytes রিপোর্টেড।
