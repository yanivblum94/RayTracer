package RayTracing;

import java.awt.*;
import java.util.List;

import static jdk.nashorn.internal.objects.NativeMath.min;

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
        red =
                (mat.DiffuseColorRed)*(1-mat.Transparency);
        green =
                (mat.DiffuseColorGreen)*(1-mat.Transparency);
        blue =
                (mat.DiffuseColorBlue)*(1-mat.Transparency);
        rgbData[index] = (byte) (255*red);//min(255*red,255) ;
        rgbData[index + 1] = (byte) (255*green) ;
        rgbData[index + 2] = (byte) (255*blue) ;
    }

    /* for a pixel which don't have a hit we define the colors using
    the background color as defined in the assignment
     */
    public static void GetBackgroundColor(byte[] rgbData, int index, Scene scene){
        rgbData[index] = (byte) scene.Settings.BackgroundColor.getRed();
        rgbData[index+1] = (byte) scene.Settings.BackgroundColor.getGreen();
        rgbData[index+2] = (byte) scene.Settings.BackgroundColor.getBlue();
    }
}

