package RayTracing;

import java.awt.*;
import java.util.List;


public class ColorUtils {
    /*function to calculate color of pixel by updating rgbData array
    on the indexes representing the pixel of the image
     */
    public static void GetColor(Hit hit, byte[] rgbData, int index, Scene scene, Ray ray) {
        Color c = Color.black;
        Shapes s = hit.Shape;
        Vector normal;
        Material mat = hit.GetMaterial(scene);
        normal = hit.Normal;
        c = CalcColor(scene, hit, mat, normal, ray);
        c = GetTransparency(hit, scene, mat, ray, c);
        Color ref = GetReflection(ray, hit, mat, scene.Settings.MaxRecursionLevels, scene);
        c = plus(c,ref);
        setColor(rgbData,index, c);

    }

    public static Color CalcColor(Scene scene,Hit hit,Material mat,Vector normal,Ray ray){
        if(hit == null){
            return scene.Settings.BackgroundColor;
        }
        Color res = Color.BLACK;
         res = plus(res, getDiffuseColor(scene, hit, mat, normal));
        return res;
    }
    /* for a pixel which don't have a hit we define the colors using
    the background color as defined in the assignment
     */
    public static void GetBackgroundColor(byte[] rgbData, int index, Scene scene) {

        rgbData[index] = (byte) scene.Settings.BackgroundColor.getRed();
        rgbData[index + 1] = (byte) scene.Settings.BackgroundColor.getGreen();
        rgbData[index + 2] = (byte) scene.Settings.BackgroundColor.getBlue();
    }

    public static Color getDiffuseColor(Scene scene, Hit hit, Material mat,
                                       Vector normal) {
        Color f = Color.black;
        for (Light l : scene.Lights) {
            Color c = Color.black;
            Ray lightRay = new Ray(l.Position, Vector.VectorSubtraction(hit.HitPoint, l.Position));
            lightRay.Direction.Normalize();
            List<Hit> hits = Hit.FindHits(lightRay, scene);
            if (hits.size() == 0) { // case where light Ray hits nothing
                continue;
            }
            float shadow = 1;//default val for NO shadow
            double d = Vector.Distance(Hit.FindClosest(hits, lightRay.Origin).HitPoint,hit.HitPoint);
            if(Math.abs(d)>0.00001){//light hits object before ours
                shadow =(float) (1-l.ShadowIntensity); //ass defined on assignment
            }
            double lightIntensity = SoftShadow.CalcLightIntensity(hit, scene, l);
            c = mult(l.LightColor,mat.DiffuseColor);
            c = mult(c,(float) lightIntensity);
            float nl =(float) Vector.DotProduct(normal, lightRay.Direction);
            nl = (float) Math.max(Math.abs(nl), 0.0);
            c = mult(c,nl);
            c = plus(c,getSpectacularColor(scene,hit,mat,normal,l, lightRay,lightIntensity));
            c =mult(c,shadow);
            f = plus(f,c);
        }
        return f;

    }

    public static Color getSpectacularColor( Scene scene, Hit hit,Material mat,
                                             Vector normal,Light light, Ray lightRay, double lightIntensity){
        Vector v = Vector.VectorSubtraction( scene.Camera.Position, hit.HitPoint);// V goes from hitPoint -> eye
        v.Normalize();
        Vector lightTry = Vector.VectorSubtraction(lightRay.Origin,hit.HitPoint);
        lightTry.Normalize();
        double temp = Math.max(Vector.DotProduct(v,Vector.getReflection(lightTry,normal)),0);
        temp = Math.pow(temp,mat.PhongSpecularityCoeffincient)*lightIntensity;
        Color c = mult(mat.SpecularColor,(float) (temp*light.SpecularIntensity));
        return c ;
    }

    public static Color GetReflection(Ray ray, Hit hitPoint, Material mat, int recursion, Scene scene){
        if(recursion == 0 || hitPoint == null || mat == null){
            return Color.BLACK;
        }
        double dotProduct = 2.0 * Vector.DotProduct(ray.Direction, hitPoint.Normal);
        Vector normalDot = Vector.ScalarMultiply(hitPoint.Normal, dotProduct);
        Vector newRayDir =Vector.VectorSubtraction(ray.Direction, normalDot);
        newRayDir.Normalize();
        Vector epsilon = Vector.VectorAddition(hitPoint.HitPoint, Vector.ScalarMultiply(newRayDir, 0.001));
        Ray reflecionRay = new Ray(epsilon, newRayDir);

        List<Hit> reflectionHits = Hit.FindHits(reflecionRay, scene);
        Hit closestFromHit;
        Material newMat;
        if(reflectionHits.size() ==0){
            closestFromHit = null;
            newMat = null;
        }
        else {
            closestFromHit = Hit.FindClosest(reflectionHits, reflecionRay.Origin);
            newMat = closestFromHit.GetMaterial(scene);
        }
        Vector norm = closestFromHit != null ? closestFromHit.Normal : null;
        Color color = CalcColor(scene, closestFromHit, newMat, norm, reflecionRay);
        color = GetTransparency(closestFromHit, scene, newMat, reflecionRay, color);
        Color reflection = GetReflection(reflecionRay, closestFromHit, newMat, recursion-1, scene);
        color = plus(color, reflection);
        color = mult(color, mat.RelectionColor);

        return color;
    }

    public static Color GetTransparency(Hit hit, Scene scene, Material currentMat, Ray ray, Color color){
        if(hit == null || currentMat == null || currentMat.Transparency ==0.0){ // if the shape is not transparent
            return color;
        }
        Vector epsilon = Vector.VectorAddition(hit.HitPoint, Vector.ScalarMultiply(ray.Direction, 0.00001));
        Ray transRay = new Ray(epsilon, ray.Direction);//construct a ray from the hit point in the same direction
        List<Hit> transHits = Hit.FindHits(transRay, scene); // find the hits after current hit point
        Material matAfter = null;
        Hit hitAfter =null;
        if(transHits.size() >0){
            hitAfter = Hit.FindClosest(transHits, transRay.Origin); // the closest hit after the current one
            matAfter = hitAfter.GetMaterial(scene); // the next material
        }
        float transparency = (float) currentMat.Transparency;//the current material trans parameter
        //calculate the 1st layer according to formula
        color = mult(color, (1-transparency));
        Vector normal = hitAfter!= null ? hitAfter.Normal : null;
        Color after = mult(CalcColor(scene, hitAfter, matAfter, normal , transRay), transparency);
        color = plus(color, after);
        while(transHits.size() > 0){
            if(hitAfter == null || matAfter == null || matAfter.Transparency == 0.0){
                return color;
            }
            transparency = (float) matAfter.Transparency;
            epsilon = Vector.VectorAddition(hitAfter.HitPoint, Vector.ScalarMultiply(ray.Direction, 0.00001));
            transRay = new Ray(epsilon, ray.Direction);//construct a ray from the hit point in the same direction
            transHits = Hit.FindHits(transRay, scene);
            if(transHits.size() == 0){continue;}
            color = mult(color,1- transparency);
            hitAfter = Hit.FindClosest(transHits, transRay.Origin);
            matAfter = hitAfter.GetMaterial(scene);
            after = mult(CalcColor(scene, hitAfter, matAfter, hitAfter.Normal, transRay),transparency );
            color = plus(color, after);
        }
        return color;
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

