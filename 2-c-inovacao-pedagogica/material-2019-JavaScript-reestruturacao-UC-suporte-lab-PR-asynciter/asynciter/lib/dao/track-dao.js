const writeFile = require('fs').promises.writeFile
const readFile = require('fs').promises.readFile
const unlink = require('fs').promises.unlink
const F_OK = require('fs').constants.F_OK
const access = require('fs').promises.access

import { trackPath } from './storage'
/**
 * @type module:track
 */
import Track from '../model/track'

/**
 * @param {mbid} mbid - Music brainz identifier.
 * @returns {Promise<Track>}
 */
export async function findById(mbid) {
    const data = await readFile(trackPath(mbid))
    return JSON.parse(data.toString())
}

/**
 * Inserts the track only if it does not exist, otherwise throws an Error. 
 * @param {Track} track
 * @returns {Promise<void>}
 */
export async function insert(track) {
    const filename = trackPath(track.mbid)
    // Check if the file exists in the current directory.
    try{
        await access(filename, F_OK)
    } catch(err) {
        return writeFile(filename, JSON.stringify(track))
    }
    throw Error('File for that track already exists!')
}

/**
 * @param {String} mbid - Music brainz identifier.
 * @returns {Promise<void>}
 */
export function remove(mbid) {
    return unlink(trackPath(mbid))
}
