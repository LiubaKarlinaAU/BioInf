package ru.spbau.karlina.bioinf.fileParsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;

/** Special parser for mzXML
 *  Recognizes only scan number, retention time and precursorMZ */
public class ParserMZXML {

    static public ArrayList<MassSpectrum> getData(String fileName) {
        ArrayList<MassSpectrum> list = new ArrayList<>();
        try {
            Scanner scanner  = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String current = scanner.nextLine().trim();
                if (current.startsWith("<scan")) {
                    parseOneSpectrum(current, scanner, list);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Parsing mzXML file problem.");
            e.printStackTrace();
        }
         return list;

    }

    static private void parseOneSpectrum(String firstLine, Scanner scanner,
                                          ArrayList<MassSpectrum> list) {
      String second = scanner.nextLine().trim();
        if (second.charAt(9) == '2') {
            long num = parseLong(firstLine.substring(11, firstLine.length() - 1));
            double precMz = 0, rTime = 0;
            while (scanner.hasNextLine()) {
                String current = scanner.nextLine().trim();

                if (current.equals("</scan>")) {
                    list.add(new MassSpectrum(num, rTime, precMz));
                    return;
                }

                if (current.startsWith("retentionTime")) {
                    rTime = parseDouble(current.substring(17, current.length() - 2));
                } else if (current.startsWith("<precursorMz")) {
                    precMz = parseDouble(current.substring(current.indexOf('>') + 1, current.length() - 14));
                }
            }
        }
    }
}
