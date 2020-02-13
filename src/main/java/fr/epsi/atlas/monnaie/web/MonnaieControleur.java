package fr.epsi.atlas.monnaie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import fr.epsi.atlas.monnaie.entity.Monnaie;
import fr.epsi.atlas.monnaie.repository.MonnaieRepository;
import fr.epsi.atlas.monnaie.service.MonnaieInexistanteException;
import fr.epsi.atlas.monnaie.service.MonnaieService;
import fr.epsi.atlas.monnaie.service.MontantDto;
import fr.epsi.atlas.monnaie.service.TauxDeChangeDto;

import java.net.URI;

import javax.transaction.Transactional;

@RestController
public class MonnaieControleur {

	@Autowired
	private MonnaieService monnaieService;
	
	MonnaieRepository monnaieRepo;

	@ExceptionHandler(MonnaieInexistanteException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String handleException(MonnaieInexistanteException e) {
		return "La monnaie entrée n'existe pas.";
	}
	
	@GetMapping(path = "/monnaie/")
	public Iterable<Monnaie> getAll() {
		return this.monnaieService.getAll();
	}
	
	@GetMapping(path = "monnaie/{codeMonnaie}")
	public Monnaie getByCode(@PathVariable String codeMonnaie) throws MonnaieInexistanteException {
		return monnaieService.getByCode(codeMonnaie);
	}

	@PostMapping(path = "/monnaie/")
	public ResponseEntity<Monnaie> create(@RequestBody Monnaie monnaie, UriComponentsBuilder uriBuilder)
			throws MonnaieInexistanteException {	
			Monnaie monnaieCreate = this.monnaieService.create(monnaie.getCode(), monnaie.getTauxDeChange());
			URI uri = uriBuilder.path("/monnaie/{codeMonnaie}").buildAndExpand(monnaieCreate.getCode()).toUri();
			return ResponseEntity.created(uri).body(monnaieCreate);	
	}
	
	@PostMapping(path = "/monnaie/{codeMonnaie}/EUR")
	public ResponseEntity<MontantDto> convertToEUR(@PathVariable String codeMonnaie, @RequestBody MontantDto montant, UriComponentsBuilder uriBuilder) 
	throws MonnaieInexistanteException {
		MontantDto montantConverted = new MontantDto();
		montantConverted.setMontant(monnaieService.getByCode(codeMonnaie).getTauxDeChange().multiply(montant.getMontant()));
		return ResponseEntity.ok(montantConverted);
	}
	
	@Transactional
	@DeleteMapping(path = "monnaie/{codeMonnaie}")
	public void deleteByCode(@PathVariable String codeMonnaie) {
		monnaieService.delete(codeMonnaie);
	}

	@PutMapping(path = "/monnaie/{codeMonnaie}")
	public ResponseEntity<Monnaie> modifyByCode(@PathVariable String codeMonnaie, @RequestBody TauxDeChangeDto tauxDeChangeDto, UriComponentsBuilder uriBuilder) 
	throws MonnaieInexistanteException {
		try {
			Monnaie monnaie = this.monnaieService.modify(codeMonnaie, tauxDeChangeDto.getTauxDeChange());
			return ResponseEntity.ok(monnaie);
		} catch (MonnaieInexistanteException e) {
			Monnaie monnaie = this.monnaieService.create(codeMonnaie, tauxDeChangeDto.getTauxDeChange());
			URI uri = uriBuilder.path("/monnaie/{codeMonnaie}").buildAndExpand(monnaie.getCode()).toUri();
			return ResponseEntity.created(uri).body(monnaie);
		}
	}
	
}
