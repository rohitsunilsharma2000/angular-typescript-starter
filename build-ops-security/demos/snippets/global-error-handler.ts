import { ErrorHandler, Injectable } from '@angular/core';
import * as Sentry from '@sentry/angular-ivy';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  handleError(error: any): void {
    Sentry.captureException(error);
    console.error(error);
  }
}
