package com.app.codesmakers.api.exceptions;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorCodes {
    public static final int NO_ERROR = 200;
    public static final int ERROR_CODE_400 = 400;


    public static String getError(String json) {
        String errorMessage = "";

        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("error")) {
                try {
                    JSONObject erroObj = jsonObject.getJSONObject("error");
                    errorMessage = erroObj.getString("message");

                    Log.e("excc", "error"+errorMessage);
                } catch (Exception e) {
                    errorMessage = jsonObject.getString("error");
                    Log.e("excc", "e"+e.getLocalizedMessage());

                }
            } else {
            }

        } catch (JSONException ex) {
            Log.e("excc", "ex"+ex.getLocalizedMessage());

        }

        return errorMessage;
    }
}
