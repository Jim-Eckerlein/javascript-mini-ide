
function add(x, y, z) {
    return x + y + z
}
let result = add(1, 2, 3)
print('1 + 2 + 3 = ' + result)

// You can assign a function to a variable, too:

let func = function(x) { return x * x }
result = func(4)
print(result)

// Reassignments are allowed, too:
func = add
result = func(1, 2, 3)
print(result)
