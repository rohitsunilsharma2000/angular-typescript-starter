# Advanced Lab

Included snippets (copy from `../snippets`):
- Design tokens (`theme-tokens.ts`)
- Storybook stories (button, input, modal, toast, table-row)
- Patient wizard (`patient-wizard.component.ts`)
- Validation utils (`validation-utils.ts`)
- i18n demo (`i18n-demo.component.ts`)
- WebSocket widget (`websocket-waiting-room.service.ts`, `websocket-widget.component.ts`)
- PWA update banner (`pwa-update-banner.component.ts`)
- IndexedDB cache (`indexeddb-cache.service.ts`)

How to try quickly
1) Copy tokens + components into your Angular workspace.
2) Run Storybook: `npm run storybook` and browse tokens/components.
3) Serve app and open routes wired to wizard/i18n demo/websocket widget.
4) Build PWA (`ng add @angular/pwa`) and place update banner in shell.

Primary demo options
- PWA toggle OR WebSocket widget (choose one to showcase); the other optional.
