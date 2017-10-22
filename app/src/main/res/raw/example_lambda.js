
/*
Lambdas are functions passed as parameters or used in returns.
Syntax (simplified):

() => {
    ...
}

*/

function doTwice(func, args) {
    func(args)
    func(args)
}

// Simple lambda:
doTwice(() => print('Lambda call'))

// With parameters:
doTwice((text) => {
    print('Lambda call: ' + text)
}, 'Hello world')


// You can even wrap your parameters into an object,
// so you can access it by reference instead of getting
// a copy for each invocation:
doTwice((myArgs) => {
    print('Lambda call: ' + myArgs.callNr++)
}, {callNr: 1})
