// Mock Router navigate/guard
class MockRouter {
  log: string[] = [];
  navigate(commands: string[]) { this.log.push('navigate:' + commands.join('/')); return Promise.resolve(true); }
}
class MockGuard {
  canActivate = true;
  check() { return this.canActivate; }
}
const router = new MockRouter();
const guard = new MockGuard();
console.log('guard allows?', guard.check());
router.navigate(['patients','1']);
console.log('router log:', router.log);
