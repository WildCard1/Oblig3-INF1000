/***************************************************************************	
*	Helt Ferdig:
* 	- Les fra fil.
*	- (1) Skriv oversikt.
* 	- (2) Registrer ny leietager.
* 	- (3) Registere betaling fra leietager.
* 	- (4) Registrer utflytting.
* 	- (5) Kjør månedskjøring.
* 	- (6) Kast ut leietager
* 	- (7) Øk husleie
* 	- (8) Avslutt.
* 	
*	 Spørsmål/problemer:
* 	- Hva menes med "For å sikre at den månedlige beregningen av husleie og fortjeneste bare skjer en gang per måned,
* 	  må du nok spørre Guldbrand om hvilke måned han må oppdatere for?"
****************************************************************************/

import java.util.*;
import java.io.*;
import easyIO.*;

class Oblig3 {
	public static void main(String[]args) {
	    Hybelhus hh = new Hybelhus();
	    hh.lesFil();
	}
}

class Hybelhus {

	In tast = new In();
	In hybeldata = new In("hybeldata.txt");
	final int ANTALLHYBLER = 6;
	final int ANTALLETG = 3;
	int maaned, aar, totalFortjeneste, totaltAntallMaaneder, maanedsleieVanligHybel, maanedsleieToppEtasjeHybel;
	Hybel[][] hybel = new Hybel[ANTALLETG][ANTALLHYBLER];
	char valg;
	char nummer;
	int etasje;
//Starter å lese inn data fra hybeldata, og lagre det i lokale variabler.

	void menyvalg() {
		System.out.println("\nVelkommen. Her er en liste over hva du kan gjøre:");
		System.out.println("1. Skriv oversikt");
		System.out.println("2. Registrere ny leietager ");
		System.out.println("3. Registrere betaling fra leietager");
		System.out.println("4. Registrere frivillig utflytting");
		System.out.println("5. Månedskjøring av husleie");
		System.out.println("6. Kast ut leietagere");
		System.out.println("7. Øk husleien");
		System.out.println("8. Avslutt\n");
		System.out.print("Tast nummeret for hva du ønsker å gjøre: ");
		
		int menyvalg = 0;
		menyvalg = tast.inInt();

		while (menyvalg > 8 || menyvalg < 0) {
		    System.out.print("Du må velge et menyvalg mellom 1 til 8: ");
		    menyvalg = tast.inInt();
		}

		switch(menyvalg) {

		case 1:
		    skrivOversikt();
		   	break;
		case 2:
			regLeietager();
			break;
		case 3:
		   regBetaling();
		   break;
		case 4:
		   regUtflytting();
		   break;
		case 5:
		   maanedskjoring();
		   break;
		case 6:
		   kastUt();
		   break;
		case 7:
		   okHusleie(maanedsleieVanligHybel, maanedsleieToppEtasjeHybel);
		   break;
		case 8:
		   avslutt();
		   System.exit(0);
		}


	}
// Her leses filen "hybeldata.txt" inn, og verdier legges inn i lokale variabler.
	
	void lesFil() {
		int i = 0;
		while (i < 1) {
			maaned = hybeldata.inInt(";"); 
			aar = hybeldata.inInt(";"); 
			totalFortjeneste = hybeldata.inInt(";"); 
			totaltAntallMaaneder = hybeldata.inInt(";"); 
			maanedsleieVanligHybel = hybeldata.inInt(" ;"); 
			maanedsleieToppEtasjeHybel = hybeldata.inInt(" ;"); 
			i++;
				for (int j = 0; j < 18 || !hybeldata.endOfFile(); j++) {
					etasje = hybeldata.inInt(";");	
					nummer = hybeldata.inChar(";"); 
					int saldo = hybeldata.inInt(";");
					String studentnavn = hybeldata.inLine(); 
					hybel[etasje-1][(int)(nummer-65)] = new Hybel(studentnavn, saldo);
					// Legger det inn korrekt i pekeren til "Hybel".
					// bruker (int)(nummer-65) for å konvertere det til rett leilighetnr,
					// og ikke (-64) fordi det da ikke stemmer med array-indeksene.
					// Array-indeks [0][0] representerer da etgnr 1 husnr A(osv...)
					// At det er akuratt -64 fant jeg ut ved å se på en ASCII-table på google.
				}
		}
		menyvalg();
	}

	//Menyvalg 1, skriv oversikt.
	
