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
        RandomImageResponse response = service.imagemAleatoria()
                .then()
                .statusCode(200)
                .extract()
                .as(RandomImageResponse.class);

        assertEquals("success", response.getStatus(), "Status deve ser 'success'");

        String message = response.getMessage();
        assertTrue(message != null && !message.isEmpty(), "Mensagem não deve ser nula ou vazia");
        assertTrue(message.startsWith("https://"), "URL deve começar com 'https://'");
        assertTrue(message.matches("https://.*\\.(jpg|jpeg|png|gif|webp)"),
                "URL deve apontar para um arquivo de imagem válido");
    }

    @Test
    @Story("Validar aleatoriedade da imagem")
    @DisplayName("Deve retornar imagens diferentes em chamadas consecutivas para validar aleatoriedade")
    @Description("Valida que o endpoint de imagem aleatória retorna imagens diferentes em chamadas consecutivas, confirmando a aleatoriedade do serviço")
    public void validarAleatoriedade() {
        RandomImageResponse response1 = service.imagemAleatoria()
                .then()
                .statusCode(200)
                .extract()
                .as(RandomImageResponse.class);

        RandomImageResponse response2 = service.imagemAleatoria()
                .then()
                .statusCode(200)
                .extract()
                .as(RandomImageResponse.class);

        String img1 = response1.getMessage();
        String img2 = response2.getMessage();

        assertNotEquals(img1, img2, "As imagens retornadas devem ser diferentes para validar aleatoriedade");
    }

    @Test
    @Story("Validar resposta para parâmetros extras")
    @DisplayName("Deve retornar uma imagem aleatória mesmo quando parâmetros extras são fornecidos, ignorando-os corretamente")
    @Description("Valida que o endpoint de imagem aleatória retorna uma resposta válida mesmo quando parâmetros extras são fornecidos")
    public void validarQueryParamsExtras() {
        RandomImageResponse response = service.imagemAleatoriaComParams("test", "123")
                .then()
                .statusCode(200)
                .extract()
                .as(RandomImageResponse.class);

        assertEquals("success", response.getStatus(), "Status deve ser 'success'");
        assertTrue(response.getMessage().contains("https://"), "URL deve conter 'https://'");
    }
}
