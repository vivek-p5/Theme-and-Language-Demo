package com.example.localdemo

import android.app.LocaleManager
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.os.LocaleListCompat
import com.example.localdemo.ui.theme.LocalDemoTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFECYCLE", "onCreate: ")
        enableEdgeToEdge()
        setContent {
            val preferencesManager = remember { PreferencesManager(this) }
            var themeType = remember { mutableIntStateOf(preferencesManager.getInt("theme",0)) }
            LocalDemoTheme(themeType = themeType.intValue) {

                val isShowSettingDialog = remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    },
                    topBar = {
                        TopAppBar(
                            title = { Text(getString(R.string.test)/*"Material3"*/) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            actions = {
                                Icon(modifier = Modifier.clickable {
                                    isShowSettingDialog.value = true
                                }, imageVector = Icons.Outlined.Settings, contentDescription = null)
                            }
                        )
                    }
                ) { innerPadding ->
                    LazyColumn(modifier = Modifier.padding(innerPadding)) {
                        items(20) {
                            ImageCard(
                                title = "Bacon ipsum",
                                description = "Bacon ipsum dolor amet pork shankle beef andouille ball tip. Meatball corned beef swine, strip steak bacon jerky doner tongue biltong pork loin drumstick sausage hamburger burgdoggen.",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                if (isShowSettingDialog.value) {
                    Dialog(onDismissRequest = { isShowSettingDialog.value = false }) {
                        val selectedIndex = remember { mutableIntStateOf(preferencesManager.getInt("theme",0)) }
                        val themes = listOf<String>(
                            "Dynamic Dark Color",
                            "Dynamic Light Color",
                            "Dark Color",
                            "Light Color",
                            "Blue Color"
                        )
                        val languages = listOf<Pair<String, String>>(
                            "English" to "en",
                            "Spanish" to "es"
                        )
                        var selectedLanguageIndex by remember { mutableIntStateOf(preferencesManager.getInt("language",0)) }
                        Column(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(12.dp)
                                )
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(12.dp)

                        ) {

                            Text("Theme", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(Modifier.height(8.dp))
                            themes.forEachIndexed { i, theme ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(i == selectedIndex.intValue, onClick = {
                                        selectedIndex.intValue = i
                                        preferencesManager.saveInt("theme",i)
                                        themeType.intValue = i
                                    })
                                    Text(theme, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("Language", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(Modifier.height(8.dp))
                            languages.forEachIndexed { i,language ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(i == selectedLanguageIndex, onClick = {
                                        selectedLanguageIndex = i
                                        preferencesManager.saveInt("language",selectedLanguageIndex)
                                        // change language

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                                            getSystemService(LocaleManager::class.java).applicationLocales =
                                                LocaleList.forLanguageTags(language.second)
                                        }else{
                                            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language.second)
                                            AppCompatDelegate.setApplicationLocales(appLocale)
                                        }
                                    })
                                    Text(language.first, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("LIFECYCLE", "onRestart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LIFECYCLE", "onResume: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LIFECYCLE", "onDestroy: ")
    }
}