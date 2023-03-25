<div class="text-center col-md-3 mb-4">
    <div class="col-md-12 card" id="game">
        <h2>
            ${game.name}
        </h2>
        <div class="scrollbar" id="desc">
            <p>
                ${game.description}
            </p>
        </div>
        <p>
            <div class="btn-group" role="group" aria-label="Button Group">
                <a href="/categories?ids=${game.categories}" class="btn btn-dark" role="button">Categories</a>
                <a href="/games/${game.id}/artists" class="btn btn-dark" role="button">Artists</a>
            </div>
        </p>
    </div>
</div>