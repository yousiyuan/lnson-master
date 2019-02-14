package com.lnson.simple.projname.utils.excel;

import com.lnson.simple.projname.common.Md5Utils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * POI包装类 读写Excel文件
 * create by : 流年公子
 * create time : 2018-07-20
 */
public final class PoiExcelUtils {

	private HashMap<String, Workbook> workBookPool = null;

	private Workbook wb = null;

	private PoiExcelUtils() {
		workBookPool = new HashMap<String, Workbook>();
	}

	private PoiExcelUtils(WorkbookTypeEnum wbType) {
		workBookPool = new HashMap<String, Workbook>();
		createWorkbook(wbType);
	}

	private PoiExcelUtils(String templateFilePath) {
		workBookPool = new HashMap<String, Workbook>();
		createWorkbook(new File(templateFilePath));
	}

	/**
	 * 创建用于读取Excel文件的POI对象
	 */
	public static PoiExcelUtils getPoi() {
		return new PoiExcelUtils();
	}

	/**
	 * 当不需要根据Excel模板将数据集导出到Excel文件时创建该POI对象
	 */
	public static PoiExcelUtils getPoi(WorkbookTypeEnum wbType) {
		return new PoiExcelUtils(wbType);
	}

	/**
	 * 当需要根据Excel模板将数据集导出到Excel文件时创建该POI对象
	 * 
	 * @param templateFilePath
	 *            Excel模板文件的绝对路径
	 */
	public static PoiExcelUtils getPoi(String templateFilePath) {
		return new PoiExcelUtils(templateFilePath);
	}

	/**
	 * 读取Excel模板，复制指定行单元格的样式到目标行，从指定的单元格开始写入数据
	 * 
	 * @param dataGrid
	 *            要写入模板的数据集
	 * @param startWriteCoordinate
	 *            从这个坐标开始写入数据集第一行第一列的值
	 */
	public <T extends Object> void writeTemplateList(List<TreeMap<T, String>> dataGrid, String startWriteCoordinate) {
		writeTemplateList(dataGrid, null, startWriteCoordinate, startWriteCoordinate);
	}

	/**
	 * 读取Excel模板，复制指定行单元格的样式到目标行，从指定的单元格开始写入数据
	 * 
	 * @param dataGrid
	 *            要写入模板的数据集
	 * @param sheetname
	 *            在哪个Sheet页操作
	 * @param startWriteCoordinate
	 *            从这个坐标开始写入数据集第一行第一列的值
	 */
	public <T extends Object> void writeTemplateList(List<TreeMap<T, String>> dataGrid, String sheetname,
			String startWriteCoordinate) {
		writeTemplateList(dataGrid, sheetname, startWriteCoordinate, startWriteCoordinate);
	}

	/**
	 * 读取Excel模板，复制指定行单元格的样式到目标行，从指定的单元格开始写入数据
	 * 
	 * @param dataGrid
	 *            要写入模板的数据集
	 * @param sheetname
	 *            在哪个Sheet页操作
	 * @param startWriteCoordinate
	 *            从这个坐标开始写入数据集第一行第一列的值
	 * @param referStyleCoordinate
	 *            参考这个坐标所在单元格的样式复制行
	 */
	private <T extends Object> void writeTemplateList(List<TreeMap<T, String>> dataGrid, String sheetname,
			String startWriteCoordinate, String referStyleCoordinate) {
		Sheet sheet = this.getSheet(sheetname);

		CellAddress startAddress = new CellAddress(startWriteCoordinate);
		int startRowNum = startAddress.getRow();
		int startColumnNum = startAddress.getColumn();

		CellAddress styleAddress = new CellAddress(referStyleCoordinate);
		int styleRowNum = styleAddress.getRow();
		Row styleRow1st = sheet.getRow(styleRowNum);
		Row styleRow2st = sheet.getRow(styleRowNum + 1);

		int j = 0;
		int size = dataGrid.size();
		for (int i = 0; i < size; i++) {
			int currentRowNum = startRowNum + i;
			Row valueRow = sheet.getRow(currentRowNum);
			if (valueRow == null) {
				valueRow = sheet.createRow(currentRowNum);
			}
			if (i + 1 > 2) {
				if ((i + 1) % 2 == 0) {
					copyRow(sheet, styleRow2st, valueRow, false);
				} else {
					copyRow(sheet, styleRow1st, valueRow, false);
				}
			}
			j = 0;
			TreeMap<T, String> columns = dataGrid.get(i);
			for (Entry<T, String> item : columns.entrySet()) {
				Cell cell = null;
				T key = item.getKey();
				String value = item.getValue();
				if (key instanceof String) {
					CellAddress cellAddress = new CellAddress(String.valueOf(key));
					cell = valueRow.getCell(cellAddress.getColumn());
				} else {
					cell = valueRow.getCell(j++ + startColumnNum);
				}
				cell.setCellValue(value);
			}
		}
	}

