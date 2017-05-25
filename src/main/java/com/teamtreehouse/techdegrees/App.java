package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        String datasource = "jdbc:h2:~/todos.db";
        String classpath = "classpath:db/init.sql";
        if (args.length > 0) {
            if (args.length != 2) {
                System.out.println("java Api <port> <data>");
                System.exit(0);
            }
            port(Integer.parseInt(args[0]));
            datasource = args[1];
        }
        Sql2o sql2o = new Sql2o(
                String.format("%s;INIT=RUNSCRIPT from '%s'", datasource, classpath),
                "", "");
        TodoDao todoDao = new Sql2oTodoDao(sql2o);
        Gson gson = new Gson();


        get("/api/v1/todos", "application/json", (req, res) -> todoDao.findAll(), gson::toJson);

        post("/api/v1/todos", "application/json", (req, res) -> {
            Todo todo = gson.fromJson(req.body() , Todo.class);
            todoDao.create(todo);
            res.status(201);
            return todo;
        }, gson::toJson);

        put("/api/v1/todos/:id", "application/json", (req,res)-> {
            Todo todo = todoDao.findById(Integer.parseInt(req.params("id")));
            todo.setName(gson.fromJson(req.body(), Todo.class).getName());
            todo.setCompleted(gson.fromJson(req.body(), Todo.class).isCompleted());
            todoDao.update(todo);
            return todo;
        },gson::toJson);

        delete("/api/v1/todos/:id", "application/json", (req, res) -> {
            todoDao.delete(Integer.parseInt(req.params("id")));
            res.status(204);
            return null;
        },gson::toJson);

    }

}
