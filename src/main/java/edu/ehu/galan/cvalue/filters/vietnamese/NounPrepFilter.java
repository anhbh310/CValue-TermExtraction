package edu.ehu.galan.cvalue.filters.vietnamese;

import edu.ehu.galan.cvalue.filters.ILinguisticFilter;
import edu.ehu.galan.cvalue.model.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NounPrepFilter implements ILinguisticFilter {
    private List<String> list;
    private List<Token> tokenList;

    @Override
    public List<String> getCandidates(LinkedList<Token> pSentence) {
        list = new ArrayList<>();
        if (pSentence != null) {
            for (Token token : pSentence) {
                if (token.getPosTag().matches("Np|Nc|N|Nu|Nb|Ny")) {
                    tokenList = new ArrayList<>();
                    tokenList.add(token);
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
        if (word.getPosTag().matches("Np|Nc|N|Nu|Nb|Ny|E")) {
            tokenList.add(word);
            candidate = candidate + " " + word.getWordForm();
            if (isFinishWithNoun()){
                list.add(candidate);
            }
            pPos++;
            if (sentenceSize - 1 == pPos + 1) {
                return;
            }
            findCandidate(pSentence, pPos, candidate);
        }
    }

    private boolean isFinishWithNoun() {
        return this.tokenList.get(tokenList.size() - 1).getPosTag().matches("Np|Nc|N|Nu|Nb|Ny");
    }

    private boolean havePrep(){
        for (Token token: this.tokenList){
            if (token.getPosTag().equalsIgnoreCase("E")){
                return true;
            }
        }
        return false;
    }
}
