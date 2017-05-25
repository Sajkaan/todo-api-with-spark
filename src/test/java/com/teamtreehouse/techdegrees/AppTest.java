package com.teamtreehouse.techdegrees;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.testing.ApiClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;


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
        Sql2o sql2o = new Sql2o(TEST_DATASOURCE + ";INIT=RUNSCRIP from " + CLASSPATH,
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

}