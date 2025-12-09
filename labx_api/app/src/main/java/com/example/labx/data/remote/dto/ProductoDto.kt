package com.example.labx.data.remote.dto

import com.example.labx.domain.model.Producto
import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object para Producto (API Railway)
 *
 * Mapea la respuesta de: https://api-dfs2-dm-production.up.railway.app/api/productos
 *
 * CAMBIOS IMPORTANTES:
 * 1. 'precio' viene como String en esta API ("15000.00"), se convierte en el mapper.
 * 2. 'categoria_nombre' puede ser null.
 * 3. 'stock' ahora viene real desde el servidor.
 */
data class ProductoDto(
    @SerializedName("id")
    val identificador: Int,

    @SerializedName("nombre")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String,

    /**
     * IMPORTANTE: La API envía el precio entre comillas (String),
     * por ejemplo: "15000.00". Lo recibimos como String para evitar errores
     * de parseo y lo convertimos a Double en la función aModelo().
     */
    @SerializedName("precio")
    val precio: String,

    @SerializedName("imagen")
    val urlImagen: String,

    /**
     * Puede venir null (ej: Croissant ID 54).
     * Usamos String? (nullable) para evitar crashes.
     */
    @SerializedName("categoria_nombre")
    val categoria: String?,

    @SerializedName("stock")
    val stock: Int
)

/**
 * Convierte el DTO de la API Railway al Modelo de Dominio de la App.
 */
fun ProductoDto.aModelo(): Producto {
    return Producto(
        id = this.identificador,
        nombre = this.titulo,
        descripcion = this.descripcion,
        // Convertimos el String "15000.00" a Double. Si falla, ponemos 0.0
        precio = this.precio.toDoubleOrNull() ?: 0.0,
        imagenUrl = this.urlImagen,
        // Si la categoría es null, mostramos "Sin Categoría" u "Otros"
        categoria = this.categoria ?: "General",
        // Ahora usamos el stock real de la API
        stock = this.stock
    )
}

/**
 * Mantiene la funcionalidad de sobreescribir stock si fuera necesario,
 * aunque ahora la API ya provee este dato.
 */
fun ProductoDto.aModeloConStock(stockPersonalizado: Int): Producto {
    return Producto(
        id = this.identificador,
        nombre = this.titulo,
        descripcion = this.descripcion,
        precio = this.precio.toDoubleOrNull() ?: 0.0,
        imagenUrl = this.urlImagen,
        categoria = this.categoria ?: "General",
        stock = stockPersonalizado
    )
}

/**
 * Convierte un Producto a DTO.
 * Nota: Convertimos el precio Double a String para respetar el formato de la API.
 */
fun Producto.aDto(): ProductoDto {
    return ProductoDto(
        identificador = this.id,
        titulo = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio.toString(),
        urlImagen = this.imagenUrl,
        categoria = this.categoria,
        stock = this.stock
    )
}

fun List<ProductoDto>.aModelos(): List<Producto> {
    return this.map { it.aModelo() }
}

fun List<Producto>.aDtos(): List<ProductoDto> {
    return this.map { it.aDto() }
}