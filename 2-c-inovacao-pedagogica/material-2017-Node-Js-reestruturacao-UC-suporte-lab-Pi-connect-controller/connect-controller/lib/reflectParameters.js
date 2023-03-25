'use strict'

module.exports = getParamNames 

const STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg
const ARGUMENT_NAMES = /([^\s,]+)/g

function getParamNames(func) {
    const fnStr = func.toString().replace(STRIP_COMMENTS, '')
    const result = fnStr
        .slice(fnStr.indexOf('(')+1, fnStr.indexOf(')'))
        .match(ARGUMENT_NAMES)
    return result === null ? [] : result
}