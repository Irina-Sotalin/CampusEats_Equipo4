package com.mycompany.campuseats;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Pago(
    String idTransaccion,
    LocalDateTime fecha,
    BigDecimal monto,
    String estado
) {}
