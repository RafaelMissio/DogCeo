package tests;

import base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.BreedImagesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ImagesService;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BreedImagesTest extends BaseTest {

    ImagesService service = new ImagesService();

    @Test
    @Story("Buscar imagens de raça válida")
    @DisplayName("Deve retornar imagens para raça válida")
    @Description("Valida que o endpoint retorna uma lista de URLs de imagens para uma raça válida")
    public void validarImagensDeRacaValida() {
        Response responseStatus = service.imagensPorRaca("hound");
        BreedImagesResponse response = responseStatus.as(BreedImagesResponse.class);

        Allure.step("Validar status HTTP 200 OK", () -> {
            assertEquals(200, responseStatus.getStatusCode(), "Esperado status 200 para raça válida");
            Allure.addAttachment("Status Code", "text/plain", String.valueOf(responseStatus.getStatusCode()), "txt");
        });

        Allure.step("Validações iniciais", () -> {
            assertAll("Validações iniciais da resposta",
                    () -> assertNotNull(response, "Response não pode ser nulo"),
                    () -> assertEquals("success", response.getStatus(), "Status esperado: 'success'"),
                    () -> assertNotNull(response.getMessage(), "Message não pode ser nulo"),
                    () -> assertFalse(response.getMessage().isEmpty(), "Message não pode estar vazia"),
                    () -> assertTrue(response.getMessage().stream().allMatch(Objects::nonNull), "Nenhuma URL deve ser nula"),
                    () -> assertTrue(response.getMessage().stream().noneMatch(s -> s == null || s.trim().isEmpty()), "Nenhuma URL deve ser nula ou vazia")
            );
            Allure.addAttachment("Response JSON", "application/json", response.toString(), "json");
        });

        Allure.step("Validar URLs", () -> {
            response.getMessage().forEach(url -> assertAll("Validação da URL: " + url,
                    () -> assertFalse(url.trim().isEmpty(), "URL não pode ser vazia"),
                    () -> assertTrue(url.startsWith("https://images.dog.ceo/"), "URL deve iniciar com https://images.dog.ceo/"),
                    () -> assertTrue(url.toLowerCase().matches(".*\\.(jpg|jpeg)$"), "URL deve terminar com .jpg ou .jpeg"),
                    () -> assertDoesNotThrow(() -> new java.net.URI(url), "URL deve ser sintaticamente válida")
            ));
        });
    }

    @Test
    @Story("Erro ao buscar imagens de raça inexistente")
    @DisplayName("Deve retornar erro 404 para raça inexistente")
    @Description("Valida que o endpoint retorna um erro apropriado quando uma raça inexistente é solicitada")
    public void validarErroAoBuscarImagensDeRacaInexistente() {
        Response response = service.imagensPorRaca("xyz");

        Allure.step("Validar resposta HTTP para raça inexistente", () -> {
            String mensagem = response.jsonPath().getString("message");

            assertAll("Validações para raça inexistente",
                    () -> assertNotNull(response, "Resposta HTTP não pode ser nula"),
                    () -> assertEquals(404, response.getStatusCode(), "Esperado 404 para raça inexistente"),
                    () -> assertNotNull(mensagem, "Message não pode ser nulo"),
                    () -> assertFalse(mensagem.trim().isEmpty(), "Message não deve estar vazia"),
                    () -> assertTrue(
                            mensagem.toLowerCase().contains("not found") ||
                                    mensagem.toLowerCase().contains("not_found") ||
                                    mensagem.toLowerCase().contains("breed"),
                            "Mensagem deve indicar que a raça não foi encontrada"
                    )
            );Allure.addAttachment("Response 404", "application/json", response.asString(), "json");
        });

        Allure.step("Validar mapeamento da resposta para BreedImagesResponse", () -> {
            try {
                BreedImagesResponse mapped = response.as(BreedImagesResponse.class);
                assertEquals("error", mapped.getStatus(), "Status esperado no corpo: 'error'");
            } catch (Exception ignored) {
            }
        });
    }

    @Test
    @Story("Buscar imagens de raça com sub-raça válida")
    @DisplayName("Deve retornar imagens para raça com sub-raça válida")
    @Description("Valida que o endpoint retorna uma lista de URLs de imagens para uma raça com sub-raça válida")
    public void validarImagensDeRacaComSubraca() {
        Response responseStatus = service.imagensPorSubraca("hound", "afghan");
        BreedImagesResponse response = responseStatus.as(BreedImagesResponse.class);

        Allure.step("Validar status HTTP 200 OK", () -> {
            assertEquals(200, responseStatus.getStatusCode(), "Esperado status 200 para raça com sub-raça válida");
            Allure.addAttachment("Status Code", "text/plain", String.valueOf(responseStatus.getStatusCode()), "txt");
        });

        Allure.step("Validações iniciais da resposta para raça com sub-raça", () -> {
            assertAll("Validações iniciais da resposta para raça com sub-raça",
                    () -> assertNotNull(response, "Response não pode ser nulo"),
                    () -> assertEquals("success", response.getStatus(), "Status esperado: 'success'"),
                    () -> assertNotNull(response.getMessage(), "Message não pode ser nulo"),
                    () -> assertFalse(response.getMessage().isEmpty(), "Message não pode estar vazia"),
                    () -> assertTrue(response.getMessage().stream().allMatch(Objects::nonNull), "Nenhuma URL deve ser nula"),
                    () -> assertTrue(response.getMessage().stream().noneMatch(s -> s == null || s.trim().isEmpty()), "Nenhuma URL deve ser nula ou vazia")
            );Allure.addAttachment("Response JSON", "application/json", response.toString(), "json");
        });

        Allure.step("Validar URLs da raça com sub-raça", () -> {
            response.getMessage().forEach(url -> assertAll("Validação da URL: " + url,
                    () -> assertFalse(url.trim().isEmpty(), "URL não pode ser vazia"),
                    () -> assertTrue(url.startsWith("https://images.dog.ceo/"), "URL deve iniciar com https://images.dog.ceo/"),
                    () -> assertTrue(url.contains("/hound-afghan/"), "URL deve conter a raça e sub-raça no formato correto"),
                    () -> assertTrue(url.toLowerCase().matches(".*\\.(jpg|jpeg)$"), "URL deve terminar com .jpg ou .jpeg"),
                    () -> assertDoesNotThrow(() -> new java.net.URI(url), "URL deve ser sintaticamente válida")
            ));
        });
    }


    @Test
    @Story("Erro ao buscar imagens de raça com sub-raça inexistente")
    @DisplayName("Deve retornar erro 404 para raça com sub-raça inexistente")
    @Description("Valida que o endpoint retorna um erro apropriado quando uma sub-raça inexistente")
    public void validarErroAoBuscarImagensDeRacaComSubracaInexistente() {
        Response response = service.imagensPorSubraca("hound", "xyz");

        Allure.step("Validar resposta HTTP para sub-raça inexistente", () -> {
            String mensagem = response.jsonPath().getString("message");

            assertAll("Validações para sub-raça inexistente",
                    () -> assertNotNull(response, "Resposta HTTP não pode ser nula"),
                    () -> assertEquals(404, response.getStatusCode(), "Esperado 404 para sub-raça inexistente"),
                    () -> assertNotNull(mensagem, "Message não pode ser nulo"),
                    () -> assertFalse(mensagem.trim().isEmpty(), "Message não deve estar vazia"),
                    () -> assertTrue(
                            mensagem.toLowerCase().contains("not found") ||
                                    mensagem.toLowerCase().contains("not_found") ||
                                    mensagem.toLowerCase().contains("breed"),
                            "Mensagem deve indicar que a sub-raça não foi encontrada"
                    )
            );
            Allure.addAttachment("Response 404", "application/json", response.asString(), "json");
        });

        Allure.step("Validar mapeamento da resposta para BreedImagesResponse", () -> {
            try {
                BreedImagesResponse mapped = response.as(BreedImagesResponse.class);
                assertEquals("error", mapped.getStatus(), "Status esperado no corpo: 'error'");
            } catch (Exception ignored) {
            }
        });
    }



}
