# Tailwind CSS — CDN Quick Track (Runnable Demos)

Minimal Tailwind path using the Play CDN (no build step) so you can copy-paste into a single HTML file, run with `live-server`, and test instantly.

## Who is this for
- Frontend/Angular/TypeScript learners needing fast UI scaffolds.
- Anyone who wants zero build setup (CDN only) but valid Tailwind syntax.

## Quick start (runnable)
1) `npm i -g live-server` if you don't have it.
2) Create `index.html` with the examples (all files below already CDN-ready):
```html
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <script src="https://cdn.tailwindcss.com"></script>
  <title>Tailwind CDN Demo</title>
</head>
<body class="bg-slate-50 text-slate-900">
  <div class="p-6">Hello Tailwind CDN</div>
</body>
</html>
```
3) `cd tailwind-core && live-server` → open `index.html`.

## Topics (copy-paste ready)
01) [topics/01-setup-cdn.md](topics/01-setup-cdn.md) — Play CDN, inline config, safest defaults.
02) [topics/02-typography-colors.md](topics/02-typography-colors.md) — Text scale, leading, color tokens.
03) [topics/03-spacing-sizing.md](topics/03-spacing-sizing.md) — Padding/margin/width/height, container.
04) [topics/04-flex-layout.md](topics/04-flex-layout.md) — Rows, wrap, alignment, gaps.
05) [topics/05-grid-layout.md](topics/05-grid-layout.md) — Auto-fit grids, spans, responsive columns.
06) [topics/06-forms.md](topics/06-forms.md) — Inputs, validation hints, focus rings.
07) [topics/07-components.md](topics/07-components.md) — Buttons, badges, cards, nav, modal.
08) [topics/08-responsive.md](topics/08-responsive.md) — Breakpoints, container, hidden/show, mobile-first.
09) [topics/09-dark-mode-theme.md](topics/09-dark-mode-theme.md) — Class-based dark, custom colors via inline config.
10) [topics/10-animations-effects.md](topics/10-animations-effects.md) — Transitions, keyframes, shadows, glass.
11) [topics/11-accessibility.md](topics/11-accessibility.md) — Focus states, skip links, reduced motion.
12) [topics/12-performance-notes.md](topics/12-performance-notes.md) — CDN caveats, when to move to build.
13) [topics/13-advanced-theming.md](topics/13-advanced-theming.md) — CSS variables + arbitrary values.
14) [topics/14-advanced-animations.md](topics/14-advanced-animations.md) — Custom keyframes, stagger, motion-safe.
15) [topics/15-state-variants.md](topics/15-state-variants.md) — data-/aria- driven variants, peer/group states.
16) [topics/16-grid-masonry-effects.md](topics/16-grid-masonry-effects.md) — Dense grid, aspect ratios, media tiles.
17) [topics/17-tailwind-core-basics.md](topics/17-tailwind-core-basics.md) — Utility-first core (spacing, colors, typography, layout).
18) [topics/18-components-patterns.md](topics/18-components-patterns.md) — Buttons→skeletons patterns.
19) [topics/19-responsive-accessibility.md](topics/19-responsive-accessibility.md) — Mobile-first, dark, focus, ARIA, motion-safe.
20) [topics/20-advanced-tailwind.md](topics/20-advanced-tailwind.md) — Config, @apply guidance, variants, performance/debug.
21) [topics/21-utility-reference.md](topics/21-utility-reference.md) — One-page utility cheat-sheet with examples.

## How to use
- Open the topic file, copy the HTML block, save as `index.html` (or drop into your page), run `live-server`.
- Everything is CDN-based, so no build step. For production, move to a proper build (`tailwindcss` CLI/Vite/Angular builder) to tree-shake classes.

## Demo idea
Start from `topics/07-components.md` and stitch navbar + hero + cards + CTA into a small hospital dashboard; add dark mode from `09-dark-mode-theme.md` and motion tweaks from `10-animations-effects.md`.
