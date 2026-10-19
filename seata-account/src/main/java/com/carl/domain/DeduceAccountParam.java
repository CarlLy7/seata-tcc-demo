package com.carl.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: carl
 * @date: 2026.10.16
 * @Since: 1.0
 */
@Data
public class DeduceAccountParam {
    private Integer userId;

    private BigDecimal balance;
}
