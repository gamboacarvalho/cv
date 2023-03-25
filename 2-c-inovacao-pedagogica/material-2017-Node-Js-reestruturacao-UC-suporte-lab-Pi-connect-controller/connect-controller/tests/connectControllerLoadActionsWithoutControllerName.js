'use strict'

const actions = {
    'dummyBastofNrMembers':  function (nr) {
        return nr
    }    
}
const controller = require('./../index')
const router = controller(actions)

module.exports = {
    testControllerDummyNrMembers : function(test) {
        test.expect(2)
        const req = { 'url': '/dummy/bastof/31/members', 'method': 'get'}        
        const res = { 'render': (view, ctx) => {
            test.equal(view, 'dummy/bastof/members')
            test.equal(ctx, '31')
        }}
        router(req, res)
        test.done()
    }
}