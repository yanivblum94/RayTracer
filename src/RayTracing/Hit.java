package RayTracing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Hit {
    public Vector HitPoint;
    public Shapes shape;
    public int Index;

    public static List<Hit> FindHits(Ray ray, Scene scene){
        List<Hit> res = new ArrayList<Hit>();
        if(scene.Spheres.size() > 0{
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

    public List<Hit> FindSphereHits(Ray ray, Scene scene) {
        /*
        TODO
         */
    }

    public List<Hit> FindPlaneHits(Ray ray, Scene scene) {
        /*
        TODO
         */
    }

    public List<Hit> FindBoxHits(Ray ray, Scene scene) {
        /*
        TODO
         */
    }

    public static Hit FindClosest(List<Hit> hits, Scene scene){
        double dist = Double.MAX_VALUE;
        Hit res = null;
        for (Hit hit : hits) {
            if (hit.HitPoint == null)
                continue;
            double d = Vector.Distance(hit.HitPoint, scene.Camera.Position);
            if (d < dist) {
                dist = d;
                res = hit;
            }
        }
        return res;
    }
}
