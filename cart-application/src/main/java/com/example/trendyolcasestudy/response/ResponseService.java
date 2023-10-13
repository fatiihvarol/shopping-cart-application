package com.example.trendyolcasestudy.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseService {
    private String message;

    private boolean result;
    public ResponseEntity<Map<String, Object>> createApiResponse(boolean result, String message) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("result", result);
        responseData.put("message", message);

        return ResponseEntity.ok(responseData);
    }

    public String getMessage() {
        return message;
    }

    public boolean isResult() {
        return result;
    }
}
