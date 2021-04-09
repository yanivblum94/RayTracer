package RayTracing;

public class Camera {
    public Vector Position;
    public Vector LookAtPoint;
    public Vector TowardsVector;
    public Vector UpVector;
    public double ScreenDistance;
    public double ScreenWidth;
    public double K;
    public Vector RightVector;
    public Vector Dx;
    public Vector Dy;

    /*
    init Direction Vectors according to what we saw in the lecture
     */
    public void InitDirectionVectors(){
        //Vector temp = Vector.VectorSubtraction(this.LookAtPoint, this.Position);
        Vector temp = Vector.VectorSubtraction(this.Position, this.LookAtPoint);
        this.TowardsVector = Vector.NormalVector(temp);
        this.TowardsVector.Normalize();
         temp = Vector.CrossProduct(this.UpVector, this.TowardsVector);
        this.RightVector = Vector.NormalVector(temp);

    }
}
