package org.springside.modules.metrics;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springside.modules.metrics.Execution.ExecutionTimer;
import org.springside.modules.metrics.utils.Clock.MockClock;

public class ExecutionTest {

	@Test
	public void normal() {
		MockClock clock = new MockClock();
		Execution.clock = clock;
		Counter.clock = clock;
		Execution execution = new Execution(new Double[] { 90d });

		ExecutionTimer timer = execution.start();
		clock.increaseTime(200);
		timer.stop();

		ExecutionTimer timer2 = execution.start();
		clock.increaseTime(300);
		timer2.stop();

		ExecutionMetric metric = execution.calculateMetric();

		assertEquals(2, metric.counterMetric.totalCount);
		assertEquals(4, metric.counterMetric.lastRate, 0);

		assertEquals(200, metric.histogramMetric.min);
		assertEquals(250, metric.histogramMetric.mean, 0);
		assertEquals(300, metric.histogramMetric.pcts.get(90d), 0);
	}
}
