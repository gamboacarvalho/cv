import * as webapi from './../lib/web/track-web-service'

test('Check for last fm API key', () => {
    const key = webapi.getLastFmApiKey('lastfmapikey.txt')
    expect(key).toBeTruthy()
})

test('Check for last fm API key on invalid file', () => {
    expect(
        () => webapi.getLastFmApiKey('unexistentfile.txt')
    ).toThrow(Error)
})

test('Get Starlight track given its mbid.', async () => {
    const track = await webapi.getById('60e94685-0481-4d3d-bd84-11c389d9b2a5')
    expect(track.name).toEqual('Starlight')
})