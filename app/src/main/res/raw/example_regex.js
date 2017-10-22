
let text = "This is a long text"
let regex = /long/i

let position = text.search(regex)
print(`Position of ${/long/i} within "${text}": ${position}\n`)

// Replace 'long' by 'short'
text = text.replace(regex, 'short')
print(text)
