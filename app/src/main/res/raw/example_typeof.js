
/*
The type of a variable is determined when assigned to a value:
*/

var x;
print('Has not a type yet: type=\n' + typeof(x));

x = 4;
print('Now, it is a number: type=' + typeof(x));

x = "jim";
print('Type can change at any time: type=' + typeof(x));

x = { name: 'jim' };
print('Object is another built-in type: type=' + typeof(x));

x = ['monday', 'tuesday', 'wednesday'];
print('Array is considered as an object: type=' + typeof(x));
