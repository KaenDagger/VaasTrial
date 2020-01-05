package io.kaendagger.vaastrial
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.vdocipher.aegis.player.VdoPlayer
import com.vdocipher.aegis.player.VdoPlayerSupportFragment


class MainActivity : AppCompatActivity() {


    private var vdoFrag : VdoPlayerSupportFragment? = null
    private var vdoService :VdoService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vdoFrag = supportFragmentManager.findFragmentById(R.id.online_vdo_player_fragment) as VdoPlayerSupportFragment

        val intent = Intent(this, VdoService::class.java)
        bindService(intent,connection,Context.BIND_AUTO_CREATE)

        // Actions could be integrated with VdoPlayerControlView
        startService(Intent(this,VdoService::class.java)
            .apply { putExtra("action","InitLoad") })

    }

    private val connection = object :ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service is VdoService.VdoBinder){
                vdoService = service.getService()
                vdoService?.setVdoInator(object : VdoService.VdoInatorCallBack{
                    override fun inator(vdoInitialzationCallBack: VdoPlayer.InitializationListener) {
                       vdoFrag?.initialize(vdoInitialzationCallBack)
                    }
                })
            }
        }
    }
}
