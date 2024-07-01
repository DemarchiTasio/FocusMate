# FocusMate App

FocusMate es una aplicación de organización y mindfulness con integración de IA, diseñada para ayudarte a gestionar tus tareas y mantener un equilibrio mental.

## Funcionalidades Principales

- Gestión de tareas
- Organización con calendario
- Meditaciones guiadas
- Seguimiento de hábitos
- Chatbot con IA

## Estructura del Proyecto

### Pantalla de Calendario

#### Funcionalidades Implementadas

1. **Vista Mensual y Diaria del Calendario**
   - La vista mensual muestra los días del mes actual, destacando el día actual y los días con eventos.
   - Al seleccionar un día específico, se muestra una vista diaria con los eventos de ese día.

2. **Gestión de Eventos**
   - Agregar eventos con o sin fecha específica.
   - Editar y eliminar eventos existentes.
   - Marcar eventos como completados.
   - Mostrar eventos sin fecha y eventos completados en listas separadas.

3. **Interfaz de Usuario**
   - Interfaz minimalista y relajante.
   - Uso de colores suaves y diseño redondeado para mejorar la experiencia del usuario.
   - Botón flotante para agregar nuevos eventos.
   - Posibilidad de filtrar eventos por día, por eventos sin fecha y por eventos completados.
     
#### Detalles Técnicos
  - Uso de Jetpack Compose para la interfaz de usuario.
  - Almacenamiento local de eventos utilizando Room.
  - Formato de fechas manejado con SimpleDateFormat.

### Otros Componentes del Proyecto
1. **Pantalla Principal**
  - Navegación principal de la aplicación.
  - Acceso rápido a las diferentes funcionalidades de la app.

2. **Pantalla de Tareas**
  - Gestión de tareas pendientes y completadas.
  - Filtros y etiquetas para una mejor organización.

3. **Pantalla de Meditación**
  - Acceso a meditaciones guiadas.
  - Seguimiento del progreso en prácticas de mindfulness.

4. **Chatbot con IA**
  - Interacción con un asistente virtual para resolver dudas y obtener recomendaciones.
