package edu.ehu.galan.cvalue.filters.vietnamese;

import edu.ehu.galan.cvalue.filters.ILinguisticFilter;
import edu.ehu.galan.cvalue.model.Token;
import sun.util.locale.provider.FallbackLocaleProviderAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Filter that searchs for Noun+Noun using Penn Treebank Tags
 *
 * @author Angel Conde Manjon
 */
public class NounFilter implements ILinguisticFilter {

    private List<String> list;
    private List<String> stopWords;

    public NounFilter(String fileName) throws FileNotFoundException {
        stopWords = new ArrayList<>();
        Scanner scan = new Scanner(new File(fileName));
        while (scan.hasNext()){
            stopWords.add(scan.nextLine());
        }
    }

    public NounFilter(){}

    @Override
    public List<String> getCandidates(LinkedList<Token> pSentence) {
        list = new ArrayList<>();
        if (pSentence != null) {
            for (Token token : pSentence) {
                if (token.getPosTag().matches("Np|Nc|N|Nu|Nb|Ny")) {
                    int pos = pSentence.indexOf(token);
                    String candidate = token.getWordForm();
                    findCandidate(pSentence, pos, candidate);
                }
            }
        }
        return list;
    }

    private void findCandidate(LinkedList<Token> pSentence, int pPos, String candidate) {
        int sentenceSize = pSentence.size();
        Token word = pSentence.get(pPos + 1);
        if (word.getPosTag().matches("Np|Nc|N|Nu|Nb|Ny") && isStopWord(word.getWordForm())) {
            candidate = candidate + " " + word.getWordForm();
            list.add(candidate);
            pPos++;
//            if (sentenceSize - 1 == pPos || !(pSentence.get(pPos).getPosTag().matches("Np|Nc|N|Nu|Nb|Ny"))) {
            if (sentenceSize - 1 == pPos ) {
                return;
            }
            findCandidate(pSentence, pPos, candidate);
        }
    }

    private boolean isStopWord(String t) {
        if (this.stopWords == null) return true;
        for (String s : this.stopWords) {
            if (s.toLowerCase().compareTo(t.toLowerCase()) == 0) return false;
        }
        return true;
    }
};