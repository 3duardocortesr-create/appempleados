Employee Management System (Java & SQLite)

Una aplicación de escritorio para la gestión de personal, desarrollada en Java utilizando la biblioteca Swing para la interfaz gráfica y SQLite para la gestión de base de datos local.

🚀 Características

    Persistencia Local: Uso de SQLite para almacenar datos de forma permanente en un archivo .db sin necesidad de servidores externos.

    Operaciones CRUD Completas:

        Crear: Registro de nuevos empleados con validación de campos.

        Leer: Visualización dinámica en un JTable.

        Actualizar: Modificación de registros existentes mediante selección directa en tabla.

        Eliminar: Borrado de registros con confirmación de seguridad.

    Interfaz Intuitiva: Diseño organizado con BorderLayout y GridLayout para una experiencia de usuario fluida.

🛠️ Tecnologías Utilizadas

    Lenguaje: Java 17+

    GUI: Java Swing (javax.swing)

    Base de Datos: SQLite via JDBC

    Arquitectura: Programación Orientada a Objetos (POO) y manejo de eventos.

📋 Requisitos Previos

Para ejecutar este proyecto, asegúrate de tener:

    JDK 17 o superior instalado.

    El driver SQLite JDBC (ej. sqlite-jdbc-3.x.x.jar) añadido a las librerías de tu proyecto.

    Un IDE como IntelliJ IDEA o Eclipse.

🔧 Instalación y Ejecución

    Clonar el repositorio:
    Bash

    git clone https://github.com/tu-usuario/employee-management-system.git

    Configurar las librerías:
    Asegúrate de incluir el conector de SQLite en el Build Path de tu proyecto.

    Ejecutar la aplicación:
    Corre la clase AppEmpleados.java. El sistema creará automáticamente el archivo EmpresaDB.db en la raíz del proyecto si no existe.

💡 Detalles Técnicos

El proyecto implementa un manejo eficiente de recursos mediante el uso de Try-with-resources para asegurar que las conexiones a la base de datos y los Statements se cierren correctamente, evitando fugas de memoria. Además, se utiliza PreparedStatement para prevenir ataques de Inyección SQL, siguiendo las mejores prácticas de seguridad en desarrollo de software.
Autor

Eduardo Cortes Robles
Estudiante de Ingeniería en TI | Aspirante a AI & ML Engineer
