package com.example.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SharedPreferenceDB {
	
	public static List<String> getList(Context context){
		String AREA3 = context.getSharedPreferences("AREA",
				Activity.MODE_PRIVATE).getString("AREA3", "");
		String[] item_area3 = AREA3.split("[|]");

		List<String> item_area31 = new ArrayList<String>();

		for (int i = 0; i < item_area3.length; i++) {
			item_area31.add(item_area3[i]);
		}

		return item_area31;
	}
	
    public static int getcount(Context context){
		String AREA3 = context.getSharedPreferences("AREA",
				Activity.MODE_PRIVATE).getString("AREA3", "");

		String[] item_area3 = AREA3.split("[|]");
		

		return item_area3.length;

    }
	public static boolean isChecked(String str, Context context) {
		Boolean flag = false;
		String AREA3 = context.getSharedPreferences("AREA",
				Activity.MODE_PRIVATE).getString("AREA3", "");

		String[] item_area3 = AREA3.split("[|]");
		for (int i = 0; i < item_area3.length; i++) {
			if (item_area3[i].equals(str)) {
				flag = true;
			}
		}

		return flag;
	}

	public static void deleteSp(String str, Context context) {

		String AREA3 = context.getSharedPreferences("AREA",
				Activity.MODE_PRIVATE).getString("AREA3", "");
		String AREA31 = context.getSharedPreferences("AREA",
				Activity.MODE_PRIVATE).getString("AREA31", "");

		String[] item_area3 = AREA3.split("[|]");
		List<String> item_area31 = new ArrayList<String>();
		String[] item_area32 = AREA31.split("[,]");
		List<String> item_area33 = new ArrayList<String>();
		for (int i = 0; i < item_area32.length; i++) {
			item_area33.add(item_area32[i]);
		}

		for (int i = 0; i < item_area3.length; i++) {
			item_area31.add(item_area3[i]);
		}
		for (int i = 0; i < item_area31.size(); i++) {
			if (item_area31.get(i).equals(str)) {
				item_area31.remove(i);
				item_area33.remove(i);
			}
		}

		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		if (item_area31 != null && item_area31.size() > 0) {
			for (int j = 0; j < item_area31.size(); j++) {
				if (j < item_area31.size() - 1) {
					sb.append(item_area31.get(j) + "|");
				} else {
					sb.append(item_area31.get(j));
				}
				if (j < item_area33.size() - 1) {
					sb1.append(item_area33.get(j) + ",");
				} else {
					sb1.append(item_area33.get(j));
				}

			}
		}

		String area32 = sb.toString();
		String area33 = sb1.toString();
		SharedPreferences mySharedPreferences1 = context.getSharedPreferences(
				"AREA", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor1 = mySharedPreferences1.edit();
		if (TextUtils.isEmpty(area32)) {
			editor1.putString("AREA3", "");
			editor1.putString("AREA31", "");
		} else {
			editor1.putString("AREA3", area32);
			editor1.putString("AREA31", area33);
		}
		editor1.commit();

	}

	public static void addSp(String str, Context context, String str1) {
		SharedPreferences mySharedPreferences1 = context.getSharedPreferences(
				"AREA", Activity.MODE_PRIVATE);
		String AREA3 = context.getSharedPreferences("AREA",
				Activity.MODE_PRIVATE).getString("AREA3", "");
		// 存放回显的文字
		String AREA31 = context.getSharedPreferences("AREA",
				Activity.MODE_PRIVATE).getString("AREA31", "");
		SharedPreferences.Editor editor1 = mySharedPreferences1.edit();

		if (TextUtils.isEmpty(AREA3)) {
			editor1.putString("AREA3", str);
			editor1.putString("AREA31", str1);
		} else {
			editor1.putString("AREA3", AREA3 + "|" + str);
			editor1.putString("AREA31", AREA31 + "," + str1);
		}

		editor1.commit();

	}

}
