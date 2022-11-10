package com.example.fragment_compose_internal_runtime_error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, ComposeFragment(), "childTag")
            .commit()
    }

    fun removeFragment() {
        supportFragmentManager.beginTransaction()
            .remove(supportFragmentManager.findFragmentByTag("childTag")!!)
            .commitNow()
            // Does not crash with commit() instead of commitNow()
            //.commit()
    }
}

class ComposeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(Color.Red)
                        .fillMaxSize()
                ) {
                    var someState by remember { mutableStateOf<Int?>(null) }

                     if (someState != null) {
                         (requireContext() as MainActivity).removeFragment()
                     }

                    // Does not crash when inside a LaunchedEffect
                    /*
                    LaunchedEffect(someState) {
                        if (someState != null) {
                            (requireContext() as MainActivity).removeFragment()
                        }
                    }
                     */

                    Text(
                        text = "Remove fragment",
                        modifier = Modifier.clickable { someState = 1 }
                    )
                }
            }
        }
    }
}
