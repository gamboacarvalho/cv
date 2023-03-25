'use strict'

const reflectParameters = require('./reflectParameters')

/**
 * A Parameter Parser is a function with desc: (req, res, next) => void,
 * which is responsible for extracting an Action parameter from the req,
 * res or next objects.
 * Whenever an Action receives a plain req, res or next, the parser just 
 * forwards that argument to the action.
 * Otherwise, the parser must look for the Action parameter in req.params,
 * req.query or req.body.
 */
const defaultParamsParser = {
    'req': (req) => req,
    'res': (req, res) => res,
    'next': (req, res, next) => next,
}
const GET = 'get', POST = 'post', PUT = 'put', DELETE = 'delete'
const keysMethods = [ GET, POST, PUT, DELETE ]
/**
 * keywordsMaps turns in { 'get' : '', 'post' : '', ...} 
 */
const keywordsMaps = keysMethods.reduce((obj, curr) => {
    obj[curr] = ''
    return obj
}, {}) 
keywordsMaps.index = '/'


class RouteInfo{

    /**
     * @constructor
     *
     * @param {Object}   target  Instance containing the method (property)
     * @param {String}   name    controller name
     * @param {String}   prop    Property name which is the action's name
     * @param {Function} action  Method of controller object
     * @param {Object} options an object with following properties:
     *            name - the name of controller when it is loading a single controller instance.
     *            redirectOnStringResult - set this property to true when an action method returns a string as the path to redirect.
     *            resultHandler - (res, ctx) => void - a function that will handle the result of the action methods.
     */
    constructor (target, name, prop, action, options) {
        const actionParts = splitActionName(prop)
        
        this.target = target
        this.action = action
        this.argsNames = reflectParameters(action)
        this.path = parseMethodName(actionParts, this.argsNames)
        this.method = parseHttpMethod(actionParts)
        this.view = parseView(name || '', actionParts, this.argsNames)
        this.argsParser = initParamsParser(this.argsNames, actionParts)
        if(options && options.resultHandler) {
            this.resultHandler = options.resultHandler
        } else {
            this.resultHandler = options && options.redirectOnStringResult 
                ? this.resultHandlerWithRedirect
                : this.defaultResultHandler
        }
    }

    middleware(req, res, next) {
        const args = this.parseArguments(req, res, next)
        if(this.argsNames.length == args.length) {
            /**
             * If the number of arguments match the number of parameters, 
             * we expect to call a synchronous method or an asynchronous
             * method returning a Promise.
             */
            this.callControllerAction(args, res, next)
        } else if(this.argsNames.length == (args.length + 1)){
            /**
             * In this case we expect to receive an extra callback argument.
             * The number of parameters should be bigger than the number of arguments, 
             * just by one element, corresponding to the callback.
             */
            this.callControllerActionWithCallback(args, res, next)
        } else {
            throw new Error('Inconsistency between arguments and parameters. Parameters = '
                + JSON.stringify(this.argsNames) 
                + ' Arguments ' + JSON.stringify(args))
        }
    }
    /**
     * Get method arguments from the Middleware arguments.
     */
    parseArguments(req, res, next) {
        return this.argsParser
            .map(parser => parser(req, res, next))
            .filter(a => a != null)
    }
    /**
     * We are calling a synchronous method or an asynchronous method
     * returning a Promise.
     */
    callControllerAction(args, res, next) {
        const ctx = this.action.apply(this.target, args)
        /**
         * Ignore result when action method receives the res parameter.
         * It means that the action method takes the responsibility of 
         * sending the response via res object.  
         */
        if(this.argsNames.indexOf('res') >= 0) {
            if(ctx != undefined && ctx != null)
                next(Error('connect-controller: You are both receiving argument res and returning a result!!! Please remove res or return undefined!!!'))
            return // Action method must send response via res !!! 
        }
        /**
         * For async action methods
         */
        if(ctx && typeof ctx.then === 'function') {
            ctx
                .then(content => this.resultHandler(res, content))
                .catch(err => next(err))
        } 
        else { // Sync action methods
            this.resultHandler(res, ctx)
        }
    }
    /**
     * We are calling a method that conforms to node.js callback convention
     * of accepting a callback as last argument and calling that callback
     * with error as the first argument and success value on the second
     * argument.
     */
    callControllerActionWithCallback(args, res, next) {
        args.push((err, content) => {
            if(err) return next(err)
            this.resultHandler(res, content)
        })
        const ctx = this.action.apply(this.target, args)
        if(ctx)
            throw new Error('Expecting a function conforming to node.js callback convention!!! It must return void!!!!')
    }


