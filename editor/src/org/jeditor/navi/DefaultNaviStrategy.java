/* ソースツリー文字コード識別用文字列ソースツリー文字コード識別用文字列 */
package org.jeditor.navi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultNaviStrategy implements NaviStrategy {

	String caller = "";

	// 状態(String)と押したボタン(String)から次の状態(String)を導く
	Map<List<String>, String> strategy;

	//@formatter:off
	static String[][] STATECHANGE = {
		{"a1","ある","a2"},
		{"a1","ない","a5"},
		{"a2","存在する","a6"},
		{"a2","存在しない","a3"},
		{"a3","正しい","a4"},
		{"a3","誤り","a7"},
		{"a4","正しい","e2"},
		{"a4","誤り","e1"},
		{"a5","if文","b1"},
		{"a5","for文","r1"},
		{"a5","次へ","e2"},
		{"a6","if文","b1"},
		{"a6","for文","r1"},
		{"a6","次へ","a3"},
		{"a7","正しい","a1"},
		{"a7","誤り","e1"},
		{"e1","終了","t"},
		{"e2","終了","t"},
		{"t","スタート","a1"},
		{"r1","はい","r2"},
		{"r1","いいえ","r4"},
		{"r2","正しい","return"},
		{"r2","誤り","r3"},
		{"r3","終了","t"},
		{"r4","正しい","r6"},
		{"r4","誤り","r5"},
		{"r5","正しい","a1"},
		{"r5","誤り","e2"},
		{"r6","正しい","return"},
		{"r6","誤り","e2"},
		{"b1","正しい","return"},
		{"b1","誤り","b2"},
		{"b2","正しい","b4"},
		{"b2","誤り","b3"},
		{"b3","正しい","a1"},
		{"b3","誤り","e2"},
		{"b4","正しい","e1"},
		{"b4","誤り","e2"},

		};
	//@formatter:on

	public DefaultNaviStrategy() {
		strategy = new HashMap<>();
		for (String[] sc : STATECHANGE) {
			strategy.put(Arrays.asList(sc[0], sc[1]), sc[2]);
		}
	}

	@Override
	public String getNextNavi(String currentState, String buttonLabel) {
		List<String> tmp = new ArrayList<>();
		tmp.add(currentState);
		tmp.add(buttonLabel);
		String nextState = strategy.get(tmp);
		if(nextState.equals("return")) {
			return caller;
		} else {
			if(currentState.charAt(0) != nextState.charAt(0)) {
				caller = currentState;
			}
			return nextState;
		}
	}
}
