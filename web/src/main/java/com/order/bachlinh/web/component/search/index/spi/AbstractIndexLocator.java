package com.order.bachlinh.web.component.search.index.spi;

import com.order.bachlinh.web.component.search.index.internal.IndexPositionContainer;
import com.order.bachlinh.web.component.search.utils.FileReader;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Log4j2
public abstract class AbstractIndexLocator implements IndexLocator {
    private final FileReader reader;
    private final IndexPositionContainer container;

    protected AbstractIndexLocator(@NonNull FileReader reader, @NonNull IndexPositionContainer container) {
        this.reader = reader;
        this.container = container;
    }

    @Override
    public void setKeywordPosition(long position) {
        container.set(position);
    }

    @Override
    public void clear() {
        container.clear();
    }

    protected long findKeywordIndex(String keyword) {
        try (FileChannel channel = openChannel(getReader().filePath())) {
            int position = search(keyword, channel, container.size());
            if (position < 0) {
                return channel.size() - 1;
            }
            return container.get(position);
        } catch (IOException e) {
            log.error("Can not open channel because IOException", e);
            return -1;
        }
    }

    protected long findPreviousIndex(String keyword) {
        try (FileChannel channel = openChannel(getReader().filePath())) {
            int position = search(keyword, channel, container.size());
            if (position >= 0) {
                return container.get(position - 1);
            }
            return position;
        } catch (IOException e) {
            log.error("Can not open channel because IOException", e);
            return -1;
        }
    }

    protected long findNextIndex(String keyword) {
        try (FileChannel channel = openChannel(getReader().filePath())) {
            int position = search(keyword, channel, container.size());
            if (position >= 0) {
                return container.get(position + 1);
            }
            return position;
        } catch (IOException e) {
            log.error("Can not open channel because IOException", e);
            return -1;
        }
    }

    protected long fullScanIndexTree(String keyword, int transfer) {
        List<Long> indexTree = container.getUnsortedIndex();
        ByteBuffer buff = ByteBuffer.allocate(keyword.getBytes(StandardCharsets.UTF_8).length + 2);
        long rs = -1;
        try (FileChannel channel = openChannel(getReader().filePath())) {
            int counter = 0;
            for (long index : indexTree) {
                channel.read(buff, index);
                String result = new String(buff.array(), StandardCharsets.UTF_8).replace("\"","");
                if (result.equals(keyword)) {
                    rs = indexTree.get(counter + transfer);
                    break;
                }
                buff.clear();
                counter++;
            }
        } catch (IOException | IndexOutOfBoundsException e) {
            log.error("Can not locate [{}] because [{}]", keyword, e.getClass().getSimpleName());
            log.error("Stacktrace: ", e);
        }
        return rs;
    }

    protected FileReader getReader() {
        return reader;
    }

    private FileChannel openChannel(String path) throws IOException {
        RandomAccessFile file = new RandomAccessFile(path, "r");
        return file.getChannel();
    }

    private int search(String keyword, FileChannel channel, int high) throws IOException {
        int low = 0;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            channel.position(container.get(mid));
            ByteBuffer data = ByteBuffer.allocate(keyword.length() + 2);
            channel.read(data, keyword.length() + (long) 2);
            String checker = new String(data.array(), StandardCharsets.UTF_8);
            if (checker.compareTo(keyword) == 0) {
                data.clear();
                return mid;
            }
            if (checker.compareTo(keyword) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
            data.clear();
        }
        return -1;
    }
}
