var person = {
    name: 'Jim',
    age: 19,
    height: 175,
    eyeColor: 'blue',
    greet: function () {
        print('Hi, I am ' + this.name);
    }
};

/*
 Iterate over the object members:
*/
print('Members:');
for(let member in person) {
    print('- ' + member);
}

person.greet(); // Call to a member function
