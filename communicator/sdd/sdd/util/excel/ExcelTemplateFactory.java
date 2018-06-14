package com.kuangchi.sdd.util.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExcelTemplateFactory {

	public static File createTemplate(String templateType) throws IOException {

		File file = null;

		if (templateType.equals("hardware")) {
			file = new File(System.getProperty("java.io.tmpdir"), UUID
					.randomUUID().toString() + ".xls");

			ExcelExportTemplate e = new ExcelExportTemplate();

			List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();

			TitleRowCell tOnet = new TitleRowCell("硬件编号", true);

			titleRowCells.add(tOnet);

			TitleRowCell tOneQ = new TitleRowCell("硬件名称", true);

			titleRowCells.add(tOneQ);

			e.createTitleRow(titleRowCells);

			try {
				e.writeToFile(file);
			} catch (IOException ee) {
				if (file != null) {
					file.delete();
				}

				throw ee;
			}

		}

		return file;
	}

}
