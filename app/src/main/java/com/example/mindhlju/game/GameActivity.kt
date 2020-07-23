package com.example.mindhlju.game.GameActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.mindhlju.R
import com.example.mindhlju.game.GameView2048
import com.example.mindhlju.game.SharePreferenceUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.content.view.*
import kotlinx.android.synthetic.main.content_main.*

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        // 显示返回按钮
        actionBar?.setDisplayHomeAsUpEnabled(true)
        // 去掉logo图标
        actionBar?.setDisplayShowHomeEnabled(false)
        actionBar?.title = "分享图片"

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
//        asd.setOnClickListener({ System.out.println("(.java:24)" + "" + "-- > " + it!!.toString()) })
        gameview2048.setOnGameNotifyListener(object : GameView2048.OnGameNotifyListener {
            override fun onscore(score: Int) {
                score_now.text = "" + score;
            }

            override fun onhighestscore(highscore: Int) {
                score_history_best.text = "" + highscore;
            }
        });
        revoke.setOnClickListener { gameview2048.revoke() }
        score_now.text = "" + SharePreferenceUtil.getIntValue(this, "score");
        score_history_best.text = "" + SharePreferenceUtil.getIntValue(this, "highScore");
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return false
            }
            R.id.action_clear -> gameview2048.clearCache()
            R.id.action_new_game -> gameview2048.newGame()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        gameview2048.setCache() //设置缓存
    }

    // 包裹范围内 属于静态方法
    companion object {

        fun actionStart(activity: Activity) {
            activity.startActivity(Intent(activity, GameActivity::class.java));
        }
    }
}
