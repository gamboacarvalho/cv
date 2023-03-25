'use strict'

const debug = require('debug')('connectCtr')
const fs = require('fs')
const path = require('path')
const express = require('express')
const RouteInfo = require('./RouteInfo')

module.exports = loadControllersFrom

loadControllersFrom.RouteInfo = RouteInfo

/**
 * Creates a new Middleware based on the parameter src controller or
 * all controllers located in src path.  
 * If src is undefined then load controllers from  __dirname + '/controllers'
 * Otherwise, argument can be one of the following:
 * @param {String} src The controllers path
 * @param {Object} src Controller instance itself
 * @param {Object} options an object with following properties:
 *            name - the name of controller when it is loading a single controller instance.
 *            redirectOnStringResult - set this property to true when an action method returns a string as the path to redirect.
 *            resultHandler - (res, ctx) => void - a function that will handle the result of the action methods.
 * @return {Middleware} Middleware of Middleware objects (Router instances)
 */
function loadControllersFrom(src, options) {
    /**
     * If src argument is a controller instance itself
     * returns a Mw with routes for each controller action.
     */
    if(src && !(typeof src == 'string')) {
        const name = options
            ? options.name
            : ''
        return addCtrActionsToRouter(src, express.Router(), name, options)
    }
    /**
     * If src argument is a path or undefined, loads all
     * controllers from /controllers folder
     */
    return loadControllersFromFolder(src, options)
}

function loadControllersFromFolder(src, options) {
    const folder = src ? 
        path.join(process.cwd(), src)
        : path.join(process.cwd(), '/controllers')
    
    return fs
        .readdirSync(folder)
        .reduce((router, item) => {
            const ctr = require(path.join(folder, item))
            const name = item.substring(0, item.length - 3) // FIX !! Remove .js extension
            return addCtrActionsToRouter(ctr, router, name, options) // Add build Mw to router
        }, express.Router())
}

/**
 * For each method of ctr object creates a new Middleware that 
 * is added to router argument.
 * Route, query or body parameters are automatically mapped
 * to method arguments. 
 * @param {Object} ctr Controller instance
 * @param {Router} router express.Router instance
 * @param {String} name controller name
 * @param {Object} options object with optional properties: name, redirectOnStringResult and resultHandler
 * @return {Router} express.Router instance
 */
function addCtrActionsToRouter(ctr, router, name, options) {
    for(let prop in ctr) {
        if(ctr[prop] instanceof Function) {
            const info = new RouteInfo(ctr, name, prop, ctr[prop], options)
            /**
             * e.g. router.get(path, Middleware).
             */
            const path = name ? '/' + name + info.path : info.path
            const mw = newMiddleware(info)
            debug('Add route to %s %s', info.method, path)
            router[info.method].call(router, path, mw)
        }
    }
    return router
}

function newMiddleware(info) {
    /**
     * We CANNOT simply return info.middleware,
     * because info::middleware() depends of `this` context, 
     * which is the info object.
     */
    return (req, res, next) => {
        /**
         * And then info.middleware() redirects to 
         * info.action and renders the corresponding view.
         */
        info.middleware(req, res, next)
    }
}