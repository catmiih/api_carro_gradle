package org.example.service;

import org.example.dto.CarroDTOInput;
import org.example.dto.CarroDTOOutput;
import org.example.model.Carro;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CarroService {
    private List<Carro> listaCarros = new ArrayList<>();
    private ModelMapper modelMapper = new ModelMapper();

    public List<CarroDTOOutput> listar() {
        return listaCarros.stream()
                .map(carro -> modelMapper.map(carro, CarroDTOOutput.class))
                .collect(Collectors.toList());
    }

    public void inserir(CarroDTOInput carroDTOInput) {
        Carro carro = modelMapper.map(carroDTOInput, Carro.class);
        listaCarros.add(carro);
    }

    public void alterar(CarroDTOInput carroDTOInput) {
        Carro carro = modelMapper.map(carroDTOInput, Carro.class);
        Optional<Carro> carroExistente = listaCarros.stream()
                .filter(c -> c.getId() == carro.getId())
                .findFirst();

        carroExistente.ifPresent(c -> {
            listaCarros.remove(c);
            listaCarros.add(carro);
        });
    }

    public CarroDTOOutput buscar(int id) {
        return listaCarros.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .map(carro -> modelMapper.map(carro, CarroDTOOutput.class))
                .orElse(null);
    }

    public void excluir(int id) {
        listaCarros.removeIf(c -> c.getId() == id);
    }
}
