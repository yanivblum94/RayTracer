package RayTracing;

public class Ray {
    public Vector Direction;
    public Vector Origin;

    public Ray(Vector origin, Vector direction) {
        Direction = direction;
        Origin = origin;
    }

    public Vector tPointOnRay(double t){
        Vector temp = Vector.ScalarMultiply(this.Direction, t);
        return Vector.VectorAddition(this.Origin, temp);
    }
}
