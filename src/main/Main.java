package main;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
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

    protected static String filePath = "/Users/xinliu/Documents/UNH/18Fall/cs853/test200/test200-train/train.pages.cbor-paragraphs.cbor";

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        String query1 = "power nap benefits";
        String query2 = "whale vocalization production of sound";
        String query3 = "pokemon puzzle league";
        //read data


        List<Paragraph> list = new ArrayList<>();
        boolean defaultScore = false;
        try {
            FileInputStream stream = new FileInputStream(new File(filePath));
            for (Data.Paragraph data: DeserializeData.iterableParagraphs(stream)){
                Paragraph p = new Paragraph(data.getParaId(),data.getTextOnly());
                list.add(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("==================Search with default score function=======================");
        System.out.println("");
        search(query1,10,list,true);
        search(query2,10,list,true);
        search(query3,10,list,true);

        System.out.println("===================search with custom score function========================");
        search(query1,10,list,false);
        search(query2,10,list,false);
        search(query3,10,list,false);

    }


    public static void search(String query,int size,List<Paragraph> list,boolean defaultScore){


        try {
            Indexer indexer = new Indexer(defaultScore);
            indexer.rebuildIndexes(list);

            SearchEngine se = new SearchEngine(defaultScore);
            TopDocs topDocs = se.performSearch(query, size);
            System.out.println("Search with search query ===> " + query);
            System.out.println("Search with default engine ===> " + defaultScore);
            System.out.println("Results found: " + topDocs.totalHits);
            ScoreDoc[] hits = topDocs.scoreDocs;
            System.out.println("Rank -------------ID -------------------------Score ------------- Text ---------  ");
            System.out.println("hits length "+ hits.length);
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
