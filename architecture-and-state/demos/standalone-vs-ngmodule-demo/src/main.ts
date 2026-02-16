import { routes } from './app/app.routes';
import { navigate } from './app/router';

async function run() {
  console.log(await navigate(routes, 'home'));
  console.log(await navigate(routes, 'legacy'));
  console.log(await navigate(routes, 'missing'));
}

run();
