const swState = { hasUpdate: true, registered: true };

function banner(state: typeof swState) {
  if (!state.registered) return 'SW not registered';
  return state.hasUpdate ? 'New version available (click to reload)' : 'Up to date';
}

console.log('Banner:', banner(swState));
