
let day = 16
let month = 4
let year = 2005

if(month >= 3) {
    month -= 2
}
else {
    month += 10
}

if((month == 11) || (month == 12)) {
    year--
}

var nCentNum = parseInt(year / 100)
var nDYearNum = year % 100

var g = parseInt(2.6 * month - 0.2)

g +=  parseInt(day + nDYearNum)
g += nDYearNum / 4
g = parseInt(g)
g += parseInt(nCentNum / 4)
g -= parseInt(2 * nCentNum)
g %= 7

if(year >= 1700 && year <= 1751) {
    g -= 3
}
else {
    if(year <= 1699) {
        g -= 4
    }
}

if(g < 0){
    g += 7
}

let weekday
switch(g) {
    case 0: weekday = 'Sunday'; break
    case 1: weekday = 'Monday'; break
    case 2: weekday = 'Tuesday'; break
    case 3: weekday = 'Wednesday'; break
    case 4: weekday = 'Thursday'; break
    case 5: weekday = 'Friday'; break
    case 6: weekday = 'Saturday'; break
}

print('You were born on a', weekday)
