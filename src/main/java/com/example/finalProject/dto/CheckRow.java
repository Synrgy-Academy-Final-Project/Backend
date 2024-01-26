package com.example.finalProject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CheckRow {
    public CheckRow(Long row) {
        this.row = row;
    }

    Long row;
}
