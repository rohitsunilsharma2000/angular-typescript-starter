# async pipe vs manual subscribe demo (simulated)

Shows manual subscription vs auto-disposed pattern.

## Commands
```bash
npm install
npm run demo
npm run typecheck
```

## Try
- Call `manualSubscribe()` multiple times and see leak counter rise.
- Imagine template async pipe auto-unsubscribing.
