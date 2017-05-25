package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.model.Todo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oTodoDaoTest {
    private Sql2oTodoDao dao;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        dao = new Sql2oTodoDao(sql2o);

        // Connection must be open through the entire test
        connection = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }


    @Test
    public void creatingTodoSetsId() throws Exception {
        Todo todo = newTestTodo();
        int originalTodoId = todo.getId();

        dao.create(todo);

        assertNotEquals(originalTodoId, todo.getId());
    }

    @Test
    public void noTodosReturnEmptyList() throws Exception {
        assertEquals(0, dao.findAll().size());
    }

    @Test
    public void addedTodosAreReturned() throws Exception {
        Todo todo = newTestTodo();
        Todo todo1 = newTestTodo();

        dao.create(todo);
        dao.create(todo1);

        assertEquals(2, dao.findAll().size());
    }

    @Test
    public void findByIdIsReturned() throws Exception {
        Todo originalTodo = newTestTodo();
        dao.create(originalTodo);

        Todo foundTodo = dao.findById(originalTodo.getId());

        assertEquals(foundTodo, originalTodo);
    }

    @Test
    public void updateReturnsProperTodo() throws Exception {
        Todo todo = newTestTodo();
        String newName = "Updated todo";
        boolean complete = true;

        dao.create(todo);

        todo.setName(newName);
        todo.setCompleted(complete);

        dao.update(todo);

        assertEquals(dao.findById(1).getName(), newName);
        assertEquals(dao.findById(1).isCompleted(), complete);

    }

    @Test
    public void deleteTodoRemovesTodo() throws Exception {
        Todo todo = newTestTodo();
        dao.create(todo);

        dao.delete(todo.getId());

        assertEquals(0, dao.findAll().size());

    }

    private Todo newTestTodo() {
        return new Todo("New Job");
    }


}