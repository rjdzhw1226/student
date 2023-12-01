package com.student.util.file;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class PDFUtils {

	/**
	 *
	 * @Title: getDefaultFont
	 * @Description: 获取系统自带字体，如未安装可先安装或引入
	 * @return PdfFont
	 * @author yanghainan
	 * @date 2020年7月8日上午10:46:39
	 */
	public static PdfFont getDefaultFont() {
		try {
//			return PdfFontFactory.createFont("C://windows//fonts//simsun.ttc,1", PdfEncodings.IDENTITY_H, false);// 引用系统字体
			return PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H", false);// 使用itext字体
		} catch (IOException e) {
			// 记录日志
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @Title: getImportFont
	 * @Description: 获取引入的其他字体 （支持Linux系统）
	 * @return PdfFont
	 * @author yanghainan
	 * @date 2020年7月8日上午10:13:01
	 */
	public static PdfFont getImportFont(String fontName) {
		// 获取resource下文件夹路径
		String path = "/template/";
		String fontPath = path + fontName;
		try {
			// 处理中文乱码（支持Linux系统）
			FontProgram fontProgram = FontProgramFactory.createFont(fontPath, false);
			PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, false);
			return font;
		} catch (IOException e) {
			// 记录日志
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 替换PDF文本表单域变量
	 *
	 * @param templatePdfPath
	 *            要替换的pdf全路径
	 * @param params
	 *            替换参数
	 * @param destPdfPath
	 *            替换后保存的PDF全路径
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static final void replaceTextFieldPdf(String templatePdfPath, String destPdfPath,
		Map<String, String> params) {
		PdfDocument pdf = null;
		try {
			// 判断文件是否存在
			File file = new File(destPdfPath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			// 有参数才替换
			if (params != null && !params.isEmpty()) {
				pdf = new PdfDocument(new PdfReader(templatePdfPath), new PdfWriter(destPdfPath));
				PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
				Map<String, PdfFormField> fields = form.getFormFields(); // 获取所有的表单域
				for (String param : params.keySet()) {
					PdfFormField formField = fields.get(param); // 获取某个表单域
					if (formField != null) {
            if(param.contains("img")){
              replaceFieldImage(params, pdf, param, formField); // 替换图片
            } else {
              formField.setFont(getImportFont("SourceHanSansCN-Regular.ttf")).setValue(params.get(param)); // 替换值
//						formField.setFont(getDefaultFont()).setValue(params.get(param)); // 替换值
            }
					}
				}
				form.flattenFields();// 锁定表单，不让修改
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pdf != null) {
				pdf.close();
			}
		}
	}

	/**
	 * 替换PDF图片表单域（文本）变量，1、获取表单域的大小；2、根据表单域的位置，确定图片的位置；3、如果图片的宽或者高大于表单域，则等比压缩图片。
	 *
	 * @param templatePdfPath
	 *            要替换的pdf全路径
	 * @param params
	 *            替换参数
	 * @param destPdfPath
	 *            替换后保存的PDF全路径
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static final void replaceImageFieldPdf(String templatePdfPath, String destPdfPath,
			Map<String, String> params) throws FileNotFoundException, IOException {
		PdfDocument pdf = new PdfDocument(new PdfReader(templatePdfPath), new PdfWriter(destPdfPath));

		if (params != null && !params.isEmpty()) {// 有参数才替换
			PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
			Map<String, PdfFormField> fields = form.getFormFields(); // 获取所有的表单域
			for (String param : params.keySet()) {
				PdfFormField formField = fields.get(param);
				if (formField != null) {
					replaceFieldImage(params, pdf, param, formField); // 替换图片
				}
			}
			form.flattenFields();// 锁定表单，不让修改
		}
		pdf.close();
	}

	/**
	 * 替换域中的图片
	 *
	 * @param params
	 * @param pdf
	 * @param param
	 * @param formField
	 * @throws MalformedURLException
	 */
	private static final void replaceFieldImage(Map<String, String> params, PdfDocument pdf, String param,
			PdfFormField formField) throws MalformedURLException {
    String value = params.get(param);
    if (StringUtils.isBlank(value)) {
      return;
    }
		String[] values = value.split("\\|");
		Rectangle rectangle = formField.getWidgets().get(0).getRectangle().toRectangle(); // 获取表单域的xy坐标
		PdfCanvas canvas = new PdfCanvas(pdf.getPage(Integer.parseInt(values[0])));
    ImageData image = ImageDataFactory.create(values[1]);
    Image image1 = new Image(image);
    float imageWidth = image.getWidth();
		float imageHeight = image.getHeight();
		float rectangleWidth = rectangle.getWidth();
		float rectangleHeight = rectangle.getHeight();

		float tempWidth = 0;
		float tempHeight = 0;

		int result = 1; // 压缩宽度
		if (imageWidth > rectangleWidth) {
			tempHeight = imageHeight * rectangleWidth / imageWidth;
			if (tempHeight > rectangleHeight) {
				tempHeight = rectangleHeight;
				result = 2; // 压缩高度
			} else {
				tempWidth = rectangleWidth;
				tempHeight = imageHeight * rectangleWidth / imageWidth;
			}
		} else {
			if (imageHeight > rectangleHeight) {
				tempHeight = rectangleHeight;
				result = 2;
			} else {
				result = 3;
			}
		}

		float y = 0;

		if (result == 1) { // 压缩宽度
			y = rectangleHeight - tempHeight;
		} else if (result == 3) { // 不压缩
			y = rectangleHeight - imageHeight;
		}

		// y/=2; // 如果想要图片在表单域的上下对齐，这个值除以2就行。同理可以计算x的偏移
//    Canvas canvasMine = new Canvas(canvas, pdf,rectangle);
//    canvasMine.add(image1);
		if (result == 1) {
			canvas.addImage(image, rectangle.getX(), rectangle.getY() + y, tempWidth, false);
		} else if (result == 2) {
			canvas.addImage(image, rectangle.getX(), rectangle.getY(), tempHeight, false, false);
		} else if (result == 3) {
			canvas.addImage(image, rectangle.getX(), rectangle.getY() + y, false);
		}
	}

	/**
	 *
	 * @Title: addWatermark
	 * @Description: 添加文字水印
	 * @param srcPdfPath 原文件路径
	 * @param destPdfPath 替换后保存的PDF全路径
	 * @param watermarkText 水印
	 * @throws FileNotFoundException
	 * @throws IOException void
	 * @author yanghainan
	 * @date 2020年7月8日上午8:48:15
	 */
	@SuppressWarnings("resource")
	public static final void addWatermark(String srcPdfPath, String destPdfPath, String watermarkText)
			throws FileNotFoundException, IOException {
		// 判断文件是否存在
		File f1 = new File(srcPdfPath);
		if (!f1.exists()) {
			return;
		}
		// 判断文件是否存在
		File f2 = new File(destPdfPath);
		if (!f2.getParentFile().exists()) {
			f2.getParentFile().mkdirs();
		}

		PdfDocument pdfDoc = new PdfDocument(new PdfReader(srcPdfPath), new PdfWriter(destPdfPath));

		pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new IEventHandler() {
			@Override
			public void handleEvent(Event event) {
				PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
				PdfDocument pdfDoc = docEvent.getDocument();
				PdfPage page = docEvent.getPage();
				PdfFont font = null;
				font = getImportFont("STSong.ttf"); // 要显示中文水印的话，需要设置中文字体
				PdfCanvas canvas = new PdfCanvas(page);
				PdfExtGState gs1 = new PdfExtGState();
				gs1.setFillOpacity(0.7f); // 水印透明度
				canvas.setExtGState(gs1);
				new Canvas(canvas, pdfDoc, page.getPageSize()).setFontColor(ColorConstants.LIGHT_GRAY) // 颜色
						.setFontSize(60) // 字体大小
						.setFont(font) // 字体的格式 即导入的字体包
						// 水印的内容（中英文都支持） 坐标（例如：298f, 421f） 当前页数 最后的值为倾斜度（170）
						.showTextAligned(new Paragraph(watermarkText), 298, 421, pdfDoc.getPageNumber(page),
								TextAlignment.CENTER, VerticalAlignment.MIDDLE, 45);
			}
		});
		pdfDoc.close();
	}

	/**
	 *
	 * @Title: pdfMerger
	 * @Description: PDF合并
	 * @param paths 需要合并的所有文件路径(路径+名称)
	 * @param outputPath 合并后的文件路径(路径+名称)
	 * 	void
	 * @author yanghainan
	 * @date 2020年7月3日下午3:21:00
	 */
	public static boolean pdfMerger(List<String> paths, String outputPath) {
		if (paths == null || paths.isEmpty() || StringUtils.isBlank(outputPath)) {
			return false;
		}
		// 首页与其他页创建方式不同，所以需要创建两个
		PdfDocument firstSourcePdf = null;
		PdfDocument secondSourcePdf = null;
		// 合并需要的工具类
		PdfMerger merger = null;
		try {
			for (int i = 0; i < paths.size(); i++) {
				if (i == 0) {
					PdfWriter pdfWriter = new PdfWriter(outputPath);
					// 启用完全压缩
//					pdfWriter.isFullCompression();
					firstSourcePdf = new PdfDocument(new PdfReader(paths.get(i)), pdfWriter);
					merger = new PdfMerger(firstSourcePdf);
					continue;
				}
				secondSourcePdf = new PdfDocument(new PdfReader(paths.get(i)));
				merger.merge(secondSourcePdf, 1, secondSourcePdf.getNumberOfPages());
				secondSourcePdf.close();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if (firstSourcePdf != null) {
				firstSourcePdf.close();
			}
			if (secondSourcePdf != null) {
				secondSourcePdf.close();
			}
			if (merger != null) {
				merger.close();
			}
		}
		return false;
	}

	/**
	 *
	 * @Title: downPdf
	 * @Description: 文件下载
	 * @param response
	 * @param reviewName
	 * @param outputFilePath
	 * @return String
	 * @author yanghainan
	 * @date 2020年7月6日下午4:52:48
	 */
	public static String downPdf(HttpServletResponse response, String reviewName, String outputFilePath) {
		File file = new File(outputFilePath);
		if (!file.exists()) {
			return "下载文件不存在";
		}
		// 设置格式
		try {
			response.reset();
			response.setContentType("application/octet-stream");
			response.setCharacterEncoding("utf-8");
			response.setContentLength((int) file.length());
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(reviewName, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 文件写出
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
			byte[] buff = new byte[1024];
			OutputStream os = response.getOutputStream();
			int i = 0;
			while ((i = bis.read(buff)) != -1) {
				os.write(buff, 0, i);
				os.flush();
			}
		} catch (Exception e) {
			return "下载失败";
		} finally {
			// 删除服务器文件
			forceDelete(file);
		}
		return "下载成功";
	}

	/**
	 *
	 * @Title: forceDelete
	 * @Description: 强制删除文件
	 * @param f
	 * @return boolean
	 * @author yanghainan
	 * @date 2020年7月7日下午3:05:14
	 */
	public static boolean forceDelete(File f) {
		boolean result = false;
		int tryCount = 0;
		while (!result && tryCount++ < 10) {
//			System.gc();
			result = f.delete();
		}
		return result;
	}

	/**
	 *
	 * @Title: forceBatchDelete
	 * @Description: 强制删除文件（批量）
	 * @param paths
	 * @return int
	 * @author yanghainan
	 * @date 2020年7月7日下午3:09:37
	 */
	public static int forceBatchDelete(List<String> paths) {
		if (paths == null || paths.isEmpty()) {
			return 0;
		}
		int result = 0;
		for (int i = 0; i < paths.size(); i++) {
			boolean delete = forceDelete(new File(paths.get(i)));
			if (delete) {
				result++;
			}
		}
		return result;
	}

  /**
   * 测试方法
   * @param dest
   * @param IMG
   * @throws Exception
   */
  public static void manipulatePdf(String dest,String IMG) throws Exception {
    PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
    Document doc = new Document(pdfDoc);

    Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
    table.addCell(new Cell(2, 1).add(new Image(ImageDataFactory.create(IMG))
      .setWidth(UnitValue.createPercentValue(100))));
    table.addCell("1");
    table.addCell("2");

    doc.add(table);

    doc.close();
  }



}
