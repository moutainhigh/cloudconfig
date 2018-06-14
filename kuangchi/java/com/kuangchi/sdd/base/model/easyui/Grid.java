package com.kuangchi.sdd.base.model.easyui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid<T> implements Serializable {

	private static final long serialVersionUID = -4272928035485436712L;

	private int total = 0;
	private List<T> rows = new ArrayList<T>();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getRows() {
		return Collections.unmodifiableList(rows);
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}