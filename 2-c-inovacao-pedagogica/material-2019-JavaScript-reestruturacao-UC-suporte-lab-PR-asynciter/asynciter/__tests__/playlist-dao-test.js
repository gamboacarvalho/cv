import * as dao from './../lib/dao/playlist-dao'
import Playlist from './../lib/model/playlist'

const electric = [
    '66083e09-9486-43f7-98c4-8253c9a5da58',
    '89f3b3a8-a63f-4d62-a9a5-fe7b740181d0',
    '9aa2be7b-8fb0-4187-bce4-9bb4e84815fc'
]

test('Playlist insert, find and delete', async () => {
    const enterSandman = '5cbb546d-5c1c-490e-9908-761b89dd5166'
    const expected = new Playlist('gamboa', 'metal', 'All hard rock musics')
    await dao.insert(expected)
    await dao.addTrack('gamboa', 'metal', enterSandman)
    const actual = await dao.findById('gamboa', 'metal')
    expect(actual).toEqual(expected)
    await dao.remove('gamboa', 'metal')    
})

test('Add track to playlist, check it and remove it', async () => {
    const forthekill = '3d1b81ec-b87d-462c-9a50-1a587e32edb0'
    /**
     * 1. Add track forthekill to gamboa's electric playlist.
     */
    await dao.addTrack('gamboa', 'electric', forthekill)
    /**
     * 2. Expect to find all former 3 tracks and the new one forthekill
     */
    const expected = electric.concat([forthekill])
    let tracks = dao.getTracks('gamboa', 'electric')[Symbol.asyncIterator]()
    expected.forEach(async item => {
        const actual = await tracks.next()
        expect(actual.value).toEqual(item)
    })
    /**
     * 3. Remove forthekill and 9aa2be7b-8fb0-4187-bce4-9bb4e84815fc
     */
    await dao.removeTrack('gamboa', 'electric', '9aa2be7b-8fb0-4187-bce4-9bb4e84815fc')
    await dao.removeTrack('gamboa', 'electric', forthekill)
    /**
     * 4. Expect to find only 2 tracks. 
     */
    const suppressed = expected.filter(mbid => 
        mbid != '9aa2be7b-8fb0-4187-bce4-9bb4e84815fc' && 
        mbid != forthekill)
    tracks = dao.getTracks('gamboa', 'electric')[Symbol.asyncIterator]()
    suppressed.forEach(async item => {
        const actual = await tracks.next()
        expect(actual.value).toEqual(item)
    })
    /**
     * 5. Put again the mbid 9aa2be7b-8fb0-4187-bce4-9bb4e84815fc in the playlist
     */
    await dao.addTrack('gamboa', 'electric', '9aa2be7b-8fb0-4187-bce4-9bb4e84815fc')
})

test('Check for absent playlist', async () => {
    try {
        await dao.findById('gamboa', 'techno')    
    } catch (err) {
        expect(err).toBeTruthy()
        return
    }
    throw Error('The find operation succeeded unexpectedly!')
})

test('Check for tracks of absent playlist', async () => {
    try {
        for await(let track of dao.getTracks('gamboa', 'techno')) {
            // Do nothing
        }
    } catch (err) {
        expect(err).toBeTruthy()
        return
    }
    throw Error('The find operation succeeded unexpectedly!')
})