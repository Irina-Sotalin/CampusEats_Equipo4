package com.mycompany.campuseats;

import java.time.LocalDateTime;
import java.util.List;

public interface RepoAnalitica {
    void guardarLineas(List<LineaPedido> lineas);
    void guardarPagos(List<Pago> pagos);
    List<LineaPedido> lineasEntre(LocalDateTime desde, LocalDateTime hasta);
    List<Pago> pagosEntre(LocalDateTime desde, LocalDateTime hasta);
}
