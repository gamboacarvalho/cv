const hbs = require('hbs')

const blocks = {}

hbs.registerHelper('extend', function(name, context) {
    let block = blocks[name]
    if (!block) {
        block = blocks[name] = []
    }

    block.push(context.fn(this)) // for older versions of handlebars, use block.push(context(this))
})

hbs.registerHelper('block', function(name) {
    const val = (blocks[name] || []).join('\n')

    // clear the block
    blocks[name] = []
    return val
})
