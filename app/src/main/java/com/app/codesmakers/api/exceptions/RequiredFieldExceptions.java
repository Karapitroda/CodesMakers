package com.app.codesmakers.api.exceptions;

/**
 * Created by Bright on 9/18/2017.
 */

public class RequiredFieldExceptions extends Exception {

    public RequiredFieldExceptions(String s) {
        super(s);
    }

    public RequiredFieldExceptions() {
        super("All fields are required!");
    }

}
