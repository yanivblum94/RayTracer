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

    /* function to calculate a vector given 2 points
    Notice: the vector direction's is calculated towards Point b
     */
    public static Vector VectorByTwoPoints(Point a, Point b){
        return new Vector(b.X-a.X, b.Y-a.Y, b.Z-a.Z);
    }

    public static Vector VectorAddition(Vector v, Vector u){
        return new Vector(v.X+u.X,v.Y+u.Y,v.Z+u.Z);
    }

    public static Vector VectorSubtraction(Vector v, Vector u){
        return new Vector(v.X-u.X,v.Y-u.Y,v.Z-u.Z);
    }

    public static double NormalVal(Vector v){
        double temp = DotProduct(v,v);
        return Math.sqrt(temp);
    }

    public static Vector NormalVector(Vector v){
        double n = NormalVal(v);
        if(n==0){return  new Vector(0,0,0);}
        return new Vector((v.X)/n, (v.Y)/n, (v.Z)/n);
    }

    public void Normalize(){
        Vector temp = new Vector(this.X, this.Y, this.Z);
        double n = NormalVal(temp);
        if(n==0){
            this.X=0; this.Y=0; this.Z=0;
            return;
        }
        this.X = this.X/n;
        this.Y = this.Y/n;
        this.Z = this.Z/n;
    }

    public static double Distance(Vector u, Vector v){//return distance between 2 points in the space
        double res = Math.pow((u.X - v.X) ,2);
        res += Math.pow((u.Y - v.Y) ,2);
        res += Math.pow((u.Z - v.Z) ,2);
        return Math.sqrt(res);
    }
}
