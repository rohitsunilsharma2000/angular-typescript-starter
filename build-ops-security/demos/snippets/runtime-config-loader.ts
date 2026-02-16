import { enableProdMode } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { AppComponent } from '../src/app/app.component';
import { environment as buildEnv } from '../src/environments/environment';

async function loadConfig() {
  try {
    const res = await fetch('/assets/config.json', { cache: 'no-store' });
    if (!res.ok) return buildEnv;
    const runtime = await res.json();
    return { ...buildEnv, ...runtime };
  } catch {
    return buildEnv;
  }
}

loadConfig().then(config => {
  if (config.production) enableProdMode();
  bootstrapApplication(AppComponent, {
    providers: [
      provideHttpClient(),
      { provide: 'APP_CONFIG', useValue: config }
    ]
  });
});