	void skrivOversikt() {
		String studentnavn; // Lager lokalvariabel her, istedenfor å skifte på hybel sin.		
		for (int i = 0; i < 3; i++) {
			for(int j = 0; j < 6; j++) {
				if (hybel[i][j].studentnavn.equals("TOM HYBEL")) 
					studentnavn = "LEDIG";
					else
						studentnavn = hybel[i][j].studentnavn;
				System.out.println("\n*********************************");
				System.out.println("Hybel" + (i+1) + ((char)(65+j)) + ":"); //(char) for å konvertere til bokstav				
				System.out.println("Studentnavn: " + studentnavn);
				System.out.println("Saldo: " + hybel[i][j].saldo);				
				System.out.println("*********************************\n");
				
			} // Slutt på andre for-løkke
		} // Slutt på første for-løkke
		
		System.out.println("Nåværende måned: " + maaned);
		System.out.println("Nåværende år: " + aar);
		System.out.println("Driftstid i måneder: " + totaltAntallMaaneder); 	
		System.out.println("Total fortjeneste: " + totalFortjeneste);
		System.out.println();	
		
		menyvalg();	
	}
	
	//Menyvalg 2, registrer ny leietager
	
	String linje; //String linje, siden programmet krasjet når jeg prøvde å lese med inChar()...
	
