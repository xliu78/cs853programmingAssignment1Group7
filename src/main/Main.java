package main;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.read_data.DeserializeData;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import javax.print.Doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    protected static String filePath = "/Users/xinliu/Downloads/test200/test200-train/train.test200.cbor.paragraphs";
    protected static String indexPath = "/Users/xinliu/Documents/UNH/18Fall/cs853/index";
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        String query1 = "power nap benefits";
        String query2 = "whale vocalization production of sound";
        String query3 = "pokemon puzzle league";
        //read data


        List<Paragraph> list = new ArrayList<>();
        boolean defualtScore = false;
        try {
            FileInputStream stream = new FileInputStream(new File(filePath));
            for (Data.Paragraph data: DeserializeData.iterableParagraphs(stream)){
                Paragraph p = new Paragraph(data.getParaId(),data.getTextOnly());
                list.add(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CborException e) {
            e.printStackTrace();
        }
        search(query1,10,list,defualtScore);
        //search(query1,10,list);
//        search(query2,10,list);
//        search(query3,10,list);
    }
//    public static void search(String queryStr, int size, boolean default_engine) {
//        try {
//            ArrayList<Paragraph> dataList = ReadData.getAllParagraphFromDataSet();
//            Indexer indexer = new Indexer();
//            indexer.rebuildIndexes(dataList);
//
//            System.out.println("Search with search query ===> " + queryStr);
//            System.out.println("Default scoring function: " + default_engine);
//
//            /*
//             * SearchEngine(boolean default_engine), while true using default
//             * Lucene scoring function.
//             */
//            SearchEngine se = new SearchEngine(default_engine);
//            TopDocs topDocs = se.performSearch(queryStr, size);
//
//            System.out.println("Result found: " + topDocs.totalHits);
//
//            System.out.println("Rank ----- Paragraph ID -------------------- Score ------------- Text ---------  ");
//            ScoreDoc[] hits = topDocs.scoreDocs;
//
//            for (int i = 0; i < hits.length; i++) {
//                Document doc = se.getDocument(hits[i].doc);
//                System.out.println((i + 1) + ". " + doc.get("id") + " (" + hits[i].score + ") " + doc.get("text"));
//
//            }
//            System.out.println("Search done");
//
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

    public static void search(String query,int size,List<Paragraph> list,boolean defualtScore){


        try {
            Indexer indexer = new Indexer(defualtScore);
            indexer.rebuildIndexes(list);

            SearchEngine se = new SearchEngine(defualtScore);
            TopDocs topDocs = se.performSearch(query, size);
            System.out.println("Results found: " + topDocs.totalHits);
            ScoreDoc[] hits = topDocs.scoreDocs;
            System.out.println("hits lengtyh "+ hits.length);
            System.out.println("Rank -------------ID -------------------------Score ------------- Text ---------  ");
            for (int i = 0; i < hits.length;i++){
                Document doc = se.getDocument(hits[i].doc);
                System.out.println((i+1)+".   "+doc.get("id")+"   Score: "+hits[i].score+"  "+doc.get("text"));
            }
            System.out.println("search finished");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CborException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
