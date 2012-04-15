package pet.ui.ta;

import java.awt.Color;
import java.awt.Font;
import java.util.*;

import javax.swing.table.AbstractTableModel;

public class MyTableModel<T> extends AbstractTableModel {

	private final List<MyColumn<T>> cols = new ArrayList<MyColumn<T>>();

	private final List<T> rows = new ArrayList<T>();

	private final List<MyColumn<T>> allcols;

	public MyTableModel(List<MyColumn<T>> cols) {
		this(cols, cols);
	}

	public MyTableModel(List<MyColumn<T>> allcols, List<MyColumn<T>> cols) {
		this.allcols = allcols;
		this.cols.addAll(cols);
	}

	public List<MyColumn<T>> getAllColumns() {
		return allcols;
	}

	public boolean getAllColumn(int allc) {
		return cols.contains(allcols.get(allc));
	}

	public void setAllColumn(int allc) {
		MyColumn<T> mycol = allcols.get(allc);
		System.out.println("toggle column " + mycol.name);
		if (cols.contains(mycol)) {
			cols.remove(mycol);
		} else {
			// TODO add at proper place
			cols.add(mycol);
		}
		fireTableStructureChanged();
	}

	/**
	 * Get the population T for the given T
	 */
	public T getPopulation(T row) {
		return null;
	}

	public void setRows(Collection<T> rows) {
		this.rows.clear();
		this.rows.addAll(rows);
		fireTableStructureChanged();
		// doesn't seem to work
		fireTableDataChanged();
		//System.out.println("table model rows now " + rows);
	}

	public T getRow(int r) {
		return rows.get(r);
	}

	@Override
	public int getColumnCount() {
		return cols.size();
	}

	@Override
	public String getColumnName(int c) {
		return cols.get(c).name;
	}

	@Override
	public int getRowCount() {
		//System.out.println("get rows " + this.getClass() + " => " + rows.size());
		return rows.size();
	}

	@Override
	public Class<?> getColumnClass(int c) {
		return cols.get(c).cl;
	}

	@Override
	public Object getValueAt(int r, int c) {
		if (r < rows.size()) {
			T row = rows.get(r);
			MyColumn<T> col = cols.get(c);
			Object val = col.getValue(row);
			if (val != null && val.getClass() != col.cl) {
				throw new RuntimeException("wrong class: " + val + " is not " + col.cl);
			}
			return val;
		}
		return null;
	}

	public String getToolTip(int r, int c) {
		//System.out.println("get tool tip for row " + r);
		if (r < rows.size()) {
			T row = rows.get(r);
			//System.out.println("row is " + row);
			MyColumn<T> col = cols.get(c);
			StringBuilder sb = new StringBuilder();
			sb.append("<html><b>").append(col.desc).append("</b>");
			sb.append("<br>").append(col.getValue(row));
			String tip = col.getToolTip(row);
			if (tip != null && tip.length() > 0) {
				sb.append("<br>").append(tip).append("</br>");
			}
			T population = getPopulation(row);
			if (population != null) {
				sb.append("<br><i>").append(col.getPopValue(population)).append(" (population)</i>");
			}
			sb.append("<br>(").append(r).append(",").append(c).append(")");
			sb.append("</html>");
			return sb.toString();
		}
		return null;
	}

	public Color getColour(int r, int c) {
		if (r < rows.size()) {
			T row = rows.get(r);
			//System.out.println("row is " + row);
			MyColumn<T> col = cols.get(c);
			return col.getColour(row);
		}
		return null;
	}

	public Font getFont(int r, int c) {
		if (r < rows.size()) {
			T row = rows.get(r);
			//System.out.println("row is " + row);
			MyColumn<T> col = cols.get(c);
			return col.getFont(row);
		}
		return null;
	}

}