	void regLeietager() {
		boolean ledigeHybler = false;
		
		System.out.println("Disse hyblene er ledige: ");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				if (hybel[i][j].studentnavn.equals("TOM HYBEL")) { 
					ledigeHybler = true;
					System.out.println("\nHybel" + (i+1) + ((char)(65+j)));
				}
			} // Slutt på andre for-løkke
		} // Slutt på første for-løkke
 
		
		if (ledigeHybler == true) {
			System.out.print("Vennligst velg hvilket etasje studenten ønsker å leie i: ");
			etasje = tast.inInt();
			while(etasje < 1 || etasje > 3) {
				System.out.print("Du må velge en etasje mellom 1 og 3: ");
				etasje = tast.inInt();
				} 
			System.out.print("Vennligst velg hvilket rom studenten ønsker å leie: ");
			linje = tast.inWord();
			nummer = linje.charAt(0);
			while(nummer != 'A' && nummer != 'B' && nummer != 'C' && nummer != 'D' && nummer != 'E' && nummer != 'F') {
				System.out.print("Du må velge en bokstav mellom A-F: ");
				linje = tast.inWord();
				nummer = linje.charAt(0);
			}
			if (hybel[etasje-1][((int)(nummer-65))].studentnavn.equals("TOM HYBEL")) {
				System.out.print("Vennligst tast inn studentens navn: ");
				hybel[etasje-1][((int)(nummer-65))].studentnavn = tast.inLine();
				depositum();
				System.out.print("\nHybel" + etasje + nummer + " er nå eid av " + hybel[etasje-1][((int)(nummer-65))].studentnavn);
				System.out.println(" og studentens saldo nå er: " + hybel[etasje-1][((int)(nummer-65))].saldo);				
				System.out.println();
				menyvalg();
			}	else{
					System.out.println("\nDu må velge en ledig hybel!");
					regLeietager(); 
				}
		}	else{
			System.out.println("\nBeklager, det er ingen ledige hybler igjen. Prøv igjen senere.\n");
			} // Slutt på første if-setning
			
		menyvalg();
	} // Slutt på regLeietager()
	
	void depositum() {	
		
		hybel[etasje-1][((int)(nummer-65))].saldo = 15000;
		
		if (etasje == 3) {
			System.out.println("\nDepositum på 15000,- er nå trukket fra din bankkonto, ");
			System.out.println("og " + maanedsleieToppEtasjeHybel + " er trukket fra ditt dep."); 
			hybel[etasje-1][((int)(nummer-65))].saldo -= maanedsleieToppEtasjeHybel;
			} else 	{
				System.out.println("\nDepositum på 15000,- er nå trukket fra din bankkonto, ");
                        	System.out.println("og " + maanedsleieVanligHybel + " er trukket fra ditt dep."); 
				hybel[etasje-1][((int)(nummer-65))].saldo -= maanedsleieVanligHybel; 
				}
	}
	
	//Menyvalg 3, registere betaling fra leietager.
	
	void regBetaling() {
		System.out.print("Vennligst tast inn hvilken etasje studenten bor i: ");
		int etasje = tast.inInt();
		while(etasje < 1 || etasje > 3) {
                	System.out.print("Du må velge en etasje mellom 1 og 3: ");
               		etasje = tast.inInt();
		}
		System.out.print("Vennligst tast inn romnr. studenten bor i(husk stor bokstav): ");
		String linje = tast.inWord();
		nummer = linje.charAt(0);
		while(nummer != 'A' && nummer != 'B' && nummer != 'C' && nummer != 'D' && nummer != 'E' && nummer != 'F') {
                	System.out.print("Du må velge en bokstav mellom A-F: ");
                       	linje = tast.inWord();
                       	nummer = linje.charAt(0); 
		}
		if (!hybel[etasje-1][((int)(nummer-65))].studentnavn.equals("TOM HYBEL")) {
				System.out.print("Hvor mye ønskes å sette inn på kontoen?: ");
				int belop = tast.inInt();
				hybel[etasje-1][((int)(nummer-65))].saldo += belop;
				System.out.println();
				System.out.print(belop + "kr har blitt tilført saldoen.");
				System.out.println(" Saldo er nå: " + hybel[etasje-1][((int)(nummer-65))].saldo);
				System.out.println();
			}else 	{
					System.out.println("Du har valgt en hybel uten leietager. Prøv på ny");
					regBetaling();
				}
		menyvalg();
	}
	
	//Menyvalg 4, registrere frivillig utflytting.
	
	void regUtflytting() {
		boolean funnet = false;
		System.out.print("Vennligst tast inn studentens navn: ");
		String navn = tast.inLine();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				if (navn.equalsIgnoreCase(hybel[i][j].studentnavn)) {
					funnet = true;
					sjekkSaldo(hybel[i][j].saldo, i, j);
					} 
			} // Slutt på andre for-løkke
		} // Slutt på første for-løkke
		if (!funnet) {
			System.out.print("Beklager, vi kunne ikke finne studentens navn i vårt system");
			System.out.print("Ønsker du å prøve igjen(J/N)? :");
			String linje = tast.inWord();
			valg = linje.charAt(0);
			while (valg != 'j' && valg != 'J' && valg != 'n' && valg != 'N') {
				System.out.print("Tast 'J' hvis du ønsker å prøve igjen, og 'N' hvis ikke: ");
				linje = tast.inWord();
				valg = linje.charAt(0);
			}
			if (valg == 'j' || valg == 'J') {
				regUtflytting();
				} else {
					menyvalg();		
							}
				
		}
	} // Slutt på regUtflytting()
		
	//sjekkSaldo ble litt rotete, men den gjør jobben...
	void sjekkSaldo(int n, int e, int b) {
		int saldo = n;
		int etasje = e; 
		int nummer = b; 
		if (e < 2) {
			if (saldo < maanedsleieVanligHybel) {
				System.out.print(" Studenten må først betale denne månedsleien.");
				System.out.print(" Studenten mangler " + (maanedsleieVanligHybel-saldo) + " kr");
				System.out.println(" Du blir nå sendt over til betalingsløsningen");
				regBetaling();
				} else {
					hybel[etasje][nummer].saldo -= maanedsleieVanligHybel;
					hybel[etasje][nummer].studentnavn = "TOM HYBEL";
					System.out.print("Studenten er nå strøket som leietager på denne hybelen");
					System.out.println(" Denne måneds månedsleie er trukket");
					System.out.print("Resterende saldo er: " + hybel[etasje][nummer].saldo + "kr\n");
					
					if (hybel[etasje][nummer].saldo > 0) {
						System.out.println(" Dette beløpet er nå satt over på studentens bankkonto");
						maanedsfortjeneste += maanedsleieVanligHybel;
						hybel[etasje][nummer].saldo = 0;
						menyvalg();	
				}
		}
	}
		if (e == 2) {
			if (saldo < maanedsleieToppEtasjeHybel) {
				System.out.print(" Studenten må først betale denne månedsleien");
				System.out.print(" Det mangles " + (maanedsleieToppEtasjeHybel-saldo) + " kr");
				System.out.println(" Du blir nå sendt over til betalingsløsningen");
					regBetaling();
					}else {
						hybel[etasje][nummer].saldo -= maanedsleieToppEtasjeHybel;
						hybel[etasje][nummer].studentnavn = "TOM HYBEL";
						System.out.print(" \nStudenten er nå strøket som leietager på denne hybelen.");
						System.out.println(" Denne måneds månedsleie er trukket.");
						System.out.print(" \nResterende saldo er: " + hybel[etasje][nummer].saldo + "kr\n");
						if (hybel[etasje][nummer].saldo > 0)
							System.out.println(" Dette beløpet er nå satt over på studentens bankkonto");
							maanedsfortjeneste += maanedsleieToppEtasjeHybel;
							hybel[etasje][nummer].saldo = 0;
							menyvalg();
					}
			} 
	} // Slutt på sjekkSaldo()
	
	//Menyvalg 5, kjør månedskjøring.
	
	int nesteMaaned = 0;
	int nesteAar = 0;
	int maanedsfortjeneste = 0;
	int gMaanedsfortjeneste = 0;
	int gammelSaldo = 0; // Hjelpevariabel for å få maanedfortjeneste korrekt.
	int antallMaaneder = 0;
	
	void maanedskjoring() {
		if (maaned == 12) {
			nesteMaaned = 1;
			nesteAar = aar + 1;
		}else {
			nesteMaaned = maaned + 1;
			nesteAar = aar;
		}
		
		System.out.print("\nØnsker du å kjøre månedskjøring for " + nesteMaaned + "/" + nesteAar + " (J/N)?: ");
		linje = tast.inWord();
		valg = linje.charAt(0);
		while (valg != 'j' && valg != 'J' && valg != 'n' && valg != 'N') {
               		System.out.print("Tast 'J' hvis du ønsker å kjøre månedskjøring, og 'N' hvis ikke: ");
                    	linje = tast.inWord();
                   	valg = linje.charAt(0);
		}
		if (valg == 'J' || valg == 'j') {
			antallMaaneder = totaltAntallMaaneder + 1;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 6; j++) {
					if (i < 2 && !hybel[i][j].studentnavn.equals("TOM HYBEL")) {
						gammelSaldo = hybel[i][j].saldo;
						hybel[i][j].saldo -= maanedsleieVanligHybel;
						if (gammelSaldo < maanedsleieVanligHybel && gammelSaldo > 0) 
							maanedsfortjeneste += gammelSaldo;
						else if (gammelSaldo >= maanedsleieVanligHybel)
							maanedsfortjeneste += maanedsleieVanligHybel;
						else
							maanedsfortjeneste += 0;

					} // Slutt på if 
					
					if (i == 2 && !hybel[i][j].studentnavn.equals("TOM HYBEL"))  {
						gammelSaldo = hybel[i][j].saldo;
						hybel[i][j].saldo -= maanedsleieToppEtasjeHybel;
						if (gammelSaldo < maanedsleieToppEtasjeHybel && gammelSaldo > 0) 
							maanedsfortjeneste += gammelSaldo;
						else if (gammelSaldo >= maanedsleieToppEtasjeHybel)
							maanedsfortjeneste += maanedsleieToppEtasjeHybel;
						else
							maanedsfortjeneste += 0;
					}		
				} // Slutt på andre for-løkke
			} // Slutt på første for-løkke
		
		totaltAntallMaaneder = antallMaaneder;
		maanedsfortjeneste -= ((1200*18) + (1700*3));
		totalFortjeneste += maanedsfortjeneste;
		gMaanedsfortjeneste = totalFortjeneste / totaltAntallMaaneder;
		maaned = nesteMaaned;
		aar = nesteAar;
		
		System.out.println("\n***************************************************************");
		System.out.println("Månedskjøringen gjaldt for: " + "		" + maaned + "/" + aar);
		System.out.println("Husleiesatsene er: " );
		System.out.println("	Vanlig hybel: " + "			" +maanedsleieVanligHybel);
		System.out.println("	Toppetasje hybel: " + "		" + maanedsleieToppEtasjeHybel);
		System.out.println("Månedsfortjenesten er: " + "			" + maanedsfortjeneste);
		System.out.println("Totalfortjeneste er: " + "			" + totalFortjeneste);
		System.out.println("Gjennomsnittslig månedsfortjeneste er:  " + gMaanedsfortjeneste);
		System.out.println("***************************************************************");
		
		}else{
			System.out.println("\nDu vil nå returnere til menyen");
		} // Slutt på else
		maanedsfortjeneste = 0;
		menyvalg();
	} // Slutt på maanedskjoring()
	
	//Menyvalg 6, kast ut leietager
	
	void kastUt() {
		boolean skylder = false;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				if (i < 2) {
					if (hybel[i][j].saldo < (-maanedsleieVanligHybel-1)) {
						skylder = true;
						tilkallHole(i, j, hybel[i][j].saldo);
					} 		
				}else {
						if (hybel[i][j].saldo < (-maanedsleieToppEtasjeHybel-1)) {
							skylder = true;
							tilkallHole(i, j, hybel[i][j].saldo);
						} // Slutt på siste if
						} // Slutt på else
			} // Slutt på andre for-løkke
		} // Slutt på første for-løkke 
		if (!skylder)
		menyvalg();
	} // Slutt på kastUt()

	void tilkallHole(int etasje, int rom, int saldo) {
		char rom1 = (char)(rom+65);
		saldo = -saldo; // For å gjøre om negativ saldo til positiv(for å fungere med tot.fortj.)
		System.out.println();
		System.out.print(hybel[etasje][rom].studentnavn);
		System.out.print(" i hybel " + (etasje+1) + rom1);
		System.out.print(" skylder mer enn en månedsleie, " + saldo + "kr, og kastes med dette ut.\n");
		totalFortjeneste += saldo + 1500;


		Out torpedo = new Out("torpedo.txt", true);
		torpedo.out(etasje+1);
		torpedo.out(rom1 +";");
		torpedo.out((saldo- 3000) +";");
		torpedo.outln((hybel[etasje][rom].studentnavn) + ";");
		torpedo.close();
		
		hybel[etasje][rom].studentnavn = "TOM HYBEL";
		hybel[etasje][rom].saldo = 0;
		kastUt();
	} // Slutt på hjelpemetoden tilkallHole
	
	//Menyvalg 7, øk husleien
	
	void okHusleie(int vanlig, int topp) {
		System.out.println("På nåværende tidspunkt er husleien: ");
		System.out.println("Vanlig: " + vanlig);
		System.out.println("Toppetasje: " + topp);
		
		System.out.print("Ønsker du å forandre på den vanlige husleiesatsen(J/N)?: ");
		String linje = tast.inWord();
		valg = linje.charAt(0);
		while (valg != 'j' && valg != 'J' && valg != 'n' && valg != 'N') {
                	System.out.print("Tast 'J' hvis du ønsker å forandre leiesatsen, og 'N' hvis ikke: ");
                       	linje = tast.inWord();
                       	valg = linje.charAt(0);
		}
		if (valg == 'J' || valg == 'j') {
			System.out.print("Hva ønsker du å forandre den til?: ");
			maanedsleieVanligHybel = tast.inInt();
			vanlig = maanedsleieVanligHybel;
			System.out.println("Nåværende vanlig leiesats er på " + vanlig + " kr");
		}
		
		System.out.print("Ønsker du å forandre toppetasje-husleien(J/N)?: ");
		linje = tast.inWord();
		valg = linje.charAt(0);
		while (valg != 'j' && valg != 'J' && valg != 'n' && valg != 'N') {
                  	System.out.print("Tast 'J' hvis du ønsker å forandre leiesatsen, og 'N' hvis ikke: ");
                    	linje = tast.inWord();
                        valg = linje.charAt(0);
		}
		if (valg == 'J' || valg == 'j') {
			System.out.print("Hva ønsker du å forandre den til?: ");
			maanedsleieToppEtasjeHybel = tast.inInt();
			topp = maanedsleieToppEtasjeHybel;
			System.out.println("Nåværende toppetasje leiesats er på " + topp + " kr"); 
		}
		menyvalg();
		
		
		} // Slutt på okHusleie()
	//Menyvalg 8, avslutt.
	void avslutt() {
		System.out.print("Vil du virkelig avslutte(J/N): ");
		linje = tast.inWord();
		valg = linje.charAt(0);
		while (valg != 'j' && valg != 'J' && valg != 'n' && valg != 'N') {
                	System.out.print("Tast 'J' hvis du ønsker å avslutte, og 'N' hvis ikke: ");
                      	linje = tast.inWord();
                    	valg = linje.charAt(0);
		}
		if (valg == 'J' || valg == 'j') {
			Out hybeldataUt = new Out("hybeldata.txt"); 
			System.out.println("\nSystemet vil nå avslutte, på anmodning fra brukeren.");
			System.out.println("\nNødvendig data er nå lagret i 'hybeldata.txt'. Ha en fortsatt fin dag!");
			hybeldataUt.out(maaned + ";" + aar + ";" + totalFortjeneste + ";");
			hybeldataUt.out(totaltAntallMaaneder + "; " + maanedsleieVanligHybel + "; ");
			hybeldataUt.outln(maanedsleieToppEtasjeHybel + ";");
			for(int i = 0; i < 3; i++) {
				for (int j = 0; j < 6; j++) {
					hybeldataUt.out((i+1) + ";" + ((char)(65+j)) + ";");
					hybeldataUt.out(hybel[i][j].saldo + ";");
					hybeldataUt.outln(hybel[i][j].studentnavn);
				}
			}
			hybeldataUt.close();
			System.exit(0);
			}else{
				System.out.println("Du vil nå returnere til menyen");
				menyvalg();
				}
			
			
			
			} // Slutt på avslutt.
}  //Slutt på klassen Hybelhus
	
class Hybel {

	String studentnavn;
	int saldo;

	Hybel(String n, int s) {
		saldo = s;
		studentnavn = n;
	}
}

