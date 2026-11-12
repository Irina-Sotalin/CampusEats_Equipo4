package com.mycompany.campuseats;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetricaPlatosTop implements Metrica {
    @Override
    public String nombre() { return "Platos más vendidos"; }

    @Override
    public String calcular(List<LineaPedido> datos) {
        Map<String, Integer> cantidades = datos.stream().collect(
            Collectors.groupingBy(
                LineaPedido::nombrePlato,
                Collectors.summingInt(LineaPedido::cantidad)
            )
        );

        List<Map.Entry<String, Integer>> top = cantidades.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                .thenComparing(Map.Entry.comparingByKey()))
            .limit(5)
            .toList();

        if (top.isEmpty()) return "No existen ventas en el período.";
        
        StringBuilder resultado = new StringBuilder();
        resultado.append("\n");
        
        for (int i = 0; i < top.size(); i++) {
            if (i > 0) resultado.append("\n");
            resultado.append(i + 1).append(". ")
                .append(top.get(i).getKey()).append(": ")
                .append(top.get(i).getValue()).append(" unidades");
        }
        return resultado.toString();
    }
}