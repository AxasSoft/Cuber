package ru.wood.cuber

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import dagger.hilt.android.AndroidEntryPoint
import ru.wood.cuber.databinding.ActivityMainBinding
import ru.wood.cuber.utill.Utill

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var navController: NavController? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = ""
        }

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //light тема
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        for (x in 16..51 ){
            Utill.DIAMETERS?.add(x)
        }

    }
}