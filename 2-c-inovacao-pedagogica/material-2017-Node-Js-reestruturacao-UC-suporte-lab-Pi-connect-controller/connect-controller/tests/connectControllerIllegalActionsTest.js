const dummyCtr = {
    'xone_id': function(id, res) {
        /**
         * This action should not return a context object, 
         * because it requires the special res argument,
         * meaning that it takes responsability to send the response.
         */
        return 'Illegal stuff'
    }
}

const controller = require('./../index')
const router = controller(dummyCtr)

module.exports.testLoadControllerWithIllegalAction = function(test) {
    test.expect(1)
    const req = { 'url': '/xone/27', 'method': 'get'}
    const res = { 'render': () => { test.ok(false) }}
    router.use((err, req, res, next) => { 
        test.ok(true) 
        test.done()
    })
    router(req, res)
}