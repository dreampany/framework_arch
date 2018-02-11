package com.dreampany.framework.data.api.network.nearby;


import com.dreampany.framework.data.api.network.data.model.Id;
import com.dreampany.framework.data.api.network.data.model.Meta;

import java.nio.ByteBuffer;

/**
 * Created by nuc on 6/18/2017.
 */

public class Packets {
    private static final byte TYPE_PEER = 1;
    private static final byte TYPE_DATA = 2;
    private static final byte TYPE_FILE = 3;

    private static final byte SUBTYPE_ID = 1;
    private static final byte SUBTYPE_META = 2;
    private static final byte SUBTYPE_OKAY = 3;
    private static final byte SUBTYPE_DATA = 4;
    private static final byte SUBTYPE_FILE_PAYLOAD_ID = 5;

    private static final byte SUBTYPE_META_REQUEST = 6;
    private static final byte SUBTYPE_DATA_REQUEST = 7;


    /**
     * Peer: meta, data
     * Bytes:
     * File:
     * Stream:
     */

    public static byte[] getPeerMetaPacket(long hash) {
        ByteBuffer buf = ByteBuffer.allocate(1 + 1 + 8);
        buf.put(TYPE_PEER);
        buf.put(SUBTYPE_META);
        buf.putLong(hash);
        return buf.array();
    }

    public static byte[] getPeerOkayPacket() {
        ByteBuffer buf = ByteBuffer.allocate(1 + 1);
        buf.put(TYPE_PEER);
        buf.put(SUBTYPE_OKAY);
        return buf.array();
    }

    public static byte[] getPeerDataPacket(byte[] data) {
        ByteBuffer buf = ByteBuffer.allocate(1 + 1 + data.length);
        buf.put(TYPE_PEER);
        buf.put(SUBTYPE_DATA);
        buf.put(data);
        return buf.array();
    }

    public static byte[] getDataPacket(byte[] bytes) {
        ByteBuffer buf = ByteBuffer.allocate(1 + bytes.length);
        buf.put(TYPE_DATA);
        buf.put(bytes);
        return buf.array();
    }

    public static byte[] getFileIdPacket(Id id) {
        ByteBuffer buf = ByteBuffer.allocate(1 + 1 + 8 + 8);
        buf.put(TYPE_FILE);
        buf.put(SUBTYPE_ID);
        buf.putLong(id.id);
        buf.putLong(id.source);
        return buf.array();
    }

    public static byte[] getFileMetaRequestPacket(long fileId) {
        ByteBuffer buf = ByteBuffer.allocate(1 + 1 + 8);
        buf.put(TYPE_FILE);
        buf.put(SUBTYPE_META_REQUEST);
        buf.putLong(fileId);
        return buf.array();
    }

    public static byte[] getFileDataRequestPacket(long fileId) {
        ByteBuffer buf = ByteBuffer.allocate(1 + 1 + 8);
        buf.put(TYPE_FILE);
        buf.put(SUBTYPE_DATA_REQUEST);
        buf.putLong(fileId);
        return buf.array();
    }

    public static byte[] getFileMetaPacket(long fileId, Meta fileMeta) {
        byte[] meta = fileMeta.toArray();
        ByteBuffer buf = ByteBuffer.allocate(1 + 1 + 8 + meta.length);
        buf.put(TYPE_FILE);
        buf.put(SUBTYPE_META);
        buf.putLong(fileId);
        buf.put(meta);
        return buf.array();
    }

    public static byte[] getFilePayloadIdPacket(long fileId, long payloadId) {
        ByteBuffer buf = ByteBuffer.allocate(1 + 1 + 8 + 8);
        buf.put(TYPE_FILE);
        buf.put(SUBTYPE_FILE_PAYLOAD_ID);
        buf.putLong(fileId);
        buf.putLong(payloadId);
        return buf.array();
    }

    public static boolean isPeer(byte[] data) {
        if (data == null || data.length == 0) {
            return false;
        }
        return data[0] == TYPE_PEER;
    }

    public static boolean isData(byte[] data) {
        if (data == null || data.length == 0) {
            return false;
        }
        return data[0] == TYPE_DATA;
    }

    public static boolean isFile(byte[] data) {
        if (data == null || data.length == 0) {
            return false;
        }
        return data[0] == TYPE_FILE;
    }

    public static boolean isId(byte[] data) {
        if (data == null || data.length <= 1) {
            return false;
        }
        return data[1] == SUBTYPE_ID;
    }

    public static boolean isMetaRequest(byte[] data) {
        if (data == null || data.length <= 1) {
            return false;
        }
        return data[1] == SUBTYPE_META_REQUEST;
    }

    public static boolean isMeta(byte[] data) {
        if (data == null || data.length <= 1) {
            return false;
        }
        return data[1] == SUBTYPE_META;
    }

    public static boolean isOkay(byte[] data) {
        if (data == null || data.length <= 1) {
            return false;
        }
        return data[1] == SUBTYPE_OKAY;
    }

    public static boolean isPeerData(byte[] data) {
        if (data == null || data.length <= 1) {
            return false;
        }
        return data[1] == SUBTYPE_DATA;
    }

    public static boolean isDataRequest(byte[] data) {
        if (data == null || data.length <= 1) {
            return false;
        }
        return data[1] == SUBTYPE_DATA_REQUEST;
    }

    public static boolean isFilePayloadId(byte[] data) {
        if (data == null || data.length <= 1) {
            return false;
        }
        return data[1] == SUBTYPE_FILE_PAYLOAD_ID;
    }
}
