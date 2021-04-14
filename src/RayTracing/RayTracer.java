package RayTracing;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

    public int imageWidth;
    public int imageHeight;

    /**
     * Runs the ray tracer. Takes scene file, output image file and image size as input.
     */
    public static void main(String[] args) {

        try {

            //Vector a = new Vector(1,3,-7);
            //Vector b = new Vector(-2,3,4);
           // System.out.println(Plane.CalcOffset(a,b));

            RayTracer tracer = new RayTracer();

            // Default values:
            tracer.imageWidth = 500;
            tracer.imageHeight = 500;

            if (args.length < 2)
                throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

            String sceneFileName = args[0];
            String outputFileName = args[1];

            if (args.length > 3)
            {
                tracer.imageWidth = Integer.parseInt(args[2]);
                tracer.imageHeight = Integer.parseInt(args[3]);
            }

            Scene imageScene = new Scene();
            imageScene.InitObjects();
            // Parse scene file:
            tracer.parseScene(sceneFileName,imageScene);

            // Render scene:
            tracer.renderScene(outputFileName, imageScene);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
        } catch (RayTracerException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    /**
     * Parses the scene file and creates the scene. Change this function so it generates the required objects.
     */
    public void parseScene(String sceneFileName, Scene imageScene) throws IOException, RayTracerException
    {
        FileReader fr = new FileReader(sceneFileName);

        BufferedReader r = new BufferedReader(fr);
        String line = null;
        int lineNum = 0;
        System.out.println("Started parsing scene file " + sceneFileName);

        while ((line = r.readLine()) != null)
        {
            line = line.trim();
            ++lineNum;

            if (line.isEmpty() || (line.charAt(0) == '#'))
            {  // This line in the scene file is a comment
                continue;
            }
            else
            {
                String code = line.substring(0, 3).toLowerCase();
                // Split according to white space characters:
                String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

                if (code.equals("cam"))
                {
                    // Add code here to parse camera parameters
                    Camera cam = new Camera();
                    cam.Position = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
                    cam.LookAtPoint = new Vector(Double.parseDouble(params[3]), Double.parseDouble(params[4]), Double.parseDouble(params[5]));
                    cam.UpVector = new Vector(Double.parseDouble(params[6]), Double.parseDouble(params[7]), Double.parseDouble(params[8]));
                    cam.ScreenDistance = Double.parseDouble(params[9]);
                    cam.ScreenWidth = Double.parseDouble(params[10]);
                    imageScene.Settings.FishEyeLens = (params.length>11) ? Boolean.parseBoolean(params[11]) : false;
                    cam.K = (params.length>12) ? Double.parseDouble(params[12]) : 0.5;
                    imageScene.Camera = cam;
                    System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
                }
                else if (code.equals("set"))
                {
                    // Add code here to parse general settings parameters
                    imageScene.Settings.BackgroundColorRed = Double.parseDouble(params[0]);
                    imageScene.Settings.BackgroundColorGreen = Double.parseDouble(params[1]);
                    imageScene.Settings.BackgroundColorBlue = Double.parseDouble(params[2]);
                    imageScene.Settings.NumOfShadowRays = Integer.parseInt(params[3]);
                    imageScene.Settings.MaxRecursionLevels = Integer.parseInt(params[4]);
                    imageScene.Settings.setBackgroundColor();
                    System.out.println(String.format("Parsed general settings (line %d)", lineNum));
                }
                else if (code.equals("mtl"))
                {
                    // Add code here to parse material parameters
                    Material mat = new Material();
                    mat.DiffuseColorRed = Double.parseDouble(params[0]);
                    mat.DiffuseColorGreen = Double.parseDouble(params[1]);
                    mat.DiffuseColorBlue = Double.parseDouble(params[2]);
                    mat.SpecularColorRed = Double.parseDouble(params[3]);
                    mat.SpecularColorGreen = Double.parseDouble(params[4]);
                    mat.SpecularColorBlue = Double.parseDouble(params[5]);
                    mat.ReflectionColorRed = Double.parseDouble(params[6]);
                    mat.ReflectionColorGreen = Double.parseDouble(params[7]);
                    mat.ReflectionColorBlue = Double.parseDouble(params[8]);
                    mat.PhongSpecularityCoeffincient = Double.parseDouble(params[9]);
                    mat.Transparency = Double.parseDouble(params[10]);
                    mat.setColors();
                    imageScene.Materials.add(mat);
                    System.out.println(String.format("Parsed material (line %d)", lineNum));
                }
                else if (code.equals("sph"))
                {
                    // Add code here to parse sphere parameters
                    Sphere sph = new Sphere();
                    sph.Center = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
                    sph.Radius = Double.parseDouble(params[3]);
                    sph.SphereMaterial = imageScene.Materials.get((Integer.parseInt(params[4])) - 1);
                    imageScene.Spheres.add(sph);
                    System.out.println(String.format("Parsed sphere (line %d)", lineNum));
                }
                else if (code.equals("pln"))
                {
                    // Add code here to parse plane parameters
                    Plane pln = new Plane();
                    pln.Normal = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
                    pln.Offset = Double.parseDouble(params[3]);
                    pln.PlaneMaterial = imageScene.Materials.get((Integer.parseInt(params[4])) - 1);
                    imageScene.Planes.add(pln);
                    System.out.println(String.format("Parsed plane (line %d)", lineNum));
                }
                else if (code.equals("box"))
                {
                    // Add code here to panew Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));rse box parameters
                    Box box = new Box();
                    box.Center = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
                    box.Scale = Double.parseDouble(params[3]);
                    box.BoxMaterial = imageScene.Materials.get((Integer.parseInt(params[4])) - 1);
                    System.out.println(String.format("Parsed box (line %d)", lineNum));
                }
                else if (code.equals("lgt"))
                {
                    // Add code here to parse light parameters
                    Light lgt = new Light();
                    lgt.Position = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
                    lgt.RedColor = Double.parseDouble(params[3]);
                    lgt.GreenColor = Double.parseDouble(params[4]);
                    lgt.BlueColor = Double.parseDouble(params[5]);
                    lgt.SpecularIntensity = Double.parseDouble(params[6]);
                    lgt.ShadowIntensity = Double.parseDouble(params[7]);
                    lgt.LightRadius = Double.parseDouble(params[8]);
                    lgt.setColor();
                    imageScene.Lights.add(lgt);
                    System.out.println(String.format("Parsed light (line %d)", lineNum));
                }
                else
                {
                    System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
                }
            }
        }

        // It is recommended that you check here that the scene is valid,
        // for example camera settings and all necessary materials were defined.
        imageScene.PixelSize = imageScene.Camera.ScreenWidth / this.imageWidth;
        System.out.println("Finished parsing scene file " + sceneFileName);

    }

    /**
     * Renders the loaded scene and saves it to the specified file location.
     */
    public void renderScene(String outputFileName,Scene imageScene)
    {
        long startTime = System.currentTimeMillis();
        double pixel = imageScene.Camera.ScreenWidth/imageWidth;
        // Create a byte array to hold the pixel data:
        byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

        imageScene.Camera.InitDirectionVectors();
        Vector screenCenter = Vector.VectorAddition(imageScene.Camera.Position,
                Vector.ScalarMultiply(imageScene.Camera.TowardsVector, imageScene.Camera.ScreenDistance)) ;//screen center = position + distance*towards
        Vector temp = Vector.VectorAddition(Vector.ScalarMultiply(imageScene.Camera.RightVector, (imageScene.Camera.ScreenWidth - pixel)),
                Vector.ScalarMultiply(imageScene.Camera.UpVector, imageHeight/500 -pixel)); //temp = (width - pixel size) * Right Vector + (height - pixel size) * Up Vector
        Vector p0 = Vector.VectorSubtraction(screenCenter, Vector.ScalarMultiply(temp, 0.5));
        imageScene.Camera.Dx = Vector.ScalarMultiply(imageScene.Camera.RightVector,pixel);
        imageScene.Camera.Dy = Vector.ScalarMultiply(imageScene.Camera.UpVector,pixel);
        for(int row=0 ; row<this.imageHeight; row++){
            for(int col=0; col<this.imageWidth; col++){
                Vector shift = Vector.VectorAddition(Vector.ScalarMultiply(imageScene.Camera.Dx,col), Vector.ScalarMultiply(imageScene.Camera.Dy,row));
                Vector currentPixel =   Vector.VectorAddition(p0, shift);
                Ray ray = new Ray(currentPixel, Vector.VectorSubtraction(currentPixel,imageScene.Camera.Position));
                ray.Direction.Normalize();//Normalize direction Vector
                List<Hit> hits = Hit.FindHits(ray, imageScene);
                //System.out.println("number of hits for ray "+row+", "+col+ " : " + hits.size());
                if(hits.size() == 0){// no hits - need background color
                    ColorUtils.GetBackgroundColor( rgbData, 3*(col+row*this.imageWidth),imageScene);
                }
               else {
                    Hit closestHitFromCam = Hit.FindClosest(hits, ray.Origin);
                    ColorUtils.GetColor(closestHitFromCam, rgbData, 3 * (col + row * this.imageWidth), imageScene,ray);
               }
                /*TODO
                image[row][col] = GetColor(hit);
                 */

            }
        }
        // Put your ray tracing code here!
        //
        // Write pixel color values in RGB format to rgbData:
        // Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
        //            green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
        //             blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
        //
        // Each of the red, green and blue components should be a byte, i.e. 0-255


        long endTime = System.currentTimeMillis();
        Long renderTime = endTime - startTime;

        System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

        // This is already implemented, and should work without adding any code.
        saveImage(this.imageWidth, rgbData, outputFileName);

        System.out.println("Saved file " + outputFileName);

    }




    //////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

    /*
     * Saves RGB data as an image in png format to the specified location.
     */
    public static void saveImage(int width, byte[] rgbData, String fileName)
    {
        try {

            BufferedImage image = bytes2RGB(width, rgbData);
            ImageIO.write(image, "png", new File(fileName));

        } catch (IOException e) {
            System.out.println("ERROR SAVING FILE: " + e.getMessage());
        }

    }

    /*
     * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
     */
    public static BufferedImage bytes2RGB(int width, byte[] buffer) {
        int height = buffer.length / width / 3;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);

        return result;
    }

    public static class RayTracerException extends Exception {
        public RayTracerException(String msg) {  super(msg); }
    }


}
