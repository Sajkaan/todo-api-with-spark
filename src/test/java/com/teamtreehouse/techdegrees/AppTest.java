package com.teamtreehouse.techdegrees;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.model.Todo;
import com.teamtreehouse.techdegrees.testing.ApiClient;
import com.teamtreehouse.techdegrees.testing.ApiResponse;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;


import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AppTest {

    public static final String PORT = "4568";
    public static final  String TEST_DATASOURCE = "jdbc:h2:mem:testing";
    public static final String CLASSPATH = "'classpath:db/init.sql'";
    private Connection connection;
    private ApiClient client;
    private Gson gson;
    private Sql2oTodoDao todoDao;


    @BeforeClass
    public static void startServer() {
        String[]args = {PORT, TEST_DATASOURCE};
        App.main(args);
    }

    @AfterClass
    public static void stopServer() {
        Spark.stop();
    }

    @Before
    public void setUp() throws Exception {
        Sql2o sql2o = new Sql2o(TEST_DATASOURCE + ";INIT=RUNSCRIPT from " + CLASSPATH,
                "", "");
        connection = sql2o.open();
        client = new ApiClient("http://localhost:" + PORT);
        todoDao = new Sql2oTodoDao(sql2o);
        gson = new Gson();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void requestTodosReturnsAllTodos() throws Exception {
        Todo todo = newTestTodo();
        Todo todo1 = newTestTodo();

        todoDao.create(todo);
        todoDao.create(todo1);

        ApiResponse res = client.request("GET", "/api/v1/todos");
        Todo[] todos = gson.fromJson(res.getBody(),Todo[].class);

        assertEquals(2, todos.length);
    }

    @Test
    public void postReturns201Status() throws Exception {
        Todo todo = newTestTodo();

        ApiResponse res = client.request("POST", "/api/v1/todos", gson.toJson(todo));

        assertEquals(201, res.getStatus());

    }

    @Test
    public void postReturnsEntry() throws Exception {
        Todo todo = newTestTodo();

        client.request("POST", "/api/v1/todos", gson.toJson(todo));

        assertEquals(1, todoDao.findAll().size());
        assertEquals("New Job", todoDao.findById(1).getName());
        assertEquals(false, todoDao.findById(1).isCompleted());

    }

    @Test
    public void savingTodosReturnsUpdatedStatus() throws Exception {
        Todo todo = newTestTodo();
        todoDao.create(todo);
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Test todo 1");
        values.put("completed", true);

        ApiResponse res = client.request("PUT",
                String.format("/api/v1/todos/%d", todo.getId()),
                gson.toJson(values));

        assertEquals(200, res.getStatus());
    }

    @Test
    public void deleteReturns204StatusAndReturnsEmptyBody() throws Exception {
        Todo todo = newTestTodo();
        todoDao.create(todo);

        ApiResponse res = client.request("DELETE", String.format("/api/v1/todos/%d", todo.getId()));

        assertEquals(204, res.getStatus());
        assertEquals(0, todoDao.findAll().size());
    }

    private Todo newTestTodo() {
        return new Todo("New Job");
    }
}