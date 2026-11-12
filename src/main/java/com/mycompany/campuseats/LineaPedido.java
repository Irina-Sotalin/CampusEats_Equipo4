package com.mycompany.campuseats;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record LineaPedido(
    String idPedido,
    String idPlato,
    int cantidad,
    String estadoActual,
    BigDecimal precioUnitarioHistorico,
    BigDecimal totalPedido,
    LocalDateTime fechaRegistro,
    Map<String, LocalDateTime> historialEstados,
    int indicePedidoFuente,
    String nombrePlato,
    String categoria
) {
    public boolean pasoPorPagado() {
        return historialEstados != null && historialEstados.containsKey("PAGADO");
    }
}
