import * as dao from './../lib/dao/track-dao'
import * as repo from './../lib/repo/track-repo'

test('Get Atomic track from Repository via Dao', async () => {
    const atomic = '66083e09-9486-43f7-98c4-8253c9a5da58'
    const track = await repo.findById(atomic)
    expect(track.name).toEqual('Atomic')
})

test('Getting illegal mbid from Repository raises an error',  async () => {
    const atomic = 'lksajljdfl-adlfadlçj'
    try {
        await repo.findById(atomic)    
    } catch (err) {
        expect(err).toBeTruthy()   
        return
    }
    throw Error('The first find operation succeeded unexpectedly!')
})

test('Get Starlight from Repository, check it via Dao but fetch it from the Web',  async () => {
    const starlight = '60e94685-0481-4d3d-bd84-11c389d9b2a5'
    /**
     * 1. Confirm that DB is empty and no track is returned.
     */
    try{
        await dao.findById(starlight)
    } catch(err) {
        expect(err).toBeTruthy()
        /**
         * 2. Repo tries to get Track and because it cannot find it,
         * then repo will fetch the Track from the Web API and insert it on DB.
         */
        let track = await repo.findById(starlight)
        expect(track.name).toEqual('Starlight')
        /**
         * 3. After that we can get it sucessfuly via Dao.
         */
        track = await dao.findById(starlight)
        expect(track.name).toEqual('Starlight')
        /**
         * 4. Remove the track and then it can not find it again.
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