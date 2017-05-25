package com.sersol.kbtools.bvt.pages;

import com.sersol.common.bvt.pages.PageRegistry;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.List;

public class LibraryGroupEditorPage extends HoldingImporterForm {
	private List<WebElement> libraryGroupsTableRows;

	/**
	 * Returns the row index identified by the specified Group Code in Library Groups table.
	 * Note: the first Group Code found is used.
	 * Returns -1 if not found.
	 *
	 * @param groupCode
	 * @return
	 */
	public int getRowIndexFor(String groupCode) {
		int groupCodeColumnIndex = getColumnIndexFor("Group Code");
		List<WebElement> rows = getLibraryGroupsTableRows();
		for (int i = 0; i < rows.size(); i++) {
			List<WebElement> cells = rows.get(i).findElements(By.xpath("./td"));
			WebElement row = cells.get(groupCodeColumnIndex);
			if (row.getText().equals(groupCode)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the column index identified by the header name in Group Code table
	 *
	 * @param header
	 * @return
	 */
	private int getColumnIndexFor(String header) {
		List<WebElement> headers = getLibraryGroupsHeaderRow().findElements(By.tagName("th"));

		for (int i = 0; i < headers.size(); i++) {
			if (headers.get(i).getText().equals(header)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Returns a list of "tr" elements excluding the header row.
	 * Return an empty list when there is only the header row present.
	 *
	 * @return
	 */
	private List<WebElement> getLibraryGroupsTableRows() {
		if (libraryGroupsTableRows == null) {
			List<WebElement> rows = utils.getTableRowsCollection("//*[@id='listedGroupsSpan']/table");
			if (rows.size() > 1) {
				libraryGroupsTableRows = new ArrayList<>(rows.subList(1, rows.size()));
			} else {
				libraryGroupsTableRows = new ArrayList<>();
			}
		}
		return libraryGroupsTableRows;
	}

	private WebElement getLibraryGroupsHeaderRow() {
		return driver.findElement(By.cssSelector("#listedGroupsSpan>table>tbody>tr"));
	}

	/**
	 * Gets the cell value in "Group Name" column identified by the 0 based row index in "Library Groups" table
	 *
	 * @param rowIndex
	 * @return
	 */
	public String getGroupName(int rowIndex) {
		return getCellValue(rowIndex, "Group Name").replace(" [u] [x]", "");
	}

	/**
	 * Gets the cell value in Libraries column identified by the 0 based row index in "Library Groups" table
	 *
	 * @param rowIndex
	 * @return
	 */
	public String getLibraries(int rowIndex) {
		return getCellValue(rowIndex, "Libraries");
	}

	/**
	 * Gets the cell value identified by the row index and header name.
	 * The header row is skipped.
	 *
	 * @param rowIndex
	 * @param header
	 * @return
	 */
	private String getCellValue(int rowIndex, String header) {
		if (getLibraryGroupsTableRowCount() == 0) {
			return "LIBRARY GROUPS TABLE IS EMPTY";
		}

		int columnIndex = getColumnIndexFor(header);
		return columnIndex == -1 ? "SPECIFIED HEADER NOT FOUND" :
				getLibraryGroupsTableRows().get(rowIndex).findElements(By.xpath("./td")).get(columnIndex).getText();
	}

	/**
	 * Gets the number of rows in Library Groups table excluding the header row
	 *
	 * @return
	 */
	public int getLibraryGroupsTableRowCount() {
		return getLibraryGroupsTableRows().size();
	}

	/**
	 * Sets the Group Code in the Group Code field
	 *
	 * @param groupCode
	 */
	public void setGroupCode(String groupCode) {
		updateField(driver.findElement(By.cssSelector("#createGroup #gcode")), groupCode);
	}

	/**
	 * Sets Group Name in the Group Name field in the Create New Group form
	 *
	 * @param groupName
	 */
	public void createGroupName(String groupName) {
		updateField(driver.findElement(By.cssSelector("#createGroup #name")), groupName);
	}

	/**
	 * Sets Library Code(s) in the Library Codes field in the Create New Group form
	 *
	 * @param libraryCodes
	 */
	public void createLibraryCodes(String libraryCodes) {
		updateField(driver.findElement(By.cssSelector("#createGroup #codes")), libraryCodes);
	}

	/**
	 * Clicks Create Group button
	 */
	public void clickCreateGroupButton() {
		driver.findElement(By.cssSelector("#createGroup #submit")).click();
	}

	/**
	 * Clicks Update Link "[u]" in the specified row of the Library Groups table
	 *
	 * @param rowIndex
	 */
	public void clickUpdateLink(int rowIndex) {
		getTableCell(rowIndex, 1).findElement(By.linkText("[u]")).click();
	}

	private WebElement getTableCell(int rowIndex, int colIndex) {
		return getLibraryGroupsTableRows().get(rowIndex).findElements(By.xpath("./td")).get(colIndex);
	}

	/**
	 * Enters Library Code(s) into the Library Codes field in the Library Group Editor form
	 *
	 * @param libraryCodes
	 */
	public void updateLibraryCodes(String libraryCodes) {
		updateField(driver.findElement(By.cssSelector("#updateandremove #codes")), libraryCodes);
	}

	/**
	 * Enters Library Group Name into the Group Name field in the Library Group Editor form
	 *
	 * @param groupName
	 */
	public void updateGroupName(String groupName) {
		updateField(driver.findElement(By.cssSelector("#updateandremove #name")), groupName);
	}

	/**
	 * Clicks the "Update XXX" button in the Library Group Editor
	 */
	public void clickUpdateButton() {
		driver.findElement(By.cssSelector("#updateSubmit")).click();
	}

	/**
	 * Clicks Delete Link "[x]" in the specified row of the Library Groups table
	 */
	public void clickDeleteLink(int rowIndex) {
		getTableCell(rowIndex, 1).findElement(By.linkText("[x]")).click();

	}

	/**
	 * Refreshes Library Group Editor page by navigating to Main page and back.
	 *
	 * @param lgep
	 * @return
	 */
	public LibraryGroupEditorPage refreshLibraryGroupEditorPage(LibraryGroupEditorPage lgep) {
		lgep.clickMainMenu();
		return PageRegistry.get(HomePage.class).selectLibraryGroupsLink();
	}
}
