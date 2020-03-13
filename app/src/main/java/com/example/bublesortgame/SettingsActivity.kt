package com.example.bublesortgame

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bublesortgame.Model.Difficulty
import com.example.bublesortgame.Model.Game
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.title = "Settings"
        mute.isChecked = GameField.mute
        when (Game.chosenDifficult)
        {
            Difficulty.getEasyDifficulty() -> easyBut.isChecked = true
            Difficulty.getStandardDifficulty() -> standBut.isChecked = true
            Difficulty.getHardDifficulty() -> hardBut.isChecked = true
        }
    }

    fun onDifficultyChecked(v : View) {
        when (dif.checkedRadioButtonId)
        {
            R.id.easyBut -> Game.chosenDifficult = Difficulty.getEasyDifficulty()
            R.id.standBut -> Game.chosenDifficult = Difficulty.getStandardDifficulty()
            R.id.hardBut -> Game.chosenDifficult = Difficulty.getHardDifficulty()
        }
    }

    fun onMute(v : View)
    {
        GameField.mute = mute.isChecked
    }

}
