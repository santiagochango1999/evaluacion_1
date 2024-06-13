package com.distribuida;

import com.distribuida.db.Book;
import com.distribuida.service.IBookService;
import com.google.gson.Gson;
import io.helidon.webserver.WebServer;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;

import java.awt.*;
import java.util.List;


public class Main {

    static IBookService bookS;
    static ContainerLifecycle lifecycle=null;
    static Gson gson=new Gson();

    public static void main(String[] args) {

        lifecycle= WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);

        bookS = CDI.current().select(IBookService.class).get();

        WebServer server = WebServer.builder().port(8081).routing(aux -> aux.get(
                "/libros",(req,res)->
                {
                    List<Book> books= bookS.buscarLibros();
                    res.send(gson.toJson(books));
                })
                .get("/libros/{id}", (req, res) -> {
                    Book book = bookS.buscarId(Integer.valueOf(req.path().pathParameters().get("id")));
                    res.send(gson.toJson(book));
                })
                .post("/libros", (req, res) -> {
                    bookS.insertar(gson.fromJson(req.content().as(String.class), Book.class));
                })
                .delete("/libros/{id}",(req, res) -> {
                    bookS.eliminar(Integer.valueOf(req.path().pathParameters().get("id")));
                    res.send("Eliminado");
                })
                .put("/libros/{id}",(req, res) -> {
                    Book book = gson.fromJson(req.content().as(String.class), Book.class);
                    book.setId(Integer.valueOf(req.path().pathParameters().get("id")));
                    bookS.modificar(book);
                    res.send("actualizado");
                })).build();
        server.start();
        bookS.buscarLibros().stream().forEach(System.out::println);
        shut();

    }

    public static void shut(){
        lifecycle.stopApplication(null);
    }


}