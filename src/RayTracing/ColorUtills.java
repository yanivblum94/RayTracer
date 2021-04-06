package RayTracing;

import java.awt.*;
import java.util.List;

public class ColorUtills {

    /*function to calculate color of pixel by updating rgbData array
    on the indexes representing the pixel of the image
     */
    public static void GetColor(Hit hit, byte[] rgbData, int index, Scene scene){
        //naive implementation - just the diffuse colors
        Shapes s = hit.shape;
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
        rgbData[index] = (byte) (255*mat.DiffuseColorRed) ;
        rgbData[index + 1] = (byte) (255*mat.DiffuseColorGreen) ;
        rgbData[index + 2] = (byte) (255*mat.DiffuseColorBlue) ;
    }
}

