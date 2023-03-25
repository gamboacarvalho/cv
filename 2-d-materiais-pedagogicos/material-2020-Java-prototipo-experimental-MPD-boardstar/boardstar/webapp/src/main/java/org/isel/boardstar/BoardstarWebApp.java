package org.isel.boardstar;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.isel.boardstar.model.Artist;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.isel.boardstar.templates.ArtistTemplate;
import org.isel.boardstar.templates.CategoryTemplate;
import org.isel.boardstar.templates.GameTemplate;
import pt.isel.mpd.util.requests.AsyncHttpRequest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static pt.isel.mpd.util.Utils.decodeParam;

public class BoardstarWebApp {

    private final static Logger LOGGER = Logger.getLogger(BoardstarWebApp.class.getName());
    private static final int PORT = 3000;
    private static final BoardstarService service = new BoardstarService(new BgaWebApi(new AsyncHttpRequest()));
    private static Configuration cfg;

    private static final long DEFAULT_GAMES_SIZE_LIMIT = 150;

    public static void main(String[] args) {

        setupTemplateConfig();
        Javalin app = Javalin.create( config -> config
                .addStaticFiles("/public")
                .addSinglePageRoot("/", "public/index.html")
        ).start(PORT);
        route(app);
    }

    private static void route(Javalin app){

        app.get("/categories", BoardstarWebApp::categoriesPageHandler);

        app.get("/categories/:id/games", BoardstarWebApp::categoryGamesPageHandler);

        app.get("/artists/:id/games", BoardstarWebApp::artistGamesPageHandler);

        app.get("/games/:id/artists", BoardstarWebApp::gameArtistsPageHandler);
    }

    /**
     * PATH: /categories
     * QUERY: name= Category Name
     *        ids= Category Ids
     **/
    private static void categoriesPageHandler(Context context) throws IOException, TemplateException {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        context.result(cf);
        PrintWriter writer = context.res.getWriter();

        String categoryName = context.queryParam("name");

        String categoryIds = context.queryParam("ids");

        LOGGER.info("Requesting Categories.");

        context.contentType("text/html");
        cfg.getTemplate("header.ftl").dump(writer);

        if(categoryName != null) {
            LOGGER.info("Category name: " + categoryName);
            searchCategoryPageHandler(categoryName, writer, cf);
            return;
        }
        if(categoryIds != null) {
            LOGGER.info("Category Ids: " + categoryIds);
            getCategoriesByIdsPageHandler(List.of(categoryIds.split(";")), writer, cf);
            return;
        }
        getAllCategoriesPageHandler(writer, cf);
    }

    /**
     * PATH: /categories/:id/games
     * QUERY: size= games limit
     **/
    private static void categoryGamesPageHandler(Context context) throws IOException, TemplateException {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        context.result(cf);
        PrintWriter writer = context.res.getWriter();

        String categoryName = decodeParam(context.pathParam("id"));
        String size = context.queryParam("size");

        LOGGER.info("Requesting Games for Category ID \"" + categoryName + "\" and size " + size);

        context.contentType("text/html");
        cfg.getTemplate("header.ftl").dump(writer);

        service
                .searchByCategoryName(categoryName, (size!=null ? Long.parseLong(size) : DEFAULT_GAMES_SIZE_LIMIT))
                .doOnSubscribe( disposable -> {
                    cfg.getTemplate("title.ftl")
                            .process(Map.of("title", categoryName), writer);
                    cfg.getTemplate("open_grid.ftl")
                            .dump(writer);
                })
                .doOnNext(game -> writeGame(game, writer))
                .doOnComplete(() -> {
                    cfg.getTemplate("footer.ftl")
                            .dump(writer);
                    cf.complete(null);
                })
                .doOnError(cf::completeExceptionally)
                .subscribe();
    }

    /**
     * PATH: /games/:id/artists
     **/
    private static void gameArtistsPageHandler(Context context) throws IOException {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        context.result(cf);
        PrintWriter writer = context.res.getWriter();

        String gameName = decodeParam(context.pathParam("id"));

        LOGGER.info("Requesting Artists for Game Name \"" + gameName + "\"");

        context.contentType("text/html");
        cfg.getTemplate("header.ftl").dump(writer);

        service
                .searchByGame(gameName)
                .flatMapObservable(Game::getArtists)
                .doOnSubscribe(disposable -> {
                    cfg.getTemplate("title.ftl")
                            .process(Map.of("title", gameName + " Artists"), writer);
                    cfg.getTemplate("open_grid.ftl")
                            .dump(writer);
                })
                .doOnNext(artist -> writeArtist(artist, writer))
                .doOnComplete(() -> {
                    cfg.getTemplate("footer.ftl")
                            .dump(writer);
                    cf.complete(null);
                })
                .doOnError(cf::completeExceptionally)
                .subscribe();
    }

