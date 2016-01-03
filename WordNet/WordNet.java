/**
 * Created by yuriyarabskyy on 08/11/15.
 */

import edu.princeton.cs.algs4.*;

public class WordNet {

    final private Digraph digraph;
    final private RedBlackBST<String,Bag<Integer>> dictionary;
    final private SeparateChainingHashST<Integer, String> reverse;
    final private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        In nounFileReader = new In(synsets);
        In relationshipFileReader = new In(hypernyms);
        int size = 0;   //size of the dictionary
        int count = 0;  //number of roots, if not one then throw exception
        dictionary = new RedBlackBST();
        reverse = new SeparateChainingHashST<>();
        while(!nounFileReader.isEmpty()){
            String line = nounFileReader.readLine();
            String[] splittedLine = line.split(",");
            for(int i = 0;i<3; i++) if(splittedLine[i]==null||splittedLine[i]=="")
                throw new java.lang.NullPointerException();
            int key = Integer.parseInt(splittedLine[0]);
            String[] synonyms = splittedLine[1].split(" ");
            reverse.put(key, splittedLine[1]);
            for(String synonym : synonyms) {
                if(dictionary.contains(synonym)){
                    dictionary.get(synonym).add(key);
                }
                else {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(key);
                    dictionary.put(synonym,bag);
                }
            }
            size++;
        }
        digraph = new Digraph(size);
        while(!relationshipFileReader.isEmpty()){
            String line = relationshipFileReader.readLine();
            String[] splittedLine = line.split(",");
            for(int i = 0;i<splittedLine.length; i++) if(splittedLine[i]==null||splittedLine[i]=="")
                throw new java.lang.NullPointerException();
            int vertex = Integer.parseInt(splittedLine[0]);
            for(int i = 1; i<splittedLine.length; i++){
                digraph.addEdge(vertex, Integer.parseInt(splittedLine[i]));
            }
        }
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if(directedCycle.hasCycle()) throw new java.lang.IllegalArgumentException();
        for(int i = 0; i<size; i++) if(digraph.outdegree(i)==0) count++;
        if(count!=1) throw new java.lang.IllegalArgumentException();
        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return dictionary.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        return dictionary.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        check(nounA,nounB);
        Iterable<Integer> v = dictionary.get(nounA);
        Iterable<Integer> w = dictionary.get(nounB);
        int length = sap.length(v, w);
        return length;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        check(nounA,nounB);
        Iterable<Integer> v = dictionary.get(nounA);
        Iterable<Integer> w = dictionary.get(nounB);
        return reverse.get(sap.ancestor(v,w));
    }

    private void check(String nounA, String nounB){
        if(!isNoun(nounA)||!isNoun(nounB)) throw new java.lang.IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args){
        WordNet wordNet = new WordNet("/Volumes/Data/Projects/WordNet/src/wordnet/synsets.txt","/Volumes/Data/Projects/WordNet/src/wordnet/hypernyms.txt");
        System.out.println(wordNet.sap("boy", "girl"));
    }



}
