
/*
Simulates a basic per-byte adding machine.
For the sake of simplicity, bits are represented individually as booleans.
One improvement could be that subtraction is supported by implement the two's complement.
Or even multiplication, division, raising or root extraction.
*/

let a = parseInt('01001110', 2)
let b = parseInt('00101011', 2)
let c
let aBytes = []
let bBytes = []
let cBytes = []
let carry = false
let offset = 0

let MAX = 8

function printByteArray(a) {
    let out = '';
    for(let i = 0; i < a.length; i++) {
        out += (a[i] ? 1 : 0);
    }
    print(out);
}

// Build byte arrays:
function buildByteArray(v, a) {
    for(let offset = 0; offset < MAX; offset++) {
        a[MAX - offset - 1] = (v & (1 << offset)) ? true : false;
    }
}
buildByteArray(a, aBytes);
buildByteArray(b, bBytes);

function add() {
    let aByte = aBytes[MAX - offset - 1];
    let bByte = bBytes[MAX - offset - 1];

    if(!carry) {
        if((aByte && !bByte) || (!aByte && bByte)) {
            cBytes[MAX - offset - 1] = true;
            carry = false;
        }
        else if(aByte && bByte) {
            cBytes[MAX - offset - 1] = false;
            carry = true;
        }
        else {
            cBytes[MAX - offset - 1] = false;
            carry = false;
        }
    }
    else {
        if(!aByte && !bByte) {
            cBytes[MAX - offset - 1] = true;
            carry = false;
        }
        else if(aByte && bByte) {
            cBytes[MAX - offset - 1] = true;
            carry = true;
        }
        else {
            cBytes[MAX - offset - 1] = false;
            carry = true;
        }
    }

    offset++;
}


for(let i = 0; i < MAX; i++) {
    add();
}

if(!carry) {
    print('Output:');
    printByteArray(cBytes);
}
else {
    print('Number overflow');
}
