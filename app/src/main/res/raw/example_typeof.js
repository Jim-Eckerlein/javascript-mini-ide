
/*
The type of a variable is determined
when assigned to a value:
*/

let x
print('Has not a type yet:')
print('type=' + typeof(x))
print()

x = 4;
print('Now, it is a number:')
print('type=' + typeof(x))
print()

x = 'jim';
print('Type can change at any time:')
print('type=' + typeof(x))
print()

x = { name: 'jim' }
print('Object is another built-in type:')
print('type=' + typeof(x))
print()

x = ['monday', 'tuesday', 'wednesday']
print('Arrays are treated as objects:')
print('type=' + typeof(x))
print()
