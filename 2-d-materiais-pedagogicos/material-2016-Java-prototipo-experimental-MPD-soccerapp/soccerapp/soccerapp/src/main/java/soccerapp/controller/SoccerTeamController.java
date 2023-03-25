package soccerapp.controller;

import soccerapp.cache.Cache;
import soccerapp.domain.CacheFactory;

import javax.servlet.http.HttpServletRequest;

import static util.Utils.getTeamIdFromUri;

public class SoccerTeamController extends SoccerController {

    private Cache cache = CacheFactory.get(SoccerTeamController.class);

    @Override
    public void close() throws Exception {
        super.close();
    }

    public String getTeam(HttpServletRequest req){
        log.info("SoccerController - getTeam");
        return cache.getOrDefault(req.getRequestURI(),
                () -> service.getTeam(getTeamIdFromUri(req.getRequestURI()))
                        .handleAsync((team, throwable) -> applyToView(team, teamView, throwable)));
    }
}
