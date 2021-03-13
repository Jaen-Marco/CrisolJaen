package com.example.crisol_Jaen.culqi;

import org.json.JSONObject;

public interface TokenCallback {

    public void onSuccess(JSONObject token);

    public void onError(Exception error);
}
