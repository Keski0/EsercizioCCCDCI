package util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import model.Movimento;

public class Util {
	
	public static LocalDate generaDataRandom(LocalDate dataMin, LocalDate dataMax)
	{
		int giorno, mese, anno;
		boolean errore = false;
		
		Random generator = new Random();
		
		do
		{
			anno = generator.nextInt(dataMax.getYear()) + 1;
		}while(anno < dataMin.getYear() || anno > dataMax.getYear());
		
		do
		{
			mese = generator.nextInt(12) + 1;
			if(mese < dataMin.getMonthValue())
			{
				errore = true;
			}
			else
			{
				errore = false;
			}
		}while(errore);
		
		do
		{
			if(mese == 1 || mese == 3 || mese == 5 || mese == 7 || mese == 8 || mese == 10 || mese == 12)
			{
				giorno = generator.nextInt(31) + 1;
			} else if(mese == 2)
			{
				if(LocalDate.of(anno, 1, 1).isLeapYear())
				{
					giorno = generator.nextInt(29) + 1;
				}
				else
				{
					giorno = generator.nextInt(28) + 1;
				}
			}
			else
			{
				giorno = generator.nextInt(30) + 1;
			}
			
			if(giorno < dataMin.getDayOfMonth())
			{
				errore = true;
			}
			else
			{
				errore = false;
			}
		}while(errore);
		
		
		return LocalDate.of(anno, mese, giorno);
	}
	
	public static void ordinaListaMovimenti(ArrayList<Movimento> lista)
	{
		Movimento a, b;
		for(int i = 0; i < lista.size(); i++)
		{
			for(int j = 0; j < lista.size() - 1; j++)
			{
				a = lista.get(i);
				b = lista.get(j);
				if(a.getData().getMonthValue() < b.getData().getMonthValue())
				{
					Collections.swap(lista, i, j);
				}
				else if(a.getData().getMonthValue() == b.getData().getMonthValue())
				{
					if(a.getData().getDayOfMonth() < b.getData().getDayOfMonth())
					{
						Collections.swap(lista, i, j);
					}
				}
			}
		}
	}
	
	public static void ordinaListaDate(ArrayList<LocalDate> lista)
	{
		LocalDate a, b;
		for(int i = 0; i < lista.size(); i++)
		{
			for(int j = 0; j < lista.size() - 1; j++)
			{
				a = lista.get(i);
				b = lista.get(j);
				if(a.getYear() < b.getYear())
				{
					Collections.swap(lista, i, j);
				} else if(a.getYear() == b.getYear())
				{
					if(a.getMonthValue() < b.getMonthValue())
					{
						Collections.swap(lista, i, j);
					}
					else if(a.getMonthValue() == b.getMonthValue())
					{
						if(a.getDayOfMonth() < b.getDayOfMonth())
						{
							Collections.swap(lista, i, j);
						}
					}
				}
			}
		}
	}
	
	public static double generaTassoInteresseAnnuo()
	{
		Random generator = new Random();
		
		int tassoInteresseAnnuo = generator.nextInt(101);
		int negativoPositivo = generator.nextInt(2);
		
		if(negativoPositivo == 0)
		{
			tassoInteresseAnnuo *= -1;
		}
		
		return tassoInteresseAnnuo / 100.0;
	}

}
