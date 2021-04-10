package RayTracing;

import java.util.List;
import java.util.Random;

public class SoftShadow {

    public static double CalcLightIntensity(Hit hit, Scene scene) {
        Random r = new Random();
        int raysCounter = 0;
        double res = 0;
        for (Light light : scene.Lights) {
            Vector dir = Vector.VectorSubtraction(light.Position, hit.HitPoint);// normal to light plane is light - hit point
            double offset = Plane.CalcOffset(dir, light.Position);// find the offset of the light plane
            dir.Normalize();
            Plane lightPlane = new Plane(dir, offset);
            Vector otherPoint = lightPlane.FindPoint(light.Position);//find another point on the plane in order to find unit vectors that create the plane
            Vector u = Vector.VectorSubtraction(otherPoint, light.Position); //u vector of light plane
            u.Normalize();
            Vector v = Vector.CrossProduct(dir, u); //v vector of light plane
            v.Normalize();
            int rays = (int) Math.pow(scene.Settings.NumOfShadowRays, 2);//number of shadow rays totally shot
            double squareSize = light.LightRadius / (scene.Settings.NumOfShadowRays); //size of one square in the light plane
            Vector temp = Vector.VectorAddition(Vector.ScalarMultiply(u, (scene.Settings.NumOfShadowRays * 0.5) * squareSize),
                    Vector.ScalarMultiply(v, (scene.Settings.NumOfShadowRays * 0.5) * squareSize)); //like in the screen center, temp is 0.5*(u*height + v*width)
            Vector s0 = Vector.VectorSubtraction(light.Position, temp); // bottom left corner square is center - temp
            Vector du = Vector.ScalarMultiply(u, squareSize);
            Vector dv = Vector.ScalarMultiply(v, squareSize);
            raysCounter = 0; //number of rays shot from the light that hit the hitting point first
            for (int row = 0; row < scene.Settings.NumOfShadowRays; row++) {
                for (int col = 0; col < scene.Settings.NumOfShadowRays; col++) {
                    Vector shift = Vector.VectorAddition(Vector.ScalarMultiply(du, col), Vector.ScalarMultiply(dv, row));
                    Vector currentSquare = Vector.VectorAddition(s0, shift);
                    Vector randomMove = GenerateRandomMoveVector(du, dv, squareSize);
                    Vector rayOrigin = Vector.VectorAddition(currentSquare, randomMove);
                    Ray ray = new Ray(rayOrigin, Vector.VectorSubtraction(rayOrigin, hit.HitPoint));//construct a ray from the square to the hit point
                    ray.Direction.Normalize();
                    List<Hit> hits = Hit.FindHits(ray, scene);//find all the objects the ray hits
                    if(hits.size() ==0){continue;}
                    Hit closest = Hit.FindClosest(hits, rayOrigin);//find the closest hit
                    if(Math.abs(Vector.Distance(closest.HitPoint, hit.HitPoint)) < 0.001){ //if the closest hit to the light source is the hit point we are checking now then the ray hits
                        raysCounter++;
                    }
                }
            }
            double ratio = ((double)raysCounter /(double) rays); //%of rays that hit the pont from the light source
            res += (1- light.ShadowIntensity) + (light.ShadowIntensity * ratio);//increase light intensity from each light source according to formula
        }
        return res;
    }

    //find a random shift vector inside the current square. moves at most (+-)0.5 square size in u and v directions. the random arg generates an number
    public static Vector GenerateRandomMoveVector(Vector u, Vector v, double size){
        Random r = new Random();
        double moveU = r.nextDouble()*0.5; //generates a number between 0-0.5
        if(r.nextBoolean()){moveU = moveU*-1;} // randomizes + or -
        moveU = moveU * size;
        double moveV = r.nextDouble()*0.5; //generates a number between 0-0.5
        if(r.nextBoolean()){moveV = moveV*-1;} // randomizes + or -
        moveV = moveV * size;
        return Vector.VectorAddition(Vector.ScalarMultiply(u,moveU), Vector.ScalarMultiply(v, moveV));
    }
}

