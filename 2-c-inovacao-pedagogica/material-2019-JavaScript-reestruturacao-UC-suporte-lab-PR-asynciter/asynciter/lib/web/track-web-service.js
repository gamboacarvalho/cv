import request from 'request-promise'
import fs from 'fs'
import path from 'path'
/**
 * @type module:track
 */
import Track from '../model/track'

const API_KEY = getLastFmApiKey('lastfmapikey.txt')
const HOST = 'http://ws.audioscrobbler.com/2.0'
const PATH = '/?method=track.getInfo&format=json'

/**
 * @param {String} mbid 
 * @returns {Promise<Track>} 
 */
export function getById(mbid) {
    return request(url(mbid))
        .then(body => {
            const dto = JSON.parse(body)
            if(dto.error)
                throw Error(dto.error.message)
            return Track.from(dto)
        })
}

export function url(mbid) {
    return `${HOST}${PATH}&api_key=${API_KEY}&mbid=${mbid}`
}

export function getLastFmApiKey(file) {
    try {
        const uri = path.join(process.cwd(), file)
        return fs.readFileSync(uri).toString()
    } catch (error) {
        throw Error('You should provide the last.fm API key in root file lastfmapikey.txt')
    }
}