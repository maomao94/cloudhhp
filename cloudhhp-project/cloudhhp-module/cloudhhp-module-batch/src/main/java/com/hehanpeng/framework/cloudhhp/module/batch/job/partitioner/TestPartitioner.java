package com.hehanpeng.framework.cloudhhp.module.batch.job.partitioner;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TestPartitioner implements Partitioner {

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> map = new HashMap<String, ExecutionContext>();
		for(int i = 0; i< gridSize;i++) {
			ExecutionContext excution = new ExecutionContext();
			excution.putInt("index", i);
			excution.putInt("gridSize", gridSize);
			map.put("partition"+i, excution);
		}
		return map;
	}

}
