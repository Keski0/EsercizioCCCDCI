package pattern;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import model.Movimento;

public class ContoCorrente extends Conto {

	private final double tassoInteresseAnnuo = 0.07;
	
	
	public ContoCorrente(String titolare, LocalDate dataApertura) {
		super(titolare, dataApertura);
	}

	@Override
	public void eseguiMovimento(double denaro, LocalDate data, String tipo)
	{
		if(tipo.equals("Prelievo"))
		{
			preleva(denaro);
			
			Movimento movimento = new Movimento(data, "Prelievo", denaro, saldo);
			movimenti.add(movimento);
		} else if(tipo.equals("Versamento"))
		{
			versa(denaro);
			
			Movimento movimento = new Movimento(data, "Versamento", denaro, saldo);
			movimenti.add(movimento);
		}
	}
	
	@Override
	public void preleva(double denaro) {
		saldo -= denaro;
	}

	@Override
	public void versa(double denaro) {
		saldo += denaro;
	}
	
	@Override
	public double generaInteressi(LocalDate fineAnno, boolean primaVolta, double ultimoSaldo) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		System.out.println("| Tasso interesse annuo: " + tassoInteresseAnnuo);
		
		long giorni;
		LocalDate dataIniziale;
		LocalDate dataSuccessiva;
		boolean fine = false;
		double interessi = 0;
		boolean flagCalcolo = false;
		boolean noOperationsInYear = true;
		
		logger.info(ContoCorrente.class.getName() + " - Interessi anno " + fineAnno.getYear());
		for(int i = 0; i < movimenti.size(); i++)
		{
			dataIniziale = movimenti.get(i).getData();
			if(i < movimenti.size() - 1)
			{
				dataSuccessiva = movimenti.get(i + 1).getData();
			}
			else
			{
				dataSuccessiva = fineAnno;
			}
			
			if(dataIniziale.getYear() == fineAnno.getYear())
			{
				if(!primaVolta && !flagCalcolo)
				{
					dataSuccessiva = dataIniziale;
					dataIniziale = LocalDate.of(fineAnno.getYear(), 1, 1);
					primaVolta = true;
					flagCalcolo = true;
				}
				if(dataSuccessiva.getYear() != fineAnno.getYear())
				{
					dataSuccessiva = fineAnno;
					fine = true;
				}
				
				giorni = ChronoUnit.DAYS.between(dataIniziale, dataSuccessiva);
				double interesseMovimento = movimenti.get(i).getSaldoFinale() * ((tassoInteresseAnnuo / 365.0) * giorni);
				
				logger.info("Giorni: " + dataIniziale + " - " + dataSuccessiva + " = " + giorni);
				logger.info(movimenti.get(i).getSaldoFinale() + " * (" + tassoInteresseAnnuo + " / " + "365)" + " * " + giorni + " = " + decimalFormat.format(interesseMovimento));
				
				ultimoSaldo = movimenti.get(i).getSaldoFinale();
				
				interessi += interesseMovimento;
				
				noOperationsInYear = false;
				
				if(flagCalcolo)
				{
					flagCalcolo = false;
					i--;
				}
				
				if(fine)
				{
					i = movimenti.size();
				}
			}
		}
		
		if(noOperationsInYear)
		{
			giorni = ChronoUnit.DAYS.between(LocalDate.of(fineAnno.getYear(), 1, 1), fineAnno) + 1;
			double interesseMovimento = ultimoSaldo * ((tassoInteresseAnnuo / 365.0) * giorni);
			
			logger.info("Giorni: " + LocalDate.of(fineAnno.getYear(), 1, 1) + " - " + fineAnno + " = " + giorni);
			logger.info(ultimoSaldo + " * (" + tassoInteresseAnnuo + " / " + "365)" + " * " + giorni + " = " + decimalFormat.format(interesseMovimento));
			
			interessi += interesseMovimento;
		}
		stampaDettagli(fineAnno, interessi, decimalFormat);
		
