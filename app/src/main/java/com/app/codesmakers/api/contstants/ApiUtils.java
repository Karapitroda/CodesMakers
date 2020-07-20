package com.app.codesmakers.api.contstants;

import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.utils.session.SessionManager;

import java.util.HashMap;
import java.util.Map;

import static com.app.codesmakers.api.contstants.Params.*;

public class ApiUtils {

    public static Map<String, Object> getConfigrationsMap() {
        Map<String, Object> objectMap = new HashMap<>();
        ConfigurationRequest request = SessionManager.getInstallationResquest();
        if (request == null) {
            request = new ConfigurationRequest();
        }
        objectMap.put(FIELD_API_KEY, request.getApiKey());
        objectMap.put(FIELD_APP_MODEL_TYPE, request.getAppModelType());
        objectMap.put(FIELD_APP_MODEL_VERSION, request.getAppModelVersion());
        objectMap.put(FIELD_DEVICE_ID, request.getDeviceID());
        objectMap.put(FIELD_DEVICE_TYPE, request.getDeviceType());
        objectMap.put(FIELD_OS_PLAYER_ID, request.getOSPlayerID());
        objectMap.put(FIELD_USER_ID, request.getUserID());
        objectMap.put(FIELD_USER_LOCATION, request.getUserLocation());

        return objectMap;
    }
}
