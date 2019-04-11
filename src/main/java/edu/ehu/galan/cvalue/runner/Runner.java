package edu.ehu.galan.cvalue.runner;

import edu.ehu.galan.cvalue.model.*;
import edu.ehu.galan.cvalue.CValueAlgortithm;
import edu.ehu.galan.cvalue.filters.vietnamese.*;
import vn.pipeline.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Runner {
    public static void main(String[] args) throws IOException {
        String[] annotators = {"pos", "wseg"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);
        List<LinkedList<Token>> tokenList = new ArrayList<>();
        LinkedList<Token> tokens = new LinkedList<>();

        tokenList.add(tokens);

        Document doc = new Document("doc.txt", "doc.txt");
        doc.setSentenceList();
        List<String> sentenceList = doc.getSentenceList();

        for (String str : sentenceList) {
            Annotation annotation = new Annotation(str);
            pipeline.annotate(annotation);
            for (Sentence sentence: annotation.getSentences()){
                for (Word word: sentence.getWords()){
                    tokens.add(new Token(word.getForm(), word.getPosTag()));
                }
            }
        }

        doc.List(tokenList);
        CValueAlgortithm cvalue = new CValueAlgortithm();
        cvalue.init(doc);
        cvalue.addNewProcessingFilter(new NounFilter());
        cvalue.runAlgorithm();
        PrintWriter writer = new PrintWriter("output.txt");
        for (Term term : doc.getTermList()) {
            writer.println(term.getTerm() + '\t' + term.getScore());
        }
        writer.close();
    }
}
