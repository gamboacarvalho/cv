import fs from 'fs'
import * as repo from '../lib/repo/playlist-repo'
import * as daoTracks from '../lib/dao/track-dao'
import { playlistPath, playlistTracksPath, trackPath } from './../lib/dao/storage'

const electric = {
    '89f3b3a8-a63f-4d62-a9a5-fe7b740181d0': {
        name: 'Enjoy the Silence',
        duration: '406000',
        artist: {name: 'Depeche Mode'},
        album: {title: 'Violator'}
    },
    '577c95ad-ef66-4f19-9b20-19582af99d99': {
        name: 'Blue Monday',
        duration: '248000',
        artist: {name: 'New Order'},
        album: {title: 'Substance 1987'}
    },
    '66083e09-9486-43f7-98c4-8253c9a5da58': {
        name: 'Atomic',
        duration: '275000',
        artist: {name: 'Blondie'},
        album: {title: 'The Best of Blondie'}
    }
}

it('Check tracks from gamboa electric playlist', async () => {
    const plst = await repo.findById('gamboa', 'electric')
    let count = 0
    for await(const track of plst.tracks) {
        const expected = electric[track.mbid]
        expect(expected.name).toEqual(track.name)
        expect(expected.duration).toEqual(track.duration)
        expect(expected.artist.name).toEqual(track.artist.name)
        expect(expected.album.title).toEqual(track.album.title)
        count++    
    }
    expect(count).toEqual(3)
})

it('Insert a new track into a new playlist CONCURRENTLY fetching track and creating the playlist', async () => {
    await testAddTrack(repo.addTrack)
})
it('Insert a new track into a new playlist SEQUENTIALLY fetching track and creating the playlist', async () => {
    await testAddTrack(repo.addTrackSeq)
})
async function testAddTrack(addTrack) {
    /**
     * Black track from Pearl Jam
     */
    const black = '8821d2ea-2854-44fc-b14f-007b507da034'
    await addTrack('gamboa', 'grunge', black)
    const tracks = repo.getTracks('gamboa', 'grunge')
    const t = await tracks.next()
    expect(t.value.name).toEqual('Black')
    expect(fs.existsSync(playlistPath('gamboa', 'grunge'))).toBeTruthy()
    expect(fs.existsSync(playlistTracksPath('gamboa', 'grunge'))).toBeTruthy()
    await repo.remove('gamboa', 'grunge')
    expect(fs.existsSync(playlistPath('gamboa', 'grunge'))).not.toBeTruthy()
    expect(fs.existsSync(playlistTracksPath('gamboa', 'grunge'))).not.toBeTruthy()
    await daoTracks.remove(black)
    expect(fs.existsSync(trackPath(black))).not.toBeTruthy()
}