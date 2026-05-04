package com.tus.coupon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Workflow input: mirrors scheduling intent from merchant task creation.
 * <p>
 * {@link JsonCreator} is require so Temporal's default {@code JacksonJsonPayloadConverter}
 * can deserialize workflow arguments from history on replay.
 * </p>
 */
public final class CouponTaskScheduleInput implements Serializable {
    private static final long serialVersionUID = 1L;


    private final long taskId;
    private final long shopNumber;
    /**
     * Epoch millis — execute at or after this instant
     */
    private final long sendTimeEpochMillis;

    @JsonCreator
    public CouponTaskScheduleInput(@JsonProperty("taskId") long taskId,
                                   @JsonProperty("shopNumber") long shopNumber,
                                   @JsonProperty("sendTimeEpochMillis") long sendTimeEpochMillis) {
        this.taskId = taskId;
        this.shopNumber = shopNumber;
        this.sendTimeEpochMillis = sendTimeEpochMillis;
    }

    public long getTaskId() {
        return taskId;
    }

    public long getShopNumber() {
        return shopNumber;
    }

    public long getSendTimeEpochMillis() {
        return sendTimeEpochMillis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CouponTaskScheduleInput that = (CouponTaskScheduleInput) o;
        return taskId == that.taskId && shopNumber == that.shopNumber
                && sendTimeEpochMillis == that.sendTimeEpochMillis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, shopNumber, sendTimeEpochMillis);
    }

    @Override
    public String toString() {
        return "CouponTaskScheduleInput{taskId=" + taskId + ", shopNumber=" + shopNumber
                + ", sendTimeEpochMillis=" + sendTimeEpochMillis + '}';
    }
}
