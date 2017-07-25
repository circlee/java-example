package com.example.jxls;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.transform.poi.PoiCellData;
import org.jxls.transform.poi.PoiTransformer;

public class MergeCommand extends AbstractCommand {
    private Area area;
    private String rows;// 合并的行数
    private String cols;// 合并的列数
    private CellStyle cellStyle;// 第一个单元格的样式

    @Override
    public String getName() {
        return "merge";
    }

    @Override
    public Command addArea(Area area) {
        if (area == null) {
            return this;
        }
        if (super.getAreaList().size() >= 1) {
            throw new IllegalArgumentException("You can add only a single area to 'merge' command");
        }
        this.area = area;
        return super.addArea(area);
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        int rows = 1, cols = 1;

        if (this.rows != null) {
            rows = (int) getTransformationConfig().getExpressionEvaluator().evaluate(this.rows, context.toMap());
        }
        if (this.cols != null) {
            cols = (int) getTransformationConfig().getExpressionEvaluator().evaluate(this.cols, context.toMap());
        }
        if (rows > 1 || cols > 1) {
            mergeRegion(cellRef, context, (PoiTransformer) this.getTransformer(), rows, cols);
        }

        area.applyAt(cellRef, context);
        return new Size(cols, rows);
    }

    protected void mergeRegion(CellRef cellRef, Context context, PoiTransformer transformer, int rows, int cols) {
        Workbook workbook = transformer.getWorkbook();
        Sheet sheet = workbook.getSheet(cellRef.getSheetName());

        CellRangeAddress region = new CellRangeAddress(cellRef.getRow(), cellRef.getRow() + rows - 1, cellRef.getCol(), cellRef.getCol() + cols - 1);
        sheet.addMergedRegion(region);

        // 合并之后单元格样式会丢失，以下操作将合并后的单元格恢复成合并前第一个单元格的样式
        if (cellStyle == null) {
            PoiCellData cellData = (PoiCellData) transformer.getCellData(cellRef);
            if (cellData != null) {
                cellStyle = cellData.getCellStyle();
            }
        }
        if (cellStyle != null) {
            for (int startRow = region.getFirstRow(); startRow <= region.getLastRow(); startRow++) {
                Row row = sheet.getRow(startRow);
                if (row == null)
                    row = sheet.createRow(startRow);
                for (int startCol = region.getFirstColumn(); startCol <= region.getLastColumn(); startCol++) {
                    Cell cell = row.getCell(startCol);
                    if (cell == null)
                        cell = row.createCell(startCol);
                    cell.setCellStyle(cellStyle);
                }
            }
        }
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }
}
