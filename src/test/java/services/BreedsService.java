package services;

import core.Endpoints;
import core.RestClient;
import io.restassured.response.Response;

public class BreedsService {

    public Response listarTodasAsRacas() {
        return RestClient.get(Endpoints.LIST_ALL_BREEDS);
    }
}