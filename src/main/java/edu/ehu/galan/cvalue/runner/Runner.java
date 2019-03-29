package edu.ehu.galan.cvalue.model;

import edu.ehu.galan.cvalue.CValueAlgortithm;
import edu.ehu.galan.cvalue.filters.english.NounFilter;
import vn.pipeline.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Runner {
    public static void main(String[] args) throws IOException {
        String[] annotators = {"pos"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);
        String str = "Con mèo ngu đang ăn cơm, con chó thì đứng nhìn";
        Annotation annotation = new Annotation(str);
        pipeline.annotate(annotation);

        List<LinkedList<Token>> tokenList = new ArrayList<>();
        LinkedList<Token> tokens = new LinkedList<Token>();
        for (Sentence sentence : annotation.getSentences()) {
            for (Word word : sentence.getWords()) {
//                System.out.println(word.getForm()+'\t'+word.getPosTag());
                tokens.add(new Token(word.getForm(), word.getPosTag()));
            }
        }
        tokenList.add(tokens);

        List<String> sentenceList = new ArrayList<>();
        sentenceList.add(str);

        Document doc = new Document("doc.txt","doc.txt");
        doc.setSentenceList(sentenceList);
        doc.List(tokenList);
        CValueAlgortithm cvalue = new CValueAlgortithm();
        cvalue.init(doc);
        cvalue.addNewProcessingFilter(new NounFilter());
        cvalue.runAlgorithm();
        for (Term term: doc.getTermList()){
            System.out.println(term.getTerm()+'\t'+term.getScore());
        }
    }
}
