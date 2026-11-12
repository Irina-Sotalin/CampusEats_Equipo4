package com.mycompany.campuseats;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FiltroSoloPagados implements Filtro {
    private final Map<String, Pago> pagosAprobados;
    private final Map<String, Plato> menu;
    private int descartadosNoPagados;
    private int descartadosErrorPago;

    public FiltroSoloPagados(Map<String, Pago> pagosAprobados, Map<String, Plato> menu) {
        this.pagosAprobados = pagosAprobados;
        this.menu = menu;
    }

    @Override
    public List<LineaPedido> aplicar(List<LineaPedido> lineas) {
        List<LineaPedido> resultado = new ArrayList<>();
        descartadosNoPagados = 0;
        descartadosErrorPago = 0;

        for (LineaPedido linea : lineas) {
            if (!linea.pasoPorPagado()) {
                descartadosNoPagados++;
                continue;
            }

            Pago pago = pagosAprobados.get(linea.idPedido());
            if (pago == null || !"APROBADA".equals(pago.estado())
                    || pago.monto().subtract(linea.totalPedido()).abs()
                        .compareTo(new BigDecimal("0.01")) > 0) {
                descartadosErrorPago++;
                continue;
            }

            Plato plato = menu.get(linea.idPlato());
            String nombre = plato == null ? "Plato retirado" : plato.nombre();
            String categoria = plato == null ? "Sin categoría" : plato.categoria();

            resultado.add(new LineaPedido(
                linea.idPedido(), linea.idPlato(), linea.cantidad(), linea.estadoActual(),
                linea.precioUnitarioHistorico(), linea.totalPedido(), linea.fechaRegistro(),
                linea.historialEstados(), linea.indicePedidoFuente(), nombre, categoria
            ));
        }
        return resultado;
    }

    @Override
    public int getElementosDescartados() {
        return descartadosNoPagados + descartadosErrorPago;
    }

    public int getDescartadosNoPagados() { return descartadosNoPagados; }
    public int getDescartadosErrorPago() { return descartadosErrorPago; }

    @Override
    public String getNombre() { return "Validación de estado y pago"; }
}
