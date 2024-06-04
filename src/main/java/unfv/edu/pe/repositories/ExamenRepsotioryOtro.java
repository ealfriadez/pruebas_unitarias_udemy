package unfv.edu.pe.repositories;

import java.util.List;
import java.util.concurrent.TimeUnit;

import unfv.edu.pe.models.Examen;

public class ExamenRepsotioryOtro implements ExamenRepository{

	@Override
	public List<Examen> findAll() {
		
		try {
			System.out.println("ExamenRepositoryOtro");
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Examen guardar(Examen examen) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
