import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by yuriyarabskyy on 09/11/15.
 */
public class Outcast {

    private WordNet wordNet;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        this.wordNet = wordnet;
    }
    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        int max = 0;
        String out = "";
        for(String noun1 : nouns){
            int dist = 0;
            for(String noun2 : nouns){
                dist += wordNet.distance(noun1, noun2);
            }
            if(dist>max){
                max = dist;
                out = noun1;
            }
        }
        return out;
    }

    public static void main(String[] args) {
        String s1 = "/Volumes/Data/Projects/WordNet/src/wordnet/synsets.txt";
        String s2 = "/Volumes/Data/Projects/WordNet/src/wordnet/hypernyms.txt";
        WordNet wordnet = new WordNet(s1, s2);
        Outcast outcast = new Outcast(wordnet);
        In in = new In("/Volumes/Data/Projects/WordNet/src/wordnet/outcast11.txt");
        String[] nouns = in.readAllStrings();
        StdOut.println(outcast.outcast(nouns));
    }
}
