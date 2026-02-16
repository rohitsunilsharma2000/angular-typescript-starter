// Service test via pure function
function add(a:number,b:number){return a+b;}
console.log('add(2,3)=', add(2,3));

// Store test pattern: reducer-like
type State={count:number};
function increment(state:State){return {...state,count:state.count+1};}
let s:State={count:0};
s=increment(s);console.log('state after increment:', s);
