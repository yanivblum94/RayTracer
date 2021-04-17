package RayTracing;

import java.util.Objects;

public class Sphere extends Shape {
    public Vector Center;
    public double Radius;
    public Material SphereMaterial;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sphere sphere = (Sphere) o;
        return Double.compare(sphere.Radius, Radius) == 0 && Center.equals(sphere.Center) && SphereMaterial.equals(sphere.SphereMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Center, Radius, SphereMaterial);
    }
}
