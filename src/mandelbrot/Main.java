package mandelbrot;

public class Main {
    public static void main(String[] args) {
        Configuration.MOVE_X = Double.parseDouble(args[0]);
        Configuration.MOVE_Y = Double.parseDouble(args[1]);
        Configuration.ZOOM = Double.parseDouble(args[2]);


        PixelColor[] pixels = Manager.calculateSet();
        //pixels = Manager.calculateAntialiasing(pixels);
        Graphics.drawPixels(pixels);
    }
}
