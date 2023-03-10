package org.example;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class MyVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.get("/test").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext routingContext) {
                List<String> names = routingContext.queryParam("name");
                List<String> titles = routingContext.queryParam("title");
                routingContext.response().end("Hi! " + names + "\t" + titles);
            }
        });

        server.requestHandler(router).listen(8080);
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        //vertx.deployVerticle("org.example.MyVerticle",new DeploymentOptions().setInstances(4));
        vertx.deployVerticle(new MyVerticle());
    }
}
