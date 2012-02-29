package com.jxls.writer.common;

import com.jxls.writer.util.CellRefUtil;

/**
 * @author Leonid Vysochyn
 *         Date: 1/25/12 1:42 PM
 */
public class CellRef {

    int col;
    int row;

    String sheetName;
    boolean isColAbs;
    boolean isRowAbs;
    boolean ignoreSheetNameInFormat = false;

    public CellRef(String sheetName, int row, int col) {
        this.sheetName = sheetName;
        this.row = row;
        this.col = col;
    }

    public CellRef(int row, int col) {
        this(null, row, col);
    }
    
    public CellRef(String cellRef){
        if(cellRef.endsWith("#REF!")) {
            throw new IllegalArgumentException("Cell reference invalid: " + cellRef);
        }

        String[] parts = CellRefUtil.separateRefParts(cellRef);
        sheetName = parts[0];
        String colRef = parts[1];
        if (colRef.length() < 1) {
            throw new IllegalArgumentException("Invalid Formula cell reference: '"+cellRef+"'");
        }
        isColAbs = colRef.charAt(0) == '$';
        if (isColAbs) {
            colRef=colRef.substring(1);
        }
        col = CellRefUtil.convertColStringToIndex(colRef);

        String rowRef=parts[2];
        if (rowRef.length() < 1) {
            throw new IllegalArgumentException("Invalid Formula cell reference: '"+cellRef+"'");
        }
        isRowAbs = rowRef.charAt(0) == '$';
        if (isRowAbs) {
            rowRef=rowRef.substring(1);
        }
        row = Integer.parseInt(rowRef)-1; // -1 to convert 1-based to zero-based
    }

    public String getCellName(){
        StringBuffer sb = new StringBuffer(32);
        if(sheetName != null && !ignoreSheetNameInFormat) {
            CellRefUtil.appendFormat(sb, sheetName);
            sb.append(CellRefUtil.SHEET_NAME_DELIMITER);
        }
        appendCellReference(sb);
        return sb.toString();
    }

    public String getFormattedSheetName(){
        StringBuffer sb = new StringBuffer(32);
        CellRefUtil.appendFormat(sb, sheetName);
        return sb.toString();
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public boolean isIgnoreSheetNameInFormat() {
        return ignoreSheetNameInFormat;
    }

    public void setIgnoreSheetNameInFormat(boolean ignoreSheetNameInFormat) {
        this.ignoreSheetNameInFormat = ignoreSheetNameInFormat;
    }

    /**
     * Appends cell reference with '$' markers for absolute values as required.
     * Sheet name is not included.
     */
    void appendCellReference(StringBuffer sb) {
        if(isColAbs) {
            sb.append(CellRefUtil.ABSOLUTE_REFERENCE_MARKER);
        }
        sb.append( CellRefUtil.convertNumToColString(col));
        if(isRowAbs) {
            sb.append(CellRefUtil.ABSOLUTE_REFERENCE_MARKER);
        }
        sb.append(row+1);
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellRef cellRef = (CellRef) o;

        if (col != cellRef.col) return false;
        if (row != cellRef.row) return false;
        if (sheetName != null ? !sheetName.equals(cellRef.sheetName) : cellRef.sheetName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = col;
        result = 31 * result + row;
        result = 31 * result + (sheetName != null ? sheetName.hashCode() : 0);
        return result;
    }
    
    public String toString(boolean ignoreSheetName){
        boolean currentIgnoreSheetValue = ignoreSheetNameInFormat;
        ignoreSheetNameInFormat = ignoreSheetName;
        String result = getCellName();
        ignoreSheetNameInFormat = currentIgnoreSheetValue;
        return result;
    }

    @Override
    public String toString() {
        return getCellName();
    }

}
