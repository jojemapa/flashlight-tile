package pjdepractica.quicksettingstile

import android.content.Intent
import android.content.pm.PackageManager
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class FlashlightTileService: TileService() {

    override fun onClick() {
        super.onClick()

        if(applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){

            val tile = qsTile

            if (FlashlightService.isLightOn) {
                stopService(Intent(this, FlashlightService::class.java))
                tile.state = Tile.STATE_INACTIVE
            } else {
                startService(Intent(this, FlashlightService::class.java))
                tile.state = Tile.STATE_ACTIVE
            }

            tile.updateTile()

        }

    }

    override fun onStartListening() {
        super.onStartListening()

        with(qsTile){
            state = if (FlashlightService.isLightOn) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            updateTile()
        }

    }

}