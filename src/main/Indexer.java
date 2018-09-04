package main;

import co.nstant.in.cbor.CborException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


public class Indexer {
    private boolean defaultEngine = true;

    public Indexer(){

    }

    public Indexer(boolean defaultEngine){
        this.defaultEngine = defaultEngine;
    }


    private IndexWriter indexWriter = null;
    //    static final String INDEX_DIRECTORY = "Users/xinliu/Documents/UNH/18Fall/cs853/index";
    public IndexWriter getIndexWriter(boolean create,boolean defaultEngine) throws IOException {
        if (indexWriter == null){
            Directory indexDir = FSDirectory.open(Paths.get("/Users/xinliu/Documents/UNH/18Fall/cs853/index"));
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());

            if (create) {
                config.setOpenMode(OpenMode.CREATE);
            } else {
                config.setOpenMode(OpenMode.CREATE_OR_APPEND);
            }

            if (!defaultEngine){
                config.setSimilarity(createCustomSimilarity());
            }


            indexWriter= new IndexWriter(indexDir,config);
        }
        return indexWriter;
    }
    private Similarity createCustomSimilarity() {

        Similarity sim = new SimilarityBase() {

            @Override
            protected float score(BasicStats stats, float freq, float docLen) {

                return freq;
            }

            @Override
            public String toString() {
                return null;
            }

        };

        return sim;
    }

    public void closeIndexWriter() throws IOException{
        if (indexWriter != null){
            indexWriter.close();
        }
    }

    public void indexFile(Paragraph p) throws IOException {
        if(p != null){
            IndexWriter writer = getIndexWriter(false,defaultEngine);
            Document d = new Document();
            d.add(new StringField("id",p.getParaID(), Field.Store.YES));
            d.add(new TextField("text",p.getParaText(),Field.Store.YES));
            d.add(new TextField("content",p.getParaText(), Field.Store.NO));

            writer.addDocument(d);
        }
    }

    public void rebuildIndexes(List<Paragraph> list) throws IOException, CborException {
        getIndexWriter(true,defaultEngine);
        if (!list.isEmpty()){
            for (Paragraph p : list){
                indexFile(p);
            }
            closeIndexWriter();
        }
    }

}

