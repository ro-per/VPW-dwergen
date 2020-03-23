import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	// public static long start, stop, totaal;
	public static Dwerg[] dwergen; // rij van dwergen

	// // ________________________ MAIN ________________________
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// VARIABELEN
		int n; // aantal testgevallen
		int W; // waarde in karaat per n (0<W<100)
		int D; // aantal dwergen per n (0<D<20)
		int A; // aantal diamanten per D (1<A<20)
		int Amin;
		int uitkomst;

		String[] diamantenStringRij;
		int[] diamanten;
		int[][] diamantenSom;

		// ITERATIE VARIABELEN
		int index; // level 1
		int i_dwerg; // level 2
		int aantalD; // level 2
		int i_diamant;// level 3

		// INLEZEN _______________________________________________1
		n = Integer.parseInt(sc.nextLine());
		// OVERLOOP AANTAL TESTGEVALLEN
		for (index = 0; index < n; index++) {
			W = Integer.parseInt(sc.nextLine()); // inlezen rekening
			D = Integer.parseInt(sc.nextLine()); // inlezen aantal dwergen

			// inlezen van diamantwaarden
			dwergen = new Dwerg[D];
			Amin = 20;
			for (i_dwerg = 0; i_dwerg < D; i_dwerg++) {
				// REGEL 1: aantal diamanten vd dwerg
				A = Integer.parseInt(sc.nextLine());
				// aantal diamanten van de dwerg die de minste diamanten heeft
				Amin = Math.min(A, Amin);
				// REGEL 2: de waarde van de diamanten
				diamantenStringRij = sc.nextLine().split(" ");
				diamanten = new int[A];
				for (i_diamant = 0; i_diamant < A; i_diamant++) {
					diamanten[i_diamant] = Integer.parseInt(diamantenStringRij[i_diamant]);
				}
				// AANMAKEN EN OPSLAAN VAN DWERG
				dwergen[i_dwerg] = new Dwerg(diamanten);
			}

			// BEPAAL MINIMALE UITGAVE _______________________________________________2
			// start = System.nanoTime();
			uitkomst = 0;
			// aantalDia per dwerg <= Amin (--> check Amin keer
			for (aantalD = 1; aantalD <= Amin; aantalD++) {
				diamantenSom = maakDiamantsom(aantalD);

				if (checkTransactie(diamantenSom, W, 0)) {
					uitkomst = aantalD;
					break;
				}
			}

			// PRINTEN _______________________________________________3
			if (index != 0) {
				System.out.println("");
			}
			if (uitkomst == 0) {
				System.out.print((index + 1) + " ONMOGELIJK");
			} else {
				System.out.print((index + 1) + " " + uitkomst);
			}

		}
		System.out.println("");
		sc.close();
		// stop = System.nanoTime();
		// System.out.println("");
		// System.out.println(stop - start);
	}

	// ________________________ SOM MAKEN ? ________________________
	public static int[][] maakDiamantsom(int aantalDia) {
		int[][] som = new int[dwergen.length][];
		// bij betalen 1 dia, geen som maken
		if (aantalDia == 1) {
			for (int i = 0; i < dwergen.length; i++) {
				som[i] = dwergen[i].getDiamanten();
			}
			return som;
			// bij betalen meerdere dia, wel som maken
		} else {
			for (int i = 0; i < dwergen.length; i++) {
				som[i] = dwergen[i].maakSom(aantalDia);
			}
			return som;
		}
	}

	// ________________________ TRANSACTIE NAKIJKEN ________________________
	public static boolean checkTransactie(int[][] diamantenSom, int rekening, int dwerg) {

		for (int i = 0; i < diamantenSom[dwerg].length; i++) {
			rekening -= diamantenSom[dwerg][i];

			if (rekening == 0 && dwerg == diamantenSom.length - 1) {
				return true;
			}

			if (rekening > 0 && dwerg != diamantenSom.length - 1) {
				// verder kijken
				if (checkTransactie(diamantenSom, rekening, dwerg + 1)) {
					return true;
				}
			}
			rekening += diamantenSom[dwerg][i];
		}
		return false;
	}

	// ________________________ DWERG ________________________
	static class Dwerg {
		public int[] diamanten;
		List<Integer> sommen;

		// CONSTRUCTOR
		Dwerg(int[] diamanten) {
			this.diamanten = diamanten;
		}

		// SOM VAN DIAMANTEN VAN DEZE DWERG
		public int[] maakSom(int aantalDia) {
			// maak een lijst van booleans die false zijn
			boolean[] gebruikt = new boolean[diamanten.length];
			for (int i = 0; i < gebruikt.length; i++) {
				gebruikt[i] = false;
			}

			sommen = new ArrayList<>();
			maakDeelSom(this.diamanten, aantalDia, 0, 0, sommen, gebruikt);

			// omzetten in array
			int[] somRij = new int[sommen.size()];
			int j = 0;
			for (Integer i : sommen) {
				somRij[j++] = i;
			}

			// leegmaken voor hergebruik van rij
			sommen.clear();

			return somRij;
		}

		// EFFECTIEVE SOM VAN EEN DWERG
		public static void maakDeelSom(int[] diamanten, int aantalDia, int pointer, int currLen, List<Integer> sommen,
				boolean[] gebruikt) {
			int som;

			// bij een hele rij ...
			if (currLen == aantalDia) {
				som = 0;
				// ... maken we de som ...
				for (int i = 0; i < diamanten.length; i++) {
					if (gebruikt[i]) {
						som += diamanten[i];
					}
				}
				// ... en slaan we hem op
				if (!sommen.contains(som)) {
					sommen.add(som);
				}
			}

			// bij einde terugkeren
			if (pointer == diamanten.length) {
				return;
			}

			gebruikt[pointer] = true;
			maakDeelSom(diamanten, aantalDia, pointer + 1, currLen + 1, sommen, gebruikt);

			gebruikt[pointer] = false;
			maakDeelSom(diamanten, aantalDia, pointer + 1, currLen, sommen, gebruikt);
		}

		// SCHRIJVER
		/*
		 * public void schrijfDwerg() { for (int i = 0; i < diamanten.length; i++) {
		 * System.out.print(diamanten[i] + " "); } System.out.println(); }
		 */
		// GETTERS
		public int[] getDiamanten() {
			return diamanten;
		}
	}
	// ________________________ ... ________________________

}
