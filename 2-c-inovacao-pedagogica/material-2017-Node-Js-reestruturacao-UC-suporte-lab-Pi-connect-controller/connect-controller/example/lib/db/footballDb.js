'use strict'

module.exports = (function(){
    /**
     * Import npm modules
     */
    const fs = require('fs')
    const fetch = require('node-fetch')
    /**
     * Constants
     */
    const FOOTBALL_HOST = 'http://api.football-data.org'
    const FOOTBALL_PATH = '/v1/'
    const FOOTBALL_CREDENTIALS = loadCredentials(__dirname +  '/footballCredentials.js')
    /**
     * footballDb module API
     */
    return {
        getLeagues,
        getLeaguesIdTable,
        getTeam,
    }

    function getLeagues() {
        const path = FOOTBALL_HOST + FOOTBALL_PATH + '/soccerseasons'
        const options = { 'headers': FOOTBALL_CREDENTIALS }
        return fetch(path, options)
            .then(res => res.json())
            .then(arr => {
                if(arr.error) throw new Error("No leagues !!!! You probably reached your request limit. Get your free API token from http://api.football-data.org/!!! --------- Message from football-data.org:" + arr.error)
                return arr.map(item => new League(item))
            })
    }

    function getLeaguesIdTable(id) {
        const path =  FOOTBALL_HOST + FOOTBALL_PATH + '/soccerseasons/' + id + '/leagueTable'
        const options = { 'headers': FOOTBALL_CREDENTIALS }
        return fetch(path, options)
            .then(res => res.json())
            .then(obj => {
                if(obj.error) throw new Error("There is no League with id = " + id)
                if(!obj.standing) throw new Error("There is no Table for id = " + id)
                return new LeagueTable(id, obj)
            })
    }

    function getTeam(teamId) {
        const path = FOOTBALL_HOST + FOOTBALL_PATH + 'teams/' + teamId
        const options = { 'headers': FOOTBALL_CREDENTIALS }
        return fetch(path, options)
            .then(res => res.json())
            .then(data => new Team(data, teamId))
    }

    /**
     * Domain Entity -- League 
     */
    function League(obj) {
        this.id = obj.id
        this.caption = obj.caption
        this.year = obj.year
    }

    /**
     * Domain Entity -- LeagueTable 
     */
    function LeagueTable(id, obj) {
        this.id = id
        this.caption = obj.leagueCaption
        this.teams  = obj.standing.map(std => new Team(std))
    }

    /**
     * Domain Entity -- Team 
     */
    function Team(obj, teamId) {
        return teamId
            ? initTeam(this, teamId, obj)
            : initTeamForTable(this, obj)
    }

    function initTeam(self, teamId, obj) {
        self.id = teamId
        self.name = obj.name
        self.code = obj.code
        self.shortName = obj.shortName
        self.squadMarketValue = obj.squadMarketValue 
        self.crestUrl = obj.crestUrl
    }

    function initTeamForTable(self, obj) {
        const path = obj._links.team.href.split('/')
        self.id = path[path.length - 1]
        self.position = obj.position
        self.name = obj.teamName
        self.points = obj.points
        self.goals = obj.goals
        self.players = []
    }


    /**
     * Utility auxiliary function -- loadCredentials
     */
    function loadCredentials(file) {
        /**
         * Change it if you find a better way of synchronously 
         * check whether a file exists, or not !!!!!!
         */
        try {
            const stats = fs.statSync(file);
            // it existsSync
            return require(file)
        }
        catch(err) {
            // it does not existsSync
            return undefined
        } 
    }
})()