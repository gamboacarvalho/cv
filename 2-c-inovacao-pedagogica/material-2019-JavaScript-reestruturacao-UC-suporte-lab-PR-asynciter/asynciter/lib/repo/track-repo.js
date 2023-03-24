import * as dao from '../dao/track-dao'
import * as webapi from '../web/track-web-service'
/**
 * @type module:track
 */
import Track from '../model/track'
/**
 * Check for the track with given mbid in Dao.
 * If it does not exist in Dao then it will fetch the track from the web
 * and insert it in Dao.
 * 
 * @param {mbid} mbid - Music brainz identifier.
 * @returns {Promise<Track>}
 */
export function findById(mbid) {
    return dao
        .findById(mbid)        // 1. Lookup for track with mbid
        .catch(err => webapi
            .getById(mbid)     // 2. If not found fetch it from the web 
            .then(track => dao 
                .insert(track) // 3. Then insert it in Dao
                .then(() => track) // 4. When insert completes return the track
        ))
}