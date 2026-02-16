# Button docs

Props
- `kind`: 'primary' | 'ghost' (default 'primary')
- `disabled`: boolean

Usage
```html
<hms-button kind="primary">Admit patient</hms-button>
<hms-button kind="ghost">View invoices</hms-button>
```

A11y
- `role="button"`, `tabindex="0"`
- `:focus-visible` outline

Style idea
```css
hms-button {
  background: var(--hms-primary, #2563eb);
  color: white;
  padding: 12px;
  border-radius: 8px;
}
```
