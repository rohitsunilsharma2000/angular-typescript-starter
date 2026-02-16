import { CatalogApi } from './app/features/catalog/data-access/catalog.api';
import { CatalogFacade } from './app/features/catalog/data-access/catalog.facade';
import { CatalogPage } from './app/features/catalog/pages/catalog.page';

async function run() {
  const page = new CatalogPage(new CatalogFacade(new CatalogApi()));
  await page.init();
  console.log('--- Rendered UI (presentational) ---');
  console.log(page.render());
  console.log('--- Simulate click ---');
  page.simulateUserClick();
}

run();
