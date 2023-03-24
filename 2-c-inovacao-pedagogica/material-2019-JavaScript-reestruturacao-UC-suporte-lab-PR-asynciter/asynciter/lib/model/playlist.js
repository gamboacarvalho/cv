/**
 * @type module:track
 */
import Track from '../model/track'
/**
 * Representing a users's playlist with a sequence of tracks.
 */
export default class Paylist {
    /**
     * @param {String} username - Username of the playlist owner. 
     * @param {String} name - name of this playlist. Should be unique within this user.
     * @param {String} desc - description for the playlist.
     * @param  {AsyncIterable<Track>}
     */
    constructor(username, name, desc, tracks) {
        this.username = username
        this.name = name
        this.desc = desc
        this.tracks = tracks
    }
    /**
     * NOT HERE !!!! tracks should be lazy and readonly.
     * => Inserts/Updates via DAO.
     * @param {*} track 
     */
    // addTrack(track) {}
}
