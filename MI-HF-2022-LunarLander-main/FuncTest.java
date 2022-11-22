import java.util.Scanner;

public class FuncTest {
    static final double ROOT_VALUE = 0.5f;

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

    public static void main(String[] args){
        int j = 0;
        for(int i = -200; i < 201; i+=10) {
            int h = linearDoubleQuantize(i, 200, 11);
            System.out.println(i + " Index: " + h);
            if(h == 10)
                j++;
        }
        System.out.println(j);


    }
}
