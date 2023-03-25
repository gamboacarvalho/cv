'use strict'

const connectCtr = require('./../index')

module.exports.testParseMethodNameWithoutParameters = function(test) {
    /**
     * Arrange
     */
    const controller = {
        dummy: function() {
            return {
                foo: () => 'foo',
                bar: 'just a string'
            }
        }
    }
    const router = connectCtr(controller,  { resultHandler: (res, ctx) => res.json(ctx) })
    const req = { 
        'url': '/dummy', 
        'method': 'get'
    }
    const res = { 
        'render': () => test.ok(false), // It should not enter here. Fail if it does.
        'json': (ctx) => {
            /**
             * Assert
             */
            test.equal(ctx.bar, 'just a string')
            test.equal(ctx.foo(), 'foo')
            test.done()
        }
    }
    test.expect(2)
    /**
     * Act
     */
    router(req, res)
}