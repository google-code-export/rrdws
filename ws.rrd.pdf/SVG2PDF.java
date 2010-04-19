import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.print.PrintTranscoder;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ImgTemplate;
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
	public static void main(String[] args) {

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream("svg.pdf"));
            document.open();
            document.add(new Paragraph("SVG Example"));

            int width = 250;
            int height = 250;
            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate template = cb.createTemplate(width,height);
            BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB); 
            Graphics2D g2 = //template.createGraphics(width,height);
            	//new LocalG2D();
            	image .createGraphics();
            
            PrintTranscoder prm = new PrintTranscoder();
            TranscoderInput ti = //new TranscoderInput("file:///c:\\java\\svg.xml");
            	new TranscoderInput(PageFormat.class.getClassLoader().getSystemResourceAsStream("test.svg"));
            prm.transcode(ti, null);
           
            PageFormat pg = new PageFormat();
            Paper pp= new Paper();
            pp.setSize(width, height);
            pp.setImageableArea(0, 0, width, height);
            pg.setPaper(pp);
            prm.print(g2, pg, 0); 
            g2.dispose(); 

            ImgTemplate img = new ImgTemplate(template);           
            document.add(img); 
        } catch (DocumentException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
        document.close();

      }

}


 