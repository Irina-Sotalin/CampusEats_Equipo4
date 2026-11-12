
package com.mycompany.campuseats;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetricaPedidosPorHora implements Metrica {
    @Override
    public String nombre() { return "Pedidos por hora"; }

    @Override
    public String calcular(List<LineaPedido> datos) {
        Map<String, Integer> horaPorPedido = datos.stream()
            .collect(Collectors.toMap(
                LineaPedido::idPedido,
                linea -> linea.historialEstados().get("PAGADO").getHour(),
                (primero, segundo) -> primero
            ));

        Map<Integer, Long> conteo = horaPorPedido.values().stream()
            .collect(Collectors.groupingBy(hora -> hora, Collectors.counting()));

        List<Map.Entry<Integer, Long>> top = conteo.entrySet().stream()
            .sorted(Map.Entry.<Integer, Long>comparingByValue(Comparator.reverseOrder())
                .thenComparing(Map.Entry.comparingByKey()))
            .limit(3)
            .toList();

        if (top.isEmpty()) return "No existen pedidos en el período.";
        
        return "\n" + top.stream()
            .map(e -> String.format("%02d:00 (%d pedidos)", e.getKey(), e.getValue()))
            .collect(Collectors.joining("\n"));
    }
}