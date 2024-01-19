package com.example.finalProject.utils;

import org.springframework.stereotype.Component;

@Component
public class Config {

    public static final Integer  EROR_CODE_404 =404;
    public  static final String DATA_NOT_FOUND = "Data not found.";
    public static final String REQUIREMENT_NOT_SATISFIED = "required parameter not satisfied";
    public static final String CANNOT_FIND_USER = "cannot find related user";
    public static final String CANNOT_FIND_PRODUCT = "cannot find related product";
    public static final String CANNOT_FIND_ORDER = "cannot find related order";
    public static final String CANNOT_FIND_MERCHANT = "cannot find related merchant";
    public static final String UUID_PATTERN = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";
}
