package usta.motos.infrastructure.adapter.in.web;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import usta.motos.domain.model.Maintenance;
import usta.motos.domain.service.MaintenanceCostCalculator;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfReports {

    public static byte[] generateMaintenanceReport(List<Maintenance> maintenances, String subtitleText) {

        Document document = new Document(PageSize.A4, 36, 36, 54, 54);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Color primaryColor = new Color(33, 37, 41);
            Color accentColor = new Color(13, 110, 253);
            Color lightGray = new Color(248, 249, 250);


            Paragraph title = new Paragraph("TALLER DE MOTOS", new Font(Font.HELVETICA, 16, Font.BOLD, accentColor));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph(subtitleText, new Font(Font.HELVETICA, 12, Font.BOLD, primaryColor));
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(8);
            document.add(subtitle);


            String fechaEmision = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph dateInfo = new Paragraph("Fecha de emisión: " + fechaEmision, new Font(Font.HELVETICA, 9, Font.ITALIC, Color.GRAY));
            dateInfo.setAlignment(Element.ALIGN_RIGHT);
            dateInfo.setSpacingAfter(15);
            document.add(dateInfo);


            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 3.5f, 2f, 2.5f, 1.8f});


            addHeaderCell(table, "Fecha", primaryColor);
            addHeaderCell(table, "Descripción", primaryColor);
            addHeaderCell(table, "Costo", primaryColor);
            addHeaderCell(table, "Mecánico", primaryColor);
            addHeaderCell(table, "Placa Moto", primaryColor);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Font bodyFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.DARK_GRAY);
            boolean alternateColor = false;
            double totalCost = 0;


            for (Maintenance m : maintenances) {
                totalCost += m.getCost();
                Color rowBg = alternateColor ? lightGray : Color.WHITE;

                addBodyCell(table, m.getMaintenanceDate() != null ? m.getMaintenanceDate().format(formatter) : "", bodyFont, rowBg, Element.ALIGN_CENTER);
                addBodyCell(table, m.getDescription() != null ? m.getDescription() : "", bodyFont, rowBg, Element.ALIGN_LEFT);
                addBodyCell(table, String.format("$ %.2f", m.getCost()), bodyFont, rowBg, Element.ALIGN_RIGHT);
                addBodyCell(table, m.getMechanicName() != null ? m.getMechanicName() : "", bodyFont, rowBg, Element.ALIGN_LEFT);
                addBodyCell(table, m.getMotorcycle() != null ? m.getMotorcycle().getPlate() : "N/A", bodyFont, rowBg, Element.ALIGN_CENTER);

                alternateColor = !alternateColor;
            }

            document.add(table);


            MaintenanceCostCalculator calculator = new MaintenanceCostCalculator();
            double totalWithIva = calculator.calculateTotalCostWithIva(totalCost);

            Paragraph totalParagraph = new Paragraph("\nCantidad de servicios: " + maintenances.size()
                    + "\nSubtotal: $ " + String.format("%.2f", totalCost)
                    + "\nIVA (19%): $ " + String.format("%.2f", totalWithIva - totalCost),
                    new Font(Font.HELVETICA, 10, Font.NORMAL, Color.DARK_GRAY));
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            totalParagraph.setSpacingBefore(10);
            document.add(totalParagraph);

            Paragraph totalIvaParagraph = new Paragraph("Total con IVA: $ " + String.format("%.2f", totalWithIva),
                    new Font(Font.HELVETICA, 11, Font.BOLD, new Color(40, 167, 69)));
            totalIvaParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalIvaParagraph);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }


    private static void addHeaderCell(PdfPTable table, String text, Color bgColor) {
        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        PdfPCell cell = new PdfPCell(new Paragraph(text, headerFont));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setPadding(8);
        table.addCell(cell);
    }


    private static void addBodyCell(PdfPTable table, String text, Font font, Color bgColor, int alignment) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(6);
        table.addCell(cell);
    }
}
