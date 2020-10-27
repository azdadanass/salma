package ma.azdad.view;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.primefaces.context.RequestContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.Color;

@ManagedBean(eager = true)
@Component
@Scope("application")
public class UtilsView {

	@Cacheable("utilsView.getMonthName")
	public String getMonthName(Integer month) {
		if (month == null)
			return null;
		return UtilsFunctions.getMonthName(month);
	}

	@Cacheable("utilsView.getCountryList")
	public List<String> getCountryList() {
		return Arrays.stream(Locale.getISOCountries()).map(s -> new Locale("", s).getDisplayCountry(Locale.US)).collect(Collectors.toList());

	}

	@Cacheable("utilsView.getYearList")
	public List<Integer> getYearList(Integer minYear, Integer maxYear) {
		return Stream.iterate(minYear, n -> n + 1).limit(maxYear + 1 - minYear).collect(Collectors.toList());
	}

	public List<Integer> getYearList(Integer range) {
		return getYearList(UtilsFunctions.getCurrentYear() - range, UtilsFunctions.getCurrentYear() + range);
	}

	public String addParameters(String path, String... tab) {
		Arrays.stream(tab).forEach(i -> System.out.println(i));
		path += "?" + tab[0];
		if (tab.length > 1)
			for (int i = 1; i < tab.length; i++)
				path += "&" + tab[i];
		return path;
	}

	public void execJavascript(String script) {
		RequestContext.getCurrentInstance().execute(script);
	}

	public Color getColor(Integer index) {
		return Color.get(index);
	}

	public String getBadge(Integer index) {
		return UtilsFunctions.getBadge(index);
	}

	public String getMapIcon(Integer index) {
		return UtilsFunctions.getMapIcon(index);
	}

	public String getLetter(Integer index) {
		return UtilsFunctions.getLetter(index);
	}

	public Date getCurrentDate() {
		return new Date();
	}

	public void excelExportation(Object document) {
		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;
		int rows; // No of rows
		rows = sheet.getPhysicalNumberOfRows();
		int cols = 0; // No of columns
		int tmp = 0;
		for (int i = 0; i < 10 || i < rows; i++) {
			row = sheet.getRow(i);
			if (row != null) {
				tmp = sheet.getRow(i).getPhysicalNumberOfCells();
				if (tmp > cols)
					cols = tmp;
			}
		}

		HSSFCellStyle dateCellStyle = wb.createCellStyle();
		CreationHelper createHelper = wb.getCreationHelper();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

		for (int r = 0; r < rows; r++) {
			row = sheet.getRow(r);
			for (int c = 0; c < cols; c++)
				formatCell(row.getCell(c), dateCellStyle);
		}

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		Font font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		cellStyle.setFont(font);

		row = sheet.getRow(0);
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++)
			row.getCell(i).setCellStyle(cellStyle);

	}

	private void formatCell(HSSFCell cell, HSSFCellStyle dateCellStyle) {
		String str;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			str = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			str = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			str = String.valueOf(cell.getNumericCellValue());
			break;
		default:
			str = "";
			break;
		}
		// CLEAN FROM HTML TAGS
		str = str.replaceAll("<[^>]*>", "");
		// DATE ?
		Date date = UtilsFunctions.getDate(str);
		if (date == null)
			date = UtilsFunctions.getDateTime(str);
		if (date != null) {
			cell.setCellValue(date);
			cell.setCellStyle(dateCellStyle);
			return;
		}
		// NUMERIC ?
		if (!str.startsWith("0")) {
			Double d = null;
			try {
				d = Double.valueOf(str.replace(" ", "").replace(" ", "").replace(",", ".").replace("'", ""));
			} catch (Exception e) {
				d = null;
			}
			if (d != null) {
				cell.setCellValue(d);
				return;
			}
		}

		cell.setCellValue(str);
	}

}