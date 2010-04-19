import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.print.PrintTranscoder;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ImgTemplate;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/** 
 * <b>Description:TODO</b>
 * @author      gennady<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 OXSEED AG <br>
 * <b>Company:</b>       OXSEED AG  <br>
 * 
 * Creation:  Apr 19, 2010::9:53:46 PM<br> 
 */
public class SVG2PDF {
	   /** Creates a new instance of Main */
    final String svg = "<?xml version='1.0' encoding='ISO-8859-1' standalone='no'?>" +
            "<svg width='1188' height='756' xmlns='http://www.w3.org/2000/svg'>" +
            " <g transform='translate(50,50) scale(5,5)'>" +
            "  <rect x='0.0' y='0' width='200.0' height='100.0' style='fill:red; stroke:black; stroke-width:.2; fill-opacity: 0.2;' />" +
            "  <rect x='20.0' y='20' width='100.0' height='50.0' style='fill:red; stroke:black; stroke-width:.2; fill-opacity: 0.4;' />" +
            "  <rect x='40.0' y='40' width='50.0' height='10.0' style='fill:blue; stroke:black; stroke-width:.2; fill-opacity: 0.1;' />" +
            "  <text x='45.0' y='45' >HelloAbcde</text>" +
            " </g>" +
            "</svg>";
    
    public SVG2PDF() {
        com.lowagie.text.Document document = new
com.lowagie.text.Document(PageSize._11X17.rotate());
        try {
            PdfWriter writer = PdfWriter.getInstance(document,	new
FileOutputStream("svgsize.pdf"));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            cb.concatCTM(1.0f, 0, 0, 1.0f , 36, 0 );
            Graphics2D g2 = cb.createGraphics(1188, 756);
            PrintTranscoder prm = new PrintTranscoder();
            TranscoderInput ti = new TranscoderInput( new StringReader(svg));
            prm.transcode(ti, null);
            PageFormat pg = new PageFormat();
            Paper pp= new Paper();
            pp.setSize( 1188, 756);
            pp.setImageableArea(0, 0, 1188, 756);
            pg.setPaper(pp);
            prm.print(g2, pg, 0);
            g2.dispose();
            cb.restoreState();
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
     }
    public static void main(String[] args) {
    	SVG2PDF m = new SVG2PDF();
    }


}


 