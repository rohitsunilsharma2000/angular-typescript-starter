# Architecture & State টিউটোরিয়াল (বাংলা, হাসপাতাল ম্যানেজমেন্ট উদাহরণ)

Angular UI ডেভেলপারদের জন্য ধারাবাহিক রোডম্যাপ: Foundations → State Basics → Store Patterns → Data Contracts & UI System → Quality Gates। উদাহরণ সব হাসপাতালে ব্যবহৃত ফিচার (appointments, patients, billing, pharmacy)।

## কীভাবে ব্যবহার করবেন
1. `00-setup/angular-architecture-setup.md` দেখে workspace + lint rules + path alias ঠিক করুন।
2. নিচের ক্রমে `topics/` ফাইল পড়ুন; প্রতিটিতে কপি-পেস্ট উদাহরণ চালান।
3. হাতে-কলম: `demos/feature-slice-state` ও `demos/shared-ui-kit` নির্দেশনা অনুসরণ করুন।

## টপিক লিস্ট (Foundations → Quality Gates)
* [01) Feature boundaries + folder structure](topics/01-feature-boundaries-structure.md)
* [02) Smart vs Presentational](topics/02-smart-vs-presentational.md)
* [03) Standalone vs NgModule interop](topics/03-standalone-vs-ngmodule.md)
* [04) State strategy decision matrix](topics/04-state-strategy-decision-matrix.md)
* [05) RxJS service store + Signals bridge](topics/05-rxjs-store-signals.md)
* [06) Error + Loading models (standard contract)](topics/06-error-loading-models.md)
* [07) Optimistic updates + rollback](topics/07-optimistic-updates.md)
* [08) ComponentStore feature slice](topics/08-componentstore-feature-slice.md)
* [09) NgRx fundamentals (actions/reducer/effects/selectors)](topics/09-ngrx-fundamentals.md)
* [10) NgRx Entity + pagination/filter/sort](topics/10-ngrx-entity-pagination.md)
* [11) Data contracts: DTO typing + runtime validation](topics/11-data-contracts-validation.md)
* [12) UI composition: Design system + Theming + a11y](topics/12-ui-composition-theming-a11y.md)
* [13) Quality gates + Checkpoint](topics/13-quality-gates-checkpoint.md)

## দ্রুত রিভিশন
- প্রতিটি ফাইলে: কেন দরকার → ধারণা → HMS উদাহরণ → Try it → Interview points → Checklist.
- কোড: standalone component + route-level providers; RxJS switchMap/catchError/finalize; signals/ComponentStore/NgRx।

## প্রাক-প্রয়োজন
- Node.js >= 18
- Angular >= 17 (standalone)
- RxJS 7+
- TypeScript strict mode
- ESLint + import boundary rules (recommended)

## Demo
- `demos/feature-slice-state`: RxJS store / ComponentStore / NgRx তিন ভার্সন নোট।
- `demos/shared-ui-kit`: theme tokens ফাইল + Button/Input ডক + a11y নোট।

## Checkpoint
- No circular dependencies
- Selectors/derived state memoized
- Shared UI components documented (props/events/examples)
- Optimistic update works with rollback
- Theme tokens centralized and reusable
