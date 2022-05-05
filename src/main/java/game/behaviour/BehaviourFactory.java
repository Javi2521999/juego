package game.behaviour;


public class BehaviourFactory {
    public IBehaviour getBehaviour(int mode) {
        IBehaviour behaviour = null;
        if (mode == 0){
            behaviour = new RandomBehaviour(); 
        }else if(mode == 1) {
            behaviour = new NextBehaviour();
        }else if(mode == 2) {
            behaviour = new ClosestBehaviour();
        }else if(mode == 3){
            behaviour = new FlyBehaviour();
        }else if(mode == 4){
            behaviour = new ManualBehaviour();
        }
        return behaviour;
    }
    
}
