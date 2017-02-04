package com.zhouzh3.pdf;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Hegel
 *
 */
public class Bookmark {

	private String title;

	private int page;

	private String action = "GoTo";

	private ArrayList<HashMap<String, Object>> kids;

	public Bookmark() {
		this.kids = new ArrayList<HashMap<String, Object>>();
	}

	public Bookmark(String title, int page) {
		this();
		this.title = title;
		this.page = page;

	}

	public HashMap<String, Object> getEntry() {
		HashMap<String, Object> entry = new HashMap<String, Object>();
		if (title != null && !"".equals(title)) {
			entry.put("Title", title);
			entry.put("Action", "GoTo");
			entry.put("Page", page + " FitH 60");
//			entry.put("Page", page + " Fit");
			if (kids.size() != 0) {
				entry.put("Kids", kids);
			}
		}

		return entry;
	}

	public Bookmark addKid(HashMap<String, Object> entry) {
		this.kids.add(entry);

		return this;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public ArrayList<HashMap<String, Object>> getKids() {
		return kids;
	}

	@Override
	public String toString() {
		return "Bookmark [title=" + title + ", page=" + page + ", action=" + action + "]";
	}

	public int getLevel() {
		int retVal = 0;
		String[] split = this.title.split("[　 ]+", 2);
		if (split.length == 2) {
			String section = split[0];
			retVal = section.split("\\.").length;
		}
		return retVal;
	}

	public static Bookmark buildBookmark(String line, int step) {
		String[] split = line.split("　(?=\\d)");
		if (split.length == 2) {
			return new Bookmark(split[0], Integer.parseInt(split[1]) + step);
		}

		System.err.println("参数不正确: 【" + line + "】");
		return null;
	}

}
