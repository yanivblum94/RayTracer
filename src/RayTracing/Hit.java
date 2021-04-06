package RayTracing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Hit {
    public Vector HitPoint;
    public Shapes Shape;
    public int Index;

    public Hit(Vector hitPoint, Shapes shape, int index) {
        HitPoint = hitPoint;
        Shape = shape;
        Index = index;
    }

    public static List<Hit> FindHits(Ray ray, Scene scene){
        List<Hit> res = new ArrayList<Hit>();
        if(scene.Spheres.size() > 0){
            res.addAll(FindSphereHits(ray,scene));
        }
        if(scene.Planes.size() > 0){
            res.addAll(FindPlaneHits(ray,scene));
        }
        if(scene.Boxes.size() > 0){
            res.addAll(FindBoxHits(ray,scene));
        }
        return res;
    }

    public static List<Hit> FindSphereHits(Ray ray, Scene scene) {
        List<Hit> res = new ArrayList<>();
        for (Sphere sphere:scene.Spheres) {
            Vector l = Vector.VectorSubtraction(sphere.Center, ray.Origin);
            double tca = Vector.DotProduct(l,ray.Direction);
            if(tca < 0) {continue;}
            double dSquare = Vector.DotProduct(l,l) - Math.pow(tca,tca);
            if(dSquare > Math.pow(sphere.Radius, 2)){continue;}
            double thc = Math.sqrt((Math.pow(sphere.Radius, 2)-dSquare));
            double t = tca - thc;
            Vector hitPoint = Vector.VectorAddition(ray.Origin,
                    Vector.ScalarMultiply(ray.Direction, t));
            Hit hit = new Hit(hitPoint, Shapes.Sphere,
                    scene.Spheres.indexOf(sphere));
            res.add(hit);
            if(thc == 0){continue;}//in case the hit is tangent
            t = tca + thc;
            hitPoint = Vector.VectorAddition(ray.Origin,
                    Vector.ScalarMultiply(ray.Direction, t));
            hit =  new Hit(hitPoint, Shapes.Sphere,
                    scene.Spheres.indexOf(sphere));
            res.add(hit);
        }
        return res;
    }

    public static List<Hit> FindPlaneHits(Ray ray, Scene scene) {
        List<Hit> res = new ArrayList<>();
        for(Plane pln: scene.Planes){
            double a = Vector.DotProduct(pln.Normal, ray.Direction);
            if(Math.abs(a)< 0.001){continue;}//the ray is unified or parallel to the plane
            double b = Vector.DotProduct(pln.Normal, ray.Origin);
            double t = -1 * (pln.Offset + b) / a;
            if(t<0){continue;}//the plane is on the other direction
            Vector hitPoint = ray.tPointOnRay(t);
            Hit hit = new Hit(hitPoint, Shapes.Plane, scene.Planes.indexOf(pln));
            res.add(hit);
        }
        return res;
    }

    public static List<Hit> FindBoxHits(Ray ray, Scene scene) {
        List<Hit> res = new ArrayList<>();
        return res;
    }

    public static Hit FindClosest(List<Hit> hits, Vector origin){
        double dist = Double.MAX_VALUE;
        Hit res = null;
        for (Hit hit : hits) {
            if (hit.HitPoint == null)
                continue;
            double d = Vector.Distance(hit.HitPoint, origin);
            if (d < dist) {
                dist = d;
                res = hit;
            }
        }
        return res;
    }
}
