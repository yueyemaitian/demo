package com.tmall.buy.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.tmall.buy.storm.bolts.WordCounter;
import com.tmall.buy.storm.bolts.WordNormalizer;
import com.tmall.buy.storm.spouts.WordReader;

public class TopologyMain {
	public static void main(String[] args) throws InterruptedException {
		// ��������
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader", new WordReader());
		builder.setBolt("word-normalizer", new WordNormalizer())
				.shuffleGrouping("word-reader");
		builder.setBolt("word-counter", new WordCounter(), 2).fieldsGrouping(
				"word-normalizer", new Fields("word"));

		// ����
		Config conf = new Config();
		conf.put("wordsFile", args[0]);
		conf.setDebug(false);

		// ��������
//		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("Getting-Started-Topologie", conf,
				builder.createTopology());
		Thread.sleep(10000);
		cluster.shutdown();
	}
}