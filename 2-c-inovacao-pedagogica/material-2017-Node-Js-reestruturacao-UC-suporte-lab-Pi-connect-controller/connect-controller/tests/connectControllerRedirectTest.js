'use strict'

const connectCtr = require('./../index')

module.exports.testParseMethodNameWithoutParameters = function(test) {
    /**
     * Arrange
     */
    const controller = {
        postActRedirect: function() {
            return 'new-uri'
        }
    }
    const router = connectCtr(controller, {redirectOnStringResult: true})
    const req = { 
        'url': '/act/redirect', 
        'method': 'post'
    }
    const res = { 
        'render': () => test.ok(false), // It should not enter here. Fail if it does.
        'redirect': (path) => {
            /**
             * Assert
             */
            test.equal(path, 'new-uri')
            test.done()
        }
    }
    test.expect(1)
    /**
     * Act
     */
    router(req, res)
}