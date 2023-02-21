package com.order.bachlinh.core.component.setup.spi;

import java.util.Collection;

public interface SetupManager {
    boolean isClose();
    Collection<Setup> loadSetup() throws ClassNotFoundException;
    void run() throws ClassNotFoundException;
    void close();
}
