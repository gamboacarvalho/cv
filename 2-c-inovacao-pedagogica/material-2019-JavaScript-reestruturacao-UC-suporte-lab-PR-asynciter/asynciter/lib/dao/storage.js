import path from 'path'
/**
 * Data files location.
 */
const STORAGE = path.join(process.cwd(), 'data')
/**
 * Filename with detailed information about the playlist.
 * @param {*} username 
 * @param {String} plstName - Name of the playlist.
 */
export function playlistPath (username, plstName) {
    return path.join(STORAGE, `plst-${username}-${plstName}.json`)
}
/**
 * Name of the file listing the tracks mbids of the playlist.
 * @param {*} username 
 * @param {String} plstName - Name of the playlist.
 */
export function playlistTracksPath (username, plstName) {
    return path.join(STORAGE, `plst-${username}-${plstName}-tracks.txt`)
}
/**
 * @param {mbid} mbid - Music brainz identifier.
 */
export function trackPath(mbid) {
    return path.join(STORAGE, `track-${mbid}.json`)
}