package com.arstagaev.permissions

import android.content.Context
import android.content.Intent
import com.arstagaev.permissions.ExternalParameters.ACTIVITY_AFTER_GRANT_PERMISSIONS
import com.arstagaev.permissions.ExternalParameters.preparedPermissions


object LauncherPermissions {


    fun start(collection: Collection<NeededPermission>,AfterAllActivity : Class<*>, context: Context)  {
        preparedPermissions.addAll(collection)
        ACTIVITY_AFTER_GRANT_PERMISSIONS = AfterAllActivity

        val intent = Intent(context,PermissionActivity::class.java)
        context.startActivity(intent)

    }

}

class Log {
    val ASSERT = 7
    val DEBUG = 3
    val ERROR = 6
    val INFO = 4
    val VERBOSE = 2
    val WARN = 5


    fun v(tag: String?, msg: String): Int {
        throw RuntimeException("Stub!")
    }

    fun v(tag: String?, msg: String?, tr: Throwable?): Int {
        throw RuntimeException("Stub!")
    }

    fun d(tag: String?, msg: String): Int {
        throw RuntimeException("Stub!")
    }

    fun d(tag: String?, msg: String?, tr: Throwable?): Int {
        throw RuntimeException("Stub!")
    }

    fun i(tag: String?, msg: String): Int {
        throw RuntimeException("Stub!")
    }

    fun i(tag: String?, msg: String?, tr: Throwable?): Int {
        throw RuntimeException("Stub!")
    }

    fun w(tag: String?, msg: String): Int {
        throw RuntimeException("Stub!")
    }

    fun w(tag: String?, msg: String?, tr: Throwable?): Int {
        throw RuntimeException("Stub!")
    }

    external fun isLoggable(var0: String?, var1: Int): Boolean
}