package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exception.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.sql.SQLException;
import java.util.List;

public class Sql2oTodoDao implements TodoDao {

    private final Sql2o sql2o;

    public Sql2oTodoDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void create(Todo todo) throws DaoException {
        String sql = "INSERT INTO todos (name) VALUES (:name)";
        try (Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .bind(todo)
                    .executeUpdate()
                    .getKey();
            todo.setId(id);
        } catch (Sql2oException ex) {
            throw new DaoException(ex, "Problem with creating todo.");
        }

    }

    @Override
    public void update(Todo todo) throws DaoException {
        String sql = "UPDATE todos SET name = :name, completed = :completed WHERE id = :id";
        try(Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("name", todo.getName())
                    .addParameter("id", todo.getId())
                    .addParameter("completed", todo.isCompleted())
                    .executeUpdate();
        } catch (Sql2oException ex) {
            throw new DaoException(ex,"Problem with updating.");
        }
    }

    @Override
    public List<Todo> findAll() {
        try (Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM todos")
                    .executeAndFetch(Todo.class);
        }
    }

    @Override
    public void delete(int id) throws DaoException {
        String sql = "DELETE FROM todos WHERE id = :id";
        try(Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            throw new DaoException(ex,"Problem with deleting.");
        }
    }

    @Override
    public Todo findById(int id) {
        String sql = "SELECT * FROM todos WHERE id = :id";
        try (Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Todo.class);
        }
    }
}
