'use strict'

module.exports = {
    'groups':  function (req, res) { res.render('viewName', req.query.nr) },
    'index': function () {return 'I am index'},
    'index_teamId': function (teamId, req, res) {
        res.render('index', { 
            teamId, 
            'reqVar': req.reqVar, 
            'resLocalVar': res.locals.resLocalVar, 
            'appLocalVar': req.app.locals.appLocalVar 
        })
    }, 
    'index_id_members':  function () {return 'I am index_id_members'},
    'dummy_bastof_nr_members':  function (nr) {return nr},
    'dummy_nr_teams_xpto_str':  function (nr, req, str) {return {
        'nr': nr, 'arg1': req.query.arg1, 'arg2': req.query.arg2, 'str': str
    }},
    'xone_stuff': function(stuff, req, next) {
        next()
    },
    'post_xone': function(req) {
        return req.body.stuff
    }
}