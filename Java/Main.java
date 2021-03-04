import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

public class Main {


    public static String getFinalString(Path path) {

        Charset iso88599 = Charset.forName("ISO-8859-9");

        String corpus = "";

        try {
            corpus = Files.readString(path, iso88599);
            corpus = corpus.trim().replaceAll(" +", " "); // remove extra spaces
            corpus = corpus.replaceAll("[\\r\\n]+", " "); // remove newline and cr
            corpus = corpus.replaceAll("([?!]?[.]+)|([«\u0093{(].{1}[»\u0094})])|([^a-zA-Z0-9ğüşâöıçİÂĞÜŞÖÇ])", " $0 ");

            corpus = corpus.trim().replaceAll(" +", " ");// delete the first char in the string
        } catch (IOException ex) {
            System.out.println("File not found...");
        }

        return corpus.toLowerCase();
    }

    public static Map<String, Integer> get_ngrams(String[] words, int stepSize) {
        Map<String, Integer> ngram_map = new HashMap<String, Integer>();

        for (int i = 0; i < words.length - stepSize - 1; i++) {
            String key = String.join(" ", Arrays.copyOfRange(words, i, i + stepSize));
            if (ngram_map.containsKey(key)) {
                ngram_map.merge(key, 1, Integer::sum);
            } else {
                ngram_map.put(key, 1);
            }
        }
        return ngram_map;
    }

    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();
        // MAPS FOR TOP 100
        Map<String, Integer> unimap = new HashMap<String, Integer>();
        Map<String, Integer> bimap = new HashMap<String, Integer>();
        Map<String, Integer> trimap = new HashMap<String, Integer>();

        // PATHS FOR THE DOCUMENTS
        Path path1 = Paths.get("BİLİM İŞ BAŞINDA.txt");
        Path path2 = Paths.get("BOZKIRDA.txt");
        Path path3 = Paths.get("DEĞİŞİM.txt");
        Path path4 = Paths.get("DENEMELER.txt");
        Path path5 = Paths.get("UNUTULMUŞ DİYARLAR.txt");

        // READ THE DOCUMENTS INTO STRINGS
        String bib = getFinalString(path1);
        String boz = getFinalString(path2);
        String deg = getFinalString(path3);
        String den = getFinalString(path4);
        String unu = getFinalString(path5);

        // FINAL STRINGS CONCATENATED
        String ultimate_string = bib + " " + boz + " " + deg + " " + den + " " + unu;
        String[] words = ultimate_string.split(" ");

        // Lists for n-grams
        unimap = get_ngrams(words, 1);
        bimap = get_ngrams(words, 2);
        trimap = get_ngrams(words, 3);


        // TOP 100's
        // UNIMAP
        List<Map.Entry<String, Integer>> top100_unigram = unimap.entrySet().stream()
                .sorted(comparing(Map.Entry::getValue, reverseOrder()))
                .limit(100)
                .collect(toList());
        // BIMAP
        List<Map.Entry<String, Integer>> top100_bigram = bimap.entrySet().stream()
                .sorted(comparing(Map.Entry::getValue, reverseOrder()))
                .limit(100)
                .collect(toList());
        // TRIMAP
        List<Map.Entry<String, Integer>> top100_trigram = trimap.entrySet().stream()
                .sorted(comparing(Map.Entry::getValue, reverseOrder()))
                .limit(100)
                .collect(toList());

        System.out.println("TOP 100 FOR 1-GRAM: ");
        System.out.println(top100_unigram + "\n");
        System.out.println("TOP 100 FOR 2-GRAM: ");
        System.out.println(top100_bigram + "\n");
        System.out.println("TOP 100 FOR 3-GRAM: ");
        System.out.println(top100_trigram);

        long endTime = System.currentTimeMillis();

        System.out.println("\nRuntime: " + (endTime - startTime) + " milliseconds");
    }


}

