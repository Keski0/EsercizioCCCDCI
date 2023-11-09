package pattern;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.Movimento;

public abstract class Conto {

	protected String titolare;
	protected LocalDate dataApertura;
	protected double saldo = 0;
	protected ArrayList<Movimento> movimenti;
	protected ArrayList<LocalDate> dateOrdinate;
	
	protected static final Logger logger = LogManager.getLogger("Conto");
	
	public Conto(String titolare, LocalDate dataApertura) {
		this.titolare = titolare;
		this.dataApertura = dataApertura;
		movimenti = new ArrayList<Movimento>();
		dateOrdinate = new ArrayList<LocalDate>();
		
		saldo += 1000;
		
		Movimento movimento = new Movimento(dataApertura, "Apertura", 1000, saldo);
		movimenti.add(movimento);
	}
	
	public abstract void preleva(double denaro);
	public abstract void versa(double denaro);
	public abstract void eseguiMovimento(double denaro, LocalDate data, String tipo);
	public abstract double generaInteressi(LocalDate fineAnno, boolean primaVolta, double ultimoSaldo);
	public abstract LocalDate getPrimaData();
}
