package com.github.raftimpl.raft;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaftOptions {

    // A follower would become a candidate if it doesn't receive any message
    // from the leader in electionTimeoutMs milliseconds
    private int electionTimeoutMilliseconds = 5000;

    // A leader sends RPCs at least this often, even if there is no data to send
    private int heartbeatPeriodMilliseconds = 500;

    // snapshot定时器执行间隔
    private int snapshotPeriodSeconds = 3600;
    // log entry大小达到snapshotMinLogSize，才做snapshot
    private int snapshotMinLogSize = 100 * 1024 * 1024;
    private int maxSnapshotBytesPerRequest = 500 * 1024; // 500k

    private int maxLogEntriesPerRequest = 5000;

    // 单个segment文件大小，默认100m
    private int maxSegmentFileSize = 100 * 1000 * 1000;

    // follower与leader差距在catchupMargin，才可以参与选举和提供服务
    private long catchupMargin = 500;

    // replicate最大等待超时时间，单位ms
    private long maxAwaitTimeout = 1000;

    // 与其他节点进行同步、选主等操作的线程池大小
    private int raftConsensusThreadNum = 20;

    // 是否异步写数据；true表示主节点保存后就返回，然后异步同步给从节点；
    // false表示主节点同步给大多数从节点后才返回。
    private boolean asyncWrite = false;

    // raft的log和snapshot父目录，绝对路径
    private String dataDir = System.getProperty("com.github.raftimpl.raft.data.dir");

    // private String dataPath = null;

    public String getDataDir() {
        return this.dataDir;
    }

    public void setDataDir(String data_path) {
        this.dataDir = data_path;

    }

    public void setSnapshotMinLogSize(int snapshotMinLogSize) {
        this.snapshotMinLogSize = snapshotMinLogSize;
    }

    public int getSnapshotMinLogSize() {
        return this.snapshotMinLogSize;
    }

    public void setSnapshotPeriodSeconds(int snapshotPeriodSeconds) {
        this.snapshotPeriodSeconds = snapshotPeriodSeconds;
    }

    public int getSnapshotPeriodSeconds() {
        return this.snapshotPeriodSeconds;
    }

    public void setMaxSegmentFileSize(int maxSegmentFileSize) {
        this.maxSegmentFileSize = maxSegmentFileSize;
    }

    public int getMaxSegmentFileSize() {
        return this.maxSegmentFileSize;
    }

    public int getRaftConsensusThreadNum() {
        return this.raftConsensusThreadNum;
    }

    public int getElectionTimeoutMilliseconds() {
        /**
         * Gets the election timeout in milliseconds.
         *
         * @return the election timeout in milliseconds
         */
        return this.electionTimeoutMilliseconds;
    }

    public void setElectionTimeoutMilliseconds(int electionTimeoutMilliseconds) {
        /**
         * Sets the election timeout in milliseconds.
         *
         * @param electionTimeoutMilliseconds the election timeout in milliseconds
         */
        this.electionTimeoutMilliseconds = electionTimeoutMilliseconds;
    }

    public int getHeartbeatPeriodMilliseconds() {
        /**
         * Gets the heartbeat period in milliseconds.
         *
         * @return the heartbeat period in milliseconds
         */
        return this.heartbeatPeriodMilliseconds;
    }

    public void setHeartbeatPeriodMilliseconds(int heartbeatPeriodMilliseconds) {
        /**
         * Sets the heartbeat period in milliseconds.
         *
         * @param heartbeatPeriodMilliseconds the heartbeat period in milliseconds
         */
        this.heartbeatPeriodMilliseconds = heartbeatPeriodMilliseconds;
    }

    public int getMaxSnapshotBytesPerRequest() {
        /**
         * Gets the maximum snapshot bytes per request.
         *
         * @return the maximum snapshot bytes per request
         */
        return this.maxSnapshotBytesPerRequest;
    }

    public void setMaxSnapshotBytesPerRequest(int maxSnapshotBytesPerRequest) {
        /**
         * Sets the maximum snapshot bytes per request.
         *
         * @param maxSnapshotBytesPerRequest the maximum snapshot bytes per request
         */
        this.maxSnapshotBytesPerRequest = maxSnapshotBytesPerRequest;
    }

    public int getMaxLogEntriesPerRequest() {
        /**
         * Gets the maximum log entries per request.
         *
         * @return the maximum log entries per request
         */
        return this.maxLogEntriesPerRequest;
    }

    public void setMaxLogEntriesPerRequest(int maxLogEntriesPerRequest) {
        /**
         * Sets the maximum log entries per request.
         *
         * @param maxLogEntriesPerRequest the maximum log entries per request
         */
        this.maxLogEntriesPerRequest = maxLogEntriesPerRequest;
    }

    public long getCatchupMargin() {
        /**
         * Gets the catchup margin.
         *
         * @return the catchup margin
         */
        return this.catchupMargin;
    }

    public void setCatchupMargin(long catchupMargin) {
        /**
         * Sets the catchup margin.
         *
         * @param catchupMargin the catchup margin
         */
        this.catchupMargin = catchupMargin;
    }

    public long getMaxAwaitTimeout() {
        /**
         * Gets the maximum await timeout in milliseconds.
         *
         * @return the maximum await timeout in milliseconds
         */
        return this.maxAwaitTimeout;
    }

    public void setMaxAwaitTimeout(long maxAwaitTimeout) {
        /**
         * Sets the maximum await timeout in milliseconds.
         *
         * @param maxAwaitTimeout the maximum await timeout in milliseconds
         */
        this.maxAwaitTimeout = maxAwaitTimeout;
    }

    public void setRaftConsensusThreadNum(int raftConsensusThreadNum) {
        /**
         * Sets the number of threads for raft consensus operations.
         *
         * @param raftConsensusThreadNum the number of threads
         */
        this.raftConsensusThreadNum = raftConsensusThreadNum;
    }

    public boolean isAsyncWrite() {
        /**
         * Checks if async write is enabled.
         *
         * @return true if async write is enabled, false otherwise
         */
        return this.asyncWrite;
    }

    public void setAsyncWrite(boolean asyncWrite) {
        /**
         * Sets whether async write is enabled.
         *
         * @param asyncWrite true to enable async write, false otherwise
         */
        this.asyncWrite = asyncWrite;
    }
}
