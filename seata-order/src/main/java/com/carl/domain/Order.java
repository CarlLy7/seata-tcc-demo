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
public class Order {
    private Integer userId;

    private Integer productId;

    private BigDecimal price;

    private Integer saleQty;
}
