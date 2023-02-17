package ocrapi.controller;

import net.sourceforge.tess4j.Tesseract;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    @PostMapping
    public ResponseEntity<String> convertFileToString(@RequestParam MultipartFile file) throws Exception {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String result = "";

        if (!"png".equals(extension) && !"jpg".equals(extension)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            Tesseract tesseract = new Tesseract();

            tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
            tesseract.setLanguage("eng");

            result = tesseract.doOCR(image);
        } catch (IOException ex) {
            throw new Exception("Error reading file");
        }

        return ResponseEntity.ok(result);
    }
}
