package services;

import core.Endpoints;
import core.RestClient;
import io.restassured.response.Response;

public class ImagesService {

    public Response imagensPorRaca(String breed) {
        return RestClient.get(String.format(Endpoints.BREED_IMAGES, breed));
    }

    public Response imagensPorSubraca(String breed, String subBreed) {
        return RestClient.get(String.format(Endpoints.BREED_SUBRACA_IMAGES, breed, subBreed));
    }

    public Response imagemAleatoria() {
        return RestClient.get(Endpoints.RANDOM_IMAGE);
    }

    public Response imagemAleatoriaComParams(String breed, String subBreed) {
        return RestClient.get(String.format(Endpoints.RANDOM_PARANS_IMAGE, breed, subBreed));
    }



}