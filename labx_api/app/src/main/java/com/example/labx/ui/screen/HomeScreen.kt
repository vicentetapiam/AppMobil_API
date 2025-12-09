package com.example.labx.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import com.example.labx.data.repository.CarritoRepository
import com.example.labx.data.repository.ProductoRepositoryImpl
import com.example.labx.domain.model.Producto
import com.example.labx.ui.viewmodel.ProductoViewModel
import com.example.labx.ui.viewmodel.ProductoViewModelFactory

/**
 * HomeScreen: Pantalla principal de la app
 * 
 * Muestra:
 * - Lista de productos disponibles
 * - Buscador y filtros por categoría
 * - Botón para ir al carrito
 * - Botón para ir a registro
 * - Botón para volver a portada
 * 
 * Autor: Prof. Sting Adams Parra Silva
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    productoRepository: ProductoRepositoryImpl,
    carritoRepository: CarritoRepository,
    onProductoClick: (Int) -> Unit,
    onCarritoClick: () -> Unit,
    onRegistroClick: () -> Unit,
    onVolverPortada: () -> Unit
) {
    // Crear ViewModel con Factory
    val viewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(productoRepository)
    )
    
    // Observar estado
    val uiState by viewModel.uiState.collectAsState()
    
    // NUEVO: Estado de búsqueda y filtros
    var textoBusqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    
    // NUEVO: Lógica de filtrado
    val productosFiltrados = remember(uiState.productos, textoBusqueda, categoriaSeleccionada) {
        uiState.productos.filter { producto ->
            // Filtro por texto (nombre o descripción)
            val coincideTexto = textoBusqueda.isBlank() || 
                producto.nombre.contains(textoBusqueda, ignoreCase = true) ||
                producto.descripcion.contains(textoBusqueda, ignoreCase = true)
            
            // Filtro por categoría
            val coincideCategoria = categoriaSeleccionada == null || 
                producto.categoria == categoriaSeleccionada
            
            coincideTexto && coincideCategoria
        }
    }
    
    // Lista de categorías únicas
    val categorias = remember(uiState.productos) {
        uiState.productos.map { it.categoria }.distinct().sorted()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos Disponibles") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A2E), // azul
                    titleContentColor = Color.White,    // color del título
                    navigationIconContentColor = Color.White, // íconos de navegación
                    actionIconContentColor = Color(0xFF39FF14)   // íconos de acción
                ),
                navigationIcon = {
                    IconButton(onClick = onVolverPortada) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver a Portada"
                        )
                    }
                },
                actions = {
                    // Botón de registro
                    IconButton(onClick = onRegistroClick) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Registro"
                        )
                    }

                    // Botón de carrito
                    IconButton(onClick = onCarritoClick) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF0F0F1A))
        ) {
            when {
                // Estado: Cargando
                uiState.estaCargando -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Estado: Error
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.cargarProductos() }) {
                            Text("Reintentar")
                        }
                    }
                }

                // Estado: Lista vacía
                uiState.productos.isEmpty() -> {
                    Text(
                        text = "No hay productos disponibles",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Estado: Éxito - mostrar lista
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Campo de búsqueda
                        OutlinedTextField(
                            value = textoBusqueda,
                            onValueChange = { textoBusqueda = it },

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            placeholder = {
                                Text(
                                    text = "Buscar productos...",
                                    color = Color.White // 👈 aquí defines el color del placeholder
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar",
                                    tint = Color.White
                                )
                            },
                            trailingIcon = {
                                if (textoBusqueda.isNotEmpty()) {
                                    IconButton(onClick = { textoBusqueda = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Limpiar",
                                            tint = Color.White
                                        )
                                    }
                                }
                            },
                            singleLine = true
                        )

                        // Chips de categorías
                        if (categorias.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Chip "Todos"
                                FilterChip(
                                    selected = categoriaSeleccionada == null,
                                    onClick = { categoriaSeleccionada = null },
                                    label = { Text("Todos") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = Color(0xFF340D48),          // fondo cuando NO está seleccionado
                                        selectedContainerColor = Color(0xFF39FF14),     // fondo cuando está seleccionado
                                        labelColor = Color.White,                 // texto cuando NO está seleccionado
                                        selectedLabelColor = Color(0xFF340D48)          // texto cuando está seleccionado
                                    )
                                )

                                // Chip por cada categoría
                                categorias.forEach { categoria ->
                                    FilterChip(
                                        selected = categoriaSeleccionada == categoria,
                                        onClick = {
                                            categoriaSeleccionada = if (categoriaSeleccionada == categoria) {
                                                null
                                            } else {
                                                categoria
                                            }
                                        },
                                        label = { Text(categoria) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            containerColor = Color(0xFF340D48),          // fondo cuando NO está seleccionado
                                            selectedContainerColor = Color(0xFF39FF14),     // fondo cuando está seleccionado
                                            labelColor = Color.White,                 // texto cuando NO está seleccionado
                                            selectedLabelColor = Color(0xFF340D48)
                                        )
                                    )
                                }
                            }
                        }

                        // Contador de resultados
                        if (textoBusqueda.isNotEmpty() || categoriaSeleccionada != null) {
                            Text(
                                text = "${productosFiltrados.size} resultado(s)",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }

                        // Lista de productos filtrados
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(productosFiltrados) { producto ->
                                ProductoCard(
                                    producto = producto,
                                    onClick = { onProductoClick(producto.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Card de producto: Muestra info básica del producto con imagen
 */
@Composable
fun ProductoCard(
    producto: Producto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A2E), // azul claro
            contentColor = Color.White         // color del texto dentro
        )


    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto con Coil
            val context = LocalContext.current
            val imageResId = context.resources.getIdentifier(
                producto.imagenUrl, // "mouse_gamer"
                "drawable",
                context.packageName
            )

            // Debug log
            Log.d("HomeScreen", "Producto: ${producto.nombre}, imagenUrl: ${producto.imagenUrl}, resId: $imageResId")

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(if (imageResId != 0) imageResId else producto.imagenUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                onError = { error ->
                    Log.e("HomeScreen", "Error cargando imagen: ${error.result.throwable.message}")
                }
            )

            // Información del producto
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = producto.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = producto.categoria,
                    fontSize = 15.sp,
                    color = Color(0xFFFFFFFF)
                )
                Text(
                    text = producto.precioFormateado(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF39FF14)
                )
            }

            // Stock disponible
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (producto.hayStock) {
                    Text(
                        text = "Stock: ${producto.stock}",
                        fontSize = 15.sp,
                        color = Color(0xFFC9C9C9)
                    )
                } else {
                    Text(
                        text = "Sin stock",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}