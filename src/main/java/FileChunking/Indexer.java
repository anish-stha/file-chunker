package FileChunking;


import java.nio.ByteBuffer;

public class Indexer {

    int INDEX_LENGTH = 4;

    public byte[] appendIndex(byte[] data, int intIndex) {
        byte[] index = intToBytes(intIndex);
        byte[] result = appendIndex(data, index);
        return result;
    }

    public byte[] appendIndex(byte[] data, byte[] index) {
        byte[] result = new byte[data.length + index.length ];
        System.arraycopy(index,0,result,0,index.length);
        System.arraycopy(data,0,result,index.length,data.length);
        return result;
    }

    public byte[] intToBytes( final int i ) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }

    public int getIndex(byte[] dataWithIndex){
        byte[] index = new byte[INDEX_LENGTH];
        System.arraycopy(dataWithIndex,0,index,0,INDEX_LENGTH);
        return ByteBuffer.wrap(index).getInt();
    }

    public byte[] getData(byte[] dataWithIndex){
        byte[] data = new byte[dataWithIndex.length - INDEX_LENGTH];
        System.arraycopy(dataWithIndex, INDEX_LENGTH, data, 0, dataWithIndex.length - INDEX_LENGTH);
        return data;
    }
}
