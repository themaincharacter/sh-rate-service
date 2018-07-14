package com.spot.hero.example.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Rate {

    private String days;
    private String times;
    private BigDecimal price;

    public List<String> getDaysList() {
        return Arrays.asList(days.split(","));
    }

    public void setDaysList(List<String> days) {
        this.days = days.stream().collect(Collectors.joining(","));
    }

    public long getStartTime() {
        return Long.parseLong(times.split("-")[0]);
    }

    public long getEndTime() {
        return Long.parseLong(times.split("-")[1]);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTimes() {
        return times;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Objects.equals(days, rate.days) &&
                Objects.equals(times, rate.times) &&
                Objects.equals(price, rate.price);
    }

    @Override
    public int hashCode() {

        return Objects.hash(days, times, price);
    }

    @Override
    public String toString() {
        return "Rate{" +
                "days='" + days + '\'' +
                ", times='" + times + '\'' +
                ", price=" + price +
                '}';
    }


}
