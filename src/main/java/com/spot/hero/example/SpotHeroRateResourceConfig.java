package com.spot.hero.example;

import com.codahale.metrics.MetricRegistry;
import com.spot.hero.example.util.MetricsContextListener;
import org.glassfish.jersey.server.ResourceConfig;
import org.mpierce.jersey2.metrics.MetricsAppEventListener;

public class SpotHeroRateResourceConfig extends ResourceConfig {

	public SpotHeroRateResourceConfig() {

		MetricRegistry mc = MetricsContextListener.REGISTRY;
		MetricsAppEventListener listener = new MetricsAppEventListener.Builder(mc).build();
		this.register(listener);
		this.register(new AppBinder());

		packages("com.spot.hero.example.resource");

	}
}