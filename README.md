FocusMate App
FocusMate es una aplicación de organización y mindfulness con integración de IA, diseñada para ayudarte a gestionar tus tareas y mantener un equilibrio mental.

Funcionalidades Principales
Gestión de tareas
Organización con calendario
Meditaciones guiadas
Seguimiento de hábitos
Chatbot con IA
Estructura del Proyecto
Pantalla de Calendario
Funcionalidades Implementadas
Vista Mensual y Diaria del Calendario

La vista mensual muestra los días del mes actual, destacando el día actual y los días con eventos.
Al seleccionar un día específico, se muestra una vista diaria con los eventos de ese día.
Gestión de Eventos

Agregar eventos con o sin fecha específica.
Editar y eliminar eventos existentes.
Marcar eventos como completados.
Mostrar eventos sin fecha y eventos completados en listas separadas.
Interfaz de Usuario

Interfaz minimalista y relajante.
Uso de colores suaves y diseño redondeado para mejorar la experiencia del usuario.
Botón flotante para agregar nuevos eventos.
Posibilidad de filtrar eventos por día, por eventos sin fecha y por eventos completados.
Código de la Pantalla de Calendario
kotlin
Copy code
// Incluye aquí el código completo de CalendarScreen.kt
Detalles Técnicos
Uso de Jetpack Compose para la interfaz de usuario.
Almacenamiento local de eventos utilizando Room.
Formato de fechas manejado con SimpleDateFormat.
Otros Componentes del Proyecto
Pantalla Principal

Navegación principal de la aplicación.
Acceso rápido a las diferentes funcionalidades de la app.
Pantalla de Tareas

Gestión de tareas pendientes y completadas.
Filtros y etiquetas para una mejor organización.
Pantalla de Meditación

Acceso a meditaciones guiadas.
Seguimiento del progreso en prácticas de mindfulness.
Chatbot con IA

Interacción con un asistente virtual para resolver dudas y obtener recomendaciones.
Instalación y Configuración
Clonar el repositorio:

sh
Copy code
git clone https://github.com/tu_usuario/focusmate.git
Abrir el proyecto en Android Studio.

Configurar el entorno de desarrollo siguiendo los pasos indicados en la documentación del proyecto.

Ejecutar la aplicación en un emulador o dispositivo físico.

Contribuciones
Las contribuciones son bienvenidas. Si deseas colaborar, sigue estos pasos:

Haz un fork del proyecto.
Crea una nueva rama con tu funcionalidad: git checkout -b mi-nueva-funcionalidad.
Realiza los cambios necesarios y haz commit: git commit -m 'Agregar nueva funcionalidad'.
Sube los cambios a tu rama: git push origin mi-nueva-funcionalidad.
Abre un pull request en el repositorio original.
Licencia
Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles.