	/**
	 * 读取Excel模板文件，并根据坐标将结果集写入新创建的Excel文件, 如果已经读取Excel模板,可以使用这个方法反复对Excel模板进行
	 * 写入，相同坐标的单元格再次写入时值会被覆盖
	 * 
	 * @param dataGrid
	 *            具有唯一CellAddress(坐标)的单元格集合
	 */
	public void writeTemplate(TreeMap<String, String> dataGrid) {
		writeTemplate(dataGrid, null);
	}

	/**
	 * 读取Excel模板文件，并根据坐标将结果集写入新创建的Excel文件, 如果已经读取Excel模板,可以使用这个方法反复对Excel模板进行
	 * 写入，相同坐标的单元格再次写入时值会被覆盖
	 * 
	 * @param dataGrid
	 *            具有唯一CellAddress(坐标)的单元格集合
	 * @param sheetname
	 *            Sheet对象名称
	 */
	public void writeTemplate(TreeMap<String, String> dataGrid, String sheetname) {
		Sheet sheet = getSheet(sheetname);
		Iterator<Entry<String, String>> iter = dataGrid.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> element = iter.next();
			String cellCoordinate = element.getKey();
			String cellValue = element.getValue();
			CellAddress cellAddress = new CellAddress(cellCoordinate);
			int rowPosition = cellAddress.getRow();
			int columnPosition = cellAddress.getColumn();
			Row row = sheet.getRow(rowPosition);
			Cell cell = row.getCell(columnPosition);
			cell.setCellValue(cellValue);
		}
	}

	/**
	 * 将结果集写入新创建的Excel文件并销毁Workbook对象
	 * 
	 * @param dataGrid
	 *            dataGrid 结果集
	 */
	public <T extends Object> void writeExcel(List<TreeMap<T, String>> dataGrid) {
		writeExcel(dataGrid, null);
	}

	/**
	 * 将结果集写入新创建的Excel文件并销毁Workbook对象
	 * 
	 * @param dataGrid
	 *            结果集
	 * @param sheetname
	 *            新建Sheet页名称
	 */
	public <T extends Object> void writeExcel(List<TreeMap<T, String>> dataGrid, String sheetname) {
		writeExcel(dataGrid, sheetname, null);
	}

	/**
	 * 将结果集写入新创建的Excel文件并销毁Workbook对象
	 * 
	 * @param dataGrid
	 *            结果集
	 * @param sheetname
	 *            新建Sheet页名称
	 * @param cellCoordinate
	 *            从指定坐标开始写入
	 */
	public <T extends Object> void writeExcel(List<TreeMap<T, String>> dataGrid, String sheetname,
			String cellCoordinate) {
		if (cellCoordinate == null || "".equals(cellCoordinate.trim())) {
			cellCoordinate = "A1";
		}
		CellAddress cellAddr = new CellAddress(cellCoordinate);
		int topOffset = cellAddr.getRow();
		int leftOffset = cellAddr.getColumn();
		Sheet sheet = this.createSheet(sheetname);
		int rowsCount = dataGrid.size();
		int j = 0;
		for (int i = 0; i < rowsCount; i++) {
			Row row = sheet.getRow(i + topOffset);
			if (row == null)
				row = sheet.createRow(i + topOffset);
			TreeMap<T, String> columns = dataGrid.get(i);
			j = 0;
			for (Entry<T, String> item : columns.entrySet()) {
				Cell cell = null;
				T key = item.getKey();
				String value = item.getValue();
				if (key instanceof String) {
					CellAddress cellAddress = new CellAddress(String.valueOf(key));
					cell = row.createCell(cellAddress.getColumn(), CellType.STRING);
				} else {
					cell = row.createCell(j++ + leftOffset, CellType.STRING);
				}
				cell.setCellValue(value);
			}
		}
	}

	/**
	 * 读取Excel（*.xlsx与*.xls均支持）返回指定Sheet页的数据集
	 * 
	 * @return 返回读取Excel得到的结果集
	 */
	public List<TreeMap<String, String>> readExcel(String filepath) {
		return readExcel(filepath, null);
	}

	/**
	 * 读取Excel（*.xlsx与*.xls均支持）返回指定Sheet页的数据集
	 * 
	 * @param sheetname
	 *            sheet页名称,默认值null或空字符串
	 * @return 返回读取Excel得到的结果集
	 */
	public List<TreeMap<String, String>> readExcel(String filepath, String sheetname) {
		return readExcel(filepath, sheetname, null);
	}

	/**
	 * 读取Excel（*.xlsx与*.xls均支持）返回指定Sheet页的数据集
	 * 
	 * @param filepath
	 *            Excel文件绝对路径
	 * @param sheetname
	 *            sheet页名称,默认值null或空字符串
	 * @param startCoordinate
	 *            从指定单元格开始读取
	 * @return 返回读取Excel得到的结果集
	 */
	public List<TreeMap<String, String>> readExcel(String filepath, String sheetname, String startCoordinate) {
		return readExcel(filepath, sheetname, startCoordinate, null);
	}

	/**
	 * 读取Excel（*.xlsx与*.xls均支持）返回指定Sheet页的数据集
	 * 
	 * @param filepath
	 *            Excel文件绝对路径
	 * @param sheetname
	 *            sheet页名称,默认值null或空字符串
	 * @param startCoordinate
	 *            从指定单元格开始读取
	 * @param endCoordinate
	 *            到指定单元格结束读取
	 * @return 返回读取Excel得到的结果集
	 */
	public List<TreeMap<String, String>> readExcel(String filepath, String sheetname, String startCoordinate,
			String endCoordinate) {
		createWorkbook(new File(filepath));
		if (wb == null)
			return null;
		Sheet sheet = getSheet(sheetname);
		if (sheet == null)
			return null;
		List<TreeMap<String, String>> dataGrid = readSheet(sheet, startCoordinate, endCoordinate);
		return dataGrid;
	}

	/**
	 * 读取Excel文件指定的Sheet页
	 * 
	 * @param sheet
	 *            Sheet页对象
	 * @param startCoordinate
	 *            从指定单元格开始读取
	 * @param endCoordinate
	 *            到指定单元格结束读取
	 * @return 返回当前Sheet页由所有单元格组成的数据集合
	 */
	private List<TreeMap<String, String>> readSheet(Sheet sheet, String startCoordinate, String endCoordinate) {
		List<TreeMap<String, String>> rowsGrid = new ArrayList<TreeMap<String, String>>();
		TreeMap<String, String> columnsGrid = null;
		int topOffSet = 0;
		int leftOffSet = 0;
		if (!(startCoordinate == null || "".equals(startCoordinate.trim()))) {
			CellAddress startCellAddr = new CellAddress(startCoordinate);
			topOffSet = startCellAddr.getRow();
			leftOffSet = startCellAddr.getColumn();
		}
		int bottomOffSet = 0;
		int rightOffSet = 0;
		if (!(endCoordinate == null || "".equals(endCoordinate.trim()))) {
			CellAddress endCellAddr = new CellAddress(endCoordinate);
			bottomOffSet = endCellAddr.getRow();
			rightOffSet = endCellAddr.getColumn();
		}
		int firstRowPosition = sheet.getFirstRowNum();
		int lastRowPosition = sheet.getLastRowNum();
		if (lastRowPosition == -1 || firstRowPosition == -1)
			return rowsGrid;
		if (topOffSet != 0) {
			firstRowPosition = topOffSet;
		}
		if (bottomOffSet != 0) {
			lastRowPosition = bottomOffSet;
		}
		for (int r = firstRowPosition; r <= lastRowPosition; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			columnsGrid = new TreeMap<String, String>();
			int lastColumnPosition = row.getLastCellNum();
			int firstColumnPosition = row.getFirstCellNum();
			if (lastColumnPosition == -1 || firstColumnPosition == -1) {
				continue;
			}
			if (leftOffSet != 0) {
				firstColumnPosition = leftOffSet;
			}
			if (rightOffSet != 0) {
				lastColumnPosition = rightOffSet;
			}
			for (int c = firstColumnPosition; c <= lastColumnPosition; c++) {
				Cell cell = row.getCell(c);
				if (cell == null) {
					continue;
				}
				String key = cell.getAddress().toString();
				String value = String.valueOf(cell);
				columnsGrid.put(key, value);
			}
			rowsGrid.add(columnsGrid);
		}
		return rowsGrid;
	}

	/**
	 * 创建一个Sheet对象的副本
	 * 
	 * @param sourceSheetName
	 *            要被克隆的Sheet对象名称
	 * @param targetSheetName
	 *            新创建的Sheet对象的名称
	 */
	public void copySheet(String sourceSheetName, String targetSheetName) {
		if (wb == null)
			return;
		int sheetIndex = wb.getSheetIndex(sourceSheetName);
		Sheet sheet = wb.cloneSheet(sheetIndex);
		int targetSheetIndex = wb.getSheetIndex(sheet);
		wb.setSheetName(targetSheetIndex, targetSheetName);
	}

	/**
	 * 行复制功能
	 * 
	 * @param sheet
	 *            当前Sheet对象
	 * @param sourceRow
	 *            从这个行对象复制样式
	 * @param targetRow
	 *            将样式应用于这个行
	 * @param copyValueFlag
	 *            是否复制值：true-连同单元格的值一同复制;false-只复制格式
	 */
	private void copyRow(Sheet sheet, Row sourceRow, Row targetRow, boolean copyValueFlag) {
		targetRow.setHeight(sourceRow.getHeight());
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(targetRow.getRowNum(),
						targetRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				sheet.addMergedRegion(newCellRangeAddress);
			}
		}
		Iterator<Cell> cellIt = sourceRow.cellIterator();
		while (cellIt.hasNext()) {
			Cell tmpCell = (Cell) cellIt.next();
			Cell newCell = targetRow.createCell(tmpCell.getColumnIndex());
			copyCell(tmpCell, newCell, copyValueFlag);
		}
	}

	/**
	 * 将源单元格复制到目标单元格
	 * 
	 * @param sourceCell
	 *            源单元格
	 * @param targetCell
	 *            目标单元格
	 * @param copyValueFlag
	 *            true则连同cell的内容一起复制
	 */
	private void copyCell(Cell sourceCell, Cell targetCell, boolean copyValueFlag) {
		CellStyle targetStyle = wb.createCellStyle();
		targetStyle.cloneStyleFrom(sourceCell.getCellStyle());
		targetCell.setCellStyle(targetStyle);
		
		if (sourceCell.getCellComment() != null) {
			targetCell.setCellComment(sourceCell.getCellComment());
		}
		
		if (copyValueFlag) {
			targetCell.setCellValue(String.valueOf(sourceCell));
		}
	}

	/**
	 * 根据指定的Sheet名称从指定Excel工作簿对象Workbook查找Sheet对象，若未指定Sheet名称，默认获取第一个Sheet页对象
	 * 
	 * @param sheetname
	 *            Sheet名称
	 * @return 返回指定的Sheet对象
	 */
	private Sheet getSheet(String sheetname) {
		Sheet sheet = null;
		if (wb == null)
			return sheet;
		if (sheetname == null || "".equals(sheetname.trim())) {
			int sheetCount = wb.getNumberOfSheets();
			if (sheetCount > 0)
				sheet = wb.getSheetAt(0);
		} else {
			sheet = wb.getSheet(sheetname);
		}
		return sheet;
	}

	/**
	 * 删除指定名称的Sheet
	 * 
	 * @param sheetname
	 *            Sheet对象的名称
	 */
	public void removeSheet(String sheetname) {
		if (wb == null)
			return;
		int sheetIndex = wb.getSheetIndex(sheetname);
		wb.removeSheetAt(sheetIndex);
	}

	/**
	 * 创建一个Sheet对象
	 * 
	 * @param sheetname
	 *            Sheet页名称
	 * @return 返回新创建的Sheet对象，若具有相同名称的Sheet对象已经存在则直接返回
	 */
	private Sheet createSheet(String sheetname) {
		Sheet sheet = null;
		if (wb == null)
			return sheet;
		if (sheetname == null || "".equals(sheetname.trim())) {
			int sheetCount = wb.getNumberOfSheets();
			sheet = wb.createSheet("Sheet" + (sheetCount + 1));
		} else {
			sheet = wb.getSheet(sheetname);
			if (sheet == null)
				sheet = wb.createSheet(sheetname);
		}
		return sheet;
	}

	/**
	 * 使用Excel模板创建Workbook对象
	 * 
	 * @param file
	 *            Excel模板文件对象
	 */
	private void createWorkbook(File file) {
		if (!file.exists())
			return;
		String hashValue = Md5Utils.getFileHashValue(file);
		Workbook memoryBook = workBookPool.get(hashValue);
		if (memoryBook != null) {
			wb = memoryBook;
			return;
		}

		InputStream inStream = null;
		try {
			String filename = file.getName().toUpperCase();
			inStream = new FileInputStream(file);
			if (filename.endsWith(WorkbookTypeEnum.EXCEL_XLS.getValue()))
				wb = new HSSFWorkbook(inStream);
			else if (filename.endsWith(WorkbookTypeEnum.EXCEL_XLSX.getValue()))
				wb = new XSSFWorkbook(inStream);

			workBookPool.put(hashValue, wb);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStream != null)
					inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建指定的Workbook子类对象
	 * 
	 * @param wbType
	 *            指明构建Workbook的类型
	 */
	private void createWorkbook(WorkbookTypeEnum wbType) {
		if (wbType.equals(WorkbookTypeEnum.EXCEL_XLS)) {
			wb = new HSSFWorkbook();
		} else if (wbType.equals(WorkbookTypeEnum.EXCEL_XLSX)) {
			wb = new XSSFWorkbook();
		}
	}

	/**
	 * 使用HttpServlet响应对象导出工作薄
	 * 
	 * @param exportFileName
	 *            导出文件名称
	 * @param response
	 *            HttpServlet响应对象
	 */
	public void exportWorkbookAs(String exportFileName, HttpServletResponse response) {
		try {
			String fileName = java.net.URLEncoder.encode(exportFileName, "UTF-8");
			response.setHeader("Content-Disposition", MessageFormat.format("attachment; filename=\"{0}\"", fileName));
			response.setContentType("application/octet-stream;charset=UTF-8");
			this.exportWorkbookAs(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将当前工作薄写入Servlet响应对象
	 * 
	 * @param response
	 *            Servlet响应对象
	 */
	private void exportWorkbookAs(ServletResponse response) {
		OutputStream outStream = null;
		try {
			outStream = new BufferedOutputStream(response.getOutputStream());
			wb.write(outStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outStream != null)
					outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将Workbook对象写入指定位置的Excel文件并保存
	 * 
	 * @param saveFilePath
	 *            Excel文件输出路径
	 */
	public void saveWorkbookAs(String saveFilePath) {
		OutputStream outStream = null;
		try {
			if (wb != null) {
				outStream = new FileOutputStream(saveFilePath);
				wb.write(outStream);
				outStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outStream != null)
					outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 当不再对Workbook进行读写时应该调用这个方法来销毁该对象
	 */
	public void closeWorkbook() {
		try {
			Workbook workBook = null;
			Collection<String> bookNames = workBookPool.keySet();
			Iterator<String> iter = bookNames.iterator();
			while (iter.hasNext()) {
				String bookname = iter.next();
				workBook = workBookPool.get(bookname);
				if (workBook != null) {
					workBookPool.remove(bookname);
					workBook.close();
				}
			}
			if (wb != null)
				wb.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 过滤字符串中的\t、\r、\n
	 * 
	 * @param sourceStr
	 *            原始字符串
	 * @return 返回格式化后的字符串
	 */
	public static String replaceBlank(String sourceStr) {
		String targetStr = "";
		if (sourceStr != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(sourceStr);
			targetStr = m.replaceAll("");
		}
		return targetStr;
	}

	/**
	 * 定义创建Workbook的类型
	 */
	public enum WorkbookTypeEnum {
		/**
		 * 定义枚举和枚举值
		 */
		EXCEL_XLS("XLS"), EXCEL_XLSX("XLSX");

		private WorkbookTypeEnum(String value) {
			this.value = value;
		}

		/**
		 * 枚举值
		 */
		private String value;

		/**
		 * 获取枚举值
		 * 
		 * @return 返回枚举值
		 */
		public String getValue() {
			return this.value;
		}
	}
}
