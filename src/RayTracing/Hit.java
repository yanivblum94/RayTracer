package RayTracing;

import java.util.*;

public class Hit {
    public Vector HitPoint;
    public Shapes Shape;
    public int Index;
    public Vector Normal;
    public Shape Surface;

    public Hit(Vector hitPoint, Shapes shape, int index) {
        HitPoint = hitPoint;
        Shape = shape;
        Index = index;
    }

    public Hit(Vector hitPoint, Shapes shape, int index, Vector normal) {
        HitPoint = hitPoint;
        Shape = shape;
        Index = index;
        Normal = normal;
    }

    public Hit(){}

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
                    hit.Normal = Vector.VectorSubtraction(scene.Spheres.get(hit.Index).Center, hitPoint);
                    hit.Normal = Vector.ScalarMultiply(hit.Normal, -1);
                    hit.Normal.Normalize();
                    hit.Surface = sphere;
                    res.add(hit);
                    continue;
                }
                else{
                    if(roots[0] > 0){
                        Vector hitPoint = ray.tPointOnRay(roots[0]);
                        Hit hit = new Hit(hitPoint, Shapes.Sphere, scene.Spheres.indexOf(sphere));
                        hit.Normal = Vector.VectorSubtraction(scene.Spheres.get(hit.Index).Center, hitPoint);
                        hit.Normal = Vector.ScalarMultiply(hit.Normal, -1);
                        hit.Normal.Normalize();
                        hit.Surface = sphere;
                        res.add(hit);
                        continue;
                    }
                    else if(roots.length >1 && roots[1] > 0){
                        Vector hitPoint = ray.tPointOnRay(roots[1]);
                        Hit hit = new Hit(hitPoint, Shapes.Sphere, scene.Spheres.indexOf(sphere));
                        hit.Normal = Vector.VectorSubtraction(scene.Spheres.get(hit.Index).Center, hitPoint);
                        hit.Normal = Vector.ScalarMultiply(hit.Normal, -1);
                        hit.Normal.Normalize();
                        hit.Surface = sphere;
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
            hit.Normal = pln.Normal;
            hit.Surface = pln;
            res.add(hit);
        }
        return res;
    }

    //using the slabs method from the instructions
    public static List<Hit> FindBoxHits(Ray ray, Scene scene) {
        List<Hit> res = new ArrayList<>();
        for(Box box : scene.Boxes){
            List<Plane> planes = box.GenerateBoxPlanes();
            AbstractMap<Hit, Plane> hits = FindPlaneHitsForBox(ray, planes, scene.Materials.indexOf(box.BoxMaterial)); //finds all the hits with the box planes
            Hit hit = box.BoxHit(hits, ray);//checks if the ray hits the box, and return the closest hit if so
            hit.Surface = box;
            if(hit != null){
                res.add(hit);
            }
        }
        return res;
    }

    public static AbstractMap<Hit, Plane> FindPlaneHitsForBox(Ray ray, List<Plane> planes, int index) {
        AbstractMap<Hit, Plane> res = new HashMap<>();
        for(Plane pln: planes){
            double a = Vector.DotProduct(pln.Normal, ray.Direction);
            if(Math.abs(a)< 0.001){continue;}//the ray is unified or parallel to the plane
            Vector temp = Vector.ScalarMultiply(pln.Normal, pln.Offset);
            temp = Vector.VectorSubtraction(temp, ray.Origin);
            double t = (Vector.DotProduct(pln.Normal, temp)) / a;
            if(t<0){continue;}
            Vector hitPoint = ray.tPointOnRay(t);
            Hit hit = new Hit(hitPoint, Shapes.Plane, index);
            res.put(hit, pln);
        }
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

    public Material GetMaterial(Scene scene){
        Shapes s = this.Shape;
        switch (s) {
            case Sphere:
                return scene.Spheres.get(this.Index).SphereMaterial;
            case Plane:
                return  scene.Planes.get(this.Index).PlaneMaterial;
            default:
                return scene.Boxes.get(this.Index).BoxMaterial;
        }
    }

    public static void RemoveSameShape(List<Hit> hits, Shape s){
        for(Hit hit : hits){
            if(hit.Shape.equals(s)){
                hits.remove(hit);
                continue;
            }
        }
    }
}
