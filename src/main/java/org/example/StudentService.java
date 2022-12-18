package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentService extends AbstractVerticle {

    private static HashMap<String, Student> data = new HashMap<>();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        //데이터 전체 조회
        router.get("/").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext routingContext) {
                ArrayList<String> arr = new ArrayList<>();
                for (String id : data.keySet()) {
                    arr.add(id);
                }
                routingContext.response().end(arr.toString());
            }
        });

        //데이터 저장
        router.post("/:id").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext routingContext) {
                String id = routingContext.pathParam("id");
                String title = routingContext.queryParam("title").get(0);
                data.put(id, new Student(id, title));
                routingContext.response().end("save success!");
            }
        });

        //데이터 조회
        router.get("/:id").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext routingContext) {
                String id = routingContext.pathParam("id");
                Student student = data.get(id);
                JsonObject obj = new JsonObject();
                obj.put("id", student.getId());
                obj.put("title", student.getTitle());

                routingContext.response().end(obj.toString());
            }
        });

        //데이터 삭제
        router.delete("/:id").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext routingContext) {
                String id = routingContext.pathParam("id");

                if(data.containsKey(id)){
                    routingContext.response().end(id + " remove OK!");
                    data.remove(id);
                }else{
                    routingContext.response().setStatusCode(404).end();
                }
            }
        });

        server.requestHandler(router).listen(8080);
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new StudentService());
    }
}
