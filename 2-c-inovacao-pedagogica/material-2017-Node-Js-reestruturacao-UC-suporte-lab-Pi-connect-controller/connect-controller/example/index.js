'use strict'

/**
 * Import npm packages
 */
const express = require('express')
const path = require('path')
const favicon = require('serve-favicon')
const cookieParser = require('cookie-parser')
const bodyParser = require('body-parser')
const hbs = require('hbs')
const sitemapHtml = require('express-sitemap-html')
/**
 * Import local libraries
 */
const favoritesDb = require('./lib/db/favoritesDb')
const routeFootball = require('./lib/routes/football')
const controller = require('./../index')

/**
 * Instantiate...
 */
const app = express()
app.set('views', path.join(__dirname, 'lib/views'))
app.set('view engine', 'hbs')
hbs.localsAsTemplateData(app)
hbs.registerPartials(__dirname + '/lib/views/favorites')
require('./lib/views/hbs-helpers')

/**
 * Add Middlewares
 */
app.use(favicon(path.join(__dirname, 'public', 'R2D2.ico')))
//app.use(logger('dev'))
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: false }))
app.use(cookieParser())
app.use(express.static(path.join(__dirname, 'public')))
app.use((req, res, next) => { 
    app.locals.favoritesList = favoritesDb // Enhance it with per user favorites based on authentication
    next()
})

/**
 * Add controller
 */
const football = controller('./lib/controllers', {redirectOnStringResult: true})
const footballApi = controller('./lib/controllers', {resultHandler: (res, ctx) => res.json(ctx)})
app.use(football)
app.use('/api', footballApi)
app.use('/', sitemapHtml(app))
/**
 * Add Routes
 */
app.use('/router', routeFootball) // <=> Route on '/routes' with same features of /football controller

/**
 * Error-handling middlewares
 */
// catch 404 and forward to default error handler
app.use(function(req, res, next) {
    const err = new Error('Not Found')
    err.status = 404
    next(err)
})

// print stacktrace
app.use(function(err, req, res, next) {
    res.status(err.status || 500)
    res.render('error', {
        message: err.message,
        error: err
    })
})

module.exports = app