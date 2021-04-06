package RayTracing;

import java.awt.*;
import java.util.List;

public class ColorUtils {

    /*function to calculate color of pixel by updating rgbData array
    on the indexes representing the pixel of the image
     */
    public static void GetColor(Hit hit, byte[] rgbData, int index, Scene scene){
        //naive implementation - just the colors from the formula without lights calculations
        Shapes s = hit.Shape;
        double red, green, blue;
        Material mat;
        switch (s){
            case Sphere:
                mat = scene.Spheres.get(hit.Index).SphereMaterial;
                break;
            case Plane:
                mat = scene.Planes.get(hit.Index).PlaneMaterial;
                break;
            case Box:
                mat = scene.Boxes.get(hit.Index).BoxMaterial;
                break;
            default:
                System.out.println("error on hit.shape ");
                return;

        }
        red = scene.Settings.BackgroundColorRed*mat.Transparency +
                (mat.DiffuseColorRed+mat.SpecularColorRed)*(1-mat.Transparency);
        green = scene.Settings.BackgroundColorGreen*mat.Transparency +
                (mat.DiffuseColorGreen+mat.SpecularColorGreen)*(1-mat.Transparency);
        blue = scene.Settings.BackgroundColorBlue*mat.Transparency +
                (mat.DiffuseColorBlue+mat.SpecularColorBlue)*(1-mat.Transparency);
        rgbData[index] = (byte) (255*red) ;
        rgbData[index + 1] = (byte) (255*green) ;
        rgbData[index + 2] = (byte) (255*blue) ;
    }

    /* for a pixel which don't have a hit we define the colors using
    the background color as defined in teh assignment
     */
    public static void GetBackgroundColor(byte[] rgbData, int index, Scene scene){
        rgbData[index] = (byte) scene.Settings.BackgroundColorRed;
        rgbData[index+1] = (byte) scene.Settings.BackgroundColorGreen;
        rgbData[index+2] = (byte) scene.Settings.BackgroundColorBlue;
    }
}

