# 📚 LiterAlura

LiterAlura es una aplicación de consola desarrollada en Java con Spring Boot que permite gestionar un catálogo de libros utilizando la API Gutendex.

## 🚀 Funcionalidades

### Funcionalidades Principales
- ✅ **Búsqueda de libros por título**
- ✅ **Listado de todos los libros buscados**
- ✅ **Listado de libros por idioma**
- ✅ **Listado de autores**
- ✅ **Consulta de autores vivos en un año determinado**
- ✅ **Estadísticas de libros por idioma**

### Funcionalidades Adicionales (Opcionales)
- ⭐ **Top 10 libros más descargados**
- ⭐ **Búsqueda de autor por nombre**
- ⭐ **Estadísticas generales**

## 🛠️ Tecnologías Utilizadas

- **Java 17+**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **PostgreSQL**
- **Jackson (Manejo de JSON)**
- **Maven**
- **API Gutendx**

## ⚙️ Configuración del Proyecto

### 1. Clonar el repositorio
```bash
git clone https://github.com/LuisVera03/LiterAlura.git
cd LiterAlura
```

### 2. Configurar la base de datos
```sql
CREATE DATABASE literalura;
```

### 3. Configurar application.properties
```properties
spring.application.name=literalura
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.format-sql=true
```

### 4. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

## 🎯 Uso de la Aplicación

Al ejecutar la aplicación, se mostrará un menú interactivo:

```
========== LITERALURA ==========
1 - Buscar libro por título
2 - Listar libros registrados
3 - Listar autores registrados
4 - Listar autores vivos en un determinado año
5 - Listar libros por idioma
6 - Estadísticas de libros por idioma
0 - Salir
================================
Seleccione una opción:
```

## 📊 API Utilizada

**Gutendx API**: https://gutendx.com/
- Catálogo gratuito de más de 70,000 libros del Proyecto Gutenberg
- No requiere autenticación
- Respuestas en formato JSON

#### Menú Principal
```
========== LITERALURA ==========
1 - Buscar libro por título
2 - Listar libros registrados
3 - Listar autores registrados
4 - Listar autores vivos en un determinado año
5 - Listar libros por idioma
6 - Estadísticas de libros por idioma
7 - Top 10 libros más descargados
8 - Buscar autor por nombre
0 - Salir
================================
```

### Estructura de Paquetes
```
src/main/java/com/alura/literalura/
├── LiteraluraApplication.java      # Clase principal con CommandLineRunner
├── model/                          # Entidades JPA y DTOs
│   ├── Autor.java                 # Entidad Author
│   ├── Libro.java                 # Entidad Book
│   ├── DatosAutor.java           # DTO para datos de autor de la API
│   ├── DatosLibro.java           # DTO para datos de libro de la API
│   └── Datos.java                # DTO contenedor de resultados
├── repository/                     # Interfaces de repositorio
│   ├── AutorRepository.java       # Repositorio de autores
│   └── LibroRepository.java       # Repositorio de libros
├── service/                        # Servicios de la aplicación
│   ├── ConsumoAPI.java           # Cliente HTTP para consumir API
│   ├── ConvierteDatos.java       # Conversión JSON con Jackson
│   └── IConvierteDatos.java      # Interfaz de conversión
└── principal/                      # Lógica de la aplicación
    └── Principal.java             # Menú y flujo principal
```

## 🗃️ Estructura de la Base de Datos

### Tabla: Libro
- id (BIGINT, PRIMARY KEY)
- titulo (VARCHAR)
- idioma (VARCHAR)
- numeroDescargas (DOUBLE)
- autor_id (BIGINT, FOREIGN KEY)

### Tabla: Autor
- id (BIGINT, PRIMARY KEY)
- nombre (VARCHAR)
- fechaNacimiento (INTEGER)
- fechaFallecimiento (INTEGER)

## ✨ Autor

**Luis Alejandro Vera**
- GitHub: [@LuisVera03](https://github.com/LuisVera03)