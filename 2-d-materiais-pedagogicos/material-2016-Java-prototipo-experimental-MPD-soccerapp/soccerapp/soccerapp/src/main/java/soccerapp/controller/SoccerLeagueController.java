package soccerapp.controller;

import soccerapp.cache.Cache;
import soccerapp.domain.CacheFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

public class SoccerLeagueController extends SoccerController {

    @Override
    public void close() throws Exception {
        super.close();
    }

    private Cache cache = CacheFactory.get(SoccerLeagueController.class);

    public String getLeagues(HttpServletRequest req) throws IOException {
        log.info("SoccerController - getLeagues");

        Supplier<CompletableFuture<String>> supplier = () -> service.getLeagues()
                .thenApplyAsync(leagueStream -> leagueStream.collect(toList()))
                .handleAsync((leagueList,throwable) -> applyToView(leagueList, allLeaguesView, throwable));
        return cache.getOrDefault(req.getRequestURI(), supplier);
    }
}
