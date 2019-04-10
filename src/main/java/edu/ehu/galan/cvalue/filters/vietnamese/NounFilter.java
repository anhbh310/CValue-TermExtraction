package edu.ehu.galan.cvalue.filters.vietnamese;

import edu.ehu.galan.cvalue.filters.ILinguisticFilter;
import edu.ehu.galan.cvalue.model.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Filter that searchs for Noun+Noun using Penn Treebank Tags
 *
 * @author Angel Conde Manjon
 */
public class NounFilter implements ILinguisticFilter {

    private List<String> list;

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
        if (word.getPosTag().matches("Np|Nc|N|Nu|Nb|Ny")) {
            candidate = candidate + " " + word.getWordForm();
            list.add(candidate);
            pPos++;
            if (sentenceSize - 1 == pPos || !(pSentence.get(pPos).getPosTag().matches("Np|Nc|N|Nu|Nb|Ny"))) {
                return;
            }
            findCandidate(pSentence, pPos, candidate);
        }
    }
}