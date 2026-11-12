package com.mycompany.campuseats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PruebaPersona2 {
    public static void main(String[] args) {
        System.out.println("INICIANDO PRUEBA DE LIMPIEZA (PERSONA 2)");

        try {
            // 1. Cargar datos usando la fábrica de tu compañero
            FuenteDatos<?> fuentePedidos = FabricaFuentes.crear("app_json");
            FuenteDatos<?> fuentePagos = FabricaFuentes.crear("pasarela_csv");
            FuenteDatos<?> fuenteMenu = FabricaFuentes.crear("menu_csv");

            List<LineaPedido> lineasCrudas = (List<LineaPedido>) fuentePedidos.leer("datos/pedidos_app.json");
            List<Pago> listaPagos = (List<Pago>) fuentePagos.leer("datos/pagos_pasarela.csv");
            List<Plato> listaMenu = (List<Plato>) fuenteMenu.leer("datos/menu_admin.csv");

            // 2. Convertir las listas de Pagos a un Mapa usando un bucle normal
            Map<String, Pago> pagosMap = new HashMap<>();
            for (int i = 0; i < listaPagos.size(); i++) {
                Pago p = listaPagos.get(i);
                pagosMap.put(p.idTransaccion(), p);
            }

            // 3. Convertir las listas de Platos a un Mapa usando un bucle normal
            Map<String, Plato> menuMap = new HashMap<>();
            for (int i = 0; i < listaMenu.size(); i++) {
                Plato plato = listaMenu.get(i);
                menuMap.put(plato.id(), plato);
            }

            // 4. Configurar el Pipeline
            Pipeline pipeline = new Pipeline();
            pipeline.agregarFiltro(new FiltroPedidosDuplicados());
            pipeline.agregarFiltro(new FiltroCantidadesValidas());
            pipeline.agregarFiltro(new FiltroSoloPagados(pagosMap, menuMap));

            // 5. Ejecutar la limpieza
            List<LineaPedido> lineasLimpias = pipeline.procesar(lineasCrudas);

            System.out.println("\n¡Limpieza completada con éxito!");

        } catch (Exception e) {
            System.err.println("Error en la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }
}