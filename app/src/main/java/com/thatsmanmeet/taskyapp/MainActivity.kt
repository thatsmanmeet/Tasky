package com.thatsmanmeet.taskyapp


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.thatsmanmeet.taskyapp.misc.Recursor
import com.thatsmanmeet.taskyapp.room.TodoDatabase
import com.thatsmanmeet.taskyapp.screens.PermissionRequestScreen
import com.thatsmanmeet.taskyapp.screens.Screen
import com.thatsmanmeet.taskyapp.screens.SetupNavGraph
import com.thatsmanmeet.taskyapp.viewmodels.MainViewModel
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Calendar
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    private lateinit var navController : NavHostController
    private val calendar = Calendar.getInstance()
    private val currentDateTime = "${calendar.get(Calendar.YEAR)}${calendar.get(Calendar.MONTH)}${calendar.get(Calendar.DATE)}${calendar.get(Calendar.HOUR_OF_DAY)}${calendar.get(Calendar.MINUTE)}${calendar.get(Calendar.SECOND)}"
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            Scaffold(contentWindowInsets = ScaffoldDefaults.contentWindowInsets) {paddingValues->
                val context = LocalContext.current
                val viewModel = MainViewModel()
                val pageState = remember {
                    mutableStateOf(ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
                }
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
                val notificationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = {isGranted->
                        viewModel.onPermissionResult(
                            permission = Manifest.permission.POST_NOTIFICATIONS,
                            isGranted = isGranted
                        )
                        if (isGranted) {
                            pageState.value = true
                            navController.navigate(route = Screen.MyApp.route) {
                                popUpTo(Screen.PermissionScreen.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                )
                val checkPermission = ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
                    pageState.value = true
                }else {
                    if (checkPermission) {
                        pageState.value = true
                    } else {
                        PermissionRequestScreen(navHostController = navController, requestOnClick = {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }, modifier = Modifier.padding(paddingValues))
                    }
                }
                // If permissions are already accepted.
                if(pageState.value){
                    navController.navigate(route = Screen.MyApp.route){
                        popUpTo(Screen.PermissionScreen.route){
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
    @Deprecated("Deprecated in Kotlin")
    fun writeFile(context: Context, partialUri: Uri) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_TITLE, "Tasky-backup-${currentDateTime}.zip") // Change the backup file name and extension
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, partialUri)
        }
        (context as Activity).startActivityForResult(intent, 1)
    }

    @Deprecated("Deprecated in Kotlin", replaceWith = ReplaceWith("newFunction()"))
    fun restoreFile(context: Context, partialUri: Uri) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, partialUri)
        }
        (context as Activity).startActivityForResult(intent, 2) // Request code is 2 for restore
    }

    @SuppressLint("SdCardPath")
    @Deprecated("Deprecated in Kotlin")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val db = TodoDatabase.getInstance(this)
        val dbPath = db.openHelper.writableDatabase.path
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // Backup operation
                val uri = data?.data ?: return
                try {
                    val outputStream = contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        val zipOutputStream = ZipOutputStream(BufferedOutputStream(outputStream))

                        val inputFile = File(dbPath!!)
                        val shmFile = File("$dbPath-shm")
                        val walFile = File("$dbPath-wal")
                        addFileToZip(inputFile, zipOutputStream, "todo_database.db")
                        addFileToZip(shmFile, zipOutputStream, "todo_database.db-shm")
                        addFileToZip(walFile, zipOutputStream, "todo_database.db-wal")

                        zipOutputStream.flush()
                        zipOutputStream.close()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (requestCode == 2) {
                // Restore operation
                val uri = data?.data ?: return
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    if (inputStream != null) {
                        val zipInputStream = ZipInputStream(BufferedInputStream(inputStream))

                        var zipEntry: ZipEntry? = zipInputStream.nextEntry
                        while (zipEntry != null) {
                            val entryName = zipEntry.name.replace(".db","")
                            val outputFile = File("/data/user/0/com.thatsmanmeet.taskyapp/databases/$entryName")
                            if (!zipEntry.isDirectory) {
                                val outputStream = FileOutputStream(outputFile)
                                copyStream(zipInputStream, outputStream)
                                outputStream.close()
                            }

                            zipInputStream.closeEntry()
                            zipEntry = zipInputStream.nextEntry
                        }

                        zipInputStream.close()
                        val reschedule = Recursor()
                        reschedule.rescheduleTasks(this)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        if(currentRoute == Screen.MyApp.route){
            AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Dialog)
                .setTitle("Confirm Exit ?")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes") { _, _ ->
                    //super.onBackPressed()
                    finish()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }
        else{
            super.onBackPressed()
        }
    }

    @Throws(IOException::class)
    private fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024 * 8)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
    }

    @Throws(IOException::class)
    private fun addFileToZip(file: File, zipOutputStream: ZipOutputStream, entryName: String) {
        val buffer = ByteArray(1024 * 8)
        val fileInputStream = FileInputStream(file)
        val bufferedInputStream = BufferedInputStream(fileInputStream)

        val zipEntry = ZipEntry(entryName)
        zipOutputStream.putNextEntry(zipEntry)

        var bytesRead: Int
        while (bufferedInputStream.read(buffer).also { bytesRead = it } != -1) {
            zipOutputStream.write(buffer, 0, bytesRead)
        }

        bufferedInputStream.close()
        zipOutputStream.closeEntry()
    }

}