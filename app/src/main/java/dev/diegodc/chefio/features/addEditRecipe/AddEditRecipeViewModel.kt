package dev.diegodc.chefio.features.addEditRecipe

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.diegodc.chefio.DestinationsArgs
import dev.diegodc.chefio.data.repositories.IRecipesRepository
import dev.diegodc.chefio.features.addEditRecipe.models.AddEditRecipeState
import dev.diegodc.chefio.models.PreparationStep
import dev.diegodc.chefio.models.Recipe
import dev.diegodc.chefio.models.UserProfile
import dev.diegodc.chefio.data.Result
import dev.diegodc.chefio.data.sources.auth.AuthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditRecipeViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val recipeRepository: IRecipesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val recipeId: String? = savedStateHandle[DestinationsArgs.RECIPE_ID_ARG]

    // Init state
    private val _uiState = MutableStateFlow(AddEditRecipeState())
    val uiState: StateFlow<AddEditRecipeState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var markerJob: Job? = null

    init {
        if (recipeId != null) {
            //TODO : get recipe details to prefill
        }
    }

    fun updatePhotoCover(path: Uri) {
        _uiState.update {
            it.copy(photoPath = path)
        }
    }

    fun updateIngredient(index: Int, ingredient: String) {
        if (index < uiState.value.ingredients.size) {
            _uiState.update {
                it.copy(
                    ingredients = it.ingredients.toMutableList().apply {
                        this[index] = ingredient
                    }
                )
            }
        }

    }

    fun updateStepPreparation(index: Int, step: String) {
        if (index < uiState.value.preparationSteps.size) {
            _uiState.update {
                it.copy(
                    preparationSteps = it.preparationSteps.toMutableList().apply {
                        this[index] = step
                    }
                )
            }
        }

    }


    fun updateTitle(newTitle: String) {
        _uiState.update {
            it.copy(title = newTitle)
        }
    }

    fun updateDescription(newDescription: String) {
        _uiState.update {
            it.copy(description = newDescription)
        }
    }

    fun updateTimeToPrepare(newTime: Float) {
        _uiState.update {
            it.copy(timeToPrepared = newTime)
        }
    }

    fun goToNextPage() {
        _uiState.update {
            it.copy(
                step = it.step + 1
            )
        }
    }

    fun saveRecipe() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        if (recipeId != null) {
            //TODO: edit recipe
        } else {
            viewModelScope.launch {
                val recipe = Recipe(
                    id = "",
                    title = _uiState.value.title,
                    createdAt = Date(),
                    image = "",
                    creator = UserProfile(
                        name = "",
                        lastName = "",
                        username = "",
                        photo = ""
                    ),
                    timeToPrepare = _uiState.value.timeToPrepared.toInt(),
                    ingredients = _uiState.value.ingredients.filter { it.isNotEmpty() },
                    preparationSteps = _uiState.value.preparationSteps.filter { it.isNotEmpty() }
                        .map {
                            PreparationStep(description = it)
                        },
                    description = _uiState.value.description,
                    address = _uiState.value.address,
                    location = _uiState.value.location?.let { LatLng(it.latitude,it.longitude) }
                )
                val result = recipeRepository.saveRecipe(recipe, _uiState.value.photoPath)
                if (result is Result.Success) {
                    Log.d("AddEditRecipeViewModel", "SUCCESS CREATION")
                    _uiState.update {
                        it.copy(isLoading = false, isSaved = true)
                    }
                } else {
                    Log.e("AddEditRecipeViewModel", (result as Result.Error).exception.toString())
                }
            }
        }
    }

    fun addNewIngredient() {
        _uiState.update {
            it.copy(
                ingredients = it.ingredients.toMutableList().apply {
                    add("")
                }
            )
        }
    }

    fun addNewPreparationStep() {
        _uiState.update {
            it.copy(
                preparationSteps = it.preparationSteps.toMutableList().apply {
                    add("")
                }
            )
        }
    }

    fun updateLocation(latitude: Double, longitude: Double, address: String? = null) {
        if (latitude != _uiState.value.location?.latitude) {
            val location = Location("")
            location.latitude = latitude
            location.longitude = longitude
            _uiState.update {
                it.copy(
                    location = location,
                    address = address?:it.address
                )
            }
        }
    }

    fun setLocation(location: Location) {
        _uiState.update {
            it.copy(
                location = location
            )
        }
    }

    fun onMarkerChanged(latitude: Double, longitude: Double) {
        markerJob?.cancel()
        markerJob = viewModelScope.launch {
            delay(2000L)
            Log.d("AddEditRecipeViewModel", "Marker search")
            val address = getAddressFromLocation()
            updateLocation(latitude, longitude)
            _uiState.update {
                it.copy(
                    address = address
                )
            }
        }
    }

    fun getAddressFromLocation(): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>? = null
        var addressText = ""

        if (_uiState.value.location != null) {
            try {
                val location = _uiState.value.location!!
                addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            if (addresses?.isNotEmpty() == true) {
                val address: Address? = addresses?.get(0)
                addressText = address?.getAddressLine(0) ?: ""
            }

        }

        return addressText
    }

    fun onSearchChanged(text: String) {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                address = text
            )
        }
        if (text.isNotEmpty()) {
            searchJob = viewModelScope.launch {
                delay(1500L)
                Log.d("AddEditRecipeViewModel", "Address search")
                getLocationFromAddress(text)
            }
        }
    }

    fun getLocationFromAddress(strAddress: String) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address: Address?

        val addresses: List<Address>? = geocoder.getFromLocationName(strAddress, 1)

        if (addresses.isNullOrEmpty().not()) {
            address = addresses!!.first()
            Log.d("AddEditRecipeViewModel", "Address found : $address")
            updateLocation(latitude = address.latitude, longitude = address.longitude, address = address.getAddressLine(0))
        }
    }

}