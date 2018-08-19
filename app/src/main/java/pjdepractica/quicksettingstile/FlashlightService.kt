package pjdepractica.quicksettingstile

import android.app.Service
import android.content.Intent
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.Camera.Parameters
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.io.IOException

class FlashlightService : Service() {

    companion object { var isLightOn = false }

    val TAG = "FlashlightService"
    var camera: Camera? = null
    var params: Parameters? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("Start", "Service is starting")
        if (camera == null) Log.i(TAG, "camera is null")
        getCamera()
        try {
            turnOnFlash()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Service.START_STICKY
    }

    private fun getCamera() {
        if (camera == null)
            try {
                camera = Camera.open()
                params = camera!!.parameters
            } catch (e: RuntimeException) {
                Log.e(TAG, "Error: " + e.message)
                Toast.makeText(this, "Camera unavailable", Toast.LENGTH_LONG).show()
            }
    }

    private fun turnOnFlash() {
        if (!isLightOn && camera != null && params != null)
            Log.i(TAG, "Turning on flash")
            with(camera!!){
                setPreviewTexture(SurfaceTexture(0))
                startPreview()
                params!!.flashMode = "torch"
                parameters = params
                isLightOn = true
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        params = camera!!.parameters
        params!!.flashMode = "off"
        with(camera!!){
            parameters = params
            stopPreview()
            isLightOn = false
            release()
        }
        camera = null
        Log.i(TAG, "FlashlightService is ending")
    }

    override fun onBind(arg0: Intent): IBinder? = null

}
