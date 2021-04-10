package RayTracing;

import java.util.Random;

public class Plane {
    public Vector Normal;
    public double Offset;
    public Material PlaneMaterial;

    public Plane(Vector normal, double offset) {
        Normal = normal;
        Offset = offset;
    }

    public Plane(){}

    public Plane(Vector normal, double offset, Material planeMaterial) {
        Normal = normal;
        Offset = offset;
        PlaneMaterial = planeMaterial;
    }

    /*
            Calculate the offset for the soft shadow light plane
             */
    public static double CalcOffset(Vector normal, Vector position){
        return (normal.X *(- position.X)) + (normal.Y *(- position.Y)) + (normal.Z *(- position.Z));
    }

    /*
    Finds a point on the plane other than the point P
    Plane: aX + bY + cZ = -d
    if we are not parallel to an axis plane we can random values for x and y and calculate z.
    Make sure the point is not the P that we have
     */
    public Vector FindPoint(Vector P){
        double x,y,z;
        Random r = new Random();
        if(Math.abs(Normal.X) < 0.0001){
            y = r.nextDouble()+0.0001;
            z = (-Offset + Normal.Y*y) / Normal.Z;
            return new Vector(0,y,z);
        }
        else if(Math.abs(Normal.Y) < 0.0001){
            x = r.nextDouble() + 0.0001;
            z = (-Offset + Normal.X*x) / Normal.Z;
            return new Vector(x,0,z);
        }
        else if(Math.abs(Normal.Z) < 0.0001){
            x = r.nextDouble() + 0.0001;
            y = (-Offset + Normal.X*x) / Normal.Y;
            return new Vector(x,y,0);
        }
        do {
            x = r.nextDouble() + 0.0001;
            y= r.nextDouble() + 0.0001;
        }
        while(x==P.X && y==P.Y);
        z = (-Offset + Normal.Y*y + Normal.X*x) / Normal.Z;
        return new Vector(x, y, z);
    }
}
