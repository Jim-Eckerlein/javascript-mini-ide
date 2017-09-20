
/*
The type of a variable is determined when assigned to a value:
*/

var x; // has not a type yet
print('Has not a type yet: type=' + typeof(x));

x = 4; // now, it's an integer
print('Now, it is an integer: type=' + typeof(x));

x = "jim";
print('Type can change at any time: type=' + typeof(x));

x = ['monday', 'tuesday', 'wednesday'];
print('Array is another type: type=' + typeof(x));

x = { name: 'jim' };
print('Object is another built-in type: type=' + typeof(x));
