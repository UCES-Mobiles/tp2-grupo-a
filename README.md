# IMDbCito - Aplicación de Películas

Una aplicación Android moderna para explorar y gestionar tus películas favoritas, desarrollada en Kotlin con arquitectura MVVM.

## Características ##

- ** Explorar Películas**: Descubre películas populares, mejor rankeadas y próximos estrenos
- ** Búsqueda Avanzada**: Busca por nombre, actor, género o década
- ** Gestión de Favoritos**: Agrega películas a favoritos y marca como vistas/pendientes
- ** Interfaz Moderna**: Diseño material design con modo claro/oscuro
- ** Almacenamiento Local**: Base de datos SQLite para favoritos offline

## Tecnologías Utilizadas ##

- **Lenguaje**: Kotlin
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **API**: The Movie Database (TMDB)
- **Base de Datos**: SQLite con Room
- **Networking**: Retrofit + Coroutines
- **Imágenes**: Glide
- **Navegación**: Navigation Component
- **UI**: Material Design 3

## Requisitos Previos ##

- Android Studio Arctic Fox o superior
- JDK 11 o superior
- Dispositivo/Emulador con Android 8.0 (API 24) o superior
- Cuenta en [The Movie Database](https://www.themoviedb.org/)

## Configuración ##

1. Obtener API Key de TMDB

1. Regístrate en [The Movie Database](https://www.themoviedb.org/)
2. Ve a [Configuración de API](https://www.themoviedb.org/settings/api)
3. Solicita una API Key (selecciona "Developer")
4. Copia tu API Key

2. Configurar el Proyecto

1. Clona o descarga el proyecto
2. Crea el archivo `local.properties` en la raíz del proyecto:

Tu archivo deberá ser:

BASE_DOMAIN=https://api.themoviedb.org/
API_TOKEN=TU_API_KEY_AQUI
