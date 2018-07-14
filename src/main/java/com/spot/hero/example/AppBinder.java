package com.spot.hero.example;
import com.spot.hero.example.service.RateService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class AppBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(RateService.class).to(RateService.class).in(Singleton.class);
    }

}
