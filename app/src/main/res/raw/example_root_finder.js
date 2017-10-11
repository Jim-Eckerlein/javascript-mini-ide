
let input = 123456;
let acceptance = 0.0001;
let bounds = [];
let result;

if(input < 0) {
    exit("Input must be positive:", input);
}

// Initialize bounds:
for(let x = input; ; x--) {
    if(x * x < input) {
        bounds[0] = x;
        bounds[1] = x + 1;
        break;
    }
}

// Actual method to find the root, called recursively:
(function findRoot() {

    // Check whether bounds are already acceptable:
    if(bounds[1] - bounds[0] < acceptance) {
        return;
    }

    let m = (bounds[1] - bounds[0]) / 2 + bounds[0];

    if(m * m < input) {
        bounds[0] = m;
    }
    else {
        bounds[1] = m;
    }

    findRoot();
})();

inaccuracy = (bounds[1] - bounds[0]) / 2;
result = inaccuracy + bounds[0];

print('Square root of', input, 'is:');
print(result);
print('With +/-' + inaccuracy, 'inaccuracy');
