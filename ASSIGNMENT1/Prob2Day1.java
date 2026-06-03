class PowerManager {

    byte sectorStates = 0;

    public void turnOnSector(int sectorIndex) {
        sectorStates = (byte)(sectorStates |(1 << sectorIndex));
    }

    public void turnOffSector(int sectorIndex) {
        sectorStates = (byte)(sectorStates &~(1 << sectorIndex));
    }

    public boolean isSectorOn(int sectorIndex) {
        return (sectorStates &(1 << sectorIndex)) != 0;
    }
}

public class Prob2Day1{
    public static void main(String[] args) {

        PowerManager p = new PowerManager();

        p.turnOnSector(1);
        p.turnOnSector(3);

        System.out.println(p.isSectorOn(3));

        p.turnOffSector(3);

        System.out.println(p.isSectorOn(3));
    }
}