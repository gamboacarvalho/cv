package soccerapp.controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.eclipse.jetty.util.log.Logger;
import soccerapp.domain.service.SoccerService;
import util.HttpServer;

import java.io.IOException;

public class SoccerController implements AutoCloseable {

    protected final SoccerService service;
    protected final Logger log;
    protected static final Template teamView, allLeaguesView,
            leagueTableStandingView, teamPlayersView, errorView;

    static {
        try{
            Handlebars hbs = new Handlebars();
            allLeaguesView = hbs.compile("AllLeaguesTemplate");
            leagueTableStandingView = hbs.compile("LeagueTableStandingTemplate");
            teamView = hbs.compile("TeamTemplate");
            teamPlayersView = hbs.compile("TeamPlayersTemplate");
            errorView = hbs.compile("ErrorTemplate");
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public SoccerController() {
        this.service = new SoccerService();
        this.log = HttpServer.LOGGER;
    }

    @Override
    public void close() throws Exception {
        if(!service.isClosed())
            service.close();
    }

    protected  <T> String applyToView(T element, Template view, Throwable throwable) {
        return throwable != null ?
                applyToView("Devido a limitações da api, o limite de pedidos foi atingido.\n Mais info:\n\t"
                        + throwable.getMessage(), errorView)
                : applyToView(element, view);
    }

    protected  <T> String applyToView (T element, Template view){
        try{
            return view.apply(element);
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
