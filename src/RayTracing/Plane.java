package RayTracing;

import java.util.Objects;
import java.util.Random;

public class Plane extends Shape {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane plane = (Plane) o;
        return Double.compare(plane.Offset, Offset) == 0 && Normal.equals(plane.Normal) && PlaneMaterial.equals(plane.PlaneMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Normal, Offset, PlaneMaterial);
    }
}
