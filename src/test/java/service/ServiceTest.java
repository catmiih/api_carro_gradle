package service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CarroDTOInput;
import org.example.dto.CarroDTOOutput;
import org.example.service.CarroService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class ServiceTest {

    private CarroService carroService;

    @BeforeEach
    public void setup() {
        carroService = new CarroService();
    }

    // Exercício 11 - Teste de Listagem de Carros
    @Test
    @Order(1)
    public void testListagemCarros() throws Exception {
        URL url = new URL("http://localhost:4567/carros");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String responseBody = response.toString();
        System.out.println(responseBody);
        assertTrue(responseBody.contains("modelo"));
    }

    // Exercício 12 - Teste de Inserção de Carro
    @Test
    @Order(2)
    public void testInserirCarro() throws Exception {
        CarroDTOInput carroDTO = new CarroDTOInput();
        carroDTO.setId(1);
        carroDTO.setModelo("Fusca");
        carroDTO.setPlaca("ABC-1234");
        carroDTO.setChassi("XYZ123456");

        URL url = new URL("http://localhost:4567/carros");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(carroDTO);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        assertEquals(201, responseCode);
    }

    // Exercício 13 - Teste de Alteração de Carro
    @Test
    @Order(3)
    public void testAtualizarCarro() throws Exception {
        CarroDTOInput carroDTO = new CarroDTOInput();
        carroDTO.setId(1);
        carroDTO.setModelo("Fusca Atualizado");
        carroDTO.setPlaca("DEF-5678");
        carroDTO.setChassi("XYZ987654");

        URL url = new URL("http://localhost:4567/carros");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(carroDTO);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
    }

    // Exercício 14 - Teste de Busca de Carro
    @Test
    @Order(4)
    public void testBuscarCarro() throws Exception {
        int id = 1;

        URL url = new URL("http://localhost:4567/carros/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String responseBody = response.toString();
        System.out.println(responseBody);
        assertTrue(responseBody.contains("Fusca Atualizado"));
    }

    // Exercício 15 - Teste de Exclusão de Carro
    @Test
    @Order(5)
    public void testExcluirCarro() throws Exception {
        int id = 1;

        URL url = new URL("http://localhost:4567/carros/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        assertEquals(204, responseCode);

        URL checkUrl = new URL("http://localhost:4567/carros/" + id);
        HttpURLConnection checkConnection = (HttpURLConnection) checkUrl.openConnection();
        checkConnection.setRequestMethod("GET");

        int checkResponseCode = checkConnection.getResponseCode();
        assertEquals(404, checkResponseCode);
    }

    // Exercício 16 - Teste de Validação de Inserção de Carro com JUnit
    @Test
    @Order(6)
    public void testValidacaoInsercaoCarro() {
        CarroDTOInput carroDTO = new CarroDTOInput();
        carroDTO.setId(2);
        carroDTO.setModelo("Civic");
        carroDTO.setPlaca("GHI-9101");
        carroDTO.setChassi("ABC123789");

        carroService.inserir(carroDTO);

        List<CarroDTOOutput> carros = carroService.listar();
        assertEquals(1, carros.size());
        assertEquals("Civic", carros.get(0).getModelo());
    }

    // Exercício 17 - Teste de Validação de Listagem de Carros com JUnit e HttpUrlConnection
    @Test
    @Order(7)
    public void testValidacaoListagemCarrosComAPI() throws Exception {
        URL url = new URL("http://localhost:4567/carros");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String responseBody = response.toString();
        System.out.println(responseBody);
        assertTrue(responseBody.contains("modelo"));
    }

    // Exercício 18 - Teste de Validação de Inserção de Carro com API Externa
    @Test
    @Order(8)
    public void testInsercaoCarroComApiExterna() throws Exception {
        URL externalApiUrl = new URL("https://freetestapi.com/api/v1/cars");
        HttpURLConnection externalConnection = (HttpURLConnection) externalApiUrl.openConnection();
        externalConnection.setRequestMethod("GET");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode carData = objectMapper.readTree(externalConnection.getInputStream()).get(0);

        CarroDTOInput carroDTO = objectMapper.convertValue(carData, CarroDTOInput.class);
        carroDTO.setId(3);
        carroDTO.setChassi("CHASSI" + Math.random());

        URL localApiUrl = new URL("http://localhost:4567/carros");
        HttpURLConnection localConnection = (HttpURLConnection) localApiUrl.openConnection();
        localConnection.setRequestMethod("POST");
        localConnection.setRequestProperty("Content-Type", "application/json");
        localConnection.setDoOutput(true);

        String jsonInputString = objectMapper.writeValueAsString(carroDTO);
        try (OutputStream os = localConnection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = localConnection.getResponseCode();
        assertEquals(201, responseCode);
    }

    // Exercício 19 - Teste de Atualização de Carro via API
    @Test
    @Order(9)
    public void testAtualizacaoCarroViaApi() throws Exception {
        CarroDTOInput carroDTO = new CarroDTOInput();
        carroDTO.setId(1);
        carroDTO.setModelo("Fusca Atualizado Novamente");
        carroDTO.setPlaca("DEF-5678");
        carroDTO.setChassi("XYZ987654");

        URL url = new URL("http://localhost:4567/carros");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(carroDTO);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
    }

    // Exercício 20: Teste de validação de inserção de um carro usando ObjectMapper e HttpUrlConnection
    @Test
    @Order(10)
    public void testValidacaoInsercaoCarroComObjectMapper() throws Exception {
        URL externalApiUrl = new URL("https://freetestapi.com/api/v1/cars");
        HttpURLConnection externalConnection = (HttpURLConnection) externalApiUrl.openConnection();
        externalConnection.setRequestMethod("GET");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode carData = objectMapper.readTree(externalConnection.getInputStream()).get(0);

        CarroDTOInput carroDTO = new CarroDTOInput();
        carroDTO.setId(7);
        carroDTO.setModelo(carData.get("model").asText());
        carroDTO.setPlaca("PLACA" + Math.random());
        carroDTO.setChassi("CHASSI" + Math.random());

        URL localApiUrl = new URL("http://localhost:4567/carros");
        HttpURLConnection localConnection = (HttpURLConnection) localApiUrl.openConnection();
        localConnection.setRequestMethod("POST");
        localConnection.setRequestProperty("Content-Type", "application/json");
        localConnection.setDoOutput(true);

        String jsonInputString = objectMapper.writeValueAsString(carroDTO);
        try (OutputStream os = localConnection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = localConnection.getResponseCode();
        assertEquals(201, responseCode);
    }
}