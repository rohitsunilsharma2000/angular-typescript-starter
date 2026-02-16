You are working inside my local repository.

PRIMARY SOURCE OF TRUTH:
- /Users/meghnadsaha/IdeaProjects/angular-typescript-starter/performance-and-ux/README.md

You MUST read that README first and follow its structure and sequencing exactly. Do not invent a different syllabus.

GOAL
1) Create (or update) a folder: performance-and-ux/
2) Ensure performance-and-ux/README.md exists and matches the same format style as my other learning modules:
   - Intro (Bangla + HMS context)
   - How to use
   - Topic list with links
   - Quick revision
   - Prerequisites
   - Demo section
3) Create all topic markdown files referenced by the README under:
   performance-and-ux/topics/*.md
4) Every topic file must include (in this exact order):
   1. শিরোনাম + 3–5 লাইন পরিচিতি (বাংলা, হাসপাতাল উদাহরণ)
   2. Why this matters (real-world)
   3. Concepts (Beginner → Intermediate → Advanced) as separate subsections
   4. Copy-paste Example (Angular, must be compile-ready snippets)
   5. Try it (Beginner task + Advanced task)
   6. Common mistakes
   7. Interview points
   8. Done when… (checklist)

LANGUAGE & STYLE RULES
- Write in Bengali with necessary technical English terms.
- No emojis.
- Examples must be HMS flavored: appointments/patients/billing dashboards, lists, tables, status updates, etc.
- Prefer Angular standalone components and route-level providers.
- Keep examples minimal but working.

PERFORMANCE & UX CONTENT REQUIREMENTS (must be covered)
- Change detection: Default vs OnPush; signals; zone-less options; async pipe vs manual subscribe.
- Code splitting: route-level lazy loading; dynamic import(); preloading strategies.
- Rendering: SSR/SSG with Angular Universal; hydration; defer/lazy images; CDK virtual scroll.
- Profiling: Angular DevTools; Chrome Performance; Lighthouse; bundle analysis; perf budgets.
- Perceived UX: skeletons/shimmers; optimistic UI; toast patterns; progress indicators.
- What to ship must be included as a checklist in README + the final checkpoint topic:
  - Enable OnPush or signals in hot paths
  - Lazy-load at least one route
  - Skeleton loader
  - Bundle size report
- Checkpoint must include:
  - Bundle < target budget (explicit KB budget)
  - LCP within goal on lab run (explicit goal)
  - No major hydration errors

FILES TO CREATE (create exactly these unless README lists different names; if README differs, follow README)
A) performance-and-ux/README.md

B) performance-and-ux/topics/
  01-perf-baseline-goals.md
  02-change-detection-onpush.md
  03-signals-hot-paths.md
  04-zone-zoneless.md
  05-async-pipe-vs-subscribe.md
  06-lazy-loading-routes.md
  07-dynamic-import-preloading.md
  08-ssr-ssg-hydration.md
  09-lazy-images-cls.md
  10-virtual-scroll-trackby.md
  11-profiling-devtools-chrome.md
  12-lighthouse-bundle-budgets.md
  13-skeletons-optimistic-ux.md
  14-toast-progress-patterns.md
  15-checkpoint-done.md

C) Demos scaffold (docs + minimal code snippets; do not build a full app unless repo already expects it)
  performance-and-ux/demos/perf-hot-paths/README.md
  performance-and-ux/demos/bundle-reports/README.md
  performance-and-ux/demos/snippets/
    onpush-hot-list.component.ts
    lazy-route.routes.ts
    skeleton-loader.component.ts
    toast.service.ts
    virtual-scroll.component.ts

COPY-PASTE EXAMPLE REQUIREMENTS
- Must include imports at top of snippet.
- Must include minimal models/interfaces.
- Must show at least one “hot path” component optimized:
  - OnPush + trackBy + derived state outside template
- Must show at least one lazy route config using standalone routing.
- Must show a skeleton loader pattern that avoids CLS (fixed heights / aspect ratio).
- Must show a toast pattern (success + error + retry).
- Must show a minimal bundle report instruction section (how to generate + what to look for).

PROCESS
1) Read performance-and-ux/README.md first.
2) If it already contains topic names, use those exact filenames and ensure links match.
3) Create all topics in the same sequence.
4) Keep each topic beginner-friendly but include advanced notes (budgets, memoization, hydration mismatch patterns, long task profiling).
5) At the end, print a short summary of created files.

ACCEPTANCE CRITERIA
- All files listed exist (or README equivalents exist).
- README links match filenames exactly.
- Each topic has Beginner → Intermediate → Advanced sections + compile-ready copy-paste Angular snippets.
- Final checkpoint topic contains explicit KB budget + explicit LCP goal + hydration error checklist.
