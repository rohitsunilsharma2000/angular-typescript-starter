const toast = { message: 'Saving patient...', type: 'info', progress: 0 };
console.log('toast start:', toast);

toast.progress = 50; console.log('toast mid:', toast);
toast.progress = 100; toast.type = 'success'; toast.message = 'Saved!'; console.log('toast done:', toast);
