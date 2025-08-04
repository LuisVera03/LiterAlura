package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);
    
    List<Libro> findByIdioma(String idioma);
    
    @Query("SELECT COUNT(l) FROM Libro l WHERE l.idioma = :idioma")
    Long countByIdioma(@Param("idioma") String idioma);
    
    @Query("SELECT l FROM Libro l ORDER BY l.numeroDescargas DESC")
    List<Libro> findTop10ByOrderByNumeroDescargasDesc();
    
    @Query("SELECT DISTINCT l.idioma FROM Libro l")
    List<String> findDistinctIdiomas();
}
