'use strict'

const controller = require('./../index')

module.exports.testParseMethodNameWithoutParameters = function(test) {
    testParseMethodName(
        function get_members() {}, // actual
        '/members',                // expected 
        'get',
        'members',
        test)
}

module.exports.testParseMethodNameWithoutParametersNorPrefix = function(test) {
    testParseMethodName(
        function members() {},   // actual
        '/members',              // expected 
        'get',
        'members',
        test)
}

module.exports.testParseMethodNameWithoutGetPrefix = function(test) {
    testParseMethodName(
        function index_id_members(id) {},   // actual
        '/:id/members',                     // expected 
        'get',
        'index/members',
        test)
}

module.exports.testParseCamelMethodNameWithoutGetPrefix = function(test) {
    testParseMethodName(
        function indexIdMembers(id) {},   // actual
        '/:id/members',                   // expected 
        'get',
        'index/members',
        test)
}


module.exports.testParseMethodNameWithGet = function(test) {
    testParseMethodName(
        function get_index_id_members(id) {},   // actual
        '/:id/members',                         // expected 
        'get', 
        'index/members',
        test)
}

module.exports.testParseCamelMethodNameWithGet = function(test) {
    testParseMethodName(
        function getIndexIdMembers(id) {},   // actual
        '/:id/members',                         // expected 
        'get', 
        'index/members',
        test)
}


module.exports.testParseMethodNameWithPut = function(test) {
    testParseMethodName(
        function put_favItem_teamId(teamId, favoritesList) {},
        '/favItem/:teamId',                         // expected 
        'put',
        'favItem',
        test)
}

module.exports.testParseCamelMethodNameWithPut = function(test) {
    testParseMethodName(
        function putFavitemTeamid(teamId, favoritesList) {},
        '/favitem/:teamId',                         // expected 
        'put',
        'favitem',
        test)
}


function testParseMethodName(func, expectedPath, expectedMethod, expectedView, test) {
    /**
     * Act
     */
    test.expect(4)
    const routeInfo = new controller.RouteInfo(null, '', func.name, func) // (target, method name, method)
    /**
     * Assert
     */
    test.equal(routeInfo.path, expectedPath)
    test.equal(routeInfo.action, func)
    test.equal(routeInfo.method, expectedMethod)
    test.equal(routeInfo.view, expectedView)
    test.done()   
}