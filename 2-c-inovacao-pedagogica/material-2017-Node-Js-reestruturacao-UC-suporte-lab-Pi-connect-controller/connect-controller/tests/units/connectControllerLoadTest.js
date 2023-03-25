'use strict'

module.exports = function(controller, nrOfActions){
    const router = require('connect')()
    router.locals = { 'appLocalVar': 'appLocalValue' }
    router.use((req, res, next) => {
        req.app = router
        req.reqVar = 'reqValue'
        res.locals = { 'resLocalVar': 'resLocalValue' }
        next()
    })
    router.use(controller)
    router.use((err, req, res, next) => { console.log(err) })

    return {
        testLoadControllers: function(test) {
            test.expect(1)
            test.equal(controller.stack.length,  nrOfActions)
            test.done() 
        },

        testControllerIndex : function(test) {
            test.expect(2)
            const req1 = { 'url': '/users', 'method': 'get'}
            const req2 = { 'url': '/usersCamel', 'method': 'get'}
            const res = { 'render': (view, ctx) => test.equal(ctx, 'I am index')}
            router(req1, res)
            router(req2, res)
            test.done()
        },

        testControllerIndexId : function(test) {
            test.expect(8)
            const req1 = { 'url': '/users/27', 'method': 'get'}
            const req2 = { 'url': '/usersCamel/27', 'method': 'get'}
            const res = { 'render': (view, ctx) => { 
                test.equal(ctx.teamId, '27')
                test.equal(ctx.appLocalVar, 'appLocalValue')
                test.equal(ctx.resLocalVar, 'resLocalValue')
                test.equal(ctx.reqVar, 'reqValue')                    
            }}
            router(req1, res)
            router(req2, res)
            test.done()
        },
        
        testControllerDummyNrMembers : function(test) {
            test.expect(4)
            const req1 = { 'url': '/users/dummy/bastof/31/members', 'method': 'get'}
            const req2 = { 'url': '/usersCamel/dummy/bastof/31/members', 'method': 'get'}
            const res1 = { 'render': (view, ctx) => {
                test.equal(view, 'users/dummy/bastof/members')
                test.equal(ctx, '31')
            }}
            const res2 = { 'render': (view, ctx) => {
                test.equal(view, 'usersCamel/dummy/bastof/members')
                test.equal(ctx, '31')
            }}
            router(req1, res1)
            router(req2, res2)
            test.done()
        },

        testControllerGroups : function(test) {
            test.expect(2)
            const req1 = { 
                'url': '/users/groups?nr=24', 
                'method': 'get',
                'query': {'nr': '24' }
            }
            const req2 = { 
                'url': '/usersCamel/groups?nr=24', 
                'method': 'get',
                'query': {'nr': '24' }
            }
            const res = { 'render': (view, ctx) =>
                test.equal(ctx, '24')}
            router(req1, res)
            router(req2, res)
            test.done()
        },

        testControllerRouteAndQueryParameters : function(test) {
            test.expect(8)
            const req1 = { 
                'url': '/users/dummy/71/teams/xpto/ola?arg1=abc&arg2=super', 
                'method': 'get',
                'query': {'arg1': 'abc', 'arg2': 'super'}
            }
            const req2 = { 
                'url': '/usersCamel/dummy/71/teams/xpto/ola?arg1=abc&arg2=super', 
                'method': 'get',
                'query': {'arg1': 'abc', 'arg2': 'super'}
            }
            const res = { 'render': (view, ctx) => {
                test.equal(ctx.nr, '71')
                test.equal(ctx.arg1, 'abc')
                test.equal(ctx.arg2, 'super')
                test.equal(ctx.str, 'ola')
            }}
            router(req1, res)
            router(req2, res)
            test.done()
        },
        testControllerActionWithReqAndNext: function(test) {
            const req1 = { 'url': '/users/xone/67', 'method': 'get'}
            const req2 = { 'url': '/usersCamel/xone/67', 'method': 'get'}
            const res = { 'render': () => {} }
            test.expect(2)
            controller.use(() => {
                test.ok(true, 'this assertion should pass')
            })
            router(req1, res)
            router(req2, res)
            test.done()
        },
        testControllerActionPost: function(test) {
            test.expect(2)
            const req1 = { 
                'url': '/users/xone', 
                'method': 'post',
                'body': { 'stuff': 73 }
            }
            const req2 = { 
                'url': '/usersCamel/xone', 
                'method': 'post',
                'body': { 'stuff': 73 }
            }
            const res = { 'render': (view, ctx) => {
                test.equal(ctx, '73')
            }}
            router(req1, res)
            router(req2, res)
            test.done()
        }
    }
}