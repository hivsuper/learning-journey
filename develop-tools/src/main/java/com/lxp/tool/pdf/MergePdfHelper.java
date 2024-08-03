package com.lxp.tool.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@UtilityClass
public class MergePdfHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MergePdfHelper.class);

    public static void mergePdfViaIText(List<InputStream> inputStreams, String destinationPath) {
        try (OutputStream mergeOutputStream = new FileOutputStream(destinationPath)) {
            Document document = new Document();
            PdfCopy copy = new PdfSmartCopy(document, mergeOutputStream);
            document.open();
            for (InputStream tempInputStream : inputStreams) {
                PdfReader pdfReader = new PdfReader(tempInputStream);
                copy.addDocument(pdfReader);
                LOGGER.info("Merging completed");
                pdfReader.close();
                tempInputStream.close();
            }
            document.close();
            copy.close();
        } catch (DocumentException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
