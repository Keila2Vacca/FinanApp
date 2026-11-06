# FINANAPP 💳
### Aplicación Móvil para el Control y Administración de Finanzas Personales
**Examen 2 – Tópicos Avanzados en Ingeniería**

FinanApp es una solución móvil nativa para Android construida con Kotlin y Jetpack Compose bajo Clean Architecture + MVVM + Repository Pattern. Permite registrar, consultar, editar y categorizar ingresos y gastos, visualizar balances en tiempo real, generar métricas estadísticas con gráficas interactivas y conservar toda la información de forma persistente y local mediante Room Database.

---

## 🚀 Stack Tecnológico

- **Lenguaje:** Kotlin 2.2.x
- **UI Toolkit:** Jetpack Compose + Material Design 3
- **Persistencia Local:** Room Database 2.6.x + KSP (Kotlin Symbol Processing)
- **Navegación:** Navigation 3 / Type-Safe Navigation con destinos serializables (`@Serializable`) y gestión reactiva de BackStack
- **Arquitectura:** Clean Architecture + Repository Pattern + MVVM
- **Manejo de Estado:** Kotlin Coroutines, StateFlow y ViewModel
- **Diseño Adaptativo:** Material 3 Window Size Class (Compact: NavigationBar, Medium: NavigationRail, Expanded: Permanent Sidebar)
- **Control de Versiones:** Git

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
│   │   └── MovementDatabase.kt         # Instancia RoomDatabase y pre-poblado de datos de examen
│   └── repository/MovementRepositoryImpl.kt # Implementación con Dispatchers.IO
├── domain/                             # CAPA DE DOMINIO
│   ├── model/
│   │   ├── Movement.kt                 # Modelo de negocio
│   │   ├── MovementType.kt             # Enum INGRESO y GASTO
│   │   ├── MovementCategory.kt         # 9 categorías del sistema
│   │   └── FinancialSummary.kt         # Balance, totales, promedios y top gastos
│   └── repository/MovementRepository.kt # Interfaz pura de repositorio (Flows y suspend functions)
├── navigation/
│   ├── Screen.kt                       # Claves serializables de navegación (@Serializable)
│   └── FinanNavHost.kt                 # Router adaptativo (Sidebar / NavigationRail / BottomBar)
├── presentation/                       # CAPA DE PRESENTACIÓN (Compose)
│   ├── common/
│   │   ├── components/                 # Componentes reutilizables (TopBar, Sidebar, StatCards, etc.)
│   │   └── theme/                      # Colores, tipografía y tema Material 3
│   ├── dashboard/                      # Pantalla 1: Inicio
│   ├── movement_register/              # Pantalla 2: Registrar / Editar
│   ├── movement_list/                  # Pantalla 3: Historial y Búsqueda
│   ├── movement_detail/                # Pantalla 4: Detalle y Eliminación
│   ├── statistics/                     # Pantalla 5: Gráficas y Métricas
│   └── settings/                       # Pantalla 6: Configuración y Datos de Prueba
└── utils/
    ├── CurrencyFormatter.kt            # Formateador monetario ($ 2.000.000)
    └── DateUtils.kt                    # Formateador de fechas
```

---

## 📊 Solución de Persistencia (Room Database)

La base de datos SQLite `finanapp_database` utiliza Room para garantizar que:
1. Las operaciones de lectura expongan flujos reactivos `Flow<T>`, actualizando automáticamente las pantallas al agregar, modificar o borrar registros.
2. Todas las operaciones de escritura (insertar, actualizar, eliminar) se ejecuten en hilos secundarios mediante `Dispatchers.IO`.
3. Al cerrar y volver a abrir la aplicación, o reiniciar el dispositivo, los registros se conservan intactos.
4. Incluye funciones de utilidad en *Configuración* para restaurar en 1 clic los 3 movimientos obligatorios del examen o limpiar el historial.

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

## 📱 Ejecución en Android Studio

1. Abre Android Studio.
2. Selecciona **File > Open** y abre la carpeta `C:\Users\USUARIO\AndroidStudioProjects\FinanApp`.
3. Presiona **Sync Project with Gradle Files**.
4. Conecta un dispositivo físico con Depuración USB o inicia un emulador Android (celular o tablet).
5. Haz clic en **Run 'app'** (Shift + F10).
