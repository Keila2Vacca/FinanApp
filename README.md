FINANAPP 💳
Aplicación Móvil para el Control y Administración de Finanzas Personales
Examen 2 – Tópicos Avanzados en Ingeniería

FinanApp es una solución móvil nativa para Android construida con Kotlin y Jetpack Compose bajo Clean Architecture + MVVM + Repository Pattern. Permite registrar, consultar, editar y categorizar ingresos y gastos, visualizar balances en tiempo real, generar métricas estadísticas con gráficas interactivas y conservar toda la información de forma persistente y local mediante Room Database.

---

🚀 Stack Tecnológico
• Lenguaje: Kotlin 2.2.10
• UI Toolkit: Jetpack Compose + Material Design 3
• Persistencia Local: Room Database 2.8.5 + KSP (Kotlin Symbol Processing)
• Navegación: Type-Safe Navigation / Custom Navigation BackStack reactivo
• Arquitectura: Clean Architecture + Repository Pattern + MVVM
• Manejo de Estado: Kotlin Coroutines, StateFlow y ViewModel (con gestión de ciclo de vida de formularios)
• Diseño Adaptativo: Material 3 Window Size Class (Compact: NavigationBar, Medium: NavigationRail, Expanded: Permanent Sidebar)
• Control de Versiones: Git & GitHub

---
📥 Instrucciones de Clonación y Ejecución
1. Clonar el repositorio desde GitHub
Abre tu terminal (Git Bash, Command Prompt o PowerShell) y ejecuta el siguiente comando para clonar el proyecto en tu máquina local:
git clone https://enlaceRepo
cd FinanApp
2. Abrir en Android Studio
1. Abre Android Studio.
2. Selecciona File > Open (o Open desde la pantalla de bienvenida).
3. Navega y selecciona la carpeta raíz del proyecto clonado (FinanApp).
4. Espera a que Android Studio sincronice las dependencias de Gradle automáticamente (Sync Project with Gradle Files). Si deseas sincronizar manualmente, puedes hacer clic en el ícono de elefante en la esquina superior derecha o ejecutar:
◦ En Windows: gradlew.bat build

3. Ejecutar la Aplicación
1. Conecta un dispositivo Android físico con la Depuración USB activada o inicia un emulador Android (compatible con celulares, tablets y escritorios).
2. Haz clic en el botón verde Run 'app' (o presiona Shift + F10) en la barra superior de Android Studio.
---
## 🏛 Arquitectura del Proyecto

El código está organizado en capas independientes con responsabilidades unidireccionales:

```text
com.topicos.finanapp/
├── FinanApplication.kt                 # Application Class y Service Locator (Room + Repo)
├── MainActivity.kt                     # Single Activity con cálculo de WindowSizeClass
├── data/                               # CAPA DE DATOS
│   ├── local/
│   │   ├── dao/MovementDao.kt          # Operaciones CRUD y agregaciones financieras en SQLite
│   │   ├── entity/MovementEntity.kt    # Entidad Room ("movements") con mapeadores de dominio
│   │   └── MovementDatabase.kt         # Instancia RoomDatabase y pre-poblado de datos
│   └── repository/MovementRepositoryImpl.kt # Implementación con Dispatchers.IO
├── domain/                             # CAPA DE DOMINIO
│   ├── model/
│   │   ├── Movement.kt                 # Modelo de negocio
│   │   ├── MovementType.kt             # Enum INGRESO y GASTO
│   │   ├── MovementCategory.kt         # Categorías oficiales del sistema
│   │   └── FinancialSummary.kt         # Balance, totales, promedios y top gastos
│   └── repository/MovementRepository.kt # Interfaz pura de repositorio (Flows y suspend functions)
├── navigation/
│   ├── Screen.kt                       # Definición de pantallas y rutas
│   └── FinanNavHost.kt                 # Router adaptativo (Sidebar / NavigationRail / BottomBar)
├── presentation/                       # CAPA DE PRESENTACIÓN (Compose)
│   ├── common/
│   │   ├── components/                 # Componentes reutilizables (TopBar, Sidebar, StatCards, etc.)
│   │   └── theme/                      # Colores, tipografía y tema Material 3
│   ├── dashboard/                      # Pantalla 1: Inicio y resumen financiero
│   ├── movement_register/              # Pantalla 2: Registrar / Editar movimientos (con gestión de estado limpia al guardar consecutivamente)
│   ├── movement_list/                  # Pantalla 3: Historial y búsqueda de movimientos
│   ├── movement_detail/                # Pantalla 4: Detalle y eliminación de registros
│   ├── statistics/                     # Pantalla 5: Gráficas estadísticas y porcentajes
│   └── settings/                       # Pantalla 6: Configuración de usuario, divisas y datos de prueba
└── utils/
    ├── CurrencyFormatter.kt            # Formateador monetario y separadores de miles
    └── DateUtils.kt                    # Formateador y manejo de fechas
```

---

📊 Explicación de la Solución de Persistencia Seleccionada
Para garantizar una persistencia robusta, eficiente y 100% nativa en Android, se seleccionó Room Database (construida sobre SQLite). Los motivos de su elección son:
1. Seguridad de Tipos en Tiempo de Compilación: Las consultas SQL en los DAOs (MovementDao) son validadas por el compilador, evitando errores en tiempo de ejecución.
2. Soportes Reactivos con Coroutines y Flow: MovementDao expone consultas de lectura como Flow<List<MovementEntity>>. Cada vez que se inserta, actualiza o elimina un movimiento, la base de datos notifica automáticamente a la UI a través del flujo reactivo, actualizando saldos y listas en tiempo real sin recargas manuales.
3. Ejecución en Hilos Secundarios: Todas las operaciones de escritura e inserción están encapsuladas dentro de withContext(Dispatchers.IO) en el repositorio, garantizando que el hilo principal (Main Thread) nunca se bloquee y la interfaz se mantenga fluida a 60/120 FPS.
4. Persistencia Transaccional: La información sobrevive al cierre de la aplicación, reinicios del dispositivo y caídas de procesos, cumpliendo estrictamente con los requisitos del examen.
---

## 🧪 Pruebas Obligatorias del Examen

| Escenario | Acción | Resultado Esperado |
|---|---|---|
| **1** | Registrar ingreso | $ 2.500.000 (Salario mensual) |
| **2** | Registrar gasto | $ 350.000 (Arriendo apartamento) |
| **3** | Comprobar balance | Balance = $ 2.150.000 |
| **4** | Registrar otro gasto | $ 150.000 (Mercado de la semana) |
| **5** | Comprobar balance | Balance = $ 2.000.000 ($ 2.500.000 - $ 500.000) |
| **6** | Editar movimiento | Se actualiza el balance inmediatamente |
| **7** | Eliminar movimiento | Se recalcula el balance en tiempo real |
| **8** | Cerrar y abrir la app | Los registros persisten en Room |
| **9** | Comprobar estadísticas | Gráficas corresponden a los registros guardados (Vivienda 70%, Alimentación 30%) |

---

