package RayTracing;

public class Vector {
    public float X;
    public float Y;
    public float Z;

    public Vector(float x, float y, float z){
        this.X = x ;
        this.Y = y ;
        this.Z = z ;
    }

    public static float DotProduct(Vector v, Vector u){
        float sum = 0;
        sum += (v.X*u.X) + (v.Y*u.Y) + (v.Z*u.Z) ;
        return sum;
    }

    public static Vector CrossProduct(Vector v, Vector u){
        float x = v.Y*u.Z - v.Z*u.Y ;
        float y = v.Z*u.X - v.X*u.Z ;
        float z = v.X*u.Y - v.Y*u.X ;
        return new Vector(x,y,z);
    }

    public static Vector ScalarMultiply(Vector v, float c){
        return new Vector(v.X * c,v.Y * c, v.Z * c);
    }

    public static void ScalarMultiplyVoid(Vector v, float c){
        v.X = c * v.X;
        v.Y = c * v.Y;
        v.Z = c * v.Z;
    }
}
