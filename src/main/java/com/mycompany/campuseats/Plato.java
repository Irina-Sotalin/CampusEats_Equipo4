package com.mycompany.campuseats;

import java.math.BigDecimal;

public record Plato(
    String id,
    String nombre,
    String categoria,
    BigDecimal precio,
    String estado
) {}
