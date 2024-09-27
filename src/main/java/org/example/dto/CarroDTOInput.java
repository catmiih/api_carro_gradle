package org.example.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarroDTOInput {

    @JsonProperty("id")
    private int id;

    @JsonProperty("model")
    private String modelo;

    @JsonProperty("mileage")
    private String placa;

    @JsonProperty("chassi")
    private String chassi;
}
