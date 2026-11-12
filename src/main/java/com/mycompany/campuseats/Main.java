package com.mycompany.campuseats;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RepoAnalitica repositorio = new RepoAnaliticaMemoria();

        List<Metrica> metricas = Arrays.asList(
            new MetricaPlatosTop(),
            new MetricaPedidosPorHora(),
            new MetricaTiempoPreparacion()
        );

        ServicioAnalitica servicio = new ServicioAnalitica(repositorio, metricas);
        servicio.cargar();

        LocalDateTime desde = LocalDateTime.of(2026, 10, 19, 0, 0);
        LocalDateTime hasta = LocalDateTime.of(2026, 10, 30, 23, 59, 59);
        servicio.resumen(desde, hasta);
    }
}
