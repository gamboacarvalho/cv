'use strict'

module.exports = {
    'groups':  function (req, res) {
        res.render('viewName', req.query.nr)
    },

    'index': function () {
        return 'I am index'
    },

    'indexTeamid': function (teamId, req, res) {
        res.render('index', { 
            teamId, 
            'reqVar': req.reqVar, 
            'resLocalVar': res.locals.resLocalVar, 
            'appLocalVar': req.app.locals.appLocalVar 
        })
    },

    'indexIdMembers':  function () {
        return 'I am index_id_members'
    },

    'dummyBastofNrMembers':  function (nr) {
        return nr
    },

    /**
     * Arguments nr and str are parsed as route parameters because
     * they are part of the method's name.
     * The rest of arguments arg1, and arg2 will be searched in 
     * req, req.query, req.body, etc...
     */
    'dummyNrTeamsXptoStr':  function (nr, req, str) {
        return {
            'nr': nr, 
            'arg1': req.query.arg1, 
            'arg2': req.query.arg2, 
            'str': str
        }
    },

    'xoneStuff': function(stuff, req, next) {
        next()
    },

    'postXone': function(req) {
        return req.body.stuff
    }
}