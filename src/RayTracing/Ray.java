package RayTracing;

public class Ray {
    public Vector Origin;
    public Vector Direction;

    public Ray(Vector origin, Vector direction) {
        Direction = direction;
        Origin = origin;
    }

    public Vector tPointOnRay(double t){
        Vector temp = Vector.ScalarMultiply(Direction, t);
        return Vector.VectorAddition(Origin, temp);
    }
}
