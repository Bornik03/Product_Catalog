package com.example.zynetic_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

class ProductDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productId = intent.getIntExtra("productId", -1)
        setContent {
            MaterialTheme {
                if (productId != -1) {
                    ProductDetailsScreen(productId)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Invalid Product ID")
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailsScreen(productId: Int) {
    var product by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(productId) {
        product = productApi.getProduct(productId)
    }

    if (product != null) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = rememberAsyncImagePainter(product!!.images.firstOrNull() ?: product!!.thumbnail),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(product!!.title, style = MaterialTheme.typography.h6)
            Spacer(Modifier.height(8.dp))
            Text(product!!.description)
            Spacer(Modifier.height(8.dp))
            Text("Category: ${product!!.category}")
            Text("Rating: ${product!!.rating}")
            Text("Price: \$${product!!.price}", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Description:")
            Text(product!!.description)
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
