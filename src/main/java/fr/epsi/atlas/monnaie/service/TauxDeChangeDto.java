package fr.epsi.atlas.monnaie.service;

import java.math.BigDecimal;

public class TauxDeChangeDto {

	private BigDecimal tauxDeChange;

	
	public BigDecimal getTauxDeChange() {
		return tauxDeChange;
	}

	public void setTauxDeChange(BigDecimal tauxDeChange) {
		this.tauxDeChange = tauxDeChange;
	}
}