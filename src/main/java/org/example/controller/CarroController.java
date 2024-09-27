package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CarroDTOInput;
import org.example.dto.CarroDTOOutput;
import org.example.service.CarroService;
import static spark.Spark.*;


public class CarroController {
    private static CarroService carroService = new CarroService();

    public static void respostasRequisicoes() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Listar carros
        get("/carros", (req, res) -> {
            res.type("application/json");
            return objectMapper.writeValueAsString(carroService.listar());
        });

        // Buscar carro por ID
        get("/carros/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            res.type("application/json");
            CarroDTOOutput carro = carroService.buscar(id);
            if (carro == null) {
                res.status(404);
                return "";
            }
            return objectMapper.writeValueAsString(carro);
        });

        // Excluir carro por ID
        delete("/carros/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            carroService.excluir(id);
            res.status(204);
            return "";
        });

        // Inserir carro
        post("/carros", (req, res) -> {
            CarroDTOInput carroDTOInput = objectMapper.readValue(req.body(), CarroDTOInput.class);
            carroService.inserir(carroDTOInput);
            res.status(201);
            return "";
        });

        // Atualizar carro
        put("/carros", (req, res) -> {
            CarroDTOInput carroDTOInput = objectMapper.readValue(req.body(), CarroDTOInput.class);
            carroService.alterar(carroDTOInput);
            res.status(200);
            return "";
        });
    }

    public static void main(String[] args) {
        respostasRequisicoes();
    }
}
