package com.arstagaev.chilloutpermisions

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arstagaev.chilloutpermisions.ui.theme.ChillOutPermisionsTheme
import com.arstagaev.permissions.ExternalParameters
import com.arstagaev.permissions.LauncherPermissions
import com.arstagaev.permissions.NeededPermission

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            ChillOutPermisionsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var preparedPermissionsx = arrayListOf<NeededPermission>()
                    preparedPermissionsx.add(NeededPermission(Manifest.permission.ACCESS_FINE_LOCATION,"permission to define current location)))",false))

                    LauncherPermissions.start(preparedPermissionsx,MainActivity::class.java,this)
                }
            }
        }
    }
}

