package others




/**
 * Proporciona una lista estática de nombres de ubicaciones.
 * En el futuro, esta función podría obtener datos de una base de datos,
 * una API remota, o cualquier otra fuente de datos.
 *
 * @return Una lista de strings que representan las ubicaciones disponibles.
 */
fun obtenerUbicaciones(): List<String> {
    // Aquí puedes definir las ubicaciones que necesites.
    // Puedes añadir, quitar o modificar estas cadenas según tus requerimientos.
    return listOf(
        "Oficina Principal",
        "Sucursal Centro",
        "Cliente - Alpha Corp",
        "Cliente - Beta Solutions",
        "Laboratorio de Pruebas",
        "Taller Interno",
        "Remoto (Teletrabajo)",
        "Bodega Central",
        "Punto de Venta Norte",
        "Otra (Especificar)" // Podrías tener una opción genérica
    )
}

/**
 * Ejemplo alternativo si quisieras trabajar con objetos más estructurados
 * en lugar de solo strings. Esto podría ser útil si cada ubicación
 * tuviera un ID u otras propiedades. Por ahora, nos enfocaremos en la
 * función `obtenerUbicaciones()` que devuelve `List<String>`.
 */
data class UbicacionItem(val id: Int, val nombre: String)

fun obtenerUbicacionesConId(): List<UbicacionItem> {
    return listOf(
        UbicacionItem(1, "Oficina Principal"),
        UbicacionItem(2, "Sucursal Centro"),
        UbicacionItem(3, "Cliente - Alpha Corp"),
    )
}