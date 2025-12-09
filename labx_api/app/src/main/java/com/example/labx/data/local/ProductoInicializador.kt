package com.example.labx.data.local

import android.content.Context
import com.example.labx.domain.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ProductoInicializador: Carga productos de ejemplo en la BD
 *
 * Se ejecuta la primera vez que se abre la app
 * Permite tener datos de prueba sin conectarse a una API
 *
 * Autor: Prof. Sting Adams Parra Silva
 */
object ProductoInicializador {

    /**
     * Inserta productos de ejemplo si la base de datos está vacía
     */
    fun inicializarProductos(context: Context) {
        val database = AppDatabase.getDatabase(context)
        val productoDao = database.productoDao()

        // Ejecutar en background (no bloquear la UI)
        CoroutineScope(Dispatchers.IO).launch {
            // Solo insertar si no hay productos
            val productosExistentes = productoDao.obtenerProductoPorId(1)
            if (productosExistentes == null) {
                val productosDeEjemplo = listOf(
                    Producto(
                        id = 1,
                        nombre = "Catan",
                        descripcion = "Un clásico juego de estrategia donde los jugadores compiten por colonizar y\n" +
                                "expandirse en la isla de Catan. Ideal para 3-4 jugadores y perfecto para noches de juego en\n" +
                                "familia o con amigos.",
                        precio = 29990.0,
                        imagenUrl = "catan", // Nombre del archivo en drawable/
                        categoria = "Juegos de Mesa",
                        stock = 15
                    ),
                    Producto(
                        id = 2,
                        nombre = "Carcassonne",
                        descripcion = "Un juego de colocación de fichas donde los jugadores construyen el paisaje\n" +
                                "alrededor de la fortaleza medieval de Carcassonne. Ideal para 2-5 jugadores y fácil de\n" +
                                "aprender.",
                        precio = 24990.0,
                        imagenUrl = "carcassonne", // Nombre del archivo en drawable/
                        categoria = "Juegos de Mesa",
                        stock = 8
                    ),
                    Producto(
                        id = 3,
                        nombre = "Controlador Inalámbrico Xbox Series X",
                        descripcion = "Ofrece una experiencia de juego cómoda con\n" +
                                "botones mapeables y una respuesta táctil mejorada. Compatible con consolas Xbox y PC.",
                        precio = 59990.0,
                        imagenUrl = "xboxcontrol", // Nombre del archivo en drawable/
                        categoria = "Accesorios",
                        stock = 12
                    ),
                    Producto(
                        id = 4,
                        nombre = "Auriculares Gamer HyperX Cloud II",
                        descripcion = "Proporcionan un sonido envolvente de calidad con un\n" +
                                "micrófono desmontable y almohadillas de espuma viscoelástica para mayor comodidad\n" +
                                "durante largas sesiones de juego.",
                        precio = 79990.0,
                        imagenUrl = "audifonos", // Nombre del archivo en drawable/
                        categoria = "Accesorios",
                        stock = 5
                    ),
                    Producto(
                        id = 5,
                        nombre = "PlayStation 5",
                        descripcion = "La consola de última generación de Sony, que ofrece gráficos\n" +
                                "impresionantes y tiempos de carga ultrarrápidos para una experiencia de juego inmersiva.",
                        precio = 549990.0,
                        imagenUrl = "play5", // Nombre del archivo en drawable/
                        categoria = "Consolas",
                        stock = 3
                    ),
                    Producto(
                        id = 6,
                        nombre = "PC Gamer ASUS ROG Strix",
                        descripcion = "Disco sólido NVMe Gen4 de 1TB, velocidades de lectura hasta 7000 MB/s, ideal para gaming y creación de contenido.",
                        precio = 1299990.0,
                        imagenUrl = "pcgamer", // Nombre del archivo en drawable/
                        categoria = "Computadores Gamers",
                        stock = 20
                    ),
                    Producto(
                        id = 7,
                        nombre = "Silla Gamer Secretlab Titan",
                        descripcion = "Diseñada para el máximo confort, esta silla ofrece un soporte\n" +
                                "ergonómico y personalización ajustable para sesiones de juego prolongadas.",
                        precio = 349990.0,
                        imagenUrl = "sillagamer", // Nombre del archivo en drawable/
                        categoria = "Sillas Gamer",
                        stock = 6
                    ),
                    Producto(
                        id = 8,
                        nombre = "Mouse Gamer Logitech G502 HERO",
                        descripcion = "Con sensor de alta precisión y botones\n" +
                                "personalizables, este mouse es ideal para gamers que buscan un control preciso y\n" +
                                "personalización.",
                        precio = 49990.0,
                        imagenUrl = "mouse", // Nombre del archivo en drawable/
                        categoria = "Mouse",
                        stock = 25
                    ),
                    Producto(
                        id = 9,
                        nombre = "Mousepad Razer Goliathus Extended\n" +
                                "Chroma",
                        descripcion = "Ofrece un área de juego amplia con\n" +
                                "iluminación RGB personalizable, asegurando una superficie suave y uniforme para el\n" +
                                "movimiento del mouse.",
                        precio = 29990.0,
                        imagenUrl = "mousepad", // Nombre del archivo en drawable/
                        categoria = "Mousepad",
                        stock = 25
                    ),
                    Producto(
                        id = 10,
                        nombre = "Polera Gamer Personalizada 'Level-Up'",
                        descripcion = "Una camiseta cómoda y estilizada, con la\n" +
                                "posibilidad de personalizarla con tu gamer tag.",
                        precio = 14990.0,
                        imagenUrl = "polera", // Nombre del archivo en drawable/
                        categoria = "Poleras Personalizadas",
                        stock = 25
                    )

                )

                // Insertar en la base de datos
                productoDao.insertarProductos(productosDeEjemplo.map { it.toEntity() })
            }
        }
    }
}

// Extension function para convertir Producto a ProductoEntity
private fun Producto.toEntity() = com.example.labx.data.local.entity.ProductoEntity(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    imagenUrl = imagenUrl,
    categoria = categoria,
    stock = stock
)
