package com.loy124.myapp.core.util.image;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Objects;

import static com.itextpdf.text.Font.FontFamily.HELVETICA;

@Slf4j
public class PdfPageEvent extends PdfPageEventHelper {

    // Watermark 폰트 설정
//  Font FONT = new Font(FontFamily.HELVETICA, 52, Font.BOLD, new GrayColor(0.75f));
    Font FONT = new Font(HELVETICA, 65, Font.BOLD, new CMYKColor(0, 0, 0, 5));

    Phrase[] header = new Phrase[2];
    int pagenumber;

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        header[0] = new Phrase("2015. 01. 07"); // 헤더 머릿말에 넣을 텍스트
    }

    @Override
    public void onChapter(PdfWriter writer, Document document,
                          float paragraphPosition, Paragraph title) {
        header[1] = new Phrase(title.getContent());
        pagenumber = 1;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        pagenumber++;
    }

    private void addBorder(PdfWriter writer, float margin, float line) {

        Rectangle rect = new Rectangle(
                margin, // 왼쪽 여백
                margin, // 아래쪽 여백
                (float) 420 - margin, // 오른쪽 여백
                (float) 594 - margin // 위쪽 여백
        );

        PdfContentByte canvas = writer.getDirectContent();

        // Draw rectangle border
        canvas.saveState();
        canvas.setColorStroke(BaseColor.GRAY); // 테두리 색 설정
        canvas.setLineWidth(line); // 테두리 두께 설정
        canvas.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight());
        canvas.stroke();
        canvas.restoreState();
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        // Watermark
        Image logo = null;

        try {
//            logo = Image.getInstance("src/main/resources/file/mincourse.png");
//            float centerX = document.getPageSize().getWidth() / 2;
//            float centerY = document.getPageSize().getHeight() / 2;
//            Objects.requireNonNull(logo).scaleToFit(100, 100);
//            // 로고를 중앙에 추가
//            logo.setAbsolutePosition(centerX - logo.getScaledWidth() / 2, centerY - logo.getScaledHeight() / 2 + 20);
////            writer.getDirectContent().addImage(logo);
//            PdfGState gs = new PdfGState();
//            gs.setFillOpacity(0.1f); // 투명도를 조절 (0.0은 완전 투명, 1.0은 완전 불투명)
//
//            // PdfGState 적용
//            writer.getDirectContent().setGState(gs);
//            // 로고를 추가
//            writer.getDirectContent().addImage(logo);
//
//            // 원래의 상태로 되돌리기 위해 PdfGState 초기화
//            writer.getDirectContent().setGState(new PdfGState());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        try {

            addBorder(writer, 12, 1f);
            addBorder(writer, 16, 2f);
            addBorder(writer, 20, 1f);

            URL url = getClass().getClassLoader().getResource("file/stamp.png");

//            logo = Image.getInstance("/src/main/resources/file/stamp.png");
            logo = Image.getInstance(Objects.requireNonNull(url));
            float centerX = document.getPageSize().getWidth();
            float centerY = document.getPageSize().getHeight();
            Objects.requireNonNull(logo).scaleToFit(30, 30);
            // 로고를 중앙에 추가
            logo.setAbsolutePosition(247, centerY -527);
//            writer.getDirectContent().addImage(logo);
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(1f); // 투명도를 조절 (0.0은 완전 투명, 1.0은 완전 불투명)

            // PdfGState 적용
            writer.getDirectContent().setGState(gs);
            // 로고를 추가
            writer.getDirectContent().addImage(logo);

            // 원래의 상태로 되돌리기 위해 PdfGState 초기화
            writer.getDirectContent().setGState(new PdfGState());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        ColumnText.showTextAligned(writer.getDirectContentUnder(),
//                Element.ALIGN_CENTER, new Phrase("mincourse", FONT), // 워터마크로 넣을 텍스트
//                297.5f, 421, 0);
                Element.ALIGN_CENTER, new Phrase("MINcourse", FONT), // 워터마크로 넣을 텍스트
                document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 0);

    }

}