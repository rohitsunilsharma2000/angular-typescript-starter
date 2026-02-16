import { test, expect } from '@playwright/test';
import AxeBuilder from '@axe-core/playwright';

test('dashboard has no serious/critical violations', async ({ page }) => {
  await page.goto('http://localhost:4200/dashboard');
  const results = await new AxeBuilder({ page }).analyze();
  const serious = results.violations.filter(v => ['serious','critical'].includes(v.impact ?? ''));
  expect(serious).toEqual([]);
});
