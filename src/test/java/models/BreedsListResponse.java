package models;

import java.util.Map;

public class BreedsListResponse {

    private Map<String, String[]> message;
    private String status;

    public Map<String, String[]> getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
