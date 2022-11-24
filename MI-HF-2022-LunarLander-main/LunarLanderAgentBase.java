import java.util.Arrays;

public class LunarLanderAgentBase {
    // The resolution of the observation space
    // The four variables of the observation space, from left to right:
    //   0: X component of the vector pointing to the middle of the platform from the lander
    //   1: Y component of the vector pointing to the middle of the platform from the lander
    //   2: X component of the velocity vector of the lander
    //   3: Y component of the velocity vector of the lander


    //*** SETUP VALUES ***
    static final int[] OBSERVATION_SPACE_RESOLUTION = {
            81, // MUST BE AN ODD NUMBER!!!
            80,
            81, // MUST BE AN ODD NUMBER!!!
            81  // MUST BE AN ODD NUMBER!!!
    };
    static final int ROOT_VALUE = 2;



    final double[][] observationSpace;
    double[][][][][] qTable;
    final int[] envActionSpace;
    private final int nIterations;

    double epsilon = 1.0f;
    int iteration = 0;
    boolean test = false;

    // your variables here
    // ...
    double[][][][][] bestTable;
    double bestReward = -200;
    double lastReward = -200;

    double alpha = 0.1; //Learning rate
    double gamma = 0.99; //Discount rate
    int epsilon_step = 100;
    double epsilon_decay = 0.9999f;
    int save_interval = 1000;
    double epsilon_min = 0.40f;
    double epsilon_max = 1.0f;

    int epoch = 0;

    public LunarLanderAgentBase(double[][] observationSpace, int[] actionSpace, int nIterations) {
        this.observationSpace = observationSpace;
        this.qTable =
                new double[OBSERVATION_SPACE_RESOLUTION[0]]
                        [OBSERVATION_SPACE_RESOLUTION[1]]
                        [OBSERVATION_SPACE_RESOLUTION[2]]
                        [OBSERVATION_SPACE_RESOLUTION[3]]
                        [actionSpace.length];
        this.envActionSpace = actionSpace;
        this.nIterations = nIterations;
    }

    public static int rootQuantize(double state, double maxValue, int maxIndex) {
        if(maxIndex % 2 == 0)
        {
            try {
                throw new Exception("maxIndex is not an odd number!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(state == maxValue)
        {
            return maxIndex-1;
        }
        if(state < 0)
        {
            return (int)Math.round((maxIndex -1) / 2 - (Math.pow(-state, ROOT_VALUE) * ((maxIndex-1) / 2 + 0.50) / Math.pow(maxValue, ROOT_VALUE)));
        }
        return (int)Math.round(Math.pow(state, ROOT_VALUE) * ((maxIndex-1) / 2 + 0.50) / (Math.pow(maxValue, ROOT_VALUE)) + (maxIndex-1) / 2);
    }

    public static int linearSingleQuantize(double state, double maxValue, int maxIndex){
        if(state == maxValue)
        {
            return maxIndex-1;
        }
        return (int)(state * maxIndex / maxValue);
    }
    public static int linearDoubleQuantize(double state, double maxValue, int maxIndex){
        if(maxIndex % 2 == 0)
        {
            try {
                throw new Exception("maxIndex is not an odd number!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(state == maxValue)
        {
            return maxIndex-1;
        }
        if(state < 0)
        {
            return (int)Math.round(((maxIndex - 1) / 2) - (-state * ((maxIndex - 1) / 2 + 0.50) / maxValue));
        }
        return (int)Math.round((state * ((maxIndex - 1) / 2 + 0.50) / maxValue) + ((maxIndex - 1) / 2) );
    }

    public static int[] quantizeState(double[][] observationSpace, double[] state) {
        int[] index = new int[observationSpace.length];

        index[0] = rootQuantize(state[0], observationSpace[0][1], OBSERVATION_SPACE_RESOLUTION[0]);
        index[1] = linearSingleQuantize(state[1], observationSpace[1][1], OBSERVATION_SPACE_RESOLUTION[1]);
        index[2] = linearDoubleQuantize(state[2], observationSpace[2][1], OBSERVATION_SPACE_RESOLUTION[2]);
        index[3] = linearDoubleQuantize(state[3], observationSpace[3][1], OBSERVATION_SPACE_RESOLUTION[3]);

        return index;
    }


    //exploration_rate = min_exploration_rate + \
    //    (max_exploration_rate - min_exploration_rate) * np.exp(-exploration_decay_rate*episode)
    public void epochEnd(double epochRewardSum) {
        epoch++;
        //epsilon = epsilon_min + (epsilon_max - epsilon_min) * Math.exp(-epsilon_decay * epoch);
        if(epsilon > epsilon_min)
            epsilon*=epsilon_decay;
        if(epoch < 10000 || epoch > 26000)
            System.out.println("Current epoch: " + epoch + " Value: " + epochRewardSum + " Epsilon: " + epsilon);
    }

    public static int argmax(double[] array) {
        double max = array[0];
        int re = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                re = i;
            }
        }
        return re;
    }

    public void learn(double[] oldState, int action, double[] newState, double reward) {

        //iteration++;

        int[] oldQuantized = quantizeState(observationSpace, oldState);
        int[] newQuantized = quantizeState(observationSpace, newState);
        int newBestAction = argmax(qTable[newQuantized[0]][newQuantized[1]][newQuantized[2]][newQuantized[3]]);

        //Bellman equation
        qTable[oldQuantized[0]][oldQuantized[1]][oldQuantized[2]][oldQuantized[3]][action] =
                        (1 - alpha) * qTable[oldQuantized[0]][oldQuantized[1]][oldQuantized[2]][oldQuantized[3]][action] +
                        alpha * reward +
                        alpha * gamma * qTable[newQuantized[0]][newQuantized[1]][newQuantized[2]][newQuantized[3]][newBestAction];
    }

    public void trainEnd() {
        // ... TODO
        //qTable = null; // TODO
        test = true;
    }
}
