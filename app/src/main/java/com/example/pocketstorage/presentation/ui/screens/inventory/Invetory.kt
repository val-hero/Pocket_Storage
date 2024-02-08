package com.example.pocketstorage.presentation.ui.screens.inventory

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pocketstorage.R
import com.example.pocketstorage.graphs.BottomBarScreen
import com.example.pocketstorage.graphs.HomeNavGraph
import com.example.pocketstorage.presentation.ui.screens.inventory.viewmodel.InventoryModel
import com.example.pocketstorage.presentation.ui.screens.inventory.viewmodel.InventoryViewModel


/*@Composable
fun Inventory() {

}*/
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Inventory(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        HomeNavGraph(navController = navController)
    }
}

@Composable
@Preview(showBackground = true)
fun InventoryScreenPreview(){
    InventoryScreen(onClick = {}, onClickAdd = {})
}

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun InventoryScreen(onClick: () -> Unit, onClickAdd: () -> Unit) {
    val viewModel = viewModel<InventoryViewModel>()
    val inventory by viewModel.filteredInventory.collectAsState()
    val isSearch by viewModel.isSearching.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .padding(top = 56.dp, start = 24.dp, bottom = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Box(Modifier.weight(1f)) {
                TextFieldSearchInventoryNameOrId(
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .fillMaxWidth(),
                    label = {
                        Text(
                            text = "product name or id",
                            color = colorResource(id = R.color.SpanishGrey)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "SearchById"
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.SpanishGrey),
                        unfocusedBorderColor = colorResource(id = R.color.SpanishGrey)
                    ),
                    viewModel
                )
            }
            ImageQRScanner {
                //Click
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {


            ButtonInventoryScreen(
                modifier = Modifier.size(width = 150.dp, height = 48.dp),
                rowContent = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "addProduct",
                        modifier = Modifier.padding(end = 15.dp)
                    )
                    Text(text = "Product", fontSize = 16.sp)
                },
                onClick = { onClickAdd() }
            )


        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            ButtonInventoryScreen(modifier = Modifier
                .padding(end = 16.dp)
                .size(width = 150.dp, height = 48.dp),
                rowContent = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Export",
                        modifier = Modifier.padding(end = 15.dp)
                    )
                    Text(text = "Export", fontSize = 16.sp)
                }) {
                //Click

            }

            ButtonInventoryScreen(modifier = Modifier
                .size(width = 150.dp, height = 48.dp),
                rowContent = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Import",
                        modifier = Modifier.padding(end = 15.dp)
                    )
                    Text(text = "Import", fontSize = 16.sp)
                }) {
                //Click

            }

        }
        Text(
            text = "Recently added",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(start = 24.dp, top = 24.dp, bottom = 16.dp)
                .fillMaxWidth()
        )

        //recycler
        if (isSearch){
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else{
            LazyColumn(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp)
                    .background(Color.White)
            ) {
                items(inventory) { inventory ->
                    ListRow(onClick, model = inventory)
                }
            }
        }


    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldSearchInventoryNameOrId(
    modifier: Modifier,
    label: @Composable () -> Unit,
    leadingIcon: @Composable () -> Unit,
    colors: TextFieldColors,
    viewModel: InventoryViewModel
) {
    val searchText by viewModel.searchText.collectAsState()
    OutlinedTextField(
        modifier = modifier,
        value = searchText,
        onValueChange = { newText->
            viewModel.onSearchTextChange(newText)
        },
        label = label,
        leadingIcon = leadingIcon,
        colors = colors
    )
}

@Composable
fun ImageQRScanner(onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.qr_scanner),
        contentDescription = "qr",
        modifier = Modifier
            .size(48.dp)
            .clickable {
                onClick()
            },
    )
}

@Composable
fun ButtonInventoryScreen(
    modifier: Modifier,
    rowContent: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.RetroBlue)),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            rowContent()
        }
    }
}

@Composable
fun ListRow(onClick: () -> Unit, model: InventoryModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .wrapContentHeight()
            .fillMaxWidth()
            .background(colorResource(id = R.color.AdamantineBlue))
            .clickable { onClick() },
    ) {
        Image(
            painter = painterResource(id = model.image),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .padding(5.dp)
        )
        Text(
            text = model.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Inventory,
        BottomBarScreen.Category,
        BottomBarScreen.Building,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        BottomNavigation(
            modifier = Modifier.border(
                border = BorderStroke(0.dp, colorResource(id = R.color.RetroBlue)),
                shape = RoundedCornerShape(8.dp)
            ),
            backgroundColor = colorResource(id = R.color.RetroBlue),
            contentColor = colorResource(id = R.color.SpanishGrey)

        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        icon = {
            screen.icon()
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = colorResource(id = R.color.SpanishGrey),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        selectedContentColor = Color.White
    )
}