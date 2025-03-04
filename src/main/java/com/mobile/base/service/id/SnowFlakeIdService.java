package com.mobile.base.service.id;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.hibernate.validator.constraints.Range;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Twitter_Snowflake<br>
 * The structure of SnowFlake is as follows (each part is separated by -):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1 bit identifier, since the long basic type is signed in Java, the highest bit is the sign bit, the positive number is 0, the negative number is 1, so the id is generally positive, the highest bit is 0<br>
 * 41-bit time cut (milliseconds), note that the 41-bit time cut is not the time cut of the current time, but the difference of the stored time cut (current time cut - start time cut)
 * The value obtained), the start time truncation here, is generally the time when our id generator starts to be used, which is specified by our program (see the startTime property of the IdWorker class below). The 41-bit time cut can be used for 69 years, year T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10-bit data machine bits that can be deployed in 1024 nodes, including 5-bit datacenterId and 5-bit workerId<br>
 * 12-bit sequence, count in milliseconds, 12-bit count sequence number supports each node every millisecond (same machine, same time cut) to generate 4096 ID numbers<br>
 * Add up to just 64 bits, which is a Long type. <br>
 * The advantage of SnowFlake is that it is sorted by time as a whole, and ID collisions (differentiated by data center ID and machine ID) are not generated in the entire distributed system, and the efficiency is high. After testing, SnowFlake can generate every second. 260,000 ID or so.
 */
public class SnowFlakeIdService {
    private static SnowFlakeIdService instance;

    // ============================== 30K unique ID / seconds===========================================
    /**
     * Start time cut (2015-01-01)
     */
    private final long twepoch = 1489111610226L;

    /**
     * The number of bits in the machine id  5 -> 6
     */
    private final long workerIdBits = 5L;

    /**
     * Number of digits in the data identifier id 5
     */
    private final long dataCenterIdBits = 5L;

    /**
     * The maximum machine id supported, the result is 31 (this shift algorithm can quickly calculate the maximum decimal number that can be represented by several binary digits)
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * The maximum data ID id supported, the result is 31
     */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * The number of bits in the id of the sequence 12 -> 5
     */
    private final long sequenceBits = 5L;

    /**
     * Machine ID is shifted to the left by 12 digits
     */
    private final long workerIdShift = sequenceBits;

    /**
     * The data identification id is shifted to the left by 17 digits (12+5)
     */
    private final long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * Time is shifted to the left by 22 bits (5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * Generate a mask for the sequence, here 4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * Work Machine ID (0~31)
     */
    private long workerId;

    /**
     * Data Center ID (0~31)
     */
    private long dataCenterId;

    /**
     * Sequence within milliseconds (0~4095)
     */
    private long sequence = 0L;

    /**
     * Time to last generated ID
     */
    private long lastTimestamp = -1L;


    // Create SequenceGenerator with a nodeId
    private SnowFlakeIdService() {

    }

    public synchronized static SnowFlakeIdService getInstance() {
        if (instance == null) {
            instance = new SnowFlakeIdService();
        }
        return instance;
    }

    /**
     * Work Machine ID (0~63)
     */
    public void setNodeId(@Range(min = 0, max = 63) int nodeId) {
        if (nodeId < 0 || nodeId > maxWorkerId) {
            throw new IllegalArgumentException(String.format("NodeId must be between %d and %d", 0, maxWorkerId));
        }
        this.workerId = nodeId;
    }

    /**
     * Data Center ID (0~31)
     */
    public void setDataCenterId(@Range(min = 0, max = 32) int dataCenterId) {
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.dataCenterId = dataCenterId;
    }


    // ==============================Methods==========================================

    /**
     * Get the next ID (this method is thread safe)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //If the current time is less than the timestamp generated by the previous ID, it means that the system clock should be thrown abnormally at this time.
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // If it is generated at the same time, then the sequence within milliseconds
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //Sequence overflow in milliseconds
            if (sequence == 0) {
                // Block to the next millisecond, get a new timestamp
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //Timestamp changed, sequence reset in milliseconds
        else {
            sequence = 0L;
        }
        // The last time the ID was generated
        lastTimestamp = timestamp;

        // Shift and put together by the OR operation to form a 64-bit ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * Block until the next millisecond until a new timestamp is obtained
     *
     * @param lastTimestamp The timestamp of the last generated ID
     * @return current timestamp
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * Returns the current time in milliseconds
     *
     * @return current time (ms)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    private static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for (int b : ints) {
                sums += b;
            }
            return (long) (sums % 32);
        } catch (UnknownHostException e) {
            // If the acquisition fails, use a random number to reserve
            return RandomUtils.nextLong(0, 31);
        }
    }

    private static Long getDataCenterId() {
        int[] ints = StringUtils.toCodePoints(SystemUtils.getHostName());
        int sums = 0;
        for (int i : ints) {
            sums += i;
        }
        return (long) (sums % 32);
    }
}
