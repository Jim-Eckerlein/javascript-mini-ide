
print('string with single quotes')
print("string with double quotes")
print()

print("mixed in single quote: ' ")
print('mixed in double quote: " ')
print()

print('escaped single quote: \'')
print("escaped double quote: \"")
print()

print("broken \
string");
print()

let foo = 4
let bar = 'string'
print(`This is a template string.
They can freely expand over multiple lines
and even include variables using special syntax:
foo is ${foo}
and this: ${bar} is bar`)
print()

print`Tagged string: foo ${foo} and bar ${bar}
are transformed into trailing arguments`
print()

print(`a template ${{member:3}.member} string`)
print()

print(`a single dollar $ is not treated special`)
