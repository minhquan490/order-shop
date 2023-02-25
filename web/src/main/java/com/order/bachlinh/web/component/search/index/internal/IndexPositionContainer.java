package com.order.bachlinh.web.component.search.index.internal;

import com.order.bachlinh.core.util.ZipUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class IndexPositionContainer {
    private final List<Long> container = Collections.synchronizedList(new ArrayList<>());
    private final AtomicReference<ByteBuffer> rawData = new AtomicReference<>(null);

    public long get(int positionIndex) {
        return container.get(positionIndex);
    }

    public void set(long position) {
        buildUnsortedData(position);
        if (!container.contains(position)) {
            container.add(position);
            container.sort(Comparator.comparingLong(Number::longValue));
        }
    }

    public void remove(int position) {
        container.remove(position);
    }

    public int size() {
        return container.size();
    }

    public void clear() {
        container.clear();
    }

    public List<Long> getUnsortedIndex() {
        return ZipUtils.decompress(rawData.get());
    }

    private void buildUnsortedData(long position) {
        Runnable runnable = () -> {
            List<Long> data;
            if (rawData.get() == null) {
                data = new ArrayList<>();
                data.add(position);
            } else {
                data = ZipUtils.decompress(rawData.get());
                data.add(position);
                rawData.get().clear();
            }
            rawData.set(ZipUtils.compress(data));
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
