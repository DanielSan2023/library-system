package com.springboot.librarysystem.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.math.BigDecimal;


public class MonetaryAmount {

    private final BigDecimal amount;

    public MonetaryAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return amount.toString();
    }

}
