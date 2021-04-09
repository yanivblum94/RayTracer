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
            Vector l = Vector.VectorSubtraction(ray.Origin, sphere.Center);
            double a = Vector.DotProduct(ray.Direction, ray.Direction);
            double b = 2 * Vector.DotProduct(l, ray.Direction);
            double c = Vector.DotProduct(l,l) - Math.pow(sphere.Radius , 2);
            double disc = Math.pow(b,2) - 4 * a * c;
            if (disc < 0) {continue;}
            else{
                double[] roots = FindRoots(a,b,disc);
                if(roots.length == 1 && roots[0] > 0){
                    Vector hitPoint = ray.tPointOnRay(roots[0]);
                    Hit hit = new Hit(hitPoint, Shapes.Sphere, scene.Spheres.indexOf(sphere));
                    res.add(hit);
                    continue;
                }
                else{
                    if(roots[0] > 0){
                        Vector hitPoint = ray.tPointOnRay(roots[0]);
                        Hit hit = new Hit(hitPoint, Shapes.Sphere, scene.Spheres.indexOf(sphere));
                        res.add(hit);
                        continue;
                    }
                    else if(roots[1] > 0){
                        Vector hitPoint = ray.tPointOnRay(roots[1]);
                        Hit hit = new Hit(hitPoint, Shapes.Sphere, scene.Spheres.indexOf(sphere));
                        res.add(hit);
                        continue;
                    }
                }
            }
        }
        return res;
    }

    private static double[] FindRoots(double a, double b, double disc){
        double denominator = 2*a;
        if (disc == 0)
            return new double[]{-b/denominator};
        else
            return SortArray(new double[]{
                    (Math.sqrt(disc) - b)/ denominator,
                    -(b+Math.sqrt(disc))/ denominator
            });
    }

    private static double[] SortArray(double[] arr){
        return (arr[0] < arr[1]) ? arr : new double[]{arr[1], arr[0]};
    }

    public static List<Hit> FindPlaneHits(Ray ray, Scene scene) {
        List<Hit> res = new ArrayList<>();
        for(Plane pln: scene.Planes){
            double a = Vector.DotProduct(pln.Normal, ray.Direction);
            if(Math.abs(a)< 0.001){continue;}//the ray is unified or parallel to the plane
            Vector temp = Vector.ScalarMultiply(pln.Normal, pln.Offset);
            temp = Vector.VectorSubtraction(temp, ray.Origin);
            double t = (Vector.DotProduct(pln.Normal, temp)) / a;
            if(t<0){continue;}
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
