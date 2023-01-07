package dev.diegodc.chefio.features.map

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun RecipeMapScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    position: LatLng
){

    val cameraPositionState = rememberCameraPositionState {
        Log.d("RecipeMapScreen", "Position : $position")
        this.position = CameraPosition.fromLatLngZoom(position, 11f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = MapProperties(),
            uiSettings = MapUiSettings(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = position),
                title = "Recipe location"
            )
        }
    }
}