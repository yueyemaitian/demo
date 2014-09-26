package com.tmall.buy.storm.bolts;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class WordCounter extends BaseBasicBolt {
	private static final long serialVersionUID = 5611637971796113706L;
	Map<String, Integer> counters = new HashMap<String, Integer>();

	/**
	 * 这个spout结束时（集群关闭的时候），我们会显示单词数量
	 */
	@Override
	public void cleanup() {
		System.err.println("-- 单词数 --");
		for (Map.Entry<String, Integer> entry : counters.entrySet()) {
			System.err.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String str = input.getString(0);
		/**
		 * 如果单词尚不存在于map，我们就创建一个，如果已在，我们就为它加1
		 */
		if (!counters.containsKey(str)) {
			counters.put(str, 1);
		} else {
			Integer c = counters.get(str) + 1;
			counters.put(str, c);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}
}