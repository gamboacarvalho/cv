'use strict'

const footballDb = require('./../db/footballDb')

/**
 * This API is similar to the football.js but with 
 * node.js callback convention instead of Promises.
 */
module.exports = {
    getLeaguesIdTable, // binds to /football/leagues/:id/table
    getLeagues,        // binds to /football/leagues
    index,             // binds to /football/
    indexId            // binds to /football/:id
}

/**
 * Every action parameter (e.g. id) taking part of method's name (e.g. _id_)
 * is bound to the corresponding argument of req.params (e.g. req.params.id).
 * In this case this function is useless and we could simply bound 
 * property 'getLeaguesIdTable' to method `footballDb.getLeaguesIdTable`.
 */
function getLeaguesIdTable(id, cb){
    footballDb
        .getLeaguesIdTable(id)
        .then(data => cb(null, data))
        .catch(err => cb(err))
}

/**
 * Every action parameter that is NOT part of the method's name
 * must be req, res or next.
 * NOTE that if you receive res, then you are responsible for sending the response.
 */
function getLeagues(req, cb) {
    const name = req.query.name? req.query.name.toLowerCase() : ''
    footballDb
        .getLeagues()
        .then(leagues => leagues
            .filter(l => !name || l.caption.toLowerCase().indexOf(name) >= 0)
            .map(addLeaguePath))
        .then(data => cb(null, data))
        .catch(err => cb(err))
    
    function addLeaguePath(league) {
        league.leagueHref = '/football/leagues/' + league.id + '/table'
        return league
    }
}

/**
 * Whenever an action receives the `res` parameter, the connect-controller
 * gets out of the way and delegates on that action the responsibility of
 * sending the response.
 * So whenever you want to do something different from the default behavior 
 * you just have to append `res` to your parameters.
 */
function index(res) {
    /**
     * If this controller is loaded with an options object set with
     * the property `redirectOnStringResult` then this is equivalent
     * to removing the `res` parameter and just return the destination
     * string path '/football/leagues'.
     */
    res.redirect('/football/leagues')
}

/**
 * If this controller is loaded with an options object set with the property 
 * `redirectOnStringResult` then this action method redirects to 
 * `/football/leagues/:id/table`.
 */
function indexId(id) {
    return '/football/leagues/' + id + '/table'
}
