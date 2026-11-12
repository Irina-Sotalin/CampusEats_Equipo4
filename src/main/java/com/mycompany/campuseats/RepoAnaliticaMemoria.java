package com.mycompany.campuseats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepoAnaliticaMemoria implements RepoAnalitica {
    private final List<LineaPedido> lineas = new ArrayList<>();
    private final List<Pago> pagos = new ArrayList<>();

    @Override
    public void guardarLineas(List<LineaPedido> nuevas) {
        lineas.clear();
        lineas.addAll(nuevas);
    }

    @Override
    public void guardarPagos(List<Pago> nuevos) {
        pagos.clear();
        pagos.addAll(nuevos);
    }

    @Override
    public List<LineaPedido> lineasEntre(LocalDateTime desde, LocalDateTime hasta) {
        return lineas.stream()
            .filter(l -> !l.fechaRegistro().isBefore(desde) && !l.fechaRegistro().isAfter(hasta))
            .collect(Collectors.toList());
    }

    @Override
    public List<Pago> pagosEntre(LocalDateTime desde, LocalDateTime hasta) {
        return pagos.stream()
            .filter(p -> !p.fecha().isBefore(desde) && !p.fecha().isAfter(hasta))
            .collect(Collectors.toList());
    }
}
