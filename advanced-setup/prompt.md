You are working inside my repository.

PRIMARY SOURCE OF TRUTH (MUST READ FIRST)
- advanced-topics/README.md
If it does not exist, create it in the same format style as my other learning modules:
  - Intro (Bangla + HMS context)
  - How to use
  - Topic list with links (sequence-wise)
  - Quick revision
  - Prerequisites
  - Demo section

GOAL
1) Create (or update) folder: advanced-topics/
2) Ensure advanced-topics/README.md exists and matches the style above.
3) Create all topic markdown files referenced by the README under:
   advanced-topics/topics/*.md
4) Every topic file must include these sections in THIS EXACT ORDER:
   1) শিরোনাম + 3–5 লাইন পরিচিতি (বাংলা, হাসপাতাল উদাহরণ)
   2) Why this matters (real-world)
   3) Concepts (Beginner → Intermediate → Advanced) as separate subsections
   4) Copy-paste Example (compile-ready snippets: Angular + config where needed)
   5) Try it (Beginner task + Advanced task)
   6) Common mistakes
   7) Interview points
   8) Done when… (checklist)

LANGUAGE & STYLE RULES
- Write in Bengali with necessary technical English terms.
- No emojis.
- Examples must be HMS flavored: patient registration wizard, appointment live queue, billing UI, etc.
- Prefer Angular standalone components + route-level providers.
- Keep examples minimal but working and realistic.
- When code references packages (storybook, idb/dexie, websocket, etc.), add a short "Install" section with commands.

ADVANCED TOPICS CONTENT REQUIREMENTS (must be covered across topics)
A) Design systems
   - Design tokens (color/spacing/radius/typography/motion) and CSS variables strategy
   - Theming (dark mode + multi-clinic branding)
   - Storybook setup (stories as spec), controls/args, docs pages
   - Storybook tests: snapshot (where useful) + interaction tests (play)
B) Forms at scale
   - Typed forms, schema-driven/dynamic forms approach
   - Stepper/wizard architecture + draft save + resume
   - Complex validation UX: inline vs summary, async validators, cross-field validation
C) Internationalization
   - Angular built-in i18n flow (mark/extract/build)
   - Locale data formatting
   - ICU messages (plural/gender)
   - RTL support strategy + testing
D) Real-time & offline
   - WebSockets with RxJS: connect/reconnect/backoff, topic routing
   - PWA/Service Worker basics + update banner
   - Background sync concept + offline mutation queue (where supported)
   - IndexedDB wrappers (idb/dexie) + versioning/migrations
E) Large-repo productivity
   - Nx or Turborepo basics, apps/libs separation
   - Path aliases + module boundaries
   - Generators/scaffolding
   - Affected builds/tests and CI speedups

WHAT TO SHIP (must be included in README + final checkpoint topic)
- Storybook with 5+ components + controls + docs + CI build
- PWA toggle OR WebSocket-powered widget (choose one primary demo; optional second)
- i18n demo page (2 locales + one ICU plural + RTL toggle)
- Checkpoint: tokens consumed by components; build still passes budgets; Storybook CI build works

CHECKPOINT (final topic must include)
- “Design tokens are consumed in components (no hardcoded colors)”
- “Build passes perf budgets (bundle size budget still green)”
- “Storybook build works in CI (no missing assets)”
- “i18n + RTL demo is stable and layout doesn’t break”
- “Real-time/offline demo handles reconnect/offline state”

DEFAULT FILES TO CREATE (if README specifies different names, follow README exactly)
A) advanced-topics/README.md

B) advanced-topics/topics/
  01-design-tokens.md
  02-theming-architecture.md
  03-storybook-setup.md
  04-storybook-testing.md
  05-dynamic-forms.md
  06-wizard-stepper-flows.md
  07-complex-validation-ux.md
  08-i18n-basics.md
  09-locale-icu.md
  10-rtl-support.md
  11-websockets-rxjs.md
  12-pwa-service-worker.md
  13-indexeddb-wrappers.md
  14-monorepo-nx-turbo.md
  15-generators-affected.md
  16-checkpoint-done.md

C) Demos scaffold (docs + minimal snippets; do not build a full app unless repo already expects it)
  advanced-topics/demos/advanced-lab/README.md
  advanced-topics/demos/snippets/
    theme-tokens.ts
    button.stories.ts
    input.stories.ts
    modal.stories.ts
    toast.stories.ts
    table-row.stories.ts
    patient-wizard.component.ts
    validation-utils.ts
    i18n-demo.component.ts
    websocket-waiting-room.service.ts
    websocket-widget.component.ts
    pwa-update-banner.component.ts
    indexeddb-cache.service.ts
  advanced-topics/demos/storybook-ci/README.md

COPY-PASTE EXAMPLE REQUIREMENTS
- Angular snippets must include imports and be compile-ready when placed in an Angular project.
- Storybook examples must show 5+ components with controls (args) and at least one interaction test (play).
- Forms examples must show a wizard with cross-field validation + async validator.
- i18n examples must show 2 locales + one ICU plural + RTL toggle guidance.
- Real-time example must show reconnect/backoff + stream to UI.
- IndexedDB example must show simple cache/draft save + versioning note.
- Monorepo topic must include a minimal Nx/Turbo folder layout example + path alias rules + affected builds concept.

PROCESS
1) Read advanced-topics/README.md first.
2) If missing, create README with the requested format and include the topic list.
3) Create topics in sequence. Keep each topic beginner-friendly but include advanced notes.
4) Ensure all README links match filenames exactly.
5) At the end, print a short summary of created files.

ACCEPTANCE CRITERIA
- All files exist and README links match filenames.
- Every topic includes Beginner → Intermediate → Advanced sections + copy-paste examples.
- Storybook CI build instructions exist and the “What to ship” checklist is present.
