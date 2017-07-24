package com.example.jxls;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.expression.ExpressionEvaluator;
import org.jxls.transform.poi.PoiTransformer;

public class MergeCommand extends AbstractCommand {
    private Area area;
    private String row = "1";

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
        ;
    }

    @Override
    public String getName() {
        return "merge";
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        Size resultSize = area.applyAt(cellRef, context);

        int startCol = cellRef.getCol();
        int startRow = cellRef.getRow();

        ExpressionEvaluator evaluator = getTransformationConfig().getExpressionEvaluator();
        int rowCount = (int) evaluator.evaluate(row, context.toMap());

        PoiTransformer transformer = (PoiTransformer) area.getTransformer();
        Workbook workbook = transformer.getWorkbook();
        Sheet resultSheet = workbook.getSheet(cellRef.getSheetName());
        CellStyle referenceStyle = getCellStyle(cellRef, resultSheet);
        CellRangeAddress region = new CellRangeAddress(startRow, startRow + rowCount - 1, startCol, startCol);
        setRegionStyle(referenceStyle, region, resultSheet, workbook);
        resultSheet.addMergedRegion(region);
        return resultSize;
    }

    private CellStyle getCellStyle(CellRef cellRef, Sheet sheet) {
        CellStyle borderStyle = sheet.getWorkbook().createCellStyle();
        Row row = sheet.getRow(cellRef.getRow());
        Cell cell = row.getCell(cellRef.getCol());
        borderStyle.cloneStyleFrom(cell.getCellStyle());
        return borderStyle;
    }

    private void setRegionStyle(CellStyle style, CellRangeAddress region, Sheet sheet, Workbook workbook) {
        CellStyle referenceStyle = workbook.createCellStyle();
        referenceStyle.cloneStyleFrom(style);
        int firstRow = region.getFirstRow();
        int lastRow = region.getLastRow();
        int firstColumn = region.getFirstColumn();
        int lastColumn = region.getLastColumn();

        for (int i = firstRow; i <= lastRow; i++) {
            Row row = CellUtil.getRow(i, sheet);
            for (int j = firstColumn; j <= lastColumn; j++) {
                Cell cell = CellUtil.getCell(row, j);
                cell.setCellStyle(referenceStyle);
            }
        }
    }

    @Override
    public Command addArea(Area area) {
        super.addArea(area);
        this.area = area;
        return this;
    }
}
