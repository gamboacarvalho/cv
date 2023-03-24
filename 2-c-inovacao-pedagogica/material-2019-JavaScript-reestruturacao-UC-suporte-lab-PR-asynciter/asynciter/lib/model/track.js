/**
 * As returned by last.fm API track getinfo.
 */
export default class Track {
    /**
     * @param {String} name
     * @param {String} mbid
     * @param {String} url
     * @param {Number} duration
     * @param {Number} listeners
     * @param {Number} playcount
     * @param {Artist} artist
     */
    constructor(name, mbid, url, duration, listeners, playcount, artist) {
        this.name = name
        this.mbid = mbid
        this.url = url
        this.duration = duration
        this.listener = listeners
        this.playcount = playcount
        this.artist = artist
    }

    static from(dto) {
        return new Track(
            dto.track.name,
            dto.track.mbid,
            dto.track.url,
            dto.track.duration,
            dto.track.listeners,
            dto.track.playcount,
            dto.track.artist)
    }
}