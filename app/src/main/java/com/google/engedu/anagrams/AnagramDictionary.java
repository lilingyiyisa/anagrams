/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;

    //store words
    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            //key: "opst" value: ["post", "spot", "pots", "tops", ...].
            String sorted = sortLetters(word);
            if(lettersToWord.containsKey(sorted))
            {
                ArrayList<String> value = lettersToWord.get(sorted);
                value.add(word);
                lettersToWord.put(sorted, value);
            }
            else
            {
                ArrayList<String> value2 = new ArrayList<>();
                value2.add(word);
                lettersToWord.put(sorted, value2);
            }
            //sizeToWords.get(4)
            Integer wordsize = word.length();
            if(sizeToWords.containsKey(wordsize))
            {
                ArrayList<String> sizedwords = sizeToWords.get(wordsize);
                sizedwords.add(word);
                sizeToWords.put(wordsize, sizedwords);
            }
            else
            {
                ArrayList<String> sizedwords2 = new ArrayList<>();
                sizedwords2.add(word);
                sizeToWords.put(wordsize, sizedwords2);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        //word is in the dictionary
        boolean valid = wordSet.contains(word);
        //not add to start only
        boolean start = false;
        if(word.substring(1, word.length()-1).equals(base))
        {
            start = false;
        }
        else
        {
            start = true;
        }
        //not add to end only
        boolean end = false;
        if(word.substring(0, word.length()-2).equals(base))
        {
            end = false;
        }
        else
        {
            end = true;
        }
        if(start && end && valid)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //find all possible anagrams of targetWord
    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0; i<wordList.size(); i++)
        {
            String dictionword = wordList.get(i);
            if(dictionword.length() == targetWord.length())
            {
                if(sortLetters(dictionword).equals(sortLetters(targetWord)))
                {
                    result.add(dictionword);
                }
            }
        }
        return result;
    }

    public String sortLetters(String input) { //when no upper/lower mixed
        char[] temparray = input.toCharArray();
        Arrays.sort(temparray);
        String output = new String(temparray);
        return output;
    }

    //all possible combinations
    //!when show all answers, this includes base + char && char + base
    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            String letter = Character.toString(alphabet);
            String sortedword = sortLetters(word+letter);
            if(lettersToWord.containsKey(sortedword))
            {
                ArrayList<String> valuearray = lettersToWord.get(sortedword);
                for(int i=0; i<valuearray.size(); i++)
                {
                    result.add(valuearray.get(i));
                }
            }
        }
        return result;
    }

    //Randomly selects a word with at least the desired number of anagrams.
    public String pickGoodStarterWord() {
        String starter = "";
        String chosenword = "";
        int max = wordList.size()-1;
        int min = 0;
        int randomstart = random.nextInt(max - min + 1) + min;
        while(starter == "" && wordLength <= MAX_WORD_LENGTH)
        {
            for(int i=randomstart; i<wordList.size(); i++)
            {
                chosenword = wordList.get(i);
                int anagramnumber = getAnagramsWithOneMoreLetter(starter).size();
                int length = chosenword.length();
                if(anagramnumber > MIN_NUM_ANAGRAMS && length == wordLength)
                {
                    starter = chosenword;
                    return starter;
                }
            }
            for(int i=0; i<randomstart; i++)
            {
                chosenword = wordList.get(i);
                int anagramnumber = getAnagramsWithOneMoreLetter(starter).size();
                int length = chosenword.length();
                if(anagramnumber > MIN_NUM_ANAGRAMS && length == wordLength)
                {
                    starter = chosenword;
                    return starter;
                }
            }
            wordLength++;
        }
        return starter;
    }
}
