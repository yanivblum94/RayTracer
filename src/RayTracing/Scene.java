package RayTracing;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    public Camera Camera;
    public GeneralSettings Settings;
    public List<Material>  Materials;
    public List<Sphere> Spheres;
    public List<Plane> Planes ;
    public List<Box> Boxes;
    public List<Light> Lights ;

    public void InitObjects(){
        this.Camera = new Camera();
        this.Settings = new GeneralSettings();
        this.Materials = new ArrayList<Material>();
        this.Spheres = new ArrayList<Sphere>();
        this.Planes = new ArrayList<Plane>();
        this.Boxes = new ArrayList<Box>();
        this.Lights = new ArrayList<Light>();
    }
}
