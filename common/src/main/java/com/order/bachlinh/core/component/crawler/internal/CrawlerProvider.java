package com.order.bachlinh.core.component.crawler.internal;

import com.order.bachlinh.core.component.crawler.spi.Closer;
import com.order.bachlinh.core.component.crawler.spi.Finder;
import com.order.bachlinh.core.component.crawler.spi.Navigator;
import com.order.bachlinh.core.component.crawler.spi.WebDriverHolder;
import com.order.bachlinh.core.component.crawler.Crawler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

public final class CrawlerProvider {

    private CrawlerProvider() {
        throw new UnsupportedOperationException("Can not instance CrawlerProvider");
    }

    public static Crawler usePhantomJS(String driverPath) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, driverPath);
        return usePhantomJS(capabilities);
    }

    public static Crawler useChromeDriver(String driverPath) {
        System.setProperty("webdriver.chrome.driver", driverPath);
        return new DefaultCrawler(new ChromeDriver());
    }

    public static Crawler useFirefoxDriver(String driverPath) {
        System.setProperty("webdriver.gecko.driver", driverPath);
        return new DefaultCrawler(new FirefoxDriver());
    }

    public static Crawler useEdgeDriver(String driverPath) {
        System.setProperty("webdriver.edge.driver", driverPath);
        return new DefaultCrawler(new EdgeDriver());
    }

    public static Crawler useSafariDriver(String driverPath) {
        System.setProperty("webdriver.safari.driver", driverPath);
        return new DefaultCrawler(new SafariDriver());
    }

    private static Crawler usePhantomJS(DesiredCapabilities capabilities) {
        return new DefaultCrawler(new PhantomJSDriver(capabilities));
    }

    private static class DefaultCrawler implements Crawler {

        private final Finder finder;
        private final Navigator navigator;
        private final Closer closer;

        DefaultCrawler(WebDriver webDriver) {
            WebDriverHolder holder = new SimpleWebDriverHolder(webDriver);
            this.finder = new DefaultFinder(holder);
            this.navigator = new DefaultNavigator(holder);
            this.closer = new DefaultCloser(holder);
        }

        @Override
        public void goTo(String url) {
            navigator.navigateTo(url);
        }

        @Override
        public void closeWindow() {
            closer.close();
        }

        @Override
        public void quit() {
            closer.quit();
        }

        @Override
        public Finder getFinder() {
            return finder;
        }
    }
}
