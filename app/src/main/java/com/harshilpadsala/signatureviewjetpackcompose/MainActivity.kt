package com.harshilpadsala.signatureviewjetpackcompose

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.harshilpadsala.signatureviewjetpackcompose.ui.theme.SignatureViewJetpackComposeTheme
import se.warting.signaturepad.SignaturePadAdapter
import se.warting.signaturepad.SignaturePadView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignatureViewJetpackComposeTheme (
                darkTheme = true
            ){
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        WatchListXNav(context = LocalContext.current)
                    }
                }
        }
    }
}

@Composable
fun WatchListXNav(context: Context){

    val  navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ){
        composable("home") {
            HomeCompose(
                onSignClick = {
                    navController.navigate("sign")
                }
            )
        }

        composable("sign") {
            SignCompose(context = context)
        }
    }
}



