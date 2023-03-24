import * as dao from '../dao/playlist-dao'
import * as tracks from '../repo/track-repo'
/**
 * @type module:playlist
 */
import Playlist from '../model/playlist'
/**
 * @type module:track
 */
import Track from '../model/track'
import { playlistTracksPath } from '../dao/storage'
/**
 * Gets from the playlist-dao the mbids of tracks belonging to the playlist
 * given by username and plstName.
 * Gets for each mbid the detailed track information from the track-dao.
 * 
 * @async
 * @param {String} username
 * @param {String} plstName - Name of the playlist.
 * @yields {Track}
 */
export async function* getTracks(username, plstName) {
    for await (const mbid of dao.getTracks(username, plstName)) {
        if(mbid != '') {
            yield tracks.findById(mbid)
        }
    }
}

/**
 * If the playlist does not exist in Dao then creates a new one
 * and return it.
 * 
 * @param {String} username
 * @param {String} plstName - Name of the playlist.
 * @returns {Promise<Playlist>}
 */
export async function findById(username, plstName) {
    try {
        /**
         * If the list already exists then get the corresponding tracks.
         */
        const dto = await dao.findById(username, plstName)
        const tracks = getTracks(username, plstName) // don't await
        return new Playlist(dto.username, dto.name, dto.desc, tracks)
    }
    catch (e) {
        /**
         * It playlist does not exist yet, then create it and
         * returns new newbie empty playlist. 
         */
        const plst = new Playlist(username, plstName, '')
        await dao.insert(plst)
        return plst
    }
}

/**
 * @param {String} username
 * @param {String} plstName - Name of the playlist.
 * @returns {Promise<void>}
 */
export function remove(username, plstName) {
    return dao.remove(username, plstName)
}

/**
 * Following  a naif approach to run two tasks concurrently:
 * 1. Try to get and insert the track via repository
 * 2. Try to get and insert the playlist via repository
 * 
 * @param {String} username - Owner of the playlist.
 * @param {String} plstName - Name of the playlist.
 * @param {String} mbid - Music brainz identifier.
 * @returns {Promise<void>} 
 */
export function addTrack(username, plstName, mbid) {
    return addTrackConcurr(username, plstName, mbid)
}

/**
 * Runs findById(mbid) and findById(username, plstName) Concurrently. 
 */
export function addTrackConcurr(username, plstName, mbid) {
    /**
     * 1. Checks for track with mbid in Dao. If it doesn't find the track in Dao then fetch...
     */
    const ptrack = tracks.findById(mbid)
    /**
     * 2. Checks for the playlist and creates if it does not exist.
     */
    const pplst = findById(username, plstName)
    /**
     * 3. Insert track mbid in the playlist if both exist.
     */
    return ptrack
       .then(track => pplst) // After ptrack completes checks for pplst
       .then(plst => dao.addTrack(username, plstName, mbid))
}     
/**
 * Runs findById(mbid) and findById(username, plstName) Sequentially. 
 */
export async function addTrackSeq(username, plstName, mbid) {
    /**
     * 1. Checks for track with mbid in Dao. If it doesn't find the track in Dao then fetch...
     */
    await tracks.findById(mbid)
    /**
     * 2. Checks for the playlist and creates if it does not exist.
     */
    await findById(username, plstName)
    /**
     * 3. Insert track mbid in the playlist if both exist.
     */
    return dao.addTrack(username, plstName, mbid)
}
