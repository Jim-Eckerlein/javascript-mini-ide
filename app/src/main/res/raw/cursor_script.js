let cursor = document.getElementById('cursor');
let old;
setInterval(() => {
    let now = Math.trunc(new Date().getMilliseconds() / 500);
    if(now !== old) {
        cursor.style.visibility = cursor.style.visibility === 'visible' ? 'hidden' : 'visible';
        old = now;
    }
}, 100);
