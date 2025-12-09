package com.example.labx.data.remote.api

import com.example.labx.data.remote.dto.ProductoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface del servicio API para operaciones de productos
 *
 * ADAPTADA PARA: https://api-dfs2-dm-production.up.railway.app
 *
 * @author Sting Parra Silva (Actualizado)
 * @version 2.0
 */
interface ProductoApiService {

    /**
     * Obtiene todos los productos disponibles
     * Endpoint: GET /api/productos
     */
    @GET("api/productos")
    suspend fun obtenerTodosLosProductos(): Response<List<ProductoDto>>

    /**
     * Obtiene un producto específico por su ID
     * Endpoint: GET /api/productos/{id}
     */
    @GET("api/productos/{id}")
    suspend fun obtenerProductoPorId(
        @Path("id") identificador: Int
    ): Response<ProductoDto>

    /**
     * Crea un nuevo producto
     * Endpoint: POST /api/productos
     */
    @POST("api/productos")
    suspend fun agregarProducto(
        @Body nuevoProducto: ProductoDto
    ): Response<ProductoDto>

    /**
     * Actualiza un producto existente
     * Endpoint: PUT /api/productos/{id}
     */
    @PUT("api/productos/{id}")
    suspend fun modificarProducto(
        @Path("id") identificador: Int,
        @Body productoActualizado: ProductoDto
    ): Response<ProductoDto>

    /**
     * Elimina un producto
     * Endpoint: DELETE /api/productos/{id}
     */
    @DELETE("api/productos/{id}")
    suspend fun borrarProducto(
        @Path("id") identificador: Int
    ): Response<Unit>

    // -----------------------------------------------------------------------
    // ZONA DE INCERTIDUMBRE (Endpoints que pueden variar según el Backend)
    // -----------------------------------------------------------------------

    /**
     * TODO: Verificar si la nueva API tiene endpoint de categorías.
     * En FakeStore era: "products/categories".
     * Si no existe en la nueva API, esta llamada fallará (404).
     * Se sugiere probar: "api/categorias" o "api/productos/categorias".
     */
    @GET("api/categorias") // Ruta hipotética
    suspend fun obtenerCategorias(): Response<List<String>>

    /**
     * TODO: Verificar cómo filtra por categoría la nueva API.
     * FakeStore usaba ruta: "products/category/{nombre}"
     * Muchas APIs usan Query Params: "api/productos?categoria={nombre}"
     *
     * He cambiado la implementación a Query Param por ser más estándar en APIs modernas.
     */
    @GET("api/productos")
    suspend fun obtenerProductosPorCategoria(
        @Query("categoria_nombre") nombreCategoria: String
    ): Response<List<ProductoDto>>

    /**
     * TODO: Verificar si el backend soporta paginación con "limit"
     */
    @GET("api/productos")
    suspend fun obtenerProductosConLimite(
        @Query("limit") limite: Int
    ): Response<List<ProductoDto>>

    /**
     * TODO: Verificar si el backend soporta ordenamiento con "sort"
     */
    @GET("api/productos")
    suspend fun obtenerProductosOrdenados(
        @Query("sort") orden: String = "asc"
    ): Response<List<ProductoDto>>

    @GET("api/productos")
    suspend fun obtenerProductosFiltrados(
        @Query("limit") limite: Int,
        @Query("sort") orden: String
    ): Response<List<ProductoDto>>
}