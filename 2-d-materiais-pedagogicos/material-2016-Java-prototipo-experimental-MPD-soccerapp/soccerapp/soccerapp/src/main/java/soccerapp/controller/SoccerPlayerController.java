package soccerapp.controller;

import soccerapp.cache.Cache;
import soccerapp.domain.CacheFactory;

import javax.servlet.http.HttpServletRequest;

import static util.Utils.getIntValueFromRequestUri;

public class SoccerPlayerController extends SoccerController {

    private Cache cache = CacheFactory.get(SoccerPlayerController.class);

    @Override
    public void close() throws Exception {
        super.close();
    }

    public String getPlayers(HttpServletRequest req) {
        log.info("SoccerController - getPlayers");

        return cache.getOrDefault(req.getRequestURI(), () -> service.getPlayers(getIntValueFromRequestUri(req))
                .handleAsync((playersList, throwable) ->
                        applyToView(playersList,teamPlayersView,throwable)));
    }
}