		return ultimoSaldo;
	}
	
	@Override
	public LocalDate getPrimaData()
	{
		return movimenti.get(0).getData();
	}
	
	public void stampaDettagli(LocalDate fineAnno, double interessiLordi, DecimalFormat decimalFormat) {
		System.out.println("|--------------------------------------------------------------------------------------------------------------------|");
		System.out.println("| Data: " + fineAnno + " " + titolare);
		System.out.println("| Data       | Tipo Operazione | Quantita' | Saldo parziale");
		Movimento movimento;
		for(int i = 0; i < movimenti.size(); i++)
		{
			movimento = movimenti.get(i);
			String stringaDenaro = Double.toString(movimento.getDenaro());
			if(stringaDenaro.length() < 9)
			{
				int lunghezza = stringaDenaro.length();
				for(int k = 0; k < 9 - lunghezza; k++)
				{
					stringaDenaro += " ";
				}
			}

			String stringaTipo = movimento.getTipo();
			if(stringaTipo.length() < 15)
			{
				int lunghezza = stringaTipo.length();
				for(int k = 0; k < 15 - lunghezza; k++)
				{
					stringaTipo += " ";
				}
			}
			System.out.println("| " + movimento.getData() + " | " + stringaTipo + " | " + stringaDenaro + " | " + movimento.getSaldoFinale());
		}
		System.out.println("|");
		double saldoDopoOperazioni = saldo;
		System.out.println("| Saldo dopo le operazioni: " + decimalFormat.format(saldoDopoOperazioni));
		System.out.println("|");
		System.out.println("| Interessi maturati al 31/12 lordi: " + decimalFormat.format(interessiLordi));
		double interessiNetti = interessiLordi - (interessiLordi * 0.26);
		System.out.println("| Interessi maturati al 31/12 netti: " + decimalFormat.format(interessiNetti));
		System.out.println("|");
		saldo += interessiNetti;
		System.out.println("| Saldo finale: " + decimalFormat.format(saldo));
		System.out.println("|--------------------------------------------------------------------------------------------------------------------|");
		
		generaPDF(fineAnno, interessiLordi, interessiNetti, saldoDopoOperazioni, decimalFormat);
	}
	
	public void generaPDF(LocalDate fineAnno, double interessiLordi, double interessiNetti, double saldoDopoOperazioni, DecimalFormat decimalFormat)
	{
		String nomeDocumento;
		PdfDocument pdf;
		
		try {
			File directory = new File("./" + fineAnno.getYear() + "");
			directory.mkdir();
			nomeDocumento = "./" + fineAnno.getYear() + "/EC_" + titolare + "_" + fineAnno + ".pdf";
			pdf = new PdfDocument(new PdfWriter(nomeDocumento));
			Document document = new Document(pdf);
			
			String riga;
			
			riga = "|-----------------------------------------------------------------------------------|";
			document.add(new Paragraph(riga));
			
			riga = "| Data: " + fineAnno + " " + titolare;
			document.add(new Paragraph(riga));
			
			riga = "| Data            | Tipo Operazione    | Quantita' | Saldo parziale";
			document.add(new Paragraph(riga));
			
			Movimento movimento;
			for(int i = 0; i < movimenti.size(); i++)
			{
				movimento = movimenti.get(i);
				String stringaDenaro = Double.toString(movimento.getDenaro());
				if(stringaDenaro.length() < 9)
				{
					int lunghezza = stringaDenaro.length();
					for(int k = 0; k < 9 - lunghezza; k++)
					{
						stringaDenaro += " ";
					}
				}
				
				String stringaTipo = movimento.getTipo();
				if(stringaTipo.length() < 15)
				{
					int lunghezza = stringaTipo.length();
					for(int k = 0; k < 15 - lunghezza; k++)
					{
						stringaTipo += "  ";
					}
					if(movimento.getTipo().equals("Apertura"))
					{
						stringaTipo += "  ";
					} else if(movimento.getTipo().equals("Prelievo"))
					{
						stringaTipo += "   ";
					}
				}
				riga = "| " + movimento.getData() + " | " + stringaTipo + " | " + stringaDenaro + " | " + movimento.getSaldoFinale();
				document.add(new Paragraph(riga));
			}
			riga = "|";
			document.add(new Paragraph(riga));
			
			riga = "| Saldo dopo le operazioni: " + decimalFormat.format(saldoDopoOperazioni);
			document.add(new Paragraph(riga));
			
			riga = "|";
			document.add(new Paragraph(riga));
			
			riga = "| Interessi maturati al 31/12 lordi: " + decimalFormat.format(interessiLordi);
			document.add(new Paragraph(riga));
			
			riga = "| Interessi maturati al 31/12 netti: " + decimalFormat.format(interessiNetti);
			document.add(new Paragraph(riga));
			
			riga = "|";
			document.add(new Paragraph(riga));
			
			riga = "| Saldo finale: " + decimalFormat.format(saldo);
			document.add(new Paragraph(riga));
			
			riga = "|-----------------------------------------------------------------------------------|";
			document.add(new Paragraph(riga));
			
			document.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
