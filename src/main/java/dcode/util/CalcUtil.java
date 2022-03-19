package dcode.util;

public class CalcUtil {

    //절삭
    public static int roundDown(int origin, int round) {

        int down = origin % round;
        return origin - down;
    }
}
