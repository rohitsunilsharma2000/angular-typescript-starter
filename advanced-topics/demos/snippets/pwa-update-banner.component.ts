import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { SwUpdate } from '@angular/service-worker';

@Component({
  standalone: true,
  selector: 'hms-update-banner',
  template: `
    <div *ngIf="hasUpdate" class="banner">
      New version available
      <button (click)="reload()">Reload</button>
    </div>
  `,
  styles: [`.banner{position:fixed;bottom:12px;left:12px;right:12px;padding:12px;background:#0ea5e9;color:white;}`],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PwaUpdateBannerComponent {
  private sw = inject(SwUpdate);
  hasUpdate = false;
  constructor() {
    this.sw.versionUpdates.subscribe(evt => { if (evt.type === 'VERSION_READY') this.hasUpdate = true; });
  }
  reload() { this.sw.activateUpdate().then(() => document.location.reload()); }
}
