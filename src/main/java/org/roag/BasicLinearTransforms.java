package org.roag;

import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Just copied this code from OpenCv documentation
 * https://docs.opencv.org/3.4.3/d3/dc1/tutorial_basic_linear_transform.html
 */
class BasicLinearTransforms {

    private byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }

    public void run(String[] args) {
        String imagePath = args.length > 0 ? args[0] : "src/main/resources/salmon_spot.jpg";
        Mat image = Imgcodecs.imread(imagePath);
        if (image.empty()) {
            System.out.println("Empty image: " + imagePath);
            System.exit(0);
        }
        Mat newImage = Mat.zeros(image.size(), image.type());
        double alpha = 1.0; /*< Simple contrast control */
        int beta = 0;       /*< Simple brightness control */
        System.out.println(" Basic Linear Transforms ");
        System.out.println("-------------------------");
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("* Enter the alpha value [1.0-3.0]: ");
            alpha = scanner.nextDouble();
            System.out.print("* Enter the beta value [0-100]: ");
            beta = scanner.nextInt();
        }
        byte[] imageData = new byte[(int) (image.total() * image.channels())];
        image.get(0, 0, imageData);
        byte[] newImageData = new byte[(int) (newImage.total() * newImage.channels())];
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                for (int c = 0; c < image.channels(); c++) {
                    double pixelValue = imageData[(y * image.cols() + x) * image.channels() + c];
                    pixelValue = pixelValue < 0 ? pixelValue + 256 : pixelValue;
                    newImageData[(y * image.cols() + x) * image.channels() + c]
                            = saturate(alpha * pixelValue + beta);
                }
            }
        }
        newImage.put(0, 0, newImageData);
        HighGui.imshow("Original Image", image);
        HighGui.imshow("New Image", newImage);
        HighGui.waitKey();
        System.exit(0);
    }

    public static void main(String[] args) {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new BasicLinearTransforms().run(args);
    }
}