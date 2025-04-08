package com.example.zynetic_assignment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Product Catalog") }
                        )
                    }
                ) {
                    ProductListScreen { productId ->
                        val intent = Intent(this, ProductDetailsActivity::class.java)
                        intent.putExtra("productId", productId)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}

data class ProductResponse(val products: List<Product>)
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): ProductResponse

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://dummyjson.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val productApi = retrofit.create(ProductApi::class.java)

@Composable
fun ProductListScreen(onProductClick: (Int) -> Unit) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }

    LaunchedEffect(Unit) {
        products = productApi.getProducts().products
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(products) { product ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable { onProductClick(product.id) }
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(product.thumbnail),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(product.title, fontWeight = FontWeight.Bold)
                        Text(
                            product.description,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
