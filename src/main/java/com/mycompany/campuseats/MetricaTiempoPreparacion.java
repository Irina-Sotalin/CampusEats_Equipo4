package com.mycompany.campuseats;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MetricaTiempoPreparacion implements Metrica {
    @Override
    public String nombre() { return "Tiempo de preparación"; }

    @Override
    public String calcular(List<LineaPedido> datos) {
        List<Double> horaPico = new ArrayList<>();
        List<Double> restoDia = new ArrayList<>();

        /* Se cuenta una sola vez cada pedido. El tiempo de referencia va desde
            PAGADO hasta LISTO_RETIRO, como indican los datos del taller. */
        java.util.Map<String, LineaPedido> pedidos = new java.util.LinkedHashMap<>();
        for (LineaPedido linea : datos) pedidos.putIfAbsent(linea.idPedido(), linea);

        for (LineaPedido linea : pedidos.values()) {
            LocalDateTime inicio = linea.historialEstados().get("PAGADO");
            LocalDateTime fin = linea.historialEstados().get("LISTO_RETIRO");
            if (inicio == null || fin == null || fin.isBefore(inicio)) continue;

            double minutos = Duration.between(inicio, fin).toSeconds() / 60.0;
            if (inicio.getHour() >= 12 && inicio.getHour() <= 13) horaPico.add(minutos);
            else restoDia.add(minutos);
        }

        if (horaPico.isEmpty() && restoDia.isEmpty()) {
            return "No hay suficientes pedidos con historial de preparación.";
        }

        String pico = horaPico.isEmpty() ? "sin datos" : String.format("%.0f minutos", promedio(horaPico));
        String resto = restoDia.isEmpty() ? "sin datos" : String.format("%.0f minutos", promedio(restoDia));
        
        // Se cambió el formato para que baje en lista
        return "\n12:00 a 13:59: " + pico + "\nResto del día: " + resto;
    }

    private double promedio(List<Double> valores) {
        return valores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }
}
