function test(x, y) {
    print('hello world');
    return x + 2 * y;
}

for(var i = 0; i < 5; i++) {
    print('hi: ' + test(i, i + 1));
}
