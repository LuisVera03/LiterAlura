package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \n========== LITERALURA ==========
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
                    Seleccione una opción: """;
            System.out.println(menu);

            try {
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosPorAño();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        mostrarEstadisticasPorIdioma();
                        break;
                    case 7:
                        mostrarTop10Libros();
                        break;
                    case 8:
                        buscarAutorPorNombre();
                        break;
                    case 0:
                        System.out.println("\nCerrando la aplicación...");
                        break;
                    default:
                        System.out.println("\nOpción inválida");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nOpción inválida. Por favor ingrese un número.");
                teclado.nextLine();
                opcion = -1;
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("\nEscribe el nombre del libro que deseas buscar:");
        var tituloLibro = teclado.nextLine();
        
        // Verificar si el libro ya existe en la base de datos
        Optional<Libro> libroExistente = libroRepository.findByTituloContainsIgnoreCase(tituloLibro);
        if (libroExistente.isPresent()) {
            System.out.println("\nEl libro ya está en la base de datos:");
            System.out.println(libroExistente.get());
            return;
        }

        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "%20"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados()
                .stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        
        if (libroBuscado.isPresent()) {
            DatosLibro datosLibro = libroBuscado.get();
            
            // Verificar que el libro tenga autor
            if (datosLibro.autores().isEmpty()) {
                System.out.println("\nEl libro no tiene autor registrado.");
                return;
            }

            // Crear o buscar el autor
            DatosAutor datosAutor = datosLibro.autores().get(0);
            Autor autor = autorRepository.findByNombreContainsIgnoreCase(datosAutor.nombre())
                    .orElseGet(() -> {
                        Autor nuevoAutor = new Autor(datosAutor);
                        return autorRepository.save(nuevoAutor);
                    });

            // Crear el libro
            Libro libro = new Libro(datosLibro);
            libro.setAutor(autor);
            
            libroRepository.save(libro);
            System.out.println("\nLibro guardado exitosamente:");
            System.out.println(libro);
        } else {
            System.out.println("\nLibro no encontrado");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados en la base de datos.");
        } else {
            System.out.println("\n========== LIBROS REGISTRADOS ==========");
            libros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("\nNo hay autores registrados en la base de datos.");
        } else {
            System.out.println("\n========== AUTORES REGISTRADOS ==========");
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosPorAño() {
        System.out.println("\nIngrese el año vivo de autor(es) que desea buscar:");
        try {
            var año = teclado.nextInt();
            teclado.nextLine();
            
            List<Autor> autoresVivos = autorRepository.findAutoresVivosEnAño(año);
            if (autoresVivos.isEmpty()) {
                System.out.println("\nNo se encontraron autores vivos en el año " + año);
            } else {
                System.out.println("\n========== AUTORES VIVOS EN " + año + " ==========");
                autoresVivos.forEach(System.out::println);
            }
        } catch (InputMismatchException e) {
            System.out.println("\nAño inválido. Por favor ingrese un número.");
            teclado.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        var menuIdioma = """
                \nSeleccione el idioma para buscar los libros:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """;
        System.out.println(menuIdioma);
        var idioma = teclado.nextLine();
        
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("\nNo se encontraron libros en el idioma: " + idioma);
        } else {
            System.out.println("\n========== LIBROS EN " + idioma.toUpperCase() + " ==========");
            librosPorIdioma.forEach(System.out::println);
        }
    }

    private void mostrarEstadisticasPorIdioma() {
        List<String> idiomas = libroRepository.findDistinctIdiomas();
        if (idiomas.isEmpty()) {
            System.out.println("\nNo hay libros registrados para mostrar estadísticas.");
            return;
        }

        System.out.println("\n\n========== ESTADÍSTICAS POR IDIOMA ==========");
        for (String idioma : idiomas) {
            Long cantidad = libroRepository.countByIdioma(idioma);
            System.out.println("Idioma: " + idioma + " - Cantidad de libros: " + cantidad);
        }

        // Estadísticas adicionales
        DoubleSummaryStatistics stats = libroRepository.findAll()
                .stream()
                .mapToDouble(Libro::getNumeroDescargas)
                .summaryStatistics();

        System.out.println("\n\n========== ESTADÍSTICAS GENERALES ==========");
        System.out.println("Total de libros: " + stats.getCount());
        System.out.println("Promedio de descargas: " + String.format("%.2f", stats.getAverage()));
        System.out.println("Máximo de descargas: " + String.format("%.0f", stats.getMax()));
        System.out.println("Mínimo de descargas: " + String.format("%.0f", stats.getMin()));
    }

    private void mostrarTop10Libros() {
        List<Libro> topLibros = libroRepository.findTop10ByOrderByNumeroDescargasDesc()
                .stream()
                .limit(10)
                .collect(Collectors.toList());

        if (topLibros.isEmpty()) {
            System.out.println("\nNo hay libros registrados.");
        } else {
            System.out.println("\n\n========== TOP 10 LIBROS MÁS DESCARGADOS ==========");
            for (int i = 0; i < topLibros.size(); i++) {
                System.out.println((i + 1) + "º " + topLibros.get(i).getTitulo() + 
                                 " - Descargas: " + String.format("%.0f", topLibros.get(i).getNumeroDescargas()));
            }
        }
    }

    private void buscarAutorPorNombre() {
        System.out.println("\nEscribe el nombre del autor que deseas buscar:");
        var nombreAutor = teclado.nextLine();
        
        List<Autor> autoresEncontrados = autorRepository.findByNombreContaining(nombreAutor);
        
        if (autoresEncontrados.isEmpty()) {
            System.out.println("\nNo se encontraron autores con el nombre: " + nombreAutor);
        } else {
            System.out.println("\n\n========== AUTORES ENCONTRADOS ==========");
            autoresEncontrados.forEach(System.out::println);
        }
    }
}
