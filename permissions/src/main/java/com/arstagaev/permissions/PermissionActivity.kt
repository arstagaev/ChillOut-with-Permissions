package com.arstagaev.permissions

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arstagaev.permissions.ExternalParameters.ACTIVITY_AFTER_GRANT_PERMISSIONS
import com.arstagaev.permissions.ExternalParameters.preparedPermissions
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.*

data class NeededPermission( val code : String,var explanation : String,var isAllowed : Boolean )

@OptIn(ExperimentalPagerApi::class)
class PermissionActivity : ComponentActivity() {

    val TAG = "PermissionActivity"
    var arrayOfPermissions = mutableStateListOf<NeededPermission>()

    private var timer = object : CountDownTimer(90000,600){
        override fun onTick(p0: Long) {

            Log.w("ccc","ccc ${arrayOfPermissions.size}")
            refreshNeedAllowingPermissions(3000)
        }

        override fun onFinish() {

        }

    }
    private fun refreshNeedAllowingPermissions(DELAY: Long) {
        arrayOfPermissions.clear()

        for (i in preparedPermissions) {

            if (i.code.contains("android.permission",ignoreCase = true)) {
                if (!hasPermission(i.code)) {
                    arrayOfPermissions.add(NeededPermission(i.code, explanation = i.explanation,isAllowed = false))
                }
            } else if (i.code.contains("android.settings",ignoreCase = true)) {
                if (i.code.contains("MANAGE_ALL_FILES_ACCESS_PERMISSION",ignoreCase = true)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (!checkStoragePermissionApi30(this)) {
                            arrayOfPermissions.add(NeededPermission(i.code,explanation = i.explanation,isAllowed = false))
                        }
                    }
                }else {
                    if (!isGPSEnabled(this) ) {
                        arrayOfPermissions.add(NeededPermission(i.code,explanation = i.explanation,isAllowed = false))
                    }
                }
            }
        }
        checkAllPermissionsAllowed(DELAY)
    }

    fun checkAllPermissionsAllowed(DELAY : Long) {
        if (arrayOfPermissions.size == 0) {
            Toast.makeText(applicationContext,"All permissions allowed",Toast.LENGTH_SHORT).show()
            timer.cancel()
            // to work activity go
            if (ACTIVITY_AFTER_GRANT_PERMISSIONS != null) {
                val intent = Intent(this@PermissionActivity,ACTIVITY_AFTER_GRANT_PERMISSIONS!!::class.java)

                CoroutineScope(Dispatchers.Main).launch {
                    delay(DELAY)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(applicationContext,"NO DEFINED ACTIVITY_AFTER_GRANT_PERMISSIONS::class.java",Toast.LENGTH_LONG).show()
                Log.e(TAG, "NO DEFINED ACTIVITY_AFTER_GRANT_PERMISSIONS::class.java")
            }


            //finish()
        }
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
        Log.i("ppp","ppp onDestroy")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        preparedPermissions.add(NeededPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,"permission to read external storage",false))
//        preparedPermissions.add(NeededPermission(Manifest.permission.ACCESS_FINE_LOCATION,"permission to define current location",false))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            preparedPermissions.add(NeededPermission(Manifest.permission.BLUETOOTH_SCAN,"permission for using bluetooth scanning",false))
//            preparedPermissions.add(NeededPermission(Manifest.permission.BLUETOOTH_CONNECT,"permission for using bluetooth connection",false))
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            preparedPermissions.add(NeededPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION,"permission to define current location in background",false))
//        }
//        preparedPermissions.add(NeededPermission(Settings.ACTION_LOCATION_SOURCE_SETTINGS,"permission to define current location in background ",false))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            preparedPermissions.add(NeededPermission(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION,"permission to read files on phone",false))
//        }
        timer.start()


//        arrayOfPermissions.map {
//            it.isAllowed = hasPermission( it.code)
//        }
        refreshNeedAllowingPermissions(0)
        setContent {
            //ChillOutPermisionsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    HorizontPagerPermissions()

                }
            //}
        }
    }


    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun HorizontPagerPermissions() {

        var a = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()
        var internalArr = remember {
            arrayOfPermissions
        }
        HorizontalPager(count = internalArr.size ,state = a) { page ->
            Log.i("mmm","mmm>>>> ${page}")
            Card(
                Modifier
                    .fillMaxSize()
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
                    Text(text = "Please allow \n${internalArr[page].explanation}",color = Color.Black,modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                        textAlign = TextAlign.Center, fontSize = 30.sp)


                    Button(onClick = {
                        //refreshNeedAllowingPermissions()
                        multiRequest(internalArr[page].code,page)
                        //requestPermission()



//                        coroutineScope.launch  {
//                            delay(3000)
//                            var new = page+1
//                            if (new > arrayOfPermissions.size-1) {
//                                new = arrayOfPermissions.size-1
//                            }
//                            a.animateScrollToPage(new)
//                        }
                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .align(Alignment.BottomCenter)
                            .padding(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF4E5C6B),
                            contentColor = Color.White),
                        //.padding(start = 10.dp, top = 20.dp)
                    ) {
                        Text(
                            text = "Allow Permission",
                            fontSize = 10.sp,fontFamily = FontFamily.Default
                        )
                    }


                }
                // Card content
            }
        }
    }

    fun multiRequest(request: String, page: Int) {
        if (request.contains("android.permission",ignoreCase = true)) {
            requestPermission(request,page)
        } else if (request.contains("android.settings",ignoreCase = true)) {
            val intent1 =  Intent(request);
            startActivity(intent1);
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
            //textview.setText("GPS Is Disabled")
        }
    }
}

