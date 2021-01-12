package net.guides.springboot2.crud.dto;

import java.util.ArrayList;
import java.util.List;

public class LuniCuSalarii {
	private int an;
	private List<Integer> luna;

	public LuniCuSalarii() {
	}

	public LuniCuSalarii(int an) {
		this.an = an;
		this.luna = new ArrayList<>();
	}

	public LuniCuSalarii(List<Integer> luna, int an) {
		this.luna = luna;
		this.an = an;
	}

	public void addLuna(Integer luna) {
		this.luna.add(luna);
	}

	public int getAn() {
		return an;
	}

	public void setAn(int an) {
		this.an = an;
	}

	public List<Integer> getLuna() {
		return luna;
	}

	public void setLuna(List<Integer> luna) {
		this.luna = luna;
	}
}