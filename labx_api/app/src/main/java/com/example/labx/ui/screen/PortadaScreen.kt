package com.example.labx.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labx.R

/**
 * PortadaScreen: Pantalla de bienvenida de la tienda
 * 
 * Funcionalidades:
 * - Muestra nombre y descripción de la tienda
 * - Botón para entrar a productos
 * - Botón para acceder al panel de administración
 * - Información de contacto
 * 
 * Autor: Prof. Sting Adams Parra Silva
 */
@Composable
fun PortadaScreen(
    onEntrarClick: () -> Unit,
    onAdminClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0F0F1A)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono grande de tienda
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la Empresa",
                modifier = Modifier.size(200.dp) // Define un tamaño de 48x48 dp para el logo
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nombre de la tienda
            Text(
                text = "Level-Up",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF39FF14)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Slogan
            Text(
                text = "Tu tienda gaming de confianza",
                fontSize = 18.sp,
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Descripción
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A2E)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎮 Equipamiento Gaming",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFFFFFF)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Periféricos • Consolas • Juegos de Mesa\nMonitores • Computadores • Sillas Gamer",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Botón principal
            Button(
                onClick = onEntrarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5A067E)
                )
            ) {
                Text(
                    text = "ENTRAR A LA TIENDA",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Admin
            OutlinedButton(
                onClick = onAdminClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF340D48)
                )

            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Panel Administrador",
                    fontSize = 16.sp,
                    color = Color(0xFFFFFFFF)

                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info adicional
            Text(
                text = "Vicente Tapia • 2025",
                fontSize = 12.sp,
                color = Color(0xFFFFFFFF)
            )
        }
    }
}
