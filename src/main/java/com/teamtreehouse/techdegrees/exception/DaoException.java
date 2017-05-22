package com.teamtreehouse.techdegrees.exception;

public class DaoException extends Exception {

    private final Exception orginalException;

    public DaoException(Exception orginalException, String msg) {
        super(msg);
        this.orginalException = orginalException;
    }
}
