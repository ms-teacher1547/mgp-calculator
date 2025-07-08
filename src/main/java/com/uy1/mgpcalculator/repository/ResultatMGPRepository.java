package com.uy1.mgpcalculator.repository;

import com.uy1.mgpcalculator.model.ResultatMGP;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultatMGPRepository extends JpaRepository<ResultatMGP, Long> {
    List<ResultatMGP> findByNomEtudiantContainingIgnoreCase(String nom);
}