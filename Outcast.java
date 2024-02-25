public class Outcast {

    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int greatestDistance = 0;
        String result = null;

        for (String noun : nouns) {
            int distance = 0;

            for (String secondNoun : nouns) {
                distance += wordNet.distance(noun, secondNoun);
            }

            if (distance > greatestDistance) {
                greatestDistance = distance;
                result = noun;
            }
        }

        return result;
    }
 }