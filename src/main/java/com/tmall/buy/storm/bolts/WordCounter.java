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
	 * ���spout����ʱ����Ⱥ�رյ�ʱ�򣩣����ǻ���ʾ��������
	 */
	@Override
	public void cleanup() {
		System.err.println("-- ������ --");
		for (Map.Entry<String, Integer> entry : counters.entrySet()) {
			System.err.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String str = input.getString(0);
		/**
		 * ��������в�������map�����Ǿʹ���һ����������ڣ����Ǿ�Ϊ����1
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