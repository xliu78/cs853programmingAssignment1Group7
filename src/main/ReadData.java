package main;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar.Data;
import edu.unh.cs.treccar.read_data.DeserializeData;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadData {



    public static ArrayList<Paragraph> getAllParagraphFromDataSet() {
        ArrayList<Paragraph> pList = new ArrayList<Paragraph>();
//		String dataFilePath = "./DataSet/test200/train.test200.cbor.paragraphs";
        String dataFilePath = "/Users/xinliu/Downloads/test200/test200-train/train.test200.cbor.paragraphs";

        FileInputStream stream = readingDataFiles(dataFilePath);
        try {
            for (Data.Paragraph dataP : DeserializeData.iterableParagraphs(stream)) {
                // System.out.println(dataP.getParaId());
                // System.out.print(dataP.getEntitiesOnly());
                // System.out.println(dataP.getTextOnly());
                // System.out.println();
                Paragraph p = new Paragraph();
                p.setParaID(dataP.getParaId());
                p.setParaText(dataP.getTextOnly());
                pList.add(p);

            }
        } catch (CborException e) {
            e.printStackTrace();
        }
        System.out.println("Get " + pList.size() + " paragraphs in total by Treccar-tool.");
        return pList;
    }

    public static FileInputStream readingDataFiles(String dataFilesPath) {
        File file = new File(dataFilesPath);
        try {
            FileInputStream fis = new FileInputStream(file);
            System.out.println("Total file size to read (in bytes) : " + fis.available());
            return fis;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

    }
}
