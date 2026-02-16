// Mock: Zone vs zoneless change detection triggers
function zoneStyle() {
  console.log('Zone: any async triggers change detection (timer, fetch, click).');
}
function zonelessStyle() {
  console.log('Zoneless: only explicit signals/markForCheck triggers.');
}
zoneStyle();
zonelessStyle();