    /**
     * PATH: /artists/:id/games
     **/
    private static void artistGamesPageHandler(Context context) throws IOException, TemplateException {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        context.result(cf);
        PrintWriter writer = context.res.getWriter();

        String artistName = decodeParam(context.pathParam("id"));
        String size = context.queryParam("size");

        LOGGER.info("Requesting Games for Artist Name \"" + artistName + "\" and size " + size);

        context.contentType("text/html");
        cfg.getTemplate("header.ftl").dump(writer);

        service.searchByArtist(artistName, (size!=null ? Long.parseLong(size) : DEFAULT_GAMES_SIZE_LIMIT))
                .doOnSubscribe( disposable -> {
                    cfg.getTemplate("title.ftl")
                            .process(Map.of("title",  artistName), writer);
                    cfg.getTemplate("open_grid.ftl")
                            .dump(writer);
                })
                .doOnNext(game -> writeGame(game, writer))
                .doOnComplete(() -> {
                    cfg.getTemplate("footer.ftl")
                            .dump(writer);
                    cf.complete(null);
                })
                .doOnError(cf::completeExceptionally)
                .subscribe();
    }

    private static void getAllCategoriesPageHandler(PrintWriter writer, CompletableFuture<Void> cf) throws IOException, TemplateException {
        cfg.getTemplate("title.ftl")
                .process(Map.of("title", "Categories"), writer);
        service
                .getCategories()
                .doOnSubscribe( disposable -> {
                    cfg.getTemplate("open_grid.ftl")
                            .dump(writer);
                })
                .doOnNext(category -> writeCategory(category, writer))
                .doOnComplete(() -> {
                    cfg.getTemplate("footer.ftl")
                            .dump(writer);
                    cf.complete(null);
                })
                .doOnError(cf::completeExceptionally)
                .subscribe();
    }

    private static void getCategoriesByIdsPageHandler(List<String> categoryIds, PrintWriter writer, CompletableFuture<Void> cf) throws IOException, TemplateException {
        cfg.getTemplate("title.ftl")
                .process(Map.of("title", "Categories"), writer);
        service
                .getCategoriesBy( category -> categoryIds.contains(category.getId()))
                .doOnSubscribe( disposable -> {
                    cfg.getTemplate("open_grid.ftl").dump(writer);
                })
                .doOnNext(category -> writeCategory(category, writer))
                .doOnComplete(() -> {
                    cfg.getTemplate("footer.ftl")
                            .dump(writer);
                    cf.complete(null);
                })
                .doOnError(cf::completeExceptionally)
                .subscribe();
    }

    private static void searchCategoryPageHandler(String categoryName, PrintWriter writer, CompletableFuture<Void> cf) throws IOException, TemplateException {
        cfg.getTemplate("title.ftl")
                .process(Map.of("title", "Search Results for \"" + categoryName + "\""), writer);
        service
                .getCategoriesBy( category -> category.getName().toLowerCase().contains(categoryName.toLowerCase()))
                .doOnSubscribe( disposable -> {
                    cfg.getTemplate("open_grid.ftl").dump(writer);
                })
                .doOnNext(category -> writeCategory(category, writer))
                .doOnComplete(() -> {
                    cfg.getTemplate("footer.ftl")
                            .dump(writer);
                    cf.complete(null);
                })
                .doOnError(cf::completeExceptionally)
                .subscribe();
    }

    private static void writeCategory(Category category, PrintWriter writer) throws IOException, TemplateException {
        CategoryTemplate catTemplate = new CategoryTemplate(category.getName(), category.getName());
        cfg.getTemplate("category.ftl")
                .process(Map.of("category", catTemplate), writer);
    }

    private static void writeGame(Game game, PrintWriter writer) throws IOException, TemplateException {
        game.getCategories()
                .map(Category::getId)
                .reduce((acc, curr) -> acc + ";" + curr)
                .doOnSuccess( categoryIds -> {
                    GameTemplate gameTemplate = new GameTemplate(game.getName(), game.getName(), game.getDescription(), categoryIds);
                    cfg.getTemplate("game.ftl")
                            .process(Map.of("game", gameTemplate), writer);
                })
                .subscribe();
    }

    private static void writeArtist(Artist artist, PrintWriter writer) throws IOException, TemplateException {
        ArtistTemplate artistTemplate = new ArtistTemplate( artist.getName(), artist.getName());
        cfg.getTemplate("artist.ftl")
                .process(Map.of("artist", artistTemplate), writer);
    }

    private static void setupTemplateConfig() {
        cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setDefaultEncoding("utf-8");

        try {
            File file = new File(BoardstarWebApp.class.getClassLoader().getResource("public/templates").getFile());
            cfg.setDirectoryForTemplateLoading(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
