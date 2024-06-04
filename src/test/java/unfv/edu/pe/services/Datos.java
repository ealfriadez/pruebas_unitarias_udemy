package unfv.edu.pe.services;

import java.util.Arrays;
import java.util.List;

import unfv.edu.pe.models.Examen;

public class Datos {

	public final static List<Examen> EXAMENES = Arrays.asList(
			new Examen(5L, "Matematicas", null),
			new Examen(6L, "Lenguaje", null),
			new Examen(7L, "Historia", null)
			);
	
	public final static List<String> PREGUNTAS = Arrays.asList("aritmetica",
			"integrales", "derivadas", "trigonometria", "geometria");
	
	public final static Examen EXAMEN = new Examen(null, "Literatura", null);
}
