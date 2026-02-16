import { getPatients } from './app/service';

async function run() {
  console.log('--- Valid payload ---');
  const ok = await getPatients('good');
  console.log(ok);

  console.log('\n--- Invalid payload (expect throw) ---');
  try {
    await getPatients('bad');
  } catch (err: any) {
    console.error('Validation error:', err.message);
  }
}

run();
