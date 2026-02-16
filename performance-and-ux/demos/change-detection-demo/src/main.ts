// Simulate render counts: Default vs OnPush
let rendersDefault = 0;
let rendersOnPush = 0;

function defaultCd(trigger: string) { rendersDefault++; console.log(`[Default] render #${rendersDefault} after ${trigger}`); }
function onPushCd(trigger: string, inputChanged: boolean) {
  if (inputChanged) { rendersOnPush++; console.log(`[OnPush] render #${rendersOnPush} after ${trigger}`); }
}

defaultCd('init'); onPushCd('init', true);
defaultCd('timer'); onPushCd('timer', false);
defaultCd('http'); onPushCd('http', true);
