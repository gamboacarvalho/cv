const asyncTest = require('./units/connectControllerAsyncActionTest.js')
const Q = require('q')

/**
 * @param action function with descriptor (resolve, reject) => void
 */
function PromiseCtor(action) {
    const deferred = Q.defer()
    action(
        res => deferred.resolve(res),
        err => deferred.reject(err)
    )
    return deferred.promise
}

module.exports = asyncTest(PromiseCtor)