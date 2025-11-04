package br.com.healthtech.imrea.ia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RiscoResponseDTO {

    @JsonProperty("score_risco_carelink")
    private int scoreRiscoCarelink;

    // Getters e Setters

    public int getScoreRiscoCarelink() {
        return scoreRiscoCarelink;
    }

    public void setScoreRiscoCarelink(int scoreRiscoCarelink) {
        this.scoreRiscoCarelink = scoreRiscoCarelink;
    }
}