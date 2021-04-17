package RayTracing;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Box extends Shape{
    public Vector Center;
    public double Scale;
    public Material BoxMaterial;

    //generates 6 planes a
    public List<Plane> GenerateBoxPlanes(){
        List<Plane> res = new ArrayList<>();
        //normal is x
        Vector normal = new Vector(1,0,0);//box is axis aligned
        double offset = Center.X + 0.5*Scale;
        Plane pln = new Plane(normal, offset, BoxMaterial);//1st YZ aligned plane
        res.add(pln);
        offset = Center.X - 0.5*Scale;
        pln = new Plane(normal, offset, BoxMaterial);//2nd YZ aligned plane
        res.add(pln);
        //normal is y
        normal = new Vector(0,1,0);
        offset = Center.Y + 0.5*Scale;
        pln = new Plane(normal, offset, BoxMaterial);//1st XZ aligned plane
        res.add(pln);
        offset = Center.Y - 0.5*Scale;
        pln = new Plane(normal, offset, BoxMaterial);//2nd XZ aligned plane
        res.add(pln);
        //normal is z
        normal = new Vector(0,0,1);
        offset = Center.Z + 0.5*Scale;
        pln = new Plane(normal, offset, BoxMaterial);//1st XYZ aligned plane
        res.add(pln);
        offset = Center.Z - 0.5*Scale;
        pln = new Plane(normal, offset, BoxMaterial);//2nd XY aligned plane
        res.add(pln);

        return res;
    }

    //gets a map of the hits and the respective planes, and the ray. returns true if the first 2 hits are not parallel planes
    public Hit BoxHit(AbstractMap<Hit, Plane> hits, Ray ray){
        double dist = Double.MAX_VALUE;
        Plane first = new Plane();
        Plane second = new Plane();
        Hit firstHit = new Hit();
        for(Hit hit :
        hits.keySet()){
            double temp = Vector.Distance(hit.HitPoint, ray.Origin);
            if(temp<dist){//find the closest hit
                dist = temp;
                firstHit = hit;
            }
        }
        first = hits.get(firstHit); //save the plane of first hit
        hits.remove(firstHit);
        dist = Double.MAX_VALUE;
        Hit secondHit = new Hit();
        for(Hit hit :
                hits.keySet()){
            double temp = Vector.Distance(hit.HitPoint, ray.Origin);
            if(temp<dist){//find the 2nd closest hit
                dist = temp;
                secondHit = hit;
            }
        }
        second = hits.get(secondHit);//save the plane of second it

        if(!first.Normal.equals(second.Normal)){//if the first 2 planes have different normals, the are not parallel and there is a box hit
            firstHit.Normal = first.Normal;
            return firstHit;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return Double.compare(box.Scale, Scale) == 0 && Center.equals(box.Center) && BoxMaterial.equals(box.BoxMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Center, Scale, BoxMaterial);
    }
}
