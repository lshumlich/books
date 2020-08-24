// Type vnd: Vendor Specific Files [8] 
// application/vnd.ms-excel: Microsoft Excel files 
// application/vnd.ms-powerpoint: Microsoft Powerpoint files 
// application/msword: Microsoft Word files 
// application/vnd.mozilla.xul+xml: Mozilla XUL files 
//

package com.pe.books;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LoadExcel extends TestCase {

    private BooksManager booksManager = BooksManager.getInstance();

    public void testLoadExcel() throws FileNotFoundException, IOException {
        System.out.println("Load an Excel file.");
        File file = new File("C:/lfs/Budget/Sample Excel Load.xls");
        //File file = new File("C:/lfs/Budget/$DataLoad.xls");
        this.process(file, new PrintWriter(System.out));
        
        //PrintStream ps;
        //PrintWriter pw;
        
        //PrintWriter pw2 = new PrintWriter(ps);
        
    }

    public void process(File file, PrintWriter out) throws FileNotFoundException, IOException {

//    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = null;

        sheet = wb.getSheet("Account");
        if (sheet != null) {
            out.println("");
            out.println("Processing Account Load.");
            processAccounts(sheet, out);
        } else
            out.println("There was no 'Account' Sheet.");

        sheet = wb.getSheet("Entry");
        if (sheet != null) {
            out.println("");
            out.println("Processing Entry Load.");
            processEntrys(sheet, out);
        } else
            out.println("There was no 'Entry' Sheet.");

        sheet = wb.getSheet("Account Update");
        if (sheet != null) {
            out.println("");
            out.println("Processing Account Update.");
            processAccountUpdates(sheet, out);
        } else
            out.println("There was no 'Account' Sheet.");
        
        transaction.commit();

    }

    public void processAccountUpdates(HSSFSheet sheet, PrintWriter out) {
        HashMap<String, Integer> colNumber = new HashMap<String, Integer>();
        Iterator<HSSFRow> cit = sheet.rowIterator();
        int rowNumber = 0;
        HSSFRow row = cit.next();
        rowNumber++;
        int col = 0;
        Iterator<HSSFCell> rit = row.cellIterator();
        while (rit.hasNext()) {
            colNumber.put(rit.next().getRichStringCellValue().toString(), new Integer(col));
            col++;
        }
        int excelRowsLoaded = 0;
        int excelRowErrors = 0;
        String code = null;
        AccountDto accountDto = null;
        Account account = null;
        String colName = null;
        while (cit.hasNext()) {
            row = cit.next();
            rowNumber++;
            try {
                code = (this.getCellValue(row, colNumber, "Code"));
                if (code == null) {
                    throw new EditException("Account Code must be entered to do an update on row " + rowNumber);
                }
                account = booksManager.getAccountByCode(code);
                if (account == null) {
                    throw new EditException("Code: " + code + " not a valid account in row " + rowNumber);
                }
                accountDto = new AccountDto(account);

                colName = "Order";
                if (colNumber.containsKey(colName))
                    accountDto.setDisplayOrder(this.getIntCellValue(row, colNumber, colName));

                colName = "Description";
                if (colNumber.containsKey(colName))
                    accountDto.setDescription(this.getCellValue(row, colNumber, colName));

                colName = "Balance";
                if (colNumber.containsKey(colName))
                    accountDto.setCurrentBalance(this.getCellValue(row, colNumber, colName));

                colName = "Budget";
                if (colNumber.containsKey(colName))
                    accountDto.setBudget(this.getCellValue(row, colNumber, colName));

                colName = "Type";
                if (colNumber.containsKey(colName))
                    accountDto.setType(this.getCellValue(row, colNumber, colName));

                colName = "Active";
                if (colNumber.containsKey(colName))
                    accountDto.setActive(this.getCellValue(row, colNumber, colName));

                booksManager.addOrUpdateAccountDto(accountDto);
                excelRowsLoaded++;
            } catch (EditException editErrors) {
                excelRowErrors++;
                out.println("Error row " + rowNumber + editErrors.getMessage());
                out.println("Error with: " + accountDto);
                Iterator<ErrorMessage> it = editErrors.getMessages().iterator();
                while (it.hasNext()) {
                    out.println("   " + it.next());
                }
            }
        }
        out.println("Accounts: --- " + excelRowsLoaded + " rows Loaded. " + excelRowErrors
                + " rows in Error.***");
    }

    public void processAccounts(HSSFSheet sheet, PrintWriter out) {
        HashMap<String, Integer> colNumber = new HashMap<String, Integer>();
        Iterator<HSSFRow> cit = sheet.rowIterator();
        HSSFRow row = cit.next();
        int col = 0;
        Iterator<HSSFCell> rit = row.cellIterator();
        while (rit.hasNext()) {
            colNumber.put(rit.next().getRichStringCellValue().toString(), new Integer(col));
            col++;
        }
        int excelRowsLoaded = 0;
        int excelRowErrors = 0;
        AccountDto accountDto = new AccountDto();
        while (cit.hasNext()) {
            row = cit.next();
            accountDto.setCode(this.getCellValue(row, colNumber, "Code"));
            accountDto.setDisplayOrder(this.getIntCellValue(row, colNumber, "Order"));
            accountDto.setDescription(this.getCellValue(row, colNumber, "Description"));
            accountDto.setCurrentBalance(this.getCellValue(row, colNumber, "Balance"));
            accountDto.setBudget(this.getCellValue(row, colNumber, "Budget"));
            accountDto.setType(this.getCellValue(row, colNumber, "Type"));
            accountDto.setActive("true");

            try {
                booksManager.addOrUpdateAccountDto(accountDto);
                excelRowsLoaded++;
            } catch (EditException editErrors) {
                excelRowErrors++;
                out.println("Error with: " + accountDto);
                Iterator<ErrorMessage> it = editErrors.getMessages().iterator();
                while (it.hasNext()) {
                    out.println("   " + it.next());
                }
            }
        }
        out.println("Accounts: --- " + excelRowsLoaded + " rows Loaded. " + excelRowErrors
                + " rows in Error.***");
    }

    public void processEntrys(HSSFSheet sheet, PrintWriter out) {
        HashMap<String, Integer> colNumber = new HashMap<String, Integer>();
        Iterator<HSSFRow> cit = sheet.rowIterator();
        HSSFRow row = cit.next();
        int col = 0;
        Iterator<HSSFCell> rit = row.cellIterator();
        while (rit.hasNext()) {
            colNumber.put(rit.next().getRichStringCellValue().toString(), new Integer(col));
            col++;
        }

        int excelRowsLoaded = 0;
        int excelRowErrors = 0;
        //Entry entry = null;
        EntryDto entryDto = new EntryDto();
        while (cit.hasNext()) {
            row = cit.next();
            entryDto.setEntryId(this.getCellValue(row, colNumber, "EntryId"));
            entryDto.setDate(this.getDateCellValue(row, colNumber, "Date"));
            entryDto.setDrCode(this.getCellValue(row, colNumber, "Debit"));
            entryDto.setDrClear(this.getCellValue(row, colNumber, "Debit Clear"));
            entryDto.setCrCode(this.getCellValue(row, colNumber, "Credit"));
            entryDto.setCrClear(this.getCellValue(row, colNumber, "Credit Clear"));
            entryDto.setDescription(this.getCellValue(row, colNumber, "Description"));
            entryDto.setAmount(this.getCellValue(row, colNumber, "Amount"));
            entryDto.setCheckNo(this.getIntCellValue(row, colNumber, "Check Number"));

            try {
                booksManager.addOrUpdateEntryDto(entryDto);
                excelRowsLoaded++;
            } catch (EditException editErrors) {
                excelRowErrors++;
                out.println("Error with: " + entryDto);
                Iterator<ErrorMessage> it = editErrors.getMessages().iterator();
                while (it.hasNext()) {
                    out.println("   " + it.next());
                }
            }
        }
        out.println("Entrys: --- " + excelRowsLoaded + " rows Loaded. " + excelRowErrors + " rows in Error.***");
    }

    private String getCellValue(HSSFRow row, HashMap<String, Integer> columnMap, String columnName) {
        if (row == null)
            return null;

        Integer columnLocation = columnMap.get(columnName);
        if (columnLocation == null)
            return null;

        HSSFCell cell = row.getCell(columnLocation.intValue());
        if (cell == null)
            return null;

        return cell.toString();
    }

    private String getDateCellValue(HSSFRow row, HashMap<String, Integer> columnMap, String columnName) {
        if (row == null)
            return null;

        Integer columnLocation = columnMap.get(columnName);
        if (columnLocation == null)
            return null;

        HSSFCell cell = row.getCell(columnLocation.intValue());
        if (cell == null)
            return null;

        java.util.Date d = cell.getDateCellValue();
        Date date = new Date(d.getTime());
        return date.toString();
    }

    private String getIntCellValue(HSSFRow row, HashMap<String, Integer> columnMap, String columnName) {
        if (row == null)
            return null;

        Integer columnLocation = columnMap.get(columnName);
        if (columnLocation == null)
            return null;

        HSSFCell cell = row.getCell(columnLocation.intValue());
        if (cell == null)
            return null;

        return "" + new BigDecimal(cell.toString()).intValue();
    }
}