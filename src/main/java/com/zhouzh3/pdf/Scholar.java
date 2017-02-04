package com.zhouzh3.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.SimpleBookmark;

public class Scholar {

	public static void main(String[] args) throws IOException, DocumentException {
		// printBookmark("src/main/resources/Packt.Mastering.Hadoop.1783983647.pdf");

		String SRC = "src/main/resources/Hive编程指南.pdf";

		Scholar scholar = new Scholar();
		scholar.bookmark(SRC, 21);

	}

	public List<HashMap<String, Object>> readBookmark(String file, int step) throws IOException {
		return readBookmark(new File(file), step);
	}

	public List<HashMap<String, Object>> readBookmark(File file, int step) throws IOException {
		ArrayList<HashMap<String, Object>> retVal = new ArrayList<HashMap<String, Object>>();

		List<String> readLines = readLines(file, "UTF-8");
		for (String line : readLines) {
			Bookmark bookmark = Bookmark.buildBookmark(line, step);
			if (bookmark != null) {
				int level = bookmark.getLevel();
				// System.out.println(bookmark.getTitle() + "== " +
				// bookmark.getLevel());
				ArrayList<HashMap<String, Object>> findParent = findParent(retVal, level);
				findParent.add(bookmark.getEntry());
			}
		}

		return retVal;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<HashMap<String, Object>> findParent(ArrayList<HashMap<String, Object>> retVal, int level) {
		ArrayList<HashMap<String, Object>> parent = retVal;
		for (int i = 1; i < level; i++) {
			// System.out.println("parent.size(): " + parent.size());
			HashMap<String, Object> hashMap = parent.get(parent.size() - 1);

			if (hashMap.containsKey("Kids")) {
				parent = (ArrayList<HashMap<String, Object>>) hashMap.get("Kids");
			} else {
				parent = new ArrayList<HashMap<String, Object>>();
				hashMap.put("Kids", parent);
			}
		}

		return parent;

	}

	public List<String> readLines(String fileName) throws IOException {
		return readLines(new File(fileName), "UTF-8");
	}

	private List<String> readLines(File file, String charset) throws IOException {
		List<String> retVal = new ArrayList<String>();

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.trim().length() > 0) {
				retVal.add(line);
			}
		}
		br.close();
		return retVal;
	}

	public void printBookmark(String fileName) throws IOException {
		String SRC = fileName;
		PdfReader reader = new PdfReader(SRC);

		List<HashMap<String, Object>> bookmark = SimpleBookmark.getBookmark(reader);
		for (HashMap<String, Object> hashMap : bookmark) {
			for (String key : hashMap.keySet()) {
				System.out.println("【" + key + "】, 【" + hashMap.get(key) + "】");
			}
			System.out.println("==================================");
		}

		reader.close();
	}

	protected void bookmark(String pdfFileName, int step) throws IOException, DocumentException {
		bookmark(pdfFileName, getBookmarkFileName(pdfFileName), step);
	}

	protected void bookmark(String pdfFileName, String bookmarkFileName, int step)
			throws IOException, DocumentException {
		File DEST = getDest(pdfFileName);
		PdfReader reader = new PdfReader(pdfFileName);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(DEST));

		List<HashMap<String, Object>> outlines = readBookmark(bookmarkFileName, step);

		stamper.setOutlines(outlines);
		stamper.close();
	}

	private String getBookmarkFileName(String fileName) {
		File file = new File(fileName);
		return new File(file.getParent(), file.getName().replaceAll("\\.\\w+$", ".txt")).getAbsolutePath();
	}

	private File getDest(String fileName) {
		File file = new File(fileName);
		return new File(file.getParent() + "/results/",
				file.getName().replaceAll("\\.\\w+$", "_" + System.currentTimeMillis() + "$0"));
	}

}
