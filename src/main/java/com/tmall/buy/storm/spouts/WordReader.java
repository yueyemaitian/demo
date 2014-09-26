package com.tmall.buy.storm.spouts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordReader extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private FileReader fileReader;
	private boolean completed = false;
	private TopologyContext context;

	public boolean isDistributed() {
		return false;
	}

	@Override
	public void ack(Object msgId) {
		System.err.println("OK:" + msgId);
	}

	@Override
	public void close() {
	}

	@Override
	public void fail(Object msgId) {
		System.err.println("FAIL:" + msgId);
	}

	/**
	 * �����������Ωһһ��������Ƿַ��ļ��е��ı���
	 */
	@Override
	public void nextTuple() {
		/**
		 * ��������᲻�ϵı����ã�ֱ�������ļ��������ˣ����ǽ��ȴ������ء�
		 */
		if (completed) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// ʲôҲ����
			}
			return;
		}
		String str;
		// ����reader
		BufferedReader reader = new BufferedReader(fileReader);
		try {
			// �������ı���
			while ((str = reader.readLine()) != null) {
				/**
				 * ���з���һ����ֵ
				 */
				this.collector.emit(new Values(str), str);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error reading tuple", e);
		} finally {
			completed = true;
		}
	}

	/**
	 * ���ǽ�����һ���ļ���ά��һ��collector����
	 */
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		try {
			this.context = context;
			this.fileReader = new FileReader(conf.get("wordsFile").toString());
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error reading file ["
					+ conf.get("wordFile") + "]");
		}
		this.collector = collector;
	}

	/**
	 * ����������"word"
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

}