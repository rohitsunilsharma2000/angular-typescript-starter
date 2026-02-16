# Storybook CI

Steps
1) Install: `npx storybook@latest init`.
2) Scripts: `storybook`, `build-storybook` already in package.json.
3) CI (GitHub Actions excerpt):
```yaml
- run: npm ci
- run: npm run build-storybook
- run: npm run test-storybook # if using test-runner
```
4) Artifacts: upload `storybook-static` for preview.
5) Budget reminder: ensure Storybook build doesn’t pull prod secrets; mock env.
