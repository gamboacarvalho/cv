import * as webapi from './../lib/web/track-web-service'
import * as dao from './../lib/dao/track-dao'

test('Try to save a track that already exists!', async () => {
    const atomic = '66083e09-9486-43f7-98c4-8253c9a5da58'
    /**
     * 1. Confirm that atomic track is in DAO
     */
    const track = await dao.findById(atomic)
    expect(track.mbid).toBe(atomic)
    /**
     * 2. Trying to insert the same track will throw an Error
     */
    try {
        await dao.insert(track)
    } catch (err) {
        expect(err).toBeTruthy()
        return
    }
    throw Error('The insert operation succeeded but it should fail!')
})

test('Save Starlight track locally, read it and delete it', async () => {
    const starlight = '60e94685-0481-4d3d-bd84-11c389d9b2a5'
    /**
     * 1. Look for starlight track in Dao and it will be not found
     */
    try{
        await dao.findById(starlight)
    } catch(err) {
        expect(err).toBeTruthy()
        /**
         * 2. Fetch starlight track from the webapi
         */
        const track = await webapi.getById(starlight)
        expect(track.name).toEqual('Starlight')
        /**
         * 3. Insert it in Dao 
         */
        await dao.insert(track)
        /**
         * 4. Lookup again for starlight and now it will find the track.
         */
        const expected = await dao.findById(starlight)
        expect(expected.name).toEqual('Starlight')
        /**
         * 5. Remove the track and then it can not find it again.
         */ 
        await dao.remove(starlight)
        try{
            await dao.findById(starlight)
        } catch(err) {
            expect(err).toBeTruthy()
        }
        return
    }
    throw Error('The first find operation succeeded unexpectedly!')
})