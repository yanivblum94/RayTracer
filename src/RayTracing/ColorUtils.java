package RayTracing;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.objects.NativeMath.max;
import static jdk.nashorn.internal.objects.NativeMath.min;

public class ColorUtils {
    public  static  int count =0;   // DELETE AT END
    /*function to calculate color of pixel by updating rgbData array
    on the indexes representing the pixel of the image
     */
    public static void GetColor(Hit hit, byte[] rgbData, int index, Scene scene, Ray ray) {
        //naive implementation - just the colors from the formula without lights calculations
        Shapes s = hit.Shape;
        Vector normal = null;
        double red, green, blue;
        Material mat;
        switch (s) {
            case Sphere:
                mat = scene.Spheres.get(hit.Index).SphereMaterial;
                normal = Vector.VectorSubtraction(hit.HitPoint, scene.Spheres.get(hit.Index).Center);
                break;
            case Plane:
                mat = scene.Planes.get(hit.Index).PlaneMaterial;
                normal = scene.Planes.get(hit.Index).Normal;
                break;
            case Box:
                mat = scene.Boxes.get(hit.Index).BoxMaterial;
                // normal = will be written by Yaniv in the future
                break;
            default:
                System.out.println("error on hit.shape ");
                return;

        }
        //System.out.println("before getDiffuseColor");
        normal.Normalize();
        getDeffuseColor(rgbData, scene, hit, mat, index, normal, ray);
        //System.out.println(count);

    }

    /* for a pixel which don't have a hit we define the colors using
    the background color as defined in the assignment
     */
    public static void GetBackgroundColor(byte[] rgbData, int index, Scene scene) {
        rgbData[index] = (byte) scene.Settings.BackgroundColor.getRed();
        rgbData[index + 1] = (byte) scene.Settings.BackgroundColor.getGreen();
        rgbData[index + 2] = (byte) scene.Settings.BackgroundColor.getBlue();
    }

    public static void getDeffuseColor(byte[] rgbData, Scene scene, Hit hit, Material mat,
                                       int index, Vector normal, Ray ray) {
        float red, green, blue;
        Color f = Color.black;
        Color c = Color.black;
        for (Light l : scene.Lights) {
            Ray lightRay = new Ray(l.Position, Vector.VectorSubtraction(hit.HitPoint, l.Position));
            lightRay.Direction.Normalize();
            List<Hit> hits = Hit.FindHits(lightRay, scene);
            if (hits.size() == 0) { // case where light Ray hits nothing
                setColor(rgbData, index, c);
                return;
            }
            boolean isHit = true;
            float shadow = 1;//default val for NO shadow
            double d = Vector.Distance(Hit.FindClosest(hits, lightRay.Origin).HitPoint,hit.HitPoint);
            if(Math.abs(d)>0.00001){//light hits object before ours
                isHit = false;
                count ++ ;
                shadow =(float) (1-l.ShadowIntensity); //ass defined on assignment
            }
            c = mult(l.LightColor,mat.DiffuseColor);
            float nl =(float) Vector.DotProduct(normal, lightRay.Direction);
            nl = (float) Math.max(Math.abs(nl), 0.0);
            c = mult(c,nl);
            if(isHit){ // on Hit we add specular Color
                c=plus(c,getSpectacularColor(scene,hit,mat,normal,l, lightRay,ray));
            }
            c =mult(c,shadow);
            //c = mult(c,(float)(1-mat.Transparency));
            f = plus(f,c);
        }
        rgbData[index] = (byte) (f.getRed());
        rgbData[index + 1] = (byte) (f.getGreen());
        rgbData[index + 2] = (byte) (f.getBlue());
    }

    public static Color getSpectacularColor( Scene scene, Hit hit,Material mat,
                                             Vector normal,Light light, Ray lightRay,Ray ray){
        Vector v = Vector.VectorSubtraction(hit.HitPoint, scene.Camera.Position);// V goes from hitPoint -> eye
        v.Normalize();
        double temp = Math.max(Vector.DotProduct(v,Vector.getReflection(lightRay.Direction,normal)),0);
        temp = Math.pow(temp,mat.PhongSpecularityCoeffincient);
        Color c = mult(mat.SpecularColor,(float) (temp*light.SpecularIntensity));
        return c ;
    }
    public static Color avgColor(List<Color> colors) {
        float red = 0, green = 0, blue = 0;
        int count = 0;
        for (Color c : colors) {
            red += (c.getRed() / 255.0F);
            green += (c.getGreen() / 255.0F);
            blue += (c.getBlue() / 255.0F);
            count++;
        }
        red = red / count;
        green = green / count;
        blue = blue / count;
        return new Color(red, green, blue);
    }

    public static void setColor(byte[] rgbData, int index, Color c) {
        rgbData[index] = (byte) (c.getRed());
        rgbData[index + 1] = (byte) (c.getGreen());
        rgbData[index + 2] = (byte) (c.getBlue());
    }
    public static Color mult(Color a, Color b){
        float red = (a.getRed() / 255.0F) * (b.getRed() / 255.0F);
        float blue = (a.getBlue() / 255.0F) * (b.getBlue() / 255.0F);
        float green = (a.getGreen() / 255.0F) * (b.getGreen() / 255.0F);
        return new Color(Math.min(red,1),Math.min(green,1),Math.min(blue,1));
    }

    public static Color plus(Color a, Color b){
        float red = (a.getRed() / 255.0F) + (b.getRed() / 255.0F);
        float blue = (a.getBlue() / 255.0F) + (b.getBlue() / 255.0F);
        float green = (a.getGreen() / 255.0F) + (b.getGreen() / 255.0F);
        return new Color(Math.min(red,1),Math.min(green,1),Math.min(blue,1));
    }

    public static Color mult(Color a, float n){
        float red = (a.getRed() / 255.0F) * n;
        float blue = (a.getBlue() / 255.0F) * n;
        float green = (a.getGreen() / 255.0F) * n;
        return new Color(Math.min(red,1),Math.min(green,1),Math.min(blue,1));

    }
}

