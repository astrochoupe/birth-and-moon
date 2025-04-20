import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Generate a CSV file from the Excel file of the INSEE (french organisation of
 * statistics).
 */
public class XlsInseeToCsv {

	private static final String EXCEL_FILENAME = "T79JNAIS.xlsx";
	private static final String CSV_FILENAME = "birthsByDate.csv";

	private static final int MIN_ROW = 5;
	private static final int MAX_ROW = 629;
	private static final int MIN_COL = 3;
	private static final int MAX_COL = 34;
	private static final int DAY_ROW = 3;
	private static final int MONTH_COL = 1;
	private static final int YEAR_COL = 0;

	/**
	 * Create a CSV file from the Excel file of the INSEE.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {

		InputStream is = Main.class.getClassLoader().getResourceAsStream(
				EXCEL_FILENAME);

		try (Workbook workbook = new XSSFWorkbook(is);
				FileWriter fileWriter = new FileWriter(CSV_FILENAME);
				PrintWriter printWriter = new PrintWriter(fileWriter);) {

			Sheet sheet = workbook.getSheetAt(0);

			System.out.println("DEBUT FICHIER");

			String year = "";

			for (int rownum = MIN_ROW; rownum < MAX_ROW; rownum++) {
				System.out.println("DEBUT LIGNE");

				Cell yearCell = sheet.getRow(rownum).getCell(YEAR_COL);
				if (!yearCell.getStringCellValue().isEmpty()) {
					year = yearCell.getStringCellValue();
				}
				System.out.print(year);

				Cell monthCell = sheet.getRow(rownum).getCell(MONTH_COL);
				String monthName = monthCell.getStringCellValue();
				String monthNum = getMonthNumFromFrenchName(monthName);
				System.out.println("-" + monthNum);

				for (int colnum = MIN_COL; colnum < MAX_COL; colnum++) {
					Cell dayInMonthCell = sheet.getRow(DAY_ROW).getCell(colnum);
					String dayInMonth = dayInMonthCell.getStringCellValue();

					Cell cell = sheet.getRow(rownum).getCell(colnum);

					if (cell.getCellType() == CellType.NUMERIC) {
						System.out.println(dayInMonth + ":"
								+ (int) cell.getNumericCellValue());
						printWriter.println(year + "-" + monthNum + "-"
								+ dayInMonth + ","
								+ (int) cell.getNumericCellValue());
					}

				}
			}
			
			System.out.println("FIN FICHIER");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getMonthNumFromFrenchName(String frenchNameOfMonth) {
		switch (frenchNameOfMonth) {
		case "Janvier":
			return "01";
		case "Février":
			return "02";
		case "Mars":
			return "03";
		case "Avril":
			return "04";
		case "Mai":
			return "05";
		case "Juin":
			return "06";
		case "Juillet":
			return "07";
		case "Août":
			return "08";
		case "Septembre":
			return "09";
		case "Octobre":
			return "10";
		case "Novembre":
			return "11";
		case "Décembre":
			return "12";
		default:
			return "ERROR";
		}
	}
}
