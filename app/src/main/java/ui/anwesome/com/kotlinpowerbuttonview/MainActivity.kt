package ui.anwesome.com.kotlinpowerbuttonview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.powerbuttonview.PowerButtonView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PowerButtonView.create(this)
    }
}
