package tests;

import base.BaseTest;
import io.qameta.allure.*;
import models.RandomImageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ImagesService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomImageTest extends BaseTest {

    ImagesService service = new ImagesService();

    @Test
    @Story("Buscar imagem aleatória")
    @DisplayName("Deve retornar uma imagem aleatória válida")
    @Description("Valida que o endpoint de imagem aleatória retorna uma resposta com status 'success' e uma URL de imagem válida")
    public void validarImagemAleatoria() {
        io.restassured.response.Response responseStatus = service.imagemAleatoria();
        RandomImageResponse response = responseStatus.as(RandomImageResponse.class);

        Allure.step("Validar status HTTP 200 OK", () -> {
            assertEquals(200, responseStatus.getStatusCode(), "Esperado status 200 para imagem aleatória");
            Allure.addAttachment("Status Code", "text/plain", String.valueOf(responseStatus.getStatusCode()), "txt");
        });

        Allure.step("Validar resposta e status", () -> {
            assertAll("Validações da resposta",
                    () -> assertNotNull(response, "Response não pode ser nulo"),
                    () -> assertEquals("success", response.getStatus(), "Status deve ser 'success'")
            );Allure.addAttachment("Response JSON", "application/json", response.toString(), "json");
        });

        Allure.step("Validar URL da imagem", () -> {
            String message = response.getMessage();
            assertAll("Validações da URL",
                    () -> assertTrue(message != null && !message.isEmpty(), "Mensagem não deve ser nula ou vazia"),
                    () -> assertTrue(message.startsWith("https://"), "URL deve começar com 'https://'"),
                    () -> assertTrue(message.matches("https://.*\\.(jpg|jpeg|png|gif|webp)"), "URL deve apontar para um arquivo de imagem válido")
            );
        });
    }


    @Test
    @Story("Validar aleatoriedade da imagem")
    @DisplayName("Deve retornar imagens diferentes em chamadas consecutivas para validar aleatoriedade")
    @Description("Valida que o endpoint de imagem aleatória retorna imagens diferentes em chamadas consecutivas, confirmando a aleatoriedade do serviço")
    public void validarAleatoriedade() {
        io.restassured.response.Response responseStatus1 = service.imagemAleatoria();
        RandomImageResponse response1 = responseStatus1.as(RandomImageResponse.class);

        io.restassured.response.Response responseStatus2 = service.imagemAleatoria();
        RandomImageResponse response2 = responseStatus2.as(RandomImageResponse.class);

        Allure.step("Validar status HTTP 200 OK para primeira chamada", () -> {
            assertEquals(200, responseStatus1.getStatusCode(), "Esperado status 200 para primeira imagem aleatória");
            Allure.addAttachment("Status Code 1", "text/plain", String.valueOf(responseStatus1.getStatusCode()), "txt");
        });

        Allure.step("Validar status HTTP 200 OK para segunda chamada", () -> {
            assertEquals(200, responseStatus2.getStatusCode(), "Esperado status 200 para segunda imagem aleatória");
            Allure.addAttachment("Status Code 2", "text/plain", String.valueOf(responseStatus2.getStatusCode()), "txt");
        });

        Allure.step("Validar aleatoriedade das imagens", () -> {
            String img1 = response1.getMessage();
            String img2 = response2.getMessage();

            assertAll("Validações de aleatoriedade",
                    () -> assertNotNull(img1, "Primeira imagem não pode ser nula"),
                    () -> assertNotNull(img2, "Segunda imagem não pode ser nula"),
                    () -> assertNotEquals(img1, img2, "As imagens retornadas devem ser diferentes para validar aleatoriedade")
            );
        });
    }


    @Test
    @Story("Validar resposta para parâmetros extras")
    @DisplayName("Deve retornar uma imagem aleatória mesmo quando parâmetros extras são fornecidos, ignorando-os corretamente")
    @Description("Valida que o endpoint de imagem aleatória retorna uma resposta válida mesmo quando parâmetros extras são fornecidos")
    public void validarQueryParamsExtras() {
        io.restassured.response.Response responseStatus = service.imagemAleatoriaComParams("test", "123");
        RandomImageResponse response = responseStatus.as(RandomImageResponse.class);

        Allure.step("Validar status HTTP 200 OK", () -> {
            assertEquals(200, responseStatus.getStatusCode(), "Esperado status 200 mesmo com parâmetros extras");
            Allure.addAttachment("Status Code", "text/plain", String.valueOf(responseStatus.getStatusCode()), "txt");
        });

        Allure.step("Validar resposta e status", () -> {
            assertAll("Validações da resposta",
                    () -> assertNotNull(response, "Response não pode ser nulo"),
                    () -> assertEquals("success", response.getStatus(), "Status deve ser 'success'")
            );
            Allure.addAttachment("Response JSON", "application/json", response.toString(), "json");
        });

        Allure.step("Validar URL da imagem", () -> {
            String message = response.getMessage();
            assertAll("Validações da URL",
                    () -> assertTrue(message != null && !message.isEmpty(), "Mensagem não deve ser nula ou vazia"),
                    () -> assertTrue(message.startsWith("https://"), "URL deve começar com 'https://'"),
                    () -> assertTrue(message.matches("https://.*\\.(jpg|jpeg|png|gif|webp)"), "URL deve apontar para um arquivo de imagem válido")
            );
        });
    }

}
