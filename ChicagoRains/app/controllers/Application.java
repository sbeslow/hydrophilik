package controllers;

import com.hydrophilik.javaCrons.utils.Config;
import play.*;
import play.mvc.*;

public class Application extends Controller {

    public static Config config;

    public static Result index() {
        return ok(views.html.index.render());
    }

    public static Result splash() {
        return ok(views.html.splash.render());
    }
}
