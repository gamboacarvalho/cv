package soccerapp.domain;

import soccerapp.cache.domain.LeagueCache;
import soccerapp.cache.domain.LeagueStandingCache;
import soccerapp.cache.domain.PlayerCache;
import soccerapp.cache.domain.TeamCache;
import soccerapp.controller.*;
import soccerapp.cache.Cache;

import java.util.HashMap;
import java.util.Map;

public class CacheFactory {

    private static Map<Class<? extends SoccerController>, Cache> cacheFactory = new HashMap<>();

    private static final int CAPACITY = 50;

    static {
        cacheFactory.put(SoccerLeagueController.class, new LeagueCache(CAPACITY));
        cacheFactory.put(SoccerLeagueStandingController.class, new LeagueStandingCache(CAPACITY));
        cacheFactory.put(SoccerTeamController.class, new TeamCache(CAPACITY));
        cacheFactory.put(SoccerPlayerController.class, new PlayerCache(CAPACITY));
    }

    public static Cache get(Class<? extends SoccerController> klass){
        return cacheFactory.get(klass);
    }
}
