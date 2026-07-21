# QuickLunch

QuickLunch es una aplicacion Android disenada para ayudar a maestros y personal de cafeteria a coordinar la asistencia al almuerzo de forma mas eficiente. La aplicacion usa Firebase para autenticacion y sincronizacion de datos en tiempo real, facilitando que el personal escolar pueda manejar conteos y participacion desde un dispositivo movil.

## Resumen

QuickLunch fue creado para un flujo escolar donde maestros y personal de cafeteria necesitan visibilidad rapida y compartida sobre la asistencia al almuerzo. En vez de depender de conteos en papel o comunicacion tardia, la aplicacion ofrece pantallas separadas por rol para que los datos puedan registrarse, verse y actualizarse desde Android.

## Funcionalidades

- Registro e inicio de sesion de usuarios
- Flujos separados para host y guest
- Integracion con Firebase Authentication
- Integracion con Firebase Realtime Database
- Pantallas separadas para host y guest
- Flujos para usuarios nuevos y existentes
- Pantalla para recuperar contrasena
- Interfaz nativa de Android construida con Java y layouts XML
- Sincronizacion en tiempo real para datos compartidos de asistencia al almuerzo

## Tecnologias

- Java
- Android SDK
- Android Studio
- Gradle Kotlin DSL
- Firebase Authentication
- Firebase Realtime Database
- Material Components
- AppCompat
- ConstraintLayout

## Estructura Del Proyecto

```text
app/src/main/java/com/example/quicklunch/
- SignUpActivity.java          # Pantalla principal de registro
- HostSignUpActivity.java      # Flujo de registro para host
- GuestSignUpActivity.java     # Flujo de registro para guest
- NewHostActivity.java         # Flujo para nuevo host
- NewGuestActivity.java        # Flujo para nuevo guest
- ExistingHostActivity.java    # Flujo para host existente
- ExistingGuestActivity.java   # Flujo para guest existente
- MainHostActivity.java        # Pantalla principal del host
- MainGuestActivity.java       # Pantalla principal del guest
- ForgotPassword.java          # Recuperacion de contrasena
- FirebaseHelper.java          # Logica auxiliar relacionada con Firebase
- User.java                    # Modelo de usuario

app/src/main/res/layout/
- Layouts XML para las pantallas de Android
```

## Requisitos

- Android Studio
- JDK 8 o superior
- Android SDK 34
- Proyecto de Firebase
- Dispositivo Android o emulador

## Configuracion De Firebase

Este proyecto usa servicios de Firebase. Para correr la aplicacion localmente, crea un proyecto en Firebase y agrega la app Android con el paquete:

```text
com.example.quicklunch
```

Luego descarga el archivo de configuracion:

```text
google-services.json
```

Colocalo aqui:

```text
app/google-services.json
```

No subas credenciales reales ni configuraciones privadas de Firebase a un repositorio publico.

## Como Ejecutar

1. Clona el repositorio:

```bash
git clone https://github.com/elvissanchez73/QuickLunch.git
```

2. Abre el proyecto en Android Studio.

3. Agrega tu archivo `google-services.json` dentro de la carpeta `app/`.

4. Deja que Gradle sincronice el proyecto.

5. Ejecuta la app en un emulador o dispositivo Android fisico.

## Flujos Principales

### Flujo Host

Los usuarios host pueden registrarse o continuar como usuarios existentes y acceder a pantallas especificas para manejar informacion relacionada al almuerzo.

### Flujo Guest

Los usuarios guest pueden registrarse o continuar como usuarios existentes y acceder a pantallas especificas para enviar o ver informacion relacionada al almuerzo.

### Datos Compartidos Con Firebase

Firebase mantiene los datos sincronizados para que los cambios realizados por un tipo de usuario puedan reflejarse para el otro tipo de usuario.

## Lo Que Aprendi

- Construir una aplicacion Android nativa con Java
- Crear navegacion entre multiples pantallas de Android
- Usar Firebase Authentication
- Leer y escribir datos con Firebase Realtime Database
- Estructurar un proyecto Android con activities, modelos, helpers y layouts XML
- Manejar flujos de usuario basados en roles

## Mejoras Futuras

- Mejorar validaciones y mensajes de error
- Agregar reglas de autorizacion mas fuertes en Firebase
- Agregar pruebas automatizadas
- Mejorar la respuesta visual en diferentes tamanos de pantalla
- Agregar reportes o resumenes para personal de cafeteria
- Agregar soporte offline para redes escolares inestables

## Autor

Construido por [Elvis J. Sanchez Robles](https://github.com/elvissanchez73).
