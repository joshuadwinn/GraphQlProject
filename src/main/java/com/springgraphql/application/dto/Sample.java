package com.springgraphql.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sample {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("thing")
    private Boolean thing;
    @JsonProperty("info")
    private String info;
}
