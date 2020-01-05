package io.kaendagger.vaastrial

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.vdocipher.aegis.media.ErrorDescription
import com.vdocipher.aegis.player.VdoPlayer

class VdoService : Service(){

    override fun onBind(intent: Intent): IBinder {
        return VdoBinder()
    }

    private lateinit var vdoPlayer: VdoPlayer
    private var vdoInator :VdoInatorCallBack? = null

    fun setVdoInator(vdoInatorCallBack: VdoInatorCallBack){
        this.vdoInator = vdoInatorCallBack
    }

    val vdoInitializationCallBack = object : VdoPlayer.InitializationListener{
        override fun onInitializationSuccess(
            p0: VdoPlayer.PlayerHost?,
            p1: VdoPlayer,
            p2: Boolean
        ) {
            vdoPlayer = p1
        }

        override fun onInitializationFailure(p0: VdoPlayer.PlayerHost?, p1: ErrorDescription?) {

        }
    }

     public interface VdoInatorCallBack{
        fun inator(vdoInitialzationCallBack :VdoPlayer.InitializationListener)
    }

    inner class VdoBinder:Binder(){
        fun getService():VdoService = this@VdoService
        fun getVdoPlayerInstance() = vdoPlayer
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = it.getStringExtra("action")
            when(action){
                "InitLoad" ->{
                    vdoPlayer.load(getSampleVdoInitParams())
                }
                "Play" -> {
                    vdoPlayer.playWhenReady = true
                }
                "Pause" ->{
                    vdoPlayer.playWhenReady = false
                }
            }
        }
        return START_STICKY_COMPATIBILITY
    }


    private fun getSampleVdoInitParams():VdoPlayer.VdoInitParams{
        val otp = "20150519versASE31ba8fc50a0ac49b8e74b9c40f49e099755cd36dc8adccaa3"
        val playBackInfo = "eyJ2aWRlb0lkIjoiM2YyOWI1NDM0YTVjNjE1Y2RhMThiMTZhNjIzMmZkNzUifQ=="
        return VdoPlayer.VdoInitParams.createParamsWithOtp(otp,playBackInfo)
    }
}
