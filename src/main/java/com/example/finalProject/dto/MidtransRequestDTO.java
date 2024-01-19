package com.example.finalProject.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class MidtransRequestDTO {
    Map<String, Object> transaction_details;
    List<Map<String, Object>> item_details;
    Map<String, Object>  customer_details;
}
