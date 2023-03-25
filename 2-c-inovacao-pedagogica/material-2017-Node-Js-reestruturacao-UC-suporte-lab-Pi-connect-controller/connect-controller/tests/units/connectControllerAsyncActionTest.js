'use strict'

module.exports = function(PromiseCtor){
    const dummyCtr = {
        'xone_id_foo_name': function(id, name) {
            return new PromiseCtor((resolve) => {
                resolve({name, id })
            })
        },
        'excep': function() {
            return new PromiseCtor((resolve, reject) => {
                reject(new Error('Illegal action'))
            })
        },
        'barIdFooName': function(id, name, cb) {
            cb(null, {id, name})
        }
    }

    const controller = require('./../../index')
    const router = controller(dummyCtr)

    return {
        testAsyncAction : function(test) {
            test.expect(2)
            const req = { 'url': '/xone/27/foo/Stuff', 'method': 'get'}
            const res = { 'render': (view, ctx) => {
                test.equal(ctx.id, 27)
                test.equal(ctx.name, 'Stuff')
                test.done()
            }}
            router(req, res)
        },
        testExceptionalAction : function(test) {
            test.expect(1)
            const req = { 'url': '/excep', 'method': 'get'}
            const res = { 'render': () => { test.ok(false) }}
            router.use((err, req, res, next) => { 
                test.equal(err.message, 'Illegal action')
                test.done()
            })
            router(req, res)
        },
        testActionWithCallback : function(test) {
            test.expect(2)
            const req = { 'url': '/bar/23/foo/Jose', 'method': 'get', 'app': {}}
            const res = { 'render': (view, ctx) => {
                test.equal(ctx.id, '23')
                test.equal(ctx.name, 'Jose')
                test.done()
            }}
            router(req, res)
        },
    }
}