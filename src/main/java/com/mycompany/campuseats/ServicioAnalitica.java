package com.mycompany.campuseats;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioAnalitica {
    private final RepoAnalitica repositorio;
    private final List<Metrica> metricas;
    private final String rutaConfiguracion;

    public ServicioAnalitica(RepoAnalitica repositorio, List<Metrica> metricas) {
        this(repositorio, metricas, "config/fuentes.json");
    }

    public ServicioAnalitica(RepoAnalitica repositorio, List<Metrica> metricas,
                             String rutaConfiguracion) {
        this.repositorio = repositorio;
        this.metricas = List.copyOf(metricas);
        this.rutaConfiguracion = rutaConfiguracion;
    }

    /* Facade: coordina lectura, conversión, limpieza y persistencia. */
    public void cargar() {
        Map<String, String> rutas = FabricaFuentes.leerConfiguracion(rutaConfiguracion);

        @SuppressWarnings("unchecked")
        List<LineaPedido> lineas = ((FuenteDatos<LineaPedido>)
            FabricaFuentes.crear("app_json")).leer(rutas.get("app_json"));

        @SuppressWarnings("unchecked")
        List<Pago> pagos = ((FuenteDatos<Pago>)
            FabricaFuentes.crear("pasarela_csv")).leer(rutas.get("pasarela_csv"));

        @SuppressWarnings("unchecked")
        List<Plato> platos = ((FuenteDatos<Plato>)
            FabricaFuentes.crear("menu_csv")).leer(rutas.get("menu_csv"));

        /* Para cada pedido se conserva el intento APROBADO más reciente. */
        Map<String, Pago> pagosAprobados = new HashMap<>();
        for (Pago pago : pagos) {
            if ("APROBADA".equals(pago.estado())) {
                Pago anterior = pagosAprobados.get(pago.idTransaccion());
                if (anterior == null || pago.fecha().isAfter(anterior.fecha())) {
                    pagosAprobados.put(pago.idTransaccion(), pago);
                }
            }
        }

        Map<String, Plato> menu = new HashMap<>();
        for (Plato plato : platos) menu.put(plato.id(), plato);

        Pipeline pipeline = new Pipeline();
        pipeline.agregarFiltro(new FiltroPedidosDuplicados());
        pipeline.agregarFiltro(new FiltroCantidadesValidas());
        pipeline.agregarFiltro(new FiltroSoloPagados(pagosAprobados, menu));

        List<LineaPedido> limpias = pipeline.procesar(lineas);
        repositorio.guardarLineas(limpias);
        repositorio.guardarPagos(pagos);

        System.out.println("Datos cargados y guardados correctamente.");
    }

    public void resumen(LocalDateTime desde, LocalDateTime hasta) {
        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            throw new IllegalArgumentException("El rango de fechas no es válido.");
        }

        List<LineaPedido> datos = repositorio.lineasEntre(desde, hasta);
        System.out.println("\n              ESTADISTICAS ");
        System.out.println("Período: " + desde + " hasta " + hasta);
        for (Metrica metrica : metricas) {
            System.out.println(metrica.nombre() + ": " + metrica.calcular(datos));
        }
    }
}
