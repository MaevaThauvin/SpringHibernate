package javabase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class StreamsTest {

	@Test
	void testMapForEach() {
		List<String> cities = List.of("Toulouse", "Pau", "Nantes", "Nîmes", "Saint-Petersbourg");
		cities.stream()
			//.map(c->c.toUpperCas()) //String -> String
			.map(String::toUpperCase) // String -> String (ref object method from class)
			//.forEach(c -> System.out.println(c));
			.forEach(System.out::println); //String -> void (ref object method from object)
	}
	
	 @Test
	 void testMapFilterCollect() {
			List<String> cities = List.of("Toulouse", " Pau ", "nantes ", " Nîmes", "SAint-Petersbourg");
			var res = cities.stream()
			.map(String::trim)
			.map(String::toLowerCase)
			.filter(c -> c.startsWith("n")) //String -> boolean
			.collect(Collectors.toList());
			// collect nécessite : 
			// -> 1 accumulateur initial (ex. panier vide)
			// -> 1 opération d'accumulation pour chaque donnée du stream
			// -> 1 opération de finalisation (accumulateur -> ?)
			// -> 1 opération de combinaison d'accumulateur (travail en //)
			System.out.println(res);
	 }
	 
	 @Test
	 void testMapFilterCollectToStats() {
			List<String> cities = List.of("Toulouse", " Pau ", "nantes ", " Nîmes", "SAint-Petersbourg");
			var res = cities.stream()
			.map(String::trim)
			.map(String::toLowerCase)
			.filter(c -> c.startsWith("n")) //String -> boolean
			.mapToInt(String::length)
			//.sum();
			//.max()
			//.average();
			//.count();
			//.min();
			//.max();
			.summaryStatistics();
			// collect nécessite : 
			// -> 1 accumulateur initial (ex. panier vide)
			// -> 1 opération d'accumulation pour chaque donnée du stream
			// -> 1 opération de finalisation (accumulateur -> ?)
			// -> 1 opération de combinaison d'accumulateur (travail en //)
			System.out.println(res);
	 }

}
