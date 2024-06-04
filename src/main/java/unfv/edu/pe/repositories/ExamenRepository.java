package unfv.edu.pe.repositories;

import java.util.List;

import unfv.edu.pe.models.Examen;

public interface ExamenRepository {

	Examen guardar(Examen examen);
	List<Examen> findAll();
	
}
