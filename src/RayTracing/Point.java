package RayTracing;

public class Point {
    public float X;
    public float Y;
    public float Z;

    public Point(float x, float y, float z){
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public static double Distance(Point x, Point y){//return distance between 2 points in the space
        double res = Math.pow((x.X - y.X) ,2);
        res += Math.pow((x.Y - y.Y) ,2);
        res += Math.pow((x.Z - y.Z) ,2);
        return Math.sqrt(res);
    }
}
