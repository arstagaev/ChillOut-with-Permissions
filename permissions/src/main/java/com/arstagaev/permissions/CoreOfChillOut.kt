package com.arstagaev.permissions

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.arstagaev.permissions.models.NeededPermission
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoreOfChillOut(context: ComponentActivity) {
    private var internalContext : ComponentActivity? = null
    private val TAG = "CoreOfChillOut"
    init {
        internalContext = context

        looperInspector()
        refreshNeedAllowingPermissions()

    }

    private fun looperInspector() {
        if (isWaitForAllowPermissions.value)
            return

        isWaitForAllowPermissions.value = true

        CoroutineScope(Dispatchers.Main).launch {
            while (isWaitForAllowPermissions.value) {
                delay(100L)
                Log.w(TAG,"How many permissions need allow: ${arrayOfPermissions.size}")
                refreshNeedAllowingPermissions()
                delay(500L)
            }
        }
    }

    companion object {

        var preparedPermissions               = arrayListOf<NeededPermission>()
        var arrayOfPermissions                = mutableStateListOf<NeededPermission>()
        private var isWaitForAllowPermissions = mutableStateOf(false)

        @OptIn(ExperimentalPagerApi::class)
        @Composable
        fun HorizontalPagerPermissions(context: ComponentActivity) {
            var pagerState = rememberPagerState()
            var internalArrayOfPermissions = remember {
                arrayOfPermissions
            }

            AnimatedVisibility(visible = isWaitForAllowPermissions.value) {
                Log.i("ccc","ccc ${internalArrayOfPermissions.size}")
                Box(
                    Modifier.fillMaxSize()) {
                    HorizontalPager(modifier = Modifier.fillMaxSize(), count = internalArrayOfPermissions.size ,state = pagerState) { page ->
                        Log.i("mmm","mmm>>>> ${page}")
                        Card(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Green)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(290.dp)
                                    .background(
                                        when (page) {
                                            1 -> {
                                                Color.Red
                                            }
                                            2 -> {
                                                Color.Yellow
                                            }
                                            else -> {
                                                Color.White
                                            }
                                        }
                                    ))
                            {
                                Text(text = "${internalArrayOfPermissions[page].explanation}",color = Color.Black,modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                    textAlign = TextAlign.Center, fontSize = 30.sp)
                                Button(onClick = {
                                    CoreOfChillOut(context).multiRequest(internalArrayOfPermissions[page].code,page,context)
                                },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                        .align(Alignment.BottomCenter)
                                        .padding(30.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFF6CAC3E),
                                        contentColor = Color.White),
                                ) {
                                    Text(
                                        text = "Allow Permission",
                                        fontSize = 10.sp,fontFamily = FontFamily.Default
                                    )
                                }
                            }
                        }
                    }
                }
            }


        }




    }

    fun multiRequest(request: String, page: Int, context: ComponentActivity) {
        if (request.contains("android.permission",ignoreCase = true)) {
            ActivityCompat.requestPermissions(context, arrayOf(request), page)
            //context.requestPermission(request,page)
        } else if (request.contains("android.settings",ignoreCase = true)) {
            val intent1 =  Intent(request);
            startActivity(context,intent1,null);
        }
    }

    val MANAGE_EXTERNAL_STORAGE_PERMISSION = "android:manage_external_storage"
    @RequiresApi(30)
    fun checkStoragePermissionApi30(activity: androidx.core.app.ComponentActivity): Boolean {
        val appOps = activity.getSystemService(AppOpsManager::class.java)
        val mode = appOps.unsafeCheckOpNoThrow(
            MANAGE_EXTERNAL_STORAGE_PERMISSION,
            activity.applicationInfo.uid,
            activity.packageName
        )

        return mode == AppOpsManager.MODE_ALLOWED
    }
    fun isGPSEnabled(context : Context) : Boolean {
        var locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        assert(locationManager != null)
        var GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (GpsStatus === true) {
            return true
        } else {
            Toast.makeText(context,"Please enable GPS", Toast.LENGTH_LONG).show()
            return false
        }
    }




    private fun refreshNeedAllowingPermissions() {
        arrayOfPermissions.clear()

        for (i in preparedPermissions) {

            if (i.code.contains("android.permission",ignoreCase = true)) {
                if (!(ContextCompat.checkSelfPermission(internalContext!!, i.code) ==
                    PackageManager.PERMISSION_GRANTED)) {
                    arrayOfPermissions.add(NeededPermission(i.code, explanation = i.explanation))
                }
            } else if (i.code.contains("android.settings",ignoreCase = true)) {
                if (i.code.contains("MANAGE_ALL_FILES_ACCESS_PERMISSION",ignoreCase = true)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (!checkStoragePermissionApi30(internalContext!!)) {
                            arrayOfPermissions.add(NeededPermission(i.code,explanation = i.explanation))
                        }
                    }
                }else {
                    if (!isGPSEnabled(internalContext!!) ) {
                        arrayOfPermissions.add(NeededPermission(i.code,explanation = i.explanation))
                    }
                }
            }
        }
        checkAllPermissionsAllowed()
    }

     private fun checkAllPermissionsAllowed() {
        if (arrayOfPermissions.size == 0) {

            isWaitForAllowPermissions.value = false
            // All permissions allowed
            Toast.makeText(internalContext,"All permissions allowed",Toast.LENGTH_SHORT).show()

        }
    }


}