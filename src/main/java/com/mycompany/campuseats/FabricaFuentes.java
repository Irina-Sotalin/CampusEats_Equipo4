package com.mycompany.campuseats;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/* Factory: crea el adaptador según el tipo declarado en fuentes.json. */
public class FabricaFuentes {
    private static final Map<String, Supplier<? extends FuenteDatos<?>>> FABRICAS = Map.of(
        "app_json", AppPedidosJsonAdapter::new,
        "pasarela_csv", PasarelaCsvAdapter::new,
        "menu_csv", MenuCsvAdapter::new
    );

    public static FuenteDatos<?> crear(String tipo) {
        Supplier<? extends FuenteDatos<?>> constructor = FABRICAS.get(tipo);
        if (constructor == null) {
            throw new IllegalArgumentException("Tipo de fuente desconocido: " + tipo);
        }
        return constructor.get();
    }

    /* Lee config/fuentes.json y devuelve las rutas declaradas por tipo. */
    public static Map<String, String> leerConfiguracion(String rutaConfig) {
        Map<String, String> rutas = new HashMap<>();
        try {
            JsonNode raiz = new ObjectMapper().readTree(new File(rutaConfig));
            for (JsonNode fuente : raiz.path("fuentes")) {
                String tipo = fuente.path("tipo").asText();
                String ruta = fuente.path("ruta").asText();
                if (!FABRICAS.containsKey(tipo)) {
                    throw new IllegalArgumentException("Tipo de fuente desconocido: " + tipo);
                }
                if (tipo.isBlank() || ruta.isBlank()) {
                    throw new IllegalArgumentException("Fuente con tipo o ruta vacíos.");
                }
                rutas.put(tipo, resolverRuta(rutaConfig, ruta));
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo leer " + rutaConfig + ": " + e.getMessage(), e);
        }
        for (String tipo : FABRICAS.keySet()) {
            if (!rutas.containsKey(tipo)) {
                throw new IllegalArgumentException("Falta la fuente " + tipo + " en " + rutaConfig);
            }
        }
        return rutas;
    }

    private static String resolverRuta(String rutaConfig, String ruta) {
        Path archivoConfig = Path.of(rutaConfig).toAbsolutePath().normalize();
        Path proyecto = archivoConfig.getParent().getParent();
        return proyecto.resolve(ruta).normalize().toString();
    }
}
