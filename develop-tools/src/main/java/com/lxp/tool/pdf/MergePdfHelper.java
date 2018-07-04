package com.lxp.tool.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class MergePdfHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MergePdfHelper.class);

    public static void mergePdfViaIText(List<InputStream> inputStreams) {
        Document document = null;
        try (OutputStream mergeOutputStream = new FileOutputStream(new File("D:/result.pdf"))) {
            document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, mergeOutputStream);
            document.open();
            PdfContentByte pdfContentByte = pdfWriter.getDirectContent();
            for (InputStream tempInputStream : inputStreams) {
                PdfReader pdfReader = null;
                pdfReader = new PdfReader(tempInputStream);
                for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
                    document.newPage();
                    PdfImportedPage page = pdfWriter.getImportedPage(pdfReader, i);
                    pdfContentByte.addTemplate(page, 0, 0);
                }
                LOGGER.info("Merging completed");
                tempInputStream.close();
            }
            document.close();
        } catch (DocumentException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
