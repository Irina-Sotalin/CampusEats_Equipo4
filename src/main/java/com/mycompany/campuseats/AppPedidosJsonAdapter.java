package com.mycompany.campuseats;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppPedidosJsonAdapter implements FuenteDatos<LineaPedido> {
    @Override
    public List<LineaPedido> leer(String ruta) {
        List<LineaPedido> lineas = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode pedidos = mapper.readTree(new File(ruta));
            int indice = 0;

            for (JsonNode pedido : pedidos) {
                indice++;
                String idPedido = pedido.path("numeroPedido").asText();
                String estadoActual = normalizarEstado(pedido.path("estado").asText());
                BigDecimal total = new BigDecimal(pedido.path("total").asText("0"));
                LocalDateTime fechaRegistro = LocalDateTime.parse(
                    pedido.path("registradoEn").asText()
                );

                Map<String, LocalDateTime> historial = new LinkedHashMap<>();
                for (JsonNode evento : pedido.path("historialEstados")) {
                    String estado = normalizarEstado(evento.path("estado").asText());
                    String fecha = evento.path("fechaHora").asText();
                    if (!estado.isBlank() && !fecha.isBlank()) {
                        historial.put(estado, LocalDateTime.parse(fecha));
                    }
                }

                for (JsonNode linea : pedido.path("lineas")) {
                    String idPlato = linea.path("platoId").asText();
                    int cantidad = linea.path("cantidad").asInt();
                    BigDecimal precio = new BigDecimal(
                        linea.path("precioUnitario").asText("0")
                    );

                    lineas.add(new LineaPedido(
                        idPedido, idPlato, cantidad, estadoActual, precio, total,
                        fechaRegistro, Map.copyOf(historial), indice, "", ""
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo pedidos_app.json: " + e.getMessage(), e);
        }
        return lineas;
    }

    private String normalizarEstado(String estado) {
        return estado == null ? "" : estado.trim().toUpperCase().replace(' ', '_');
    }
}
