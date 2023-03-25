require('whatwg-fetch')

window.favoritesHandler = favoritesHandler

function favoritesHandler(id, checkFavorite){
    const favoritesList = document.getElementById('favoritesList')
    const path = "/favorites/favItem/" + id
    const res = (checkFavorite.checked)
        ? addTeam(path, favoritesList)
        : removeTeam(id, path, favoritesList)
    res.catch(err => alert(err))
}

function addTeam(path, favoritesList) {
    return fetch(path,  {
            method: 'PUT',
        })
        .then(res => {
            if(res.status == 200) return res.text()
            else throw Error('Illegal operation adding team!!!')
        })
        .then(data => {
            favoritesList.innerHTML += data
        })
}

function removeTeam(id, path, favoritesList) {
    return fetch(path,  {method: 'DELETE'})
        .then(data => {
            const item = document.getElementById('favItem' + id)
            favoritesList.removeChild(item)
        })
}