'use strict'

const footballDb = require('./../db/footballDb')
const express = require('express')

const router = express.Router()
router.get('/leagues/:id/table', getLeaguesIdTable)
router.get('/leagues', getLeagues)
router.get('/', index)
module.exports = router

function getLeaguesIdTable(req, res, next) {
    const id = req.params.id
    footballDb
        .getLeaguesIdTable(id)
        .then(league => {
            res.render('football/leagues/table', league)
        })
        .catch(err => next(err))
}

function getLeagues(req, res, next) {
    let name
    if(req.query.name) name = req.query.name.toLowerCase()
    footballDb
        .getLeagues()
        .then(leagues => {
            leagues = leagues
                .filter(l => !name || l.caption.toLowerCase().indexOf(name) >= 0)
                .map(addLeaguePath)
            res.render('football/leagues', leagues)
        })
        .catch(err => next(err))

    function addLeaguePath(league) {
        league.leagueHref = '/router/leagues/' + league.id + '/table'
        return league
    }
}

function index(req, res, next) {
    res.redirect('/router/leagues')
}
