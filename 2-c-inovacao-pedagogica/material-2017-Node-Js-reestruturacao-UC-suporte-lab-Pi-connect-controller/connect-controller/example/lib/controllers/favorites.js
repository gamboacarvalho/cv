'use strict'

const footballDb = require('./../db/footballDb')

module.exports = {
    /**
     * Binds to /favorites/favItem/:teamId
     * @param teamId Route parameter with the id of selected team.
     */
    'put_favItem_teamId': function(teamId, req) {
        const favoritesList = req.app.locals.favoritesList
        if(favoritesList.find(t => t.id == teamId)) {
            const err = new Error('Team already exists in favorites!')
            err.status = 409
            throw err
        }
        return footballDb
            .getTeam(teamId)
            .then(team => {
                favoritesList.push(team)
                return {
                    id: teamId,
                    name: team.name,
                    layout: false
                }
            })
    },

    /**
     * This is a synchronous action that does not do any IO.
     * Simply returning with NO exceptions just means success and
     * connect-controller will send a 200 response status.
     */
    'delete_favItem_teamId': function(teamId, req) {
        const favoritesList = req.app.locals.favoritesList
        const index = favoritesList.findIndex(t => t.id == teamId)
        if(index < 0) {
            const err = new Error('Team is not parte of Favorites!!!')
            err.status = 409
            throw err
        }
        favoritesList.splice(index, 1)
    }
}