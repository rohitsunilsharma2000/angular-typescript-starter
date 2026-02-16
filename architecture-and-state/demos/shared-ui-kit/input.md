# Input docs

Props
- `label`: string
- `placeholder`: string
- `error`: string | null

Usage
```html
<label class="block text-sm">
  Patient Name
  <input class="border px-3 py-2 rounded" placeholder="Rima" />
</label>
<p class="text-red-600 text-xs">{{ error }}</p>
```

A11y
- `aria-invalid` when error present
- associate label with `for` + `id`

Style idea
```css
input.hms-input { border: 1px solid #cbd5e1; padding: 12px; border-radius: 8px; }
input.hms-input:focus-visible { outline: 2px solid #2563eb; outline-offset: 2px; }
```
