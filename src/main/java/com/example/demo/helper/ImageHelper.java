package com.example.demo.helper;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

public class ImageHelper {

    /**
     * Compress and convert an image to WebP format.
     * If no WebP writer is found, saves the original image as a fallback.
     *
     * @param inputFile  Input image file (JPG/PNG, etc.)
     * @param outputFile Desired output file (should end with .webp)
     * @param quality    Compression quality (0.0 to 1.0)
     * @throws IOException if conversion or fallback saving fails
     */
    public static void compressToWebP(File inputFile, File outputFile, float quality) throws IOException {
        // Ensure output folder exists
        if (outputFile.getParentFile() != null && !outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        // Read input image
        BufferedImage image = ImageIO.read(inputFile);
        if (image == null) {
            throw new IOException("Invalid image file: " + inputFile.getAbsolutePath());
        }

        // Find a WebP writer
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType("image/webp");

        // 🟡 Fallback: if no WebP plugin found, save original instead of crashing
        if (!writers.hasNext()) {
            System.err.println("⚠️  No WebP writer found — saving original image as fallback: " + outputFile.getName());
            Files.copy(inputFile.toPath(), outputFile.toPath());
            return;
        }

        ImageWriter writer = writers.next();

        try (FileImageOutputStream output = new FileImageOutputStream(outputFile)) {
            writer.setOutput(output);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality); // 0.0 (low) to 1.0 (high)
            }

            writer.write(null, new IIOImage(image, null, null), param);
            System.out.println("✅ Image successfully converted to WebP: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            // Final safeguard fallback
            System.err.println("⚠️ WebP conversion failed, saving original: " + e.getMessage());
            Files.copy(inputFile.toPath(), outputFile.toPath());
        } finally {
            writer.dispose();
        }
    }
}
