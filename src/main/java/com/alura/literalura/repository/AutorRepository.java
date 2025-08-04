package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    
    Optional<Autor> findByNombreContainsIgnoreCase(String nombre);
    
    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :año AND (a.fechaFallecimiento > :año OR a.fechaFallecimiento IS NULL)")
    List<Autor> findAutoresVivosEnAño(@Param("año") Integer año);
    
    @Query("SELECT a FROM Autor a WHERE a.nombre ILIKE %:nombre%")
    List<Autor> findByNombreContaining(@Param("nombre") String nombre);
}
