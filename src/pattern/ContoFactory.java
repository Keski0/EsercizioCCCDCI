package pattern;

import java.time.LocalDate;

public class ContoFactory {
	
	public Conto getConto(String titolare, LocalDate dataApertura, String type) {
		
		if(type.equals("Corrente"))
		{
			return new ContoCorrente(titolare, dataApertura);
		} else if(type.equals("Deposito"))
		{
			return new ContoDeposito(titolare, dataApertura);
		} else if(type.equals("Investimento"))
		{
			return new ContoInvestimento(titolare, dataApertura);
		}
		
		return null;
	}

}
