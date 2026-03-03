package tests;

import base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.BreedsListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.BreedsService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Dog API")
@Feature("Listar Raças")
@Owner("Rafael Missio")
@Severity(SeverityLevel.NORMAL)
public class BreedsListTest extends BaseTest {

    BreedsService service = new BreedsService();

    @Test
    @Story("Listar todas as raças")
    @DisplayName("Deve listar todas as raças disponíveis")
    @Description("Valida que o endpoint retorna uma lista de raças com a estrutura correta e dados esperados")
    public void validarListaDeRacas() {
        Response responseStatus = service.listarTodasAsRacas();
        BreedsListResponse response = responseStatus.as(BreedsListResponse.class);

        Allure.step("Validar status HTTP 200 OK", () -> {
            assertEquals(200, responseStatus.getStatusCode(), "Esperado status 200 para listar raças");
            Allure.addAttachment("Status Code", "text/plain", String.valueOf(responseStatus.getStatusCode()), "txt");
        });

        Allure.step("Validar resposta e estrutura básica da lista de raças", () -> {
            assertAll("Validações da lista de raças",
                    () -> assertNotNull(response, "Response não pode ser nulo"),
                    () -> assertNotNull(response.getStatus(), "Status não pode ser nulo"),
                    () -> assertEquals("success", response.getStatus(), "Status esperado: 'success'"),
                    () -> assertNotNull(response.getMessage(), "Message não pode ser nulo"),
                    () -> assertFalse(response.getMessage().isEmpty(), "Message não deve estar vazia"),
                    () -> assertTrue(response.getMessage().keySet().stream().allMatch(k -> k instanceof String), "Todas as chaves devem ser String"),
                    () -> assertTrue(response.getMessage().containsKey("hound"), "Deve conter a chave 'hound'")
            );Allure.addAttachment("Response JSON", "application/json", response.toString(), "json");
        });
    }



    @Test
    @Story("Validar estrutura do JSON de raças")
    @DisplayName("Deve validar a estrutura do JSON retornado para a lista de raças")
    @Description("Valida que a estrutura do JSON retornado pelo endpoint de listar raças segue o formato esperado")
    public void validarEstruturaDoJson_service() {
        BreedsListResponse response = service.listarTodasAsRacas().as(BreedsListResponse.class);

        Allure.step("Validar resposta e estrutura básica do JSON", () -> {
            assertAll("Validar estrutura básica do JSON",
                    () -> assertNotNull(response, "Response não pode ser nulo"),
                    () -> assertNotNull(response.getStatus(), "Status não pode ser nulo"),
                    () -> assertEquals("success", response.getStatus(), "Status esperado: 'success'")
            );Allure.addAttachment("Response JSON", "application/json", response.toString(), "json");
        });

        Allure.step("Validar estrutura do Map de raças", () -> {
            Map<?,?> message = assertInstanceOf(Map.class, response.getMessage(), "Message deve ser um Map");
            assertAll("Validações do Map",
                    () -> assertFalse(message.isEmpty(), "Message não deve estar vazio"),
                    () -> assertTrue(message.keySet().stream().allMatch(k -> k instanceof String), "Todas as chaves devem ser String")
            );
        });

        Allure.step("Validar valor da raça 'hound'", () -> {
            Map<?,?> message = assertInstanceOf(Map.class, response.getMessage(), "Message deve ser um Map");
            if (message.containsKey("hound")) {
                Object val = message.get("hound");
                assertAll("Validações do valor 'hound'",
                        () -> assertNotNull(val, "Valor de 'hound' não pode ser nulo"),
                        () -> assertTrue(
                                val instanceof java.util.Collection || val instanceof String || val.getClass().isArray(),
                                "Valor de 'hound' deve ser uma coleção, string ou array"
                        )
                );
            }
        });
    }

}