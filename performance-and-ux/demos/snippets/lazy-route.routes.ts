import { Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { OnpushHotListComponent } from './onpush-hot-list.component';

export const PERF_ROUTES: Routes = [
  {
    path: '',
    component: OnpushHotListComponent,
    providers: [provideHttpClient()],
  },
];
