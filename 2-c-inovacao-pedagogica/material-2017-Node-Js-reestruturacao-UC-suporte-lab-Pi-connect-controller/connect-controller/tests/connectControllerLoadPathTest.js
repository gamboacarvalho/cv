'use strict'

const controller = require('./../index')

/**
 * mws is a Middleware of Middlewares (Router objects).
 * Each mws element is a Router corresponding to each controller.
 * In this case there is a single Router corresponding to userCtr source.
 * In turn, each Router contains a Middleware for each method of the controller. 
 */
const mws = controller('./tests/controllers') // Loads all modules from given path

module.exports = require('./units/connectControllerLoadTest')(mws, 16)