import { test, expect } from '@playwright/test';

test('auth + patient create smoke', async ({ page }) => {
  await page.goto('http://localhost:4200/login');
  await page.getByLabel('Email').fill('nurse@hms.test');
  await page.getByLabel('Password').fill('secret');
  await page.getByRole('button', { name: /login/i }).click();

  await expect(page.getByRole('heading', { name: /dashboard/i })).toBeVisible();
  await page.route('**/api/patients', route => route.fulfill({ json: [] }));
  await page.getByRole('link', { name: /patients/i }).click();
  await page.getByRole('button', { name: /add patient/i }).click();
  await page.getByLabel(/name/i).fill('Rima');
  await page.getByRole('button', { name: /save/i }).click();
  await expect(page.getByText(/patient created/i)).toBeVisible();
});
