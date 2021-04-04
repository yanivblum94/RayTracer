package RayTracing;

public class Camera {
    public Vector Position;
    public Vector LookAtPoint;
    public Vector UpVector;
    public double ScreenDistance;
    public double ScreenWidth;
    public double K;
    public Vector RightVector;

    /*
    init Direction Vectors according to what we saw in the lecture
     */
    public void InitDirectionVectors(){
        Vector temp = Vector.VectorSubtraction(this.LookAtPoint, this.Position);
        this.LookAtPoint = Vector.NormalVector(temp);
        this.UpVector.Normalize();
         temp = Vector.CrossProduct(this.UpVector, this.LookAtPoint);
        this.RightVector = Vector.NormalVector(temp);
    }
}
