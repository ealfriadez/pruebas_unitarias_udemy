package unfv.edu.pe.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import unfv.edu.pe.models.Examen;
import unfv.edu.pe.repositories.ExamenRepository;
import unfv.edu.pe.repositories.PreguntaRepository;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
	
	@Mock
	ExamenRepository repository;
	@Mock
	PreguntaRepository preguntaRepository;
	
	@InjectMocks
	ExamenServiceImpl service;
	
	@BeforeEach
	void setUp() {
		
		//Otra forma de inicializar los mocks
		//MockitoAnnotations.openMocks(this);
		/*
		 * repository = mock(ExamenRepository.class); preguntaRepository =
		 * mock(PreguntaRepository.class); service = new ExamenServiceImpl(repository,
		 * preguntaRepository);
		 */
	}

	@DisplayName("Test para buscar examen por nombre")
	@Test
	void testFindExamenPorNombre() {			
		
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		
		Optional<Examen> examen = service.findExamenPorNombre("Historia");		
		
		assertTrue(examen.isPresent());
		assertEquals(7L, examen.orElseThrow().getId());
		assertEquals("Historia", examen.orElseThrow().getNombre());
	}

	@DisplayName("Test para buscar examen por nombre en lista vacia")
	@Test
	void testFindExamenPorNombreListaVacia() {

		List<Examen> datos = Collections.emptyList();
		
		when(repository.findAll()).thenReturn(datos);
		
		Optional<Examen> examen = service.findExamenPorNombre("Historia");		
		
		assertFalse(examen.isPresent());
	}
	
	@DisplayName("Test para buscar preguntas en examen")
	@Test
	void testPreguntasExamen() {

		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("integrales"));
	}
	
	@DisplayName("Test para buscar preguntas en examen con VERIFY")
	@Test
	void testPreguntasExamenVerify() {

		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("integrales"));
		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
	}
	
	/*
	 * @DisplayName("Test para buscar si no existe examen con VERIFY")
	 * 
	 * @Test void testNoExisteExamenVerify() { // Given
	 * when(repository.findAll()).thenReturn(Collections.emptyList());
	 * when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos
	 * .PREGUNTAS);
	 * 
	 * // When Examen examen = service.findExamenPorNombreConPreguntas("Historia");
	 * 
	 * //Then assertNull(examen); verify(repository).findAll();
	 * verify(preguntaRepository).findPreguntasPorExamenId(7L); }
	 */
	
	@DisplayName("Test para guardar examen")
	@Test
	void testGuardarExamen() {
		// Given
		Examen newExamen = Datos.EXAMEN;
		newExamen.setPreguntas(Datos.PREGUNTAS);
		
		when(repository.guardar(any(Examen.class))).then(new Answer<Examen>() {

			Long secuencia = 8L;
			@Override
			public Examen answer(InvocationOnMock invocation) throws Throwable {
				Examen examen = invocation.getArgument(0);
				examen.setId(secuencia++);				
				return examen;
			}
		});
		
		// When
		Examen examen = service.guardar(newExamen);
		
		// Then
		assertNotNull(examen.getId());
		assertEquals(8L, examen.getId());
		assertEquals("Literatura", examen.getNombre());
		
		verify(repository).guardar(any(Examen.class));
		verify(preguntaRepository).guardarVarias(anyList());
	}
	
	/*
	 * @DisplayName("Test para manejar excepciones")
	 * 
	 * @Test void testManejoException() { // Given
	 * when(repository.findAll()).thenReturn(Collections.emptyList());
	 * when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos
	 * .PREGUNTAS);
	 * 
	 * // When Examen examen = service.findExamenPorNombreConPreguntas("Historia");
	 * 
	 * //Then assertNull(examen); verify(repository).findAll();
	 * verify(preguntaRepository).findPreguntasPorExamenId(7L); }
	 */
	
	@DisplayName("Test para manejar excepciones")
	@Test
	void testManejoExceptionDos() {
		// Given
		when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
		when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);
		
		// When				
		Exception exception = assertThrows(IllegalArgumentException.class, () ->{
			service.findExamenPorNombreConPreguntas("Matematicas");
		});
		
		//Then
		assertEquals(IllegalArgumentException.class, exception.getClass());
		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(isNull());
	}
	
	@DisplayName("Test para coincidencia de argumentos")
	@Test
	void testArgumentMatchers() {
		// Given	
		
		// When
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		service.findExamenPorNombreConPreguntas("Lenguaje");
		
		// Then
		verify(repository).findAll();
		//verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg.equals(5L)));
		verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg >= 6L));
		//verify(preguntaRepository).findPreguntasPorExamenId(eq(5L));
	}
}