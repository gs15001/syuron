package org.jeditor.navi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultNaviStrategy implements NaviStrategy {

	// 状態(String)と押したボタン(String)から次の状態(String)を導く
	Map<List<String>, String> strategy;

	//@formatter:off
	static String[][][] STATECHANGE = {
		{ { "s1", "1" }, { "s2" } },
		{ { "s2", "1" }, { "s1" } },
		{ { "s3", "1" }, { "s2" } },
		{ { "s4", "1" }, { "s2" } },
		{ { "s5", "1" }, { "s2" } },
		{ { "s6", "1" }, { "s2" } },
		{ { "s7", "1" }, { "s2" } },
		};
	//@formatter:on

	public DefaultNaviStrategy() {
		strategy = new HashMap<>();
		for (String[][] sc : STATECHANGE) {
			strategy.put(Arrays.asList(sc[0]), sc[1][0]);
		}
	}

	@Override
	public String getNextNavi(String currentState, String buttonLabel) {
		List<String> tmp = new ArrayList<>();
		tmp.add(currentState);
		tmp.add(buttonLabel);
		return strategy.get(tmp);
	}

}
