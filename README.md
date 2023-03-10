
# ChefIO

Aplicaci贸n de Recetas que te ayudara a poder compartir y buscar multiples platos de comida.

En esta aplicaci贸n muestra:

- Material theming
- Google Maps
- Animation
- Compose Navigation
- Hilt
## 馃捇 Tech Stack

**Front:** Para el desarrollo de la UI se uso Jetpack Compose. Esta es una herramienta que nos ayuda a construir la UI de forma rapida e intuitiva, ya que todo este toolkit esta basado en Kotlin

**Librer铆as:**

-[**Accompanist**](https://github.com/google/accompanist): Conjunto de librer铆as brindada por Google para requirimientos que todavia no estan disponibles en Jetpack Compose.

-[**Coil**](https://coil-kt.github.io/coil/): Librer铆a de carga de imagenes soportada para Kotlin Coroutines.

-[**Paging**](https://developer.android.com/jetpack/androidx/releases/paging): Apoyo para la carga paginada de datos.

-[**Google Maps**](https://github.com/googlemaps/android-maps-compose): Componentes de Google Maps para Jetpack Compose.

-[**Hilt**](https://dagger.dev/hilt/): Herramienta para inyeccion de dependencias, nos provee de una implementaci贸n estandar y rapida de Dagger para las aplicaciones Android.

**Backend:** Firebase. Provee soluci贸n en Cloud para una rapida implementaci贸n de Base de datos. Y
Cloudinary como repositorio de imagenes


##  Arquitectura

Para la arquitectura se uso MVVM, siendo esta la arquitectura recomendada para el desarrollo usando Jetpack Compose.
Tambi茅n se usaron patrones como Repository y Data Source.
## Features

#### [Splash Screen](app/src/main/java/dev/diegodc/chefio/features/splash)
La pantalla Splash permite la carga inicial donde verificamos si ya se encuentra un usuario logeado.

#### [Theming](app/src/main/java/dev/diegodc/chefio/common/theme)
Chefio sigue la guias de Material Design.

#### [Common UI](app/src/main/java/dev/diegodc/chefio/common/ui)
Jetpack Compose hace facil la implementacion de componentes reusables.  