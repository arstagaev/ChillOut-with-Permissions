package com.arstagaev.chilloutpermisions

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.arstagaev.chilloutpermisions.ui.theme.ChillOutPermisionsTheme
import com.arstagaev.permissions.CoreOfChillOut
import com.arstagaev.permissions.CoreOfChillOut.Companion.preparedPermissions
import com.arstagaev.permissions.models.NeededPermission

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /**
         * Should put some needed permissions in preparedPermissions array
         */
        preparedPermissions.add(NeededPermission(Manifest.permission.ACCESS_FINE_LOCATION,"permission to define current location"))
        preparedPermissions.add(NeededPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION,"permission to define current location, but worked in background"))
        preparedPermissions.add(NeededPermission(Manifest.permission.CAMERA,"permission for camera"))
        var core = CoreOfChillOut(this) // initialize core module


        setContent {
            ChillOutPermisionsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CoreOfChillOut.HorizontalPagerPermissions(this@MainActivity)
                }
            }
        }
    }
}

