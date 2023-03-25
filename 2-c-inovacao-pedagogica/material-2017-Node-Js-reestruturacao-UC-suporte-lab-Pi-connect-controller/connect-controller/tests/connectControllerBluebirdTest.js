const asyncTest = require('./units/connectControllerAsyncActionTest.js')
const PromiseCtor = require('bluebird')
module.exports = asyncTest(PromiseCtor)