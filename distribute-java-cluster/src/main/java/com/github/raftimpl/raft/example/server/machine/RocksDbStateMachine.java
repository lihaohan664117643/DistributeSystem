package com.github.raftimpl.raft.example.server.machine;

import btree4j.BTreeException;
import com.github.raftimpl.raft.RaftNode;
import com.github.raftimpl.raft.StateMachine;
import com.github.raftimpl.raft.example.server.service.ExampleProto;
import com.github.raftimpl.raft.proto.RaftProto;
import com.github.raftimpl.raft.storage.SegmentedLog;

import java.util.ArrayList;
import java.util.List;

import org.rocksdb.RocksDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDBException;
import org.rocksdb.DBOptions;
import org.rocksdb.ColumnFamilyOptions;
import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyHandle;
import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RocksDbStateMachine implements StateMachine {
    private static final Logger LOG = LoggerFactory.getLogger(RocksDbStateMachine.class);
    private RocksDB db;
    private final String raftDataDir;

    public RocksDbStateMachine(String raftDataDir) {
        this.raftDataDir = raftDataDir;
    }

    private RocksDB startDb(String db_path) {
        RocksDB tmpDB = null;
        try {
            DBOptions dbOptions = new DBOptions()
                    .setCreateIfMissing(true)
                    .setCreateMissingColumnFamilies(true);

            // 配置列族选项
            ColumnFamilyOptions cfOptions = new ColumnFamilyOptions();
            cfOptions.setWriteBufferSize(8 * 1024 * 1024); // 8MB写缓冲区

            // 创建RocksDB实例
            String dbPath = db_path;
            List<ColumnFamilyDescriptor> cfDescriptors = new ArrayList<>();
            cfDescriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOptions));

            List<ColumnFamilyHandle> columnFamilyHandles = new ArrayList<>();
            tmpDB = RocksDB.open(dbOptions, dbPath, cfDescriptors, columnFamilyHandles);
        } catch (RocksDBException e) {
            LOG.warn("Exception when trying to open the db, msg={}", e.getMessage());
        }
        return tmpDB;
    }

    @Override
    public void writeSnap(String snapshotDir, String tmpSnapshotDataDir, RaftNode raftNode,
            long localLastAppliedIndex) {
        try {
            File snapshotData = new File(snapshotDir + File.separator + "data");
            File tmpSnapshotData = new File(tmpSnapshotDataDir);
            FileUtils.copyDirectory(snapshotData, tmpSnapshotData);
            RocksDB tmpDB = this.startDb(tmpSnapshotDataDir);

            SegmentedLog raftLog = raftNode.getRaftLog();
            for (long index = raftNode.getSnapshot().getMeta().getLastIncludedIndex()
                    + 1; index <= localLastAppliedIndex; index++) {
                // get the data from the log
                RaftProto.LogEntry entry = raftLog.getEntry(index);
                if (entry.getType() == RaftProto.EntryType.ENTRY_TYPE_DATA) {
                    ExampleProto.SetRequest request = ExampleProto.SetRequest.parseFrom(entry.getData().toByteArray());
                    tmpDB.put(request.getKey().getBytes(), request.getValue().getBytes());
                }
            }
        } catch (Exception e) {
            LOG.warn("writeSnapshot meet exception, msg={}", e.getMessage());
        }

    }

    @Override
    public void readSnap(String snapshotDir) {
        try {
            // 将快照目录复制到数据目录
            if (db != null) {
                db.close();
                db = null;
            }
            String dataDir = raftDataDir + File.separator + "rocksdb_data";
            LOG.info("------------current data idr: {}", dataDir);
            System.out.println("reading from the snapshot");
            System.out.println(dataDir);
            File dataFile = new File(dataDir);
            if (dataFile.exists()) {
                FileUtils.deleteDirectory(dataFile);
            }
            File snapshotFile = new File(snapshotDir);
            if (snapshotFile.exists()) {
                FileUtils.copyDirectory(snapshotFile, dataFile);
            }

            db = this.startDb(dataDir);
        } catch (Exception e) {
            LOG.warn("meet exception, msg={}", e.getMessage());
        }

    }

    @Override
    public void applyData(byte[] dataBytes) {
        try {
            if (db == null) {
                throw new BTreeException("database is closed, please wait for reopen");
            }
            LOG.info("writing the data to the db")
            ExampleProto.SetRequest request = ExampleProto.SetRequest.parseFrom(dataBytes);
            db.put(request.getKey().getBytes(), request.getValue().getBytes());
        } catch (Exception e) {
            LOG.warn("meet exception, msg={}", e.getMessage());
        }

    }

    @Override
    public byte[] get(byte[] dataBytes) {
        byte[] result = null;
        try {
            if (db == null) {
                throw new RocksDBException("database is closed, please wait for reopen");
            }
            LOG.info("reading the data from the db")
            result = db.get(dataBytes);
        } catch (Exception e) {
            LOG.warn("read leveldb exception, msg={}", e.getMessage());
        }
        return result;

    }
}
