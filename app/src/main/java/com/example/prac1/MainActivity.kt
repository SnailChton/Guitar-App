package com.example.prac1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavGraphNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.prac1.ui.theme.Prac1Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Frame()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Frame () {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val items = listOf(
        DrawerItem (Icons.Default.List, "Уроки"),
        DrawerItem (Icons.Default.AccountBox, "Профиль")
    )
    val selectedItem = remember {
        mutableStateOf(items[0])
    }
    val navController = rememberNavController()
    val appBarTitle = remember {
        mutableStateOf("Some Screen")
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Guitar App",
                        fontSize = 24.sp
                    )
                }
                Divider()
                items.forEach {item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = selectedItem.value == item,
                        icon = {
                               Icon(imageVector = item.imageVector, contentDescription = "")
                        },
                        onClick = {
                            scope.launch { 
                                selectedItem.value = item
                                appBarTitle.value = item.title
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Text(appBarTitle.value)
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) {
            LessonsList()
        }
    }
}
@Composable
fun Lesson (name: String) {
    val successState = remember {
        mutableStateOf(false)
    }
    val successColor = remember {
        mutableStateOf(Color.White)
    }
    val successText = remember {
        mutableStateOf("Не завершено")
    }

    Card (
        colors = CardDefaults.cardColors(
            containerColor = successColor.value
        ),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable {
                successState.value = !successState.value
                if (successState.value) {
                    successColor.value = Color.Green
                    successText.value = "Завершено"
                } else {
                    successColor.value = Color.White
                    successText.value = "Не завершено"
                }
            },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box (
            modifier = Modifier.padding(5.dp)
        ) {
            Row () {
                Column () {
                    Text (
                        text = name,
                        fontSize = 20.sp
                    )
                    Text(text = successText.value)
                }
            }
        }
    }
}
@Composable
fun LessonsList () {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp)
    ) {
        itemsIndexed(
            listOf("Введение.", "Ноты.", "Постановка.", "Аккорды.", "Перебор.", "Бой.", "Табулатура.", "Перкуссия.")
        ) { index, item ->
            Lesson(name = "Урок $index. $item")
        }
    }
}

data class DrawerItem(
    val imageVector: ImageVector,
    val title: String
)