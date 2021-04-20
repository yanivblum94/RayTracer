package RayTracing;

import java.util.List;
import java.util.Random;

public class SoftShadow {

    public static double CalcLightIntensity(Hit hit, Scene scene, Light light) {//returns the light intensity from a specific light source in a hit point according to soft shadows method
        Random r = new Random();
        double raysCounter = 0.0;
        Vector lightDir = Vector.VectorSubtraction(light.Position, hit.HitPoint);
        lightDir.Normalize();
        Ray lightRay = new Ray(light.Position, lightDir);
        Vector [] axis = PerpPlane(light, lightRay);
        Vector u = axis[0]; //u vector of light plane
        Vector v = axis[1];; //v vector of light plane
        int rays = (int) Math.pow(scene.Settings.NumOfShadowRays, 2);//number of shadow rays totally shot
        double squareSize = light.LightRadius / (scene.Settings.NumOfShadowRays); //size of one square in the light plane
        Vector temp = Vector.VectorAddition(Vector.ScalarMultiply(u, light.LightRadius),
                    Vector.ScalarMultiply(v, light.LightRadius )); //like in the screen center, temp is (u*height + v*width)
        Vector s0 = Vector.VectorSubtraction(light.Position, Vector.ScalarMultiply(temp, 0.5)); // bottom left corner square is center - 0.5temp
        Vector du = Vector.ScalarMultiply(u, squareSize);
        Vector dv = Vector.ScalarMultiply(v, squareSize);
        for (int row = 0; row < scene.Settings.NumOfShadowRays; row++) {
            for (int col = 0; col < scene.Settings.NumOfShadowRays; col++) {
                Vector shift = Vector.VectorAddition(Vector.ScalarMultiply(du, col), Vector.ScalarMultiply(dv, row));
                Vector currentSquare = Vector.VectorAddition(s0, shift);
                Vector randomMove = GenerateRandomMoveVector(squareSize);//generates a random move vector within the square
                Vector rayOrigin = Vector.VectorAddition(currentSquare, randomMove);
                Ray ray = new Ray(rayOrigin, Vector.VectorSubtraction(hit.HitPoint, rayOrigin));//construct a ray from the square to the hit point
                ray.Direction.Normalize();
                List<Hit> hits = Hit.FindHits(ray, scene);//find all the objects the ray hits
                if(hits.size() ==0){continue;}
                Hit closest = Hit.FindClosest(hits, rayOrigin);//find the closest hit
                if(Math.abs(Vector.Distance(closest.HitPoint, hit.HitPoint)) < 0.001){ //if the closest hit to the light source is the hit point we are checking now then the ray hits
                    raysCounter = raysCounter + 1.0;
                }
                else{//bonus part taking into accounting transparent values
                    Material mat = closest.GetMaterial(scene);
                    double partialRay = 1.0; // a variable that sums which part of the ray continues
                    if(mat.Transparency == 0.0){continue;}//if the material we hit is opaque, no light passes
                    else{
                        partialRay *= mat.Transparency;
                        Vector epsilon = Vector.VectorAddition(hit.HitPoint, Vector.ScalarMultiply(ray.Direction, 0.001));
                        Ray transRay = new Ray(epsilon, ray.Direction);//construct a ray from the hit point with same direction from light
                        List<Hit> transHits = Hit.FindHits(transRay, scene);//find the next hits in same direction
                        while(transHits.size() > 0){
                            Hit closestTrans = Hit.FindClosest(transHits, transRay.Origin);//find the closest hit
                            mat = closestTrans.GetMaterial(scene);//get the hit's material
                            if(mat.Transparency == 0.0){
                                partialRay = 0.0; //no light gets to the hitting point
                                break;//if the material is opaque quit the while
                            }
                            partialRay *= mat.Transparency;
                            epsilon = Vector.VectorAddition(closestTrans.HitPoint, Vector.ScalarMultiply(ray.Direction, 0.001));
                            transRay = new Ray(epsilon, ray.Direction);//construct a ray from the hit point with same direction from light
                            transHits = Hit.FindHits(transRay, scene);//find the next hits in same direction
                        }
                        raysCounter += partialRay;//add the partial ray that comes through
                    }
                }
            }
        }
        double ratio = (raysCounter /(double) rays); //%of rays that hit the pont from the light source
        return ( (1- light.ShadowIntensity) + (light.ShadowIntensity * ratio) );//increase light intensity from each light source according to formula
    }

    //find a random shift vector inside the current square. moves at most (+-)0.5 square size in u and v directions. the random arg generates an number
    public static Vector GenerateRandomMoveVector(double size){
        Random r = new Random();
        double moveU = r.nextDouble()*0.5; //generates a number between 0-0.5
        if(r.nextBoolean()){moveU = moveU*-1;} // randomizes + or -
        moveU = moveU * size;
        double moveV = r.nextDouble()*0.5; //generates a number between 0-0.5
        if(r.nextBoolean()){moveV = moveV*-1;} // randomizes + or -
        moveV = moveV * size;
        return new Vector(moveU, moveV, 0);
    }

    //find a perpendicular plane from the light and the ray
    public static Vector[] PerpPlane(Light light, Ray ray){
        double d = Vector.DotProduct(light.Position, ray.Direction);
        Vector U = new Vector(1, 0, (-ray.Direction.X + d) / ray.Direction.Z);
        Vector V = new Vector(1, 0, (-ray.Direction.Y + d) / ray.Direction.Z);
        U.Normalize();
        V.Normalize();
        return new Vector[] {U, V };
    }
}

