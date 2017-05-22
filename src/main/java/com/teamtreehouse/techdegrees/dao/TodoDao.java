package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exception.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;

import java.util.List;

public interface TodoDao {

    void create(Todo todo) throws DaoException;

    void update(Todo todo) throws DaoException;

    List<Todo> findAll();

    void delete(int id) throws DaoException;

    Todo findById(int id);
}
