package RayTracing;

import java.awt.*;

public class Material {

    public double DiffuseColorRed;
    public double DiffuseColorGreen;
    public double DiffuseColorBlue;
    public double SpecularColorRed;
    public double SpecularColorGreen;
    public double SpecularColorBlue;
    public double PhongSpecularityCoeffincient;
    public double ReflectionColorRed;
    public double ReflectionColorGreen;
    public double ReflectionColorBlue;
    public double Transparency;
    public Color DiffuseColor ;
    public Color SpecularColor ;
    public Color RelectionColor;
    public double ambientReflection;

    public void setColors(){
        DiffuseColor =new Color((float)Math.min(1.0,DiffuseColorRed), (float)Math.min(1.0,DiffuseColorGreen)
                , (float) Math.min(1.0,DiffuseColorBlue));
        SpecularColor = new Color((float)Math.min(1.0,SpecularColorRed), (float)Math.min(1.0,SpecularColorGreen)
                , (float) Math.min(1.0,SpecularColorBlue));
        RelectionColor = new Color((float)Math.min(1.0,ReflectionColorRed), (float)Math.min(1.0,ReflectionColorGreen)
                , (float) Math.min(1.0,ReflectionColorBlue));
    }
}
