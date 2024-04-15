package com.stu74526.project_74526

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.stu74526.project_74526.ui.theme.DorsetColor
import kotlin.math.roundToInt

var totalCart = 0.0

@Composable
fun CartMain(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar()
        OrderBody()
        BottomBarGlobal(home = { navController.navigate(Routes.HomePage.route) })
    }
}

@Composable
fun OrderBody() {

    Column(
        modifier = Modifier.fillMaxHeight(0.8f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        val totalM = remember { mutableDoubleStateOf(totalCart) }
        // Utilisez un LaunchedEffect pour observer les changements de productsCart
        LaunchedEffect(productsCart) {
            Log.d(ContentValues.TAG, "productsCart changed: $productsCart")
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxHeight(0.8f)
        )
        {
            productsCart.keys.forEach {
                val product = allProducts[it]
                if (product != null) {
                    item {
                        CardProductOrder(
                            product = product,
                            quantity = productsCart[it]!!,
                            totalM
                        )
                    }
                    Log.d(ContentValues.TAG, "Recompose: $product")
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        )
        {

            Column {
                Text(
                    text = "Total", fontSize = 23.sp, color = Color.Gray,
                    fontWeight = FontWeight(500)
                )

                val euros = totalM.doubleValue.toInt()
                val cents = ((totalM.doubleValue - euros) * 100).toInt()

                Row()
                {
                    Text(
                        text = "$euros",
                        fontSize = 25.sp,
                        color = Color.Black,
                        fontWeight = FontWeight(700)
                    )
                    Text(
                        text = "€${cents.toString().padStart(2, '0')}",
                        fontSize = 22.sp,
                        color = Color.Black,
                        fontWeight = FontWeight(700)
                    )
                }
            }

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(DorsetColor),
                modifier = Modifier.clip(MaterialTheme.shapes.small)
            ) {
                Text(
                    text = "Pay Now", color = Color.White, fontSize = 23.sp,
                    fontWeight = FontWeight(600),
                    modifier = Modifier.padding(
                        start = 22.dp,
                        end = 22.dp,
                        top = 15.dp,
                        bottom = 15.dp
                    )
                )
            }
        }
    }
}

@Composable
fun CardProductOrder(product: Product, quantity: Int, totalM: MutableDoubleState) {
    var actualQuantity: MutableIntState = remember(product.id) { mutableIntStateOf(quantity) }

    val price = (product.price * actualQuantity.intValue)
    val euros = price.toInt()
    val cents = ((price - euros) * 100).toInt()

    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .height(80.dp)
            .border(1.dp, Color.LightGray, MaterialTheme.shapes.large)
            .fillMaxWidth(0.9f)
            .padding(end = 15.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Button(
            onClick = { },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            modifier = Modifier
                .size(80.dp)
                .background(Color.LightGray),
            contentPadding = PaddingValues(5.dp)
        )
        {
            ShowImageString(
                drawable = product.image,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Row(modifier = Modifier.padding(start = 10.dp))
        {
            Column {
                Text(
                    text = product.name, fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Row()
                {
                    Text(text = "Total: $euros", fontSize = 15.sp)
                    Text(text = "€${cents.toString().padStart(2, '0')}", fontSize = 12.sp)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Button(
                    onClick = {
                        productsCart.remove(product.id)
                        totalCart -= price
                        totalM.doubleValue = totalCart
                        removeProductCart(product.id)
                    },
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .height(30.dp)
                        .width(30.dp)
                        .padding(0.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(Color.Red),
                ) {
                    Text(
                        text = "X", fontSize = 12.sp, fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center, color = Color.White
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                )
                {

                    if (actualQuantity.intValue > 1) {
                        ButtonsQuantity(onClick = {
                            actualQuantity.intValue -= 1
                            productsCart[product.id] = actualQuantity.intValue
                            totalCart -= product.price
                            totalM.doubleValue = totalCart
                            val productCartData: HashMap<String, Any?> = hashMapOf(
                                "product_id" to product.id,
                                "user_id" to userId,
                                "quantity" to actualQuantity.intValue,
                            )
                            updateProductCart(productCartData)
                        }, "-")
                    } else {
                        ButtonsQuantity(onClick = {

                        }, "-", enabled = false)
                    }
                    Text(
                        text = actualQuantity.intValue.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            start = 10.dp,
                            end = 10.dp
                        )
                    )
                    ButtonsQuantity(onClick = {
                        actualQuantity.intValue += 1
                        productsCart[product.id] = actualQuantity.intValue
                        totalCart += product.price
                        totalM.doubleValue = totalCart
                        val productCartData: HashMap<String, Any?> = hashMapOf(
                            "product_id" to product.id,
                            "user_id" to userId,
                            "quantity" to actualQuantity.intValue,
                        )
                        updateProductCart(productCartData)
                    }, "+")
                }

            }

        }
    }
}
