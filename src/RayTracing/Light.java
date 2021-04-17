package RayTracing;

import java.awt.*;

public class Light {
    public Vector Position;
    public double RedColor;
    public double GreenColor;
    public double BlueColor;
    public double SpecularIntensity;
    public double ShadowIntensity;
    public double LightRadius;
    public Color LightColor;

    public void setColor(){
        LightColor =new Color((float)RedColor, (float)GreenColor, (float) BlueColor);
    }


}
