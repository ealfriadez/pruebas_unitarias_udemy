package unfv.edu.pe.services;

import java.util.Optional;

import unfv.edu.pe.models.Examen;

public interface ExamenService {

	Optional<Examen> findExamenPorNombre(String nombre);
	Examen findExamenPorNombreConPreguntas(String nombre);
	Examen guardar(Examen examen);
}
