package com.helmes.stetsko;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Anagrams {

    private static final String DEFAULT_ENCODING = "windows-1251";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in, DEFAULT_ENCODING);
        System.out.println("insert word:");
        String searchAnagramsFor = scanner.nextLine( );
        List<String> arrayList = new ArrayList<>( );

        try (BufferedReader bufferedReader = new BufferedReader
                (new FileReader(Paths.get("resources", "pldf-win.txt")
                        .toFile( )))) {
            bufferedReader.lines( ).forEach(arrayList::add);
        }

        String[] dictionary = new String[arrayList.size( )];

        for (int i = 0; i < arrayList.size( ); i++) {
            dictionary[i] = arrayList.get(i);
        }

        Collection collectAnagrams = collectAnagrams(dictionary, searchAnagramsFor);
        System.out.println("anagrams found: " + collectAnagrams); //collectAnagrams.toString( )
    }

    private static Collection collectAnagrams(String[] dictionary, String searchAnagramsFor) {
        if (searchAnagramsFor == null || searchAnagramsFor.length( ) < 1 || dictionary == null) {
            return Collections.EMPTY_LIST;
        }

        Map<Integer, Integer> searchingWord = countOfSymbols(searchAnagramsFor);
        return Arrays
                .stream(dictionary)
                .parallel( )
                .filter(el -> isAnagram(el, searchAnagramsFor, searchingWord))
                .collect(Collectors.toList( ));
    }

    private static boolean isAnagram(String firstWord, String secondWord, Map<Integer, Integer> countedSearchWord) {
        if (secondWord.equals(firstWord)) {
            return true;
        } else {
            if (firstWord == null || firstWord.length( ) != secondWord.length( )) {
                return false;
            }

            Map<Integer, Integer> countedAnagram = countOfSymbols(firstWord);
            for (Map.Entry entry : countedSearchWord.entrySet( )) {
                if (!countedAnagram.containsKey(entry.getKey( )) ||
                        !countedAnagram.get(entry.getKey( )).equals(entry.getValue( ))) {
                    return false;
                }
            }
            return true;
        }
    }

    private static Map<Integer, Integer> countOfSymbols(String word) {
        Map<Integer, Integer> result = new HashMap<>(word.length( ));
        word.chars( )
                .forEach(c -> result.merge(c, 1, (oldCount, newVal) -> oldCount + newVal));
        return result;
    }
}

