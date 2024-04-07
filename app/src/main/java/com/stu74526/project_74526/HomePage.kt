package com.stu74526.project_74526

import android.content.ContentValues
import android.util.Log
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import coil.compose.rememberImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

var actualCategory by mutableStateOf("All")

var categories by mutableStateOf(emptyMap<String, String>())
var products by mutableStateOf(emptyMap<String, String>())

@Composable
fun DisplayImageFromUrl(url: String) {
    val painter = rememberImagePainter(data = url)

    Image(
        painter = painter,
        contentDescription = "Image from URL",
        modifier = Modifier
            .size(75.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun HomePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
        {
            AppBar()
            SearchBar()
            Category()
        }
        BottomAppBar {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ShowImage(drawable = R.drawable.home, modifier = Modifier.size(50.dp))
                ShowImage(drawable = R.drawable.historical, modifier = Modifier.size(50.dp)
                    .clickable { /* Handle category click */ })
                ShowImage(drawable = R.drawable.cart, modifier = Modifier.size(50.dp)
                    .clickable { /* Handle cart click */ })
                ShowImage(drawable = R.drawable.profile, modifier = Modifier.size(50.dp)
                    .clickable { /* Handle profile click */ })
            }
        }
    }
}

@Composable
fun AppBar()
{
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically)
    {
        Text(
            text = "Online Shopping App",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
        ShowImage(drawable = R.drawable.cart, modifier = Modifier.size(50.dp))
        DisplayImageFromUrl("https://thispersondoesnotexist.com/")
    }
}

@Composable
fun SearchBar()
{
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Box(
        Modifier
            .border(1.dp, Color.DarkGray, CircleShape)
            .fillMaxWidth(0.8f),
        contentAlignment = Alignment.CenterStart)
    {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center)
        {

            BasicTextField(value = text, onValueChange = {
                if (it.text.length <= 35) {
                text = it
            } },
                modifier = Modifier
                    .height(30.dp)
                    .padding(5.dp)
                    .weight(1f),
                )
            ShowImage(drawable =R.drawable.search, modifier = Modifier
                .size(30.dp)
                .padding(5.dp, 5.dp, 10.dp, 5.dp))
        }
    }
}

@Composable
fun Category() {

    LaunchedEffect(Unit) {
        categories = getCategories()
    }

    Row(modifier = Modifier
        .horizontalScroll(rememberScrollState())
        .fillMaxWidth()) {
        categories.forEach { (key, value) ->
            Button(onClick = { actualCategory = key
                Log.d(ContentValues.TAG, "CategoryAAAAAAAAA: $actualCategory")
            }){
                Text(text = value)
            }
        }
    }
    if(categories.isNotEmpty())
    {
        Product()
    }
}

suspend fun getCategories(): Map<String, String> {
    val categories = mutableMapOf<String, String>()

    categoryCollection.get().addOnSuccessListener { documents ->
        for (document in documents) {
            categories[document.id] = document.data[categoryName].toString()
        }
    }.await()

    return categories
}

@Composable
fun Product()
{
    LaunchedEffect(actualCategory) {
        Log.d(ContentValues.TAG, "CategoryLAUCHE: $actualCategory")
        if(actualCategory != "All")
            products = getProductsByCategory()
        else{
            products = getProducts()
        }
    }

    if(products.isNotEmpty())
    {
        val productValue = products.values.toList()
        LazyVerticalGrid(modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight(0.9f),
            columns = GridCells.Adaptive(minSize = 128.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(products.size) { index ->
                val product = productValue[index]
                CardProduct(name = product)
                Log.d(ContentValues.TAG, "Product: $product")
            }
            Log.d(ContentValues.TAG, "Product: $products Size: ${products.size}")
        }
    }

}

suspend fun getProducts(): Map<String, String> {
    val products = mutableMapOf<String, String>()

    productCollection.get().addOnSuccessListener { documents ->
        for (document in documents) {
            products[document.id] = document.data[productName].toString()
        }
    }.await()

    return products
}

suspend fun getProductsByCategory(): Map<String, String> {
    val products = mutableMapOf<String, String>()
    Log.d(ContentValues.TAG, "Category: $actualCategory")
    productCollection.whereEqualTo(productCategoryId, actualCategory).get().addOnSuccessListener { documents ->
        for (document in documents) {
            Log.d(ContentValues.TAG, "Product: ${document.data[productName]}")
            products[document.id] = document.data[productName].toString()
        }
    }.await()

    return products
}

@Composable
fun CardProduct(name : String)
{
    Column(modifier = Modifier
        .clip(MaterialTheme.shapes.large)
        .background(Color.White)
        .width(150.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Button(
            onClick = {},
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            modifier = Modifier
                .padding(0.dp)
                .width(150.dp)
                .height(90.dp),
            contentPadding = PaddingValues(5.dp)
        )
        {
            ShowImage(drawable = R.drawable.pop_lexa, modifier = Modifier.fillMaxSize())
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically)
        {

            Column(modifier = Modifier
                .clickable { }
                .padding(start = 5.dp, bottom = 5.dp)) {
                Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "Product Price", fontSize = 13.sp)
            }
            ShowImage(drawable = R.drawable.heart, modifier = Modifier
                .size(25.dp)
                .padding(end = 5.dp, bottom = 5.dp))
        }
    }
}