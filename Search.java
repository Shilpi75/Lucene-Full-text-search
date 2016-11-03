/**
 * Created by shilpi on 2/11/16.
 */

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.Scanner;


public class Search {

    public static void main(String[] args) throws IOException {
        String string;
        long startTime, endTime;
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter w = new IndexWriter(index, config);


        File folder = new File("/home/shilpi/Desktop/music");
        File[] listOfFiles = folder.listFiles();

        int i = 0;
        System.out.println(System.currentTimeMillis());
        for (File file : listOfFiles) {
            if (file.isFile()) {
                BufferedReader br = new BufferedReader(new FileReader(file));

                String text[] = br.readLine().toString().split(":");
                String title = text[1];
                br.readLine();
                String text2[] = br.readLine().toString().split(":");
                String artist = text2[1];
                br.readLine();
                String text3[] = br.readLine().toString().split(":");
                String album = text3[1];
                br.readLine();
                String text4[] = br.readLine().toString().split(":");
                String lyrics = text4[1];
                Document doc = new Document();

             //   System.out.println(title+" "+artist+" "+album+" "+lyrics);
                doc.add(new TextField("title", title, Field.Store.YES));
                doc.add(new StringField("artist", artist, Field.Store.YES));
                doc.add(new StringField("album", album, Field.Store.YES));
                doc.add(new TextField("lyrics", lyrics, Field.Store.YES));

                w.addDocument(doc);

                System.out.println("Inserted: " + (i++));


            }


        }
        w.close();
        System.out.println(System.currentTimeMillis());

        //Query
        char choice = 'y';
        int option;
        do {

            System.out.println("\nEnter 1. Phrase Search \n 2. Free Text Search\n");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            option = Integer.parseInt(br.readLine());
            if (option == 2) {

                System.out.println("Enter String: ");
                string = br.readLine();
                startTime = System.currentTimeMillis();
                try {

                    System.out.println("String: "+string+"\n");

                   //PhraseQuery query = new PhraseQuery();

                    Query q = new QueryParser("lyrics", analyzer).parse(string);
                    System.out.println(q);
                    int hitsPerPage = 20;
                    IndexReader reader = DirectoryReader.open(index);
                    IndexSearcher searcher = new IndexSearcher(reader);
                    TopDocs docs = searcher.search(q, hitsPerPage);
                    ScoreDoc[] hits = docs.scoreDocs;
                    System.out.println(hits.length);
                    for(int j=0;j<hits.length;++j) {
                        int docId = hits[j].doc;
                        Document d = searcher.doc(docId);
                        System.out.println((j + 1) + ". " + d.get("title") + "\n" + d.get("artist")+"\n"+d.get("album"));
                    }



                } catch (ParseException e) {
                    e.printStackTrace();
                }

                endTime = System.currentTimeMillis();
                System.out.println("Time: " + (endTime - startTime));
            } else if (option == 1) {


                System.out.println("Enter String: ");
                string = br.readLine();
                startTime = System.currentTimeMillis();
                try {

                    System.out.println("String: "+string+"\n");

                    //PhraseQuery query = new PhraseQuery();

                    Query q = new QueryParser("lyrics", analyzer).parse("\""+string+"\"");
                    System.out.println(q);
                    int hitsPerPage = 20;
                    IndexReader reader = DirectoryReader.open(index);
                    IndexSearcher searcher = new IndexSearcher(reader);
                    TopDocs docs = searcher.search(q, hitsPerPage);
                    ScoreDoc[] hits = docs.scoreDocs;
                    System.out.println(hits.length);
                    for(int j=0;j<hits.length;++j) {
                        int docId = hits[j].doc;
                        Document d = searcher.doc(docId);
                        System.out.println((j + 1) + ". " + d.get("title") + "\n" + d.get("artist")+"\n"+d.get("album"));
                    }



                } catch (ParseException e) {
                    e.printStackTrace();
                }

                endTime = System.currentTimeMillis();
                System.out.println("Time: " + (endTime - startTime));



            endTime = System.currentTimeMillis();
            System.out.println("Time: " + (endTime - startTime));

            } else {
                System.out.println("Wrong Choice.");
            }

            System.out.println("\nWant to continue?(y/n): ");
            Scanner scanner = new Scanner(System.in);
            choice =  (char) br.read();

        } while (choice == 'y');

    }


//    private static void addDoc(IndexWriter w, String title, String artist, String album, String lyrics) throws IOException {
//        Document doc = new Document();
//
//        doc.add(new StringField("title", title, Field.Store.YES));
//        doc.add(new StringField("artist", artist, Field.Store.YES));
//        doc.add(new StringField("album", album, Field.Store.YES));
//        doc.add(new StringField("lyrics", lyrics, Field.Store.YES));
//        w.addDocument(doc);
//    }


}


