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
		{"t","ステップ1","d0"},
		{"t","ステップ2","i0"},
		{"t","ステップ3","p0"},
		{"t","ステップ4","a0"},
		{"a0","次へ","a1"},
		{"a1","ある","a2"},
		{"a1","ない","a5"},
		{"a2","存在する","a6"},
		{"a2","存在しない","a3"},
		{"a3","正しい","a4"},
		{"a3","誤り","a7"},
		{"a4","正しい","e2"},
		{"a4","誤り","e1"},
		{"a5","条件文","b1"},
		{"a5","繰り返し文","r1"},
		{"a5","次へ","e2"},
		{"a6","条件文","b1"},
		{"a6","繰り返し文","r1"},
		{"a6","次へ","a3"},
		{"a7","正しい","a1"},
		{"a7","誤り","e1"},
		{"r1","はい","r2"},
		{"r1","いいえ","r4"},
		{"r2","正しい","return"},
		{"r2","誤り","r3"},
		{"r3","終了","t"},
		{"r4","正しい","r6"},
		{"r4","誤り","r5"},
		{"r5","正しい","a1"},
		{"r5","誤り","e1"},
		{"r6","正しい","return"},
		{"r6","誤り","e1"},
		{"b1","正しい","return"},
		{"b1","誤り","b2"},
		{"b2","正しい","b4"},
		{"b2","誤り","b3"},
		{"b3","正しい","a1"},
		{"b3","誤り","e1"},
		{"b4","正しい","e2"},
		{"b4","誤り","e1"},
		{"d0","次へ","d1"},
		{"d1","発生している","e3"},
		{"d1","発生していない","d2"},
		{"d2","理想通り","e4"},
		{"d2","誤り","d3"},
		{"d3","発生している","e5"},
		{"d3","発生していない","d4"},
		{"d4","表示される","i0"},
		{"d4","表示されない","e6"},
		{"e1","終了","t"},
		{"e2","終了","t"},
		{"e3","終了","t"},
		{"e4","終了","t"},
		{"e5","終了","t"},
		{"e6","終了","t"},
		{"e7","終了","t"},
		{"e8","終了","t"},
		{"i0","次へ","i1"},
		{"i1","ある","i2"},
		{"i1","ない","i3"},
		{"i2","全て誤り","p0"},
		{"i2","一部正しい","i5"},
		{"i3","全て誤り","p0"},
		{"i3","一部正しい","i4"},
		{"i4","乱数","e7"},
		{"i4","日付データ","e8"},
		{"i4","どちらもない","t"},
		{"i5","次へ","p0"},
		{"p0","次へ","p1"},
		{"p1","分割","p2"},
		{"p1","メソッド","p2_2"},
		{"p2","正しい","p3"},
		{"p2","誤り","p3"},
		{"p2_2","正しい","p4"},
		{"p2_2","誤り","p3"},
		{"p3","分割","p2"},
		{"p3","メソッド","p2_2"},
		{"p3","次へ","a0"},
		{"p4","正しい","p3"},
		{"p4","誤り","p3"},
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
		return nextState;
	}

}
