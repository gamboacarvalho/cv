'use strict'

const controller = require('./../index')
const userCtr = require('./controllers/users.js')
const userCamelCtr = require('./controllers/usersCamel.js')
const express = require('express')

/**
 * mws is a Middleware of Middlewares (Router objects).
 * Each mws element is a Router corresponding to each controller.
 * In this case there is a single Router corresponding to userCtr source.
 * In turn, each Router contains a Middleware for each method of the controller. 
 */
const mws1 = controller( userCtr, { name: 'users'} )
const mws2 = controller( userCamelCtr, { name: 'usersCamel'} )
const router = express.Router()
router.use(mws1)
router.use(mws2)

module.exports = require('./units/connectControllerLoadTest')(router, 2)