    /**
     * The defaultResultHandler renders the view corresponding to this
     * action method and passing the ctx parameter as argument.
     */
    defaultResultHandler(res, ctx) {
        res.render(this.view, ctx)
    }

    /**
     * Whenever an action returns a string it is considered the path 
     * for a redirect. 
     * Otherwise it proceeds with res.render(...).
     */
    resultHandlerWithRedirect (res, ctx) {
        if (typeof ctx === 'string' || ctx instanceof String)
            return res.redirect(ctx)
        res.render(this.view, ctx)
    }
}

/**
 * Returns an array with the action's name split by underscores
 * or lowerCamelCase.
 */
function splitActionName(actionName) {
    if(actionName.indexOf('_') > 0) return actionName.split('_')
    else return actionName.split(/(?=[A-Z])/).map(p => p.toLowerCase())
}

/**
 * Returns the route path for the corresponding controller
 * method name.
 */
function parseMethodName(parts, argsNames) {
    /**
     * argsName could be in different case from that
     * of function's name.
     */
    const argsNamesLower = argsNames.map(arg => arg.toLowerCase())
    /**
     * Suppresses HTTP method if exists
     */
    if(keysMethods.indexOf(parts[0].toLowerCase()) >= 0)
        parts = parts.slice(1)
    /**
     * Converts each method part into route path  
     */
    return parts.reduce((prev, curr) => {
        if(keywordsMaps[curr]) prev += keywordsMaps[curr] 
        else {
            if(prev.slice(-1) != '/') prev += '/'
            const index = argsNamesLower.indexOf(curr.toLowerCase())
            if(index >= 0) {
                prev += ':'
                prev += argsNames[index] // Preserve argument Case
            } else {
                prev += curr
            }
        }
        return prev
    }, '')
}

/**
 * Gets the HTTP method from the Controller method name.
 * Otherwise returns 'get'. 
 */
function parseHttpMethod(parts) {
    const prefix = parts[0].toLowerCase()
    if(keysMethods.indexOf(prefix) >= 0)
        return prefix
    else 
        return GET
}

/**
 * @return {Array} Arrays of functions (req, res, next) => value. 
 * Each function looks for its Action parameter in req, res or next
 * object.
 */
function initParamsParser(argsNames, parts) {
    /**
     * arguments could be in different case from that
     * of function's name.
     */
    parts = parts.map(p => p.toLowerCase())
    /**
     * For each Action parameter returns a new  parser, 
     * which is responsible for getting the corresponding argument
     * from the req object.
     */
    return argsNames.map((name, index) => {
        if(defaultParamsParser[name]) return defaultParamsParser[name]
        if(parts.indexOf(name.toLowerCase()) >= 0) return req => req.params[name]
        if(index != (argsNames.length - 1))
            throw new Error('Parameter ' + name + ' not found in method name. Parameters can only be req, res, next or being part of method name.' )
        // return lookupParameterOnReq(name, index, argsNames.length) // !!!!! DEPRECATED >= 2.0.1
        return () => null
    })
}

/**
 * !!!!!DEPRECATED >= 2.0.1
 * Given the name of a parameter search for it in req, req.query,
 * req.body, res.locals, app.locals.
 */
function lookupParameterOnReq(name, index, length) {
    return (req, res) => {
        if(req[name]) return req[name]
        if(req.query && req.query[name]) return req.query[name]
        if(req.body && req.body[name]) return req.body[name]
        if(res.locals && res.locals[name]) return res.locals[name]
        if(req.app.locals && req.app.locals[name]) return req.app.locals[name]
        /**
         * In this case there is not a matching property in Request object for
         * the given parameter name. 
         * We just accept it, if that is the last parameter of the method, in which 
         * case it should correspond to a callback.
         */
        if(index != (length - 1))
            throw new Error('Parameter ' + name + ' not found in Request object!!!' )

        return null
    }
}

/**
 * @param {String} name controller name path with prefix /  
 * @property {String} prop Property name
 */
function parseView(controllerPath, parts, argsNames) {
    /**
     * argsName could be in different case from that
     * of function's name.
     */
    argsNames = argsNames.map(arg => arg.toLowerCase())

    const actionPath = parts
        .filter(p => argsNames.indexOf(p.toLowerCase()) < 0)   // Removes route parameters
        .filter(p => keysMethods.indexOf(p) < 0)               // Removes prefix get, or put, etc
        .join('/')
    return controllerPath != ''
        ? controllerPath + '/' + actionPath
        : actionPath
}

module.exports = RouteInfo