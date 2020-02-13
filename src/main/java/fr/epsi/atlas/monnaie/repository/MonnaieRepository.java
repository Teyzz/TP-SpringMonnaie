package fr.epsi.atlas.monnaie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.epsi.atlas.monnaie.entity.Monnaie;

public interface MonnaieRepository extends CrudRepository<Monnaie, String>{	
	
	//@Query("select Monnaie.tauxDeChange from Monnaie where Monnaie.tauxDeChange > 1")
	//List<Monnaie> doSomething();
	
}