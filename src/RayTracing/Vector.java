package RayTracing;

public class Vector {
    public double X;
    public double Y;
    public double Z;

    public Vector(double x, double y, double z){
        this.X = x ;
        this.Y = y ;
        this.Z = z ;
    }

    public static double DotProduct(Vector v, Vector u){
        double sum = 0;
        sum += (v.X*u.X) + (v.Y*u.Y) + (v.Z*u.Z) ;
        return sum;
    }

    public static Vector CrossProduct(Vector v, Vector u){
        double x = v.Y*u.Z - v.Z*u.Y ;
        double y = v.Z*u.X - v.X*u.Z ;
        double z = v.X*u.Y - v.Y*u.X ;
        return new Vector(x,y,z);
    }

    public static Vector ScalarMultiply(Vector v, double c){
        return new Vector(v.X * c,v.Y * c, v.Z * c);
    }

    public static void ScalarMultiplyVoid(Vector v, double c){
        v.X = c * v.X;
        v.Y = c * v.Y;
        v.Z = c * v.Z;
    }
}
