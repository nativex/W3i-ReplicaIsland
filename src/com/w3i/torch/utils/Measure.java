package com.w3i.torch.utils;

import android.util.SparseArray;

import com.w3i.common.Log;

public class Measure {
	private static Measure instance = null;
	private SparseArray<MeasureItem<?>> items;
	public static int id = 0;

	private Measure() {
		items = new SparseArray<Measure.MeasureItem<?>>();
	}

	private static void checkInstance() {
		if (instance == null) {
			instance = new Measure();
		}
	}

	public static int startMeasure(
			String name,
			float initialValue) {
		checkInstance();
		MeasureItem<Float> item = instance.new MeasureItem<Float>();
		item.name = name;
		item.setValue(initialValue);
		id++;
		instance.items.put(id, item);
		return id;
	}

	public static void addMeasure(
			int id,
			float newValue) {
		if (instance == null) {
			return;
		}
		MeasureItem<Float> item = (MeasureItem<Float>) instance.items.get(id);
		item.value += newValue;
	}

	public static void finishMeasure(
			int id) {
		if (instance == null) {
			return;
		}
		MeasureItem<Float> item = (MeasureItem<Float>) instance.items.get(id);
		float value = item.value;
		Log.d("Measure: " + item.name + " : " + item.value);
		clearItem(id, item);
	}

	private static void clearItem(
			int id,
			MeasureItem<?> item) {
		if (instance == null) {
			return;
		}
		instance.items.delete(id);
		if (instance.items.size() == 0) {
			instance = null;
		}
	}

	class MeasureItem<T> {

		private String name;
		private T value;

		public void setValue(
				T value) {
			this.value = value;
		}

		public T getValue(
				T value) {
			return value;
		}

	}
}
