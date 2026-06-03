abstract class SpaceVessel {
    short shipId;
    boolean status;
    char shipClass;

    SpaceVessel(short shipId, boolean status, char shipClass) {
        this.shipId = shipId;
        this.status = status;
        this.shipClass = shipClass;
    }
}

class MiningShip extends SpaceVessel {

    float[][] oreWeight;

    MiningShip(short shipId, boolean status, char shipClass,
               float[][] oreWeight) {
        super(shipId, status, shipClass);
        this.oreWeight = oreWeight;
    }

    public float calculateTotalOreWeight() {
        float total = 0;

        for(int i=0;i<oreWeight.length;i++) {
            for(int j=0;j<oreWeight[i].length;j++) {
                total += oreWeight[i][j];
            }
        }

        return total;
    }

    public float findHeaviestContainer() {
        float max = oreWeight[0][0];

        for(int i=0;i<oreWeight.length;i++) {
            for(int j=0;j<oreWeight[i].length;j++) {
                if(oreWeight[i][j] > max)
                    max = oreWeight[i][j];
            }
        }

        return max;
    }
}

public class Prob1Day1
 {
    public static void main(String[] args) {

        float[][] cargo = {
                {1200.5f,4500.5f},
                {6000.0f,3000.0f}
        };

        MiningShip ship =new MiningShip((short)101,true,'A',cargo);

        System.out.println("Total Weight = "+ ship.calculateTotalOreWeight());

        System.out.println("Heaviest Container = "+ ship.findHeaviestContainer());
    }
    
}