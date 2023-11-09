package main;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import pattern.*;
import util.Util;

public class MainConto {
	
	public static void main(String[] args) {
		
		ContoFactory contoFactory = new ContoFactory();
		
		ContoCorrente contoCorrente = (ContoCorrente) contoFactory.getConto("Daniele", LocalDate.of(2021, 3, 12), "Corrente");
		ContoDeposito contoDeposito = (ContoDeposito) contoFactory.getConto("Alessia", LocalDate.of(2021, 1, 1), "Deposito");
		ContoInvestimento contoInvestimento = (ContoInvestimento) contoFactory.getConto("Marco", LocalDate.of(2021, 6, 2), "Investimento");
		
		Scanner scanner = new Scanner(System.in);
		int numeroOperazioni;
		String operazione;
		ArrayList<String> operazioni = new ArrayList<String>();
		double quantita[];

		System.out.println("|---------------------------------------------------CONTO CORRENTE---------------------------------------------------|");
		System.out.print("| Inserisci il numero di operazioni da effettuare: ");
		numeroOperazioni = scanner.nextInt();
		quantita = new double[numeroOperazioni];
		
		for(int i = 0; i < numeroOperazioni; i++)
		{
			System.out.print("| Inserisci il tipo di operazione da effettuare (Versamento - v, Prelievo - p): ");
			operazione = scanner.nextLine();
			if(operazione.isEmpty())
			{
				operazione = scanner.nextLine();
			}
			if(operazione.equals("v") || operazione.equals("V"))
			{
				operazione = "Versamento";
			} else if(operazione.equals("p") || operazione.equals("P"))
			{
				operazione = "Prelievo";
			}
			operazioni.add(operazione);
			System.out.print("| Inserisci la quantita': ");
			quantita[i] = scanner.nextDouble();
		}
		generaMovimenti(contoCorrente, operazioni, quantita);
		operaInteressi(contoCorrente);
		
		System.out.println("\n\n\n");
		
		operazioni.clear();
		
		System.out.println("|---------------------------------------------------CONTO DEPOSITO---------------------------------------------------|");
		System.out.print("| Inserisci il numero di operazioni da effettuare: ");
		numeroOperazioni = scanner.nextInt();
		quantita = new double[numeroOperazioni];
		for(int i = 0; i < numeroOperazioni; i++)
		{
			System.out.print("| Inserisci il tipo di operazione da effettuare (Versamento - v, Prelievo - p): ");
			operazione = scanner.nextLine();
			if(operazione.isEmpty())
			{
				operazione = scanner.nextLine();
			}
			if(operazione.equals("v") || operazione.equals("V"))
			{
				operazione = "Versamento";
			} else if(operazione.equals("p") || operazione.equals("P"))
			{
				operazione = "Prelievo";
			}
			operazioni.add(operazione);
			System.out.print("| Inserisci la quantita': ");
			quantita[i] = scanner.nextDouble();
		}
		generaMovimenti(contoDeposito, operazioni, quantita);
		operaInteressi(contoDeposito);
		
		System.out.println("\n\n\n");
		
		operazioni.clear();
		
		System.out.println("|-------------------------------------------------CONTO INVESTIMENTO-------------------------------------------------|");
		System.out.print("| Inserisci il numero di operazioni da effettuare: ");
		numeroOperazioni = scanner.nextInt();
		quantita = new double[numeroOperazioni];
		for(int i = 0; i < numeroOperazioni; i++)
		{
			System.out.print("| Inserisci il tipo di operazione da effettuare (Versamento - v, Prelievo - p): ");
			operazione = scanner.nextLine();
			if(operazione.isEmpty())
			{
				operazione = scanner.nextLine();
			}
			if(operazione.equals("v") || operazione.equals("V"))
			{
				operazione = "Versamento";
			} else if(operazione.equals("p") || operazione.equals("P"))
			{
				operazione = "Prelievo";
			}
			operazioni.add(operazione);

			System.out.print("| Inserisci la quantita': ");
			quantita[i] = scanner.nextDouble();
		}
		generaMovimenti(contoInvestimento, operazioni, quantita);
		operaInteressi(contoInvestimento);
		
		scanner.close();
	}
	
	private static void generaMovimenti(Conto conto, ArrayList<String> operazioni, double quantita[])
	{
		ArrayList<LocalDate> dateMovimenti = new ArrayList<LocalDate>();
		LocalDate dataGenerata;
		
		for(int i = 0; i < operazioni.size(); i++)
		{
			dataGenerata = Util.generaDataRandom(conto.getPrimaData(), LocalDate.of(2023, 12, 31));
			dateMovimenti.add(dataGenerata);
		}

		Util.ordinaListaDate(dateMovimenti);
		
		for(int i = 0; i < operazioni.size(); i++)
		{
			conto.eseguiMovimento(quantita[i], dateMovimenti.get(i), operazioni.get(i));
		}
	}
	
	private static void operaInteressi(Conto conto)
	{
		LocalDate dataOggi = LocalDate.now();
		int annoOggi = dataOggi.getYear();
		LocalDate primaData = conto.getPrimaData();
		int annoPrimaData = primaData.getYear();		
		int ripetizioni = annoOggi - annoPrimaData;
		boolean primaVolta;
		double ultimoSaldo = -1;
		
		for(int i = 0; i <= ripetizioni; i++)
		{
			if(i == 0)
			{
				primaVolta = true;
			}
			else
			{
				primaVolta = false;
			}
			ultimoSaldo = conto.generaInteressi(LocalDate.of(annoPrimaData + i, 12, 31), primaVolta, ultimoSaldo);
		}
	}
}
