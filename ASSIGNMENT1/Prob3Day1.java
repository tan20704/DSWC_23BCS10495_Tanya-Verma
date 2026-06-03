class DNASequencer {

    StringBuilder dna;

    DNASequencer(int capacity) {
        dna = new StringBuilder(capacity);
    }

    public void ingestSequence(char[] sensorData) {

        for(char ch : sensorData) {
            dna.append(ch);
        }
    }

    public void mutateDNA(String target,String replacement) {

        int index = dna.indexOf(target);

        if(index != -1) {
            dna.replace(index,index + target.length(),replacement);
        }
    }

    public void display() {
        System.out.println(dna);
    }
}

public class Prob3Day1{
    public static void main(String[] args) {

        DNASequencer d =new DNASequencer(100000);

        char[] data ={'A','C','T','G','A'};

        d.ingestSequence(data);

        d.display();

        d.mutateDNA("CT","GG");

        d.display();
    }
}