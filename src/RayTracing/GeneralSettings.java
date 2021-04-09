package RayTracing;

import java.awt.*;

public class GeneralSettings {
    public double BackgroundColorRed;
    public double BackgroundColorGreen;
    public double BackgroundColorBlue;
    public int NumOfShadowRays;
    public int MaxRecursionLevels;
    public boolean FishEyeLens;
    public Color BackgroundColor;

    public void setBackgroundColor(){
        BackgroundColor = new Color((float) BackgroundColorRed, (float)BackgroundColorGreen,(float) BackgroundColorBlue);
    }
}
