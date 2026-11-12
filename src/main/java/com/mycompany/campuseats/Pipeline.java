package com.mycompany.campuseats;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {
    private final List<Filtro> filtros = new ArrayList<>();

    public void agregarFiltro(Filtro filtro) {
        filtros.add(filtro);
    }

    public List<LineaPedido> procesar(List<LineaPedido> datosEntrada) {
        List<LineaPedido> datosActuales = new ArrayList<>(datosEntrada);
        System.out.println("\n              LIMPIEZA DE DATOS ");
        System.out.println("Líneas iniciales: " + datosActuales.size());

        for (Filtro filtro : filtros) {
            int antes = datosActuales.size();
            datosActuales = filtro.aplicar(datosActuales);
            if (filtro instanceof FiltroSoloPagados filtroPagos) {
                System.out.println("Pedidos no pagados: "
                    + filtroPagos.getDescartadosNoPagados());
                System.out.println("Pago no aprobado o monto diferente: "
                    + filtroPagos.getDescartadosErrorPago());
            } else {
                System.out.println(filtro.getNombre() + ": "
                    + filtro.getElementosDescartados() + " descartados");
            }
            System.out.println("Líneas restantes: " + datosActuales.size());
        }

        long pedidos = datosActuales.stream()
            .map(LineaPedido::idPedido).distinct().count();

        System.out.println("LÍNEAS FINALES: " + datosActuales.size());
        System.out.println("PEDIDOS ÚNICOS: " + pedidos);
        return datosActuales;
    }
}
