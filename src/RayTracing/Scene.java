package RayTracing;

import java.awt.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    public Camera Camera;
    public GeneralSettings Settings;
    public List<Material> Materials;
    public List<Sphere> Spheres;
    public List<Plane> Planes;
    public List<Box> Boxes;
    public List<Light> Lights;
    public Vector Vx;
    public Vector Vy;
    public double PixelSize;

    public void InitObjects(){
        this.Settings = new GeneralSettings();
        this.Materials = new ArrayList<Material>();
        this.Spheres = new ArrayList<Sphere>();
        this.Planes = new ArrayList<Plane>();
        this.Boxes = new ArrayList<Box>();
        this.Lights = new ArrayList<Light>();
    }

    public Color getAmbientLighting(){
        List<Color> colors = new ArrayList<Color>();
        for (Light l: Lights) {
            colors.add(l.LightColor);
        }
        Color lightsAvgColor = ColorUtils.avgColor(colors);
        colors.clear();
        for (Sphere s: Spheres){
            colors.add(ColorUtils.mult(s.SphereMaterial.DiffuseColor,(float)s.SphereMaterial.ambientReflection));
        }
        for (Plane p : Planes){
            colors.add(ColorUtils.mult(p.PlaneMaterial.DiffuseColor,(float)p.PlaneMaterial.ambientReflection));
        }
        for (Box b : Boxes){
            colors.add(ColorUtils.mult(b.BoxMaterial.DiffuseColor,(float)b.BoxMaterial.ambientReflection));
        }
        lightsAvgColor = ColorUtils.mult(lightsAvgColor,ColorUtils.avgColor(colors));
        return lightsAvgColor;
    }
}

