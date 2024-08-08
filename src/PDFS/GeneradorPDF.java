
package PDFS;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.nio.file.Paths;

public class GeneradorPDF {

    public static void generarFactura(String idCancha, String idTurno, String nombreCompleto, String cedula, String fecha) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(25, 700);
            contentStream.showText("Factura de Reserva");
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(25, 650);
            contentStream.showText("ID de Cancha: " + idCancha);
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("ID de Turno: " + idTurno);
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Nombre Completo: " + nombreCompleto);
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Cédula: " + cedula);
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Fecha: " + fecha);
            contentStream.endText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ruta donde se guardará el PDF
        String ruta = "src/PDFS/FacturaReserva_" + idTurno + ".pdf";
        try {
            document.save(Paths.get(ruta).toFile());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
